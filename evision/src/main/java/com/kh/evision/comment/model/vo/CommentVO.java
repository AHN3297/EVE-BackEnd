package com.kh.evision.comment.model.vo;

import java.sql.Date;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CommentVO {
	private Long commentNo;
	private String commentContent;
	private Long refBno;
	private String commentWriter;
	private Date createDate;
}
