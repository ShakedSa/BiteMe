package Entities;

import java.io.File;
import java.io.Serializable;
import java.sql.Blob;

import Enums.BranchName;

public class NewSupplier implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2554844582125465637L;
	private String userName;
	private String resturantType;
	private String resturantName;
	private String resturantAddress;
	private Blob imagUpload;
	private String monthlyCommision;
	private BranchName branchName;
	/**
	 * @param userName
	 * @param resturantType
	 * @param resturantName
	 * @param resturantAddress
	 * @param imagUpload
	 * @param monthlyCommision
	 * @param branchName
	 */
	public NewSupplier(String userName, String resturantType, String resturantName, String resturantAddress,
			Blob imagUpload, String monthlyCommision, BranchName branchName) {
		super();
		this.userName = userName;
		this.resturantType = resturantType;
		this.resturantName = resturantName;
		this.resturantAddress = resturantAddress;
		this.imagUpload = imagUpload;
		this.monthlyCommision = monthlyCommision;
		this.branchName = branchName;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @return the resturantType
	 */
	public String getResturantType() {
		return resturantType;
	}
	/**
	 * @return the resturantName
	 */
	public String getResturantName() {
		return resturantName;
	}
	/**
	 * @return the resturantAddress
	 */
	public String getResturantAddress() {
		return resturantAddress;
	}
	/**
	 * @return the imagUpload
	 */
	public Blob getImagUpload() {
		return imagUpload;
	}
	/**
	 * @return the monthlyCommision
	 */
	public String getMonthlyCommision() {
		return monthlyCommision;
	}
	/**
	 * @return the branchName
	 */
	public BranchName getBranchName() {
		return branchName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @param resturantType the resturantType to set
	 */
	public void setResturantType(String resturantType) {
		this.resturantType = resturantType;
	}
	/**
	 * @param resturantName the resturantName to set
	 */
	public void setResturantName(String resturantName) {
		this.resturantName = resturantName;
	}
	/**
	 * @param resturantAddress the resturantAddress to set
	 */
	public void setResturantAddress(String resturantAddress) {
		this.resturantAddress = resturantAddress;
	}
	/**
	 * @param imagUpload the imagUpload to set
	 */
	public void setImagUpload(Blob imagUpload) {
		this.imagUpload = imagUpload;
	}
	/**
	 * @param monthlyCommision the monthlyCommision to set
	 */
	public void setMonthlyCommision(String monthlyCommision) {
		this.monthlyCommision = monthlyCommision;
	}
	/**
	 * @param branchName the branchName to set
	 */
	public void setBranchName(BranchName branchName) {
		this.branchName = branchName;
	}
	
}