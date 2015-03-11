package com.google.gwt.cs310project.crimemapper.server;

import java.util.List;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.cs310project.crimemapper.client.LoginInfo;
import com.google.gwt.cs310project.crimemapper.client.LoginService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;


@SuppressWarnings("serial")
public class LoginServiceImpl extends RemoteServiceServlet implements
    LoginService {
	
	public void addAccount(String account){
		AccountManagement am = new AccountManagement();
	    am.writeXML("AdminAccount.xml", account);
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