package com.ml.auth.controller;

import com.ml.auth.domain.User;
import com.ml.auth.request.UserProfileRequestDto;
import com.ml.auth.service.UserService;
import com.ml.coreweb.exception.ApiError;
import com.ml.coreweb.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
/**
 * BookWormV2
 * Created on 19/8/22 - Friday
 * User Khan, C M Abdullah
 * Ref:
 */
@RestController
@RequestMapping("/auth")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/user/profile")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ApiResponse<?> getCurrentUser(
			Authentication authentication,
			//@CurrentUser
			@Valid @RequestBody UserProfileRequestDto userProfileRequestDto) {
		User user = userService.findByEmail(userProfileRequestDto.getEmail()).orElseThrow(
				ApiError.createSingletonSupplier("Email not found " +
						userProfileRequestDto.getEmail(), HttpStatus.BAD_REQUEST));
		return new ApiResponse<>(user);
	}
}
