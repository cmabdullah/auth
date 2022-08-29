package com.ml.auth.service;

import com.ml.auth.constants.AuthConstants;
import com.ml.auth.domain.Role;
import com.ml.auth.repository.RoleRepository;
import com.ml.coreweb.exception.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * BookWormV2
 * Created on 19/8/22 - Friday
 * User Khan, C M Abdullah
 * Ref:
 */
@Slf4j
@Service
public class RoleServiceImpl implements RoleService{
	private final RoleRepository roleRepository;

	@Autowired
	public RoleServiceImpl(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Override
	public Set<Role> save(Set<Role> roles) {
		log.info("role facing");
		List<Role> existingRole = roleRepository.findAll();
		List<Role> roleList = existingRole.stream()
				.filter(Objects::nonNull)
				.filter(role -> !existingRole.contains(role))
				.collect(Collectors.toList());

		List<Role> savedRoles = Optional.of(roleRepository.saveAll(roleList))
				.orElseThrow(ApiError.createSingletonSupplier(AuthConstants.ROLE_SAVE_FAILED, HttpStatus.GATEWAY_TIMEOUT));
		log.info("role saved success");
		return new HashSet<>(roleRepository.saveAll(savedRoles));
	}
}
