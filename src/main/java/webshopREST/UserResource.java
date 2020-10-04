package webshopREST;

import java.security.Principal;
import java.util.List;

import javax.annotation.security.DenyAll;
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
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import types.ErrorMessage;
import types.User;
import webshopREST.database.SingletonDatabase;
import webshopREST.errors.DataNotFoundException;


/**
 * Root resource (exposed at "users" path)
 */
@Path("/users")
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
	private SingletonDatabase database = SingletonDatabase.getDatabase();
//	@Context
//	private WebshopSecurityContext securityContext;

    @GET
    public Response getUsers() {
    	List<User> users = database.getUsers();
		return Response.status(Status.OK).entity(users).build();
    }
    
    @GET
    @RolesAllowed({"admin","user"})
    @Path("/{userId}")
    public Response getUser(@PathParam("userId") String userId, @Context SecurityContext securityContext) {
    	//Gives error if user requests someone else's data
    	User requester = (User) securityContext.getUserPrincipal();
    	if (!requester.getRoles().contains("admin")) {
    		if (!requester.getId().equalsIgnoreCase(userId)) {
    			return Response.status(Status.UNAUTHORIZED)
    				.entity(new ErrorMessage("User cannot access other user's info.", 401, "http://myDocs.org"))
    				.build();
    		}
    	}
    	
    	System.out.println("getUser metodissa resourcessa");
    	User user = database.getUser(userId);
		return Response.status(Status.OK).entity(user).build();
		
    }
    
    @POST    
    @RolesAllowed("admin")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(User user) {

    	user.addLink("/webshopREST/webapi/users/" + user.getId(),  "self");
    	
    	User addedUser = database.addUser(user);
		return Response.status(Status.CREATED).entity(addedUser).build();
    }
    
    @PUT
    @RolesAllowed("admin")
    @Path("/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response replaceUser(@PathParam("userId") String userId, User user) {
    	
    	user.addLink("/webshopREST/webapi/users/" + user.getId(),  "self");
    	
    	User replacedUser = database.replaceUser(userId, user);
		return Response.status(Status.OK).entity(replacedUser).build();
    }
    
    @DELETE
    @RolesAllowed("admin")
    @Path("/{userId}")
    public Response removeUser(@PathParam("userId") String userId) {
    	if (database.removeUser(userId)) {
			return Response.status(Status.OK).entity("{}").build();
    	}
    	// if database.removeUser() == false, user with id was not found (or data reading from shopdata.json has failed)
    	return Response.status(Status.BAD_REQUEST).entity("{}").build();
    }
}
