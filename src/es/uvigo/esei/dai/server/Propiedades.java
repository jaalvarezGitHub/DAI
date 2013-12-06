package es.uvigo.esei.dai.server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Propiedades {

	private static Propiedades singleton=null;
	private static Properties propiedades;
	
    private Propiedades() {
    	propiedades=new Properties();
    }
    public static Propiedades getInstancia() {
        if (singleton == null) 
            singleton=new Propiedades();
        return singleton;
    }
    public  void loadPropiedades(String nombre_archivo) throws FileNotFoundException, IOException{
    	propiedades.load(new FileInputStream(nombre_archivo));
    }
    public int getPuerto(){
    	return Integer.parseInt(propiedades.getProperty("port"));	
    }
    public  int getConexion(){
    	return Integer.parseInt(propiedades.getProperty("numClients"));	
    }
    public  String getNombreBD(){
    	return propiedades.getProperty("db.url");	
    }
    public  String getUsuario(){
    	return propiedades.getProperty("db.user");	
    }
    public  String getContrasena(){
    	return propiedades.getProperty("db.password");	
    }
}

