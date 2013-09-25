/*
 * Copyright (c) 2009 - Ahcene Bounceur
 */
package org.elementz;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class MyPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private ElementZ_Model ezm;
	private Ball[][] matrix = new Ball[8][8];
	private int v = 50;
	private BufferedImage im = null;
	private int size = 40;

	public MyPanel() {
		ezm = new ElementZ_Model();
		initialiser();
		setBackground(new Color(0, 0, 10));
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(im, 0, 0, this);
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				g.setColor(new Color(255, 255, 255, 20));
				g.fillRect(i * 50 - 4, j * 50 - 4, 48, 48);
				g.setColor(new Color(255, 255, 255, 30));
				g.drawRect(i * 50 - 4, j * 50 - 4, 48, 48);
			}
		}
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				matrix[i][j].draw(g);
			}
		}
	}

	public void initialiser() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				matrix[i][j] = new Ball(this, j * v, i * v, size, ezm.getXY(i,
						j) - 1, j, i);
				addMouseMotionListener(matrix[i][j]);
				addMouseListener(matrix[i][j]);
			}
		}
	}

	public void afficher() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				matrix[i][j].setColor(MyColor.tabColor[ezm.getXY(i, j) - 1]);
			}
		}
	}

	public void permut(Ball b1, Ball b2) {
		ezm.permut(b1.getPosY(), b1.getPosX(), b2.getPosY(), b2.getPosX());
		ezm.prepar();
		afficher();
	}
}
