package com.google.gwt.cs310project.crimemapper.client;

import java.io.Serializable;

public class CrimeData implements Serializable {
	
	private String type;
	private int year;
	private int month;
	private String location;
	private String id;
	
	public CrimeData(String type, int year, int month, String location){
		this.type = type;
		this.year = year;
		this.location = location;
		this.id = null;
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

	public void setID(String lineNumber){
		this.id = "" + month + "" + year;
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
	
	

}
