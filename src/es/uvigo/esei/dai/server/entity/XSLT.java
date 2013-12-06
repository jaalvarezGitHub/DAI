package es.uvigo.esei.dai.server.entity;

public class XSLT {
	String uuid;
	String xsd;
	String content;
	
	public XSLT(String uid,String contenido){
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
	public String getXSD(){
		return xsd;
	}
	public void setXSD(String xsd){
		this.xsd=xsd;
	}
}

