package com.cudotiem.accountservice.service;

import java.util.List;

import com.cudotiem.accountservice.dto.ProfileDto;
import com.cudotiem.accountservice.entity.Profile;

public interface IProfileService {

	public List<Profile> getAllProfile();
	public Profile getProfile(Long id);
	public String addProfile();
	public String updateProfile(Long id, ProfileDto profileDto);
}