package es.uvigo.esei.dai.server;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class HTTPResponse {
	
	private String estado;
	private String version;
	private String contenido;
	private Map<String,String> cabecera;
	
	public HTTPResponse(String e, String v, String c, Map<String,String> cab){
		
		this.estado=e;
		this.version=v;
		this.contenido=c;
		this.cabecera=cab;	
	}
	
	public void print(Writer bw) throws IOException{
				
		bw.write(this.version+" "+this.estado+"\r\n");
		Iterator<Entry<String,String>> it=cabecera.entrySet().iterator();
		Map.Entry<String,String> m;
		
		while(it.hasNext()){
			m=it.next();
			bw.write(m.getKey()+": "+m.getValue());
			bw.write("\n");
		}
	    bw.write("\n");
	    bw.write(this.contenido);
	    bw.close();
	}

}
