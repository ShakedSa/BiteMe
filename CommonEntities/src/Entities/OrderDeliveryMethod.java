package Entities;

import java.io.Serializable;
import java.util.ArrayList;

import Enums.TypeOfOrder;

public class OrderDeliveryMethod implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5415006015327877176L;
	private Order order;
	private Delivery delivery;
	private TypeOfOrder typeOfOrder;
	private Customer customerInfo;
	private float finalPrice;
	private boolean isRefundSelected = false;
	private float refundForBreakDown = 0;

	public OrderDeliveryMethod(Order order, Delivery delivery, TypeOfOrder typeOfOrder, Customer customerInfo) {
		super();
		this.order = order;
		this.delivery = delivery;
		this.typeOfOrder = typeOfOrder;
		this.customerInfo = customerInfo;
	}

	/**
	 * @return the order
	 */
	public Order getOrder() {
		return order;
	}

	/**
	 * @return the delivery
	 */
	public Delivery getDelivery() {
		return delivery;
	}

	/**
	 * @return the typeOfOrder
	 */
	public TypeOfOrder getTypeOfOrder() {
		return typeOfOrder;
	}

	/**
	 * @return the customerInfo
	 */
	public Customer getCustomerInfo() {
		return customerInfo;
	}

	/**
	 * @param orderNumber the order to set
	 */
	public void setOrderr(Order order) {
		this.order = order;
	}

	/**
	 * @param deliveryNumber the delivery to set
	 */
	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}

	/**
	 * @param typeOfOrder the typeOfOrder to set
	 */
	public void setTypeOfOrder(TypeOfOrder typeOfOrder) {
		this.typeOfOrder = typeOfOrder;
	}

	/**
	 * @param customerInfo the customerInfo to set
	 */
	public void setCustomerInfo(Customer customerInfo) {
		this.customerInfo = customerInfo;
	}

	/**
	 * @return the finalPrice
	 */
	public float getFinalPrice() {
		return finalPrice;
	}

	/**
	 * @param finalPrice the finalPrice to set
	 */
	public void setFinalPrice(float finalPrice) {
		this.finalPrice = finalPrice;
	}
	
	

	/**
	 * @return the isRefundSelected
	 */
	public boolean isRefundSelected() {
		return isRefundSelected;
	}

	/**
	 * @param isRefundSelected the isRefundSelected to set
	 */
	public void setRefundSelected(boolean isRefundSelected) {
		this.isRefundSelected = isRefundSelected;
	}

	/**
	 * @return the refundForBreakDown
	 */
	public float getRefundForBreakDown() {
		return refundForBreakDown;
	}

	/**
	 * @param refundForBreakDown the refundForBreakDown to set
	 */
	public void setRefundForBreakDown(float refundForBreakDown) {
		this.refundForBreakDown = refundForBreakDown;
	}

	public void calculateFinalPrice() {
		float price = 0;
		price += order.getOrderPrice();
		switch (typeOfOrder) {
		case preorderDelivery:
			delivery.setDiscount(price * (float) 0.1);
			price += delivery.getDelievryPrice() - delivery.getDiscount();
			break;
		case BasicDelivery:
			price += delivery.getDelievryPrice();
			break;
		case sharedDelivery:
			float deliveryPrice = delivery.getDelievryPrice();
			int amount = ((SharedDelivery) delivery).getAmountOfPeople();
			System.out.println(amount);
			int deliveryDiscount = (amount - 1) * 5;
			System.out.println(deliveryDiscount);
			if(deliveryDiscount > 10) {
				delivery.setDiscount(10);
			}else {
				delivery.setDiscount(deliveryDiscount);
			}
			deliveryPrice -= delivery.getDiscount(); // Delivery price for each person in the order.
			deliveryPrice *= amount; // Total delivery price in order.
			delivery.setDeliveryPrice(deliveryPrice);
			delivery.setDiscount(delivery.getDiscount() * amount);
			price += deliveryPrice;
			break;
		default:
			break;
		}
		setFinalPrice(price);
	}

	public String toString() {
		StringBuilder b = new StringBuilder("Delivery Type: " + typeOfOrder + "\n");
		switch (typeOfOrder) {
		case preorderDelivery:
		case BasicDelivery:
		case sharedDelivery:
			b.append(delivery.toString());
			break;
		case takeaway:
			b.append("Take away");
			break;
		default:
			break;
		}
		b.append("\nFinal Price: " + finalPrice);
		return b.toString();
	}

}
