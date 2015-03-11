package com.google.gwt.cs310project.crimemapper.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.gwt.cs310project.crimemapper.client.CrimeData;
import com.google.gwt.cs310project.crimemapper.client.CrimeDataByYear;
import com.google.gwt.cs310project.crimemapper.client.CrimeDataService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class CrimeDataServiceImpl extends RemoteServiceServlet implements CrimeDataService {

	// Data persistence fields
	//private static final Logger LOG = Logger.getLogger(CrimeMapper.class.getName());
	private static final PersistenceManagerFactory PMF =
			JDOHelper.getPersistenceManagerFactory("transactions-optional");

	public CrimeDataByYear getCrimeDataByYear(String url) {

		ArrayList<CrimeData> crimeDataList = new ArrayList<CrimeData>();
		crimeDataList = parseCrimeData(url);
		int year = 0;
		if(crimeDataList.size() > 0){
			year = crimeDataList.get(0).getYear();
		}
		return new CrimeDataByYear(year, crimeDataList);
	}

	public ArrayList<CrimeData> parseCrimeData(String url) {
		ArrayList<CrimeData> crimeDataList = new ArrayList<CrimeData>();
		try {
			URL crime = new URL(url);
			BufferedReader crimeIn = new BufferedReader(
					new InputStreamReader(crime.openStream()));
			String inputLine = crimeIn.readLine();
			int lineNumber = 1;
			// The first line of the CSV file contains no data
			while ((inputLine = crimeIn.readLine()) != null) {
				CrimeData cd = parseCrimeDataLine(inputLine);
				cd.setID(lineNumber);
				crimeDataList.add(cd);
				lineNumber++;
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

		if (type.equals("Commercial BE")){
			type = "Commercial Break and Enter";
		}

		if (type.equals("Theft From Auto Over  $5000")){
			type = "Theft From Auto Over $5000";
		}

		if (type.equals("Thef Of Auto Under $5000")) {
			type = "Theft Of Auto Under $5000";
		}

		int year = Integer.parseInt(sc.next());
		int month = Integer.parseInt(sc.next());
		String location = sc.next();
		sc.close();
		return new CrimeData(type, year, month, location);
	}

	@SuppressWarnings("unchecked")
	@Override
	public TreeMap<Integer, CrimeDataByYear> getCrimeDataMap() {
		TreeMap<Integer, CrimeDataByYear> crimeDataMap = null;
		PersistenceManager pm = getPersistenceManager();
		try {
			/*
			 * TODO: If crimeDataMap isn't the only TreeMap we have
			 * among all our persistent data, we might need to
			 * change this.
			 */
			Query q = pm.newQuery(GlobalPersistentData.class);
			List<GlobalPersistentData> globalPersistentDataList =
					(List<GlobalPersistentData>) q.execute();
			int size = globalPersistentDataList.size();
			assert size == 1 || size == 0;
			GlobalPersistentData globalPersistentData = getGlobalPersistentData();
			crimeDataMap = globalPersistentData.getCrimeDataMap();
			if (crimeDataMap == null) {
				crimeDataMap = new TreeMap<Integer, CrimeDataByYear>();
				globalPersistentData.setCrimeDataMap(crimeDataMap);
			}
		} finally {
			pm.close();
		}
		return new TreeMap<Integer, CrimeDataByYear>(crimeDataMap);
	}
	
	@Override
	public void setCrimeDataMap(TreeMap<Integer, CrimeDataByYear> crimeDataMap) {
		GlobalPersistentData globalPersistentData = getGlobalPersistentData();
		globalPersistentData.setCrimeDataMap(crimeDataMap);
	}

	@SuppressWarnings("unchecked")
	private GlobalPersistentData getGlobalPersistentData() {
		GlobalPersistentData globalPersistentData = null;
		PersistenceManager pm = getPersistenceManager();
		try {
			/*
			 * TODO: If crimeDataMap isn't the only TreeMap we have
			 * among all our persistent data, we might need to
			 * change this.
			 */
			Query q = pm.newQuery(GlobalPersistentData.class);
			List<GlobalPersistentData> globalPersistentDataList =
					(List<GlobalPersistentData>) q.execute();
			int size = globalPersistentDataList.size();
			assert size == 1 || size == 0;
			if (size == 0) {
				globalPersistentData = new GlobalPersistentData();
				globalPersistentData.setCrimeDataMap(new TreeMap<Integer, CrimeDataByYear>());
				pm.makePersistent(globalPersistentData);
			} else {
				globalPersistentData = globalPersistentDataList.get(0);
			}
		} finally {
			pm.close();
		}
		return globalPersistentData;
	}

	private PersistenceManager getPersistenceManager() {
		return PMF.getPersistenceManager();
	}
}
