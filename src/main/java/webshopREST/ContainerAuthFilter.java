package webshopREST;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;
import java.util.List;
import java.util.StringTokenizer;

import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import types.ErrorMessage;
import types.User;
import webshopREST.database.SingletonDatabase;
import webshopREST.errors.DataNotFoundException;
import webshopREST.errors.InvalidInputException;

import javax.annotation.Priority;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class ContainerAuthFilter implements ContainerRequestFilter {
	private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
	private static final String AUTHORIZATION_HEADER_PREFIX = "Basic ";
	private static final String SECURED_URL_PREFIX= "secured";
	private final SingletonDatabase database = SingletonDatabase.getDatabase();
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		// FIXME: ei vieläkään toimi
		System.out.println("filter-metodissa");

		User user = null;
		List<String> authHeader = requestContext.getHeaders().get(AUTHORIZATION_HEADER_KEY);
		if (authHeader != null && authHeader.size() > 0) {
			String authToken = authHeader.get(0);
			authToken = authToken.replaceFirst(AUTHORIZATION_HEADER_PREFIX, "");
			String decodedString = new String(Base64.getDecoder().decode(authToken));
			System.out.println("decoded: " + decodedString);
			if (decodedString.length() == 1) {
				throw new InvalidInputException("Login information missing");
			}
			StringTokenizer tokenizer = new StringTokenizer(decodedString, ":");
			//if (tokenizer.toString().contains(" : "))
			String username = tokenizer.nextToken();
			String password = tokenizer.nextToken();
			if (database.userCredentialExists(username, password)) {
				user = database.getUser(username);
				System.out.println("filter loop, user löydetty " + user.getName());
				String scheme = requestContext.getUriInfo().getRequestUri().getScheme();
				requestContext.setSecurityContext(new WebshopSecurityContext(user, scheme));
			}
		}

		if ((requestContext.getUriInfo().getPath().contains(SECURED_URL_PREFIX)) || (requestContext.getMethod().equals("GET"))) {
			if (user != null) return;
			ErrorMessage errorMessage = new ErrorMessage("User cannot access the resource.", 401, "http://myDocs.org");
			Response unauthorizedStatus = Response.status(Response.Status.UNAUTHORIZED).entity(errorMessage).build();
			requestContext.abortWith(unauthorizedStatus);
		}
		
	}


}