package Config;

/**
 * Representing a single order from the db.
 * 
 * @author Aviel Malayev
 * @author Natali Krief
 * @author Michael Ben Israel
 * @author Eden Ben Abu
 * @author Shaked Sabag
 * @version November 2021 (1.0)
 */
public class Order {
	private String orderNumber, restaurant, phoneNumber, orderAddress, orderTime, typeOfOrder;

	/**
	 * Constructor. Creating a new order.
	 * 
	 * @param orderNumber
	 * @param restaurant
	 * @param orderTime
	 * @param phoneNumber
	 * @param typeOfOrder
	 * @param orderAddress
	 */
	public Order(String orderNumber, String restaurant, String orderTime, String phoneNumber, String typeOfOrder,
			String orderAddress) {
		this.orderNumber = orderNumber;
		this.restaurant = restaurant;
		this.orderTime = orderTime;
		this.phoneNumber = phoneNumber;
		this.typeOfOrder = typeOfOrder;
		this.orderAddress = orderAddress;
	}

	/**
	 * Order number getter.
	 * 
	 * @return orderNumber
	 */
	public String getOrderNumber() {
		return orderNumber;
	}

	/**
	 * Order number setter.
	 * 
	 * @param orderNumber
	 */
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	/**
	 * Restaurant getter.
	 * 
	 * @return restaurant
	 */
	public String getRestaurant() {
		return restaurant;
	}

	/**
	 * Restaurant setter.
	 * 
	 * @param restaurant
	 */
	public void setRestaurant(String restaurant) {
		this.restaurant = restaurant;
	}

	/**
	 * Phone number getter.
	 * 
	 * @return phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * Phone number setter.
	 * 
	 * @param phoneNumber
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * Address getter.
	 * 
	 * @return orderAddress
	 */
	public String getOrderAddress() {
		return orderAddress;
	}

	/**
	 * Address setter.
	 * 
	 * @param orderAddress
	 */
	public void setOrderAddress(String orderAddress) {
		this.orderAddress = orderAddress;
	}

	/**
	 * Order time getter.
	 * 
	 * @return orderTime
	 */
	public String getOrderTime() {
		return orderTime;
	}

	/**
	 * Order time setter.
	 * 
	 * @param orderTime
	 */
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	/**
	 * Type of order getter.
	 * 
	 * @return typeOfOrder
	 */
	public String getTypeOfOrder() {
		return typeOfOrder;
	}

	/**
	 * Type of order setter.
	 * 
	 * @param typeOfOrder
	 */
	public void setTypeOfOrder(String typeOfOrder) {
		this.typeOfOrder = typeOfOrder;
	}

	/**
	 * Overriding to string. Returning a formatted string representing this order.
	 * 
	 */
	public String toString() {
		return String.format("%s %s %s %s %s %s\n", orderNumber, restaurant, orderTime, phoneNumber, typeOfOrder,
				orderAddress);
	}
}