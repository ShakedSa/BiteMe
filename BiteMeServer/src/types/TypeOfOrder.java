package types;

/**
 * Enum representing a type of order delivery method.
 * 
 * @author Aviel Malayev
 * @author Natali Krief
 * @author Michael Ben Israel
 * @author Eden Ben Abu
 * @author Shaked Sabag
 * @version November 2021 (1.0)
 * */
public enum TypeOfOrder 
{
	basicDelivery,
	takeaway,
	preorderDelivery,
	sharedDelivery,
	robotDelivery;
	
	@Override
	public String toString() {
		switch(this) {
			case basicDelivery:
				return "basicDelivery";
			case takeaway:
				return "takeaway";
			case preorderDelivery:
				return "preorderDelivery";
			case sharedDelivery:
				return "sharedDelivery";
			case robotDelivery:
				return "robotDelivery";
			default:
				return "";
		}
	}
}
