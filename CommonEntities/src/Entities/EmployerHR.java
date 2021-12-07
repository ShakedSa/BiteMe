package Entities;

import java.io.File;

import Enums.BranchName;
import Enums.Status;
import Enums.UserType;

/**
 * @author Eden
 * EmployerHR class, stores employerID and extends user
 */
public class EmployerHR extends User{

	private String employerID;
	
	public EmployerHR(String userName, String password, String firstName, String lastName,
			String id, String email,String phoneNumber, UserType userType, String organization,
			BranchName mainBranch, String role, Status status, File avatar, String employerID) 
	{
		
		super(userName, password, firstName, lastName, id, email, phoneNumber,
				userType, organization, mainBranch, role, status,avatar);
		this.employerID=employerID;
	}

	
	
}
