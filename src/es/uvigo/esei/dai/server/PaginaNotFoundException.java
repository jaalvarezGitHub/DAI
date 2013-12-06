package es.uvigo.esei.dai.server;


public class PaginaNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	private final String id;

	public PaginaNotFoundException(String id) {
		this.id = id;
	}

	public PaginaNotFoundException(String message, String id) {
		super(message);
		this.id = id;
	}

	public PaginaNotFoundException(Throwable cause, String id) {
		super(cause);
		this.id = id;
	}

	public PaginaNotFoundException(String message, Throwable cause, String id) {
		super(message, cause);
		this.id = id;
	}

	public PaginaNotFoundException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace, String id) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.id = id;
	}
	
	public String getId() {
		return this.id;
	}
}