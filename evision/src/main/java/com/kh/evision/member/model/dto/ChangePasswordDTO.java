package com.kh.evision.member.model.dto;

import org.springframework.web.bind.annotation.RequestMapping;

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
public class ChangePasswordDTO {
	@Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+\\-=]*$", message = "비밀번호는 영어, 숫자, 특수문자만 가능합니다.")
	@Size(min = 8, max = 20, message = "비밀번호는 최소 8자, 최대 20자 입니다.")
	private String currentPassword;
	
	@Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+\\-=]*$", message = "비밀번호는 영어, 숫자, 특수문자만 가능합니다.")
	@Size(min = 8, max = 20, message = "비밀번호는 최소 8자, 최대 20자 입니다.")
	private String newPassword;

}
