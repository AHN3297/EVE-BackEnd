package com.kh.evision.comment.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kh.evision.comment.model.dto.CommentDTO;
import com.kh.evision.comment.model.vo.CommentVO;

@Mapper
public interface CommentMapper {
	
	//댓글 작성
	int save(CommentVO comment);
	
	//댓글 조회
	List<CommentDTO> findAll(Long boardNo);
	
	//댓글 삭제
	int delete(Long commentNo);

}
