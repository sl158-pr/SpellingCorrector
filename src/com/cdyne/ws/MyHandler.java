package com.cdyne.ws;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Set;

import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;


/*****************************************************************************************************************
** File       : MyHandler.java 
** Desc       : Helps in displaying/Logging the SOAP request and response in XML format
*               
** Author     : Sumanth Lakshminarayana
**Student ID  : 1000830230
** Date       : 10/24/12   
******************************************************************************************************************
**                              Change History                                                
******************************************************************************************************************
**  Version:        Date:			    Author:		               Description:
**  --------        --------            ---------             ----------------------------------
**    0.0           11/24/12       Sumanth Lakshminarayana           Created.
*****************************************************************************************************************/
public class MyHandler implements SOAPHandler<SOAPMessageContext> {

	public boolean handleMessage(SOAPMessageContext smc) {

		Boolean outboundProperty = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);// To check whether the request/response exists 

		SOAPMessage message = smc.getMessage(); // Gets the SOAP request/response message in SOAP format

		if (!outboundProperty.booleanValue()) {
			Client.SOAPMonitorWindow.append("SOAP Response : ");
		} else {
			Client.SOAPMonitorWindow.append("SOAP Request : ");
		}

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			message.writeTo(baos);
			//Logs the SOAP request/response to SOAP MONITOR window in XML format
			Client.SOAPMonitorWindow.append(prettyFormat(new String(baos.toByteArray())+"\n"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Client.SOAPMonitorWindow.append("\n");
		return outboundProperty;
	}

	public Set getHeaders() {
		return null;
	}

	public boolean handleFault(SOAPMessageContext context) {
		return true;
	}

	public void close(MessageContext context) {}
	
	/**
	 * Method that converts RAW SOAP message to XML(USER readable) format
	 
	 */
	
	public static String prettyFormat(String input, int indent) {
        try
        {
            Source xmlInput = new StreamSource(new StringReader(input));
            StringWriter stringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult(stringWriter);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            // This statement works with JDK 6
            transformerFactory.setAttribute("indent-number", indent);
             
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString();
        }
        catch (Throwable e)
        {
            // You'll come here if you are using JDK 1.5
            // you are getting an the following exeption
            // java.lang.IllegalArgumentException: Not supported: indent-number
            // Use this code (Set the output property in transformer.
            try
            {
                Source xmlInput = new StreamSource(new StringReader(input));
                StringWriter stringWriter = new StringWriter();
                StreamResult xmlOutput = new StreamResult(stringWriter);
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", String.valueOf(indent));
                transformer.transform(xmlInput, xmlOutput);
                return xmlOutput.getWriter().toString();
            }
            catch(Throwable t)
            {
                return input;
            }
        }
    }
 
    public static String prettyFormat(String input) {
        return prettyFormat(input, 2);
    }
	
}