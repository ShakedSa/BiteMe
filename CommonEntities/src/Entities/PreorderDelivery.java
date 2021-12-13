package Entities;

public class PreorderDelivery extends Delivery {

	private String deliveryTime;

	/**
	 * 
	 */
	private static final long serialVersionUID = 2785999044405825850L;

	public PreorderDelivery(String orderAddress, String firstName, String lastName, String phoneNumber,
			float orderPrice, float discount, String deliveryTime) {
		super(orderAddress, firstName, lastName, phoneNumber, orderPrice, discount);
		this.deliveryTime = deliveryTime;
	}

	/**
	 * @return the deliveryTime
	 */
	public String getDeliveryTime() {
		return deliveryTime;
	}

	/**
	 * @param deliveryTime the deliveryTime to set
	 */
	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public String toString() {
		if (deliveryTime != null) {
			return super.toString() + "\nDelivery Scheduled to: " + deliveryTime;
		}
		return super.toString();
	}
}
