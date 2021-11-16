package Config;

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
