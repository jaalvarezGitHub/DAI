package es.uvigo.esei.dai.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import es.uvigo.esei.dai.server.controller.HtmlController;
import es.uvigo.esei.dai.server.controller.XmlController;
import es.uvigo.esei.dai.server.controller.XsdController;
import es.uvigo.esei.dai.server.controller.XsltController;
import es.uvigo.esei.dai.server.dbdao.HTMLDBDAO;
import es.uvigo.esei.dai.server.dbdao.XMLDBDAO;
import es.uvigo.esei.dai.server.dbdao.XSDDBDAO;
import es.uvigo.esei.dai.server.dbdao.XSLTDBDAO;
import es.uvigo.esei.dai.server.entity.XSD;

public class HiloServicio implements Runnable {
	private final Socket socket;

	public HiloServicio(Socket socket) {
		this.socket = socket;
	}
	public void run() {
		
		Propiedades p=Propiedades.getInstancia();
		Map<String, String> parametros_respuesta_http = new HashMap<String, String>();
		HTTPResponse hres;
		
		try (Socket socket = this.socket) {
			
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream()));
			BufferedReader br = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
	
			try{
				HTTPRequest hreq = new HTTPRequest(br);
				
				switch(hreq.getRecurso()){
					case "html":
						HTMLDBDAO paginaDBDAO = new HTMLDBDAO(DriverManager.getConnection(p.getNombreBD(), p.getUsuario(), p.getContrasena()));
						HtmlController htmlcontroler = new HtmlController(paginaDBDAO);
						
						switch (hreq.getMetodo()) {
							case "GET":
								String uuid = hreq.getParametros().get("uuid");
								if (uuid != null) 
									hres = htmlcontroler.getPagina(uuid);
								else
									hres=htmlcontroler.getPaginaIndex(); 
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
						XMLDBDAO xmlDBDAO = new XMLDBDAO(DriverManager.getConnection(p.getNombreBD(), p.getUsuario(), p.getContrasena()));
						XmlController xmlcontroler = new XmlController(xmlDBDAO);
						switch (hreq.getMetodo()) {
							case "GET":
								String uuid = hreq.getParametros().get("uuid");
								if (uuid != null) 
									hres = xmlcontroler.getPagina(uuid);
								else
									hres=xmlcontroler.getPaginaIndex(); 
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
						XSDDBDAO xsdDBDAO = new XSDDBDAO(DriverManager.getConnection(p.getNombreBD(), p.getUsuario(), p.getContrasena()));
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
						XSLTDBDAO xsltDBDAO = new XSLTDBDAO(DriverManager.getConnection(p.getNombreBD(), p.getUsuario(), p.getContrasena()));
						XsltController xsltcontroler = new XsltController(xsltDBDAO);
						XSDDBDAO xsdltDBDAO = new XSDDBDAO(DriverManager.getConnection(p.getNombreBD(), p.getUsuario(), p.getContrasena()));
					//	XsdController xsdltcontroler = new XsdController(xsdltDBDAO);
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
			}
		
		 } catch (IOException e) {
				System.out.println("Error en el servicio al cliente: "+ e.getMessage());
	
		 }
	}
}

