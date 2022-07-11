package it.polito.tdp.genes.model;

public class Classification {

	private String geneId;
	private String Localization;
	public Classification(String geneId, String localization) {
		super();
		this.geneId = geneId;
		Localization = localization;
	}
	public String getGeneId() {
		return geneId;
	}
	public void setGeneId(String geneId) {
		this.geneId = geneId;
	}
	public String getLocalization() {
		return Localization;
	}
	public void setLocalization(String localization) {
		Localization = localization;
	}
	
	
}
