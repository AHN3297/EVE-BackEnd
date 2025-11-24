package com.kh.evision.member.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateMemberDTO {
	private String newName;
	private String newNickname;
	private String newAddress;
	private String newPhone;
	private String newEmail;
}
