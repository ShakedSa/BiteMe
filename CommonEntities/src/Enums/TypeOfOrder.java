package Enums;

public enum TypeOfOrder {

	BasicDelivery, takeaway, preorderDelivery, sharedDelivery, RobotDelivery;

	public static TypeOfOrder getEnum(String name) {
		switch (name) {
		case "Basic Delivery":
			return BasicDelivery;
		case "Takeaway":
			return takeaway;
		case "Preorder Delivery":
			return preorderDelivery;
		case "Shared Delivery":
			return sharedDelivery;
		case "Robot Delivery":
			return RobotDelivery;
		default:
			return null;
		}
	}

	public String toString() {
		switch (this) {
		case BasicDelivery:
			return "Basic Delivery";
		case takeaway:
			return "Takeaway";
		case preorderDelivery:
			return "Preorder Delivery";
		case sharedDelivery:
			return "Shared Delivery";
		case RobotDelivery:
			return "Robot Delivery";
		default:
			return null;
		}
	}
}
