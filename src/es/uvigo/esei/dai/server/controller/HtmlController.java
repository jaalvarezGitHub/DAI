package es.uvigo.esei.dai.server.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import es.uvigo.esei.dai.server.HTTPResponse;
import es.uvigo.esei.dai.server.PaginaNotFoundException;
import es.uvigo.esei.dai.server.dbdao.HTMLDBDAO;
import es.uvigo.esei.dai.server.entity.HTML;

public class HtmlController {
	
	private final HTMLDBDAO paginaDBDAO;
	
	public HtmlController(HTMLDBDAO paginaDBDAO){
		this.paginaDBDAO=paginaDBDAO;
	}
	
	public HTTPResponse getPagina(String uuid) throws PaginaNotFoundException, SQLException {
		String contenido_pagina = paginaDBDAO.get(uuid).getContent();
		Map<String, String> parametros_respuesta_http = new HashMap<String, String>();
		
		parametros_respuesta_http.put("Content-Length",contenido_pagina.length() + "");
		parametros_respuesta_http.put("Content-Type","text/html; charset =UTF8");
		return  new HTTPResponse("200 OK", "HTTP/1.1",contenido_pagina, parametros_respuesta_http);			
	}
	
	public  HTTPResponse post(String p)throws  SQLException {
		Map<String, String> parametros_respuesta_http = new HashMap<String, String>();
		String uuid=UUID.randomUUID().toString();
		HTML pagina=new HTML(uuid,p);
		this.paginaDBDAO.create(pagina);
		
		String paginaPost="<html><head><title>WEB</title></head><body><a href=html?uuid="+ pagina.getUUID()+">Pagina con UUID"+pagina.getUUID()+"</a></body></html>";
		parametros_respuesta_http.put("Content-Length",String.valueOf(paginaPost.length()));
		parametros_respuesta_http.put("Content-Type","text/html; charset =UTF8");
		return new HTTPResponse("200 OK", "HTTP/1.1",paginaPost,parametros_respuesta_http);
	}	
	
	public HTTPResponse delete(String uuid) throws PaginaNotFoundException, SQLException{
		Map<String, String> parametros_respuesta_http = new HashMap<String, String>();
		
		this.paginaDBDAO.delete(uuid);
		String paginaDelete = "<html><head><title>WEB</title></head><body><p>Se ha borrado la pagina con uiid"+uuid +"</p></body></html>";
		parametros_respuesta_http.put("Content-Length",paginaDelete.length() + "");
		parametros_respuesta_http.put("Content-Type","text/html; charset =UTF8");
		return new HTTPResponse("200 OK", "HTTP/1.1",paginaDelete, parametros_respuesta_http);
		
	}
	
	public HTTPResponse getPaginaIndex() throws SQLException{
		Map<String, String> parametros_respuesta_http = new HashMap<String, String>();
		String paginaIndex="<html><head><title> WEB</title ></head><body><p>Listado de páginas</p>"
				+"<a href=html?uuid=11111111-1111-1111-1111-111111111111>Añadir nueva pagina</a><br></br>";
		ArrayList<String> lista=paginaDBDAO.getUUIDS();
		
		Iterator<String> it=lista.iterator();
		while(it.hasNext()){
			String identificador=it.next();
			if(!identificador.equals("11111111-1111-1111-1111-111111111111")){//Si no es la pagina de post que mostramos arriba
				paginaIndex=paginaIndex+"<a href=html?uuid="+identificador+"> Pagina con UUID:"+identificador+"</a><br></br>"; 
			}
		}
		paginaIndex=paginaIndex+"</body></html>";
		
		parametros_respuesta_http.put("Content-Length",	paginaIndex.length() + "");
		parametros_respuesta_http.put("Content-Type","text/html; charset =UTF8");
		return  new HTTPResponse("200 OK", "HTTP/1.1", paginaIndex,parametros_respuesta_http);
	}	
}

