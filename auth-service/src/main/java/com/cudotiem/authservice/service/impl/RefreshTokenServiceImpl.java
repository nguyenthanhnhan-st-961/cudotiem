package com.cudotiem.authservice.service.impl;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cudotiem.authservice.entity.RefreshToken;
import com.cudotiem.authservice.exception.TokenRefreshException;
import com.cudotiem.authservice.repository.RefreshTokenRepository;
import com.cudotiem.authservice.repository.UserRepository;
import com.cudotiem.authservice.service.RefreshTokenService;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService{

	@Value("${cudotiem.app.jwtRefreshExpirationMs}")
	private Long refreshTokenDurationMs;
	
	@Autowired
	RefreshTokenRepository refreshTokenRepository;
	
	@Autowired
	UserRepository userRepository;
	 
	@Override
	public Optional<RefreshToken> findByToken(String token) {
		return refreshTokenRepository.findByToken(token);
	}

	@Override
	public RefreshToken createRefreshToken(Long userId) {
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setUser(userRepository.findById(userId).get());
		refreshToken.setExperyDate(Instant.now().plusMillis(refreshTokenDurationMs));
		refreshToken.setToken(UUID.randomUUID().toString());
		refreshToken = refreshTokenRepository.save(refreshToken);
		return refreshToken;
	}

	@Override
	public RefreshToken verifyExpiration(RefreshToken token) {
		if(token.getExperyDate().compareTo(Instant.now()) < 0) {
			refreshTokenRepository.delete(token);
			throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
		}
		return token;
	}

	@Override
	@Transactional
	public int deleteByUserId(Long userId) {
		return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
	}

}
