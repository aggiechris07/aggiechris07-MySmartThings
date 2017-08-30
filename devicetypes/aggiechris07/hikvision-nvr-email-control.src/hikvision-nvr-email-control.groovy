/**
 *  Hikvision NVR Email Control
 * 	Device Handler
 *
 *	Discription:
 *	Create an virtual switch for hikvision camera/NVR that allows control of email notifications
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

	simulator {
	}
    
    definition (name: "Hikvision NVR Email Control", namespace: "aggiechris07", author: "Chris Johnson") {
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

	tiles(scale: 2) {
		standardTile("button", "device.switch", width: 4, height: 4, canChangeIcon: false) {
			state "off", label: 'In Off', action: "switch.on", icon: "st.security.alarm.off", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'In On', action: "switch.off", icon: "st.security.alarm.on", backgroundColor: "#79b821", nextState: "off"
		}
		standardTile("offButton", "device.switch", width: 2, height: 2, canChangeIcon: false) {
			state "default", label: 'Force InOff', action: "switch.off", icon: "st.security.alarm.off", backgroundColor: "#ffffff"
		}
		standardTile("onButton", "device.switch", width: 2, height: 2, canChangeIcon: false) {
			state "default", label: 'Force InOn', action: "switch.on", icon: "st.security.alarm.on", backgroundColor: "#79b821"
		}
        standardTile("switch2", "device.switch2", width: 4, height: 4, canChangeIcon: false) {
        	state "off", label: "Out Off", action: "on2", icon: "st.switches.switch.off", backgroundColor: "#ffffff", nextState: "on"
            state "on", label: "Out On", action: "off2", icon: "st.switches.switch.on", backgroundColor: "#79b821", nextState: "off"
        }
        standardTile("offswitch2", "device.switch", width: 2, height: 2, canChangeIcon: false) {
			state "default", label: 'Force OutOff', action: "off2", icon: "st.security.alarm.off", backgroundColor: "#ffffff"
		}
		standardTile("onswitch2", "device.switch", width: 2, height: 2, canChangeIcon: false) {
			state "default", label: 'Force OutOn', action: "on2", icon: "st.security.alarm.on", backgroundColor: "#79b821"
		}
/*      standardTile("refresh", "device.switch",  width: 2, height: 2, inactiveLabel: false, decoration: "flat") {
            state "default", label:"", action:"refresh.refresh", icon:"st.secondary.refresh"
        }
        standardTile("blank2", "blank2", inactiveLabel: false, width: 2, height: 2, decoration: "flat", wordWrap: true) {
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
                state "default", label: 'Line:'
                }
            standardTile("cameraLineStatus${i}", "device.cameraLineStatus${i}", inactiveLabel: false, width: 1, height: 1, decoration: "flat", wordWrap: true) {
                state "default", label: 'N/A', backgroundColor:"#cccccc"
                state "OFF", label: 'OFF', icon: "st.motion.motion.active", backgroundColor:"#ffffff"
                state "ON",  label: 'ON', icon: "st.motion.motion.inactive", backgroundColor:"#79b821"
                } 
        }
		standardTile("blank1", "blank1", inactiveLabel: false, width: 2, height: 3, decoration: "flat", wordWrap: true) {
			state "default", label: ''
            }*/
	}

    main "button"
		details (["button", "onButton","offButton","switch2","onswitch2","offswitch2",
	        "blank2", "refresh", "blank2"/*,
            "cameraLabel1", "cameraLabel2", "cameraLabel3",
            "cameraIntrusionText1", "cameraIntrusionStatus1", "cameraIntrusionText2", "cameraIntrusionStatus2", "cameraIntrusionText3", "cameraIntrusionStatus3",
            "cameraLineText1", "cameraLineStatus1", "cameraLineText2", "cameraLineStatus2", "cameraLineText3", "cameraLineStatus3",
            "cameraLabel4", "cameraLabel5", "cameraLabel6",
            "cameraIntrusionText4", "cameraIntrusionStatus4", "cameraIntrusionText5", "cameraIntrusionStatus5", "cameraIntrusionText6", "cameraIntrusionStatus6",
            "cameraLineText4", "cameraLineStatus4", "cameraLineText5", "cameraLineStatus5", "cameraLineText6", "cameraLineStatus6",
            "cameraLabel7", "cameraLabel8",
            "blank1",
            "cameraIntrusionText7", "cameraIntrusionStatus7", "cameraIntrusionText8", "cameraIntrusionStatus8",
            "cameraLineText7", "cameraLineStatus7", "cameraLineText8", "cameraLineStatus8"*/
            ])

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

