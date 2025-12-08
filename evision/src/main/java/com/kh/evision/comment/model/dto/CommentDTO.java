package com.kh.evision.comment.model.dto;

import java.sql.Date;

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
public class CommentDTO {
	private Long commentNo;
	private String commentContent;
	private Long refBno;
	private Long commentWriter;
	private String memberName;
	private Date createDate;
	private String status;

	
    // 이미지 정보 추가
    private String imageUrl;      // 화면 표시용
    private String originName;    // 원본 파일명
}
