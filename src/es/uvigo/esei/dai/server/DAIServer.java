package es.uvigo.esei.dai.server;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class DAIServer {
	
	public static void main(String[] args) {
			
		if(args.length==2){ 
			ConfigProperties cp= ConfigProperties.getInstance();
			try {
				cp.loadProperties(args[0], args[1]);
			} catch (ParserConfigurationException | SAXException | IOException e) {
				System.out.println("Error durante la configuración del servidor");
			}
			HiloServidor.arrancarServidor(); 
			
		}else
			System.out.print("El numero de argumentos es incorrecto");
	}
}
