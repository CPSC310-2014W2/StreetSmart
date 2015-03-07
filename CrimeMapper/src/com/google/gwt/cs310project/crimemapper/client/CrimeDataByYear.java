package com.google.gwt.cs310project.crimemapper.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.cs310project.crimemapper.client.CrimeTypes;

@SuppressWarnings("serial")
public class CrimeDataByYear implements Serializable {

	private int year;
	
	private HashMap<String, ArrayList<CrimeData>> crimesDataList;
	
	private CrimeDataByYear(){}

	public CrimeDataByYear(int year, ArrayList<CrimeData> crimes){
		
		this.year = year;
		
		crimesDataList = new HashMap<String, ArrayList<CrimeData>>();
		
		for (int i = 0; i < CrimeTypes.getNumberOfTypes(); i++) {
			crimesDataList.put(CrimeTypes.getType(i), new ArrayList<CrimeData>());
		}
		
		for (CrimeData crime : crimes) {
			if (!crimesDataList.containsKey(crime.getType())) {
				System.out.println(crime.getType());
			}
			crimesDataList.get(crime.getType()).add(crime);
		}
	}

	public int getYear(){
		return this.year;
	}
	
	public String yearToString(){
		return  "" + this.year;
	}

	public int getNumberOfCrimeTypeOccurrences(String crimeType){
		return crimesDataList.get(crimeType).size();
	}
	
	public String toString(){
		int numCrimes = 0;
		for (int i = 0; i < CrimeTypes.getNumberOfTypes(); i++) {
			numCrimes = numCrimes + crimesDataList.get(CrimeTypes.getType(i)).size();
		}
		return "" + getYear() + " had a total of " + numCrimes + " crimes.";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((crimesDataList == null) ? 0 : crimesDataList.hashCode());
		result = prime * result + year;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CrimeDataByYear other = (CrimeDataByYear) obj;
		if (crimesDataList == null) {
			if (other.crimesDataList != null)
				return false;
		} else if (!crimesDataList.equals(other.crimesDataList))
			return false;
		if (year != other.year)
			return false;
		return true;
	}
}
