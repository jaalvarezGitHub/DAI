package es.uvigo.esei.dai.server;

import java.io.FileNotFoundException;
import java.io.IOException;

public class DAIServer {

	
	public static void main(String[] args) {
			
		if(args.length==1){
			Propiedades p=Propiedades.getInstancia();
			try {
				p.loadPropiedades(args[0]);
				HiloServidor.arrancarServidor();
			} catch (FileNotFoundException e) {
				System.out.println("El archivo espficifaco no ha sido encontrado"+ e.getMessage());
			} catch (IOException e) {
				System.out.print("Se ha producido un error al cargar el archivo"+ e.getMessage());
			}
		}else
			System.out.print("El numero de argumentos es incorrecto");
	}
}
