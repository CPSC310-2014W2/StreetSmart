package com.google.gwt.cs310project.crimemapper.client;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.dom.DOMSource; 
import javax.xml.transform.stream.StreamResult; 

public class AccountManagement {
	
	public void readXML(String fileName) throws ParserConfigurationException,
	SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();

		Document document = builder.parse(new File(fileName));
		
		List<AdminAccount> employees = new ArrayList<AdminAccount>();

		NodeList nodeList = document.getDocumentElement().getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {

			Node node = nodeList.item(i);

			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element elem = (Element) node;

				// Get the value of all sub-elements.
				String address = elem.getElementsByTagName("Address")
						.item(0).getChildNodes().item(0).getNodeValue();
				employees.add(new AdminAccount(address));
			}
		}
	}
	
	public void writeXML(String fileName, String newMember) throws ParserConfigurationException,
	SAXException, IOException {
		
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
	 	try{
	 		TransformerFactory transformerFactory = TransformerFactory.newInstance();
	 		Transformer transformer = transformerFactory.newTransformer();
	 		DOMSource source = new DOMSource(document);
	 		StreamResult streamResult =  new StreamResult(new File("Employees.xml"));
	 		transformer.transform(source, streamResult);
	 	}
	 	catch (Exception e) {
	 		e.printStackTrace();
	 	}	
	}
}

