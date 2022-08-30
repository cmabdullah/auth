package com.ml.auth.common;

import com.ml.auth.domain.Role;
import lombok.Value;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * BookWormV2
 * Created on 19/8/22 - Friday
 * User Khan, C M Abdullah
 * Ref:
 */
@Value(staticConstructor = "of")
public class UserContext {

	private final long userId;
	private final String email;
	private final Collection<Role> roles;

	public static UserContext of(long userId, String email, Role role){
		return of( userId,email, Collections.singleton(role));
	}
	
//	public static UserContext of(long userId, String email, List<Role> roles){
//		return of(userId, email, roles);
//	}
}
