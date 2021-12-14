package Enums;

public enum PaymentMethod {

	CreditCard, BusinessCode;
	
	public static String getEnum(PaymentMethod method) {
		switch(method) {
		case CreditCard:
			return "Credit Card";
		case BusinessCode:
			return "Business Card";
		default:
			return "";
		}
	}
}
