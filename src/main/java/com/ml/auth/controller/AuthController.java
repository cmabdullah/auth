package com.ml.auth.controller;

import com.ml.auth.request.LoginRequestDto;
import com.ml.auth.response.SignUpResponseDto;
import com.ml.auth.service.UserService;
import com.ml.auth.request.SignUpRequestDto;
import com.ml.coreweb.response.ApiResponse;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping("/public")
@Slf4j
public class AuthController {

	private final UserService userService;

	@Autowired
	public AuthController(UserService userService){
		this.userService = userService;
	}

	@ApiOperation(value = "[TEST]signup --> done")
	@PostMapping("/signup")
	ApiResponse<?> registerUser(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
		log.debug("signup called");
		SignUpResponseDto signUpResponseDto = userService.save(signUpRequestDto);
		return new ApiResponse<>(signUpResponseDto);
	}

	@ApiOperation(value = "[TEST]login --> done")
	@PostMapping("/login")
	public ApiResponse<?> authenticateUser(@Valid @RequestBody LoginRequestDto loginRequestDto) {
		return new ApiResponse<>(userService.adminLoginIn(loginRequestDto));
	}
}
