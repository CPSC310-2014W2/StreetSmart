package com.google.gwt.cs310project.crimemapper.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MapDataServiceAsync {
	void getHereMapData(ArrayList<CrimeData> crimeDataList, AsyncCallback<ArrayList<LatLon>> callback);	
}
