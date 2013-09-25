/*
 * Copyright (c) 2009 - Ahcene Bounceur
 */
package org.elementz;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Ball implements MouseMotionListener, MouseListener {
	// (x,y) : coordonnées graphiques de la zone carrée de MyPanel contenant la boule
	// ces coordonnées sont utiles à l'affichage des boules su MyPanel
	private int x; // abscisse du coin inférieur gauche de la zone
	private int y; // ordonnée du coin inférieur gauche de la zone
	private int size; // taille de la zone (longueur du côté du carré)
	private Color color; // couleur de la boule
	
	// (posX,posY) : coordonnées logiques de la boule
	private int posX; // numéro de ligne
	private int posY; // numéro de colonne
	private MyPanel panel; // objet graphique pour l'affichage du plateau
	private int value; // code qui indique en particulier la valeur de la couleur
	
	private boolean selected = false;
	private boolean highlighted = false;
	private static Ball first = null;

	public Ball(Ball ball) {
		this.panel = ball.getPanel();
		this.x = ball.getX();
		this.y = ball.getY();
		this.size = ball.getSize();
		this.value = ball.getValue();
		this.color = MyColor.tabColor[value];
		this.posX = ball.getPosX();
		this.posY = ball.getPosY();
	}

	public Ball(MyPanel panel, int x, int y, int size, int value, int posX,
			int posY) {
		this.panel = panel;
		this.x = x;
		this.y = y;
		this.size = size;
		this.value = value;
		this.color = MyColor.tabColor[value];
		this.posX = posX;
		this.posY = posY;
	}

	public MyPanel getPanel() {
		return panel;
	}

	public int getValue() {
		return value;
	}

	public int getSize() {
		return size;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

	public Color getColor() {
		return color;
	}

	public void draw(Graphics g) {
		g.setColor(new Color(255, 255, 250, 50));
		g.fillOval(x + 1, y + 1, size, size);
		g.setColor(color);
		g.fillOval(x, y, size, size);
		g.setColor(new Color(255, 255, 255, 50));
		g.fillOval(x + 2, y + 2, size - 4, size - 10);
		g.setColor(new Color(255, 255, 255, 60));
		g.fillOval(x + 8, y + 5, size - 16, size - 30);
		if (highlighted) {
			for (int i = 0; i < 4; i++) {
				g.setColor(new Color(color.getRed(), color.getGreen(), color
						.getBlue(), 255 - (i * 60)));
				g.drawOval(x - i, y - i, size + (2 * i), size + (2 * i));
			}
		}
		if (selected) {
			for (int i = 0; i < 10; i++) {
				g.setColor(new Color(color.getRed(), color.getGreen(), color
						.getBlue(), 255 - (i * 25)));
				g.drawOval(x - i, y - i, size + (2 * i), size + (2 * i));
			}
		}
	}

	public void condSelect() {
		selected = !selected;
		panel.repaint();
	}

	public void select() {
		selected = true;
		panel.repaint();
	}

	public void deselect() {
		selected = false;
		panel.repaint();
	}

	public void highlight() {
		highlighted = true;
		panel.repaint();
	}

	public void deHighlight() {
		highlighted = false;
		panel.repaint();
	}

	public boolean isInside(int x, int y) {
		// Indique si le point de coordonnées graphiques (x,y)
		// est dans la zone correspondant à "this".
		//
		// Rappel : les attributs "x" et "y" de "this" sont les
		// coordonnées graphiques du coin inférieur gauche de
		// la zone correspondant à "this". Cette zone est un
		// carré de côté "size".
		return (x > this.x && y > this.y &&
				x < (this.x + size) && y < (this.y + size));
	}

	public void mouseDragged(MouseEvent e) {

	}

	public void mouseMoved(MouseEvent e) {
		if (isInside(e.getX(), e.getY()) && !highlighted) {
			highlight();
		}
		if (!isInside(e.getX(), e.getY()) && highlighted) {
			deHighlight();
		}
	}

	public void mouseClicked(MouseEvent e) {
		if (isInside(e.getX(), e.getY())) {
			if (first == null) {
				first = this;
				select();
			} else {
				this.deselect();
				first.deselect();
				panel.permut(this, first);
				first = null;
			}
		}
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	@Override
	public String toString() {
		// Fourni les coordonnées graphique (x, y),
		// les coordonnées logiques (posX, posY),
		// la taille (size) et le code de couleur (value)
		StringBuffer label = new StringBuffer();
		label.append("Coordonnées graphiques : ("+x+","+y+")\n");
		label.append("Coordonnées logiques   : ("+posX+","+posY+")\n");
		label.append("Taille et code couleur : "+size+"/"+value);
		return label.toString();
	}
	
	/******************************************************************************/
	
	public static void main(String[] argv) {
		// Fonction main contenant des jeux de tests pour les méthodes à compléter.
		// 	En commentaire : la trace d'exécution attendue.

		MyPanel panel = new MyPanel();
		int x = 50;
		int y = 50;
		int size = 40;
		int colorIndexFrom0To5 = 4;
		int posX = 1;
		int posY = 1;
		
		Ball b1 = new Ball(panel, x, y, size, colorIndexFrom0To5, posX, posY);
		System.out.println(b1);
		/*	Coordonnées graphiques : (50,50)
			Coordonnées logiques   : (1,1)
			Taille et code couleur : 40/4
		*/
		
		System.out.println("Click (55,89) dans b1 ? "+ (b1.isInside(55,89) ? "OUI" : "NON"));
		System.out.println("Click (89,55) dans b1 ? "+ (b1.isInside(89,55) ? "OUI" : "NON"));
		System.out.println("Click (42,60) dans b1 ? "+ (b1.isInside(42,60) ? "OUI" : "NON"));
		System.out.println("Click (60,42) dans b1 ? "+ (b1.isInside(60,42) ? "OUI" : "NON"));
		/*	Click (55,89) dans b1 ? OUI
			Click (89,55) dans b1 ? OUI
			Click (42,60) dans b1 ? NON
			Click (60,42) dans b1 ? NON
		*/

	}
	
}
