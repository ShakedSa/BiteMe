package Entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Eden test Product class, stores : int productID, String type, String
 *         dishName, ArrayList<Component> components, float price
 */

public class Product implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3079554028160660848L;
	private String restaurantName;
	private String type;
	private String dishName;
	private ArrayList<Component> components;
	private float price;
	private String description;

	/**
	 * @param restaurantName
	 * @param type
	 * @param dishName
	 * @param components
	 * @param price
	 * @param description
	 */
	public Product(String restaurantName, String type, String dishName, ArrayList<Component> components, float price,
			String description) {
		super();
		this.restaurantName = restaurantName;
		this.type = type;
		this.dishName = dishName;
		this.components = components;
		this.price = price;
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public String getDishName() {
		return dishName;
	}

	public ArrayList<Component> getComponents() {
		return components;
	}

	public float getPrice() {
		return price;
	}

	public String getDescription() {
		return description;
	}

	public String getRestaurantName() {
		return restaurantName;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setDishName(String dishName) {
		this.dishName = dishName;
	}

	public void setComponents(ArrayList<Component> components) {
		this.components = components;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		if (components != null)
			return dishName + "<" + price + ">" + "components: " + components;
		return dishName + "<" + price + ">";
	}

}
