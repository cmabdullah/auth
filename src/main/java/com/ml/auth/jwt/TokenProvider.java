package com.ml.auth.jwt;

import com.ml.auth.common.UserContext;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;

/*
 * @created 13/06/2021 - 10:26 AM
 * @project ClassifiedECommerce
 * @author C M Abdullah Khan
 * REF:
 */
public interface TokenProvider {

	String createAccessJwtToken(UserContext userContext);

	String createRefreshToken(UserContext userContext);

	boolean revokeToken(UserContext userContext);

	boolean validateToken(String authToken);

	Integer getUserIdFromToken(String token);

	Claims parseClaims(String token);

	@Deprecated
	String createToken(Authentication authentication);
}
