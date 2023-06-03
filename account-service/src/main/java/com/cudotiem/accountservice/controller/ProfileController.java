package com.cudotiem.accountservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cudotiem.accountservice.dto.ProfileDto;
import com.cudotiem.accountservice.payload.response.UserInfoResponse;
import com.cudotiem.accountservice.service.IProfileService;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {

	@Autowired
	IProfileService profileService;

	@GetMapping
	public ResponseEntity<List<ProfileDto>> getAllProfiles() {
		return ResponseEntity.ok().body(profileService.getAllProfile());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ProfileDto> getProfile(@PathVariable Long id) {
		return ResponseEntity.ok().body(profileService.getProfile(id));
	}
	
	@GetMapping("/user-info/{username}")
	public ResponseEntity<UserInfoResponse> getUserInfo(@PathVariable String username) {
		return ResponseEntity.ok().body(profileService.getUserInfo(username));
	}

	@PostMapping
	public ResponseEntity<String> addNewProfile() {
		profileService.addProfile();
		return ResponseEntity.ok().body("add new profile successfully");
	}

	@PutMapping("/{id}")
	public ResponseEntity<String> updateProfile(@PathVariable Long id, @RequestBody ProfileDto profileDto) {
		profileService.updateProfile(id, profileDto);
		return ResponseEntity.ok().body("update profile successfully");
	}
}
