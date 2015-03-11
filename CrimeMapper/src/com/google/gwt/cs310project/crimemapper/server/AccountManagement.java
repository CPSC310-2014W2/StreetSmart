package com.google.gwt.cs310project.crimemapper.server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AccountManagement {
	
	
	public List<String> readXML(String fileName){
		
		List<String> employees = new ArrayList<String>();
		
		try{
		
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new File(fileName));
		
		NodeList nodeList = document.getDocumentElement().getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {

			Node node = nodeList.item(i);

			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element elem = (Element) node;

				// Get the value of all sub-elements.
				String address = elem.getElementsByTagName("Address")
						.item(0).getChildNodes().item(0).getNodeValue();
				employees.add(address);
			}
		}
		} catch (Exception e) {
	 		e.printStackTrace();
	 	}	
		
		return employees; 
	}
	
	public void writeXML(String fileName, String newMember){
		
		try{
			
			//FilePermission p = new FilePermission("AdminAccount.xml", "read,write");
		
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new File(fileName));
			
		//add in the DOM 
		Element rootElement = document.getDocumentElement();
	    Element em = document.createElement("Employee");
		rootElement.appendChild(em);
	    Element name = document.createElement("Address");
	    name.appendChild(document.createTextNode(newMember));
	    em.appendChild(name);
	    
	    // Use a Transformer for output
	 		TransformerFactory transformerFactory = TransformerFactory.newInstance();
	 		Transformer transformer = transformerFactory.newTransformer();
	 		DOMSource source = new DOMSource(document);
	 		StreamResult streamResult =  new StreamResult(new File(fileName));
	 		transformer.transform(source, streamResult);
	 	}
	 	catch (Exception e) {
	 		e.printStackTrace();
	 	}	
	}
}

