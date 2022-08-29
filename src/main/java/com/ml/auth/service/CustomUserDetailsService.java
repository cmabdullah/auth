package com.ml.auth.service;

import com.ml.auth.common.UserPrincipal;
import com.ml.auth.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * BookWormV2
 * Created on 19/8/22 - Friday
 * User Khan, C M Abdullah
 * Ref:
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserService userService;

	@Autowired
	public CustomUserDetailsService(UserService userService) {
		this.userService = userService;
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userService.findByEmail(email)
				.orElseThrow(() ->
						new UsernameNotFoundException("User not found with email : " + email)
				);
		return UserPrincipal.create(user);
	}

	@Transactional
	public UserDetails loadUserById(Long id) {
		User user = userService.findById(id).orElseThrow(
				() -> new UsernameNotFoundException(String.format("%s not found with %s : '%s'", "User", "id", id)));
		return UserPrincipal.create(user);
	}
}
