package Enums;

public enum Size {
	Small, Medium, Large;

	public String toString() {
		switch (this) {
		case Small:
			return "Small";
		case Large:
			return "Large";
		case Medium:
			return "Medium";
		default:
			return "";
		}
	}
}
