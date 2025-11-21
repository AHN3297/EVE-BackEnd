package com.kh.evision.file;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class FileInfo {
	
	private int fileId;
	private String originName;
	private String changeName;
	private String filePath;
	
	private MultipartFile file;

}
