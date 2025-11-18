package com.kh.evision.file;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {
	
	// 파일 경로 필드
	private final Path fileLocation;
	
	// 파일 경로 생성자에 서버의 업로드 경로 추가
	public FileService() {
		this.fileLocation = Paths.get("uploads").toAbsolutePath().normalize();
	}
	
	// 파일 업로드 메소드
	public String store(MultipartFile file) {
		
		String originalFilename = file.getOriginalFilename();
		String changedFileName = getChangedFileName(originalFilename);
		
		Path targetLocation = this.fileLocation.resolve(changedFileName);
		
	}
	
	// 파일 이름 변경 메소드
	private String getChangedFileName(String originalFileName) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("Evision_");
		
		String currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		sb.append(currentTime);
		sb.append("_");
		
		int num = (int)(Math.random() * 900) + 100;
		sb.append(num);
		
		String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
		sb.append(ext);
		
		return sb.toString();
		
	}

}
