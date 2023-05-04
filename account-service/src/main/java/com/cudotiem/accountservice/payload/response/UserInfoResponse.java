package com.cudotiem.accountservice.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {

	private String fullname;
	private String phoneNumber;
	private String avatar;
}
