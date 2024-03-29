package com.cudotiem.accountservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDto {

	private String fisrtName;
	private String lastName;
	private String phoneNumber;
	private String avatar;
	private Integer idAddress;
}
