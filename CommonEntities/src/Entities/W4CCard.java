package Entities;

import java.io.Serializable;

/**
 * @author Shaked
 * @author Natali
 * @author Eden
 *
 * @version December 05 2021 - v1.0
 */
public class W4CCard implements Serializable {

	/**
	 * Serializable autogenerated serialversion.
	 */
	private static final long serialVersionUID = 3336427101534731062L;

	private int w4cID;
	private String employerID;
	private String qrCode;
	private String creditCardNumber;
	private float monthlyBudget;
	private float balance;
	private float dailyBudget;
	private float dailyBalance;

	/**
	 * @param w4cID
	 * @param employerID
	 * @param qrCode
	 * @param creditCardNumber
	 * @param monthlyBudget
	 * @param balance
	 * @param dailyBudget
	 */
	public W4CCard(int w4cID, String employerID, String qrCode, String creditCardNumber, float monthlyBudget,
			float balance, float dailyBudget, float dailyBalance) {
		super();
		this.w4cID = w4cID;
		this.employerID = employerID;
		this.qrCode = qrCode;
		this.creditCardNumber = creditCardNumber;
		this.monthlyBudget = monthlyBudget;
		this.balance = balance;
		this.dailyBudget = dailyBudget;
		this.dailyBalance = dailyBalance;
	}

	public W4CCard() {

	}

	public String getCreditCardNumber() {
		return creditCardNumber;
	}

	public float getMonthlyBudget() {
		return monthlyBudget;
	}

	public float getBalance() {
		return balance;
	}

	public float getDailyBudget() {
		return dailyBudget;
	}

	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

	public void setMonthlyBudget(float monthlyBudget) {
		this.monthlyBudget = monthlyBudget;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	public void setDailyBudget(float dailyBudget) {
		this.dailyBudget = dailyBudget;
	}

	public int getW4CID() {
		return w4cID;
	}

	public String getEmployerID() {
		return employerID;
	}

	public String getQRCode() {
		return qrCode;
	}

	/**
	 * @return the dailyBalance
	 */
	public float getDailyBalance() {
		return dailyBalance;
	}

	/**
	 * @param dailyBalance the dailyBalance to set
	 */
	public void setDailyBalance(float dailyBalance) {
		this.dailyBalance = dailyBalance;
	}

}
