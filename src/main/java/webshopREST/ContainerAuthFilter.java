package webshopREST;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Base64;
import java.util.List;
import java.util.StringTokenizer;

import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import types.ErrorMessage;
import types.User;
import webshopREST.database.SingletonDatabase;
import webshopREST.errors.DataNotFoundException;
import webshopREST.errors.InvalidInputException;

import javax.annotation.Priority;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class ContainerAuthFilter implements ContainerRequestFilter {
	private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
	private static final String AUTHORIZATION_HEADER_PREFIX_BASIC = "Basic ";
	private static final String AUTHORIZATION_HEADER_PREFIX_JWT = "Bearer ";
	private final SingletonDatabase database = SingletonDatabase.getDatabase();
	
	private static final ErrorMessage FORBIDDEN_ErrMESSAGE = new ErrorMessage("Access blockedfor all users !!!", 403, "http://myDocs.org");
	private static final ErrorMessage UNAUTHORIZED_ErrMESSAGE = new ErrorMessage("User cannot access the resource.", 401, "http://myDocs.org");
	
	
	@Context private ResourceInfo resourceInfo;
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		System.out.println("filter-metodissa");

		User user = null;
		List<String> authHeader = requestContext.getHeaders().get(AUTHORIZATION_HEADER_KEY);
		String authToken = null;
		if (authHeader != null && authHeader.size() > 0) {
			authToken = authHeader.get(0);
		}	
		
		//Basic authentication
		if (authToken != null && authToken.startsWith(AUTHORIZATION_HEADER_PREFIX_BASIC)) {	
			authToken = authToken.replaceFirst(AUTHORIZATION_HEADER_PREFIX_BASIC, "");
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
				System.out.println("");
			}
		}
		
		//JWT authentication
		if (authToken != null && authToken.startsWith(AUTHORIZATION_HEADER_PREFIX_JWT)) {	
			authToken = authToken.replaceFirst(AUTHORIZATION_HEADER_PREFIX_JWT, "");
			//TODO:tokenin dekoodaus ja vahvistus, userin ja securitycontextin asettaminen.
		}
		
		
		//Authorization
		//Requested method annotations are checked first
		Method resMethod = resourceInfo.getResourceMethod();
		
		if(resMethod.isAnnotationPresent(PermitAll.class)) return;
		
		if(resMethod.isAnnotationPresent(DenyAll.class)){
		    Response response = Response.status(Response.Status.FORBIDDEN).entity(FORBIDDEN_ErrMESSAGE).build();
		    requestContext.abortWith(response);
		}

		if(resMethod.isAnnotationPresent(RolesAllowed.class)){
			//If user has role that is allowed in method annotation, filter lets request pass
		    if(rolesMatched(user,resMethod.getAnnotation(RolesAllowed.class))) return;
		    
		    Response response = Response.status(Response.Status.UNAUTHORIZED).entity(UNAUTHORIZED_ErrMESSAGE).build();
		    requestContext.abortWith(response);
		}
		
		//Annotations for the requested resource's class are checked next
		Class<?> resClass = resourceInfo.getResourceClass();
		if(resClass.isAnnotationPresent(DenyAll.class)){
		    Response response = Response.status(Response.Status.FORBIDDEN).entity(FORBIDDEN_ErrMESSAGE).build();
		    requestContext.abortWith(response);
		}

		if(resClass.isAnnotationPresent(RolesAllowed.class)){
		    if(rolesMatched(user,resClass.getAnnotation(RolesAllowed.class))) return;
		    
		    Response response = Response.status(Response.Status.UNAUTHORIZED).entity(UNAUTHORIZED_ErrMESSAGE).build();
		    requestContext.abortWith(response);
		}
		
	}
	
	
	/** Checks if given user has a role listed in given RolesAllowed annotation.
	 * Returns false if user is null.
	 * @param user whose roles will be checked
	 * @param annotation that contains roles
	 * @return did user have a role contained in allowed roles
	 */
	private boolean rolesMatched(User user, RolesAllowed annotation) {
		if (user == null) return false;
		
		//Ruma, mutta halusin kayttaa varmuuden vuoksi IgnoreCasea.
		boolean rolesMatch = false;
		for (String role: annotation.value()) {
			for (String usersRole: user.getRoles()) {
				if (usersRole.equalsIgnoreCase(role)) {
					rolesMatch = true;
					break;
				}
			}
		}
		return rolesMatch;
	}
				


}