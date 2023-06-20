package com.cudotiem.authservice.security.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.cudotiem.authservice.entity.User;
import com.cudotiem.authservice.exception.TokenExpiredException;
import com.cudotiem.authservice.security.service.CustomUserDetails;

import io.jsonwebtoken.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);

	@Value("${cudotiem.app.jwtSecret}")
	private String jwtSecret;

	@Value("${cudotiem.app.jwtExpirationMs}")
	private int jwtExpirationMs;

	@Value("${cudotiem.app.jwtCookieName}")
	private String jwtCookie;

	@Value("${cudotiem.app.jwtRefreshCookieName}")
	private String jwtRefreshCookie;

	public ResponseCookie generateJwtCookie(CustomUserDetails userDetails) {
		String jwt = generateTokenFromUsername(userDetails.getUsername());
		return generateCookie(jwtCookie, jwt, "/api");
	}
	
	public ResponseCookie generateJwtCookie(User user) {
		String jwt = generateTokenFromUsername(user.getUsername());
		return generateCookie(jwtCookie, jwt, "/api");
	}
	
	
	public ResponseCookie generateRefreshJwtCookie(String refreshToken) {
		return generateCookie(jwtRefreshCookie, refreshToken, "/api/v1/auth/refreshtoken");
	}
	
	public String getJwtFromCookies(HttpServletRequest request) {
		return getCookieValueByName(request, jwtCookie);
	}
	
	public String getJwtRefreshFromCookies(HttpServletRequest request) {
		return getCookieValueByName(request, jwtRefreshCookie);
	}

	public ResponseCookie getCleanJwtCookie() {
		ResponseCookie cookie = ResponseCookie.from(jwtCookie, null).path("/api").build();
		return cookie;
	}
	
	public ResponseCookie getCleanJwtRefreshCookie() {
		ResponseCookie cookie = ResponseCookie.from(jwtRefreshCookie, null).path("/api/v1/auth/refreshtoken").build();
		return cookie;
	}

	public String getUsernameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			LOGGER.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			LOGGER.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			LOGGER.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			LOGGER.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			LOGGER.error("JWT claims string is empty: {}", e.getMessage());
		}
		return false;
	}

	public String generateTokenFromUsername(String username) {
		return Jwts.builder().setSubject(username).setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}

	private ResponseCookie generateCookie(String name, String value, String path) {
		ResponseCookie cookie = ResponseCookie.from(name, value).path(path).maxAge(24 * 60 * 60).sameSite("none").secure(true).httpOnly(true).build();
		return cookie;
	}
	
	private String getCookieValueByName(HttpServletRequest request, String name) {
		Cookie cookie = WebUtils.getCookie(request, name);
		if(cookie != null) {
			return cookie.getValue();
		} else {
			return null;
		}
	}
}
