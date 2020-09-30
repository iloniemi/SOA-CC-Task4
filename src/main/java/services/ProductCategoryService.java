package services;

import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import types.ProductCategory;
import types.ShopData;
import webshopREST.errors.DataNotFoundException;
import webshopREST.errors.HandlingException;

/**
 * Class for Product category services
 *
 */
public class ProductCategoryService {
	
	private static List<ProductCategory> productCategories;
	
	public ProductCategoryService () {
		if (productCategories == null) {
			try {
				// We use easy-to-use Jackson library for parsing shopdata.json straight to Java types 
				ObjectMapper mapper = new ObjectMapper();
				InputStream is = ShopData.class.getResourceAsStream("/shopdata.json");
				
				ShopData shopData = mapper.readValue(is, ShopData.class);
				
				// For nested orders it would be like orders = shopData.getCustomers().get(customerId).getOrders();
				productCategories = shopData.getProductCategories();
			} catch (Exception e) {
				e.printStackTrace();
				productCategories = null;
			}
		}
	}
	
	/**
	 * Function returns the list of product categories
	 * @return list of product categories or throws an exception if there was an error
	 */
	public List<ProductCategory> getProductCategories() {
		if (productCategories == null) {
			throw new HandlingException("Could not get categories");
		}
		return productCategories;
	}
	
	/**
	 * Function returns the requested product category if found
	 * @param categoryId id to find
	 * @return ProductCategory with categoryId, exception if not found (or if there was an error)
	 */
	public ProductCategory getCategory(String categoryId) {
		if (productCategories == null) {
			throw new DataNotFoundException("Category with id "+ categoryId + " not found");
		}
 
			return productCategories.stream().filter(category -> category.getId().equals(categoryId)).findFirst().orElse(null);
	}
	
	/**
	 * Function to add a new category
	 * @param category category object to be added
	 * @return Added category object (or exception if there was an error)
	 */
	public ProductCategory addCategory(ProductCategory newCategory) {
		if (productCategories == null) {
			throw new DataNotFoundException("Problem adding category "+ newCategory);
		}
		
		productCategories.add(newCategory);
		return newCategory;

	}
	
	/**
	 * Replace/edit a category
	 * @param cateogryId id to be replaced
	 * @param newCateogry category that replaces old category
	 * @return replaced category, throws exception if not found (or if there was an error)
	 */
	public ProductCategory replaceCategory(String categoryId, ProductCategory newCategory) {
		ProductCategory category = this.getCategory(categoryId);
		
		if (category == null) {
			throw new HandlingException("Category with id "+ categoryId + " could not be replaced");
		}
		
			category.setName(newCategory.getName());
			category.setProducts(newCategory.getProducts());
			return category;
	}
	
	/**
	 * Function to remove product categories
	 * @param categoryId id to be removed
	 * @return  true if found and succeeded
	 */
	public boolean removeCategory(String categoryId) {
		if (productCategories == null) {
			throw new HandlingException("Category with id "+ categoryId + " not removed");
		}
			return productCategories.removeIf(customer -> customer.getId().equals(categoryId));
		
	}
	
	
}
