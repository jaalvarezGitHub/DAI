package es.uvigo.esei.dai.server;



import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;

import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import es.uvigo.esei.dai.service.DaiService;

public class RemoteDaiService {
	
	private ArrayList<DaiService> listaServicios=null;
	
	public RemoteDaiService() throws DOMException, MalformedURLException{
		this.listaServicios = new ArrayList<DaiService>();
		ConfigProperties cp = ConfigProperties.getInstance();
		NodeList nodeList = cp.getServer();
		
		Service service;
		for(int i=0; i<nodeList.getLength();i++){
			NamedNodeMap nodeMap = nodeList.item(i).getAttributes();

			try{
				service = Service.create (new URL(nodeMap.getNamedItem("wsdl").getTextContent()),
					new  QName(nodeMap.getNamedItem("namespace").getTextContent(), nodeMap.getNamedItem("service").getTextContent()));
			
				this.listaServicios.add(service.getPort(DaiService.class));
				
			}catch(WebServiceException e){}
		}
		
	}
	
	
	public String buscarPagina(String metodo, String uuid) throws  PaginaNotFoundException, SQLException,
		 IllegalArgumentException, NoSuchMethodException, SecurityException, ClassNotFoundException, IllegalAccessException{
		
		Class c=Class.forName("es.uvigo.esei.dai.service.DaiService");
		Class[] args_class = { String.class };
		
		Method m = c.getMethod(metodo, args_class);
		Object resultado = null;
		for(DaiService service: listaServicios){
			try{
				resultado = m.invoke(service, uuid);
			}catch(InvocationTargetException ite){
				if(ite.getCause() instanceof PaginaNotFoundException)
					throw (PaginaNotFoundException) ite.getCause();
				else
					throw (SQLException) ite.getCause();
			}
			if(resultado != null)
				return (String) resultado;
		}
		return null;
	}
	public ArrayList<DaiService> getServerIndex() {
		return this.listaServicios;
	}
		
}
