package Entities;

import java.io.Serializable;

/**
 * @author Michael
 *
 */
public class Delivery implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8991119981743155411L;
	private String orderAddress;
	private String firstName, lastName;
	private String phoneNumber;
	private float deliveryPrice;
	private float discount;
	public static final float DeliveryPrice = 25;

	public Delivery(String orderAddress, String firstName, String lastName, String phoneNumber, float deliveryPrice,
			float discount) {
		super();
		this.orderAddress = orderAddress;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.deliveryPrice = deliveryPrice;
		this.discount = discount;
	}

	/**
	 * @return the orderAddress
	 */
	public String getOrderAddress() {
		return orderAddress;
	}

	/**
	 * @return the deliveryPrice
	 */
	public float getDelievryPrice() {
		return deliveryPrice;
	}

	/**
	 * @return the discount
	 */
	public float getDiscount() {
		return discount;
	}

	/**
	 * @param orderAddress the orderAddress to set
	 */
	public void setOrderAddress(String orderAddress) {
		this.orderAddress = orderAddress;
	}

	/**
	 * @param deliveryPrice the orderPrice to set
	 */
	public void setDeliveryPrice(float deliveryPrice) {
		this.deliveryPrice = deliveryPrice;
	}

	/**
	 * @param discount the discount to set
	 */
	public void setDiscount(float discount) {
		this.discount = discount;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String toString() {
		StringBuilder b = new StringBuilder("Delivery Details:\n");
		if (orderAddress != null) {
			b.append("Address: " + orderAddress + "\n");
		}
		b.append("Delivery Price: " + deliveryPrice + "\u20AA\n");
		b.append("Discount on your order: " + discount + "\u20AA");
		return b.toString();
	}

}
