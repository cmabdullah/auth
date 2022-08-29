package com.ml.auth.jwt;

import com.ml.auth.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 * @created 15/06/2021 - 1:11 AM
 * @project ClassifiedECommerce
 * @author C M Abdullah Khan
 * REF:
 */
@Service
public class TokenAuthenticationFilter extends OncePerRequestFilter  {

	private final TokenProvider tokenProvider;
	private final CustomUserDetailsService customUserDetailsService;

	@Autowired
	public TokenAuthenticationFilter(TokenProvider tokenProvider, @Lazy CustomUserDetailsService customUserDetailsService){
		this.tokenProvider = tokenProvider;
		this.customUserDetailsService = customUserDetailsService;
	}

//	public TokenAuthenticationFilter(){
//
//	}


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String jwt = getJwtFromRequest(request);

		if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
//			Integer userId = tokenProvider.getUserIdFromToken(jwt);
			Claims claims = tokenProvider.parseClaims(jwt);
			String userEmail = claims.getSubject();
			UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);
			UsernamePasswordAuthenticationToken authentication =
					new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		filterChain.doFilter(request, response);
	}

	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
	}
}