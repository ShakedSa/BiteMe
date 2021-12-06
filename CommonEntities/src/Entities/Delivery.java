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
	private int deliveryNumber;
	private String orderAddress;
	private float orderPrice;
	private float discount;
	
	public Delivery(int deliveryNumber, String orderAddress,
			float orderPrice, float discount) {
		this.deliveryNumber = deliveryNumber;
		this.orderAddress = orderAddress;
		this.orderPrice = orderPrice;
		this.discount = discount;
	}
	/**
	 * @return the deliveryNumber
	 */
	public int getDeliveryNumber() {
		return deliveryNumber;
	}
	/**
	 * @return the orderAddress
	 */
	public String getOrderAddress() {
		return orderAddress;
	}
	/**
	 * @return the orderPrice
	 */
	public float getOrderPrice() {
		return orderPrice;
	}
	/**
	 * @return the discount
	 */
	public float getDiscount() {
		return discount;
	}
	/**
	 * @param deliveryNumber the deliveryNumber to set
	 */
	public void setDeliveryNumber(int deliveryNumber) {
		this.deliveryNumber = deliveryNumber;
	}
	/**
	 * @param orderAddress the orderAddress to set
	 */
	public void setOrderAddress(String orderAddress) {
		this.orderAddress = orderAddress;
	}
	/**
	 * @param orderPrice the orderPrice to set
	 */
	public void setOrderPrice(float orderPrice) {
		this.orderPrice = orderPrice;
	}
	/**
	 * @param discount the discount to set
	 */
	public void setDiscount(float discount) {
		this.discount = discount;
	}
	
	
}
