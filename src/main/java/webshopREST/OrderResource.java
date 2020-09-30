package webshopREST;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import database.SingletonDatabase;
import types.Order;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrderResource {
	private SingletonDatabase database = SingletonDatabase.getDatabase();
	
	@GET
	public Response getOrders(@PathParam("customerId") String customerId) {
    	List<Order> orders = database.getOrders(customerId);
    	if (orders != null) {
    		return Response.status(Status.OK).entity(orders).build();
    	}
    	// if orders == null, the data reading from shopdata.json has failed
    	return Response.status(Status.INTERNAL_SERVER_ERROR).entity("[]").build();
	}
	
	@GET
	@Path("/{orderId}")
	public Response getOrder(@PathParam("customerId") String customerId, @PathParam("orderId") String orderId) {
    	Order order = database.getOrder(customerId, orderId);
		return Response.status(Status.OK).entity(order).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addCustomer(@PathParam("customerId") String customerId, Order order) {
		
		order.addLink("/webshopREST/webapi/customers/" + customerId + "/orders/"+order.getId(), "self");
		
    	Order addedOrder = database.addOrder(customerId, order);
    	return Response.status(Status.CREATED).entity(addedOrder).build();
    }
	
	@PUT
	@Path("/{orderId}")
	@Consumes(MediaType.APPLICATION_JSON)
    public Response replaceOrder(@PathParam("customerId") String customerId, 
    								@PathParam("orderId") String orderId, Order order) {

		order.addLink("/webshopREST/webapi/customers/" + customerId + "/orders/"+order.getId(), "self");
		
    	Order replacedOrder = database.replaceOrder(customerId, orderId, order);
    	return Response.status(Status.OK).entity(replacedOrder).build();
	}
	
	@DELETE
	@Path("/{orderId}")
	public Response deleteOrder(@PathParam("customerId") String customerId, @PathParam("orderId") String orderId) {
    	if (database.removeOrder(customerId, orderId)) {
			return Response.status(Status.OK).entity("{}").build();
    	}
    	// if database.removeOrder() == false, order with id was not found (or data reading from shopdata.json has failed)
    	return Response.status(Status.BAD_REQUEST).entity("{}").build();
	}
}