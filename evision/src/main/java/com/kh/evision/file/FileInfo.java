package com.kh.evision.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileInfo {
	
	private Long fileId;
	
	private Long primaryKeyNo;
	
	private String originName;
	private String changeName;
	private String filePath;
	
	// private MultipartFile file;

}
