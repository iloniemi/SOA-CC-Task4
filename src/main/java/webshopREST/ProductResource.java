package webshopREST;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import types.Product;
import webshopREST.database.SingletonDatabase;

@Path("/")
@RolesAllowed("admin")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {
	private SingletonDatabase database = SingletonDatabase.getDatabase();
	
	@GET
	@PermitAll
	public Response getProducts(@PathParam("categoryId") String categoryId, @QueryParam("manufacturer") String manufacturer) {
    	List<Product> products = database.getProducts(categoryId, manufacturer);
    	if (products != null) {
    		return Response.status(Status.OK).entity(products).build();
    	}
    	// if products == null, the data reading from shopdata.json has failed
    	return Response.status(Status.INTERNAL_SERVER_ERROR).entity("[]").build();
	}
	
	@GET
	@PermitAll
	@Path("/{productId}")
	public Response getProduct(@PathParam("categoryId") String categoryId, @PathParam("productId") String productId) {
    	Product product = database.getProduct(categoryId, productId);
		return Response.status(Status.OK).entity(product).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addCustomer(@PathParam("categoryId") String categoryId, Product product) {
		
		product.addLink("/webshopREST/webapi/productcategories/" + categoryId + "/products/" + product.getId(), "self");

		
    	Product addedProduct = database.addProduct(categoryId, product);
    	return Response.status(Status.CREATED).entity(addedProduct).build();
    }
	
	@PUT
	@Path("/{productId}")
	@Consumes(MediaType.APPLICATION_JSON)
    public Response replaceProduct(@PathParam("categoryId") String categoryId, 
    								@PathParam("productId") String productId, Product product) {
		
		product.addLink("/webshopREST/webapi/productcategories/" + categoryId + "/products/" + product.getId(), "self");
		
    	Product replacedProduct = database.replaceProduct(categoryId, productId, product);
    	return Response.status(Status.OK).entity(replacedProduct).build();
	}
	
	@DELETE
	@Path("/{productId}")
	public Response deleteProduct(@PathParam("categoryId") String categoryId, @PathParam("productId") String productId) {
    	if (database.removeProduct(categoryId, productId)) {
			return Response.status(Status.OK).entity("{}").build();
    	}
    	// if database.removeProduct() == false, product with id was not found (or data reading from shopdata.json has failed)
    	return Response.status(Status.BAD_REQUEST).entity("{}").build();
	}
}
