package Entities;

import java.util.ArrayList;

/**
 * @author Eden test
 * Product class, stores :
 * int productID, String type, String dishName, ArrayList<Component> components, float price
 */

public class Product {
	
	private int productID;
	private String type;
	private String dishName;
	private ArrayList<Component> components;
	private float price;
	
	/**
	 * Product Constructor
	 * @param productID
	 * @param type
	 * @param dishName
	 * @param components
	 * @param price
	 */
	public Product(int productID, String type, String dishName, ArrayList<Component> components, float price) {
		super();
		this.productID = productID;
		this.type = type;
		this.dishName = dishName;
		this.components = components;
		this.price = price;
	}
	
	
	//GETTERS/SETTERS:
	/**
	 * @return the productID
	 */
	public int getProductID() {
		return productID;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the dishName
	 */
	public String getDishName() {
		return dishName;
	}

	/**
	 * @return the components
	 */
	public ArrayList<Component> getComponents() {
		return components;
	}

	/**
	 * @return the price
	 */
	public float getPrice() {
		return price;
	}

	/**
	 * @param productID the productID to set
	 */
	public void setProductID(int productID) {
		this.productID = productID;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @param dishName the dishName to set
	 */
	public void setDishName(String dishName) {
		this.dishName = dishName;
	}

	/**
	 * @param components the components to set
	 */
	public void setComponents(ArrayList<Component> components) {
		this.components = components;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(float price) {
		this.price = price;
	}
	
	
}
