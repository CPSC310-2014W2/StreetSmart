package com.google.gwt.cs310project.crimemapper.client;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.ArrayList;
import java.util.TreeMap;

@RemoteServiceRelativePath("mapData")
public interface MapDataService extends RemoteService {
	
	ArrayList<LatLon> getHereMapData(ArrayList<CrimeData> crimeDataList); 
}
