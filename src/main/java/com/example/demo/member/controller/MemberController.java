package com.example.demo.member.controller;

import java.util.HashMap;
import java.util.Map;

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

	// 會員登入-會員註冊在這裡
	
	@GetMapping("/memberCenter")
	public String memberCenter() {
		return "member/memberCenter";
	}
	// 會員註冊
	// GetMapping
	@GetMapping("/register")
	public String register() {
		return "member/register";
	}

	// PostMapping
	@PostMapping("/register")
	@ResponseBody
	public ResponseEntity<?> register(@ModelAttribute Member member) {
		try {
			boolean exist = memberService.memberExistCheck(member);
			if (!exist) {
				return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "電子信箱或手機號碼已被使用"));
			}

			Member insertedMember = memberService.register(member); // 改用新 register()
			if (insertedMember != null) {
				return ResponseEntity.ok(Map.of("status", "success", "message", "註冊成功，驗證信已寄出！", "name",
						insertedMember.getName(), "email", insertedMember.getEmail()));
			} else {
				return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "註冊失敗，請稍後再試"));
			}
		} catch (MessagingException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "寄送驗證信失敗，請稍後再試"));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "註冊異常：" + e.getMessage()));
		}
	}

	@PostMapping("/login")
	@ResponseBody
	public Map<String, Object> login(@RequestParam("email") String email, @RequestParam("password") String password,
			HttpSession session) {
		Map<String, Object> response = new HashMap<>();

		// 1. 檢查該email的帳號有沒有存在
		Member loginMember = memberService.login(email, password);

		if (loginMember == null) {
			// 找不到或密碼錯
			response.put("status", "error");
			response.put("message", "帳號或密碼錯誤");
			return response;
		}

		if (!loginMember.isVerification()) {
			// 找到，但未驗證
			response.put("status", "unverified");
			response.put("message", "帳號尚未啟用，請完成信箱驗證！");
			response.put("memberId", loginMember.getMemberId()); // 傳回memberId，方便前端跳轉
			return response;
		}

		// 2. 登入成功，設置 Session
		session.setAttribute("memberDetail", loginMember);
		session.setAttribute("memberName", loginMember.getName());
		session.setAttribute("memberEmail", loginMember.getEmail());
		session.setAttribute("memberId", loginMember.getMemberId());

		response.put("status", "success");
		response.put("name", loginMember.getName());
		return response;
	}
	
	@GetMapping("/verify")
	public String verifyAccount(@RequestParam("code") String code, Model model) {
		String result = memberService.verifyMember(code);

		switch (result) {
		case "success" -> {
			model.addAttribute("message", "帳號已成功啟用，請登入！");
			return "member/verifySuccessByFE";
		}
		case "already_verified" -> {
			model.addAttribute("message", "此帳號已經啟用過，請直接登入！");
			return "member/verifySuccessByFE";
		}
		case "error" -> {
			model.addAttribute("message", "驗證失敗，連結可能已過期或無效！");
			return "member/verifyFailByFE";
		}
		default -> {
			model.addAttribute("message", "發生未知錯誤，請聯絡客服！");
			return "member/verifyFailByFE";
		}
		}
	}

	// 顯示「重新發送驗證信」頁面
	@GetMapping("/resendVerify")
	public String showResendVerifyPage(HttpSession session, Model model) {
		// 從session拿memberEmail
		String memberEmail = (String) session.getAttribute("memberEmail");
		if (memberEmail == null) {
			return "redirect:/login"; // 沒登入直接打回去
		}
		model.addAttribute("memberEmail", memberEmail);
		return "member/resendVerify"; // resources/templates/member/resendVerify.html
	}

	// 處理按下「重新發送驗證信」
	@PostMapping("/resendVerify/send")
	@ResponseBody
	public Map<String, Object> resendVerifyEmail(HttpSession session) {
		Map<String, Object> response = new HashMap<>();

		try {
			String email = (String) session.getAttribute("memberEmail");
			if (email == null) {
				response.put("status", "error");
				response.put("message", "請重新登入");
				return response;
			}

			Member member = memberService.findByEmail(email);
			if (member == null) {
				response.put("status", "error");
				response.put("message", "查無會員資料");
				return response;
			}

			if (member.isVerification()) {
				response.put("status", "already_verified");
				response.put("message", "此帳號已啟用，無需重新驗證");
				return response;
			}

			// 重新寄原本的驗證碼
			memberService.sendVerificationEmail(member.getEmail(), member.getOriginEmailCode());

			response.put("status", "success");
			response.put("message", "驗證信已重新寄送，請至信箱查收");
			return response;

		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", "error");
			response.put("message", "寄送失敗，請稍後再試！");
			return response;
		}
	}
	
	
	//登出
	@GetMapping("/logout")
	public String logout(HttpSession httpSession,RedirectAttributes redirectAttributes) {
		httpSession.removeAttribute("memberDetail");
		httpSession.removeAttribute("memberName");
		httpSession.removeAttribute("memberEmail");
		httpSession.removeAttribute("memberId");
	    httpSession.invalidate(); // 清除所有session
	    redirectAttributes.addFlashAttribute("logoutSuccess", true);
		return "redirect:/";

	}
	
	@GetMapping("/forgotPasswordPage")
	public String forgotPasswordPage() {
		return "member/forgotPasswordPage";
	}

	@PostMapping("/sendResetPassword")
	@ResponseBody
	public Map<String, Object> sendResetPassword(@RequestParam("email") String email) {
		Map<String, Object> response = new HashMap<>();
		try {
			memberService.sendResetPasswordEmail(email);
			response.put("status", "success");
			response.put("message", "已發送重設密碼信件！");
		} catch (RuntimeException | MessagingException e) {
			response.put("status", "error");
			response.put("message", e.getMessage());
		}
		return response;
	}

	@GetMapping("/resetPassword")
	public String resetPasswordPage(@RequestParam("email") String email, @RequestParam("code") String code,
			Model model) {
		model.addAttribute("email", email);
		model.addAttribute("code", code);
		return "member/resetPassword"; // 指向 Thymeleaf 頁面
	}
	
	@PostMapping("/resetPasswordConfirm")
	@ResponseBody
	public Map<String, Object> resetPasswordConfirm(@RequestParam("password") String password,
			@RequestParam("email") String email, @RequestParam("code") String code) {
		Map<String, Object> response = new HashMap<>();
		try {
			memberService.resetPassword(password, email, code);
			response.put("status", "success");
			response.put("message", "密碼重設成功，請重新登入！");
		} catch (RuntimeException e) {
			response.put("status", "error");
			response.put("message", e.getMessage());
		}
		return response;
	}
	
	
	@GetMapping("/changeEmailPage")
	public String changeEmailPage() {
	    return "member/changeEmail"; // 對應到 templates/member/changeEmail.html
	}
	
	
	// 信箱變更
	@PostMapping("/changeEmail")
	@ResponseBody
	public Map<String, Object> changeEmail(@RequestParam("newEmail") String newEmail, HttpSession session) {
		Map<String, Object> response = new HashMap<>();
		try {
			Long memberId = (Long) session.getAttribute("memberId");
			memberService.requestChangeEmail(memberId, newEmail);
			response.put("status", "success");
			response.put("message", "已寄送變更信箱驗證信！");
		} catch (Exception e) {
			response.put("status", "error");
			response.put("message", e.getMessage());
		}
		return response;
	}

	@GetMapping("/confirmNewEmail")
	public String confirmNewEmail(@RequestParam("code") String code, Model model) {
		String result = memberService.confirmChangeEmail(code);
		model.addAttribute("message", result);
		return "member/changeEmailResult"; // 顯示結果
	}
	
	@GetMapping("/changePasswordPage")
	public String changePasswordPage() {
	    return "member/changePassword"; 
	}
	
	
	//後台網站
	
	//會員列表
	@GetMapping("/memberListEmp")
	public String memberListEmp() {
	    return "member/memberListEmp"; 
	}
	

}
