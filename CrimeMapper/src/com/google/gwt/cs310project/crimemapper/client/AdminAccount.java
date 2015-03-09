package com.google.gwt.cs310project.crimemapper.client;

public class AdminAccount {
	//private String ID;
    private String Address;
    
    public AdminAccount() {}

    public AdminAccount(String Address) {
    	this.Address = Address;
    }

	@Override
    public String toString() {
            return "<"+Address +">";
    }
}
