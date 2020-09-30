package types;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import webshopREST.errors.DataNotFoundException;

@XmlRootElement
public class Customer {
	// base value for newly created ids
	private static int count = 1000;
	
	private String id = count++ + "";
	private String firstName;
	private String lastName;
	private List<Order> orders;
	
	private List<Link> links = new ArrayList<Link>();
	

	public Customer() {}
	
	public Customer(String firstName, String lastName, List<Order> orders) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.orders = orders;
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
	 * @return the id
	 */
	public String getId() {
		return id;
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
	 * @return the orders
	 */
	public List<Order> getOrders() {
		return orders;
	}
	/**
	 * @param orders the orders to set
	 */
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	/** Adds a new order
	 * @param order to be added
	 * @return
	 */
	public Order addOrder(Order order) {
		this.orders.add(order);
		return order;
	}

	/** Finds an order with specified id or throws an exception if not found
	 * @param orderId id to search with
	 * @return found order
	 */
	public Order getOrder(String orderId) {
		Order found = this.orders.stream().filter(ord -> ord.getId().equals(orderId))
		.findFirst().orElse(null);
		
		if (found == null) {
			throw new DataNotFoundException("Order with id " + orderId + "not found.");
		}
		
		return found;
	}
}
