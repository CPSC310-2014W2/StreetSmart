package com.google.gwt.cs310project.crimemapper.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.ArrayList;
import java.util.TreeMap;

@RemoteServiceRelativePath("crimeData")
public interface CrimeDataService extends RemoteService {
	CrimeDataByYear getCrimeDataByYear(String url);
	TreeMap<Integer, CrimeDataByYear> getCrimeDataMap();
	void setCrimeDataMap(TreeMap<Integer, CrimeDataByYear> crimeDataMap);
	void addPersistentCrimeDataByYear(CrimeDataByYear crimeDataByYear);
	CrimeDataByYear getPersistentCrimeDataByYear(int year);
	ArrayList<String> getAdminAccounts();
	void setAdminAccounts(ArrayList<String> admins);
}
