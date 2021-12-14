package Enums;

public enum Doneness {
	rare, mediumRare, medium, mediumWell, wellDone;

	public String toString() {
		switch (this) {
		case rare:
			return "rare";
		case mediumRare:
			return "mediumRare";
		case medium:
			return "medium";
		case mediumWell:
			return "mediumWell";
		case wellDone:
			return "wellDone";
		default:
			return "";
		}
	}
}
