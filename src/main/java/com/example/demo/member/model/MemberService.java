package com.example.demo.member.model;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Service
public class MemberService {
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private PasswordEncoder pwdEncoder;
	
	@Autowired
    private JavaMailSender mailSender;
	
	@Transactional
	public Member updateMember(Long id,Member updatedMember) {
		Optional<Member> optional = memberRepository.findById(id);
		if(optional.isPresent()) {
			Member originalMember = optional.get();					
			String newName  = updatedMember.getName();
			String newDateOfBirth  = updatedMember.getDateOfBirth();
			String newGender  = updatedMember.getGender();
		
		//更新資料
		originalMember.setName(newName);
		originalMember.setGender(newGender);
		originalMember.setDateOfBirth(newDateOfBirth);
		//更新資料 (密碼)
		if(updatedMember.getPassword() != null && !updatedMember.getPassword().isBlank()) {
			String newPassword  = updatedMember.getPassword();
			String hashedPwd = pwdEncoder.encode(newPassword);
			originalMember.setPassword(hashedPwd);
		}
			return originalMember;
		}else {
			return null;
		}
	}
	
	
	
	@Transactional
	public Member updateMemberByEmp(Long id, Member updatedMember) {
	    Optional<Member> optional = memberRepository.findById(id);
	    if (optional.isPresent()) {
	        Member originalMember = optional.get();
	        originalMember.setName(updatedMember.getName());
	        originalMember.setEmail(updatedMember.getEmail());
	        originalMember.setPhoneNumber(updatedMember.getPhoneNumber());
	        originalMember.setDateOfBirth(updatedMember.getDateOfBirth());
	        originalMember.setGender(updatedMember.getGender());
	        if (updatedMember.getPassword() != null && !updatedMember.getPassword().isBlank()) {
	            String hashedPwd = pwdEncoder.encode(updatedMember.getPassword());
	            originalMember.setPassword(hashedPwd);
	        }

	        return originalMember;
	    } else {
	        return null;
	    }
	}

	
	
	public Member insertMember(Member member,String verifyCode) {
		String originalPwd =  member.getPassword();
		String hashedPwd = pwdEncoder.encode(originalPwd);
		member.setPassword(hashedPwd);
		member.setVerification(false);
		member.setVerificationCode(verifyCode);
		Member insertBean = memberRepository.save(member);
		
		
		
		
		return insertBean;
	}
	
	public boolean memberExistCheck(Member member) {
		String email = member.getEmail();
		String phoneNumber = member.getPhoneNumber();
		
		System.out.println(email + " "+phoneNumber);
		//檢查信箱&手機號碼是否已註冊
		List<Member> existMembers = memberRepository.findByEmailOrPhoneNumber(email,phoneNumber);
		if(!existMembers.isEmpty()) {
			//已存在回傳false 不給註冊
			return false;
		}else {
			//未存在回傳true 表示允許註冊
			return true;
		}
		
	}
	
	public Member login(String email,String originalPwd) {
		Member member = memberRepository.findByEmail(email);
		if(member!=null) {
			boolean result = pwdEncoder.matches(originalPwd, member.getPassword());
		
			if(result) {
				return member;
			}
		}
		return null;

	}
	public List<Member> findByPhoneNumberContaining(String phoneNumber) {
		List<Member> members = memberRepository.findByPhoneNumberContaining(phoneNumber);
		if (members!=null) {
			return members;
		}
		return null;
		
	}
	
	public List<Member> findByEmailContaining(String email) {
		List<Member> members = memberRepository.findByEmailContaining(email);
		if (members!=null) {
			return members;
		}
		return null;
		
	}
	
	public List<Member> findByNationalIdContaining(String nationalId) {
		List<Member> members = memberRepository.findByNationalIdContaining(nationalId);
		if (members!=null) {
			return members;
		}
		return null;
		
	}
	
	public Member findById(Long id) {
		Optional<Member> optional = memberRepository.findById(id);
		if (optional.isPresent()) {
			Member member = optional.get();
			return member;
		}
		return null;
		
	}
	
	public List<Member> findAll() {
		List<Member> members = memberRepository.findAll();
		if (!members.isEmpty()) {
			return members;
		}
		return null;
		
	}

