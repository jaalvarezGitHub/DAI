package es.uvigo.esei.dai.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.ws.Endpoint;

import es.uvigo.esei.dai.service.DaiServiceImpl;

public class HiloServidor {

	public static void arrancarServidor() {
		try{
			ConfigProperties cp= ConfigProperties.getInstance(); 
		
			try (ServerSocket serverSocket = new ServerSocket(cp.getHttp()) ) {
				ExecutorService executor = Executors.newFixedThreadPool(cp.getNumClients());
				///
				Connection connection = DriverManager.getConnection(cp.getUrl(), cp.getUser(), cp.getPass());
				Endpoint endpoint =Endpoint.create(new DaiServiceImpl(connection));
				endpoint.setExecutor(Executors.newFixedThreadPool(50));
				endpoint.publish("http://localhost:8000/dai");				
				////
				
				while (true) {
					executor.execute(new HiloServicio(serverSocket.accept()));
				}
			} catch (IOException e) {
				System.out.println("Error en el servidor: " + e.getMessage());
			}
		}catch (Exception e) {
			System.out.println(e);
		}
	}
}