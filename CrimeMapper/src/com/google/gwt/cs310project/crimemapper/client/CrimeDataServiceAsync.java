package com.google.gwt.cs310project.crimemapper.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CrimeDataServiceAsync {
	void getCrimeData(String url, AsyncCallback<ArrayList<CrimeData>> callback);
}
