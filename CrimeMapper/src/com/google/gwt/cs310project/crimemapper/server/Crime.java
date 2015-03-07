package com.google.gwt.cs310project.crimemapper.server;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.users.User;
import com.google.gwt.cs310project.crimemapper.client.CrimeDataByYear;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Crime {
	@PrimaryKey
	  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	  private Long id;
	  @Persistent
	  private User user;
	  @Persistent
	  private CrimeDataByYear crimeDBYear;
	  @Persistent
	  private Date createDate;

	  public Crime() {
	    this.createDate = new Date();
	  }

	  public Crime(User user, CrimeDataByYear crimeDBYear) {
	    this();
	    this.user = user;
	    this.crimeDBYear = crimeDBYear;
	  }

	  public Long getId() {
	    return this.id;
	  }

	  public User getUser() {
	    return this.user;
	  }

	  public CrimeDataByYear getCrimeDBYear() {
	    return this.crimeDBYear;
	  }

	  public Date getCreateDate() {
	    return this.createDate;
	  }

	  public void setUser(User user) {
	    this.user = user;
	  }

	  public void setCrimeDBYear(CrimeDataByYear crimeDBYear) {
	    this.crimeDBYear = crimeDBYear;
	  }
	}
	