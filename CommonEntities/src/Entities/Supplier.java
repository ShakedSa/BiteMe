package Entities;

import java.io.File;
import java.util.ArrayList;

import Enums.BranchName;
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
	int monthlyComissionPercent;
	BranchName relatedBranch;

	public Supplier(String userName, String password, String firstName, String lastName, String id, String email,
					String phoneNumber, UserType userType, String organization,
					BranchName mainBranch, String role, Status status,File avatar,
					String restaurantName, ArrayList<Product> Menu, int monthlyComissionPercent,BranchName relatedBranch) 
	{
		super(userName, password, firstName, lastName, id, email, phoneNumber, userType, organization, mainBranch, role, status,
				avatar);
		this.restaurantName=restaurantName;
		this.relatedBranch=relatedBranch;
		this.Menu=Menu;
		this.monthlyComissionPercent=monthlyComissionPercent;
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
	public int getMonthlyComissionPercent() {
		return monthlyComissionPercent;
	}

	/**
	 * @return the relatedBranch
	 */
	public BranchName getRelatedBranch() {
		return relatedBranch;
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
	public void setMonthlyComissionPercent(int monthlyComissionPercent) {
		this.monthlyComissionPercent = monthlyComissionPercent;
	}

	/**
	 * @param relatedBranch the relatedBranch to set
	 */
	public void setRelatedBranch(BranchName relatedBranch) {
		this.relatedBranch = relatedBranch;
	}

}
