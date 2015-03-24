package com.google.gwt.cs310project.crimemapper.server;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.net.*;
import java.io.*;
import java.util.List;

import javax.jdo.JDOException;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.gwt.cs310project.crimemapper.client.CrimeData;
import com.google.gwt.cs310project.crimemapper.client.CrimeDataByYear;
import com.google.gwt.cs310project.crimemapper.client.CrimeDataService;
import com.google.gwt.cs310project.crimemapper.client.CrimeTypes;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class CrimeDataServiceImpl extends RemoteServiceServlet implements CrimeDataService {

	// Data persistence fields
	private static final PersistenceManagerFactory PMF =
			JDOHelper.getPersistenceManagerFactory("transactions-optional");

	public CrimeDataByYear getCrimeDataByYear(String url) {
		// TODO: Shouldn't we make this method static?

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

	@Override
	public TreeMap<Integer, CrimeDataByYear> getCrimeDataMap() {
		TreeMap<Integer, CrimeDataByYear> crimeDataMap = null;
		PersistenceManager pm = getPersistenceManager();
		try {
			GlobalPersistentData globalPersistentData = getGlobalPersistentData(pm);
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
		PersistenceManager pm = getPersistenceManager();
		try {
			getGlobalPersistentData(pm).setCrimeDataMap(crimeDataMap);
		} finally {
			pm.close();
		}
	}

	@Override
	public ArrayList<String> getAdminAccounts() {
		ArrayList<String> adminAcct = null;
		PersistenceManager pm = getPersistenceManager();
		try {
			GlobalPersistentData globalPersistentData = getGlobalPersistentData(pm);
			adminAcct = globalPersistentData.getAdminAccounts();
			if (adminAcct == null) {
				adminAcct = new ArrayList<String>();
				globalPersistentData.setAdminAccounts(adminAcct);
			}
		} finally {
			pm.close();
		}
		return new ArrayList<String>();
	}

	@Override
	public void setAdminAccounts(ArrayList<String> admins) {
		PersistenceManager pm = getPersistenceManager();
		try {
			getGlobalPersistentData(pm).setAdminAccounts(admins);
		} finally {
			pm.close();
		}
	}

	private GlobalPersistentData getGlobalPersistentData(PersistenceManager pm) {
		GlobalPersistentData globalPersistentData = null;
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
		return globalPersistentData;
	}

	private PersistenceManager getPersistenceManager() {
		return PMF.getPersistenceManager();
	}

	@Override
	public void addPersistentCrimeDataByYear(CrimeDataByYear crimeDataByYear) {
		PersistenceManager pm = getPersistenceManager();
		try {
			/*for (Map.Entry<String, ArrayList<CrimeData>> i : crimeDataByYear.getCrimesDataList().entrySet()) {
				for (CrimeData j : i.getValue()) {
					pm.makePersistent(j);
				}
			}*/
			pm.makePersistent(crimeDataByYear);
		} finally {
			pm.close();
		}
	}

	@Override
	public CrimeDataByYear getPersistentCrimeDataByYear(int year) {
		PersistenceManager pm = getPersistenceManager();
		CrimeDataByYear crimeDataByYear = null;
		/*ArrayList<CrimeData> crimes = new ArrayList<CrimeData>();
		try {
			for (int i = 0; i < CrimeTypes.getNumberOfTypes(); i++) {
				String crimeType = CrimeTypes.getType(i);
				for (int month = 1; month <= 12; month++) {
					Query q = pm.newQuery(CrimeData.class);
					q.declareParameters("Integer y, Integer m, String t");
					q.setFilter("year == y && month == m && type == t");
					List<CrimeData> crimeDataList = (List<CrimeData>) q.execute(year, month, crimeType);
					if (!crimeDataList.isEmpty()) {
						for (CrimeData crimeData : crimeDataList) {
							crimes.add((CrimeData) pm.detachCopy(crimeData));
						}
					}
				}
			}
			if (!crimes.isEmpty()) {
				crimeDataByYear = new CrimeDataByYear(year, crimes);
			}
		} finally {
			pm.close();
		}*/
		try {
			Query q = pm.newQuery(CrimeDataByYear.class);
			q.declareParameters("Integer y");
			q.setFilter("year == y");
			List<CrimeDataByYear> cdbyList = (List<CrimeDataByYear>) q.execute(year);
			assert cdbyList.size() == 1 || cdbyList.size() == 0;
			if (cdbyList.size() == 1) {
				crimeDataByYear = pm.detachCopy(cdbyList.get(0));
			}
		} finally {
			pm.close();
		}
		return crimeDataByYear;
	}
}
