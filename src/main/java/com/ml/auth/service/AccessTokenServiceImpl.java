package com.ml.auth.service;

import com.ml.auth.domain.AccessToken;
import com.ml.auth.repository.AccessTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Reusable Lib
 * Created on 1/9/22 - Thursday
 * User Khan, C M Abdullah
 * Ref:
 */
@Service
public class AccessTokenServiceImpl implements AccessTokenService {
	
	private final AccessTokenRepository accessTokenRepository;
	
	@Autowired
	public AccessTokenServiceImpl (AccessTokenRepository accessTokenRepository){
		this.accessTokenRepository = accessTokenRepository;
	}
	@Override
	public List<AccessToken> saveAll(List<AccessToken> accessTokens) {
		return accessTokenRepository.saveAll(accessTokens);
	}
	
	@Override
	public AccessToken save(AccessToken accessToken) {
		return accessTokenRepository.save(accessToken);
	}
}
