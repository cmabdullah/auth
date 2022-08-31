package com.ml.auth.repository;

import com.ml.auth.domain.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Reusable Lib
 * Created on 1/9/22 - Thursday
 * User Khan, C M Abdullah
 * Ref:
 */
public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {
}
