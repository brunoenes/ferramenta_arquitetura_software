package br.com.artigo.model;

import java.util.ArrayList;

public class InfoPacote {
	
	
	private String nomePacote;
	
	private InfoClasse classePadrao;
	
	private ArrayList<InfoClasse> classesPacote;
	
	public InfoPacote() {
		this.classesPacote = new ArrayList<>();
	}

	public String getNomePacote() {
		return nomePacote;
	}

	public void setNomePacote(String nomePacote) {
		this.nomePacote = nomePacote;
	}

	public ArrayList<InfoClasse> getClassesPacote() {
		return classesPacote;
	}

	public void setClassesPacote(ArrayList<InfoClasse> classesPacote) {
		this.classesPacote = classesPacote;
	}

	public InfoClasse getClassePadrao() {
		return classePadrao;
	}

	public void setClassePadrao(InfoClasse classePadrao) {
		this.classePadrao = classePadrao;
	}

}
