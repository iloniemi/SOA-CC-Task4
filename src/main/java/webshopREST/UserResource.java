package webshopREST;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import database.SingletonDatabase;
import types.User;

/**
 * Root resource (exposed at "users" path)
 */
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
	private SingletonDatabase database = SingletonDatabase.getDatabase();

    @GET
    public Response getUsers() {
    	List<User> users = database.getUsers();
    	if (users != null) {
    		return Response.status(Status.OK).entity(users).build();
    	}
    	// if users == null, the data reading from shopdata.json has failed
    	return Response.status(Status.INTERNAL_SERVER_ERROR).entity("[]").build();
    }
    
    @GET
    @Path("/{userId}")
    public Response getUser(@PathParam("userId") String userId) {
    	User user = database.getUser(userId);
    	if (user != null) {
			return Response.status(Status.OK).entity(user).build();
    	}
    	// if user == null, user with id was not found (or data reading from shopdata.json has failed)
    	return Response.status(Status.BAD_REQUEST).entity("{}").build();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(User user) {

    	user.addLink("/webshopREST/webapi/users/" + user.getId(),  "self");
    	user.addLink("/webshopREST/webapi/users/" + user.getId() + "/orders", "orders");
    	
    	User addedUser = database.addUser(user);
    	if (addedUser != null) {
    		return Response.status(Status.CREATED).entity(addedUser).build();
    	}
    	// if user == null, the data reading from shopdata.json has failed
    	return Response.status(Status.INTERNAL_SERVER_ERROR).entity("{}").build();
    }
    
    @PUT
    @Path("/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response replaceUser(@PathParam("userId") String userId, User user) {
    	
    	user.addLink("/webshopREST/webapi/users/" + user.getId(),  "self");
    	user.addLink("/webshopREST/webapi/users/" + user.getId() + "/orders", "orders");
    	
    	User replacedUser = database.replaceUser(userId, user);
    	if (replacedUser != null) {
			return Response.status(Status.OK).entity(replacedUser).build();
    	}
    	// if user == null, user with id was not found (or data reading from shopdata.json has failed)
    	return Response.status(Status.BAD_REQUEST).entity("{}").build();
    }
    
    @DELETE
    @Path("/{userId}")
    public Response removeUser(@PathParam("userId") String userId) {
    	if (database.removeUser(userId)) {
			return Response.status(Status.OK).entity("{}").build();
    	}
    	// if database.removeUser() == false, user with id was not found (or data reading from shopdata.json has failed)
    	return Response.status(Status.BAD_REQUEST).entity("{}").build();
    }
}
