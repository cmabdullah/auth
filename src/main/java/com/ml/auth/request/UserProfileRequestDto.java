package com.ml.auth.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * BookWormV2
 * Created on 19/8/22 - Friday
 * User Khan, C M Abdullah
 * Ref:
 */

public class UserProfileRequestDto {
	@NotBlank
	@Email
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
