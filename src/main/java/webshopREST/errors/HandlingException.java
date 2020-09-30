package webshopREST.errors;

/**
 * Class for other exceptions
 * @author domanasi
 *
 */
public class HandlingException extends RuntimeException{
	private static final long serialVersionUID = 2L;
	
		public HandlingException (String msg) {
			super(msg);
		}

}
