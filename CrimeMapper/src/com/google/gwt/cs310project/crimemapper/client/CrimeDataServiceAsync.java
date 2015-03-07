package com.google.gwt.cs310project.crimemapper.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CrimeDataServiceAsync {
	void getCrimeDataByYear(String url, AsyncCallback<CrimeDataByYear> callback);
}
