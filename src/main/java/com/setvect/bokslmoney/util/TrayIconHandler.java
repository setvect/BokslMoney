package com.setvect.bokslmoney.util;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class TrayIconHandler {
	private static TrayIcon trayIcon;

	public static void registerTrayIcon(Image image, String toolTip, ActionListener action) {
		if (SystemTray.isSupported()) {
			if (trayIcon != null) {
				trayIcon = null;
			}
			trayIcon = new TrayIcon(image);
			trayIcon.setImageAutoSize(true);

			if (toolTip != null) {
				trayIcon.setToolTip(toolTip);
			}

			if (action != null) {
				trayIcon.addActionListener(action);
			}

			try {
				for (TrayIcon registeredTrayIcon : SystemTray.getSystemTray().getTrayIcons()) {
					SystemTray.getSystemTray().remove(registeredTrayIcon);
				}
				SystemTray.getSystemTray().add(trayIcon);
			} catch (AWTException e) {
				log.error("I got catch an error during add system tray !", e);
			}
		} else {
			log.error("System tray is not supported !");
		}
	}
}
