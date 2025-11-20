package com.kh.evision.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.kh.evision.exception.FileUploadFailureException;

@Component
public class FileService {
	
	// 파일 경로 필드
	private final Path fileLocation;
	
	// 생성자에 서버로의 파일 업로드 경로 추가
	public FileService() {
		this.fileLocation = Paths.get("uploads").toAbsolutePath().normalize();
	}
	
	// 파일 업로드 메소드
	public FileInfo store(MultipartFile file) {
		
		// 파일명 관련작업
		String originalFileName = file.getOriginalFilename();
		String changedFileName = getChangedFileName(originalFileName);
		
		// 파일 경로 관련작업
		Path targetLocation = this.fileLocation.resolve(changedFileName);
		FileInfo fileInfo = new FileInfo(originalFileName, changedFileName, targetLocation);
		
		// 업로드 시도
		try {
			
			Files.copy(file.getInputStream(),
					   targetLocation,
					   StandardCopyOption.REPLACE_EXISTING);
			return fileInfo;
			
		// 예외처리
		} catch (IOException e) {
			
			e.printStackTrace();
			throw new FileUploadFailureException("파일 업로드에 실패했습니다.");
			
		}
		
	}
	
	// 파일 이름 변경 메소드
	private String getChangedFileName(String originalFileName) {
		
		// 접두규칙
		StringBuilder sb = new StringBuilder();
		sb.append("Evision_");
		
		// 년월일시분초
		String currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		sb.append(currentTime);
		sb.append("_");
		
		// 임의의 수 생성
		int num = (int)(Math.random() * 900) + 100;
		sb.append(num);
		
		// 원본 확장자 추가
		String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
		sb.append(ext);
		
		return sb.toString();
		
	}

}
