package com.google.gwt.cs310project.crimemapper.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CrimeServiceAsync {
	public void addCrime(CrimeDataByYear crimeDBYear, AsyncCallback<Void> async);
	public void getCrimes(AsyncCallback<CrimeDataByYear[]> async);

}