def xmlEmailString(){
    def xmlTestString = """<EventTriggerNotification>
<id>email</id>
<notificationMethod>email</notificationMethod>
</EventTriggerNotification>
"""
	return xmlTestString
    }

def xmlEndString(){    
   	def xmlTargetString = """</EventTriggerNotificationList>
</EventTrigger>"""
	return xmlTargetString
    }

def fieldDetectionURL(){
	def URLString = "/ISAPI/Event/triggers/fielddetection-"
    return URLString
}

def lineDetectionURL(){
	def URLString = "/ISAPI/Event/triggers/linedetection-"
    return URLString
}

def xmlCenterString(){
	def xmlTestStringTwo = """<EventTriggerNotification>
<id>center</id>
<notificationMethod>center</notificationMethod>
</EventTriggerNotification>
"""
	return xmlTestStringTwo
}

def on() {
        sendEvent(name: "switch", value: "on") 
        pathSetAndHttpGet()
        log.debug "Executed ON"
}

def off() {
        sendEvent(name: "switch", value: "off") 
        pathSetAndHttpGet()
        log.debug "Executed Off"
}

def on2() {
	sendEvent(name: "switch2", value: "on") 
	pathSetAndHttpGetOutside()
	log.debug "Executed ON2"
}

def off2() {
    sendEvent(name: "switch2", value: "off") 
	pathSetAndHttpGetOutside()
	log.debug "Executed Off2"
}

/*def refresh() {
	pathSetAndHttpGetOutside()
    pathSetAndHttpGet()
    for(int i = 0; i < 3; i++) {
		if (device.currentValue("cameraIntrusionStatus${i+1}") == "ON" && device.currentValue("cameraIntrusionStatus${i+1}") != "default"){
        	sendEvent(name: "switch2", value: "on2")
            break
		} else {
        	sendEvent(name: "switch2", value: "off2")
        }
		if (device.currentValue("cameraLineStatus${i+1}") == "ON" && device.currentValue("cameraLineStatus${i+1}") != "default"){
			sendEvent(name: "switch2", value: "on2")
            break
		} else {
        	sendEvent(name: "switch2", value: "off2")
        }
	}
	for(int i = 3; i < 8; i++) {
		if (device.currentValue("cameraIntrusionStatus${i+1}") == "ON" && device.currentValue("cameraIntrusionStatus${i+1}") != "default"){
			sendEvent(name: "switch", value: "on")
			break
		} else {
            sendEvent(name: "switch", value: "off")
        }
		if (device.currentValue("cameraLineStatus${i+1}") == "ON" && device.currentValue("cameraLineStatus${i+1}") != "default"){
			sendEvent(name: "switch", value: "on")
			break
			} else {
            sendEvent(name: "switch", value: "off")
        }
	}
}
*/

