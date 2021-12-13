package Entities;

public class SharedDelivery extends Delivery {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3803571920726125209L;
	private String businessCode;
	private int amountOfPeople;

	public SharedDelivery(String orderAddress, String firstName, String lastName, String phoneNumber, float orderPrice,
			float discount, String businessCode, int amountOfPeople) {
		super(orderAddress, firstName, lastName, phoneNumber, orderPrice, discount);
		this.businessCode = businessCode;
		this.amountOfPeople = amountOfPeople;
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
		return super.toString() + "\nAmount of people in order: " + amountOfPeople + "\nBusiness Code: " + businessCode;
	}

}
