package webshopREST.errors;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ErrorMessage {
	private String errorMessage;
	private int errorCode;
	private String documentation;
	
	/**
	 * Empty constructor
	 */
	public ErrorMessage() {
		
	}
	
	/**
	 * Constructor for error message
	 * @param errorMessage the message
	 * @param errorCode the code of the error
	 * @param documentation documentation for the error
	 */
	public ErrorMessage(String errorMessage, int errorCode, String documentation) {
		super();
		this.errorMessage = errorMessage;
		this.errorCode = errorCode;
		this.documentation = documentation;
	}
	
	/**
	 * Function that returns an error message
	 * @return the error message
	 */
	public String getErrorMessage( ) {
		return errorMessage;
	}
	
	/**
	 * Function that returns the code of an error
	 * @return number code for the error
	 */
	public int getErrorCode() {
		return errorCode;
	}
	
	/**
	 * Function that returns the documentation about an error
	 * @return documentation regarding the error
	 */
	public String getDocumentation() {
		return documentation;
	}
	
	/**
	 * Function to set the error message
	 * @param message new error message
	 */
	public void setErrorMessage(String message) {
		this.errorMessage = message;
	}
	
	/**
	 * Function that sets the integer for the error number
	 * @param code number representation of the error code
	 */
	public void setErrorCode (int code) {
		this.errorCode = code;
	}
	
	/**
	 * Function that sets the documentation about an error
	 * @param documentation
	 */
	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}
}
