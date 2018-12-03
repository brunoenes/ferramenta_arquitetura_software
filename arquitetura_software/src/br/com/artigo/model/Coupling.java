package br.com.artigo.model;

import java.text.DecimalFormat;

public class Coupling {
	
	private String nomePacote;
	
	private int qtdClassePacote;
	
	private int valor;
	
	private String porcentagem;

	public Coupling() {
		
	}

	public String getNomePacote() {
		return nomePacote;
	}

	public void setNomePacote(String nomePacote) {
		this.nomePacote = nomePacote;
	}

	public int getQtdClassePacote() {
		return qtdClassePacote;
	}

	public void setQtdClassePacote(int qtdClassePacote) {
		this.qtdClassePacote = qtdClassePacote;
	}

	public int getValor() {
		return valor;
	}

	public void setValor(int valor) {
		this.valor = valor;
		this.setPorcentagem(valor);
	}

	public String getPorcentagem() {
		return porcentagem;
	}

	public void setPorcentagem(int valor) {
		DecimalFormat df = new DecimalFormat("0.00");
		this.porcentagem =  df.format(( (100 * valor) / qtdClassePacote ));
	}

}
