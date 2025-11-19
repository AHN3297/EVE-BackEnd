package com.kh.evision.comment.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kh.evision.comment.model.dto.CommentDTO;
import com.kh.evision.comment.model.vo.CommentVO;

@Mapper
public interface CommentMapper {
	
	int save(CommentVO comment);
	
	List<CommentDTO> findAll(Long boardNo);


}
