package com.cudotiem.accountservice.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cudotiem.accountservice.dto.ProfileDto;
import com.cudotiem.accountservice.entity.Address;
import com.cudotiem.accountservice.entity.Profile;
import com.cudotiem.accountservice.mapper.ProfileMapper;
import com.cudotiem.accountservice.payload.response.UserInfoResponse;
import com.cudotiem.accountservice.repository.AddressRepository;
import com.cudotiem.accountservice.repository.ProfileRepository;
import com.cudotiem.accountservice.service.IProfileService;

@Service
public class ProfileServiceImpl implements IProfileService {

	@Autowired
	ProfileRepository profileRepository;

	@Autowired
	AddressRepository addressRepository;

	@Autowired
	ProfileMapper mapper;

	public List<ProfileDto> getAllProfile() {
		List<ProfileDto> result = new ArrayList<>();
		List<Profile> profiles = profileRepository.findAll();
		profiles.forEach(profile -> result.add(mapper.toProfileDto(profile)));
		return result;
	}

	public ProfileDto getProfile(Long id) {
		Profile profile = profileRepository.findById(id).get();
		return mapper.toProfileDto(profile);
	}
	
	public UserInfoResponse getUserInfo(String username) {
		Profile profile = profileRepository.findByUsername(username);
		
		UserInfoResponse userInfo = new UserInfoResponse();
		userInfo.setAvatar(profile.getAvatar());
		userInfo.setFullname(profile.getFullName());
		userInfo.setPhoneNumber(profile.getPhoneNumber());
		return userInfo;
	}

	public String addProfile() {
		profileRepository.save(new Profile());
		return "add new profile successfully";
	}

	public String updateProfile(Long id, ProfileDto profileDto) {
		Profile profile = profileRepository.findById(id).get();

		Address address = addressRepository.findById(profileDto.getIdAddress()).get();

		profile.setFisrtName(profileDto.getFisrtName());
		profile.setLastName(profileDto.getLastName());
		profile.setFullName(profileDto.getFisrtName() + " " + profileDto.getLastName());
		profile.setPhoneNumber(profileDto.getPhoneNumber());
		profile.setAvatar(profileDto.getAvatar());
		profile.setAddress(address);

		if (profile != null) {
			profileRepository.save(profile);
		}
		return "update profile successfully";
	}
}
