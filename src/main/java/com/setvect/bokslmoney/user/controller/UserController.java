package com.setvect.bokslmoney.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.setvect.bokslmoney.ApplicationUtil;
import com.setvect.bokslmoney.user.repository.UserRepository;
import com.setvect.bokslmoney.user.vo.UserVo;

@Controller
@RequestMapping(value = "/user")
public class UserController {
	/** 로깅 */
	private static Logger logger = LoggerFactory.getLogger(UserController.class);
	/** 암호화 인코더. */
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	// ============== 뷰==============

	/**
	 * @param request
	 *            servlet
	 * @return view 페이지
	 */
	@RequestMapping(value = "/loginUserEdit.do")
	public String page(final HttpServletRequest request) {
		return "user/login_user_edit";
	}

	// ============== 수정 ==============
	/**
	 * 패스워드 변경
	 *
	 * @param session
	 *            ..
	 * @param password
	 *            변경할 비밀번호
	 */
	@RequestMapping(value = "/edit.do")
	@ResponseBody
	public void edit(final HttpSession session, @RequestParam("password") final String password) {
		String loginId = ApplicationUtil.getLoginUser(session).getUserId();
		UserVo edit = userRepository.findById(loginId).get();
		edit.setPassword(passwordEncoder.encode(password));
		userRepository.save(edit);
	}

}
