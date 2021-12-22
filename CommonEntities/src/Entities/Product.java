package Entities;

import java.io.Serializable;
import java.util.ArrayList;
import Enums.TypeOfProduct;

import Enums.Size;

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
	private TypeOfProduct type;
	private String dishName;
	private ArrayList<Component> components;
	private float price;
	private String description;
	private int amount = 1;

	/**
	 * @param restaurantName
	 * @param type
	 * @param dishName
	 * @param components
	 * @param price
	 * @param description
	 */
	public Product(String restaurantName, TypeOfProduct type, String dishName, ArrayList<Component> components,
			float price, String description) {
		super();
		this.restaurantName = restaurantName;
		this.type = type;
		this.dishName = dishName;
		this.components = components;
		this.price = price;
		this.description = description;
	}

	/**
	 * @return type
	 */
	public TypeOfProduct getType() {
		return type;
	}

	/**
	 * @return dishName
	 */
	public String getDishName() {
		return dishName;
	}

	/**
	 * @return components
	 */
	public ArrayList<Component> getComponents() {
		return components;
	}

	/**
	 * @return price
	 */
	public float getPrice() {
		return price;
	}

	/**
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return restaurantName
	 */
	public String getRestaurantName() {
		return restaurantName;
	}

	/**
	 * @param type
	 */
	public void setType(TypeOfProduct type) {
		this.type = type;
	}

	/**
	 * @param dishName
	 */
	public void setDishName(String dishName) {
		this.dishName = dishName;
	}

	/**
	 * @param components
	 */
	public void setComponents(ArrayList<Component> components) {
		this.components = components;
	}

	/**
	 * @param price
	 */
	public void setPrice(float price) {
		this.price = price;
	}

	/**
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		if (components != null)
			return dishName + " <" + price + "¤> " + "components: " + components;
		return dishName + "<" + price + ">";
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Product)) {
			return false;
		}
		Product other = (Product)obj;
		return dishName.equals(other.dishName) && restaurantName.equals(other.restaurantName);
	}
}
