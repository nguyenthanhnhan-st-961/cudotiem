package com.cudotiem.authservice.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cudotiem.authservice.entity.ERole;
import com.cudotiem.authservice.entity.Role;
import com.cudotiem.authservice.entity.User;
import com.cudotiem.authservice.payload.request.LoginRequest;
import com.cudotiem.authservice.payload.request.SignupRequest;
import com.cudotiem.authservice.payload.response.MessageResponse;
import com.cudotiem.authservice.payload.response.UserInfoResponse;
import com.cudotiem.authservice.repository.RoleRepository;
import com.cudotiem.authservice.repository.UserRepository;
import com.cudotiem.authservice.security.jwt.JwtUtils;
import com.cudotiem.authservice.security.service.CustomUserDetails;
import com.cudotiem.authservice.service.UserService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
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

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

		ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(
				jwtCookie.toString()
		/*
		 * new UserInfoResponse(userDetails.getId(), userDetails.getUsername(),
		 * userDetails.getEmail(), roles)
		 */);
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
//
//		User user = new User();
//		user.setUsername(signupRequest.getUsername());
//		user.setEmail(signupRequest.getEmail());
//		user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
//
//		Set<String> strRoles = signupRequest.getRoles();
//		Set<Role> roles = new HashSet<>();
//
//		if (strRoles == null) {
//			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
//					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//			roles.add(userRole);
//		} else {
//			strRoles.forEach(role -> {
//				switch (role) {
//				case "admin":
//					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
//							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//					roles.add(adminRole);
//					break;
//
//				case "mod":
//					Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
//							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//					roles.add(modRole);
//					break;
//
//				default:
//					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
//							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//					roles.add(userRole);
//				}
//			});
//		}
//
//		user.setRoles(roles);
//		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	@PostMapping("/signout")
	public ResponseEntity<?> logoutUser() {
		ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
				.body(new MessageResponse("You're been signed out!"));
	}

	@PostMapping("/token")
	public String getToken(@RequestBody LoginRequest authRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		if (authentication.isAuthenticated()) {
			return jwtUtils.generateTokenFromUsername(authRequest.getUsername());
		} else {
			throw new RuntimeException("invalid access");
		}
	}

	@GetMapping("/validate")
	public String validateToken(@RequestParam String token) {
		jwtUtils.validateJwtToken(token);
		return "token is valid";
	}
	
	@GetMapping("/verify")
	public String verifyUser(@RequestParam String code) {
		if(userService.verify(code)) {
			return "verify successfully";
		} else {
			return "verify failed";
		}
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
