package com.kh.evision.member.model.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.evision.auth.model.vo.CustomUserDetails;
import com.kh.evision.exception.custom.member.AdminException;
import com.kh.evision.exception.custom.member.CustomAuthenticationException;
import com.kh.evision.exception.custom.member.IdDuplicateException;
import com.kh.evision.exception.custom.member.NicknameDuplicateException;
import com.kh.evision.exception.custom.member.NoMatchPasswordException;
import com.kh.evision.exception.custom.member.NoPasswordException;
import com.kh.evision.exception.custom.member.NotUserException;
import com.kh.evision.exception.custom.member.RoleException;
import com.kh.evision.exception.custom.member.StatusException;
import com.kh.evision.member.model.dao.MemberMapper;
import com.kh.evision.member.model.dto.ChangePasswordDTO;
import com.kh.evision.member.model.dto.ChangeRoleDTO;
import com.kh.evision.member.model.dto.LicenseDTO;
import com.kh.evision.member.model.dto.MemberDTO;
import com.kh.evision.member.model.dto.UpdateMemberDTO;
import com.kh.evision.member.model.vo.LicenseVO;
import com.kh.evision.member.model.vo.MemberVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;
    
    /**
     * 아이디 중복 확인, 닉네임 중복 확인, DTO-> VO변환
     */
    @Override
    public int signUp(MemberDTO member) {
        int count = memberMapper.countByMemberId(member.getMemberId());
        if(1 == count) {
            throw new IdDuplicateException("이미 존재하는 아이디입니다.");
        }
     
        int countNick = memberMapper.countByNickname(member.getNickname());
        if(1 == countNick) {
            throw new NicknameDuplicateException("이미 존재하는 닉네임입니다.");
        }
        
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
        
        if (result == 0) {
            log.error("회원가입 실패 - DB insert 실패: {}", member.getMemberId());
            throw new CustomAuthenticationException("회원가입에 실패했습니다. 잠시 후 다시 시도해주세요.");
        }
		return result;

    }
    
    /**
     * 
     */
    @Override
    public MemberDTO getMemberInfo(String memberNo) {
    	// auth로 조회
        // memberNo 기준으로 조회
        MemberDTO member = memberMapper.loadByMemberNo(memberNo);

        if (member == null) {
            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
        }

        // 비밀번호 제외
        member.setMemberPwd(null);

        return member;
    }
    
    // 얘는 비밀번호 변경할때 사용
    // 비밀번호 확인 예외처리
    @Override
    public boolean verifyPassword(String memberNo, String password) {
    	MemberDTO member = memberMapper.loadByMemberNo(memberNo);
    	// 1. 비밀번호값이 안들어왔을 때
    	if(password == null) {
    		throw new NoPasswordException("비밀번호를 입력해주세요.");
    	}
    	
    	boolean isMatch = passwordEncoder.matches(password, member.getMemberPwd());
    	// 2. 비밀번호가 일치하지않을 때
    	if(!isMatch) {
    		throw new NoMatchPasswordException("비밀번호가 일치하지 않습니다.");
    	}
        return true;
    }

    /**
     * 
     */
    @Override
    public void changePassword(ChangePasswordDTO password) {
        
        // 새 비밀번호 확인 검증
        if (!password.getNewPassword().equals(password.getConfirmPassword())) {
            throw new IllegalArgumentException("새 비밀번호가 일치하지 않습니다.");
        }
        
        // 현재 로그인한 사용자 정보
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails)auth.getPrincipal();
        String memberNo = user.getUsername();
        
        // DB에서 회원 정보 조회
        MemberDTO member = memberMapper.loadByMemberNo(memberNo);
        
        
        // 현재 비밀번호 검증
        if(!passwordEncoder.matches(password.getCurrentPassword(), member.getMemberPwd())) {
            throw new CustomAuthenticationException("현재 비밀번호가 일치하지 않습니다.");
        }
        
        // 새 비밀번호 암호화 및 업데이트
        String newPassword = passwordEncoder.encode(password.getNewPassword());
        Map<String, Object> changeRequest = Map.of(
            "memberNo", memberNo,
            "newPassword", newPassword
        );

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
		// AccessDeniedException말고 새로만들면 여긴끝

	    // 1 acting admin 체크
	    if (!"ROLE_ADMIN".equals(actingRole)) {
	        throw new AccessDeniedException("권한이 없습니다! 관리자만 변경이 가능합니다.");
	    }

	    // 2 비활성화 계정이면 차단
	    if (!"Y".equals(change.getStatus())) {
	        throw new NotUserException("활성화된 계정이 아닙니다.");
	    }

	    // 3 ROLE_USER만 operator로 변경 가능
	    String currentRole = change.getCurrentRole();
	    if (!"ROLE_USER".equals(currentRole)) {
	        throw new NotUserException("ROLE_USER 계정만 OPERATOR로 변경 가능합니다.");
	    }

	    // 4 newRole 제한
	    if (!"ROLE_OPERATOR".equals(change.getNewRole())) {
	        throw new NotUserException("OPERATOR로만 변경 가능 합니다.");
	    }

	    int result = memberMapper.changeRole(change.getMemberNo(), change.getNewRole());

	    if (result == 0) {
	        throw new NotUserException("역할 변경에 실패했습니다.");
	    }

	    return true;
	}


	// 1. 회원이 없을때 
	@Override
	public List<MemberVO> memberManage() {
		return memberMapper.memberManage();
	}

	@Override
	public void updateMemberInfo(String memberNo, UpdateMemberDTO updateDto) {
		// DTO에서 null 아닌 필드만 Map으로 변환 모든 필드를 담으면 null값을 update문에 써야함 
		// null인값은 Map에 안들어감 ㅋㅋ 그래서 SQL에도 안들어감
		// 그래서 sql문보면  if != null일때만 업데이트가 됨
        Map<String, Object> params = new HashMap<>();
        
        params.put("memberNo", memberNo);
        
        
        if (updateDto.getNewName() != null) params.put("memberName", updateDto.getNewName());
        if (updateDto.getNewNickname() != null) params.put("nickname", updateDto.getNewNickname());
        if (updateDto.getNewAddress() != null) params.put("address", updateDto.getNewAddress());
        if (updateDto.getNewPhone() != null) params.put("phone", updateDto.getNewPhone());
        if (updateDto.getNewEmail() != null) params.put("email", updateDto.getNewEmail());

        
        memberMapper.updateMemberInfo(params);
		
	}

	@Override
	public void deleteMyAccount(String memberNo) {
		MemberDTO member = memberMapper.loadByMemberNo(memberNo);

	    if (member == null) {
	        throw new RuntimeException("회원 정보가 존재하지 않습니다.");
	    }

	    memberMapper.softDelete(memberNo);
		
	}

	
	@Override
	public void deleteMemberByAdmin(String memberNo, String actingRole, String actingMemberNo) {
	    MemberDTO target = memberMapper.loadByMemberNo(memberNo);
	    if (target == null) {
	        throw new StatusException("회원 정보가 존재하지 않습니다.");
	    }
	    
	    // 비활성화 된 계정 삭제되는 예외를 처리
	    if(target.getStatus()=='N') {
	    	throw new StatusException("이미 탈퇴되어있는 회원입니다.");
	    }

	    // USER는 삭제 불가
	    if ("ROLE_USER".equals(actingRole)) {
	        throw new RoleException("권한이 없습니다.");
	    }

	    // ADMIN 계정은 누구도 삭제 못함  예외던지기 해야함 내가 직접 그래서 내 메시지가 안넘어가는거임 403으로감 저거는
	    // 그래서 앞단은 403 에러니까 그냥 오류처리하는거임
	    if ("ROLE_ADMIN".equals(target.getRoleStatus())) {
	        throw new AdminException("관리자 계정은 삭제할 수 없습니다.");
	    }

	    // OPERATOR가 OPERATOR 삭제는 가능하도록 유지
	    if ("ROLE_OPERATOR".equals(target.getRoleStatus())) {
	        throw new RoleException("운영자 계정은 삭제할 수 없습니다.");
	    }
	    // OPERATOR가 ADMIN 삭제는 윗 조건에서 이미 걸림

	    memberMapper.softDelete(memberNo);
	}

	

	@Override
	public void infoVeryfyLicense(LicenseDTO licenseDTO, String memberNo) {
	    try {
	        // 날짜 변환
	        Date renewDate = java.sql.Date.valueOf(licenseDTO.getRenewDate());
	        
	        // DTO → VO 변환
	        LicenseVO licenseVO = LicenseVO.builder()
	                .memberNo(Long.parseLong(memberNo))
	                .licenseNo(licenseDTO.getLicenseNo())
	                .renewDate(renewDate)
	                .issuingAgency(licenseDTO.getIssuingAgency())
	                .LicenseClass(licenseDTO.getLicenseClass())
	                .build();

	        // DB insert
	        memberMapper.insertLicense(licenseVO);
	        
	    } catch (NumberFormatException e) {
	        throw new IllegalArgumentException("회원 번호 형식이 올바르지 않습니다.", e);
	    } catch (IllegalArgumentException e) {
	        // Date.valueOf()에서 발생할 수 있는 예외
	        throw new IllegalArgumentException("날짜 형식이 올바르지 않습니다. (YYYY-MM-DD)", e);
	    } catch (DataAccessException e) {
	        // DB 관련 예외
	        throw new RuntimeException("운전면허 정보 저장 중 오류가 발생했습니다.", e);
	    } catch (Exception e) {
	        throw new RuntimeException("운전면허 인증 처리 중 예상치 못한 오류가 발생했습니다.", e);
	    }
	}

	@Override
	public boolean hasLicense(String memberNo) {
		return memberMapper.countLicenseByMemberNo(memberNo) > 0;
	}
    
 }
    
