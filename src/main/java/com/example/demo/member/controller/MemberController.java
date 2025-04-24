package com.example.demo.member.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.member.model.Member;
import com.example.demo.member.model.MemberService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/member")
public class MemberController {

	@Autowired
	private MemberService memberService;

	// Login Controller
	@GetMapping("/login")
	public String loginPage() {
		return "member/login";
	}

	@PostMapping("/login")
	@ResponseBody
	public Map<String, Object> login(@RequestParam("email") String email, @RequestParam("password") String password,
			HttpSession httpSession) {
		Map<String, Object> response = new HashMap<>();
		Member LoginBean = memberService.login(email, password);
		if (LoginBean != null) {
			httpSession.setAttribute("memberDetail", LoginBean);
			httpSession.setAttribute("memberName", LoginBean.getName());
			httpSession.setAttribute("memberEmail", LoginBean.getEmail());
			httpSession.setAttribute("memberId", LoginBean.getMemberId());
			response.put("status", "success");
			response.put("name", LoginBean.getName());
		} else {
			response.put("status", "error");
			response.put("message", "帳號或密碼錯誤");
		}
		return response;
	}

	@GetMapping("/logout")
	public String logout(HttpSession httpSession, RedirectAttributes redirectAttributes) {
		httpSession.removeAttribute("memberDetail");
		httpSession.invalidate(); // 清除所有session
		redirectAttributes.addFlashAttribute("logoutSuccess", true);
		return "redirect:/";

	}

	
	
	
	
	@GetMapping("/register")
	public String register() {
		return "member/register";
	}

//前台會員註冊
	@PostMapping("/register")
	public ResponseEntity<?> register(@ModelAttribute Member member) {
		boolean result = memberService.memberExistCheck(member);

		if (result) {
			String verifyCode = UUID.randomUUID().toString().replace("-", "");
			Member insertMember = memberService.insertMember(member, verifyCode);
			String Toemail = insertMember.getEmail();
			String siteURL = "http://localhost:8080";
			String verifyLink = siteURL + "/member/verify?code=" + verifyCode;
			System.out.println(Toemail + " " + verifyLink);
			try {
				memberService.sendVerificationEmail(Toemail, verifyLink);
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			;
			if (insertMember != null) {
				return ResponseEntity.ok(Map.of("status", "success", "message", "註冊成功", "name", insertMember.getName(),
						"email", insertMember.getEmail()));
			} else {
				return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "註冊失敗，新增失敗"));
			}
		} else {
			return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "電子信箱或手機號碼已被使用"));
		}
	}

	@GetMapping("/verify")
	public String verifyAccount(@RequestParam("code") String code, Model model) {
		boolean verified = memberService.verifyMessage(code);

		if (verified) {
			model.addAttribute("message", "帳號已成功啟用，請登入！");
			return "member/verifySuccess";
		} else {
			model.addAttribute("message", "驗證失敗，連結可能已過期或無效！");
			return "member/verifyFail";
		}
	}

	@PostMapping("/email/change")
	@ResponseBody
	public ResponseEntity<String> changeEmail(@RequestParam String newEmail, HttpSession session)
			throws MessagingException {
		Member member = (Member) session.getAttribute("memberDetail");
		// 驗證是否重複或非法，可選擇加上
		if (newEmail.equals(member.getEmail())) {
			return ResponseEntity.badRequest().body("新信箱與舊信箱相同");
		} else {
			memberService.EmailChange(member, newEmail);
			return ResponseEntity.ok("認證信已寄出，請至原信箱確認變更操作");
		}
	}

	@GetMapping("/email/confirm")
	public String confirmEmailChange(@RequestParam("code") String code, Model model) {
		boolean newEmailStatus = memberService.newEmailConfirm(model, code);
		if (newEmailStatus) {
			model.addAttribute("message", "信箱變更成功，請使用新信箱登入");
		} else {
			model.addAttribute("message", "驗證失敗，連結無效或已過期");
		}
		return "member/verifySuccess";
	}





//GetMapping
	@GetMapping("/memberDetail")
	public String memberDetail() {
		return "member/memberDetail";
	}
}