package com.google.gwt.cs310project.crimemapper.client;

import java.util.LinkedList;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserSettingsServiceAsync {
	public void setSelectedRow(int selectedRow, AsyncCallback<Void> async);
	public void getSelectedRow(AsyncCallback<Integer> async);
	public void getSearchHistory(AsyncCallback<LinkedList<String>> callback);
	public void addToSearchHistory(String searchTerm, AsyncCallback<Void> callback);
	public void clearSearchHistory(AsyncCallback<Void> callback);
}
