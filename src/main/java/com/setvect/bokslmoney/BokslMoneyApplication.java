package com.setvect.bokslmoney;

import java.net.URL;

import org.apache.catalina.Container;
import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.setvect.bokslmoney.code.service.CodeService;
import com.setvect.bokslmoney.util.BeanUtils;

/**
 * Spring boot application 시작점.
 */
@SpringBootApplication
@ImportResource({ "classpath:/spring/context-transaction.xml" })
@EnableScheduling
@EnableAspectJAutoProxy
public class BokslMoneyApplication extends SpringBootServletInitializer {
	/** 설정 파일 경로. */
	private static final String CONFIG_PROPERTIES = "/application.properties";
	/** 테스트 설정 파일 경로 */
	private static final String CONFIG_PROPERTIES_TEST = "/test.properties";

	/**
	 * Application 시작점.
	 *
	 * @param args
	 *            사용 안함
	 */
	public static void main(final String[] args) {
		// spring boot에서 클래스가 및 properties 변경되었을 때 restart 안되게 함.
		// 즉 reload 효과
		System.setProperty("spring.devtools.restart.enabled", "false");
		SpringApplication.run(BokslMoneyApplication.class, args);
	}

	@Override
	protected final SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
		return application.sources(BokslMoneyApplication.class);
	}

	/**
	 * Spring scope가 아닌 곳에서 Spring Bean 객체를 접근하기 위해 사용.
	 *
	 *
	 * @return ApplicationContextProvider
	 */
	@Bean
	ApplicationContextProvider getApplicationContext() {
		return new ApplicationContextProvider();
	}

	/**
	 * 서비스 시작점 초기화.
	 *
	 * @return Spring boot 시작 bean
	 */
	@Bean
	InitializingBean init() {
		return () -> {
			String testEnv = System.getProperty(BokslMoneyConstant.TEST_CHECK_PROPERTY_NAME);
			URL configUrl;
			if (Boolean.parseBoolean(testEnv)) {
				configUrl = BokslMoneyApplication.class.getResource(CONFIG_PROPERTIES_TEST);
			} else {
				configUrl = BokslMoneyApplication.class.getResource(CONFIG_PROPERTIES);
			}
			EnvirmentProperty.init(configUrl);
			CodeService codeService = BeanUtils.getBean(CodeService.class);
			codeService.init();
		};
	}

	/**
	 * spring application 형태로 실행 했을 때 톰겟 설정.<br>
	 * 독립적인 WAS 환경에서 실행 했을 본 메소드는 의미 없음.
	 *
	 * @return .
	 */
	@Bean
	WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> containerCustomizer() {
		return (ConfigurableServletWebServerFactory container) -> {
			if (container instanceof TomcatServletWebServerFactory) {
				TomcatServletWebServerFactory tomcat = (TomcatServletWebServerFactory) container;
				tomcat.addConnectorCustomizers((connector) -> {
					// post 데이터 최대 업로드
					connector.setMaxPostSize(1024 * 1024 * 10); // 10 MB
				});

				tomcat.addContextCustomizers((Context context) -> {
					Container jsp = context.findChild("jsp");
					if (jsp instanceof Wrapper) {
						// true면 변경 체크함, false 안함.
						((Wrapper) jsp).addInitParameter("development", "true");
					}
				});
			}
		};
	}
}
