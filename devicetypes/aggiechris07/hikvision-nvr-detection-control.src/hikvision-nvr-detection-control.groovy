/**
 *  Hikvision NVR Detection Control
 * 	Device Handler
 *
 *	Discription:
 *	Create an virtual switch for hikvision camera/NVR that allows control of detection alarms
 *
 *	Author: aggiechris07
 *  Copyright 2017 Chris Johnson
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *  Last Updated : 7/31/2017
 *  version: 0.1*
 */
 
 //** Needed to be able to serialize back to XML
import groovy.xml.XmlUtil

metadata {

//* simulator metadata
	simulator {
	}
    
	definition (name: "Hikvision NVR Detection Control", namespace: "aggiechris07", author: "Chris Johnson") {
		capability "Actuator"
		capability "Switch"
		capability "Sensor"
	}

//* UI tile definitions
	tiles {
		standardTile("button", "device.switch", width: 2, height: 2, canChangeIcon: false) {
			state "off", label: 'Off', action: "switch.on", icon: "st.security.alarm.off", backgroundColor: "#ffffff", nextState: "on"
				state "on", label: 'On', action: "switch.off", icon: "st.security.alarm.on", backgroundColor: "#79b821", nextState: "off"
		}
		standardTile("offButton", "device.button", width: 1, height: 1, canChangeIcon: false) {
			state "default", label: 'Force Off', action: "switch.off", icon: "st.security.alarm.off", backgroundColor: "#ffffff"
		}
		standardTile("onButton", "device.switch", width: 1, height: 1, canChangeIcon: false) {
			state "default", label: 'Force On', action: "switch.on", icon: "st.security.alarm.on", backgroundColor: "#79b821"
		}
		main "button"
			details (["button","onButton","offButton"])
	}

//*Input information device settings
	preferences {
		section("Camera Information"){
			input "user_name", "text", title: "User Name", required: true
			input "password", "password", title: "Password", required: true
			input "camera_ip", "text", title: "IP address of NVR", required: true
			input "camera_port", "text", title: "Port of Camera, Normally 80", required: true
    	 	input "camera_number", "text", title: "Camera Number, number at the end of URL for each camera", required: true
    	    input "line_detection", "bool", title: "Change Line Detection", required: true
			input "intrusion_detection", "bool", title: "Change Intrusion Detection", required: true
		}
	}
}

//*actions to take when the switch is moved to the on position
def on() {
	log.debug "Executing ON" 
	sendEvent(name: "switch", value: "on") 
    log.debug "intrusion_detection ${intrusion_detection}"
    log.debug "line_detection ${line_detection}"
	pathSetAndHttpGet()
	log.debug "Executed ON"
}

//*actions to take when the switch is moved to the on position
def off() {
    log.debug "Executing Off" 
    sendEvent(name: "switch", value: "off") 
	pathSetAndHttpGet()
	log.debug "Executed Off"
}

//*test which detection type to change, and trigger httpget to test settings
def pathSetAndHttpGet(){
    if (intrusion_detection){
		def detectionPath = "/ISAPI/Smart/FieldDetection/${camera_number}" 
        log.debug "detectionPath ${detectionPath}"
		getCameraCurrentSettings(detectionPath)
    	}
	if (line_detection){
		def detectionPath = "/ISAPI/Smart/LineDetection/${camera_number}" 
        log.debug "detectionPath ${detectionPath}"
		getCameraCurrentSettings(detectionPath)
    	}   
	}
   
//*test which detection type to change, and send trigger httpput
def pathSetAndHttpPut(putXml){
    if (intrusion_detection){
		def detectionPath = "/ISAPI/Smart/FieldDetection/${camera_number}" 
        log.debug "detectionPath ${detectionPath}"
		sendXmlBack(putXml, detectionPath)
    	}
	if (line_detection){
		def detectionPath = "/ISAPI/Smart/LineDetection/${camera_number}" 
        log.debug "detectionPath ${detectionPath}"
		sendXmlBack(putXml, detectionPath)
    	}   
	}
    
def parse(description) {
	log.debug "Parsing"
    def msg = parseLanMessage(description)
//	log.debug "msg ${msg}"
    def bodyString = msg.body              // => request body as a string
	log.debug "bodyString ${bodyString}"
    def parsedbody = new XmlParser().parseText(bodyString)
//	log.debug "parsedbody before ${parsedbody}"
	def buttonTest = device.currentState("switch").getValue()
	log.debug "buttonTrst value def off() ${buttonTest}"
    def enabledtest = parsedbody.enabled.text()
    log.debug "before enabled setting ${enabledtest}" 
    if (parsedbody.statusCode.text() == "1"){
		log.debug "Sucess"
  		}
    if (device.currentState("switch").getValue() == "on" && parsedbody.enabled.text() == "false"){
		parsedbody.enabled[0].value = 'true'
    	enabledtest = parsedbody.enabled.text()
    	log.debug "after enabled setting ${enabledtest}"
 // 		log.debug "parsedbody after ${parsedbody}"
 		def outputXml = XmlUtil.serialize(parsedbody)
  	    log.debug "outputXml ${outputXml}"
		pathSetAndHttpPut(outputXml)  
		}
    if (device.currentState("switch").getValue() == "on" && parsedbody.enabled.text() == "true"){
    	sendEvent(name: "switch", value: "on") 
        log.debug "Already True"
    	}
    if (device.currentState("switch").getValue() == "off" && parsedbody.enabled.text() == "true"){
		parsedbody.enabled[0].value = 'false'
    	enabledtest = parsedbody.enabled.text()
    	log.debug "after enabled setting ${enabledtest}"
 // 		log.debug "parsedbody after ${parsedbody}"
 		def outputXml = XmlUtil.serialize(parsedbody)
		pathSetAndHttpPut(outputXml)
        }
	if (device.currentState("switch").getValue() == "off" && parsedbody.enabled.text() == "false"){
    	sendEvent(name: "switch", value: "off")
        log.debug "Already False"
    	}
	}
    