def parse(description) {
	log.debug "Parsing"
    def msg = parseLanMessage(description)
    def bodyString = msg.body
	log.debug "parse: bodyString ${bodyString}"
    def parsedbody = new XmlParser().parseText(bodyString)
	def insideSwitchTest = device.currentValue("switch")
	log.debug "parse: insideSwitch is: ${insideSwitchTest}"
    def outsideSwitchTest = device.currentValue("switch2")
	log.debug "parse: outsideSwitch is: ${outsideSwitchTest}"
    if ((device.currentValue("switch") == "on" || device.currentValue("switch2") == "on") && (parsedbody.eventType.text() == "fielddetection" || parsedbody.eventType.text() == "linedetection")){
        def parsedPutPath = parsedSetPath(parsedbody)
        def outputXml = addEmailNotification(bodyString,parsedbody)
        if (outputXml){
            log.debug "when switch is on or on2 outputXml ${outputXml}"
            log.debug "when switch is on or on2 parsedPutPath is ${parsedPutPath}"
            /*def xmlHasBeenSent = */sendXmlBack(outputXml, parsedPutPath)
            } else {
            log.debug "there was a null in the add if during parse"
            }
    }
    if ((device.currentValue("switch") == "off" || device.currentValue("switch2") == "off") && (parsedbody.eventType.text() == "fielddetection" || parsedbody.eventType.text() == "linedetection")){
        def parsedPutPath = parsedSetPath(parsedbody)
        def outputXml = removeEmailNotification(bodyString, parsedbody)
        if (outputXml){
            log.debug "when switch is off or off2 outputXml ${outputXml}"
            log.debug "when switch is off or off2 parsedPutPath is ${parsedPutPath}"
            /*def xmlHasBeenSent = */sendXmlBack(outputXml, parsedPutPath)
            } else {
            log.debug "there was a null in the remove if during parse"
        }
    }
    if (parsedbody.statusCode.text() == "1"){
		//getCameraCurrentSettings(parsedbody.requestURL.text())
        def parsedBodyRequestURL = parsedbody.requestURL.text()
        log.debug "put of ${parsedbody.requestURL.text()} Success"
        //sendNotificationEvent("A put to ${parsedBodyRequestURL} succeded. Out is ${device.currentValue("switch2")} and In is ${device.currentValue("switch")}")
    } 
    if (parsedbody.statusCode.text() == "2" ||  parsedbody.statusCode.text() == "3" || parsedbody.statusCode.text() == "4" || parsedbody.statusCode.text() == "5" || parsedbody.statusCode.text() == "6" || parsedbody.statusCode.text() == "7" ){
    	log.debug "put of ${parsedbody.requestURL.text()} has failed"
        def parsedBodyRequestURL = parsedbody.requestURL.text()
        //sendNotificationEvent("A put to ${parsedBodyRequestURL} has failed ")
    }
 /*   if (xmlHasBeenSent){
    	setCameraTiles(parsedbody,xmlHasBeenSent)
    	} else { 
        setCameraTiles(parsedbody,bodyString)
    }*/
}

//*test which detection type to change, and trigger httpget to test settings
def pathSetAndHttpGet(){
	def cameraNumber = cameraNumberArray()
	def intrusionDetection = intrusionDetectionArray()
	def lineDetection = lineDetectionArray()
    def fieldURL = fieldDetectionURL()
    def lineURL = lineDetectionURL()
	log.debug "pathSetAndHttpGet: cameraNumber is ${cameraNumber}; intrusionDetection is ${intrusionDetection}; lineDetection is ${lineDetection}"
	for(int i = 3; i < 8; i++) {
    	if (cameraNumber[i] == "NA"){
        sendEvent(name: "cameraIntrusionStatus${i+1}", value: "default")
        sendEvent(name: "cameraIntrusionStatus${i+1}", value: "default")
        sendEvent(name: "cameraLineStatus${i+1}", value: "default")
        }
        if (cameraNumber[i] != "NA"){
           log.debug "pathSetAndHttpGet: during the loop that actually sends the get cameraNumber[${i+1}]is ${cameraNumber[i]}; intrusionDetection[${i+1}] is ${intrusionDetection[i]}; lineDetection[${i+1}] is ${lineDetection[i]}"
           if (intrusionDetection[i] == "true"){
                def detectionPath = "${fieldURL}${cameraNumber[i]}/" 
                log.debug "pathSetAndHttpGet: detectionPath ${detectionPath}"
                getCameraCurrentSettings(detectionPath)
                }
           if (lineDetection[i] == "true"){
               def detectionPath = "${lineURL}${cameraNumber[i]}/" 
               log.debug "pathSetAndHttpGet: detectionPath ${detectionPath}"
               getCameraCurrentSettings(detectionPath)
            }   
        }
    }
}

