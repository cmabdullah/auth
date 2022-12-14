package com.ml.auth.common;

import com.ml.auth.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/****
 * UserPrincipal will be used as UserContext
 * need to be polished highly
 */
public class UserPrincipal implements UserDetails {
	private Long id;
	private String email;
	private String password;
	private Collection<? extends GrantedAuthority> authorities;
	private Map<String, Object> attributes;
	private Instant lastPasswordChangeTime;

	public UserPrincipal(Long id, String email, String password, Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.authorities = authorities;
	}
	
	public UserPrincipal(Long id, String email, String password, Collection<? extends GrantedAuthority> authorities,
						 Instant lastPasswordChangeTime) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.authorities = authorities;
		this.lastPasswordChangeTime = lastPasswordChangeTime;
	}

	public static UserPrincipal create(User user) {

		List<GrantedAuthority> authorities = user.getRoles().stream()
				.filter(Objects::nonNull).map(role -> new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors.toList());

		return new UserPrincipal(
				user.getId(),
				user.getEmail(),
				user.getPassword(),
				authorities,
				user.getLastPasswordChangeTime()
		);
	}

	public static UserPrincipal create(User user, Map<String, Object> attributes) {
		UserPrincipal userPrincipal = UserPrincipal.create(user);
		userPrincipal.setAttributes(attributes);
		return userPrincipal;
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	//@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	//@Override
	public String getName() {
		return String.valueOf(id);
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public Instant getLastPasswordChangeTime() {
		return lastPasswordChangeTime;
	}
}
