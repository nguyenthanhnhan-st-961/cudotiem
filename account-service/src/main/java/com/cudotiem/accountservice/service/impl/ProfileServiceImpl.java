package com.cudotiem.accountservice.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cudotiem.accountservice.dto.ProfileDto;
import com.cudotiem.accountservice.entity.Profile;
import com.cudotiem.accountservice.mapper.ProfileMapper;
import com.cudotiem.accountservice.repository.ProfileRepository;
import com.cudotiem.accountservice.service.IProfileService;

@Service
public class ProfileServiceImpl implements IProfileService{

	@Autowired
	ProfileRepository profileRepository;
	
	@Autowired
	ProfileMapper mapper;
	
	public List<Profile> getAllProfile() {
//		List<ProfileDto> result = new ArrayList<>();
		List<Profile> profiles = profileRepository.findAll();
//		profiles.forEach(profile -> result.add(
//				mapper.toProfileDto(profile)));
		return profiles;
	}

	public Profile getProfile(Long id) {
		Profile profile = profileRepository.findById(id).get();
//		return mapper.toProfileDto(profile);
		return profile;
	}

	public String addProfile() {
		profileRepository.save(new Profile());
		return "add new profile successfully";
	}
	
	public String updateProfile(Long id, ProfileDto profileDto) {
		Profile profile = profileRepository.findById(id).get();
		if(profile != null) {
			profileRepository.save(mapper.toProfile(profileDto));
		}
		return "update profile successfully";
	}
}
