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

	/**
	 * 
	 */
	private static final long serialVersionUID = -8631032615035425968L;
	private String employerCode;
	
	public EmployerHR(String userName, String password, String firstName, String lastName,
			String id, String email,String phoneNumber, UserType userType, String organization,
			BranchName mainBranch, String role, Status status, File avatar, String employerCode) 
	{
		
		super(userName, password, firstName, lastName, id, email, phoneNumber,
				userType, organization, mainBranch, role, status,avatar);
		this.employerCode=employerCode;
	}

	
	
}
