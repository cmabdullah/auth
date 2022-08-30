package com.ml.auth.constants;

/**
 * Reusable Lib
 * Created on 29/8/22 - Monday
 * User Khan, C M Abdullah
 * Ref:
 */
public enum UserStatus {
	CREATED("created"),
	ACTIVE("active"),
	DELETED("deleted"),
	PASSWORD_CHANGED("passwordChanged"),
	NOT_EXIST("notExist");
	
	private final String status;
	UserStatus(String status){
		this.status = status;
	}
	
	public String getStatus() {
		return status;
	}
}
