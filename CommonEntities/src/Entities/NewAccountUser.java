package Entities;

import java.io.Serializable;

public class NewAccountUser implements Serializable {

	/**
	 * @param userName
	 * @param firstName
	 * @param lastName
	 * @param id
	 * @param email
	 * @param phone
	 */
	public NewAccountUser(String userName, String firstName, String lastName, String id, String email, String phone) {
		super();
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.id = id; 
		this.email = email;
		this.phone = phone;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 4295735809398290510L;
	private String userName;
	private String firstName;
	private String lastName;
	private String id;
	private String email;
	private String phone;
	

}
