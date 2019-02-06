package com.setvect.bokslmoney.test.common.controller;

import com.setvect.bokslmoney.test.MainTestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
public class HomeControllerTest extends MainTestBase {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void index() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("http://localhost/login.do"))
				.andDo(print());
	}

	@Test
	public void login() throws Exception {
		mockMvc.perform(get("/login.do"))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("/WEB-INF/views/login.jsp"))
				.andDo(print());
	}
}