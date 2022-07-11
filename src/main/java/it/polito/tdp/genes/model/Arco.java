package it.polito.tdp.genes.model;

public class Arco {
	
	private String interaction1;
	private String interaction2;
	private int peso;
	
	
	public Arco(String interaction1, String interaction2, int peso) {
		this.interaction1 = interaction1;
		this.interaction2 = interaction2;
		this.peso = peso;
	}
	public String getInteraction1() {
		return interaction1;
	}
	public void setInteraction1(String interaction1) {
		this.interaction1 = interaction1;
	}
	public String getInteraction2() {
		return interaction2;
	}
	public void setInteraction2(String interaction2) {
		this.interaction2 = interaction2;
	}
	public int getPeso() {
		return peso;
	}
	public void setPeso(int peso) {
		this.peso = peso;
	}
	
	
	

}
