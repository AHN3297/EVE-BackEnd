package com.kh.evision.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class ImgInfo {
	
	private Long imgId;
	
	private Long carNo;
	
	private String originName;
	private String changeName;
	private String filePath;
	
	// private MultipartFile file;

}
