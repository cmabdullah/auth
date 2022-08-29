package com.ml.auth.validator;

import com.ml.auth.constants.AuthConstants;
import com.ml.auth.repository.UserRepository;
import com.ml.auth.request.SignUpRequestDto;
import com.ml.coreweb.exception.ApiError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * BookWormV2
 * Created on 19/8/22 - Friday
 * User Khan, C M Abdullah
 * Ref:
 */
@Component
public class SignUpRequestDtoValidator {

	private final UserRepository userRepository;

	@Autowired
	public SignUpRequestDtoValidator( UserRepository userRepository){
		this.userRepository = userRepository;
	}

	public void validate(SignUpRequestDto signUpRequestDto) {
		if(userRepository.existsByEmail(signUpRequestDto.getEmail())) {
			throw new ApiError(AuthConstants.USER_ALREADY_EXIST, HttpStatus.FORBIDDEN);
		}
	}
}
