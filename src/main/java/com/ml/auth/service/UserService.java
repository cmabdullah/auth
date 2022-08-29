package com.ml.auth.service;


import com.ml.auth.domain.User;
import com.ml.auth.request.LoginRequestDto;
import com.ml.auth.request.SignUpRequestDto;
import com.ml.auth.response.LoginResponseDto;
import com.ml.auth.response.SignUpResponseDto;

import java.util.Optional;

/**
 * BookWormV2
 * Created on 19/8/22 - Friday
 * User Khan, C M Abdullah
 * Ref:
 */
public interface UserService {

	Optional<User> findByEmail(String email);

	Boolean existsByEmail(String email);

	Optional<User> findById(Long id);

	SignUpResponseDto save(SignUpRequestDto signUpRequestDto);
	SignUpResponseDto saveAdmin(SignUpRequestDto signUpRequestDto);

	LoginResponseDto adminLoginIn(LoginRequestDto loginRequestDto);


}
