package com.kh.evision.api.model.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kh.evision.api.model.dao.ApiDAO;
import com.kh.evision.api.model.dto.ResponseDTO;
import com.kh.evision.api.model.vo.ResponseVO;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ApiService {

    private final ApiDAO apiDAO;  // MyBatis DAO 주입

    public String requestChargerInfo(int pageNo) throws Exception {
        // 서비스 키 (공공 데이터 포털에서 제공된 인증키)
        String serviceKey = "52aa9c9f9752c4bb85b09a0ad25b262d2713ee5ea6efdab594d34b441011d3d4"; 
        
        // 기본 URL
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552584/EvCharger/getChargerInfo");
        urlBuilder.append("?" + "serviceKey=" + serviceKey);  // 인증키
        urlBuilder.append("&" + "numOfRows=" + 10);  // 한 페이지 결과 수
        urlBuilder.append("&" + "pageNo=" + pageNo);  // 페이지 번호
        urlBuilder.append("&" + "dataType=" + "JSON");  // 응답 데이터 타입 (JSON)

        // 최종 URL
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        // 응답 코드 확인
        int responseCode = conn.getResponseCode();
        BufferedReader rd;
        if (responseCode >= 200 && responseCode <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        // 응답 내용 읽기
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        // JSON 응답 파싱
        String jsonResponse = sb.toString();
        
        // Jackson ObjectMapper로 JSON 문자열을 ResponseVO 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseVO responseVO = objectMapper.readValue(jsonResponse, ResponseVO.class);

        // DB에 저장할 ResponseDTO 리스트
        List<ResponseDTO> responseDTOList = responseVO.getItems().getItem();  // ResponseVO에서 items -> item 리스트로 접근

        // DB에 저장 (MyBatis 사용)
        for (ResponseDTO dto : responseDTOList) {
            dto.setStatus(dto.getStatus());  // status 값을 충전기 상태로 변환
            apiDAO.insertStation(dto);  // MyBatis에서 정의한 insertStation 메소드 호출
        }

        return jsonResponse;  // 응답 결과 반환
    }
}
