package com.ml.auth.jwt;

import com.ml.auth.common.UserContext;
import com.ml.auth.common.UserPrincipal;
import com.ml.auth.service.UserService;
import com.ml.coreweb.exception.ApiError;
import com.ml.coreweb.util.DateTimeUtil;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
	private final UserService userService;

	@Autowired
	private TokenProviderImpl(JwtProperties jwtProperties,
							  @Lazy UserService userService) {
		this.jwtProperties = jwtProperties;
		this.userService = userService;
	}

	@Override
	public String createAccessJwtToken(UserContext userContext) {
		String userId = String.valueOf(userContext.getUserId());
		String userEmail = userContext.getEmail();
		Claims claims = Jwts.claims().setSubject(userEmail).setId(userId);
		claims.put(JWT_CONTEXT, userContext);
		long accessTokenValidity = Long.parseLong(jwtProperties.getAccessTokenValidity());
		return getJwt(userEmail, userId, claims, accessTokenValidity);
	}

	@Override
	public String createRefreshToken(UserContext userContext) {
		String userId = String.valueOf(userContext.getUserId());
		String userEmail = userContext.getEmail();
		Claims claims = Jwts.claims().setSubject(userEmail).setId(userId);
		claims.put(JWT_CONTEXT, userContext);
		long refreshTokenValidity = Long.parseLong(jwtProperties.getRefreshTokenValidity());
		return getJwt(userEmail, userId, claims, refreshTokenValidity);
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
	
	@Override
	public boolean isTokenValid(String authToken) {
		try {
			if (StringUtils.isBlank(authToken)) {
				return false;
			}
			parseClaims(authToken);
			return true;
		} catch (Exception e){
			log.error("token.not.valid {}", e.getLocalizedMessage());
			return false;
		}
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
		Claims claims = Jwts.claims().setSubject(userEmail).setId(userId);
		List<String> authorities = userPrincipal.getAuthorities().stream()
						.filter(Objects::nonNull).map(GrantedAuthority::getAuthority)
										   .collect(Collectors.toList());
		
		Map<String, Object> userContext =
				Map.of(USER_ID, userId,
				USER_EMAIL, userEmail,
				ROLES, authorities);
		
		claims.put(JWT_CONTEXT, userContext);
		long tokenValidity = Long.parseLong(jwtProperties.getAccessTokenValidity());
		return getJwt(userEmail, userId, claims, tokenValidity);
	}
	
	private String getJwt(String userEmail, String userId, Claims claims, long tokenValidity) {
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
