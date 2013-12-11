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
	
	public DaiServiceImpl(Connection connection){
		this.connection=connection;
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
	public String getHtmlContent(String uuid) throws SQLException  {
		HTMLDBDAO htmlDao = new HTMLDBDAO(this.connection);
		try {
			return htmlDao.get(uuid).getContent();
		} catch (PaginaNotFoundException e) {
			 return null;
		}
	}
	@Override
	public String getXmlContent(String uuid) throws  SQLException {
		XMLDBDAO xmlDao = new XMLDBDAO(this.connection);
		try {
			return xmlDao.get(uuid).getContent();
		} catch (PaginaNotFoundException e) {
			return null;
		}
	}

	@Override
	public String getXsdContent(String uuid) throws  SQLException {
		XSDDBDAO xsdDao = new XSDDBDAO(this.connection);
		try {
			return xsdDao.get(uuid).getContent();
		} catch (PaginaNotFoundException e) {
			return null;
		}
	}

	@Override
	public String getXsltContent(String uuid) throws SQLException {
		XSLTDBDAO xsltDao = new XSLTDBDAO(this.connection);
		try {
			return xsltDao.get(uuid).getContent();
		} catch (PaginaNotFoundException e) {
			return null;
		}
	}

}
