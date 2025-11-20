package com.kh.evision.api.model.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.springframework.stereotype.Service;

@Service
public class ApiService {

    public String requestChargerInfo(int pageNo) throws Exception {
        // 서비스 키 (공공 데이터 포털에서 제공된 인증키, URL 인코딩 필요)
        String serviceKey = "52aa9c9f9752c4bb85b09a0ad25b262d2713ee5ea6efdab594d34b441011d3d4"; 
        
        // 기본 URL
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552584/EvCharger/getChargerInfo");
        urlBuilder.append("?" + "serviceKey=" + serviceKey);  // 인증키
        urlBuilder.append("&" + "numOfRows=" + 10);  // 한 페이지 결과 수
        urlBuilder.append("&" + "pageNo=" + pageNo);  // 페이지 번호
        urlBuilder.append("&" + "dataType=" + "JSON");  // 응답 데이터 타입 (XML)

        // 최종 URL
        URL url = new URL(urlBuilder.toString());
        System.out.println(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");  // 요청 헤더에 content-type 설정

        // 응답 코드 확인
        int responseCode = conn.getResponseCode();
        System.out.println("Response code: " + responseCode);

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

        return sb.toString();  // 응답 결과 반환
    }
    
}
