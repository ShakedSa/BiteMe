package Entities;

public class RobotDelivery extends Delivery {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4294200112092512863L;

	public RobotDelivery(int deliveryNumber, String orderAddress, float orderPrice, float discount) {
		super(deliveryNumber, orderAddress, orderPrice, discount);
	}
	

}
