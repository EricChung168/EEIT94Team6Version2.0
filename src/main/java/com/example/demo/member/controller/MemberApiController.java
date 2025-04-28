package com.example.demo.member.controller;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.member.model.Member;
import com.example.demo.member.model.MemberDTOByEmp;
import com.example.demo.member.model.MemberDTOFE;
import com.example.demo.member.model.MemberRepository;
import com.example.demo.member.model.MemberService;
import com.example.demo.member.model.MemberType;
import com.example.demo.member.model.MemberTypeRepository;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/memberAPI")
public class MemberAPIController {
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private MemberTypeRepository memberTypeRepository;
	@Autowired
    private MemberService memberService;
	
	@Autowired
	private PasswordEncoder pwdEncoder;


	 // 1. 讀取會員資料
    @GetMapping("/memberDetial/{memberId}")
    public MemberDTOFE getMemberDetail(@PathVariable Long memberId) {
        Member member = memberService.findById(memberId);
        return new MemberDTOFE(
            member.getMemberId(),
            member.getName(),
            member.getGender(),
            member.getEmail(),
            member.getPhoneNumber(),
            member.getNationalId(),
            member.getDateOfBirth() != null ? member.getDateOfBirth().toString() : null,
            member.getMemberType().getTypeName()
        );
    }

    // 2. 更新會員資料
    @PostMapping("/updateProfile")
    public Map<String, Object> updateMemberProfile(
            @RequestParam Long memberId,
            @RequestParam String name,
            @RequestParam String dateOfBirth,
            @RequestParam String gender,
            @RequestParam String nationalId
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            memberService.updateMemberProfile(memberId, name, dateOfBirth, gender,nationalId);
            response.put("status", "success");
            response.put("message", "會員資料更新成功！");
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "更新失敗：" + e.getMessage());
        }
        return response;
    }
    @PostMapping("/changePassword")
    public Map<String, Object> changePassword(
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmNewPassword,
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();

        Member loginMember = (Member) session.getAttribute("memberDetail");

        if (loginMember == null) {
            response.put("status", "error");
            response.put("message", "請先登入！");
            return response;
        }

        // 1. 驗證原密碼是否正確
        if (!pwdEncoder.matches(currentPassword, loginMember.getPassword())) {
            response.put("status", "error");
            response.put("message", "原密碼錯誤！");
            return response;
        }

        // 2. 驗證新密碼不能跟原密碼一樣
        if (pwdEncoder.matches(newPassword, loginMember.getPassword())) {
            response.put("status", "error");
            response.put("message", "新密碼不能與原密碼相同！");
            return response;
        }

        // 3. 驗證新密碼與確認新密碼一致
        if (!newPassword.equals(confirmNewPassword)) {
            response.put("status", "error");
            response.put("message", "新密碼與確認新密碼不一致！");
            return response;
        }

        // 4. 更新密碼
        loginMember.setPassword(pwdEncoder.encode(newPassword));
        memberRepository.save(loginMember);

        response.put("status", "success");
        response.put("message", "密碼變更成功！");
        return response;
    }
    
    @GetMapping("/memberListEmp")
    public List<MemberDTOByEmp> getAllMembersForEmployee() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return memberRepository.findAll().stream()
            .map(member -> new MemberDTOByEmp(
                    member.getMemberId(),
                    member.getName(),
                    member.getGender(),
                    member.getEmail(),
                    member.getPhoneNumber(),
                    member.getCreateTime().format(formatter)
            ))
            .collect(Collectors.toList());
    }
    
    @PostMapping("/updateProfileByEmp")
    public Map<String, Object> updateProfileByEmp(
            @RequestParam Long memberId,
            @RequestParam String name,
            @RequestParam String gender,
            @RequestParam String email,
            @RequestParam String phoneNumber,
            @RequestParam String dateOfBirth
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            memberService.updateMemberByEmp(memberId, name, gender, email, phoneNumber, dateOfBirth);
            response.put("status", "success");
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        return response;
    }
    
    
//    後台看會員的詳細資料
    
    @GetMapping("/memberDetailByEmp/{memberId}")
    public Map<String, Object> getMemberDetailByEmp(@PathVariable Long memberId) {
        Member member = memberService.findById(memberId);
        Map<String, Object> response = new HashMap<>();
        if (member == null) {
            response.put("status", "error");
            response.put("message", "找不到會員資料");
            return response;
        }

        response.put("status", "success");
        response.put("memberId", member.getMemberId());
        response.put("name", member.getName());
        response.put("gender", member.getGender());
        response.put("dateOfBirth", member.getDateOfBirth());
        response.put("nationalId", member.getNationalId());
        response.put("email", member.getEmail());
        response.put("phoneNumber", member.getPhoneNumber());
        response.put("newEmail", member.getNewEmail());
        response.put("verification", member.isVerification());
        response.put("createTime", member.getCreateTime() != null ? member.getCreateTime().toLocalDate().toString() : null);
        response.put("memberType", member.getMemberType() != null ? member.getMemberType().getTypeName() : null);
        return response;
    }
    
    //後台新增會員
    @PostMapping("/registerByEmp")
    public Map<String, Object> registerMemberByEmployee(
            @RequestParam String name,
            @RequestParam String gender,
            @RequestParam String email,
            @RequestParam String phoneNumber,
            @RequestParam String dateOfBirth,
            @RequestParam String nationalId
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 直接新增會員，不走認證
            Member member = new Member();
            member.setName(name);
            member.setGender(gender);
            member.setEmail(email);
            member.setPhoneNumber(phoneNumber);
            member.setDateOfBirth(dateOfBirth);
            member.setNationalId(nationalId);

            // 預設密碼：電話號碼當密碼
            member.setPassword(pwdEncoder.encode(phoneNumber));

            // 預設已驗證
            member.setVerification(true);

            // 預設會員身份（要確保這裡有一個預設 MemberType存在，例如普通會員）
            MemberType defaultType = memberService.findDefaultMemberType(); // 你要確保這個 service存在
            member.setMemberType(defaultType);

            memberRepository.save(member);

            response.put("status", "success");
            response.put("message", "會員新增成功！");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("status", "error");
            response.put("message", "新增失敗：" + e.getMessage());
        }
        return response;
    }

    //取得所有會員身分
    @GetMapping("/memberTypes")
    public List<Map<String, Object>> getAllMemberTypes() {
        return memberTypeRepository.findAll().stream()
                .map(type -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", type.getMemberTypeId());
                    map.put("name", type.getTypeName());
                    return map;
                })
                .toList();
    }
}

