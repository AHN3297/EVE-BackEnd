package com.kh.evision.comment.model.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kh.evision.auth.model.vo.CustomUserDetails;
import com.kh.evision.board.model.service.BoardService;
import com.kh.evision.comment.model.dao.CommentMapper;
import com.kh.evision.comment.model.dto.CommentDTO;
import com.kh.evision.comment.model.vo.CommentVO;
import com.kh.evision.exception.InvalidParameterException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

	private final BoardService boardService;
	private final CommentMapper commentMapper;

	@Override
	public CommentVO save(CommentDTO comment, CustomUserDetails userDetails) {
		
	    // 게시글 존재 여부 확인 (이 부분에서 에러 발생)
	    // boardService.findByBoardNo()를 호출하는데 null이 전달되고 있음
	    if (comment.getRefBno() != null) {
	        boardService.findByBoardNo(comment.getRefBno()); // ✅ null 체크 추가
	    } else {
	        throw new InvalidParameterException("게시글 번호가 없습니다.");
	    }
		
		boardService.findByBoardNo(comment.getRefBno()); // 외부에 노출된 메소드 호출
		String memberId = userDetails.getUsername();
		
		CommentVO c = CommentVO.builder()
							   .commentWriter(Long.valueOf(userDetails.getUsername()))
							   .commentContent(comment.getCommentContent())
							   .refBno(comment.getRefBno())
							   .build();
		commentMapper.save(c);
		return c;
	}

	@Override
	public List<CommentDTO> findAll(Long boardNo) {
		boardService.findByBoardNo(boardNo);
	    List<CommentDTO> comments = commentMapper.findAll(boardNo);
	        
	    return comments != null ? comments : new ArrayList<>();				

	}
	
	@Override
	public void delete(Long commentNo, CustomUserDetails userDetails) {
	    log.info("=== 댓글 삭제 ===");
	    log.info("commentNo: {}", commentNo);
	    log.info("user: {}", userDetails.getUsername());
	    
	    // 권한 확인 로직 추가 (선택사항)
	    // 댓글 작성자와 현재 사용자가 같은지 확인
	    
	    int result = commentMapper.delete(commentNo);
	    
	    if (result == 0) {
	        throw new InvalidParameterException("댓글 삭제에 실패했습니다.");
	    }
	    
	    log.info("댓글 삭제 완료");
	}
	
}
