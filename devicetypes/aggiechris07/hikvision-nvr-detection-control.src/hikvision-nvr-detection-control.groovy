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
    	capability "Polling"
    	capability "Refresh"

    attribute "switch", "string"
    attribute "switch2", "string"

    command "on"
    command "off"
    command "on2"
    command "off2"
	}

//* UI tile definitions
	tiles(scale: 2) {
		standardTile("button", "device.switch", width: 4, height: 4, canChangeIcon: false) {
			state "off", label: 'Off', action: "switch.on", icon: "st.security.alarm.off", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'On', action: "switch.off", icon: "st.security.alarm.on", backgroundColor: "#79b821", nextState: "off"
		}
		standardTile("offButton", "device.switch", width: 2, height: 2, canChangeIcon: false) {
			state "default", label: 'Force Off', action: "switch.off", icon: "st.security.alarm.off", backgroundColor: "#ffffff"
		}
		standardTile("onButton", "device.switch", width: 2, height: 2, canChangeIcon: false) {
			state "default", label: 'Force On', action: "switch.on", icon: "st.security.alarm.on", backgroundColor: "#79b821"
		}
        standardTile("switch2", "device.switch2", width: 1, height: 1, canChangeIcon: false) {
        	state "on", label: "switch2", action: "off2", icon: "st.switches.switch.on", backgroundColor: "#79b821"
        	state "off", label: "switch2", action: "on2", icon: "st.switches.switch.off", backgroundColor: "#ffffff"
        }
        standardTile("refresh", "device.switch",  width: 1, height: 1, inactiveLabel: false, decoration: "flat") {
                        state "default", label:"", action:"refresh.refresh", icon:"st.secondary.refresh"
        }
        standardTile("blank2", "blank2", inactiveLabel: false, width: 4, height: 1, decoration: "flat", wordWrap: true) {
			state "default", label: ''
        }
        for(int i = 1; i < 9; i++) {
            standardTile("cameraLabel${i}", "cameraLabel${i}", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
                state "default", label: "Camera ${i}" 
            }
            standardTile("cameraIntrusionText${i}", "device.cameraIntrusionText${i}", inactiveLabel: false, width: 1, height: 1, decoration: "flat", wordWrap: true) {
                state "default", label: 'Intrusion:' 
            }
            standardTile("cameraIntrusionStatus${i}", "device.cameraIntrusionStatus${i}", inactiveLabel: false, width: 1, height: 1, decoration: "flat", wordWrap: true) {
                state "default", label: 'N/A', backgroundColor:"#cccccc"
                state "OFF", label: 'OFF', icon: "st.security.alarm.clear", backgroundColor:"#ffffff"
                state "ON",  label: 'ON', icon: "st.security.alarm.alarm", backgroundColor:"#79b821"
                } 
            standardTile("cameraLineText${i}", "device.cameraLineText${i}", inactiveLabel: false, width: 1, height: 1, decoration: "flat", wordWrap: true) {
                state "default", label: ' \n Line: \n '
                }
            standardTile("cameraLineStatus${i}", "device.cameraLineStatus${i}", inactiveLabel: false, width: 1, height: 1, decoration: "flat", wordWrap: true) {
                state "default", label: 'N/A', backgroundColor:"#cccccc"
                state "OFF", label: 'OFF', icon: "st.motion.motion.active", backgroundColor:"#ffffff"
                state "ON",  label: 'ON', icon: "st.motion.motion.inactive", backgroundColor:"#79b821"
                } 
        }
	standardTile("blank1", "blank1", inactiveLabel: false, width: 2, height: 3, decoration: "flat", wordWrap: true) {
		state "default", label: ''
            	}
	}
       
		main "button"
			details (["button", "onButton","offButton",
            	"switch2", "refresh", "blank2",
                "cameraLabel1", "cameraLabel2", "cameraLabel3",
                "cameraIntrusionText1", "cameraIntrusionStatus1", "cameraIntrusionText2", "cameraIntrusionStatus2", "cameraIntrusionText3", "cameraIntrusionStatus3",
                "cameraLineText1", "cameraLineStatus1", "cameraLineText2", "cameraLineStatus2", "cameraLineText3", "cameraLineStatus3",
                "cameraLabel4", "cameraLabel5", "cameraLabel6",
                "cameraIntrusionText4", "cameraIntrusionStatus4", "cameraIntrusionText5", "cameraIntrusionStatus5", "cameraIntrusionText6", "cameraIntrusionStatus6",
                "cameraLineText4", "cameraLineStatus4", "cameraLineText5", "cameraLineStatus5", "cameraLineText6", "cameraLineStatus6",
                "cameraLabel7", "cameraLabel8",
                "blank1",
                "cameraIntrusionText7", "cameraIntrusionStatus7", "cameraIntrusionText8", "cameraIntrusionStatus8",
                "cameraLineText7", "cameraLineStatus7", "cameraLineText8", "cameraLineStatus8"
                ])

