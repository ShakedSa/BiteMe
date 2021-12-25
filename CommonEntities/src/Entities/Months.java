package Entities;

import java.util.Collection;
import java.util.HashMap;

import javafx.print.Collation;

public class Months {
	public static final HashMap<String,Integer> month;
	public static final HashMap<Integer,String> monthInNumbers;
	static {	
		month  = new HashMap<String,Integer>();
		monthInNumbers  = new HashMap<Integer,String>();
		month.put("January",1);
		month.put("February",2);
		month.put("March",3);
		month.put("April",4);
		month.put("May",5);
		month.put("June",6);
		month.put("July",7);
		month.put("August",8);
		month.put("September",9);
		month.put("October",10);
		month.put("November",11);
		month.put("December",12);
		monthInNumbers.put(1,"January");
		monthInNumbers.put(2,"February");
		monthInNumbers.put(3,"March");
		monthInNumbers.put(4,"April");
		monthInNumbers.put(5,"May");
		monthInNumbers.put(6,"June");
		monthInNumbers.put(7,"July");
		monthInNumbers.put(8,"August");
		monthInNumbers.put(9,"September");
		monthInNumbers.put(10,"October");
		monthInNumbers.put(11,"November");
		monthInNumbers.put(12,"December");
	};
	public static String getMonth (int i) {
		if(i<0||i> 12 ) return null;
		return monthInNumbers.get(i);
	}
	public static String getMonthNumberString (String m) {
		if(!month.containsKey(m)) return null;
		return month.get(m).toString();
	}
	public static int getMonth (String m) {
		if(!month.containsKey(m)) return -1;
		return month.get(m);
	}
	public static Collection<String> getMonths (){
		return monthInNumbers.values(); 
	}
	public static int getQuarter(String m) {
		if(!month.containsKey(m)) return -1;
		return month.get(m)%4+1;
	}
	
}