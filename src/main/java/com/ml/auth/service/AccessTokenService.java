package com.ml.auth.service;

import com.ml.auth.domain.AccessToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Reusable Lib
 * Created on 1/9/22 - Thursday
 * User Khan, C M Abdullah
 * Ref:
 */
public interface AccessTokenService {
	List<AccessToken> saveAll(List<AccessToken> accessTokens);
	
	AccessToken save(AccessToken accessToken);
}
