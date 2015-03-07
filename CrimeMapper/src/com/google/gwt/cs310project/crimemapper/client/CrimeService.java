package com.google.gwt.cs310project.crimemapper.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("crime")
public interface CrimeService extends RemoteService {
	  public void addCrime(CrimeDataByYear crimeDBYear) throws NotLoggedInException;
	  public CrimeDataByYear[] getCrimes() throws NotLoggedInException;
	}
