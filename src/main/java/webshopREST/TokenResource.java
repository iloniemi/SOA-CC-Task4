package webshopREST;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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
@RolesAllowed({"admin","user"})
public class TokenResource {
	private SingletonDatabase database = SingletonDatabase.getDatabase();
	
	//basic authilla tullaan tanne ja paastaan heti useriin kasiksi.
	@GET
	@Produces(MediaType.APPLICATION_JSON)
    public Response getToken(@Context SecurityContext securityContext) {
		User user = (User) securityContext.getUserPrincipal();
		
		AuthenticationToken token = new AuthenticationToken();
		try {
			token = TokenService.getToken(user);
		} catch (InvalidKeyException | NoSuchAlgorithmException | UnsupportedEncodingException e) {
			Response.status(Status.INTERNAL_SERVER_ERROR).entity("{}").build();
		}
		
		return Response.status(Status.OK).entity(token).build();
    }
}
