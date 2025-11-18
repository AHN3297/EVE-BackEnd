package com.kh.evision.member.model.vo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberVO {
	private Long memberNo;
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
