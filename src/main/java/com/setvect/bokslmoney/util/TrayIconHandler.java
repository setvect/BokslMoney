package com.setvect.bokslmoney.util;

import java.awt.AWTException;
import java.awt.Frame;
import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.setvect.bokslmoney.ActionEvent;

import lombok.extern.log4j.Log4j2;

/**
 * 트레이 아이콘 표시
 */
@Log4j2
public abstract class TrayIconHandler {
	/** 트레이 아이콘 */
	private static TrayIcon trayIcon;

	/**
	 * 트레이 아이콘
	 *
	 * @param image
	 *            트래이 아이콘
	 * @param toolTip
	 *            트래이 아이콘 tooltip
	 * @param action
	 *            트래이 아이콘 클릭시 발생하는 이벤트
	 * @param popup
	 *            트레이 아이콘 오른쪽 마우스 클릭 시 표시되는 팝업 메뉴
	 */
	public static void registerTrayIcon(final Image image, final String toolTip, final ActionEvent action,
			final PopupMenu popup) {
		if (SystemTray.isSupported()) {
			final Frame frame = new Frame("");
			frame.setUndecorated(true);

			if (trayIcon != null) {
				trayIcon = null;
			}
			trayIcon = new TrayIcon(image);
			trayIcon.setImageAutoSize(true);

			if (toolTip != null) {
				trayIcon.setToolTip(toolTip);
			}

			trayIcon.setPopupMenu(popup);

			trayIcon.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(final MouseEvent e) {
					if (e.getButton() == MouseEvent.BUTTON1) {
						action.action();
					}
				}
			});

			try {
				for (TrayIcon registeredTrayIcon : SystemTray.getSystemTray().getTrayIcons()) {
					SystemTray.getSystemTray().remove(registeredTrayIcon);
				}
				SystemTray.getSystemTray().add(trayIcon);
			} catch (AWTException e) {
				log.error("I got catch an error during add system tray !", e);
			}
		} else {
			log.warn("System tray is not supported !");
		}
	}

}
