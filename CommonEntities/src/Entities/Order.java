package Entities;

import java.io.Serializable;
import java.util.ArrayList;

import Enums.PaymentMethod;

public class Order implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8005643298429981017L;
	private int orderNumber;
	private String restaurantName;
	private ArrayList<Product> products;
	private PaymentMethod paymentMethod;
	private String OrderTime;
	private float orderPrice;
	private String orderRecieved;
	private String plannedTime;
	private String dateTime;
	private String status;

	public Order(int orderNumber, String restaurantName, ArrayList<Product> products, PaymentMethod paymentMethod,
			String orderTime, float orderPrice, String orderRecieved, String dateTime) {
		super();
		this.orderNumber = orderNumber;
		this.restaurantName = restaurantName;
		this.products = products;
		this.paymentMethod = paymentMethod;
		OrderTime = orderTime;
		this.orderPrice = orderPrice;
		this.orderRecieved = orderRecieved;
		this.dateTime = dateTime;
	}

	public Order(int orderNumber, String orderTime, String orderRecieved, String plannedTime, String status) {
		super();
		this.orderNumber = orderNumber;
		this.OrderTime = orderTime;
		this.orderRecieved = orderRecieved;
		this.plannedTime = plannedTime;
		this.status = status;
	}
	
	public Order() {
		
	}

	/**
	 * @return the orderNumber
	 */
	public int getOrderNumber() {
		return orderNumber;
	}

	/**
	 * @return the restaurantName
	 */
	public String getRestaurantName() {
		return restaurantName;
	}

	/**
	 * @return the products
	 */
	public ArrayList<Product> getProducts() {
		return products;
	}

	/**
	 * @return the paymentMethod
	 */
	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	/**
	 * @return the orderTime
	 */
	public String getOrderTime() {
		return OrderTime;
	}

	/**
	 * @return the orderPrice
	 */
	public float getOrderPrice() {
		return orderPrice;
	}
	
	/**
	 * @return the plannedTime
	 */
	public String getPlannedTime() {
		return plannedTime;
	}

	/**
	 * @param plannedTime the plannedTime to set
	 */
	public void setPlannedTime(String plannedTime) {
		this.plannedTime = plannedTime;
	}

	/**
	 * @return the orderRecieved
	 */
	public String getOrderRecieved() {
		return orderRecieved;
	}

	/**
	 * @return the dateTime
	 */
	public String getDateTime() {
		return dateTime;
	}

	/**
	 * @param restaurantName the restaurantName to set
	 */
	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}

	/**
	 * @param products the products to set
	 */
	public void setProducts(ArrayList<Product> products) {
		this.products = products;
	}

	/**
	 * @param paymentMethod the paymentMethod to set
	 */
	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	/**
	 * @param ordeTime the ordeTime to set
	 */
	public void setOrderTime(String ordeTime) {
		OrderTime = ordeTime;
	}

	/**
	 * @param orderPrice the orderPrice to set
	 */
	public void setOrderPrice(float orderPrice) {
		this.orderPrice = orderPrice;
	}

	/**
	 * @param orderRecieved the orderRecieved to set
	 */
	public void setOrderRecieved(String orderRecieved) {
		this.orderRecieved = orderRecieved;
	}

	/**
	 * @param dateTime the dateTime to set
	 */
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	/**
	 * @param orderNumber the orderNumber to set
	 */
	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}
	
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	public void calculateOrderPrice() {
		if(products == null) {
			setOrderPrice(0);
			return;
		}
		float price = 0;
		for (Product p : products) {
			price += p.getPrice();
		}
		setOrderPrice(price);
	}

	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("order number: " + orderNumber);
		b.append("Order time: " + OrderTime);
		b.append("Recieved time: " + orderRecieved);
		b.append("Planned time: " + plannedTime);
		b.append("Status: " + status);
//		b.append("Order from: " + restaurantName);
//		b.append("\nList of items: " + products);
//		b.append("\nOrder time: " + dateTime);
		return b.toString();
	}

}
