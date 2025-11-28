package com.kh.evision.member.model.dto;

import java.util.Date;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
	
	// 아이디는 영문, 숫자, 특수문자 사용가능, 5~20자로 제한
	@Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+\\-=]*$", message = "아이디에는 영어, 숫자, 특수문자만 가능합니다.")
	@Size(min = 5, max = 20, message = "아이디는 최소 5자, 최대 20자입니다.")
	private String memberId;
	
	// 비밀번호는 영문, 숫자, 특수문자 사용가능, 8~20자로 제한
	@Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+\\-=]*$", message = "비밀번호는 영어, 숫자, 특수문자만 가능합니다.")
	@Size(min = 8, max = 20, message = "비밀번호는 최소 8자, 최대 20자 입니다.")
	private String memberPwd;
	
	// 닉네임은 아무문자나 사용가능, 5~40자로 제한
	@Pattern(regexp = "^[^\\s]+$", message = "닉네임을 한글자 이상 입력해주세요")
	private String nickname;
	
	// 주소는 아무문자나 사용가능 100자로 제한하고 한글자 이상은 입력해야함
	@Pattern(regexp = "^.{1,100}$", message = "주소를 한 글자 이상, 100자 이내로 입력해주세요")
	private String address;
	
	// 휴대폰 전화는 010-xxxx-xxxx 총 13자로 최소 13자 아니면 안되게 max는 15자로 제한
	// 숫자와 '-' 허용
	@Pattern(regexp = "01[0-9]-\\d{3,4}-\\d{4}$", message = "휴대전화 번호는 01x-xxxx-xxxx형식이어야합니다. ")
	@Size(min = 12, max = 15, message = "휴대전화 번호는 최소 12자~15자입니다.")
	private String phone;
	
	// 이메일은 NULL이 들어와도 됨, 50자로 제한
	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "이메일 형식이 아닙니다.")
	@Size(max=50, message = "이메일은 50자 이내입니다!")
	private String email;
	
	private String roleStatus;
	private Date enrollDate;
	private char status;
	
	
}
