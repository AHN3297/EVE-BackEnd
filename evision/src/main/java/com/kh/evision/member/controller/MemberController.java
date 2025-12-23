package com.kh.evision.member.controller;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.evision.ResponseDTO.ResponseData;
import com.kh.evision.auth.model.vo.CustomUserDetails;
import com.kh.evision.member.model.dto.ChangePasswordDTO;
import com.kh.evision.member.model.dto.ChangeRoleDTO;
import com.kh.evision.member.model.dto.LicenseDTO;
import com.kh.evision.member.model.dto.MemberDTO;
import com.kh.evision.member.model.dto.UpdateMemberDTO;
import com.kh.evision.member.model.service.MemberService;
import com.kh.evision.member.model.vo.MemberVO;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/member")
public class MemberController {
    
    private final MemberService memberService;
    
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
    
    // 회원가입 엔드포인트
    @PostMapping("/join")
    public ResponseEntity<ResponseData<Object>> signUp(@Valid @RequestBody MemberDTO member) {
    	memberService.signUp(member);
    	/*
    	ResponseData rd = ResponseData.builder()
    			                      .message("회원가입성공")
    			                      .data(member)
    			                      .build();
    	return ResponseEntity.status(HttpStatus.CREATED).body(rd);
    	*/
    	return ResponseData.ok(member,"회원가입 성공");
		
    }
    
    @GetMapping("/info")
    public ResponseEntity<ResponseData<Object>> getMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String memberNo = userDetails.getUsername();
        MemberDTO member = memberService.getMemberInfo(memberNo);
        return ResponseData.ok(member,"마이페이지 조회 성공");
    }
    
    @PostMapping("/verify-password")
    public ResponseEntity<ResponseData<Object>> verifyPassword(
            @RequestBody Map<String, String> request,
            @AuthenticationPrincipal UserDetails userDetails) {  
        
        // memberNo 가져오는 방법 확인
        String memberNo = userDetails.getUsername(); 
        
        String password = request.get("password");
        boolean isValid = memberService.verifyPassword(memberNo, password);
        
        Map<String, Boolean> response = new HashMap<>();
        response.put("success", isValid);
        
        return ResponseData.ok(response,"비밀번호 인증완료");
    }
    
    @GetMapping("/operator/member-manage")
    @PreAuthorize("hasRole('OPERATOR') or hasRole('ADMIN')")
    public ResponseEntity<ResponseData<Object>> memberManage() {
        List<MemberVO> members = memberService.memberManage();
        return ResponseData.ok(members, "회원관리페이지");
    }

    
    @PutMapping("/changePwd")
    public ResponseEntity<ResponseData<Object>> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
    	memberService.changePassword(changePasswordDTO);
    	return ResponseData.ok("비밀번호 변경 성공");
    }
    
    
    @PutMapping("/info")
    public ResponseEntity<ResponseData<Object>> updateMember(@AuthenticationPrincipal CustomUserDetails userDetails, 
    		                              @RequestBody UpdateMemberDTO updateDto) {
        String memberNo = userDetails.getUsername();
        memberService.updateMemberInfo(memberNo, updateDto);
        return ResponseData.ok("회원정보가 수정되었습니다.");
    }
    
    @DeleteMapping("/info")
    public ResponseEntity<ResponseData<Object>> deleteMyAccount(@AuthenticationPrincipal CustomUserDetails userDetails,
    		                                 @RequestBody Map<String, String> request) {
    	String memberNo = userDetails.getUsername();
    	memberService.deleteMyAccount(memberNo);
    	return ResponseData.ok("회원 탈퇴가 완료되었습니다.");
    }
    
    @PutMapping("/admin/change-role/{memberNo}")
    public ResponseEntity<ResponseData<Object>> changeRole(@PathVariable("memberNo") Long memberNo,
                                        @RequestBody ChangeRoleDTO change,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        change.setMemberNo(memberNo);
        
        String actingRole = userDetails.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .filter(auth -> auth.startsWith("ROLE_"))
                .findFirst()
                .orElse(null);
        
        memberService.changeRole(change, actingRole);
        
        return ResponseData.ok("변경에 성공했습니다!");
    }
    
    @DeleteMapping("/operator/member-manage/{memberNo}")
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
    public ResponseEntity<ResponseData<Object>> deleteMemberByAdmin(@PathVariable("memberNo") String memberNo, 
    		                                     @AuthenticationPrincipal CustomUserDetails actingUser) {
        String actingRole = actingUser.getAuthorities().iterator().next().getAuthority();
        String actingMemberNo = actingUser.getUsername();

        memberService.deleteMemberByAdmin(memberNo, actingRole, actingMemberNo);

        return ResponseData.ok("관리자에 의해 회원이 삭제되었습니다.");
    }


    @PostMapping("/infoLicense")
    public ResponseEntity<ResponseData<Object>> infoVerifyLicense(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody LicenseDTO licenseDTO) {
        
        String memberNo = userDetails.getUsername();
        memberService.infoVeryfyLicense(licenseDTO, memberNo);
        return ResponseData.ok("운전면허 인증 완료");
    }

    
    @GetMapping("/hasLicense/{memberNo}")
    public ResponseEntity<ResponseData<Object>> checkLicense(@PathVariable("memberNo") String memberNo){
    	boolean exists = memberService.hasLicense(memberNo);
    	return ResponseData.ok(exists);
    }

    
    

    
}