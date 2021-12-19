package Entities;

import java.io.Serializable;
import java.sql.Blob;

import Enums.BranchName;

public class ImportedUser implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ID; 
	private String UserName; 
	private String Password;
	private String FirstName; 
	private String LastName; 
	private String Email; 
	private String PhoneNumber; 
	private String Role; 
	private String Organization; 
	BranchName mainBranch;
	Blob Avatar;
	
	public ImportedUser(String iD, String userName, String password, String firstName, String lastName, String email,
			String phoneNumber, String role, String organization, BranchName mainBranch, Blob avatar) {
		super();
		ID = iD;
		UserName = userName;
		Password = password;
		FirstName = firstName;
		LastName = lastName;
		Email = email;
		PhoneNumber = phoneNumber;
		Role = role;
		Organization = organization;
		this.mainBranch = mainBranch;
		Avatar = avatar;
	} 
	
	/**
	 * @return the iD
	 */
	public String getID() {
		return ID;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return UserName;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return Password;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return FirstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return LastName;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return Email;
	}
	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return PhoneNumber;
	}
	/**
	 * @return the role
	 */
	public String getRole() {
		return Role;
	}
	/**
	 * @return the organization
	 */
	public String getOrganization() {
		return Organization;
	}
	/**
	 * @return the mainBranch
	 */
	public BranchName getMainBranch() {
		return mainBranch;
	}
	/**
	 * @return the avatar
	 */
	public Blob getAvatar() {
		return Avatar;
	}
	/**
	 * @param iD the iD to set
	 */
	public void setID(String iD) {
		ID = iD;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		UserName = userName;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		Password = password;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		FirstName = firstName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		LastName = lastName;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		Email = email;
	}
	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		PhoneNumber = phoneNumber;
	}
	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		Role = role;
	}
	/**
	 * @param organization the organization to set
	 */
	public void setOrganization(String organization) {
		Organization = organization;
	}
	/**
	 * @param mainBranch the mainBranch to set
	 */
	public void setMainBranch(BranchName mainBranch) {
		this.mainBranch = mainBranch;
	}
	/**
	 * @param avatar the avatar to set
	 */
	public void setAvatar(Blob avatar) {
		Avatar = avatar;
	}

}
