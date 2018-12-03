package br.com.artigo.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.text.DecimalFormat;

import javax.swing.JPanel;

public class Circulo extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2227899986999470749L;
	private Color color;
	private String nomeClasse1 = "";
	private String nomeClasse2 = "";
	private float valorSimilaridade = -1;

	public Circulo(Color color) {
		this.color = color;
	}

	public Circulo(Color color, String nomeClasse) {
		this.color = color;
		this.trataNomeClasse(nomeClasse);
	}

	public Circulo(Color color, String nomeClasse, float similaridade) {
		this.color = color;
		this.trataNomeClasse(nomeClasse);
		this.valorSimilaridade = 100 * similaridade;
	}

	private void trataNomeClasse(String nomeClasse) {
		String[] aux = nomeClasse.split(".java");
		String nomeCompleto = aux[0];
		boolean segundoNome = false;

		for (int i = 0; i < nomeCompleto.length(); i++) {
			Character c = nomeCompleto.charAt(i);

			if (!segundoNome) {
				if (i != 0) {
					if (Character.isUpperCase(c)) {
						segundoNome = true;
					}
				}
			}

			if (!segundoNome) {
				nomeClasse1 += c;
			} else {
				nomeClasse2 += c;
			}

		}

	}

	public void paint(Graphics g) {
		g.setColor(this.color);
		g.fillOval(0, 0, 70, 70);

		g.setColor(Color.BLACK);
		Font f = new Font(g.getFont().getFontName(), g.getFont().getStyle(), 11);
		g.setFont(f);
		if (this.nomeClasse1 == "") {
			g.drawString("Produto", 15, 30);
			g.drawString("Controller.java", 6, 40);

		} else {
			DecimalFormat df = new DecimalFormat("0.00");
			g.drawString(this.nomeClasse1, 15, 30);
			if (nomeClasse2 != "")
				g.drawString(this.nomeClasse2, 10, 40);
			if (this.valorSimilaridade != -1)
				g.drawString(df.format(this.valorSimilaridade) + "%", 10, 50);
		}

	}
}
