package Entities;

public class RobotDelivery extends Delivery {


	/**
	 * 
	 */
	private static final long serialVersionUID = 4294200112092512863L;

	
	public RobotDelivery(String orderAddress, String firstName, String lastName, String phoneNumber, float orderPrice,
			float discount) {
		super(orderAddress, firstName, lastName, phoneNumber, orderPrice, discount);
	}	

}
