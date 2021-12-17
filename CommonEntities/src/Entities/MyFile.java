package Entities;

import java.io.Serializable;
import java.util.ArrayList;

public class MyFile implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3037364578344496376L;
	private ArrayList<String> Description=null;
	private String fileName=null;	
	private int size=0;
	public  byte[] mybytearray;
	
	
	public void initArray(int size)
	{
		mybytearray = new byte [size];	
	}
	
	public MyFile( String fileName) {
		Description=new ArrayList<String>();
		this.fileName = fileName;
	}
	
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public byte[] getMybytearray() {
		return mybytearray;
	}
	
	public byte getMybytearray(int i) {
		return mybytearray[i];
	}

	public void setMybytearray(byte[] mybytearray) {
		
		for(int i=0;i<mybytearray.length;i++)
		this.mybytearray[i] = mybytearray[i];
	}

	public ArrayList<String> getDescription() {
		return Description;
	}

	public void setDescription(ArrayList<String> description) {
		Description = description;
	}	
}

