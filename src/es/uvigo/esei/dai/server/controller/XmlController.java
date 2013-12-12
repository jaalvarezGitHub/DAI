package es.uvigo.esei.dai.server.controller;


import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import javax.xml.XMLConstants;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

import es.uvigo.esei.dai.server.HTTPResponse;
import es.uvigo.esei.dai.server.PaginaNotFoundException;
import es.uvigo.esei.dai.server.dbdao.XMLDBDAO;
import es.uvigo.esei.dai.server.entity.XML;

public class XmlController {
	private final XMLDBDAO XmlDBDAO;
	
	public XmlController(XMLDBDAO paginaDBDAO){
		this.XmlDBDAO=paginaDBDAO;
	}
	
	public HTTPResponse getPagina(String uuid) throws PaginaNotFoundException, SQLException {
		String contenido_pagina = XmlDBDAO.get(uuid).getContent();
		Map<String, String> parametros_respuesta_http = new HashMap<String, String>();
		
		parametros_respuesta_http.put("Content-Length",contenido_pagina.length() + "");
		parametros_respuesta_http.put("Content-Type","text/xml; charset=utf-8");
		return  new HTTPResponse("200 OK", "HTTP/1.1",contenido_pagina, parametros_respuesta_http);			
	}
	
	public HTTPResponse getPagina(String uuid,String xsd, String xslt) throws PaginaNotFoundException, SQLException, SAXException, IOException, TransformerException {
		
		String xml= XmlDBDAO.get(uuid).getContent();
		
		StreamSource streamXsd = new StreamSource(new StringReader(xslt));
		Map<String, String> parametros_respuesta_http = new HashMap<String, String>();
	
		XmlController.validateXML(new StreamSource(new StringReader(xml)),streamXsd);
		
		TransformerFactory tfactory =TransformerFactory.newInstance();
		Transformer transformer = tfactory.newTransformer(new StreamSource(new StringReader(xslt)));
		StringWriter stringWriter = new StringWriter();
		transformer.transform(new StreamSource(new StringReader(xml)), new StreamResult(stringWriter));
		
		parametros_respuesta_http.put("Content-Length",xml.length() + "");
		parametros_respuesta_http.put("Content-Type","text/html; charset =UTF8");

			return  new HTTPResponse("200 OK", "HTTP/1.1",stringWriter.toString(), parametros_respuesta_http);	
			
	}
	
	public static boolean validateXML(StreamSource xml, StreamSource xsd) throws SAXException, IOException{
		
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = schemaFactory.newSchema(xsd);
			
			Validator validator= schema.newValidator();
			validator.validate(xml);
			
			return true;
		
	}
	
	public  HTTPResponse post(String p)throws  SQLException {
		Map<String, String> parametros_respuesta_http = new HashMap<String, String>();
		String uuid=UUID.randomUUID().toString();
		XML pagina=new XML(uuid,p);
		this.XmlDBDAO.create(pagina);
		
		String paginaPost="<html><head><title>WEB</title></head><body><a href=xml?uuid="+ pagina.getUUID()+">Pagina con UUID"+pagina.getUUID()+"</a></body></html>";
		parametros_respuesta_http.put("Content-Length",String.valueOf(paginaPost.length()));
		parametros_respuesta_http.put("Content-Type","text/html; charset =UTF8");
		return new HTTPResponse("200 OK", "HTTP/1.1",paginaPost,parametros_respuesta_http);
	}
	
	public HTTPResponse delete(String uuid) throws PaginaNotFoundException, SQLException{
		Map<String, String> parametros_respuesta_http = new HashMap<String, String>();
		
		this.XmlDBDAO.delete(uuid);
		String paginaDelete = "<html><head><title>WEB</title></head><body><p>Se ha borrado la pagina con uiid"+uuid +"</p></body></html>";
		parametros_respuesta_http.put("Content-Length",paginaDelete.length() + "");
		parametros_respuesta_http.put("Content-Type","text/html; charset =UTF8");
		return new HTTPResponse("200 OK", "HTTP/1.1",paginaDelete, parametros_respuesta_http);	
	}
	
	public HTTPResponse getPaginaIndex() throws SQLException{
		Map<String, String> parametros_respuesta_http = new HashMap<String, String>();
		String paginaIndex="<html><head><title> WEB</title ></head><body><p>Listado de páginas XML</p>"
				+"<a href=html?uuid=11111111-1111-1111-1111-111111111113>Añadir nueva pagina</a><br></br>";
		ArrayList<String> lista=XmlDBDAO.getUUIDS();
		
		Iterator<String> it=lista.iterator();
		while(it.hasNext()){
			String identificador=it.next();
			if(!identificador.equals("11111111-1111-1111-1111-111111111111")){//Si no es la pagina de post que mostramos arriba
				paginaIndex=paginaIndex+"<a href=xml?uuid="+identificador+"> Pagina con UUID:"+identificador+"</a><br></br>"; 
			}
		}
		paginaIndex=paginaIndex+"</body></html>";
		
		parametros_respuesta_http.put("Content-Length",	paginaIndex.length() + "");
		parametros_respuesta_http.put("Content-Type","text/html; charset =UTF8");
		return  new HTTPResponse("200 OK", "HTTP/1.1", paginaIndex,parametros_respuesta_http);
	}	
}

