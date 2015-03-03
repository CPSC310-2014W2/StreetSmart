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
import com.google.gwt.cs310project.crimemapper.client.NotLoggedInException;
import com.google.gwt.cs310project.crimemapper.client.CrimeService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class CrimeServiceImpl extends RemoteServiceServlet implements CrimeService{

	 private static final Logger LOG = Logger.getLogger(CrimeServiceImpl.class.getName());
	  private static final PersistenceManagerFactory PMF =
	      JDOHelper.getPersistenceManagerFactory("transactions-optional");

	  public void addCrime(String year) throws NotLoggedInException {
	    checkLoggedIn();
	    PersistenceManager pm = getPersistenceManager();
	    try {
	      pm.makePersistent(new Crime(getUser(), year));
	    } finally {
	      pm.close();
	    }
	  }

	  public void removeCrime(String year) throws NotLoggedInException {
	    checkLoggedIn();
	    PersistenceManager pm = getPersistenceManager();
	    try {
	      long deleteCount = 0;
	      Query q = pm.newQuery(Crime.class, "user == u");
	      q.declareParameters("com.google.appengine.api.users.User u");
	      List<Crime> crimes = (List<Crime>) q.execute(getUser());
	      for (Crime crime : crimes) {
	        if (year.equals(crime.getYear())) {
	          deleteCount++;
	          pm.deletePersistent(crime);
	        }
	      }
	      if (deleteCount != 1) {
	        LOG.log(Level.WARNING, "removeCrime deleted "+deleteCount+" Crimes");
	      }
	    } finally {
	      pm.close();
	    }
	  }

	  public String[] getCrimes() throws NotLoggedInException {
	    checkLoggedIn();
	    PersistenceManager pm = getPersistenceManager();
	    List<String> years = new ArrayList<String>();
	    try {
	      Query q = pm.newQuery(Crime.class, "user == u");
	      q.declareParameters("com.google.appengine.api.users.User u");
	      q.setOrdering("createDate");
	      List<Crime> crimes = (List<Crime>) q.execute(getUser());
	      for (Crime crime : crimes) {
	        years.add(crime.getYear());
	      }
	    } finally {
	      pm.close();
	    }
	    return (String[]) years.toArray(new String[0]);
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
