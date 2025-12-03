package com.kh.evision.member.controller;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<String> signUp(@Validated @RequestBody MemberDTO member) {
        log.info("회원가입 요청 수신: {}", member);
        int result = memberService.signUp(member);
        if (result > 0) {
            log.info("회원가입 성공: {}", member.getMemberId());
            return ResponseEntity.status(201).body("회원가입 성공");
        } else {
            log.warn("회원가입 실패: {}", member.getMemberId());
            return ResponseEntity.badRequest().body("회원가입 실패");
        }
    }
    
    @GetMapping("/info")
    public ResponseEntity<MemberDTO> getMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String memberNo = userDetails.getUsername(); // JWT subject = memberNo
        MemberDTO member = memberService.getMemberInfo(memberNo);
        return ResponseEntity.ok(member);
    }
    
    @PostMapping("/verify-password")
    public ResponseEntity<Map<String, Boolean>> verifyPassword(
            @RequestBody Map<String, String> request,
            @AuthenticationPrincipal UserDetails userDetails) {  // 또는 Principal principal
        
        // memberNo 가져오는 방법 확인
        String memberNo = userDetails.getUsername(); // 또는 적절한 방법으로
        
        System.out.println("Controller - memberNo: " + memberNo);
        System.out.println("Controller - request: " + request);
        
        String password = request.get("password");
        boolean isValid = memberService.verifyPassword(memberNo, password);
        
        Map<String, Boolean> response = new HashMap<>();
        response.put("success", isValid);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/operator/member-manage")
    public ResponseEntity<List<MemberVO>> memberManage(@RequestHeader("Authorization") String token) {
        // token 검증
        List<MemberVO> members = memberService.memberManage();
        return ResponseEntity.ok(members);
    }

    
    @PutMapping("/changePwd")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        try {
            memberService.changePassword(changePasswordDTO);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("비밀번호 변경에 실패했습니다.");
        }
    }
    
    
    @PutMapping("/info/update")
    public ResponseEntity<?> updateMember(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UpdateMemberDTO updateDto
    ) {
        String memberNo = userDetails.getUsername();
        memberService.updateMemberInfo(memberNo, updateDto);
        return ResponseEntity.ok("회원정보가 수정되었습니다.");
    }
    
    @DeleteMapping("/info/delete")
    public ResponseEntity<?> deleteMyAccount(
    		@AuthenticationPrincipal CustomUserDetails userDetails,
    		@RequestBody Map<String, String> request
    		) {
    	String memberNo = userDetails.getUsername();
    	String password = request.get("password");
    	
    	memberService.deleteMyAccount(memberNo, password);
    	
    	return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
    }
    
    @PutMapping("/admin/change-role/{memberNo}")
    public ResponseEntity<?> changeRole(
    		@PathVariable("memberNo") Long memberNo,
            @RequestBody ChangeRoleDTO change,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
    	change.setMemberNo(memberNo);
        String actingRole = userDetails.getAuthorities().iterator().next().getAuthority();
        boolean success = memberService.changeRole(change, actingRole);

        return success ? ResponseEntity.ok("변경에 성공했습니다!")
                       : ResponseEntity.badRequest().body("변경에 실패했습니다...");
    }
    
    
    @DeleteMapping("/operator/member-manage/{memberNo}")
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
    public ResponseEntity<?> deleteMemberByAdmin(
            @PathVariable("memberNo") String memberNo,
            @AuthenticationPrincipal CustomUserDetails actingUser
    ) {
        String actingRole = actingUser.getAuthorities().iterator().next().getAuthority();
        String actingMemberNo = actingUser.getUsername();

        memberService.deleteMemberByAdmin(memberNo, actingRole, actingMemberNo);

        return ResponseEntity.ok("관리자에 의해 회원이 삭제되었습니다.");
    }


    @PostMapping("/infoLicense")
    public ResponseEntity<String> infoVefyLicense(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody LicenseDTO licenseDTO) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body("로그인이 필요합니다.");
        }

        try {
            String memberNo = userDetails.getUsername();
            memberService.infoVeryfyLicense(licenseDTO, memberNo);
            return ResponseEntity.status(HttpStatus.CREATED).body("운전면허 인증 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("운전면허 인증 실패: " + e.getMessage());
        }
    }

    
    @GetMapping("/hasLicense/{memberNo}")
    public ResponseEntity<Boolean> checkLicense(@PathVariable("memberNo") String memberNo){
    	boolean exists = memberService.hasLicense(memberNo);
    	return ResponseEntity.ok(exists);
    }

    
    

    
}