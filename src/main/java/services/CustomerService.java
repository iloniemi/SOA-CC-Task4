package services;

import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import types.Customer;
import types.ShopData;

public class CustomerService {
	// customer is class variable: this is simpliest way to persist data for each session
	private static List<Customer> customers;
	
	public CustomerService() {
		if (customers == null) {
			try {
				// We use easy-to-use Jackson library for parsing shopdata.json straight to Java types 
				ObjectMapper mapper = new ObjectMapper();
				InputStream is = ShopData.class.getResourceAsStream("/shopdata.json");
				
				ShopData shopData = mapper.readValue(is, ShopData.class);
				
				// For nested orders it would be like orders = shopData.getCustomers().get(customerId).getOrders();
				customers = shopData.getCustomers();
			} catch (Exception e) {
				e.printStackTrace();
				customers = null;
			}
		}
	}
	
	/**
	 * 
	 * @return list of customers or null if there was an error
	 */
	public List<Customer> getCustomers() {
		return customers;
	}
	
	/**
	 * 
	 * @param customerId id to find
	 * @return customer with customerId, null if not found (or if there was an error)
	 */
	public Customer getCustomer(String customerId) {
		if (customers != null) {
			return customers.stream().filter(customer -> customer.getId().equals(customerId)).findFirst().orElse(null);
		}
		return null;
	}
	
	/**
	 * 
	 * @param customer customer object to be added
	 * @return Added customer object (or null if there was an error)
	 */
	public Customer addCustomer(Customer customer) {
		if (customers != null) {
			customers.add(customer);
			return customer;
		}
		return null;
	}
	
	/**
	 * 
	 * @param customerId id to be replaced
	 * @param newCustomer customer to be replaced by
	 * @return replaced customer, null if not found (or if there was an error)
	 */
	public Customer replaceCustomer(String customerId, Customer newCustomer) {
		Customer customer = this.getCustomer(customerId);
		if (customer != null) {
			customer.setFirstName(newCustomer.getFirstName());
			customer.setLastName(newCustomer.getLastName());
			customer.setOrders(newCustomer.getOrders());
			return customer;
		}
		return null;
	}
	
	/**
	 * 
	 * @param customerId id to be removed
	 * @return removed true if found and succeed
	 */
	public boolean removeCustomer(String customerId) {
		if (customers != null) {
			return customers.removeIf(customer -> customer.getId().equals(customerId));
		}
		return false;
	}
}
