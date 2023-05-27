package com.cudotiem.authservice.service;

import java.util.Optional;

import com.cudotiem.authservice.entity.RefreshToken;

public interface RefreshTokenService {

	public Optional<RefreshToken> findByToken(String token);
	public RefreshToken createRefreshToken(Long userId);
	public RefreshToken verifyExpiration(RefreshToken token);
	public int deleteByUserId(Long userId);
}
