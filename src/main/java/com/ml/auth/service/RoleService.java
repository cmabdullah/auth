package com.ml.auth.service;

import com.ml.auth.domain.Role;

import java.util.Set;

/**
 * BookWormV2
 * Created on 19/8/22 - Friday
 * User Khan, C M Abdullah
 * Ref:
 */
public interface RoleService {
	Set<Role> save(Set<Role> role);
}
