package com.google.gwt.cs310project.crimemapper.client;

import java.util.ArrayList;
import java.util.TreeMap;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CrimeDataServiceAsync {
	void getCrimeDataByYear(String url, AsyncCallback<CrimeDataByYear> callback);
	void getCrimeDataMap(AsyncCallback<TreeMap<Integer, CrimeDataByYear>> callback);
	void setCrimeDataMap(TreeMap<Integer, CrimeDataByYear> crimeDataMap, AsyncCallback<Void> callback);
	void setAdminAccounts(ArrayList<String> adminAccounts, AsyncCallback<Void> callback);
	void getAdminAccounts(AsyncCallback<ArrayList<String>> callback);
}
