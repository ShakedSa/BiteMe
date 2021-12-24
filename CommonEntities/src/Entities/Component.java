package Entities;

import java.io.Serializable;

import Enums.Doneness;
import Enums.Size;

/**
 * @author Eden Component class, stores int componentId; String restrictions;
 *         Size size; Doneness doneness;
 */
public class Component implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9060288320679741380L;
	private String restrictions;
	private Size size;
	private Doneness doneness;
	public static final double largeSizePrice = 1.25;
	public static final double smallSizePrice = 0.67;

//	/**
//	 * constructor for supplier who wants to add new component:
//	 * 
//	 * @param size
//	 * @param doneness
//	 * @param restrictions
//	 */
//	public Component(Size size, Doneness doneness, String restrictions) {
//		this.size = size;
//		this.doneness = doneness;
//		this.restrictions = restrictions;
//	}
//
//	/**
//	 * constructor for supplier who wants to add new component:
//	 * 
//	 * @param componentId
//	 * @param size
//	 * @param doneness
//	 * @param restrictions
//	 */
//	public Component(int componentId, Size size, Doneness doneness, String restrictions) {
//		this.size = size;
//		this.doneness = doneness;
//		this.restrictions = restrictions;
//	}

	/**
	 * Constructor to create new size component.
	 * 
	 * @param size;
	 */
	public Component(Size size) {
		this.size = size;
		doneness = null;
		restrictions = null;
	}

	/**
	 * Constructor to create new doneness component.
	 * 
	 * @param doneness
	 */
	public Component(Doneness doneness) {
		this.doneness = doneness;
		size = null;
		restrictions = null;
	}

	/**
	 * Constructor to create new restrictions component.
	 * 
	 * @param restrictions
	 */
	public Component(String restrictions) {
		this.restrictions = restrictions;
		size = null;
		doneness = null;
	}

	public String getRestrictions() {
		return restrictions;
	}

	public Size getSize() {
		return size;
	}

	public Doneness getDoneness() {
		return doneness;
	}

	public void setRestrictions(String restrictions) {
		this.restrictions = restrictions;
	}

	public void setSize(Size size) {
		this.size = size;
	}

	public void setDoneness(Doneness doneness) {
		this.doneness = doneness;
	}

	@Override
	public String toString() {
		if (size != null) {
			return size.toString();
		}
		if (doneness != null) {
			return doneness.toString();
		}
		if (restrictions != null) {
			return restrictions;
		}
		return "";
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Component)) {
			return false;
		}
		Component other = (Component)obj;
		if(restrictions != null && other.restrictions != null) {
			return restrictions.equals(other.restrictions);
		}
		if(doneness != null && other.doneness != null) {
			return doneness.equals(other.doneness);
		}
		if(size != null && other.size != null) {
			return size.equals(other.size);
		}
		return false;
	}
}
