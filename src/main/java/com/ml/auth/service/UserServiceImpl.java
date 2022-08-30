package com.ml.auth.service;

import com.ml.auth.common.UserContext;
import com.ml.auth.constants.AuthConstants;
import com.ml.auth.constants.AuthProvider;
import com.ml.auth.constants.UserStatus;
import com.ml.auth.domain.Role;
import com.ml.auth.domain.User;
import com.ml.auth.jwt.TokenProvider;
import com.ml.auth.repository.UserRepository;
import com.ml.auth.request.LoginRequestDto;
import com.ml.auth.request.SignUpRequestDto;
import com.ml.auth.response.LoginResponseDto;
import com.ml.auth.response.SignUpResponseDto;
import com.ml.auth.validator.SignUpRequestDtoValidator;
import com.ml.coreweb.exception.ApiError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * BookWormV2
 * Created on 19/8/22 - Friday
 * User Khan, C M Abdullah
 * Ref:
 */
@Service
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final SignUpRequestDtoValidator signUpRequestDtoValidator;
	private final AuthenticationManager authenticationManager;
	private final TokenProvider tokenProvider;
	private final RoleService roleService;
	
	@Autowired
	private UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
							SignUpRequestDtoValidator signUpRequestDtoValidator,
							AuthenticationManager authenticationManager,
							TokenProvider tokenProvider,
							RoleService roleService
	) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.signUpRequestDtoValidator = signUpRequestDtoValidator;
		this.authenticationManager = authenticationManager;
		this.tokenProvider = tokenProvider;
		this.roleService = roleService;
		
	}
	
	@Override
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	@Override
	public Boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}
	
	@Override
	public Optional<User> findById(Long id) {
		return userRepository.findById(id);
	}
	
	@Override
	public SignUpResponseDto save(SignUpRequestDto signUpRequestDto) {
		signUpRequestDtoValidator.validate(signUpRequestDto);
		User user = new User();
		user.setEmail(signUpRequestDto.getEmail());
		user.setPassword(passwordEncoder.encode(signUpRequestDto.getPassword()));
		user.setProvider(AuthProvider.LOCAL);
		user.setUserStatus(UserStatus.ACTIVE);
		Set<Role> role = new HashSet<>();
		role.add(new Role("ROLE_USER"));
//		Set<Role> role2 = roleService.save(role);
		user.setRoles(role);
		User newUser = userRepository.save(user);
		return new SignUpResponseDto(newUser.getEmail(), AuthConstants.REGISTRATION_SUCCESS);
	}
	
	@Override
	public SignUpResponseDto saveAdmin(SignUpRequestDto signUpRequestDto) {
		signUpRequestDtoValidator.validate(signUpRequestDto);
		User user = new User();
		user.setEmail(signUpRequestDto.getEmail());
		user.setPassword(passwordEncoder.encode(signUpRequestDto.getPassword()));
		user.setProvider(AuthProvider.LOCAL);
		user.setUserStatus(UserStatus.ACTIVE);
		Set<Role> role = new HashSet<>();
		role.add(new Role("ROLE_ADMIN"));
//		Set<Role> role2 = roleService.save(role);
		user.setRoles(role);
		User newUser = userRepository.save(user);
		return new SignUpResponseDto(newUser.getEmail(), AuthConstants.REGISTRATION_SUCCESS);
	}
	
	@Override
	public LoginResponseDto adminLoginIn(LoginRequestDto loginRequestDto) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginRequestDto.getEmail(), loginRequestDto.getPassword()));
		
		User user = findByEmail(loginRequestDto.getEmail()).orElseThrow(
				ApiError.createSingletonSupplier("user not valid", HttpStatus.NOT_FOUND));
		boolean isUserDeleted = isUserDeleted(user);
		if (isUserDeleted) {
			throw new ApiError("user.not.valid", HttpStatus.NOT_FOUND);
		}
		
		//match password
		if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
			//To do
			// increment bad signIn Attempt
			throw new ApiError("username.or.password.not.matched", HttpStatus.EXPECTATION_FAILED);
		}
		
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserContext userContext = UserContext.of(user.getId(), user.getEmail(), user.getRoles());
		String accessToken = tokenProvider.createAccessJwtToken(userContext);
		
		// check refresh token is available or not
		//to do
		String refreshToken = tokenProvider.createRefreshToken(userContext);
		LoginResponseDto loginResponseDto = new LoginResponseDto();
		loginResponseDto.setAccessToken(accessToken);
		loginResponseDto.setEmail(loginRequestDto.getEmail());
		loginResponseDto.setTokenType(AuthConstants.TOKEN_TYPE);
		return loginResponseDto;
	}
	
	@Override
	public boolean isUserDeleted(User user) {
		UserStatus status = user.getUserStatus();
		return !user.isActive() && status == UserStatus.DELETED;
	}
}
