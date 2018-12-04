package br.com.artigo.model;

import java.util.ArrayList;

public class InfoClasse {

	private String nomeClasse;

	private InfoPacote pacoteAtual;

	private InfoPacote pacoteAlterado;

	private ArrayList<String> tipos;

	private ArrayList<Float> similaridades;

	private float similaridadeClasse;

	private float simNovoPacote;

	private float mSimilPacoteAtual;

	private ArrayList<Coupling> afferentCoupling;

	private ArrayList<Coupling> efferentCoupling;

	private Coupling afferentAtualCl;

	private Coupling afferentAtualizadoCl;

	private Coupling efferentAtualCl;

	private Coupling efferentAtualizadoCl;

	private Coupling afferentAtual;

	private Coupling afferentAtualizado;

	private Coupling efferentAtual;

	private Coupling efferentAtualizado;

	public InfoClasse() {
		this.tipos = new ArrayList<>();
		this.similaridades = new ArrayList<>();
		this.afferentCoupling = new ArrayList<>();
		this.efferentCoupling = new ArrayList<>();
	}

	public String getNomeClasse() {
		return nomeClasse;
	}

	public void setNomeClasse(String nomeClasse) {
		this.nomeClasse = nomeClasse;
	}

	public ArrayList<String> getTipos() {
		return tipos;
	}

	public void setTipos(ArrayList<String> tipos) {
		this.tipos = tipos;
	}

	public ArrayList<Float> getSimilaridades() {
		return similaridades;
	}

	public void setSimilaridades(ArrayList<Float> similaridades) {
		this.similaridades = similaridades;
	}

	public float getSimilaridadeClasse() {
		return similaridadeClasse;
	}

	public void setSimilaridadeClasse(float similaridadeClasse) {
		this.similaridadeClasse = similaridadeClasse;
	}

	public InfoPacote getPacoteAtual() {
		return pacoteAtual;
	}

	public void setPacoteAtual(InfoPacote pacoteAtual) {
		this.pacoteAtual = pacoteAtual;
	}

	public InfoPacote getPacoteAlterado() {
		return pacoteAlterado;
	}

	public void setPacoteAlterado(InfoPacote pacoteAlterado) {
		this.pacoteAlterado = pacoteAlterado;
	}

	public float getmSimilPacoteAtual() {
		return mSimilPacoteAtual;
	}

	public void setmSimilPacoteAtual(float mSimilPacoteAtual) {
		this.mSimilPacoteAtual = mSimilPacoteAtual;
	}

	public float getSimNovoPacote() {
		return simNovoPacote;
	}

	public void setSimNovoPacote(float simNovoPacote) {
		this.simNovoPacote = simNovoPacote;
	}

	public ArrayList<Coupling> getAfferentCoupling() {
		return afferentCoupling;
	}

	public void setAfferentCoupling(ArrayList<Coupling> afferentCoupling) {
		this.afferentCoupling = afferentCoupling;
	}

	public ArrayList<Coupling> getEfferentCoupling() {
		return efferentCoupling;
	}

	public void setEfferentCoupling(ArrayList<Coupling> efferentCoupling) {
		this.efferentCoupling = efferentCoupling;
	}

	public Coupling getAfferentAtualCl() {
		return afferentAtualCl;
	}

	public void setAfferentAtualCl(Coupling afferentAtualCl) {
		this.afferentAtualCl = afferentAtualCl;
	}

	public Coupling getAfferentAtualizadoCl() {
		return afferentAtualizadoCl;
	}

	public void setAfferentAtualizadoCl(Coupling afferentAtualizadoCl) {
		this.afferentAtualizadoCl = afferentAtualizadoCl;
	}

	public Coupling getEfferentAtualCl() {
		return efferentAtualCl;
	}

	public void setEfferentAtualCl(Coupling efferentAtualCl) {
		this.efferentAtualCl = efferentAtualCl;
	}

	public Coupling getEfferentAtualizadoCl() {
		return efferentAtualizadoCl;
	}

	public void setEfferentAtualizadoCl(Coupling efferentAtualizadoCl) {
		this.efferentAtualizadoCl = efferentAtualizadoCl;
	}

	public Coupling getAfferentAtual() {
		return afferentAtual;
	}

	public void setAfferentAtual(Coupling afferentAtual) {
		this.afferentAtual = afferentAtual;
	}

	public Coupling getAfferentAtualizado() {
		return afferentAtualizado;
	}

	public void setAfferentAtualizado(Coupling afferentAtualizado) {
		this.afferentAtualizado = afferentAtualizado;
	}

	public Coupling getEfferentAtual() {
		return efferentAtual;
	}

	public void setEfferentAtual(Coupling efferentAtual) {
		this.efferentAtual = efferentAtual;
	}

	public Coupling getEfferentAtualizado() {
		return efferentAtualizado;
	}

	public void setEfferentAtualizado(Coupling efferentAtualizado) {
		this.efferentAtualizado = efferentAtualizado;
	}

	

}
