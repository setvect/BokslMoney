package com.setvect.bokslmoney.test.common.controller;

import com.setvect.bokslmoney.code.repository.CodeMainRepository;
import com.setvect.bokslmoney.code.vo.CodeMainVo;
import com.setvect.bokslmoney.test.MainTestBase;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CodeControllerTest extends MainTestBase {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TestRestTemplate testRestTemplate;

	@MockBean
	CodeMainRepository mockCodeMainRepository;

	@Test
	public void page() throws Exception {
		mockMvc.perform(get("/code/page.do"))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("/WEB-INF/views/login.jsp"))
				.andDo(print());
	}

	@Test
	public void getCodeMain() {
		CodeMainVo codeMainVo = new CodeMainVo();
		codeMainVo.setCodeMainId("main");
		codeMainVo.setName("복슬이");
		Optional<CodeMainVo> value = Optional.of(codeMainVo);
		when(mockCodeMainRepository.findById("1")).thenReturn(value);

//		Object a = testRestTemplate.getForObject("/code/getCodeMain.json?codeMainId=1", Object.class);
//		System.out.println("=========================");
//		System.out.println(a);
//		assertThat(a.getName()).isEqualTo("복슬이");
	}
}