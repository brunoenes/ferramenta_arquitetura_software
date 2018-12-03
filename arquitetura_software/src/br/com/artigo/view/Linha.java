package br.com.artigo.view;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class Linha extends JPanel {
	

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3949625464125157510L;

	public Linha() {
		
	}
	
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawLine(0,5, 300, 5);
	}
}
