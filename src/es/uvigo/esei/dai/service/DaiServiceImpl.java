package es.uvigo.esei.dai.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.jws.WebService;

import es.uvigo.esei.dai.server.PaginaNotFoundException;
import es.uvigo.esei.dai.server.dbdao.HTMLDBDAO;
import es.uvigo.esei.dai.server.dbdao.XMLDBDAO;
import es.uvigo.esei.dai.server.dbdao.XSDDBDAO;
import es.uvigo.esei.dai.server.dbdao.XSLTDBDAO;

@WebService (
		endpointInterface ="es.uvigo.esei.dai.service.DaiService"
		)

public class DaiServiceImpl implements DaiService{
	
	public Connection connection=null;
	public String name=null;
	public String port=null;
	
	public DaiServiceImpl(Connection connection, String name, String port){
		this.connection=connection;
		this.name=name;
		this.port=port;
	}
	@Override
	public String getName(){
		return this.name;
	}
	@Override
	public String getPort(){
		return this.port;
	}
	@Override
	public ArrayList<String> getHtmlUUID() throws SQLException {
		HTMLDBDAO htmlDao = new HTMLDBDAO(this.connection);
		return htmlDao.getUUIDS();
	}

	@Override
	public ArrayList<String> getXmlUUID() throws SQLException {
		XMLDBDAO xmlDao = new XMLDBDAO(this.connection);
		return xmlDao.getUUIDS();
	}

	@Override
	public ArrayList<String> getXsdUUID() throws SQLException {
		XSDDBDAO xsdDao = new XSDDBDAO(this.connection);
		return xsdDao.getUUIDS();
	}

	@Override
	public ArrayList<String> getXslUUID() throws SQLException {
		XSLTDBDAO xsltDao = new XSLTDBDAO(this.connection);
		return xsltDao.getUUIDS();
	}
	
	@Override
	public String getHtmlContent(String uuid) throws SQLException, PaginaNotFoundException  {
		HTMLDBDAO htmlDao = new HTMLDBDAO(this.connection);
		return htmlDao.get(uuid).getContent();
		
	}
	@Override
	public String getXmlContent(String uuid) throws  SQLException, PaginaNotFoundException {
		XMLDBDAO xmlDao = new XMLDBDAO(this.connection);
		return xmlDao.get(uuid).getContent();
		
	}

	@Override
	public String getXsdContent(String uuid) throws  SQLException, PaginaNotFoundException {
		XSDDBDAO xsdDao = new XSDDBDAO(this.connection);
		return xsdDao.get(uuid).getContent();
	
	}

	@Override
	public String getXsltContent(String uuid) throws SQLException, PaginaNotFoundException {
		XSLTDBDAO xsltDao = new XSLTDBDAO(this.connection);
		return xsltDao.get(uuid).getContent();
	
	}
	@Override
	public String getXsltXsd(String uuid) throws SQLException, PaginaNotFoundException {
		XSLTDBDAO xsltDao = new XSLTDBDAO(this.connection);
		return xsltDao.get(uuid).getXSD();
	
	}

}
