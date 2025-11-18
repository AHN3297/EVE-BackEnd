package com.kh.evision.member.model.dto;

import java.util.Date;

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

	private int memberNo;
	private String memberName;
	private String memberId;
	private String memberPwd;
	private String nickname;
	private String address;
	private String phone;
	private String email;
	private Date enrollDate;
	private char status;
	private String roleStatus;
	
	
}
