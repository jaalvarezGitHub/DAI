package es.uvigo.esei.dai.server;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class ConfigProperties {
	private static ConfigProperties cp;
	private static Document document;

	private ConfigProperties() {} 
	
	public void loadProperties(String xmlPath, String xsdPath) throws ParserConfigurationException, SAXException, IOException{
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema s = sf.newSchema(new File(xsdPath));
		
		DocumentBuilderFactory bf = DocumentBuilderFactory.newInstance();
		bf.setNamespaceAware(true);
		bf.setValidating(false);
		bf.setSchema(s);
		
		DocumentBuilder b= bf.newDocumentBuilder();
		
		document = b.parse(new File(xmlPath));	
	}
    public static ConfigProperties getInstance() {
        if (cp == null) 
            cp=new ConfigProperties();
        return cp;
    }
	public String getUrl(){
		return  document.getElementsByTagName("url").item(0).getTextContent();
	}
	public String getUser(){
		return  document.getElementsByTagName("user").item(0).getTextContent();
	}
	public String getPass(){
		return  document.getElementsByTagName("password").item(0).getTextContent();
	}
	public int getNumClients(){
		return  Integer.parseInt(document.getElementsByTagName("numClients").item(0).getTextContent());
	}	
	public String getWebService(){
		return  document.getElementsByTagName("webservice").item(0).getTextContent();
	}
	public int getHttp(){
		return  Integer.parseInt(document.getElementsByTagName("http").item(0).getTextContent());
	}
	public  NodeList getServer(){
		 return  document.getElementsByTagName("server");

	}
}
