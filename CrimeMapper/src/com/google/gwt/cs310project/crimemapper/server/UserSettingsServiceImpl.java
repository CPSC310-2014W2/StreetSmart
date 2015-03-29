package com.google.gwt.cs310project.crimemapper.server;

import java.util.LinkedList;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.cs310project.crimemapper.client.NotLoggedInException;
import com.google.gwt.cs310project.crimemapper.client.UserSettingsService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class UserSettingsServiceImpl extends RemoteServiceServlet implements UserSettingsService{

	private static final PersistenceManagerFactory PMF =
			JDOHelper.getPersistenceManagerFactory("transactions-optional");

	public void setSelectedRow(int selectedRow) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try {
			getUserSettings(pm).setSelectedRow(selectedRow);
		} finally {
			pm.close();
		}
	}


	public int getSelectedRow() throws NotLoggedInException {
		checkLoggedIn();
		int selectedRow = 0;
		PersistenceManager pm = getPersistenceManager();
		try {
			selectedRow = getUserSettings(pm).getSelectedRow();
		} finally {
			pm.close();
		}
		return selectedRow;
	}
	
	@SuppressWarnings("unchecked")
	private UserSettings getUserSettings(PersistenceManager pm) {
		List<UserSettings> userSettingsList = null;
		Query q = pm.newQuery(UserSettings.class, "user == u");
		q.declareParameters("com.google.appengine.api.users.User u");
		userSettingsList = (List<UserSettings>) q.execute(getUser());
		int size = userSettingsList.size();
		UserSettings userSettings = null;
		assert size == 1 || size == 0;
		if (size == 0) {
			userSettings = new UserSettings(getUser());
			pm.makePersistent(userSettings);
		} else {
			userSettings = userSettingsList.get(0);
		}
		return userSettings;
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


	@Override
	public LinkedList<String> getSearchHistory() throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try {
			return pm.detachCopy(getUserSettings(pm)).getSearchHistory();
		} finally {
			pm.close();
		}
	}


	@Override
	public void addToSearchHistory(String searchTerm) throws NotLoggedInException {
		checkLoggedIn();
		// TODO Auto-generated method stub
		PersistenceManager pm = getPersistenceManager();
		try {
			
		} finally {
			
		}
	}


	@Override
	public void clearSearchHistory() throws NotLoggedInException {
		checkLoggedIn();
		// TODO Auto-generated method stub
		
	}
}
