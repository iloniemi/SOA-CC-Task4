package types;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User implements Principal {	
	private String id;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private List<String> roles;
	private List<Link> links = new ArrayList<Link>();
	

	public User() {}
	
	public User(String id, String password, String firstName, String lastName, String email, List<String> roles) {
		this.id = id;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.roles = roles;
	}
	
	/**
	 * 
	 * @param url the link
	 * @param rel what the link relates to
	 */
	public void addLink(String url, String rel) {
		Link link = new Link();
		link.setHref(url);
		link.setRel(rel);
		links.add(link);
	}
	
	
	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return this.getId();
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the id
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param id the id to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the roles
	 */
	public List<String> getRoles() {
		return roles;
	}
	/**
	 * @param roles the roles to set
	 */
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
}
