package webshopREST.errors;

/**
 * Class for 404 exceptions
 * @author domanasi
 *
 */
public class DataNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public DataNotFoundException (String msg) {
		super(msg);

	}

}
