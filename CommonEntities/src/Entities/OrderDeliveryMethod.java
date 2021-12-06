package Entities;
import java.io.Serializable;

import Enums.TypeOfOrder;

public class OrderDeliveryMethod implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5415006015327877176L;
	private Order OrderNumber;
	private Delivery deliveryNumber;
	private TypeOfOrder typeOfOrder;
	private Customer customerInfo;
	
	
	public OrderDeliveryMethod(Order orderNumber, Delivery deliveryNumber, TypeOfOrder typeOfOrder, Customer customerInfo) {
		super();
		OrderNumber = orderNumber;
		this.deliveryNumber = deliveryNumber;
		this.typeOfOrder = typeOfOrder;
		this.customerInfo = customerInfo;
	}
	
	
	/**
	 * @return the orderNumber
	 */
	public Order getOrderNumber() {
		return OrderNumber;
	}
	/**
	 * @return the deliveryNumber
	 */
	public Delivery getDeliveryNumber() {
		return deliveryNumber;
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
	 * @param orderNumber the orderNumber to set
	 */
	public void setOrderNumber(Order orderNumber) {
		OrderNumber = orderNumber;
	}
	/**
	 * @param deliveryNumber the deliveryNumber to set
	 */
	public void setDeliveryNumber(Delivery deliveryNumber) {
		this.deliveryNumber = deliveryNumber;
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
	
	
	
}
