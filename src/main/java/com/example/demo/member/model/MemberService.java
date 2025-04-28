package com.example.demo.member.model;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.member.model.Member;
import com.example.demo.member.model.MemberRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MemberService {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private MemberTypeRepository memberTypeRepository;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private PasswordEncoder pwdEncoder;

	@Autowired
	private MemberMailService mailService;

	public Member findById(Long id) {
		Member member = null;
		Optional<Member> optional = memberRepository.findById(id);
		if (optional.isPresent()) {
			member = optional.get();
		}
		return member;
	}

	// 註冊會員
	@Transactional
	public Member register(Member member) throws MessagingException {
		// 密碼加密
		String hashedPwd = pwdEncoder.encode(member.getPassword());
		member.setPassword(hashedPwd);

		// 產生5碼驗證碼
		String verifyCode = generateVerifyCode();
		member.setOriginEmailCode(verifyCode);
		member.setVerification(false);

		// 註冊時給「一般會員」的MemberType
		if (member.getMemberType() == null) {
			MemberType defaultType = memberTypeRepository.findByTypeName("一般會員");
			member.setMemberType(defaultType);
		}

		Member savedMember = memberRepository.save(member);

		// 寄送驗證信
		sendVerificationEmail(member.getEmail(), verifyCode);

		return savedMember;
	}

	// 發送驗證信
	public void sendVerificationEmail(String toEmail, String verifyCode) throws MessagingException {
		String subject = "會員註冊驗證信 - 光影之門";
		String verifyLink = "http://localhost:8080/member/verify?code=" + verifyCode;
		String content = getVerificationEmailContent(verifyLink);

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
		helper.setTo(toEmail);
		helper.setSubject(subject);
		helper.setText(content, true);

		mailSender.send(message);
	}

	// 產生五碼驗證碼
	public String generateVerifyCode() {
		return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 5).toUpperCase();
	}

	// 美化版驗證信內容
	private String getVerificationEmailContent(String verifyLink) {
		return """
				<!DOCTYPE html>
				<html>
				<head>
				  <meta charset='UTF-8'>
				  <title>帳號驗證信</title>
				</head>
				<body style='font-family: Arial, sans-serif; background-color: #f2f2f2; padding: 20px;'>
				  <div style='background-color: #ffffff; padding: 20px; border-radius: 5px;'>
				    <h2>親愛的會員您好：</h2>
				    <p>感謝您的註冊，請點擊下方按鈕完成帳號啟用：</p>
				    <div style='text-align: center; margin: 30px;'>
				      <a href='%s' style='display: inline-block; padding: 10px 20px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px;'>立即驗證</a>
				    </div>
				    <p style='color: #999;'>如果您未申請註冊，請忽略此信件。</p>
				  </div>
				</body>
				</html>
				"""
				.formatted(verifyLink);
	}

	// 信箱變更驗證
	private String getChangeEmailContent(String verifyLink) {
		return """
				<!DOCTYPE html>
				<html>
				<head>
				  <meta charset='UTF-8'>
				  <title>信箱變更驗證</title>
				</head>
				<body style='font-family: Arial, sans-serif; background-color: #f2f2f2; padding: 20px;'>
				  <div style='background-color: #ffffff; padding: 20px; border-radius: 5px;'>
				    <h2>親愛的會員您好：</h2>
				    <p>您已申請變更註冊信箱，請點擊下方按鈕完成信箱驗證：</p>
				    <div style='text-align: center; margin: 30px;'>
				      <a href='%s' style='display: inline-block; padding: 10px 20px; background-color: #ffc107; color: white; text-decoration: none; border-radius: 5px;'>驗證新信箱</a>
				    </div>
				    <p style='color: #999;'>如果您未申請變更，請忽略此信件。</p>
				  </div>
				</body>
				</html>
				"""
				.formatted(verifyLink);
	}

	// 重設密碼信 HTML 內容
	private String getResetPasswordEmailContent(String resetLink) {
		return """
				<!DOCTYPE html>
				<html>
				<head>
				  <meta charset='UTF-8'>
				  <title>重設密碼通知</title>
				</head>
				<body style='font-family: Arial, sans-serif; background-color: #f2f2f2; padding: 20px;'>
				  <div style='background-color: #ffffff; padding: 20px; border-radius: 5px;'>
				    <h2>親愛的會員您好：</h2>
				    <p>我們收到您重設密碼的請求，請點擊下方按鈕設定新的密碼：</p>
				    <div style='text-align: center; margin: 30px;'>
				      <a href='%s' style='display: inline-block; padding: 10px 20px; background-color: #28a745; color: white; text-decoration: none; border-radius: 5px;'>重設密碼</a>
				    </div>
				    <p style='color: #999;'>如果您沒有申請重設密碼，請忽略此信件。</p>
				  </div>
				</body>
				</html>
				"""
				.formatted(resetLink);
	}

	// 會員登入
	public Member login(String email, String password) {
		Member member = memberRepository.findByEmail(email);

		if (member == null) {
			// 找不到這個email
			return null;
		}

		// 比對密碼
		boolean passwordMatch = pwdEncoder.matches(password, member.getPassword());
		if (!passwordMatch) {
			return null;
		}

		// 找到了且密碼正確，回傳member
		return member;
	}

	// 會員工具

	// 檢查會員是否存在
	public boolean memberExistCheck(Member member) {
		String email = member.getEmail();
		String phoneNumber = member.getPhoneNumber();

		System.out.println(email + " " + phoneNumber);
		// 檢查信箱&手機號碼是否已註冊
		List<Member> existMembers = memberRepository.findByEmailOrPhoneNumber(email, phoneNumber);
		if (!existMembers.isEmpty()) {
			// 已存在回傳false 不給註冊
			return false;
		} else {
			// 未存在回傳true 表示允許註冊
			return true;
		}

	}

	@Transactional
	public String verifyMember(String code) {
		Member member = memberRepository.findByOriginEmailCode(code);

		if (member == null) {
			return "error"; // 找不到這個驗證碼
		}

		if (member.isVerification()) {
			return "already_verified"; // 已經啟用了
		}

		// 激活會員
		member.setVerification(true);
		member.setOriginEmailCode(null); // 成功後清掉驗證碼避免重複使用
		memberRepository.save(member);

		return "success";
	}

	public Member findByEmail(String email) {
		return memberRepository.findByEmail(email);
	}

