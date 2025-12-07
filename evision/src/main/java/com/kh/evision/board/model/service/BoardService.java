package com.kh.evision.board.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kh.evision.auth.model.vo.CustomUserDetails;
import com.kh.evision.board.model.dto.BoardDTO;

public interface BoardService {
	
	void save(BoardDTO board, MultipartFile file, String username);
	
	List<BoardDTO> findAll(int pageNo);
	
	BoardDTO findByBoardNo(Long boardNo);
	
	BoardDTO update(BoardDTO board, MultipartFile file, Long boardNo, CustomUserDetails userDetails);
	
	void deleteByBoardNo(Long boardNo, CustomUserDetails userDetails);
	
}
