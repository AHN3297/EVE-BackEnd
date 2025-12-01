package com.kh.evision.board.model.service;

import java.security.InvalidParameterException;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kh.evision.auth.model.vo.CustomUserDetails;
import com.kh.evision.board.model.dao.BoardMapper;
import com.kh.evision.board.model.dto.BoardDTO;
import com.kh.evision.board.model.vo.BoardVO;
import com.kh.evision.exception.CustomAuthenticationException;
import com.kh.evision.file.FileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
	
	private final BoardMapper boardMapper;
	private final FileService fileService;
	
	@Override
	public void save(BoardDTO board, MultipartFile file, String username) {
		
		BoardVO.BoardVOBuilder builder = BoardVO.builder()
			.boardTitle(board.getBoardTitle())
			.boardContent(board.getBoardContent())
			.boardWriter(username);
		
		// 파일이 있으면 저장
		if(file != null && !file.isEmpty()) {
			String filePath = fileService.store(file);
			log.info("파일 저장 완료: {}", filePath);
			// 필요하면 파일 정보를 BoardVO에 추가
		}
		
		BoardVO b = builder.build();
		boardMapper.save(b);
	}

	@Override
	public List<BoardDTO> findAll(int pageNo) {
		if(pageNo < 0) {
			throw new InvalidParameterException("유효하지 않은 접근입니다.");
		}
		RowBounds rb = new RowBounds(pageNo * 3, 3);
		return boardMapper.findAll(rb);
	}

	@Override
	public BoardDTO findByBoardNo(Long boardNo) {
		return getBoardOrThrow(boardNo);
	}
	
	private BoardDTO getBoardOrThrow(Long boardNo) {
		BoardDTO board = boardMapper.findByBoardNo(boardNo);
		if(board == null) {
			throw new InvalidParameterException("유효하지 않은 접근입니다.");
		}
		return board;
	}
	
	@Override
	public BoardDTO update(BoardDTO board, MultipartFile file, Long boardNo, CustomUserDetails userDetails) {
		
		// 1. 게시글번호가 존재하는 게시글인가
		// 2. 현재 토큰 소유주가 게시글작성자와 일치하는가
		validateBoard(boardNo, userDetails);
		
		// 3. 새로운 파일이 첨부되었는가
		board.setBoardNo(boardNo);
		if(file != null && !file.isEmpty()) {
			String filePath = fileService.store(file);
			log.info("파일 업데이트 완료: {}", filePath);
			// board.setFileUrl(filePath); // 필요시 추가
		}
		
		// 4. UPDATE
		boardMapper.update(board);
		return board;
	}
	
	private void validateBoard(Long boardNo, CustomUserDetails userDetails) {
		BoardDTO board = getBoardOrThrow(boardNo);
		if(!board.getBoardWriter().equals(userDetails.getUsername())) {
			throw new CustomAuthenticationException("게시글 작성자가 아닙니다.");
		}
	}

	@Override
	public void deleteByBoardNo(Long boardNo, CustomUserDetails userDetails) {
		validateBoard(boardNo, userDetails);
		boardMapper.deleteByBoardNo(boardNo);
	}
	
}

/*

	private final BoardMapper boardMapper;
	private final FileService fileService;
	
	@Override
	public void save(BoardDTO board, MultipartFile file, String username) {
		
	    BoardVO.BoardVOBuilder builder = BoardVO.builder()
	            .boardTitle(board.getBoardTitle())
	            .boardContent(board.getBoardContent())
	            .boardWriter(username);
	        
	        // 게시글 저장
	        BoardVO b = builder.build();
	        boardMapper.save(b);
	        
	        // 파일이 있으면 TB_BOARD_IMG에 저장
	        if(file != null && !file.isEmpty()) {
	            String originalName = file.getOriginalFilename();
	            String changeName = fileService.store(file); // URL 반환
	            
	            BoardImg img = BoardImg.builder()
	                .imgNo(b.getBoardNo())  // 방금 저장한 게시글 번호
	                .originName(originalName)
	                .changeName(changeName)
	                .build();
	                
	            boardImgMapper.save(img);
	        }
	    }
		
		BoardVO b = null;
		
		if(file != null && !file.isEmpty()) {
			// 이름 바꾸기
			String filePath = fileService.store(file);
			b = BoardVO.builder().boardTitle(board.getBoardTitle())
								 .boardContent(board.getBoardContent())
								 .boardWriter(username)
								 .build();
		} else {
			b = BoardVO.builder()
					   .boardTitle(board.getBoardTitle())
					   .boardContent(board.getBoardContent())
					   .boardWriter(username)
					   .build();
		}
		
		boardMapper.save(b);
	}

	@Override
	public List<BoardDTO> findAll(int pageNo) {
		if(pageNo < 0) {
			throw new InvalidParameterException("유효하지 않은 접근입니다.");
		}
		RowBounds rb = new RowBounds(pageNo * 3, 3);
		return boardMapper.findAll(rb);
	}

	@Override
	public BoardDTO findByBoardNo(Long boardNo) {
		return getBoardOrThrow(boardNo);
	}
	
	private BoardDTO getBoardOrThrow(Long boardNo) {
		BoardDTO board = boardMapper.findByBoardNo(boardNo);
		if(board == null) {
			throw new InvalidParameterException("유효하지 않은 접근입니다.");
		}
		return board;
	}
	
	@Override
	public BoardDTO update(BoardDTO board, MultipartFile file
						   ,Long boardNo, CustomUserDetails userDetails) {
		
		// 1.게시글번호가 존재하는 게시글인가
		// 2. 현재 토큰 소유주가 게시글작성자와 일치하는가
		// 3. 새로운 파일이 첨부되었는가
		// 4. 만약 첨부되었다면 새롭게 파일을 업로드 한 후 새로운 패스로 변경
		// 5. 모두 완료되면 UPDATE

		validateBoard(boardNo, userDetails);
		board.setBoardNo(boardNo);
		if(file != null && !file.isEmpty()) {
			String filePath = fileService.store(file);
//			board.setFileUrl(filePath);
		}
		boardMapper.update(board);
		return null;
	}
	
	private void validateBoard(Long boardNo, CustomUserDetails userDetails) {
	BoardDTO board = getBoardOrThrow(boardNo);
	if(!board.getBoardWriter().equals(userDetails.getUsername())) {
		throw new CustomAuthenticationException("게시글 작성자가 아닙니다.");
		}
	}

	@Override
	public void deleteByBoardNo(Long boardNo, CustomUserDetails userDetails) {

		validateBoard(boardNo, userDetails);
		boardMapper.deleteByBoardNo(boardNo);

	}
	}
*/	

