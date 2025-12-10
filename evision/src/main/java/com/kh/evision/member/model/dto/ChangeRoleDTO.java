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
public class ChangeRoleDTO {
	private Long memberNo; // Long 은 null값이 기본값이고 객체로 취급됨 어차피 null값이 없다.
	private String status;
	private String newRole;
	private String currentRole;

}
