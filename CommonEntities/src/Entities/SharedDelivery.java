package Entities;

public class SharedDelivery extends Delivery {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3803571920726125209L;
	private String businessCode;
	private int amountOfPeople;
	
	
	public SharedDelivery(int deliveryNumber, String orderAddress,
			float orderPrice, float discount, String businessCode, int amountOfPeople) {
		super(deliveryNumber, orderAddress, orderPrice, discount);
		this.amountOfPeople = amountOfPeople;
		this.businessCode = businessCode;
	}
	
	public SharedDelivery(float orderPrice) {
		super(orderPrice);
	}


	/**
	 * @return the businessCode
	 */
	public String getBusinessCode() {
		return businessCode;
	}


	/**
	 * @return the amountOfPeople
	 */
	public int getAmountOfPeople() {
		return amountOfPeople;
	}


	/**
	 * @param businessCode the businessCode to set
	 */
	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}


	/**
	 * @param amountOfPeople the amountOfPeople to set
	 */
	public void setAmountOfPeople(int amountOfPeople) {
		this.amountOfPeople = amountOfPeople;
	}
	
	public String toString() {
		return super.toString() + "\nAmount of people in order: " + amountOfPeople;
	}

}
