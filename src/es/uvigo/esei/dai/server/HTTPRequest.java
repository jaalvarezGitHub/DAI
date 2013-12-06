package es.uvigo.esei.dai.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTTPRequest {
	
	 private String version;
	 private String metodo ;
	 private String recurso;
	 private Map<String,String> parametros=new HashMap<String,String>();
     private Map<String,String> cabecera=new HashMap<String,String>();
	 private int tam_contenido;
	 
	 public HTTPRequest(Reader peticion) throws IOException{

		  BufferedReader br=new BufferedReader(peticion);
		 
		  String linea, par;
		  String ref_1 [] ,ref_2 [];
		  Pattern patron;
		  Matcher matcher;
		  
		  linea=br.readLine();
	
		  patron=Pattern.compile("(?<metodo>.*)\\s(?:(/.)*)?/(?<recurso>[^?]*)(\\s|\\?(?<parametros>.*)\\s)(?<version>.*)");
		  matcher=patron.matcher(linea);
		  matcher.find();
		   
		  this.recurso=matcher.group("recurso");
		  this.metodo=matcher.group("metodo").toUpperCase();
		  this.version=matcher.group("version");
		  
		  par=matcher.group("parametros");
		  if(par!=null){
			   
			  ref_1=par.split("&");
			  for(int i=0; i<ref_1.length;i++){
				  ref_2=ref_1[i].split("=");
				  parametros.put(ref_2[0], ref_2[1]);
			  }
		  }
		  while(!(linea=br.readLine()).equals("")){
			  ref_1=linea.split(":");
			  cabecera.put(ref_1[0], ref_1[1]);
		  }
		  
		  if(cabecera.get("Content-Length")!=null){
					
			 this.tam_contenido=Integer.parseInt((cabecera.get("Content-Length").trim()));
			 
			  if(this.tam_contenido>0){
				  char [] contenido=new char [this.tam_contenido];
				  br.read(contenido,0, this.tam_contenido);
				  linea=URLDecoder.decode(new String(contenido),"UTF-8");
				  ref_1=linea.split("&");
				  for(int i=0; i<ref_1.length;i++) {
					  ref_2=ref_1[i].split("=");
					  this.parametros.put(ref_2[0],ref_2[1]);
				  }
			  }
		  }
	 	}
	 
	 public String getMetodo(){
		 return metodo;
	 }
	 public String getVersion(){
		 return version;
	 }
	 public String getRecurso(){
		 return recurso;
	 }
	 public int getTam(){
		 return tam_contenido;
	 }
	 public Map<String,String> getParametros(){
		 return parametros;
	 }
	 public Map<String,String> getCabecera(){
		 return cabecera;
	 }
 }
