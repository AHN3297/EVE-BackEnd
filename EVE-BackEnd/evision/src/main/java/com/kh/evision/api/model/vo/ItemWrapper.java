package com.kh.evision.api.model.vo;


import java.util.List;

import com.kh.evision.api.model.dto.ResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemWrapper {
    private List<ResponseDTO> item;  // item 배열을 List<ResponseDTO>로 매핑
}