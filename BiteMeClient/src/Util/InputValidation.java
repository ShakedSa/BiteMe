package Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for input validation. contain static method for input validation.
 * 
 * @author Shaked
 */
public class InputValidation {
	/**
	 * Method for validating input. Checks if the string is not empty.
	 * 
	 * @param String text
	 * @return boolean
	 */
	public static boolean checkValidText(String input) {
		if (input == null || input.equals("")) {
			return false;
		}
		return true;
	}

	/**
	 * Method for validating input. Checks if the string contains special
	 * characters.
	 * 
	 * @param String input
	 * @return boolean
	 */
	public static boolean checkSpecialCharacters(String input) {
		Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(input);
		return m.find();
	}

	/**
	 * Method for validating input. Checks if the string contains characters.
	 * 
	 * @param String input
	 * @return boolean
	 */
	public static boolean checkContainCharacters(String input) {
		Pattern p = Pattern.compile("[a-z]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(input);
		return m.find();
	}

	/**
	 * Method for validating phone number. checks if the phone number contains
	 * letters or not 10 digit length.
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean checkPhoneNumber(String input) {
		if(checkSpecialCharacters(input)) {
			return false;
		}
		if (checkContainCharacters(input)) {
			return false;
		}
		if (input.length() != 10) {
			return false;
		}
		return true;
	}
}
