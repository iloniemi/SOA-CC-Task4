package types;

import java.util.List;

/**
 * This class is only for helping Jackson object generation
 */
public class ShopData {
	private List<Customer> customers;
	private List<ProductCategory> productCategories;
	
	public ShopData() {}
	
	public ShopData(List<Customer> customers, List<ProductCategory> productCategories) {
		this.customers = customers;
		this.productCategories = productCategories;
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
