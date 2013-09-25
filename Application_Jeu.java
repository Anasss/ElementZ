/*
 * Copyright (c) 2009 - Ahcene Bounceur
 */

package org.elementz;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

public class Application_Jeu {
	public static void main(String[] args) {
		JFrame jf = new JFrame("ElementZ");
		jf.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		MyPanel jp = new MyPanel();
		jf.getContentPane().add(jp);
		jf.setSize(410, 440);
		jf.setVisible(true);
	}
}
