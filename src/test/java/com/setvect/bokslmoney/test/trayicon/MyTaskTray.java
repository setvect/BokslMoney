package com.setvect.bokslmoney.test.trayicon;

import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;

public class MyTaskTray {
	public static void main(String arg[]) throws MalformedURLException {
		final Frame frame = new Frame("");
		frame.setUndecorated(true);
		// Check the SystemTray is supported
		if (!SystemTray.isSupported()) {
			System.out.println("SystemTray is not supported");
			return;
		}
		final TrayIcon trayIcon = new TrayIcon(
				Toolkit.getDefaultToolkit().getImage("src/main/resources/icon/paw-solid-32.png"), "Library Drop");
		final SystemTray tray = SystemTray.getSystemTray();

		// Create a pop-up menu components
		final PopupMenu popup = createPopupMenu();
		trayIcon.setPopupMenu(popup);
		trayIcon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					frame.add(popup);
					popup.show(frame, e.getXOnScreen(), e.getYOnScreen());
				}
			}
		});
		try {
			frame.setResizable(false);
			frame.setVisible(true);
			tray.add(trayIcon);
		} catch (AWTException e) {
			System.out.println("TrayIcon could not be added.");
		}

	}

	protected static PopupMenu createPopupMenu() {
		final PopupMenu popup = new PopupMenu();
		MenuItem aboutItem = new MenuItem("About");
		CheckboxMenuItem cb1 = new CheckboxMenuItem("Set auto size");
		CheckboxMenuItem cb2 = new CheckboxMenuItem("Set tooltip");
		Menu displayMenu = new Menu("Display");
		MenuItem errorItem = new MenuItem("Error");
		MenuItem warningItem = new MenuItem("Warning");
		MenuItem infoItem = new MenuItem("Info");
		MenuItem noneItem = new MenuItem("None");
		MenuItem exitItem = new MenuItem("Exit");
		// Add components to pop-up menu
		popup.add(aboutItem);
		popup.addSeparator();
		popup.add(cb1);
		popup.add(cb2);
		popup.addSeparator();
		popup.add(displayMenu);
		displayMenu.add(errorItem);
		displayMenu.add(warningItem);
		displayMenu.add(infoItem);
		displayMenu.add(noneItem);
		popup.add(exitItem);
		return popup;
	}
}