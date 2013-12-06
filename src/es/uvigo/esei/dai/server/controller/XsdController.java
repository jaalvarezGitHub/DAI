package es.uvigo.esei.dai.server.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import es.uvigo.esei.dai.server.HTTPResponse;
import es.uvigo.esei.dai.server.PaginaNotFoundException;
import es.uvigo.esei.dai.server.dbdao.XSDDBDAO;
import es.uvigo.esei.dai.server.entity.XSD;

public class XsdController {
	
	private final XSDDBDAO XsdDBDAO;
	
	public XsdController(XSDDBDAO XsdDBDAO){
		this.XsdDBDAO=XsdDBDAO;
	}
	
	public HTTPResponse getPagina(String uuid) throws PaginaNotFoundException, SQLException {
		String contenido_pagina = XsdDBDAO.get(uuid).getContent();
		Map<String, String> parametros_respuesta_http = new HashMap<String, String>();
		
		parametros_respuesta_http.put("Content-Length",contenido_pagina.length() + "");
		parametros_respuesta_http.put("Content-Type","text/html; charset =UTF8");
		return  new HTTPResponse("200 OK", "HTTP/1.1",contenido_pagina, parametros_respuesta_http);			
	}
	
	public  HTTPResponse post(String p)throws  SQLException {
		Map<String, String> parametros_respuesta_http = new HashMap<String, String>();
		String uuid=UUID.randomUUID().toString();
		XSD xsd=new XSD(uuid,p);
		this.XsdDBDAO.create(xsd);
		
		String paginaPost="<html><head><title>WEB</title></head><body><a href=xsd?uuid="+ xsd.getUUID()+">Pagina con UUID"+xsd.getUUID()+"</a></body></html>";
		parametros_respuesta_http.put("Content-Length",String.valueOf(paginaPost.length()));
		parametros_respuesta_http.put("Content-Type","text/html; charset =UTF8");
		return new HTTPResponse("200 OK", "HTTP/1.1",paginaPost,parametros_respuesta_http);
	}	
	
	public HTTPResponse delete(String uuid) throws PaginaNotFoundException, SQLException{
		Map<String, String> parametros_respuesta_http = new HashMap<String, String>();
		
		this.XsdDBDAO.delete(uuid);
		String paginaDelete = "<html><head><title>WEB</title></head><body><p>Se ha borrado la pagina con uiid"+uuid +"</p></body></html>";
		parametros_respuesta_http.put("Content-Length",paginaDelete.length() + "");
		parametros_respuesta_http.put("Content-Type","text/html; charset =UTF8");
		return new HTTPResponse("200 OK", "HTTP/1.1",paginaDelete, parametros_respuesta_http);
		
	}
	
	public HTTPResponse getPaginaIndex() throws SQLException{
		Map<String, String> parametros_respuesta_http = new HashMap<String, String>();
		String paginaIndex="<html><head><title> WEB</title ></head><body><p>Listado de páginas</p>"
				+"<a href=html?uuid=11111111-1111-1111-1111-111111111114>Añadir nueva pagina</a><br></br>";
		ArrayList<String> lista=XsdDBDAO.getUUIDS();
		
		Iterator<String> it=lista.iterator();
		while(it.hasNext()){
			String identificador=it.next();
			if(!identificador.equals("11111111-1111-1111-1111-111111111111")){//Si no es la pagina de post que mostramos arriba
				paginaIndex=paginaIndex+"<a href=xsd?uuid="+identificador+"> Pagina con UUID:"+identificador+"</a><br></br>"; 
			}
		}
		paginaIndex=paginaIndex+"</body></html>";
		
		parametros_respuesta_http.put("Content-Length",	paginaIndex.length() + "");
		parametros_respuesta_http.put("Content-Type","text/html; charset =UTF8");
		return  new HTTPResponse("200 OK", "HTTP/1.1", paginaIndex,parametros_respuesta_http);
	}	
}
