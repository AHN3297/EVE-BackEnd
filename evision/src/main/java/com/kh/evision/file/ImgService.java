package com.kh.evision.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.kh.evision.exception.FileUploadFailureException;
import com.kh.evision.exception.InvalidImgFormatException;

@Component
public class ImgService {
	
	// 파일 경로 필드
	private final Path imgLocation;
	
	// 지원 확장자 저장용 필드
	private final Set<String> supportedExts = Set.of(
			
		"jpg",
		"jpeg",
		"png",
		"gif",
		"bmp",
		"heic",
		"heif",
		"webp",
		"svg"
		
	);
	
	// 생성자에 서버로의 파일 업로드 경로 추가
	public ImgService() {
		this.imgLocation = Paths.get("uploads").toAbsolutePath().normalize();
	}
	
	// 파일 업로드 메소드
	public ImgInfo store(MultipartFile file, Long carNo) {
		
		// 파일명 관련작업
		String originalImgName = file.getOriginalFilename();
		String changedImgName = getChangedFileName(originalImgName);
		
		// 파일 경로 관련작업
		Path targetLocation = this.imgLocation.resolve(changedImgName);
		// 객체에서 경로 필드의 자료형을 String으로 변경 후 -> targetLocation이 Path형이니 넣어줄 때 toString으로 변환
		ImgInfo imgInfo = ImgInfo.builder()
								 .carNo(carNo)
								 .originName(originalImgName)
								 .changeName(changedImgName)
								 .filePath(targetLocation.toString())
								 .build();
				// new FileInfo(originalImgName, changedImgName, targetLocation.toString());
		// 이걸로 해보고 안되면 absolute path로 ? 이러면 C: 이게 들어가서 안될지도?
		
		// 업로드 시도
		try {
			
			Files.copy(file.getInputStream(),
					   targetLocation,
					   StandardCopyOption.REPLACE_EXISTING);
			return imgInfo;
			
		// 예외처리
		} catch (IOException e) {
			
			e.printStackTrace();
			throw new FileUploadFailureException("파일 업로드에 실패했습니다.");
			
		}
		
	}
	
	// 이미지 이름 변경 메소드
	private String getChangedFileName(String originalFileName) {
		
		// 접두규칙 -> 이미지용
		StringBuilder sb = new StringBuilder();
		sb.append("Evision_Img");
		
		// 년월일시분초
		String currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		sb.append(currentTime);
		sb.append("_");
		
		// 임의의 수 생성
		int num = (int)(Math.random() * 900) + 100;
		sb.append(num);
		
		// 원본 확장자 분리
		String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
		
		// 검증 메소드 호출 -> 이미지 아니면 어케야하지? 파일 서비스로 연결하고싶은데...? 지금은 바로 예외발생시켜뒀음
		isImg(ext);

		// 원본 확장자 추가
		sb.append(ext);
		
		return sb.toString();
		
	}
	
	// 이미지 확장자 검증 메소드
	private void isImg(String originalExt) {
		
		String testExt = originalExt.substring(1).toLowerCase();
		
		// 원본 이미지 확장자 검증
		if(!supportedExts.contains(testExt)) {
			throw new InvalidImgFormatException("지원되는 이미지 형식이 아닙니다.");
		}
		
	}

}
