package es.uvigo.esei.dai.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import es.uvigo.esei.dai.server.PaginaNotFoundException;

public class client {
	
	public static void main(String[] args) throws MalformedURLException, PaginaNotFoundException, SQLException {
		URL url = new URL("http://localhost:8000/dai?wsdl");
		QName name = new QName("http://service.dai.esei.uvigo.es/","DaiServiceImplService");
		
		Service service =  Service.create(url,name);
		
		DaiService ds = service.getPort(DaiService.class);
		
		System.out.println(ds.getHtmlContent("da545b22-48d8-fa02-69bc-592da5c8f9a2"));
	}	
}
