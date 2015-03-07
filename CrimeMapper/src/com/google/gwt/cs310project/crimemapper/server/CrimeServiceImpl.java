package com.google.gwt.cs310project.crimemapper.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.cs310project.crimemapper.client.CrimeDataByYear;
import com.google.gwt.cs310project.crimemapper.client.NotLoggedInException;
import com.google.gwt.cs310project.crimemapper.client.CrimeService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class CrimeServiceImpl extends RemoteServiceServlet implements CrimeService{

	 private static final Logger LOG = Logger.getLogger(CrimeServiceImpl.class.getName());
	  private static final PersistenceManagerFactory PMF =
	      JDOHelper.getPersistenceManagerFactory("transactions-optional");

	  public void addCrime(CrimeDataByYear crimeDBYear) throws NotLoggedInException {
	    checkLoggedIn();
	    PersistenceManager pm = getPersistenceManager();
	    try {
	      pm.makePersistent(new Crime(getUser(), crimeDBYear));
	    } finally {
	      pm.close();
	    }
	  }


	  public CrimeDataByYear[] getCrimes() throws NotLoggedInException {
	    checkLoggedIn();
	    PersistenceManager pm = getPersistenceManager();
	    List<CrimeDataByYear> crimeDBYear = new ArrayList<CrimeDataByYear>();
	    try {
	      Query q = pm.newQuery(Crime.class, "user == u");
	      q.declareParameters("com.google.appengine.api.users.User u");
	      q.setOrdering("createDate");
	      List<Crime> crimes = (List<Crime>) q.execute(getUser());
	      for (Crime crime : crimes) {
	        crimeDBYear.add(crime.getCrimeDBYear());
	      }
	    } finally {
	      pm.close();
	    }
	    return (CrimeDataByYear[]) crimeDBYear.toArray(new CrimeDataByYear[0]);
	  }

	  private void checkLoggedIn() throws NotLoggedInException {
	    if (getUser() == null) {
	      throw new NotLoggedInException("Not logged in.");
	    }
	  }

	  private User getUser() {
	    UserService userService = UserServiceFactory.getUserService();
	    return userService.getCurrentUser();
	  }

	  private PersistenceManager getPersistenceManager() {
	    return PMF.getPersistenceManager();
	  }
	}
