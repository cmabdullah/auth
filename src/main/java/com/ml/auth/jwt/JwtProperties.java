package com.ml.auth.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;

/*
 * @created 13/06/2021 - 11:24 AM
 * @project ClassifiedECommerce
 * @author C M Abdullah Khan
 * REF:
 */
@Component
public class JwtProperties {

	@Value("${auth.client.id}")
	private String clientId;
	@Value("${auth.client.secret}")
	private String clientSecret;
	@Value("${auth.token.type}")
	private String tokenType;
	@Value("${auth.token.private-signing-key}")
	private String tokenPrivateSigningKey;
	@Value("${auth.token.public-verifier-key}")
	private String tokenPrivateVerifierKey;
	@Value("${token.validity}")
	private String tokenValidity;

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getTokenValidity() {
		return tokenValidity;
	}

	public void setTokenValidity(String tokenValidity) {
		this.tokenValidity = tokenValidity;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public String getTokenPrivateSigningKey() {
		return tokenPrivateSigningKey;
	}

	public void setTokenPrivateSigningKey(String tokenPrivateSigningKey) {
		this.tokenPrivateSigningKey = tokenPrivateSigningKey;
	}

	public String getTokenPrivateVerifierKey() {
		return tokenPrivateVerifierKey;
	}

	public void setTokenPrivateVerifierKey(String tokenPrivateVerifierKey) {
		this.tokenPrivateVerifierKey = tokenPrivateVerifierKey;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof JwtProperties)) return false;
		JwtProperties that = (JwtProperties) o;
		return Objects.equals(getClientId(), that.getClientId()) &&
				Objects.equals(getClientSecret(), that.getClientSecret()) &&
				Objects.equals(getTokenValidity(), that.getTokenValidity()) &&
				Objects.equals(getTokenType(), that.getTokenType()) &&
				Objects.equals(getTokenPrivateSigningKey(), that.getTokenPrivateSigningKey()) &&
				Objects.equals(getTokenPrivateVerifierKey(), that.getTokenPrivateVerifierKey());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getClientId(), getClientSecret(), getTokenValidity(), getTokenType(), getTokenPrivateSigningKey(), getTokenPrivateVerifierKey());
	}

	@Override
	public String toString() {
		return "JwtProperties{" +
				"clientId='" + clientId + '\'' +
				", clientSecret='" + clientSecret + '\'' +
				", tokenValidity='" + tokenValidity + '\'' +
				", tokenType=" + tokenType +
				", tokenPrivateSigningKey='" + tokenPrivateSigningKey + '\'' +
				", tokenPrivateVerifierKey='" + tokenPrivateVerifierKey + '\'' +
				'}';
	}
}
