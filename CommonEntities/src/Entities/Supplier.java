package Entities;

import java.io.File;
import java.util.ArrayList;

import Enums.BranchName;
import Enums.RestaurantType;
import Enums.Status;
import Enums.UserType;

/**
 * @author Eden
 * Supplier class, extends user and stores user+ : String restaurantName, ArrayList<Product> Menu, int monthlyComissionPercent,Branch relatedBranch
 */
public class Supplier extends User{


	private static final long serialVersionUID = 1L;
	
	String restaurantName;
	ArrayList<Product> Menu;
	Float monthlyComissionPercent;
	//BranchName relatedBranch;
	String restaurantAddress; // change line 22 - aviel
	RestaurantType restaurantType;
	MyFile restaurantLogo;

	public Supplier(String userName, String password, String firstName, String lastName, String id, String email,
					String phoneNumber, UserType userType, String organization,
					BranchName mainBranch, String role, Status status,File avatar,
					String restaurantName, ArrayList<Product> Menu, Float monthlyComissionPercent,String relatedBranch) 
	{
		super(userName, password, firstName, lastName, id, email, phoneNumber, userType, organization, mainBranch, role, status,
				avatar);
		this.restaurantName=restaurantName;
		//this.relatedBranch=relatedBranch;
		this.restaurantAddress=relatedBranch; // change related branch to the actual address of the restaurant that is in the DB - aviel
		this.Menu=Menu;
		this.monthlyComissionPercent=monthlyComissionPercent;
	}
	
	public Supplier(String restaurantName, RestaurantType restaurantType, MyFile restaurantLogo) {
		super();
		this.restaurantName=restaurantName;
		this.restaurantType = restaurantType;
		this.restaurantLogo = restaurantLogo;
	}

	/**
	 * @return the restaurantName
	 */
	public String getRestaurantName() {
		return restaurantName;
	}

	/**
	 * @return the menu
	 */
	public ArrayList<Product> getMenu() {
		return Menu;
	}

	/**
	 * @return the monthlyComissionPercent
	 */
	public Float getMonthlyComissionPercent() {
		return monthlyComissionPercent;
	}
	/*
	/**
	 * @return the relatedBranch
	 *
	public BranchName getRelatedBranch() {
		return relatedBranch;
	}
	*/
	
	/**
	 * @return the restaurantAddress
	 */
	public String getRestaurantAddress() {
		return restaurantAddress;
	}
	

	/**
	 * @param restaurantName the restaurantName to set
	 */
	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}

	/**
	 * @param menu the menu to set
	 */
	public void setMenu(ArrayList<Product> menu) {
		Menu = menu;
	}

	/**
	 * @param monthlyComissionPercent the monthlyComissionPercent to set
	 */
	public void setMonthlyComissionPercent(Float monthlyComissionPercent) {
		this.monthlyComissionPercent = monthlyComissionPercent;
	}
	/*
	/**
	 * @param relatedBranch the relatedBranch to set
	 *
	public void setRelatedBranch(BranchName relatedBranch) {
		this.relatedBranch = relatedBranch;
	}
	*/
	
	/**
	 * @param set restaurantAddress
	 */
	public void setRestaurantAddress(String restaurantAddress) {
		this.restaurantAddress = restaurantAddress;
	}
	
	/**
	 * @return the restaurantType
	 */
	public RestaurantType getRestaurantType() {
		return restaurantType;
	}

	/**
	 * @param restaurantType the restaurantType to set
	 */
	public void setRestaurantType(RestaurantType restaurantType) {
		this.restaurantType = restaurantType;
	}

	/**
	 * @return the restaurantLogo
	 */
	public MyFile getRestaurantLogo() {
		return restaurantLogo;
	}

	/**
	 * @param restaurantLogo the restaurantLogo to set
	 */
	public void setRestaurantLogo(MyFile restaurantLogo) {
		this.restaurantLogo = restaurantLogo;
	}

	@Override
	public String toString() {
		return super.toString() + "Restaurant: " + restaurantName;
	}

}
