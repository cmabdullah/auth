package com.ml.auth.service;

import com.ml.auth.common.UserContext;
import com.ml.auth.constants.AuthConstants;
import com.ml.auth.constants.AuthProvider;
import com.ml.auth.constants.UserStatus;
import com.ml.auth.domain.AccessToken;
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
import com.ml.coreweb.util.DateTimeUtil;
import io.jsonwebtoken.Claims;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.ZonedDateTime;
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
	private final AccessTokenService accessTokenService;
	
	@Autowired
	private UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
							SignUpRequestDtoValidator signUpRequestDtoValidator,
							AuthenticationManager authenticationManager,
							@Lazy TokenProvider tokenProvider,
							RoleService roleService,
							AccessTokenService accessTokenService
	) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.signUpRequestDtoValidator = signUpRequestDtoValidator;
		this.authenticationManager = authenticationManager;
		this.tokenProvider = tokenProvider;
		this.roleService = roleService;
		this.accessTokenService = accessTokenService;
		
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
		user.setLastPasswordChangeTime(DateTimeUtil.timeNow().toInstant());
		UserContext userContext = UserContext.of(0, user.getEmail(), user.getRoles());
		String refreshToken = tokenProvider.createRefreshToken(userContext);
		user.setRefreshToken(refreshToken);
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
		user.setLastPasswordChangeTime(DateTimeUtil.timeNow().toInstant());
		Set<Role> role = new HashSet<>();
		role.add(new Role("ROLE_ADMIN"));
//		Set<Role> role2 = roleService.save(role);
		user.setRoles(role);
		User newUser = userRepository.save(user);
		return new SignUpResponseDto(newUser.getEmail(), AuthConstants.REGISTRATION_SUCCESS);
	}
	
	@Override
	//@Transactional implementation needed
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
		
		//removed expired tokens
		boolean dataUpdated = removeAndUpdateAccessTokenAndRefreshToken(user);
		
		String oldRefreshToken = user.getRefreshToken();
		
		boolean isRefreshTokenValid = StringUtils.isNoneBlank(oldRefreshToken);
		UserContext userContext = UserContext.of(user.getId(), user.getEmail(), user.getRoles());
		String newAccessToken = tokenProvider.createAccessJwtToken(userContext);
		
		//to do check update time with issuer time
		LoginResponseDto loginResponseDto = new LoginResponseDto();
		if (!isRefreshTokenValid) {
			// check refresh token is available or not
			//to do
			String refreshToken = tokenProvider.createRefreshToken(userContext);
			user.setRefreshToken(refreshToken);
			loginResponseDto.setRefreshToken(refreshToken);
		} else {
			loginResponseDto.setRefreshToken(oldRefreshToken);
		}
		
		AccessToken accessToken = new AccessToken();
		accessToken.setAccessToken(newAccessToken);
		accessToken.setUser(user);
		
		accessTokenService.save(accessToken);
		userRepository.save(user);
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		loginResponseDto.setAccessToken(newAccessToken);
		loginResponseDto.setEmail(loginRequestDto.getEmail());
		loginResponseDto.setTokenType(AuthConstants.TOKEN_TYPE);
		return loginResponseDto;
	}
	
	//todo optimise code
	private boolean removeAndUpdateAccessTokenAndRefreshToken(User user) {
		boolean refreshOrAccessTokenNeedToBeUpdate = false;
		Set<AccessToken> accessTokenList = user.getAccessTokens();
		if (CollectionUtils.isNotEmpty(accessTokenList)) {
			Set<AccessToken> livedAccessTokenList =
					accessTokenList.stream().filter(Objects::nonNull)
							.filter(accessToken -> tokenProvider.isTokenValid(
									accessToken.getAccessToken()))
							.filter(accessToken -> tokenProvider.claimsAreValidAfterChangePassword(
									accessToken.getAccessToken(),
									DateTimeUtil.zonedDateTimeFromInstant(
											accessToken.getUser().getLastPasswordChangeTime())))
							.collect(Collectors.toSet());
			
			//todo find and remove invalid access token list
			List<AccessToken> deadAccessTokenList =
					accessTokenList.stream().filter(Objects::nonNull)
							.filter(accessToken -> !livedAccessTokenList.contains(accessToken))
							.collect(Collectors.toList());
			
			user.setAccessTokens(livedAccessTokenList);
			refreshOrAccessTokenNeedToBeUpdate = true;
		}
		String refreshToken = user.getRefreshToken();
		if (!tokenProvider.isTokenValid(refreshToken)) {
			//prepare refresh token
			user.setRefreshToken(null);
			refreshOrAccessTokenNeedToBeUpdate = true;
		} else {
			ZonedDateTime lastChangedPassword = DateTimeUtil.zonedDateTimeFromInstant(user.getLastPasswordChangeTime());
			boolean isTokenValidAfterUserChangedPassword =
					tokenProvider.claimsAreValidAfterChangePassword(refreshToken, lastChangedPassword);
			if (!isTokenValidAfterUserChangedPassword) {
				user.setRefreshToken(null);
				refreshOrAccessTokenNeedToBeUpdate = true;
			}
		}
		return refreshOrAccessTokenNeedToBeUpdate;
	}
	
	@Override
	public boolean isUserDeleted(User user) {
		UserStatus status = user.getUserStatus();
		return !user.isActive() && status == UserStatus.DELETED;
	}
}
