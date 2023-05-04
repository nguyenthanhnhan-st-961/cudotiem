package com.cudotiem.accountservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cudotiem.accountservice.dto.ProfileDto;
import com.cudotiem.accountservice.entity.Profile;
import com.cudotiem.accountservice.service.IProfileService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

	@Autowired
	IProfileService profileService;

	@GetMapping
	public List<Profile> getAllProfiles() {
		return profileService.getAllProfile();
	}
	
	@GetMapping("/{id}")
	public Profile getProfile(@PathVariable Long id) {
		return profileService.getProfile(id);
	}

	@PostMapping
	public ResponseEntity<String> addNewProfile() {
		profileService.addProfile();
		return ResponseEntity.ok().body("add new profile successfully");
	}

	@PutMapping("/{id}")
	public ResponseEntity<String> updateProfile(@PathVariable Long id, @RequestBody ProfileDto profileDto) {
		ProfileDto request = profileDto;
		profileService.updateProfile(id, request);
		return ResponseEntity.ok().body("update profile successfully");
	}
}
