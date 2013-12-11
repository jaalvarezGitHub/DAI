package es.uvigo.esei.dai.server;



import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

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
	
	
	public String buscarPagina(String metodo, String uuid) throws PaginaNotFoundException, SQLException, 
		IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException{
		
		Class c=Class.forName("es.uvigo.esei.dai.service.DaiService");
		Class[] args_class = { String.class };
		
		Method m = c.getMethod(metodo, args_class);
		Object resultado = null;
		for(DaiService service: listaServicios){
			resultado = m.invoke(service, uuid);
			if(resultado != null)
				return (String) resultado;
		}
		return null;
	}
	
}
