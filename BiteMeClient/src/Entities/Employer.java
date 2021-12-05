package Entities;

import java.io.Serializable;

public class Employer implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6020532991917495602L;
	private String employerID;
	private String employerCode;
	private String employerName;
	private boolean isApproved;
	
	
	/**
	 * @param employerID
	 * @param employerCode
	 * @param employerName
	 * @param isApproved
	 */
	public Employer(String employerID, String employerCode, String employerName, boolean isApproved) {
		this.employerID = employerID;
		this.employerCode = employerCode;
		this.employerName = employerName;
		this.isApproved = isApproved;
	}


	public String getEmployerName() {
		return employerName;
	}


	public boolean isApproved() {
		return isApproved;
	}


	public void setEmployerName(String employerName) {
		this.employerName = employerName;
	}


	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}
	
	public String getEmployerID() {
		return employerID;
	}
	
	public String getEmployerCode() {
		return employerCode;
	}
	
}
