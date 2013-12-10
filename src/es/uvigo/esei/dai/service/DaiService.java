package es.uvigo.esei.dai.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.soap.SOAPBinding;

import es.uvigo.esei.dai.server.PaginaNotFoundException;

@WebService
@SOAPBinding(use=Use.ENCODED)
public interface DaiService {
	
	public Connection connection=null;

	
	@WebMethod public ArrayList<String>  getHtmlUUID() throws SQLException;
	@WebMethod public ArrayList<String>  getXmlUUID() throws SQLException;
	@WebMethod public ArrayList<String>  getXsdUUID() throws SQLException;
	@WebMethod public ArrayList<String>  getXslUUID() throws SQLException;
	
	@WebMethod String getHtmlContent(String uuid) throws PaginaNotFoundException, SQLException;
	@WebMethod String getXmlContent(String uuid) throws PaginaNotFoundException, SQLException;
	@WebMethod String getXsltContent(String uuid) throws PaginaNotFoundException, SQLException;
	@WebMethod String getXsdContent(String uuid) throws PaginaNotFoundException, SQLException;
}
