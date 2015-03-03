package com.google.gwt.cs310project.crimemapper.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CrimeDataServiceAsync {
	ArrayList<CrimeData> getCrimeData(String url, AsyncCallback<ArrayList<CrimeData>> async);
}
