package Entities;

import java.io.File;

import Enums.BranchName;
import Enums.Status;
import Enums.UserType;

/**
 * @author Eden
 * BranchManager class, extends user
 */
public class BranchManager extends User{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BranchManager(String userName, String password, String firstName, String lastName,
			String id, String email, String phoneNumber, UserType userType, String organization,
			BranchName mainBranch, String role, Status status, File avatar) 
	{
		super(userName, password, firstName, lastName, id, email, phoneNumber,
				userType, organization, mainBranch, role, status, avatar);

	}
	
}