def getCameraCurrentSettings(getPath) {
//*Set Method type for HTTP request via physicalgraph.device.HubAction
    def method = "GET"    
    log.debug "The get method is $method"

//*set path for the camera feature to adjust based on hikvision API 
//*http://oversea-download.hikvision.com/uploadfile/Leaflet/ISAPI/HIKVISION%20ISAPI_2.0-IPMD%20Service.pdf
log.debug "get path is: $getPath"

//*Generate the BASIC authentication key string for HTTP request via physicalgraph.device.HubAction
//*Basic Access according to RFC 2617
    //def userpass = "${user_name}:${password}"
    def userpassascii = "${user_name}:${password}"
	def userpass = "Basic " + userpassascii.encodeAsBase64().toString()
    //log.debug "userpass: ${userpass}"

//*Assign new device ID based on the hex of the IP and port of camera
//*Believe this step is needed because of a quirk in HTTP request via physicalgraph.device.HubAction
//*Probably should set the camera/NVR IP to static to make sure that device id is unique and the http signals go to the right place
    def hosthex = convertIPtoHex(camera_ip).toUpperCase()
    def porthex = convertPortToHex(camera_port).toUpperCase()
    device.deviceNetworkId = "$hosthex:$porthex" 
    //log.debug "The device id configured is: $device.deviceNetworkId"

//*set headers for camera access based on IP and port inputs, and generated basic key
    def headers = [:] 
    headers.put("HOST", "$camera_ip:$camera_port")
    //headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
    headers.put("Authorization", userpass)
    log.debug "The get Header is $headers"

//*Send HTTP request
	try{
		//def result = new physicalgraph.device.HubAction(
      	sendHubCommand(new physicalgraph.device.HubAction(
			method: method,
			path: getPath,
			headers: headers,
            device.deviceNetworkId//,
            //[callback: cmdResponse]
            ))
		//log.debug result
    	//return result 
        //log.debug result
		  } catch (e) {
        	log.error "something went wrong with http GET: $e"
	    	}
    return method
    }
    
def sendXmlBack(xmlPutBack, putPath) {
	
//*Set Method type for HTTP request via physicalgraph.device.HubAction
    def method = "PUT"    
    log.debug "The put method is $method"
	log.debug "xml put back is ${xmlPutBack}"
//*set path for the camera feature to adjust based on hikvision API 
//*http://oversea-download.hikvision.com/uploadfile/Leaflet/ISAPI/HIKVISION%20ISAPI_2.0-IPMD%20Service.pdf
    log.debug "put path is: $path"

//*Generate the BASIC authentication key string for HTTP request via physicalgraph.device.HubAction
//*Basic Access according to RFC 2617
    //def userpass = "${user_name}:${password}"
    def userpassascii = "${user_name}:${password}"
	def userpass = "Basic " + userpassascii.encodeAsBase64().toString()
    //log.debug "userpass: ${userpass}"

//*Assign new device ID based on the hex of the IP and port of camera
//*Believe this step is needed because of a quirk in HTTP request via physicalgraph.device.HubAction
//*Probably should set the camera/NVR IP to static to make sure that device id is unique and the http signals go to the right place
    def hosthex = convertIPtoHex(camera_ip).toUpperCase()
    def porthex = convertPortToHex(camera_port).toUpperCase()
    device.deviceNetworkId = "$hosthex:$porthex" 
    //log.debug "The device id configured is: $device.deviceNetworkId"

//*set headers for camera access based on IP and port inputs, and generated basic key
    def headers = [:] 
    headers.put("HOST", "$camera_ip:$camera_port")
    //headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
    headers.put("Authorization", userpass)
    //log.debug "The Header is $headers"

//*Send HTTP request
	try{
		//def result = new physicalgraph.device.HubAction(
      	sendHubCommand(new physicalgraph.device.HubAction(
			method: method,
			path: putPath,
			headers: headers,
            body: xmlPutBack,
            device.deviceNetworkId//,
            //[callback: cmdResponse]
            ))
		//log.debug result
    	//return result 
        //log.debug result
		  } catch (e) {
        	log.error "something went wrong with http put: $e"
	    	}
	//result
	//log.debug result
    return method
}

//*convert IP to Hex, for device ID
private String convertIPtoHex(ipAddress) { 
    String hexIp = ipAddress.tokenize( '.' ).collect {  String.format( '%02x', it.toInteger() ) }.join()
    //log.debug "IP address entered is $ipAddress and the converted hex code is $hexIp"
    return hexIp
}

//*convert Port To Hex, for device ID
private String convertPortToHex(port) {
	String hexPort = port.toString().format( '%04x', port.toInteger() )
    //log.debug "Port entered is $port and the converted hex code is $hexPort"
    return hexPort
}
