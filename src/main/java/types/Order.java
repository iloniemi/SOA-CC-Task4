package types;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Order {
	// base value for newly created ids
	private static int count = 1000;

	private String id = count++ + "";
	private String deliveryAddress;

	private List<Link> links = new ArrayList<Link>();

	public Order() {
	}

	public Order(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
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
	 * @return the deliveryAddress
	 */
	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	/**
	 * @param deliveryAddress the deliveryAddress to set
	 */
	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	/**
	 * Updates the order with given data
	 * 
	 * @param order containing new data
	 * @return order with updated information
	 */
	public Order update(Order order) {
		this.deliveryAddress = order.getDeliveryAddress();
		return this;
	}

}
