package com.google.gwt.cs310project.crimemapper.client;

import com.google.gwt.cs310project.crimemapper.client.LoginInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {
	public void login(String requestUri, AsyncCallback<LoginInfo> async);
}