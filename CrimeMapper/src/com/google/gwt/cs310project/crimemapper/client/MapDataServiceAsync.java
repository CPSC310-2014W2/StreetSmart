package com.google.gwt.cs310project.crimemapper.client;

import java.util.ArrayList;
import java.util.TreeMap;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MapDataServiceAsync {
	void getMapData(ArrayList<CrimeData> crimeDataList, AsyncCallback<TreeMap<LatLon, Integer>> callback);	
}
