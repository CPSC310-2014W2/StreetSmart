package com.google.gwt.cs310project.crimemapper.server;

import java.util.Date;

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
	  private int selectedRow;

	  public UserSettings() {
	    this.createDate = new Date();
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
	}
	