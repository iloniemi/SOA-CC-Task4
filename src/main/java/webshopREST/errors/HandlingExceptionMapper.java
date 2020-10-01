package webshopREST.errors;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import types.ErrorMessage;

@Provider
public class HandlingExceptionMapper implements ExceptionMapper<HandlingException> {

	@Override
	public Response toResponse(HandlingException exception) {
		ErrorMessage errorMessage = new ErrorMessage(exception.getMessage(),500,"http://myDocs.org");
		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorMessage).build();
	}

}
