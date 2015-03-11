package com.google.gwt.cs310project.crimemapper.client;

import java.io.Serializable;
import java.util.HashMap;

@SuppressWarnings("serial")
public final class CrimeTypes implements Serializable {
	
	private static HashMap<Integer, String> crimeTypes = new HashMap<Integer, String>();
	
	
	private CrimeTypes() {
		// Do not allow instantiation of this class
	}
	
	static {
		crimeTypes.put(0, "Mischief Under $5000");
		crimeTypes.put(1, "Mischief Over $5000");
		crimeTypes.put(2, "Theft From Auto Under $5000");
		crimeTypes.put(3, "Theft From Auto Over $5000");
		crimeTypes.put(4, "Theft Of Auto Under $5000");
		crimeTypes.put(5, "Theft Of Auto Over $5000");
		crimeTypes.put(6, "Commercial Break and Enter");
	}
	
	public static String getType(int i) {
		return crimeTypes.get(i);
	}
	
	public static int getNumberOfTypes() {
		return crimeTypes.size();
	}

}