//	密碼變更
	@Transactional
	public void sendResetPasswordEmail(String email) throws MessagingException {
		Member member = memberRepository.findByEmail(email);
		if (member == null) {
			throw new RuntimeException("查無此信箱");
		}

		String resetCode = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 5).toUpperCase();

		member.setResetPasswordCode(resetCode);
		memberRepository.save(member);

		String resetLink = "http://localhost:8080/member/resetPassword?email=" + member.getEmail() + "&code="
				+ resetCode;

		sendResetEmail(member.getEmail(), resetLink);
	}

	private void sendResetEmail(String toEmail, String link) throws MessagingException {
		String subject = "重設密碼 - 光影之門";
		String content = """
				親愛的會員您好：<br>
				請點選以下連結重設您的密碼：<br>
				<a href='%s'>點我重設密碼</a><br><br>
				如果您沒有提出此要求，請忽略本郵件。
				""".formatted(link);

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
		helper.setTo(toEmail);
		helper.setSubject(subject);
		helper.setText(content, true);

		mailSender.send(message);
	}

	@Transactional
	public void resetPassword(String newPassword, String email, String code) {
		Member member = memberRepository.findByEmailAndResetPasswordCode(email, code);
		if (member == null) {
			throw new RuntimeException("連結已失效或無效");
		}

		member.setPassword(pwdEncoder.encode(newPassword)); // 建議加密，例如使用 BCrypt
		member.setResetPasswordCode(null); // 清除使用過的 reset code
		memberRepository.save(member);
	}

	/**
	 * 要求變更信箱：寄出驗證信
	 */
	@Transactional
	public void requestChangeEmail(Long memberId, String newEmail) throws MessagingException {
		Member member = memberRepository.findById(memberId).orElse(null);
		if (member == null) {
			throw new RuntimeException("會員不存在！");
		}

		String changeEmailCode = UUID.randomUUID().toString().replace("-", "").substring(0, 5).toUpperCase();
		member.setNewEmail(newEmail);
		member.setNewEmailCode(changeEmailCode);
		memberRepository.save(member);

		String verifyLink = "http://localhost:8080/member/confirmNewEmail?code=" + changeEmailCode;
		mailService.sendHtmlEmail(newEmail, "變更信箱驗證 - 光影之門", buildChangeEmailContent(verifyLink));
	}

	/**
	 * 確認變更信箱
	 */
	@Transactional
	public String confirmChangeEmail(String code) {
		Member member = memberRepository.findByNewEmailCode(code);
		if (member == null) {
			return "驗證失敗或連結已過期！";
		}

		member.setEmail(member.getNewEmail());
		member.setNewEmail(null);
		member.setNewEmailCode(null);
		memberRepository.save(member);
		return "信箱變更成功！";
	}

	private String buildChangeEmailContent(String link) {
		return """
				親愛的會員您好，<br>
				您申請了變更電子郵件地址，請點選以下連結完成驗證：<br>
				<a href="%s">點此驗證新信箱</a><br>
				若不是您本人操作，請忽略本信件。
				""".formatted(link);
	}

	// 前台更新會員資料
    @Transactional
    public void updateMemberProfile(Long memberId, String name, String dateOfBirth, String gender,String nationalId) {
        Member member = findById(memberId);
        member.setName(name);
        member.setDateOfBirth(dateOfBirth);
        member.setGender(gender);
        member.setNationalId(nationalId);

        memberRepository.save(member);
    }
    
    
    //後台更新會員資料
    @Transactional
    public void updateMemberByEmp(Long memberId, String name, String gender, String email, String phoneNumber, String dateOfBirth) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("查無此會員"));
        member.setName(name);
        member.setGender(gender);
        member.setEmail(email);
        member.setPhoneNumber(phoneNumber);
        member.setDateOfBirth(dateOfBirth);
        memberRepository.save(member);
    }
    
    public MemberType findDefaultMemberType() {
        // 你可以這樣設計，例如找 typeName = '普通會員'
        return memberTypeRepository.findByTypeName("一般會員")
            .orElseThrow(() -> new RuntimeException("找不到預設會員身份"));
    }
}
