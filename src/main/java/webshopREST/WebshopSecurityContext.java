package webshopREST;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

import types.User;

public class WebshopSecurityContext implements SecurityContext {
	private User user;
	private String scheme;
	
	public WebshopSecurityContext(User user, String scheme) {
		this.user = user;
		this.scheme = scheme;
	}
	
	@Override
	public Principal getUserPrincipal() {
		return this.user;
	}
	
	@Override
	public boolean isUserInRole(String role) {
		System.out.println("ollaan isUserInRole, rooli " + role);
		if (user.getRoles() != null) {
			return user.getRoles().contains(role);
		}
		return false;
	}
	
	@Override
	public boolean isSecure() {
		return "https".equals(this.scheme);
	}
	
	@Override
	public String getAuthenticationScheme() {
		return SecurityContext.BASIC_AUTH;
	}
}