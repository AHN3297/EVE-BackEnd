package com.kh.evision.board.model.vo;

import java.sql.Date;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BoardVO {
	private Long boardNo; 
	private String boardTitle; 
	private String boardWriter;
	private String boardContent; 
	private Long count;
	private String status;
	private Date createDate;

}
