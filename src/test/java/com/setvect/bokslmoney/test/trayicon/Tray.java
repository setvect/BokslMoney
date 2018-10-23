package com.setvect.bokslmoney.test.trayicon;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.setvect.bokslmoney.util.TrayIconHandler;

public class Tray {
	public static void main(String[] args) {
		TrayIconHandler.registerTrayIcon(
				Toolkit.getDefaultToolkit().getImage("src/main/resources/icon/paw-solid-32.png"), "Example",
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.exit(0);
					}
				});
	}

}
