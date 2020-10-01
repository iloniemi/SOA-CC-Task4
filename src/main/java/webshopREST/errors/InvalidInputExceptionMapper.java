package webshopREST.errors;


import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import types.ErrorMessage;

@Provider
public class InvalidInputExceptionMapper implements ExceptionMapper<InvalidInputException> {

	public InvalidInputExceptionMapper() {
	}
	
	@Override
	public Response toResponse(InvalidInputException exception) {
		ErrorMessage errorMessage = new ErrorMessage(exception.getMessage(),404,"http://myDocs.org");
		return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
	}

}
