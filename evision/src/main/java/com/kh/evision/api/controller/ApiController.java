package com.kh.evision.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kh.evision.api.model.service.ApiService;

import lombok.RequiredArgsConstructor;

@Controller
@ResponseBody
@RequestMapping("api")
@RequiredArgsConstructor
public class ApiController {
	private final ApiService service;
	
	@GetMapping("/station")
	public String getStation(@RequestParam(name="pageNo") int pageNo) throws Exception {
		return service.requestChargerInfo(pageNo);
	}
	
}
