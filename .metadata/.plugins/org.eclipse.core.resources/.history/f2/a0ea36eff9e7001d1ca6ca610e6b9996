package com.cudotiem.accountservice.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cudotiem.accountservice.dto.ProfileDto;
import com.cudotiem.accountservice.entity.Address;
import com.cudotiem.accountservice.entity.Profile;
import com.cudotiem.accountservice.repository.AddressRepository;

@Component
public class ProfileMapper {
	
	@Autowired
	AddressRepository addressRepository;
	
	public Profile toProfile(ProfileDto profileDto) {
		Address address = addressRepository.findById(profileDto.getIdAddress()).get();

		Profile profile = new Profile();
		profile.setFisrtName(profileDto.getFisrtName());
		profile.setLastName(profileDto.getLastName());
		profile.setFullName(profileDto.getFisrtName() + " " + profileDto.getLastName());
		profile.setPhoneNumber(profileDto.getPhoneNumber());
		profile.setAvatar(profileDto.getAvatar());
		profile.setAddress(address);
		
		return profile;
	}
	
	public ProfileDto toProfileDto(Profile profile) {
		ProfileDto result = new ProfileDto(profile.getFisrtName(), profile.getLastName(), 
				profile.getPhoneNumber(), profile.getAvatar(), 
				profile.getAddress().getId());
		return result;
	}
}
