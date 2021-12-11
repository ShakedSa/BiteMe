package Entities;

import java.io.Serializable;

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
public class Component implements Serializable { 
	/**
	 * 
	 */
	private static final long serialVersionUID = 9060288320679741380L;
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
	public Component(Size size,Doneness doneness,String restrictions ) {
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
	public Component(int componentId, Size size,Doneness doneness,String restrictions) {
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
	
	public String toString() {
		StringBuilder b = new StringBuilder();
		if(size != null) {
			b.append(size);
		}
		if(doneness != null) {
			b.append(doneness);
		}
		if(restrictions != null) {
			b.append(restrictions);
		}
		return b.toString();
	}
}
