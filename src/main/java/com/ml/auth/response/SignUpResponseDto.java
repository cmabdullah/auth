package com.ml.auth.response;

/**
 * BookWormV2
 * Created on 19/8/22 - Friday
 * User Khan, C M Abdullah
 * Ref:
 */
public class SignUpResponseDto {

	private String email;
	private String message;

	public SignUpResponseDto() {
	}

	public SignUpResponseDto(String email, String message) {
		this.email = email;
		this.message = message;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
