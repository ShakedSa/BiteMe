package Config;

public class Order {
	private String orderNumber, restaurant, phoneNumber, orderAddress, orderTime, typeOfOrder;

	public Order(String orderNumber, String restaurant, String orderTime, String phoneNumber,
			String typeOfOrder, String orderAddress) {
		this.orderNumber = orderNumber;
		this.restaurant = restaurant;
		this.orderTime = orderTime;
		this.phoneNumber = phoneNumber;
		this.typeOfOrder = typeOfOrder;
		this.orderAddress = orderAddress;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(String restaurant) {
		this.restaurant = restaurant;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getOrderAddress() {
		return orderAddress;
	}

	public void setOrderAddress(String orderAddress) {
		this.orderAddress = orderAddress;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getTypeOfOrder() {
		return typeOfOrder;
	}

	public void setTypeOfOrder(String typeOfOrder) {
		this.typeOfOrder = typeOfOrder;
	}
	
	public String toString() {
		return String.format("%s %s %s %s %s %s\n", orderNumber,restaurant, orderTime, phoneNumber,typeOfOrder,orderAddress);
	}
}