package webshopREST;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.server.internal.process.MappableException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import types.ErrorMessage;
import types.User;
import webshopREST.database.SingletonDatabase;
import webshopREST.database.TokenService;
import webshopREST.errors.DataNotFoundException;
import webshopREST.errors.InvalidInputException;

import javax.annotation.Priority;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.json.JsonObject;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class ContainerAuthFilter implements ContainerRequestFilter {
	private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
	private static final String AUTHORIZATION_HEADER_PREFIX_BASIC = "Basic ";
	private static final String AUTHORIZATION_HEADER_PREFIX_JWT = "Bearer ";
	private final SingletonDatabase database = SingletonDatabase.getDatabase();

	private static final ErrorMessage FORBIDDEN_ErrMESSAGE = new ErrorMessage("Access blockedfor all users !!!", 403,
			"http://myDocs.org");
	private static final ErrorMessage UNAUTHORIZED_ErrMESSAGE = new ErrorMessage("User cannot access the resource.",
			401, "http://myDocs.org");

	@Context
	private ResourceInfo resourceInfo;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		System.out.println("filter-metodissa");

		User user = null;
		List<String> authHeader = requestContext.getHeaders().get(AUTHORIZATION_HEADER_KEY);
		String authToken = null;
		if (authHeader != null && authHeader.size() > 0) {
			authToken = authHeader.get(0);
		}

		// Basic authentication
		if (authToken != null && authToken.startsWith(AUTHORIZATION_HEADER_PREFIX_BASIC)) {
			authToken = authToken.replaceFirst(AUTHORIZATION_HEADER_PREFIX_BASIC, "");
			String decodedString = new String(Base64.getDecoder().decode(authToken));
			System.out.println("decoded: " + decodedString);
			if (decodedString.length() == 1) {
				throw new InvalidInputException("Login information missing");
			}
			StringTokenizer tokenizer = new StringTokenizer(decodedString, ":");
			// if (tokenizer.toString().contains(" : "))
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
		
		//TODO: tyhjät tokenit pääsevät autorisoinnin läpi, tämä on nopea fixi
		System.out.println(authToken);
		if (authToken.trim().equals("Bearer")) throw new InvalidInputException ("Empty tokens not allowed");
		System.out.println(authToken);
		
		
		// JWT authentication
		if (authToken != null && authToken.startsWith(AUTHORIZATION_HEADER_PREFIX_JWT)) {
			System.out.println("Löytyi auth token: " + authToken);

			authToken = authToken.replaceFirst(AUTHORIZATION_HEADER_PREFIX_JWT, "");

			String[] parts = authToken.split("\\.");

			// TODO: Wrongly formatted token can cause Internal exception
			String headers = (new String(Base64.getUrlDecoder().decode(parts[0])));
			ObjectNode node = new ObjectMapper().readValue(headers, ObjectNode.class);

			if (!(node.get("alg").asText().equals("HS256") && node.get("typ").asText().equals("JWT"))) {
				throw new InvalidInputException("Given token is not of a supported type (JWT with HS256 hashing)");

			}
			String payload = (new String(Base64.getUrlDecoder().decode(parts[1])));
			String signature = (new String(Base64.getUrlDecoder().decode(parts[2])));

			// Check the expiration
			node = new ObjectMapper().readValue(payload, ObjectNode.class);
			Long now = new Date().getTime();
			Long expiration = node.get("exp").asLong();
			if (expiration < now) {
				throw new InvalidInputException("The given token has expired " + ((now - expiration) / 1000)
						+ " seconds ago. Request a new token.");
			}
			
			//Check if the token has been signed by the service
			Boolean authorized = false;
			try {
				authorized = TokenService.checkTokenValidity(payload, signature);
			} catch (InvalidKeyException | JsonProcessingException | UnsupportedEncodingException
					| NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			if (!authorized) {
				throw new InvalidInputException("Given token has not been authorized by the service");
			}
			
			//Autorisointi
			user = database.getUser(node.get("sub").asText());
			System.out.println("filter loop, user löydetty " + user.getName());
			String scheme = requestContext.getUriInfo().getRequestUri().getScheme();
			requestContext.setSecurityContext(new WebshopSecurityContext(user, scheme));
			System.out.println("");

		}

		// Authorization
		// Requested method annotations are checked first
		Method resMethod = resourceInfo.getResourceMethod();

		if (resMethod.isAnnotationPresent(PermitAll.class))
			return;

		if (resMethod.isAnnotationPresent(DenyAll.class)) {
			Response response = Response.status(Response.Status.FORBIDDEN).entity(FORBIDDEN_ErrMESSAGE).build();
			requestContext.abortWith(response);
		}

		if (resMethod.isAnnotationPresent(RolesAllowed.class)) {
			// If user has role that is allowed in method annotation, filter lets request
			// pass
			if (rolesMatched(user, resMethod.getAnnotation(RolesAllowed.class)))
				return;

			Response response = Response.status(Response.Status.UNAUTHORIZED).entity(UNAUTHORIZED_ErrMESSAGE).build();
			requestContext.abortWith(response);
		}

		// Annotations for the requested resource's class are checked next
		Class<?> resClass = resourceInfo.getResourceClass();
		if (resClass.isAnnotationPresent(DenyAll.class)) {
			Response response = Response.status(Response.Status.FORBIDDEN).entity(FORBIDDEN_ErrMESSAGE).build();
			requestContext.abortWith(response);
		}

		if (resClass.isAnnotationPresent(RolesAllowed.class)) {
			if (rolesMatched(user, resClass.getAnnotation(RolesAllowed.class)))
				return;

			Response response = Response.status(Response.Status.UNAUTHORIZED).entity(UNAUTHORIZED_ErrMESSAGE).build();
			requestContext.abortWith(response);
		}

	}

	/**
	 * Checks if given user has a role listed in given RolesAllowed annotation.
	 * Returns false if user is null.
	 * 
	 * @param user       whose roles will be checked
	 * @param annotation that contains roles
	 * @return did user have a role contained in allowed roles
	 */
	private boolean rolesMatched(User user, RolesAllowed annotation) {
		if (user == null)
			return false;

		// Ruma, mutta halusin kayttaa varmuuden vuoksi IgnoreCasea.
		for (String role : annotation.value()) {
			for (String usersRole : user.getRoles()) {
				if (usersRole.equalsIgnoreCase(role)) {
					return true;
				}
			}
		}
		return false;
	}

}