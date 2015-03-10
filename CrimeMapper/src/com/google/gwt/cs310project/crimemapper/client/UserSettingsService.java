package com.google.gwt.cs310project.crimemapper.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("userSettings")
public interface UserSettingsService extends RemoteService {
	  public void setSelectedRow(int selectedRow) throws NotLoggedInException;
	  public int getSelectedRow() throws NotLoggedInException;
	}
