package com.google.gwt.cs310project.crimemapper.server;

import org.eclipse.jetty.security.IdentityService;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.server.UserIdentity;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.cs310project.crimemapper.client.LoginInfo;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {

	public LoginInfo login(String requestUri){
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		LoginInfo loginInfo = new LoginInfo();
		
		if(user != null){
			loginInfo.setLoggedIn(true);
			loginInfo.setEmailAddress(user.getEmail());
		      loginInfo.setNickname(user.getNickname());
		      loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
		    } else {
		      loginInfo.setLoggedIn(false);
		      loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
		    }
		    return loginInfo;
		  }
	}
