package webshopREST;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.Response.Status;

import types.AuthenticationToken;
import types.User;
import webshopREST.database.SingletonDatabase;
import webshopREST.database.TokenService;


@Path("/tokens")
@PermitAll
public class TokenResource {
	private SingletonDatabase database = SingletonDatabase.getDatabase();
	
	//basic authilla tullaan tanne ja paastaan heti useriin kasiksi.
	@POST
	@Produces(MediaType.APPLICATION_JSON)
    public Response getToken(@Context SecurityContext securityContext) {
		User user = (User) securityContext.getUserPrincipal();
		
		AuthenticationToken token = TokenService.getToken(user);
		return Response.status(Status.OK).entity(token).build();
    }
}
