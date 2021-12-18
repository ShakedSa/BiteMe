package Entities;
import java.io.File;
import java.io.Serializable;
import java.sql.Blob;

import Enums.BranchName;
import Enums.Status;
import Enums.UserType;

/**
 * Abstract class to describe a User. Implements Serializable for ocsf.
 * 
 */
public class NewUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7090956517946690459L;
	private String userName;
	private String password;
	private String firstName;
	private String lastName;
	private String id;
	private String email;
	private String phoneNumber;
	private UserType userType;
	private String organization;
	private BranchName mainBranch;
	private String role;
	private int isLoggedIn;
	private Status status;
	private Blob avatar;
	
	private NewSupplier supplier;
	/**
	 * @return the supplier
	 */
	public NewSupplier getSupplier() {
		return supplier;
	}
	/**
	 * @param supplier the supplier to set
	 */
	public void setSupplier(NewSupplier supplier) {
		this.supplier = supplier;
	}
	/**
	 * @param userName
	 * @param password
	 * @param firstName
	 * @param lastName
	 * @param id
	 * @param email
	 * @param phoneNumber
	 * @param userType
	 * @param organization
	 * @param mainBranch
	 * @param role
	 * @param isLoggedIn
	 * @param status
	 * @param avatar
	 */
	public NewUser(String userName, String password, String firstName, String lastName, String id, String email,
			String phoneNumber, UserType userType, String organization, BranchName mainBranch, String role,
			int isLoggedIn, Status status, Blob avatar) {
		super();
		this.userName = userName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.id = id;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.userType = userType;
		this.organization = organization;
		this.mainBranch = mainBranch;
		this.role = role;
		this.isLoggedIn = isLoggedIn;
		this.status = status;
		this.avatar = avatar;
	}
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
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
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}
	/**
	 * @return the userType
	 */
	public UserType getUserType() {
		return userType;
	}
	/**
	 * @return the organization
	 */
	public String getOrganization() {
		return organization;
	}
	/**
	 * @return the mainBranch
	 */
	public BranchName getMainBranch() {
		return mainBranch;
	}
	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}
	/**
	 * @return the isLoggedIn
	 */
	public int getIsLoggedIn() {
		return isLoggedIn;
	}
	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}
	/**
	 * @return the avatar
	 */
	public Blob getAvatar() {
		return avatar;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
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
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	/**
	 * @param userType the userType to set
	 */
	public void setUserType(UserType userType) {
		this.userType = userType;
	}
	/**
	 * @param organization the organization to set
	 */
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	/**
	 * @param mainBranch the mainBranch to set
	 */
	public void setMainBranch(BranchName mainBranch) {
		this.mainBranch = mainBranch;
	}
	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}
	/**
	 * @param isLoggedIn the isLoggedIn to set
	 */
	public void setIsLoggedIn(int isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
	}
	/**
	 * @param avatar the avatar to set
	 */
	public void setAvatar(Blob avatar) {
		this.avatar = avatar;
	}
	
}
