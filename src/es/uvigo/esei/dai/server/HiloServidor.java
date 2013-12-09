package es.uvigo.esei.dai.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HiloServidor {

	public static void arrancarServidor() {
		try{
			ConfigProperties cp= ConfigProperties.getInstance(); 
		
			try (ServerSocket serverSocket = new ServerSocket(cp.getHttp()) ) {
				ExecutorService executor = Executors.newFixedThreadPool(cp.getNumClients());
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