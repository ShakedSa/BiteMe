package Entities;

import java.io.File;
import java.io.Serializable;

import Enums.BranchName;

public class Report implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5432585210792937987L;
	private String reportID;
	private String title;
	private String date;
	private File content;
	private BranchName branchName;
	
	
	public Report(String reportID, String title, String date,
			File content,BranchName branchName) {
		this.reportID = reportID;
		this.title = title;
		this.date = date;
		this.content = content;
		this.branchName = branchName;
	}


	/**
	 * @return the reportID
	 */
	public String getreportID() {
		return reportID;
	}
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}


	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}


	/**
	 * @return the content
	 */
	public File getContent() {
		return content;
	}


	/**
	 * @return the branchName
	 */
	public BranchName getBranchName() {
		return branchName;
	}


	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}


	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}


	/**
	 * @param content the content to set
	 */
	public void setContent(File content) {
		this.content = content;
	}


	/**
	 * @param branchName the branchName to set
	 */
	public void setBranchName(BranchName branchName) {
		this.branchName = branchName;
	}


}