//*Input information device settings
	preferences {
		section("NVR Information"){
			input "user_name", "text", title: "User Name", required: true
			input "password", "password", title: "Password", required: true
			input "nvr_ip", "text", title: "IP address of NVR", required: true
			input "nvr_port", "text", title: "Port of NVR, Normally 80", required: true
			}           
        for(int i = 1; i < 9; i++) {
        section("Camera ${i}"){
            input "camera_number_${i}", "text", title: "Camera ${i} Number. NA to skip", required: true
            input "Intrusion_detection_${i}", "bool", title: "Camera ${i} Intrusion Detection", required: true
            input "line_detection_${i}", "bool", title: "Camera ${i} Line Detection", required: true
            }
        }
 	}
}

//*actions to take when the switch is moved to the on position
def on() {
	log.debug "Executing ON" 
	sendEvent(name: "switch", value: "on") 
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

def refresh() {
  log.debug "refresh"
  pathSetAndHttpGet()
}

def on2() {
    log.debug "on2"
    sendEvent(name: "switch2", value: "on") 
    def detectionPath = "/ISAPI/Event/triggers/fielddetection-4/notifications" 
    log.debug "detectionPath ${detectionPath}"
    getCameraCurrentSettings(detectionPath)
}

def off2() {
    log.debug "off2"
    sendEvent(name: "switch2", value: "off") 
    def detectionPath = "/ISAPI/Event/triggers/fielddetection-4/notifications" 
    log.debug "detectionPath ${detectionPath}"
    getCameraCurrentSettings(detectionPath)
}
def cameraNumberArray(){
	def cameraNumber = ["$camera_number_1", "$camera_number_2", "$camera_number_3", "$camera_number_4", "$camera_number_5", "$camera_number_6", "$camera_number_7", "$camera_number_8"] as String[]
	return cameraNumber
    }
    
def intrusionDetectionArray(){     
    def intrusionDetection = ["$Intrusion_detection_1", "$Intrusion_detection_2", "$Intrusion_detection_3", "$Intrusion_detection_4", "$Intrusion_detection_5", "$Intrusion_detection_6", "$Intrusion_detection_7", "$Intrusion_detection_8"] as String[]
	return intrusionDetection
    }
    
def lineDetectionArray(){
	def lineDetection= ["$line_detection_1", "$line_detection_2", "$line_detection_3", "$line_detection_4", "$line_detection_5", "$line_detection_6", "$line_detection_7", "$line_detection_8"] as String[]
	return lineDetection
    }

//*test which detection type to change, and trigger httpget to test settings
def pathSetAndHttpGet(){
	def cameraNumber = cameraNumberArray()
	def intrusionDetection = intrusionDetectionArray()
	def lineDetection = lineDetectionArray()
	log.debug "cameraNumber:${cameraNumber} intrusionDetection:${intrusionDetection} lineDetection:${lineDetection}"
	for(int i = 0; i < 8; i++) {
    	if (cameraNumber[i] == "NA"){
        sendEvent(name: "cameraIntrusionStatus${i+1}", value: "default")
        sendEvent(name: "cameraIntrusionStatus${i+1}", value: "default")
        sendEvent(name: "cameraLineStatus${i+1}", value: "default")
        }
        if (cameraNumber[i] != "NA"){
           log.debug "cameraNumber[${i+1}]:${cameraNumber[i]} intrusionDetection[${i+1}]:${intrusionDetection[i]} lineDetection[${i+1}]:${lineDetection[i]}"
           if (intrusionDetection[i] == "true"){
                def detectionPath = "/ISAPI/Smart/FieldDetection/${cameraNumber[i]}" 
                log.debug "detectionPath ${detectionPath}"
                getCameraCurrentSettings(detectionPath)
                }
           if (lineDetection[i] == "true"){
               def detectionPath = "/ISAPI/Smart/LineDetection/${cameraNumber[i]}" 
               log.debug "detectionPath ${detectionPath}"
               getCameraCurrentSettings(detectionPath)
            }   
        }
    }
}
    
def parse(description) {
	log.debug "Parsing"
    def msg = parseLanMessage(description)
    def bodyString = msg.body              // => request body as a string
	log.debug "bodyString ${bodyString}"
    def parsedbody = new XmlParser().parseText(bodyString)
	def buttonTest = device.currentState("switch").getValue()
	log.debug "button state during parse is: ${buttonTest}"
    def enabledtest = parsedbody.enabled.text()
    log.debug "before enabled setting ${enabledtest}"
    setCameraTiles(parsedbody)
    //removeEmailNotification(bodyString)
   // addEmailNotification(bodyString)
    if (parsedbody.statusCode.text() == "1"){
		getCameraCurrentSettings(parsedbody.requestURL.text())
        log.debug "Success"
    }
    if (device.currentState("switch").getValue() == "on" && parsedbody.enabled.text() == "false"){
		parsedbody.enabled[0].value = 'true'
    	enabledtest = parsedbody.enabled.text()
        log.debug "after enabled setting ${enabledtest}"
 		def outputXml = XmlUtil.serialize(parsedbody)
		def parsedPutPath = parsedSetPath(parsedbody)
		sendXmlBack(outputXml, parsedPutPath)  
	}
    if (device.currentState("switch").getValue() == "on" && parsedbody.enabled.text() == "true"){
    	sendEvent(name: "switch", value: "on") 
        log.debug "Already True"
    }
    if (device.currentState("switch").getValue() == "off" && parsedbody.enabled.text() == "true"){
		parsedbody.enabled[0].value = 'false'
    	enabledtest = parsedbody.enabled.text()
    	log.debug "after enabled setting ${enabledtest}"
 		def outputXml = XmlUtil.serialize(parsedbody)
		def parsedPutPath = parsedSetPath(parsedbody)
        log.debug "parsedPutPath:$parsedPutPath"
		sendXmlBack(outputXml, parsedPutPath)  
	}
	if (device.currentState("switch").getValue() == "off" && parsedbody.enabled.text() == "false"){
    	sendEvent(name: "switch", value: "off")
        log.debug "Already False"
    }
}
    

def getCameraCurrentSettings(getPath) {
    def method = "GET"    
	log.debug "http $method to $getPath executing"
	def getHeaders = setHeaderForHttp()
	try{
    	sendHubCommand(new physicalgraph.device.HubAction(
			method: method,
			path: getPath,		//*set path based on hikvision API http://oversea-download.hikvision.com/uploadfile/Leaflet/ISAPI/HIKVISION%20ISAPI_2.0-IPMD%20Service.pdf
			headers: getHeaders,
            device.deviceNetworkId
            ))
     } catch (e) {
        	log.error "something went wrong with http GET: $e"
	 }
}
    
def sendXmlBack(xmlPutBack, putPath) {
    def method = "PUT"    
	log.debug "http $method to $putPath executing with XML: $xmlPutBack"
    def putHeaders = setHeaderForHttp()
	try{
      	sendHubCommand(new physicalgraph.device.HubAction(
			method: method,
			path: putPath,		//*set path based on hikvision API http://oversea-download.hikvision.com/uploadfile/Leaflet/ISAPI/HIKVISION%20ISAPI_2.0-IPMD%20Service.pdf
			headers: putHeaders,
            body: xmlPutBack,
            device.deviceNetworkId
            ))
        } catch (e) {
        	log.error "something went wrong with http put: $e"
	    }
}

private String convertIPtoHex(ipAddress) { 
    String hexIp = ipAddress.tokenize( '.' ).collect {  String.format( '%02x', it.toInteger() ) }.join()
    return hexIp
}

private String convertPortToHex(port) {
	String hexPort = port.toString().format( '%04x', port.toInteger() )
    return hexPort
}

private setHeaderForHttp(){
    def userpassascii = "${user_name}:${password}"
	def userpass = "Basic " + userpassascii.encodeAsBase64().toString()
    def hosthex = convertIPtoHex(nvr_ip).toUpperCase()
    def porthex = convertPortToHex(nvr_port).toUpperCase()
    if (device.deviceNetworkId != "$hosthex:$porthex"){
    device.deviceNetworkId = "$hosthex:$porthex"
    log.debug "The device id configured is: $device.deviceNetworkId"
    }
    def headers = [:] 
    headers.put("HOST", "$nvr_ip:$nvr_port")
    headers.put("Authorization", userpass)
    return headers
}

def parsedSetPath(testParsedBody){
	 def parsedCameraNumber = testParsedBody.id.text()
     def parsedFieldIdNumber = testParsedBody.FieldDetectionRegionList.FieldDetectionRegion.id.text()
     def parsedLineIdNumber = testParsedBody.LineItemList.LineItem.id.text()
     log.debug "parsedCameraNumber:${parsedCameraNumber} parsedFieldIdNumber:${parsedFieldIdNumber} parsedLineIdNumber:${parsedLineIdNumber}"
	if (parsedFieldIdNumber){
    log.debug "parsedFieldIdNumber if loop activated"
		def parseDetectionPath = "/ISAPI/Smart/FieldDetection/${parsedCameraNumber}"
        log.debug "parseDetectionPath:$parseDetectionPath"
        return parseDetectionPath
	}
	if (parsedLineIdNumber){
        log.debug "parsedLineIdNumber if loop activated"
		def parseDetectionPath = "/ISAPI/Smart/LineDetection/${parsedCameraNumber}"
        log.debug "parseDetectionPath:$parseDetectionPath"
        return parseDetectionPath
	}
}
    
def setCameraTiles(tileParsedBody){
	def cameraNumber = cameraNumberArray()
	def intrusionDetection = intrusionDetectionArray()
	def lineDetection = lineDetectionArray()
	def tileCameraNumber = tileParsedBody.id.text()
	def tileFieldIdNumber = tileParsedBody.FieldDetectionRegionList.FieldDetectionRegion.id.text()
	def tileLineIdNumber = tileParsedBody.LineItemList.LineItem.id.text()
    def tileEnabled = tileParsedBody.enabled.text()
	log.debug "tileCameraNumber:${tileCameraNumber} tileieldIdNumber:${tileFieldIdNumber} tileLineIdNumber:${tileLineIdNumber}"
	for(int i = 0; i < 8; i++) {
    	if (cameraNumber[i] == tileCameraNumber){
    	   	if (tileFieldIdNumber){
    			log.debug "tileFieldIdNumber if loop activated"
                if ( tileEnabled == "true"){
					sendEvent(name: "cameraIntrusionStatus${i+1}", value: "ON")
                }
				if ( tileEnabled == "false"){
					sendEvent(name: "cameraIntrusionStatus${i+1}", value: "OFF")
                }
			}
			if (tileLineIdNumber){
        		log.debug "tileLineIdNumber if loop activated"
                if ( tileEnabled == "true"){
					sendEvent(name: "cameraLineStatus${i+1}", value: "ON")
                }
				if ( tileEnabled == "false"){
					sendEvent(name: "cameraLineStatus${i+1}", value: "OFF")
                }
			}
        }
    }
}

def removeEmailNotification(xmlString){
    def xmlTestString = """<EventTriggerNotification>
<id>email</id>
<notificationMethod>email</notificationMethod>
</EventTriggerNotification>
"""
    if (xmlString.contains(xmlTestString)) {
    log.debug "found the string"
	def xmlReturnString = xmlString.replace(xmlTestString,"")
    log.debug "xmlReturnString ${xmlReturnString}"
	}
}

def addEmailNotification(xmlString){
    def xmlTestString = """<EventTriggerNotification>
<id>email</id>
<notificationMethod>email</notificationMethod>
</EventTriggerNotification>
"""
	def xmlTargetString = """</EventTriggerNotificationList>"""
    def xmlReplaceString = """<EventTriggerNotification>
<id>email</id>
<notificationMethod>email</notificationMethod>
</EventTriggerNotification>
</EventTriggerNotificationList>
"""
    if (xmlString.contains(xmlTestString)) {
    log.debug "email was already found"
	} else {	
    def xmlReturnString = xmlString.replace(xmlTargetString,xmlReplaceString)
    log.debug "email was added"
    log.debug "xmlReturnString ${xmlReturnString}"
    }
    
}
