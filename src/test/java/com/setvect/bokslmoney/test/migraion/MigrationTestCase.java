package com.setvect.bokslmoney.test.migraion;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.setvect.bokslmoney.migration.service.MigrationService;
import com.setvect.bokslmoney.test.MainTestBase;

/**
 */
public class MigrationTestCase extends MainTestBase {

	@Autowired
	private MigrationService migrationService;

	@Test
	public void run() {
		// migrationService.run();
		System.out.println("끝...");
	}
}