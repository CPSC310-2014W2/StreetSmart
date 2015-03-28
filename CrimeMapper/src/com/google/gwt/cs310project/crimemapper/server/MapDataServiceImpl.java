package com.google.gwt.cs310project.crimemapper.server;

import java.util.ArrayList;
import java.util.TreeMap;

import com.google.gwt.cs310project.crimemapper.client.CrimeData;
import com.google.gwt.cs310project.crimemapper.client.LatLon;
import com.google.gwt.cs310project.crimemapper.client.MapDataService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;


@SuppressWarnings("serial")
public class MapDataServiceImpl extends RemoteServiceServlet implements
		MapDataService {

	@Override
	public TreeMap<LatLon, Integer> getMapData(ArrayList<CrimeData> crimeDataList) {
		// TODO Auto-generated method stub
		return null;
	}

}
