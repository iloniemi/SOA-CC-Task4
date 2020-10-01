package types;

import java.util.List;

/**
 * This class is only for helping Jackson object generation
 */
public class ShopData {
	private List<User> users;
	private List<Customer> customers;
	private List<ProductCategory> productCategories;
	
	public ShopData() {}
	
	public ShopData(List<User> users, List<Customer> customers, List<ProductCategory> productCategories) {
		this.users = users;
		this.customers = customers;
		this.productCategories = productCategories;
	}

	/**
	 * @return the users
	 */
	public List<User> getUsers() {
		return users;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(List<User> users) {
		this.users = users;
	}


	/**
	 * @return the customers
	 */
	public List<Customer> getCustomers() {
		return customers;
	}

	/**
	 * @param customers the customers to set
	 */
	public void setCustomers(List<Customer> customers) {
		this.customers = customers;
	}

	/**
	 * @return the productCategories
	 */
	public List<ProductCategory> getProductCategories() {
		return productCategories;
	}

	/**
	 * @param productCategories the productCategories to set
	 */
	public void setProductCategories(List<ProductCategory> productCategories) {
		this.productCategories = productCategories;
	}
	
	
}
