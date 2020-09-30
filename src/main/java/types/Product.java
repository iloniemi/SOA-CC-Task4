package types;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Product {
	// base value for newly created ids
	private static int count = 1000;
		
	private String id = count++ + "";
	private String name;
	private String manufacturer;
	private int availability;
	
	private List<Link> links = new ArrayList<Link>();
	
	public Product() {}
	
	public Product(String id, String name, String manufacturer, int availability) {
		this.name = name;
		this.manufacturer = manufacturer;
		this.availability = availability;
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the manufacturer
	 */
	public String getManufacturer() {
		return manufacturer;
	}

	/**
	 * @param manufacturer the manufacturer to set
	 */
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	/**
	 * @return the availability
	 */
	public int getAvailability() {
		return availability;
	}

	/**
	 * @param availability the availability to set
	 */
	public void setAvailability(int availability) {
		this.availability = availability;
	}

	/** Copies info from given product to this one.
	 * Except the id stays the same.
	 * @param product to copy info from
	 * @return this product
	 */
	public Product update(Product product) {
		this.setAvailability(product.getAvailability());
		this.setManufacturer(product.getManufacturer());
		this.setName(product.getName());
		return this;
	}
	
	
}
