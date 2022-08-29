package com.ml.auth.repository;

import com.ml.auth.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * BookWormV2
 * Created on 19/8/22 - Friday
 * User Khan, C M Abdullah
 * Ref:
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
