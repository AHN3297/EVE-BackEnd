package com.kh.evision.api.model.vo;

import java.util.List;

import com.kh.evision.api.model.dto.ResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseVO {
	private Long resultCode;
	private String resultMsg;  // 응답 메시지
    private int totalCount;    // 총 개수
    private int pageNo;        // 추가: pageNo 필드
    private int numOfRows; 		// 페이지 번호
    private ItemWrapper items;  // items는 객체이고 그 안에 item 배열이 있음
    
}