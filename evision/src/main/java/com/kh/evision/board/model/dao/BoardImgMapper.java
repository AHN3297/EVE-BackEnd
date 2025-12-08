package com.kh.evision.board.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.kh.evision.board.model.vo.BoardImgVO;

@Mapper
public interface BoardImgMapper {
    void save(BoardImgVO img);

    BoardImgVO findByImgNo(Long imgNo);

    void deleteByImgNo(Long imgNo);
}
