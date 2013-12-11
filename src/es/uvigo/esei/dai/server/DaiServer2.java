package es.uvigo.esei.dai.server;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.Executors;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.Endpoint;

import org.xml.sax.SAXException;

import es.uvigo.esei.dai.service.DaiServiceImpl;

public class DaiServer2 {
	public static void main(String[] args) {
		
		if(args.length==2){ 
			ConfigProperties cp= ConfigProperties.getInstance();
			try {
				cp.loadProperties(args[0], args[1]);
				
				Connection connection = DriverManager.getConnection(cp.getUrl(), cp.getUser(), cp.getPass());
				Endpoint endpoint =Endpoint.create(new DaiServiceImpl(connection));
				endpoint.setExecutor(Executors.newFixedThreadPool(50));
				endpoint.publish(cp.getWebService());	
				
				HiloServidor.arrancarServidor();
			} catch (ParserConfigurationException | SAXException | IOException e) {
				System.out.println("Error durante la configuración del servidor");
			} catch (SQLException e) {
				System.out.println("Error en la conexión con la base de datos");
			}			
		}else
			System.out.print("El numero de argumentos es incorrecto");
	}
}
