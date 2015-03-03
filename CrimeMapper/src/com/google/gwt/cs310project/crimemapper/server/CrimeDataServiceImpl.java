package com.google.gwt.cs310project.crimemapper.server;

import java.util.ArrayList;
import java.net.*;
import java.io.*;

import com.google.gwt.cs310project.crimemapper.client.CrimeData;
import com.google.gwt.cs310project.crimemapper.client.CrimeDataService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class CrimeDataServiceImpl implements CrimeDataService {

	public ArrayList<CrimeData> getCrimeData(String url) {

		ArrayList<CrimeData> crimeDataList = new ArrayList<CrimeData>();

		try {
			URL crime = new URL(url);
			BufferedReader crimeIn = new BufferedReader(
				new InputStreamReader(crime.openStream()));
			String inputLine;
			while ((inputLine = crimeIn.readLine()) != null) {
				crimeDataList.add(parseCrimeDataLine(inputLine));
			}
		} catch (Exception e) {
			// Assume the URL works correctly for now, so do nothing
			e.printStackTrace();
		}

		return crimeDataList;
	}
	
	private CrimeData parseCrimeDataLine(String inputLine) {
		return null;
	}
}
