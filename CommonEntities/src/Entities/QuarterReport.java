package Entities;

import java.io.File;

import Enums.BranchName;

public class QuarterReport extends Report {
	
/**
	 * 
	 */
	private static final long serialVersionUID = -5532624314759118258L;
private BranchManager branchManager;
	
	public QuarterReport(String reportID, String title,
			String date, File content, BranchName branchName, BranchManager branchManager) {
		super(reportID, title, date, content, branchName);
		this.branchManager = branchManager;
	}

	/**
	 * @return the branchManager
	 */
	public BranchManager getBranchManager() {
		return branchManager;
	}
	
}
