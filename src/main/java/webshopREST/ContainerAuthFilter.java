package webshopREST;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import database.SingletonDatabase;
import types.User;

import javax.annotation.Priority;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class ContainerAuthFilter implements ContainerRequestFilter {
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		// FIXME: Tämä on vain kopioitu luentodioista
		/*
		private final SingletonDatabase database = SingletonDatabase.getDatabase();
		User user = null;
		List<String> authHeader = requestContext.getHeaders().get(AUTHORIZATION_HEADER_KEY);
		if (authHeader != null && authHeader.size() > 0) {
			if (UserService.userCredentialExists(username, password)) {
				user = UserService.getUser(username);
				String scheme = requestContext.getUriInfo().getRequestUri().getScheme();
				requestContext.setSecurityContext(newMyCustomSecurityContext(user, scheme));
			}
		}
		
		if ((requestContext.getUriInfo().getPath().contains(SECURED_URL_PREFIX)) || (requestContext.getMethod().equals("DELETE"))) {
			if (user != null) return;
			ErrorMessage errorMessage = new ErrorMessage("User cannot access the resource.", 401, "http://myDocs.org");
			Response unauthorizedStatus = Response.status(Response.Status.UNAUTHORIZED).entity(errorMessage).build();
			requestContext.abortWith(unauthorizedStatus);
		}
		*/
	}
}