package com.google.gwt.cs310project.crimemapper.client;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gwt.cs310project.crimemapper.client.CrimeTypes;

@SuppressWarnings("serial")
public class CrimeDataByYear implements Serializable {

	private int year;
	private ArrayList<CrimeData> crimesDataList;
	private ArrayList<ArrayList<CrimeData>> filterByCrimeTypeList;
	@SuppressWarnings("unused")
	private CrimeDataByYear(){}

	public CrimeDataByYear(int year, ArrayList<CrimeData> crimes){
		this.year = year;
		this.crimesDataList = crimes;
		filterByCrimeTypeList = new ArrayList<ArrayList<CrimeData>>();
		sortCrimeType();
	}

	public int getYear(){
		return this.year;
	}
	
	public String yearToString(){
		return  "" + this.year;
	}
	public ArrayList<ArrayList<CrimeData>> getSortedCrimeList(){
		return filterByCrimeTypeList;
	}
	public ArrayList<CrimeData> getCrimes(){
		return this.crimesDataList;
	}

	public ArrayList<CrimeData> filterByCrimeType(String crimeType){

		ArrayList<CrimeData> filteredCrimeData = new ArrayList<CrimeData>();

		if(isCrimeType(crimeType)){
			for (CrimeData crime: this.crimesDataList){

				if(crime.getType().equals(crimeType))
					filteredCrimeData.add(crime);
			}
		}

		return filteredCrimeData;
	}
	
	private void sortCrimeType(){
		int n = CrimeTypes.getNumberOfTypes();
		int i = 0; 
		while(i < n){
			filterByCrimeTypeList.add(filterByCrimeType(CrimeTypes.getType(i)));
			i++;
		}
		
	}

	public int getNumberOfCrimeTypeOccurrences(String crimeType){
		int num = 0;
		
		if(isCrimeType(crimeType)){
			num = filterByCrimeType(crimeType).size();
		}
		return num;
	}
	
	public boolean isCrimeType(String crimeType){
		boolean isCrimeType = false;

		int i = 0;
		int n = CrimeTypes.getNumberOfTypes();
		while(i < n){
			String type = CrimeTypes.getType(i);
			if(type.equals(crimeType)){
				isCrimeType = true;
			}
			i++;
		}
		return isCrimeType;
	}
	
	public String toString(){
		return "" + getYear() + " had a total of " + crimesDataList.size() + " crimes.";
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