def pathSetAndHttpGetOutside(){
	def cameraNumber = cameraNumberArray()
	def intrusionDetection = intrusionDetectionArray()
	def lineDetection = lineDetectionArray()
    def fieldURL = fieldDetectionURL()
    def lineURL = lineDetectionURL()
	log.debug "pathSetAndHttpGetOutside: cameraNumber is ${cameraNumber}; intrusionDetection is ${intrusionDetection}; lineDetection is ${lineDetection}"
	for(int i = 0; i < 3; i++) {
    	if (cameraNumber[i] == "NA"){
        sendEvent(name: "cameraIntrusionStatus${i+1}", value: "default")
        sendEvent(name: "cameraIntrusionStatus${i+1}", value: "default")
        sendEvent(name: "cameraLineStatus${i+1}", value: "default")
        }
        if (cameraNumber[i] != "NA"){
           log.debug "pathSetAndHttpGetOutside: cameraNumber[${i+1}] is ${cameraNumber[i]}; intrusionDetection[${i+1}] is ${intrusionDetection[i]}; lineDetection[${i+1}] is ${lineDetection[i]}"
           if (intrusionDetection[i] == "true"){
                def detectionPath = "${fieldURL}${cameraNumber[i]}/" 
                log.debug "pathSetAndHttpGetOutside: detectionPath ${detectionPath}"
                getCameraCurrentSettings(detectionPath)
                }
           if (lineDetection[i] == "true"){
               def detectionPath = "${lineURL}${cameraNumber[i]}/" 
               log.debug "pathSetAndHttpGetOutside: detectionPath ${detectionPath}"
               getCameraCurrentSettings(detectionPath)
            }   
        }
    }
} 

def getCameraCurrentSettings(getPath) {
    def method = "GET"    
	log.debug "getCameraCurrentSettings: http ${method} to ${getPath} executing"
	def getHeaders = setHeaderForHttp()
	try{
    	sendHubCommand(new physicalgraph.device.HubAction(
			method: method,
			path: getPath,		//*set path based on hikvision API http://oversea-download.hikvision.com/uploadfile/Leaflet/ISAPI/HIKVISION%20ISAPI_2.0-IPMD%20Service.pdf
			headers: getHeaders,
            device.deviceNetworkId
            ))
     	} catch (e) {
        	log.error "getCameraCurrentSettings: something went wrong with http GET: ${e}"
	 }
}
    
def sendXmlBack(xmlPutBack, putPath) {
	if (xmlPutBack && putPath){
        def method = "PUT"    
        log.debug "sendXmlBack: http ${method} to ${putPath} executing with XML: ${xmlPutBack}"
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
                log.error "sendXmlBack: something went wrong with http put: ${e}"
        }
    return xmlPutBack
    } else {
    log.debug "sendXmlBack: didn't work because one of these is null: putpath = ${putPath} the xml = ${xmlPutBack}"
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
    log.debug "userpass ${userpass}"
    def hosthex = convertIPtoHex(nvr_ip).toUpperCase()
    def porthex = convertPortToHex(nvr_port).toUpperCase()
    if (device.deviceNetworkId != "${hosthex}:${porthex}"){
    device.deviceNetworkId = "${hosthex}:${porthex}"
    }
    def headers = [:] 
    headers.put("HOST", "${nvr_ip}:${nvr_port}")
    headers.put("Authorization", userpass)
    return headers
}

