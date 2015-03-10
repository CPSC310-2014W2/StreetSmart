package com.google.gwt.cs310project.crimemapper.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserSettingsServiceAsync {
	public void setSelectedRow(int selectedRow, AsyncCallback<Void> async);
	public void getSelectedRow(AsyncCallback<Integer> async);

}
