package com.cudotiem.authservice.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cudotiem.authservice.entity.RefreshToken;
import com.cudotiem.authservice.entity.User;
import com.cudotiem.authservice.exception.TokenRefreshException;
import com.cudotiem.authservice.payload.request.LoginRequest;
import com.cudotiem.authservice.payload.request.SignupRequest;
import com.cudotiem.authservice.payload.response.LoginResponse;
import com.cudotiem.authservice.payload.response.MessageResponse;
import com.cudotiem.authservice.payload.response.UserInfoResponse;
import com.cudotiem.authservice.repository.RoleRepository;
import com.cudotiem.authservice.repository.UserRepository;
import com.cudotiem.authservice.security.jwt.JwtUtils;
import com.cudotiem.authservice.security.service.CustomUserDetails;
import com.cudotiem.authservice.service.RefreshTokenService;
import com.cudotiem.authservice.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserService userService;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	RefreshTokenService refreshTokenService;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		User user = userRepository.findByUsernameOrEmail(loginRequest.getUsernameOrEmail()).get();
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

		ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

		ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshJwtCookie(refreshToken.getToken());

		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
				.header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString()).body(new LoginResponse(jwtCookie.getValue(), roles));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {

		if (userRepository.existsByUsername(signupRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signupRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}

		userService.registerUser(signupRequest);
		return ResponseEntity.ok(
				new MessageResponse("<h2 class='text-xl font-bold mb-3 text-blue-800'>Mở tài khoản thành công</h2>\r\n"
						+ "        <p class=''>\r\n" + "          Vui lòng kiểm tra hộp thư đến email\r\n"
						+ "          <span\r\n" + "            class='text-blue-500 font-medium\r\n" + "      '\r\n"
						+ "          >\r\n" + signupRequest.getEmail() + "\r\n" + "          </span>\r\n"
						+ "          để xác nhận tài khoản của bạn\r\n" + "        </p>"));
	}

	@PostMapping("/signout")
	public ResponseEntity<?> logoutUser() {
		Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (!principle.toString().equals("anonymousUser")) {
			Long idUser = ((CustomUserDetails) principle).getId();
			refreshTokenService.deleteByUserId(idUser);
		}

		ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();
		ResponseCookie jwtRefreshCookie = jwtUtils.getCleanJwtRefreshCookie();
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
				.header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
				.body(new MessageResponse("You're been signed out!"));
	}

	public ResponseEntity<?> refreshToken(HttpServletRequest request) {
		String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);
		if ((refreshToken != null) && (refreshToken.length() > 0)) {
			return refreshTokenService.findByToken(refreshToken).map(refreshTokenService::verifyExpiration)
					.map(RefreshToken::getUser).map(user -> {
						ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user);
						return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
								.body(new MessageResponse("Token is refreshed successfully!"));
					}).orElseThrow(() -> new TokenRefreshException(refreshToken, "Refresh token is not in database!"));
		}
		return ResponseEntity.badRequest().body(new MessageResponse("Refresh token is empty!"));
	}

//	@PostMapping("/token")
//	public String getToken(@RequestBody LoginRequest authRequest) {
//		Authentication authentication = authenticationManager.authenticate(
//				new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
//		if (authentication.isAuthenticated()) {
//			return jwtUtils.generateTokenFromUsername(authRequest.getUsername());
//		} else {
//			throw new RuntimeException("invalid access");
//		}
//	}

	@GetMapping("/validate")
	public String validateToken(@RequestParam String token) {
		jwtUtils.validateJwtToken(token);
		return "token is valid";
	}

	@GetMapping("/verify")
	public ResponseEntity<?> verifyUser(@RequestParam String code) {
		if (userService.verify(code)) {
			return ResponseEntity.ok().body("User registered successfully!");
		} else {
			return ResponseEntity.ok().body("User registered failed");
		}
	}
	
	@GetMapping("/get-username/{token}")
	public String getUsernameFromToken(@PathVariable String token) {
		return jwtUtils.getUsernameFromJwtToken(token);
	}

	// test

	@GetMapping("/test/all")
	public String allAccess() {
		return "Public Content.";
	}

	@GetMapping("/test/user")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public String userAccess() {
		return "User Content.";
	}

	@GetMapping("/test/mod")
	@PreAuthorize("hasRole('MODERATOR')")
	public String moderatorAccess() {
		return "Moderator Board.";
	}

	@GetMapping("/test/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return "Admin Board.";
	}
}
