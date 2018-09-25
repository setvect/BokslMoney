package com.setvect.bokslmoney.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import com.setvect.bokslmoney.BokslMoneyConstant;

/**
 * 보안 관련(spring security) 설정.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	/** 사용자 정보 관리. */
	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	@Autowired
	public void configure(final AuthenticationManagerBuilder auth) throws Exception {
		PasswordEncoder passwordEncoder = passwordEncoder();
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}

	@Override
	public void configure(final WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/assets/**").antMatchers("/html/**").antMatchers("/h2-console/**")
				.antMatchers("/error/**");
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.authorizeRequests()//
				.antMatchers("/**").access("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")//
				// 아래 코드 넣으면 로그인 시 에러 남. 그 이후에 에러 안남.
				// .anyRequest().authenticated()//
				.and().formLogin().loginPage("/login.do").permitAll().failureUrl("/login.do?error")//
				.and().logout().logoutUrl("/logout.do").permitAll().logoutSuccessUrl("/login.do?logout")//
				.and().csrf()//
				.and().exceptionHandling().accessDeniedPage("/403");

		http.csrf().disable();
		http.headers().frameOptions().disable();
		http.rememberMe().key(BokslMoneyConstant.Login.REMEMBER_ME_KEY)
				.rememberMeServices(tokenBasedRememberMeServices());
	}

	/**
	 * @return rememberMe 토근 생성 방식
	 */
	@Bean
	public TokenBasedRememberMeServices tokenBasedRememberMeServices() {
		TokenBasedRememberMeServices tokenBasedRememberMeServices = new TokenBasedRememberMeServices(
				BokslMoneyConstant.Login.REMEMBER_ME_KEY, userDetailsService);
		tokenBasedRememberMeServices.setCookieName(BokslMoneyConstant.Login.REMEMBER_COOKIE_NAME);
		return tokenBasedRememberMeServices;
	}

	/**
	 * @return 패스워드 암호화
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}

}