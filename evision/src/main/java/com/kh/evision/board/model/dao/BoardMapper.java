package com.kh.evision.board.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.kh.evision.board.model.dto.BoardDTO;
import com.kh.evision.board.model.vo.BoardVO;
import com.kh.evision.file.ImgInfo;

@Mapper
public interface BoardMapper {
	
	void save(BoardVO b);
	
	List<BoardDTO> findAll(RowBounds rb);
	
	BoardDTO findByBoardNo(Long boardNo);
	
	void update(BoardDTO board);
	
	void deleteByBoardNo(Long boardNo);
	
	//이미지 첨부
	void saveBoardImg(ImgInfo imgInfo);
}
