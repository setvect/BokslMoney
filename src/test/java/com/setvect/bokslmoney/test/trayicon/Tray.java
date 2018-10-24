package com.setvect.bokslmoney.test.trayicon;

import java.awt.Toolkit;

import com.setvect.bokslmoney.util.TrayIconHandler;

public class Tray {
	public static void main(String[] args) {
		TrayIconHandler.registerTrayIcon(
				Toolkit.getDefaultToolkit().getImage("src/main/resources/icon/paw-solid-32.png"), "Example",
				() -> System.exit(0), null);
	}

}
