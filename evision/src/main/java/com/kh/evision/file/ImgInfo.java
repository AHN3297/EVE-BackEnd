package com.kh.evision.file;

import java.nio.file.Path;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ImgInfo {
	
	private String originName;
	private String changeName;
	private String filePath;

}
