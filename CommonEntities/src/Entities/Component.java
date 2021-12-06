package Entities;

import Enums.Doneness;
import Enums.Size;

 /**
 * @author Eden
 * Component class, stores 
 * 	 int componentId;
	 String restrictions;
	 Size size;
	 Doneness doneness;
 */
public class Component { 
	private int componentId;
	private String restrictions;
	private Size size;
	private Doneness doneness;
	
	
	/**
	 * constructor for supplier who wants to add new component:
	 * @param size
	 * @param doneness
	 * @param restrictions
	 */
	Component(Size size,Doneness doneness,String restrictions ) {
		this.size=size;
		this.doneness=doneness;
		this.restrictions=restrictions;
	}
	
	/**
	 * constructor for supplier who wants to add new component:
	 * @param componentId
	 * @param size
	 * @param doneness
	 * @param restrictions
	 */
	Component(int componentId, Size size,Doneness doneness,String restrictions ) {
		this.componentId=componentId;
		this.size=size;
		this.doneness=doneness;
		this.restrictions=restrictions;
	}
	public int getComponentId() {
		return componentId;
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

	
	
}
