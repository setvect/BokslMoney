package com.setvect.bokslmoney;

import java.awt.Desktop;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import com.setvect.bokslmoney.code.service.CodeService;
import com.setvect.bokslmoney.user.service.UserService;
import com.setvect.bokslmoney.util.BeanUtils;
import com.setvect.bokslmoney.util.TrayIconHandler;
import lombok.extern.log4j.Log4j2;
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

/**
 * Spring boot application 시작점.
 */
@SpringBootApplication
@ImportResource({"classpath:/spring/context-transaction.xml"})
@EnableScheduling
@EnableAspectJAutoProxy
@Log4j2
public class BokslMoneyApplication extends SpringBootServletInitializer {
	/**
	 * tomcat-embed-core-8.5.34.jar
	 * 중복 방지를 위해 임의로 bind할 포드
	 */
	private static final int PREVENT_DUPLICATION = 11228;
	/**
	 * 설정 파일 경로.
	 */
	private static final String CONFIG_PROPERTIES = "/application.properties";
	/**
	 * 테스트 설정 파일 경로
	 */
	private static final String CONFIG_PROPERTIES_TEST = "/test.properties";

	/**
	 * 초기화 완료 여부
	 */
	private static boolean initComplete = false;

	/**
	 * 프로그램 실행 여부 체크 용도(중복 실행 방지)
	 */
	@SuppressWarnings("unused")
	private static DatagramSocket runCheck;

	/**
	 * Application 시작점.
	 *
	 * @param args 사용 안함
	 */
	public static void main(final String[] args) {
		// (new File("./db/BokslMoney_db_bak.mv.db")).delete();
		checkDuplicateExecute();
		applyTray();

		// spring boot에서 클래스가 및 properties 변경되었을 때 restart 안되게 함.
		// 즉 reload 효과
		System.setProperty("spring.devtools.restart.enabled", "false");
		SpringApplication.run(BokslMoneyApplication.class, args);

		initComplete = true;

		if (BokslMoneyConstant.OPEN_WEB) {
			openWeb();
		}
	}

	/**
	 * 중복 실행 여부 체크. 중복실행이 된 경우 지금 실행한 프로세스를 종료함.
	 */
	private static void checkDuplicateExecute() {
		try {
			System.out.println();
			runCheck = new DatagramSocket(PREVENT_DUPLICATION);
		} catch (SocketException e) {
			System.err.println("프로그램이 중복 실행되었습니다.");
			// TODO 아래 방법을에서 설정 파일에서 포트 정보를 얻을 수 있는 방법이 있을까?
			try {
				Desktop.getDesktop().browse(new URI("http://127.0.0.1:8080"));
			} catch (IOException | URISyntaxException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * 윈도우 트레이 아이콘 적용
	 */
	private static void applyTray() {
		// 팝업 항목
		final PopupMenu popup = new PopupMenu();
		MenuItem openItem = new MenuItem("Open");
		openItem.addActionListener((e) -> openWeb());
		MenuItem exitItem = new MenuItem("Exit");
		exitItem.addActionListener((e) -> System.exit(1));
		popup.add(openItem);
		popup.add(exitItem);

		URL img = BokslMoneyApplication.class.getResource("/icon/paw-solid-32.png");
		TrayIconHandler.registerTrayIcon(Toolkit.getDefaultToolkit().getImage(img), "복슬머니", () -> {
			openWeb();
		}, popup);
	}

	/**
	 * 웹 페이지 오픈
	 */
	private static void openWeb() {
		if (!initComplete) {
			log.info("Not initialized.");
			return;
		}

		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(new URI("http://127.0.0.1:" + BokslMoneyConstant.WEB_PORT));
			} catch (IOException | URISyntaxException e) {
				log.error(e);
			}
		}
	}

	@Override
	protected final SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
		return application.sources(BokslMoneyApplication.class);
	}

	/**
	 * Spring scope가 아닌 곳에서 Spring Bean 객체를 접근하기 위해 사용.
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
			UserService userService = BeanUtils.getBean(UserService.class);
			userService.init();
			CodeService codeService = BeanUtils.getBean(CodeService.class);
			codeService.init();

//			MakerSampleDataService sampleMaker = BeanUtils.getBean(MakerSampleDataService.class);
//			sampleMaker.makeSampleData();
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
