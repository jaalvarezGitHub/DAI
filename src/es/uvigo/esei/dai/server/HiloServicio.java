package es.uvigo.esei.dai.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import es.uvigo.esei.dai.server.controller.HtmlController;
import es.uvigo.esei.dai.server.controller.XmlController;
import es.uvigo.esei.dai.server.controller.XsdController;
import es.uvigo.esei.dai.server.controller.XsltController;
import es.uvigo.esei.dai.server.dbdao.HTMLDBDAO;
import es.uvigo.esei.dai.server.dbdao.XMLDBDAO;
import es.uvigo.esei.dai.server.dbdao.XSDDBDAO;
import es.uvigo.esei.dai.server.dbdao.XSLTDBDAO;
import es.uvigo.esei.dai.server.entity.XSD;
import es.uvigo.esei.dai.server.entity.XSLT;

public class HiloServicio implements Runnable {
	private final Socket socket;

	public HiloServicio(Socket socket) {
		this.socket = socket;
	}
	public void run() {
		
		Map<String, String> parametros_respuesta_http = new HashMap<String, String>();
		HTTPResponse hres;
		
		try (Socket socket = this.socket) {
			
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream()));
			BufferedReader br = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
	
			try{
				ConfigProperties cp=ConfigProperties.getInstance();
				Connection connection = DriverManager.getConnection(cp.getUrl(), cp.getUser(), cp.getPass());
				
				HTTPRequest hreq = new HTTPRequest(br);
				
				switch(hreq.getRecurso()){
					case "html":
						HTMLDBDAO paginaDBDAO = new HTMLDBDAO(connection);
						HtmlController htmlcontroler = new HtmlController(paginaDBDAO);
						
						switch (hreq.getMetodo()) {
							case "GET":
								String uuid = hreq.getParametros().get("uuid");
								if (uuid != null) 
									try{
										hres = htmlcontroler.getPagina(uuid);
									}catch(PaginaNotFoundException e ){
										RemoteDaiService remoteDaiService=new RemoteDaiService(); 
										hres = new HTTPResponse("200 OK", "HTTP/1.1",remoteDaiService.buscarPagina("getHtmlContent", uuid), parametros_respuesta_http);
									}
								else{
									RemoteDaiService remoteDaiService=new RemoteDaiService(); 
									hres=htmlcontroler.getPaginaIndex(remoteDaiService.getServerIndex()); 
								}
								hres.print(bw);
							break;
							case "POST":
								hres=htmlcontroler.post( hreq.getParametros().get("html")); 
								hres.print(bw);
							break;
							case "DELETE":
								String uuid_delete=hreq.getParametros().get("uuid");
								hres=htmlcontroler.delete(uuid_delete);
								hres.print(bw);
							break;	
						}
					break;
					case "xml":
						XMLDBDAO xmlDBDAO = new XMLDBDAO(connection);
						XmlController xmlcontroler = new XmlController(xmlDBDAO);
						switch (hreq.getMetodo()) {
							case "GET":
								String uuid = hreq.getParametros().get("uuid");
								String uuidXslt =hreq.getParametros().get("xslt");
								if(uuidXslt !=null){
									XSLT xslt_xml; XSD xsd_xml;
									try{
										xslt_xml = new XSLTDBDAO(connection).get(uuidXslt);
									}catch(PaginaNotFoundException e){
										RemoteDaiService remoteDaiService=new RemoteDaiService(); 
										xslt_xml = new XSLT(uuid, remoteDaiService.buscarPagina("getXsltContent", uuidXslt), remoteDaiService.buscarPagina("getXsltXsd", uuidXslt));
									}
									
									try{
										xsd_xml = new XSDDBDAO(connection).get(xslt_xml.getXSD());
									}catch(PaginaNotFoundException e){
										RemoteDaiService remoteDaiService=new RemoteDaiService(); 
										xsd_xml = new XSD(xslt_xml.getXSD(), remoteDaiService.buscarPagina("getXsdContent", xslt_xml.getXSD()));
									}
									hres = xmlcontroler.getPagina(uuid,xsd_xml.getContent(), xslt_xml.getContent());	
								}else if (uuid != null){
									try{
										hres = xmlcontroler.getPagina(uuid);
									}catch(PaginaNotFoundException e){
										RemoteDaiService remoteDaiService=new RemoteDaiService(); 
										hres = new HTTPResponse("200 OK", "HTTP/1.1",remoteDaiService.buscarPagina("getXmlContent", uuid), parametros_respuesta_http);
									}
									
								}else{
									hres=xmlcontroler.getPaginaIndex(); 
								}
								hres.print(bw);
							break;
							case "POST":
								hres=xmlcontroler.post( hreq.getParametros().get("xml")); 
								hres.print(bw);
							break;
							case "DELETE":
								String uuid_delete=hreq.getParametros().get("uuid");
								hres=xmlcontroler.delete(uuid_delete);
								hres.print(bw);
							break;	
						}
					break;
					case "xsd":
						XSDDBDAO xsdDBDAO = new XSDDBDAO(connection);
						XsdController xsdcontroler = new XsdController(xsdDBDAO);
						switch (hreq.getMetodo()) {
							case "GET":
								String uuid = hreq.getParametros().get("uuid");
								if (uuid != null) 
									hres = xsdcontroler.getPagina(uuid);
								else
									hres=xsdcontroler.getPaginaIndex(); 
								hres.print(bw);
							break;
							case "POST":
								hres=xsdcontroler.post( hreq.getParametros().get("xsd")); 
								hres.print(bw);
							break;
							case "DELETE":
								String uuid_delete=hreq.getParametros().get("uuid");
								hres=xsdcontroler.delete(uuid_delete);
								hres.print(bw);
							break;	
						}
					break;
					case "xslt":
						XSLTDBDAO xsltDBDAO = new XSLTDBDAO(connection);
						XsltController xsltcontroler = new XsltController(xsltDBDAO);
						XSDDBDAO xsdltDBDAO = new XSDDBDAO(connection);

						switch (hreq.getMetodo()) {
							case "GET":
								String uuid = hreq.getParametros().get("uuid");
								if (uuid != null) 
									hres = xsltcontroler.getPagina(uuid);
								else
									hres=xsltcontroler.getPaginaIndex(); 
								hres.print(bw);
							break;
							case "POST": 
								XSD xsd = xsdltDBDAO.get(hreq.getParametros().get("xsd"));
								hres=xsltcontroler.post( hreq.getParametros().get("xslt"),xsd.getUUID()); 
								hres.print(bw);
							break;
							case "DELETE":
								String uuid_delete=hreq.getParametros().get("uuid");
								hres=xsltcontroler.delete(uuid_delete);
								hres.print(bw);
							break;	
						}
					break;
				}
			} catch (PaginaNotFoundException e) {
					
				String paginaIndex = "<html><head><title>WEB</title></head><body><p>NOT FOUND</p></body></html>";
				parametros_respuesta_http.put("Content-Length",paginaIndex.length() + "");
				parametros_respuesta_http.put("Content-Type","text/html; charset =UTF8");
				hres = new HTTPResponse("404 NOT FOUND", "HTTP/1.1",paginaIndex, parametros_respuesta_http);
				hres.print(bw);
			} catch (SQLException sqle) {
				
				String paginaIndex = "<html><head><title>WEB</title></head><body><p>Error en el servidor</p></body></html>";
				parametros_respuesta_http.put("Content-Length",paginaIndex.length() + "");
				parametros_respuesta_http.put("Content-Type","text/html; charset =UTF8");
				hres = new HTTPResponse("500 SERVER ERROR", "HTTP/1.1",paginaIndex, parametros_respuesta_http);
				hres.print(bw);
			} catch (SAXException saxe){
				
				String paginaIndex = "<html><head><title>WEB</title></head><body><p>Error en el servidor(400 Bad request)</p></body></html>";
				parametros_respuesta_http.put("Content-Length",paginaIndex.length() + "");
				parametros_respuesta_http.put("Content-Type","text/html; charset =UTF8");
				hres = new HTTPResponse("400 BAD REQUEST", "HTTP/1.1",paginaIndex, parametros_respuesta_http);
				hres.print(bw);
			}catch (TransformerException transe){
			
				String paginaIndex = "<html><head><title>WEB</title></head><body><p>Error en la transformación del documento</p></body></html>";
				parametros_respuesta_http.put("Content-Length",paginaIndex.length() + "");
				parametros_respuesta_http.put("Content-Type","text/html; charset =UTF8");
				hres = new HTTPResponse("200 OK", "HTTP/1.1",paginaIndex, parametros_respuesta_http);
				hres.print(bw);
		} catch (Exception e) {
			String paginaIndex = "<html><head><title>WEB</title></head><body><p>Error en el servidor</p></body></html>";
			parametros_respuesta_http.put("Content-Length",paginaIndex.length() + "");
			parametros_respuesta_http.put("Content-Type","text/html; charset =UTF8");
			hres = new HTTPResponse("500 SERVER ERROR", "HTTP/1.1",paginaIndex, parametros_respuesta_http);
			hres.print(bw);
			
	 }
		
		 } catch (IOException e) {
				e.printStackTrace();
	
		 }
	}
}

