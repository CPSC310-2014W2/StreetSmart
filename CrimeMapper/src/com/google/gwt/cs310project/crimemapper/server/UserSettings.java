package com.google.gwt.cs310project.crimemapper.server;

import java.util.Date;
import java.util.LinkedList;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.users.User;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class UserSettings {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	private User user;
	@Persistent
	private Date createDate;
	@Persistent
	private int selectedRow = -1;
	@Persistent(serialized = "true", defaultFetchGroup="true")
	private LinkedList<String> searchHistory;
	
	private static final int MAX_SEARCH_HISTORY_SIZE = 5;

	public UserSettings() {
		this.createDate = new Date();
	}
	
	public UserSettings(User user) {
		this();
		this.user = user;
	}

	public UserSettings(User user, int selectedRow) {
		this();
		this.user = user;
		this.selectedRow = selectedRow;
	}

	public Long getId() {
		return this.id;
	}

	public User getUser() {
		return this.user;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public int getSelectedRow() {
		return this.selectedRow;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setSelectedRow(int selectedRow) {
		this.selectedRow = selectedRow;
	}
	
	public LinkedList<String> getSearchHistory() {
		return this.searchHistory;
	}
	
	public void addToSearchHistory(String searchTerm) {
		if (searchHistory.size() > MAX_SEARCH_HISTORY_SIZE) {
			searchHistory.removeLast();
		}
		searchHistory.addFirst(searchTerm);
	}
	
	public void clearSearchHistory() {
		searchHistory.clear();
	}
}
