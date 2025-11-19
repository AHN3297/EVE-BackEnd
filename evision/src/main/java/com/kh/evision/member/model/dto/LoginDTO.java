package com.kh.evision.member.model.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {
	@Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+\\-=]*$", message = "아이디에는 영어, 숫자, 특수문자만 가능합니다.")
	@Size(min = 5, max = 20, message = "아이디는 최소 5자, 최대 20자입니다.")
    private String memberId;
	
	@Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+\\-=]*$", message = "비밀번호는 영어, 숫자, 특수문자만 가능합니다.")
	@Size(min = 8, max = 20, message = "비밀번호는 최소 8자, 최대 20자 입니다.")
    private String memberPwd;
}