def parsedSetPath(testParsedBody){
	def parsedEventType = testParsedBody.eventType.text()
    def parsedCameraNumber = testParsedBody.videoInputChannelID.text()
    def fieldURL = fieldDetectionURL()
    def lineURL = lineDetectionURL()
    if (parsedEventType == "fielddetection"){
		def parseDetectionPath = "${fieldURL}${parsedCameraNumber}/notifications"
		return parseDetectionPath
	}
	if (parsedEventType == "linedetection"){
		def parseDetectionPath = "${lineURL}${parsedCameraNumber}/notifications" 
		return parseDetectionPath
	}
}
/*    
def setCameraTiles(tileParsedBody,tilebody){
	def cameraNumber = cameraNumberArray()
	def intrusionDetection = intrusionDetectionArray()
	def lineDetection = lineDetectionArray()
	def tileCameraNumber = tileParsedBody.videoInputChannelID.text()
	def tileEventType = tileParsedBody.eventType.text()
    def xmlTestString = xmlEmailString()
	log.debug "setCameraTiles: tileCameraNumber is ${tileCameraNumber}; tileEventType is ${tileEventType}"
	for(int i = 0; i < 8; i++) {
    	if (cameraNumber[i] == tileCameraNumber){
    	   	if (tileEventType == "fielddetection"){
    			log.debug "setCameraTiles: tileEventType = fielddetection if loop activated"
               if (tilebody.contains(xmlTestString)){
					sendEvent(name: "cameraIntrusionStatus${i+1}", value: "ON")
                	} else {
					sendEvent(name: "cameraIntrusionStatus${i+1}", value: "OFF")
                	}
			}
			if (tileEventType == "linedetection"){
        		log.debug "setCameraTiles: tileEventType = linedetection if loop activated"
                if (tilebody.contains(xmlTestString)){
					sendEvent(name: "cameraLineStatus${i+1}", value: "ON")
                } else {
					sendEvent(name: "cameraLineStatus${i+1}", value: "OFF")
                }
			}
        }
    }
}
*/
def removeEmailNotification(xmlString,xmlParsedString){
	def cameraNumber = cameraNumberArray()
    def xmlCameraNumber = xmlParsedString.videoInputChannelID.text()
	def xmlEventType = xmlParsedString.eventType.text()
    def xmlTestString = xmlEmailString()
	for(int i = 0; i < 8; i++) {
        log.debug "removeEmailNotification: cameraNumber[i] ${cameraNumber[i]} i is ${i} outside is ${device.currentValue("switch2")} inside is ${device.currentValue("switch")}"
    	if (cameraNumber[i] == xmlCameraNumber && ((i<3 && device.currentValue("switch2") == "off") || (i>2 && device.currentValue("switch") == "off"))){
        	if (xmlString.contains(xmlTestString)) {
                def xmlReturnString = xmlString.replace(xmlTestString,"")
                def xmlFinalString = splitXmlString(xmlReturnString)
                log.debug "removeEmailNotification: email was removed, here is the xmlFinalString ${xmlFinalString}"
                return xmlFinalString
        		} else {
                log.debug "Email was already removed"
            }
	    }
	}
}

def addEmailNotification(xmlString, xmlParsedString){
    def cameraNumber = cameraNumberArray()
    def xmlCameraNumber = xmlParsedString.videoInputChannelID.text()
    def xmlTestString = xmlEmailString()
	def xmlTargetString = xmlEndString()
    def xmlReplaceString = "${xmlTestString}${xmlTargetString}"
	def xmlTestStringTwo = xmlCenterString()
	def xmlTargetStringTwo = xmlCenterString()
	def xmlReplaceStringTwo =  "${xmlTestString}${xmlTestStringTwo}"
	for(int i = 0; i < 8; i++) {
    log.debug "addEmailNotification: cameraNumber[i] ${cameraNumber[i]} i is ${i} outside is ${device.currentValue("switch2")} inside is ${device.currentValue("switch")}"
		if (cameraNumber[i] == xmlCameraNumber && ((i<3 && device.currentValue("switch2") == "on") || (i>2 && device.currentValue("switch") == "on"))){    
            if (xmlString.contains(xmlTestString)) {
                log.debug "addEmailNotification: email was already found"
                } else {	
                if (xmlString.contains(xmlTestStringTwo)) {
                    def xmlReturnString = xmlString.replace(xmlTargetStringTwo,xmlReplaceStringTwo)
                    def xmlFinalString = splitXmlString(xmlReturnString)
                    log.debug "addEmailNotification: email was added over the Center with xmlFinalString  ${xmlFinalString}"
                    return xmlFinalString
                    } else { 
                    def xmlReturnString = xmlString.replace(xmlTargetString,xmlReplaceString)
                    def xmlFinalString = splitXmlString(xmlReturnString)
                    log.debug "addEmailNotification: email was added to end xmlFinalString  ${xmlFinalString}"
                    return xmlFinalString
                }
        	}
		} 
	}
}

def splitXmlString(xmlToSplit){
	def targetLastLine = "</EventTrigger>"
    def removeLast = xmlToSplit.replace(targetLastLine,"")
    def splitResult = removeLast.split('<EventTriggerNotificationList')
    def beginOfString = """<?xml version="1.0" encoding="UTF-8" ?>
<EventTriggerNotificationList"""
	def stringToReturn = "${beginOfString}${splitResult[1]}"
    def trimmedStringToReturn = stringToReturn.trim()
    log.debug "splitXmlString: trimmedStringToReturn is ${trimmedStringToReturn}"
	return trimmedStringToReturn
    }