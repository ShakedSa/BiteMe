package Enums;

public enum TypeOfProduct {

	mainDish,entry,dessert,drink;
	
	public static TypeOfProduct getEnum(String name) {
		switch(name) {
		case "main dish":
			return mainDish;
		case "entry":
			return entry;
		case "dessert":
			return dessert;
		case "drink":
			return drink;
		default:
			return null;
		}
	}
	public String toString() {
		switch(this) {
		case mainDish:
			return "main dish";
		case entry:
			return "entry";
		case dessert:
			return "dessert";
		case drink:
			return "drink";
		default:
			return "";
		}
	}
}