	@Transactional
	public boolean deleteMemberById(Long id) {
		 if (memberRepository.existsById(id)) {
				memberRepository.deleteById(id);
				return true;
		    } else {
		        return false;
		    }
	}
	
	public void sendVerificationEmail(String Toemail,String verifyLink)throws MessagingException {
		 String subject = "會員註冊驗證信 - 光影之門";
		    String content = """
		        親愛的會員您好：<br>
		        請點選以下連結完成帳號啟用：<br>
		        <a href="%s">點我驗證</a>
		        <br><br>
		        如果您未註冊，請忽略這封信。
		        """.formatted(verifyLink);
		    MimeMessage message = mailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
	        helper.setTo(Toemail);
	        helper.setSubject(subject);
	        helper.setText(content, true); // 第二個參數 true 表示啟用 HTML
	        mailSender.send(message);
	        System.out.println("送出成功"+message);
	}
	
	@Transactional
	public boolean verifyMessage(String code) {
	    Member member = memberRepository.findByVerificationCode(code);
	    if (member == null || member.isVerification()) {
	        return false;
	    }
	    member.setVerification(true);
	    member.setVerificationCode(null); // 清除驗證碼
	    memberRepository.save(member);
	    return true;
	}
	@Transactional
	public void EmailChange(Member member,String newEmail) throws MessagingException {
		String token = UUID.randomUUID().toString().replace("-", "");
	    member.setNewEmail(newEmail);
	    member.setVerificationCode(token);	    
	    memberRepository.save(member);
	    
	    
	    
	    String verifyLink = "http://localhost:8080/member/email/confirm?code=" + token;
	    String subject = "會員變更信箱 - 光影之門";
	    String content = """
	        親愛的會員您好：<br>
	        請點選以下連結完成新信箱：<br>
	        <a href="%s">點我驗證</a>
	        """.formatted(verifyLink);
	    MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(member.getEmail());
        helper.setSubject(subject);
        helper.setText(content, true); // 第二個參數 true 表示啟用 HTML
	    
	}
	@Transactional
	public boolean newEmailConfirm(Model model,String code) {
		 Member member = memberRepository.findByVerificationCode(code);
		    if (member == null || member.getNewEmail() == null) {
		        return false;
		    }else {
		    	// 執行變更信箱
		    	member.setEmail(member.getNewEmail());
		    	member.setNewEmail(null);
		    	member.setVerificationCode(null);
		    	memberRepository.save(member);
		    	return true;
		    }
	}
	
	@Transactional
	public void registerMemberByEmp(Member member) {
	    member.setPassword(pwdEncoder.encode(member.getPassword()));
	    memberRepository.save(member);
	}
	
	public Member findByIdByEmp(Long id) {
	    return memberRepository.findById(id).orElseThrow(() -> new RuntimeException("找不到會員"));
	}
	
	
	@Transactional
	public void updateMemberByEmp(Member updatedMember) {
	    Member original = memberRepository.findById(updatedMember.getMemberId())
	            .orElseThrow(() -> new RuntimeException("找不到會員"));

	    // 欄位逐一更新（可選擇只更新允許項目）
	    original.setName(updatedMember.getName());
	    original.setEmail(updatedMember.getEmail());
	    original.setPhoneNumber(updatedMember.getPhoneNumber());
	    original.setGender(updatedMember.getGender());
	    original.setDateOfBirth(updatedMember.getDateOfBirth());

	    memberRepository.save(original);
	}
	
	
//	管理員手動寄送驗證碼
	public void sendVerificationEmailById(Long memberId) throws MessagingException {
	    Member member = memberRepository.findById(memberId)
	        .orElseThrow(() -> new RuntimeException("找不到會員"));

	    String code = UUID.randomUUID().toString().replace("-", "");
	    member.setVerificationCode(code);
	    memberRepository.save(member);

	    String verifyLink = "http://localhost:8080/member/verify?code=" + code;
	    sendVerificationEmail(member.getEmail(), verifyLink);
	}
	
//	後台欄位搜尋會員
	public List<Member> searchByName(String keyword) {
	    return memberRepository.findByNameContainingIgnoreCase(keyword);
	}

	public List<Member> searchByEmail(String keyword) {
	    return memberRepository.findByEmailContainingIgnoreCase(keyword);
	}

	public List<Member> searchByPhone(String keyword) {
	    return memberRepository.findByPhoneNumberContaining(keyword);
	}

}
