package es.uvigo.esei.dai.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HiloServidor {

	public static void arrancarServidor() {
		try{
			Propiedades p=Propiedades.getInstancia();
			try (ServerSocket serverSocket = new ServerSocket(p.getPuerto())) {
				ExecutorService executor = Executors.newFixedThreadPool(p.getConexion());
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