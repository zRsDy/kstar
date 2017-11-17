package com.ibm.kstar.conf;

public class AdminConfiguration {

	private String[] admins;

	public String[] getAdmins() {
		return admins;
	}

	public void setAdmins(String[] admins) {
		this.admins = admins;
	}
	
	public boolean isAdmin(String username){
		for(String admin : admins){
			if(admin.equals(username)){
				return true;
			}
		}
		return false;
	}
	
}
