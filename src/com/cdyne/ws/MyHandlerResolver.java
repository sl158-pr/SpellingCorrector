package com.cdyne.ws;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;



/*****************************************************************************************************************
** File       : MyHandlerResolver.java 
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
public class MyHandlerResolver implements HandlerResolver {

	public List getHandlerChain(PortInfo portInfo) {
		ArrayList<MyHandler> handlerChain = new ArrayList<MyHandler>();
		handlerChain.add(new MyHandler());
		return handlerChain;
	}
}
