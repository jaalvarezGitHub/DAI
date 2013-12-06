package es.uvigo.esei.dai.server.entity;

public class XML {
	String uuid;
	String content;
	
	public XML(String uid,String contenido){
		this.uuid=uid;
		this.content=contenido;
	}
	public String getUUID(){
		return this.uuid;
	}
	public String getContent(){
		return this.content;
	}
	public void setUUID(String uid){
		this.uuid=uid;
	}
	public void setContent(String contenido){
		this.content=contenido;
	}
}
