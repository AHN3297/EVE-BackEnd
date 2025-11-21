package com.kh.evision.file;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ImgInfo {
	
	private int imgId;
	private String originName;
	private String changeName;
	private String filePath;
	
	private MultipartFile file;

}
