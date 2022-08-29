package com.ml.auth.common;

import com.ml.auth.domain.Role;
import lombok.Value;

import java.util.Collection;
import java.util.Collections;

/**
 * BookWormV2
 * Created on 19/8/22 - Friday
 * User Khan, C M Abdullah
 * Ref:
 */
@Value(staticConstructor = "of")
public class UserContext {

	private final int userId;
	private final Collection<Role> roles;

	public static UserContext of(int userId, Role role){
		return of(userId, Collections.singleton(role));
	}
}
