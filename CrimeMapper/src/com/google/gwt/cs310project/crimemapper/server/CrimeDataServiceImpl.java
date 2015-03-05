package com.google.gwt.cs310project.crimemapper.server;

import java.util.ArrayList;
import java.util.Scanner;
import java.net.*;
import java.io.*;

import com.google.gwt.cs310project.crimemapper.client.CrimeData;
import com.google.gwt.cs310project.crimemapper.client.CrimeDataService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class CrimeDataServiceImpl extends RemoteServiceServlet implements CrimeDataService {

	public ArrayList<CrimeData> getCrimeData(String url) {
		// TODO: Shouldn't we make this method static?

		ArrayList<CrimeData> crimeDataList = new ArrayList<CrimeData>();

		try {
			URL crime = new URL(url);
			BufferedReader crimeIn = new BufferedReader(
				new InputStreamReader(crime.openStream()));
			String inputLine = crimeIn.readLine();
			// The first line of the CSV file contains no data
			while ((inputLine = crimeIn.readLine()) != null) {
				crimeDataList.add(parseCrimeDataLine(inputLine));
			}
			crimeIn.close();
		} catch (Exception e) {
			// Assume the URL works correctly for now, so do nothing
			e.printStackTrace();
		}

		return crimeDataList;
	}
	
	private CrimeData parseCrimeDataLine(String inputLine) {
		Scanner sc = new Scanner(inputLine);
		sc.useDelimiter(",");
		String type = sc.next();
		// change type "Commercial BE" to "Commercial Break and Enter"
		if (type.equals("Commercial BE")){
			type = "Commercial Break and Enter";}
		int year = Integer.parseInt(sc.next());
		int month = Integer.parseInt(sc.next());
		String location = sc.next();
		sc.close();
		return new CrimeData(type, year, month, location);
	}
}
