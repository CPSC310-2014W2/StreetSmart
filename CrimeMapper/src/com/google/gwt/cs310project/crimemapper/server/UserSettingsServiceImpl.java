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
import com.google.gwt.cs310project.crimemapper.client.UserSettingsService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class UserSettingsServiceImpl extends RemoteServiceServlet implements UserSettingsService{

	private static final Logger LOG = Logger.getLogger(UserSettingsServiceImpl.class.getName());
	private static final PersistenceManagerFactory PMF =
			JDOHelper.getPersistenceManagerFactory("transactions-optional");

	public void setSelectedRow(int selectedRow) throws NotLoggedInException {
		checkLoggedIn();
		getUserSettings().setSelectedRow(selectedRow);
	}


	public int getSelectedRow() throws NotLoggedInException {
		checkLoggedIn();
		int selectedRow = 0;
		selectedRow = getUserSettings().getSelectedRow();
		return selectedRow;
	}
	
	private UserSettings getUserSettings() {
		PersistenceManager pm = getPersistenceManager();
		List<UserSettings> userSettings = null;
		try {
			Query q = pm.newQuery(UserSettings.class, "user == u");
			q.declareParameters("com.google.appengine.api.users.User u");
			userSettings = (List<UserSettings>) q.execute(getUser());
			assert userSettings.size() == 1;
		} finally {
			pm.close();
		}
		return userSettings.get(0);
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
