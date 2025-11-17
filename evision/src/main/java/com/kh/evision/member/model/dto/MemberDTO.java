package com.kh.evision.member.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberDTO {
	private String memberName;
	private String memberId;
	private String memberPwd;
	private String nickname;
	private String address;
	private String phone;
	private String email;
	private String roleStatus;
}
