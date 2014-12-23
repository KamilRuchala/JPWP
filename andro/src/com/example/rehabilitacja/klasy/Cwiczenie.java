package com.example.rehabilitacja.klasy;

public class Cwiczenie {
	private String nazwa;
	private String serie;
	private String powtorzenia;
	private String link;
	
	public Cwiczenie(){

	}
	
	public Cwiczenie(String nazwa, String serie, String powtorzenia, String link) {
		super();
		this.nazwa = nazwa;
		this.serie = serie;
		this.powtorzenia = powtorzenia;
		this.link = link;
	}
	
	public Cwiczenie(String nazwa, String serie, String powtorzenia) {
		super();
		this.nazwa = nazwa;
		this.serie = serie;
		this.powtorzenia = powtorzenia;
		this.link = "";
	}

	public String getNazwa() {
		return nazwa;
	}

	public String getSerie() {
		return serie;
	}

	public String getPowtorzenia() {
		return powtorzenia;
	}

	public String getLink() {
		return link;
	}
		
}
