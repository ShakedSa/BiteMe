package Entities;

import java.io.File;

import Enums.BranchName;

public class MounthlyReport extends Report {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1307826367537589027L;
	private String reportType;

	public MounthlyReport(String reportID, String title, String date,
			File content, BranchName branchName,  String reportType) {
		super(reportID, title, date, content, branchName);
		this.reportType = reportType;
	}

	/**
	 * @return the reportType
	 */
	public String getReportType() {
		return reportType;
	}

	/**
	 * @param reportType the reportType to set
	 */
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	


}
