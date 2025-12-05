package com.kh.evision.comment.model.service;

import java.util.List;

import com.kh.evision.auth.model.vo.CustomUserDetails;
import com.kh.evision.comment.model.dto.CommentDTO;
import com.kh.evision.comment.model.vo.CommentVO;

public interface CommentService {

	// 댓글 작성
	CommentVO save(CommentDTO comment, CustomUserDetails userDetails);
	
	// 댓글 조회
	List<CommentDTO> findAll(Long boardNo);
	
	// 댓글 삭제
    void delete(Long commentNo, CustomUserDetails userDetails);

}
