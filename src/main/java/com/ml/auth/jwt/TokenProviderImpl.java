package com.ml.auth.jwt;

import com.ml.auth.common.UserContext;
import com.ml.auth.common.UserPrincipal;
import com.ml.coreweb.exception.ApiError;
import com.ml.coreweb.util.DateTimeUtil;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.ml.auth.constants.AuthConstants.*;


/*
 * @created 13/06/2021 - 10:37 AM
 * @project ClassifiedECommerce
 * @author C M Abdullah Khan
 * REF:
 */
@Slf4j
@Service
public class TokenProviderImpl implements TokenProvider {

	private final JwtProperties jwtProperties;

	@Autowired
	private TokenProviderImpl(JwtProperties jwtProperties) {
		this.jwtProperties = jwtProperties;
	}

	@Override
	public String createAccessJwtToken(UserContext userContext) {
		String userId = String.valueOf(userContext.getUserId());

		Claims claims = Jwts.claims().setSubject(userId).setId(userId);
		claims.put(JWT_CONTEXT, userContext);

		long tokenValidity = Long.parseLong(jwtProperties.getTokenValidity());

		ZonedDateTime currentTime = DateTimeUtil.timeNow();

		ZonedDateTime expirationZDT = currentTime.plusSeconds(tokenValidity);

		Date issuedDate = DateTimeUtil.dateFromZonedDateTimeToNow();
		Date expirationDate = DateTimeUtil.dateFromZonedDateTimeToNow(expirationZDT);

		return Jwts.builder()
				.setSubject(userId)//user id
				.setClaims(claims)
				.setId(userId)
				//.setIssuer()
				.setIssuedAt(issuedDate)
				.setExpiration(expirationDate)
				.signWith(SignatureAlgorithm.HS512, jwtProperties.getClientSecret())
				.compact();
	}

	@Override
	public String createRefreshToken(UserContext userContext) {
		return null;
	}

	@Override
	public boolean revokeToken(UserContext userContext) {
		return false;
	}

	@Override
	public boolean validateToken(String authToken) {
		parseClaims(authToken);
		return true;
	}

	@Deprecated
	public Integer getUserIdFromToken(String token) {
		Claims claims = Jwts.parser()
				.setSigningKey(jwtProperties.getClientSecret())
				.parseClaimsJws(token)
				.getBody();

		return Integer.parseInt(claims.getSubject());
	}

	@Override
	public Claims parseClaims(String token) {
		try {
			return Jwts.parser().setSigningKey(jwtProperties.getClientSecret()).parseClaimsJws(token).getBody();
		} catch (SignatureException ex) {
			log.error("Invalid JWT signature");
			throw new ApiError(INVALID_JWT_SIGNATURE, HttpStatus.UNAUTHORIZED);
		} catch (MalformedJwtException ex) {
			log.error("Invalid JWT token");
			throw new ApiError(INVALID_JWT_SIGNATURE, HttpStatus.UNAUTHORIZED);
		} catch (ExpiredJwtException ex) {
			log.error("Expired JWT token");
			throw new ApiError(INVALID_JWT_SIGNATURE, HttpStatus.UNAUTHORIZED);
		} catch (UnsupportedJwtException ex) {
			log.error("Unsupported JWT token");
			throw new ApiError(INVALID_JWT_SIGNATURE, HttpStatus.UNAUTHORIZED);
		} catch (IllegalArgumentException ex) {
			log.error("JWT claims string is empty.");
			throw new ApiError(INVALID_JWT_SIGNATURE, HttpStatus.UNAUTHORIZED);
		} catch (JwtException ex) {
			log.error("JWT exception");
			throw new ApiError(INVALID_JWT_SIGNATURE, HttpStatus.UNAUTHORIZED);
		}
	}

	@Override
	public String createToken(Authentication authentication) {
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		String userEmail = userPrincipal.getEmail();
		String userId = String.valueOf(userPrincipal.getId());
		Claims claims = Jwts.claims().setSubject(userEmail).setId(userEmail);

		Map<String, Object> userContext = new HashMap<>();
		userContext.put(USER_ID, userId);
		userContext.put(USER_EMAIL, userEmail);
		userContext.put(USER_NAME, "");
		userContext.put(ROLES, "");
		claims.put(JWT_CONTEXT, userContext);

		long tokenValidity = Long.parseLong(jwtProperties.getTokenValidity());

		ZonedDateTime currentTime = DateTimeUtil.timeNow();

		ZonedDateTime expirationZDT = currentTime.plusSeconds(tokenValidity);

		Date issuedDate = DateTimeUtil.dateFromZonedDateTimeToNow();
		Date expirationDate = DateTimeUtil.dateFromZonedDateTimeToNow(expirationZDT);

		return Jwts.builder()
				.setSubject(userEmail)//user id
				.setClaims(claims)
				.setId(userId)
				.setIssuer(ISSUER)
				.setIssuedAt(issuedDate)
				.setExpiration(expirationDate)
				.signWith(SignatureAlgorithm.HS512, jwtProperties.getClientSecret())
				.compact();
	}
}
