package Entities;

import java.io.File;

import Enums.Branch;
import Enums.Status;
import Enums.UserType;

public class CEO extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CEO(String userName, String password, String firstName, String lastName, String id,
			String email, String phoneNumber, UserType userType, String organization,
			Branch mainBranch, String role, Status status, File avatar)
	{
		super(userName, password, firstName, lastName, id, email, phoneNumber, userType,
				organization,mainBranch, role, status, avatar);
		
		
	}

}
