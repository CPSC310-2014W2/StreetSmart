package com.google.gwt.cs310project.crimemapper.server;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.cs310project.crimemapper.client.AdminAccount;
import com.google.gwt.cs310project.crimemapper.client.CrimeDataByYear;
import com.google.gwt.cs310project.crimemapper.client.LoginInfo;
import com.google.gwt.cs310project.crimemapper.client.LoginService;
import com.google.gwt.cs310project.crimemapper.client.NotLoggedInException;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.util.List;

import javax.jdo.PersistenceManager;

import com.google.gwt.cs310project.crimemapper.client.AdminAccount;

public class LoginServiceImpl extends RemoteServiceServlet implements
    LoginService {
	
	public void addAccount(String account){
		AccountManagement am = new AccountManagement();
	    am.writeXML(System.getProperty("user.dir")+"AdminAccount.xml", account);
	  }

  public LoginInfo login(String requestUri){
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    LoginInfo loginInfo = new LoginInfo();

    if (user != null) {
      loginInfo.setLoggedIn(true);
      loginInfo.setEmailAddress(user.getEmail());
      loginInfo.setNickname(user.getNickname());
      loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
      loginInfo.setAdmin(userService.isUserAdmin());
      
      AccountManagement am = new AccountManagement();
      List<String> list = am.readXML("AdminAccount.xml");
	  loginInfo.setAccountList(list);
      
      
    } else {
      loginInfo.setLoggedIn(false);
      loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
    }
    return loginInfo;
  }

}