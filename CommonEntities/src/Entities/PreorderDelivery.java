package Entities;

public class PreorderDelivery extends Delivery {

	private String deliveryTime;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2785999044405825850L;

	public PreorderDelivery(int deliveryNumber, String orderAddress
			, float orderPrice, float discount, String deliveryTime) {
		super(deliveryNumber, orderAddress, orderPrice, discount);
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
	
	

}
