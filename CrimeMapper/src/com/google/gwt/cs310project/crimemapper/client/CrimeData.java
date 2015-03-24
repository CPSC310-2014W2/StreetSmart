package com.google.gwt.cs310project.crimemapper.client;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CrimeData implements Serializable {
	
	private String type;
	private int year;
	private int month;
	private String location;
	private String id;
	
	@SuppressWarnings("unused")
	private CrimeData(){}
	
	public CrimeData(String type, int year, int month, String location){
		this.type = type; 
		this.year = year;
		this.month = month;
		this.location = location;
	}
	
	public String formatLocation(String location){
		location.replace("XX", "00");
		location.replace("/", "+");
		return location;
	}
	
	public String getId(){
		return id;
	}
	
	public String getType() {
		return type;
	}


	public int getYear() {
		return year;
	}


	public int getMonth() {
		return month;
	}

	public String getLocation() {
		return location;
	}

	public void setID(int lineNumber){
		this.id = "" + month + "" + year + "" + lineNumber;
	}

	public void setType(String type) {
		this.type = type;
	}


	public void setYear(int year) {
		this.year = year;
	}


	public void setMonth(int month) {
		this.month = month;
	}


	public void setLocation(String location) {
		this.location = location;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj.getClass() != CrimeData.class) {
			return false;
		}
		boolean eq = ((CrimeData) obj).getType().equals(this.type)
				&& ((CrimeData) obj).getYear() == this.year
				&& ((CrimeData) obj).getMonth() == this.month
				&& ((CrimeData) obj).getLocation().equals(this.location);
		String id2 = ((CrimeData) obj).getId();
		if (id2 != null) {
			eq = eq && (id2.equals(this.id));
		}
		return eq;
	}

	@Override
	public String toString() {
		
		String crimeString = "In " + year + " in the " + month + " month, this crime of [" + type +"] " 
		                + "took place at " + location + ", Vancouver, BC.";
		return crimeString;
	}
}
