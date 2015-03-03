package com.google.gwt.cs310project.crimemapper.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CrimeServiceAsync {
	public void addCrime(String year, AsyncCallback<Void> async);
	  public void removeCrime(String year, AsyncCallback<Void> async);
	  public void getCrimes(AsyncCallback<String[]> async);

}
