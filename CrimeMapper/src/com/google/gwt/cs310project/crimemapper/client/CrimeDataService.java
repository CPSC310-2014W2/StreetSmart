package com.google.gwt.cs310project.crimemapper.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.ArrayList;

@RemoteServiceRelativePath("crimeData")
public interface CrimeDataService extends RemoteService {
	ArrayList<CrimeData> getCrimeData(String url);
}
