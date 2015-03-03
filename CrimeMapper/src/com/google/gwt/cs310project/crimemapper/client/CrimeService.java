package com.google.gwt.cs310project.crimemapper.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("crime")
public interface CrimeService extends RemoteService {
	  public void addCrime(String year) throws NotLoggedInException;
	  public void removeCrime(String year) throws NotLoggedInException;
	  public String[] getCrimes() throws NotLoggedInException;
	}
