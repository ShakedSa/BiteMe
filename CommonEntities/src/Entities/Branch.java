package Entities;

import java.io.Serializable;
import Enums.BranchName;

public class Branch implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7545747177565237611L;
	private BranchName branchName;
	private BranchManager branchManager;
	
	public Branch(BranchName branchName, BranchManager branchManager) {
		super();
		this.branchName = branchName;
		this.branchManager = branchManager;
	}
	
	
	/**
	 * @return the branchName
	 */
	public BranchName getBranchName() {
		return branchName;
	}
	/**
	 * @return the branchManager
	 */
	public BranchManager getBranchManager() {
		return branchManager;
	}
	/**
	 * @param branchName the branchName to set
	 */
	public void setBranchName(BranchName branchName) {
		this.branchName = branchName;
	}
	/**
	 * @param branchManager the branchManager to set
	 */
	public void setBranchManager(BranchManager branchManager) {
		this.branchManager = branchManager;
	}
		
}
