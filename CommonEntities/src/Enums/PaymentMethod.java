package Enums;

public enum PaymentMethod {

	CreditCard, BusinessCode, Both;
	
	public static String getEnum(PaymentMethod method) {
		switch(method) {
		case CreditCard:
			return "Credit Card";
		case BusinessCode:
			return "Business Card";
		case Both:
			return "Credit Card & Business Card";
		default:
			return "";
		}
	}
}
