package com.kh.evision.member.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    
    @GetMapping("/operator/member-manage")
    public ResponseEntity<List<MemberVO>> memberManage() {
        List<MemberVO> members = memberService.memberManage();
        return ResponseEntity.ok(members);
    }
    
    @PutMapping("/info/changePwd")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDTO password){
    	memberService.changePassword(password);
    	return ResponseEntity.ok("확인되었습니다.");
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
    
    
    @PostMapping("/admin/change-role/{memberNo}")
    public ResponseEntity<?> changeRole(
            @RequestBody ChangeRoleDTO change,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        String actingRole = userDetails.getAuthorities().iterator().next().getAuthority();
        boolean success = memberService.changeRole(change, actingRole);

        return success ? ResponseEntity.ok("변경에 성공했습니다!")
                       : ResponseEntity.badRequest().body("변경에 실패했습니다...");
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
    
    @PostMapping("/license")
    public ResponseEntity<String> verifyLicense(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody LicenseDTO licenseDTO
    ) {
        memberService.verifyLicense(userDetails.getUsername(), licenseDTO);
        return ResponseEntity.ok("라이센스 인증 완료");
    }

    
    

    
}