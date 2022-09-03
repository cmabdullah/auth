package com.ml.auth.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * BookWormV2
 * Created on 19/8/22 - Friday
 * User Khan, C M Abdullah
 * Ref:
 */
public class PasswordChangedRequestDto {
	@NotBlank
	@Email
	private String email;
	@NotBlank
	private String oldPassword;
	@NotBlank
	private String newPassword;
	@NotBlank
	private String retypePassword;
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getOldPassword() {
		return oldPassword;
	}
	
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	
	public String getNewPassword() {
		return newPassword;
	}
	
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
	public String getRetypePassword() {
		return retypePassword;
	}
	
	public void setRetypePassword(String retypePassword) {
		this.retypePassword = retypePassword;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PasswordChangedRequestDto that = (PasswordChangedRequestDto) o;
		return Objects.equals(email, that.email) && Objects.equals(oldPassword, that.oldPassword) && Objects.equals(newPassword, that.newPassword) && Objects.equals(retypePassword, that.retypePassword);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(email, oldPassword, newPassword, retypePassword);
	}
	
	@Override
	public String toString() {
		return "PasswordChangedRequestDto{" +
					   "email='" + email + '\'' +
					   ", oldPassword='" + oldPassword + '\'' +
					   ", newPassword='" + newPassword + '\'' +
					   ", retypePassword='" + retypePassword + '\'' +
					   '}';
	}
}
