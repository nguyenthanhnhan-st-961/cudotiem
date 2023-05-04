package com.cudotiem.accountservice.service;

import java.util.List;

import com.cudotiem.accountservice.dto.ProfileDto;
import com.cudotiem.accountservice.entity.Profile;
import com.cudotiem.accountservice.payload.response.UserInfoResponse;

public interface IProfileService {

	public List<ProfileDto> getAllProfile();
	public ProfileDto getProfile(Long id);
	public UserInfoResponse getUserInfo(Long id);
	public String addProfile();
	public String updateProfile(Long id, ProfileDto profileDto);
}
