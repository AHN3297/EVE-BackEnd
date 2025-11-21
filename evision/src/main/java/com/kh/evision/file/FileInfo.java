package com.kh.evision.file;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FileInfo {
	
	private int fileId;
	private String originName;
	private String changeName;
	private String filePath;

}
