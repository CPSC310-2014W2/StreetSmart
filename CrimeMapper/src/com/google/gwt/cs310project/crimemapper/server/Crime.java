package com.google.gwt.cs310project.crimemapper.server;

import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.users.User;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Crime {
	@PrimaryKey
	  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	  private Long id;
	  @Persistent
	  private User user;
	  @Persistent
	  private String year;
	  @Persistent
	  private Date createDate;

	  public Crime() {
	    this.createDate = new Date();
	  }

	  public Crime(User user, String year) {
	    this();
	    this.user = user;
	    this.year = year;
	  }

	  public Long getId() {
	    return this.id;
	  }

	  public User getUser() {
	    return this.user;
	  }

	  public String getYear() {
	    return this.year;
	  }

	  public Date getCreateDate() {
	    return this.createDate;
	  }

	  public void setUser(User user) {
	    this.user = user;
	  }

	  public void setYear(String year) {
	    this.year = year;
	  }
	}
	