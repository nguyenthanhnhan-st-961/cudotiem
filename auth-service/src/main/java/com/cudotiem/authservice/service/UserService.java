package com.cudotiem.authservice.service;

import com.cudotiem.authservice.entity.User;
import com.cudotiem.authservice.payload.request.SignupRequest;

public interface UserService {

	public User registerUser(SignupRequest signupRequest);
	
	public boolean verify(String verificationCode);
}
