package com.kh.evision.member.model.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.evision.auth.model.vo.CustomUserDetails;
import com.kh.evision.exception.custom.member.CustomAuthenticationException;
import com.kh.evision.exception.custom.member.IdDuplicateException;
import com.kh.evision.exception.custom.member.NicknameDuplicateException;
import com.kh.evision.member.model.dao.MemberMapper;
import com.kh.evision.member.model.dto.ChangePasswordDTO;
import com.kh.evision.member.model.dto.ChangeRoleDTO;
import com.kh.evision.member.model.dto.LicenseDTO;
import com.kh.evision.member.model.dto.MemberDTO;
import com.kh.evision.member.model.dto.UpdateMemberDTO;
import com.kh.evision.member.model.vo.LicenseVO;
import com.kh.evision.member.model.vo.MemberVO;
import com.kh.evision.token.model.dao.TokenMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;
    private final TokenMapper tokenMapper; 
    
    
    @Override
    public int signUp(MemberDTO member) {
        // 아이디 중복 확인
        int count = memberMapper.countByMemberId(member.getMemberId());
        if(1 == count) {
            throw new IdDuplicateException("이미 존재하는 아이디입니다.");
        }
        
        // 닉네임 중복 확인
        int countNick = memberMapper.countByNickname(member.getNickname());
        if(1 == countNick) {
            throw new NicknameDuplicateException("이미 존재하는 닉네임입니다.");
        }
        
        // DTO → VO 변환
        MemberVO memberVO = MemberVO.builder()
        	    .memberId(member.getMemberId())
        	    .memberPwd(passwordEncoder.encode(member.getMemberPwd()))
        	    .memberName(member.getMemberName())
        	    .nickname(member.getNickname() != null && !member.getNickname().isEmpty() ? member.getNickname() : "사용자")  // 기본값
        	    .address(member.getAddress() != null && !member.getAddress().isEmpty() ? member.getAddress() : "미입력")      // 기본값
        	    .phone(member.getPhone() != null && !member.getPhone().isEmpty() ? member.getPhone() : "010-0000-0000")      // 기본값
        	    .email(member.getEmail() != null ? member.getEmail() : null)
        	    .status('Y')
        	    .roleStatus(member.getRoleStatus())
        	    .build();
        int result = memberMapper.signUp(memberVO);
        log.info("사용자 등록 성공 : {}", memberVO);
		return result;

    }
    
    @Override
    public MemberDTO getMemberInfo(String memberNo) {
        // memberNo 기준으로 조회
        MemberDTO member = memberMapper.loadByMemberNo(memberNo);

        if (member == null) {
            throw new RuntimeException("회원 정보가 존재하지 않습니다.");
        }

        // 비밀번호 제외
        member.setMemberPwd(null);

        return member;
    }



	@Override
	public void changePassword(ChangePasswordDTO password) {
		CustomUserDetails user = validatePassword(password.getCurrentPassword());
		String newPassword = passwordEncoder.encode(password.getNewPassword());
		Map<String, Object> changeRequest = Map.of("memberNo", user.getUsername(),
												   "newPassword", newPassword);
	
		memberMapper.changePassword(changeRequest);
	
	}
    
	
	private CustomUserDetails validatePassword(String password) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails user = (CustomUserDetails)auth.getPrincipal();
		
		
		if(!passwordEncoder.matches(password, user.getPassword())) {
			throw new CustomAuthenticationException("비밀번호가 일치하지 않습니다.");
		}
		return user;
	}


	@Override
	public boolean changeRole(ChangeRoleDTO change, String actingRole) {
		if(!"ROLE_ADMIN".equals(actingRole)) {
			throw new AccessDeniedException("권한이 없습니다! 관리자만 변경이 가능합니다.");
		}
		// 1. admin을 변경할 수 없게 예외처리
		// 2. admin 외에는 변경할 수 없음
		// 3. 오직 user -> operator만 가능하게
		if(!change.getNewRole().equals("ROLE_USER") && !change.getNewRole().equals("ROLE_OPERATOR")) {
			throw new IllegalArgumentException("변경할 수 없는 권한입니다."); // 일단 지금은 이거쓰고 학원에서는 예외를 만들자
			// 변경되는 값이 user랑 oprator임, 이거 2개 외에는 변경이 되면 안된다는뜻
		}
		int result = memberMapper.changeRole(change);
		
		return result > 0;
	}


	@Override
	public List<MemberVO> memberManage() {
		return memberMapper.memberManage();
	}

	@Override
	public void updateMemberInfo(String memberNo, UpdateMemberDTO updateDto) {
		// DTO에서 null 아닌 필드만 Map으로 변환 모든 필드를 담으면 null값을 update문에 써야함 null인값은 Map에 안들어감 ㅋㅋ 그래서 SQL에도 안들어감
		// 그래서 sql문보면  if != null일때만 업데이트가 됨
        Map<String, Object> params = new HashMap<>();
        
        params.put("memberNo", memberNo);
        
        if (updateDto.getNewName() != null) params.put("nickname", updateDto.getNewName());
        if (updateDto.getNewNickname() != null) params.put("nickname", updateDto.getNewNickname());
        if (updateDto.getNewAddress() != null) params.put("address", updateDto.getNewAddress());
        if (updateDto.getNewPhone() != null) params.put("phone", updateDto.getNewPhone());
        if (updateDto.getNewEmail() != null) params.put("email", updateDto.getNewEmail());

        
        memberMapper.updateMemberInfo(params);
		
	}

	@Override
	public void deleteMyAccount(String memberNo, String password) {
		MemberDTO member = memberMapper.loadByMemberNo(memberNo);

	    if (member == null) {
	        throw new RuntimeException("회원 정보가 존재하지 않습니다.");
	    }

	    // 비밀번호 검증
	    if (!passwordEncoder.matches(password, member.getMemberPwd())) {
	        throw new RuntimeException("비밀번호가 일치하지 않습니다.");
	    }

	    memberMapper.softDelete(memberNo);
		
	}

	@Override
	public void deleteMemberByAdmin(String memberNo, String actingRole, String actingMemberNo) {
	    MemberDTO target = memberMapper.loadByMemberNo(memberNo);
	    if (target == null) {
	        throw new RuntimeException("회원 정보가 존재하지 않습니다.");
	    }

	    // USER는 삭제 불가
	    if ("ROLE_USER".equals(actingRole)) {
	        throw new AccessDeniedException("권한이 없습니다.");
	    }

	    // ADMIN 계정은 누구도 삭제 못함  예외던지기 해야함 내가 직접 그래서 내 메시지가 안넘어가는거임 403으로감 저거는
	    // 그래서 앞단은 403 에러니까 그냥 오류처리하는거임
	    if ("ROLE_ADMIN".equals(target.getRoleStatus())) {
	        throw new AccessDeniedException("관리자 계정은 삭제할 수 없습니다.");
	    }

	    // OPERATOR가 OPERATOR 삭제는 가능하도록 유지
	    // OPERATOR가 ADMIN 삭제는 윗 조건에서 이미 걸림

	    memberMapper.softDelete(memberNo);
	}

	

	@Override
	public void infoVeryfyLicense(LicenseDTO licenseDTO, String memberNo) {
		Date renewDate = java.sql.Date.valueOf(licenseDTO.getRenewDate());
	    try {
	        // DTO → VO 변환
	        LicenseVO licenseVO = LicenseVO.builder()
	                .memberNo(Long.parseLong(memberNo)) // 로그인된 회원 번호
	                .licenseNo(licenseDTO.getLicenseNo()) // 문자열로 받았다면 parse
	                .renewDate(renewDate)
	                .issuingAgency(licenseDTO.getIssuingAgency())
	                .LicenseClass(licenseDTO.getLicenseClass())
	                .build();

	        // DB insert
	        memberMapper.insertLicense(licenseVO);
	    } catch (NumberFormatException e) {
	        throw new IllegalArgumentException("번호 형식이 올바르지 않습니다.", e);
	    }
	}

	@Override
	public boolean hasLicense(String memberNo) {
		return memberMapper.countLicenseByMemberNo(memberNo) > 0;
	}


	
	
	
    
    
 }
    
