package com.example.rehabilitacja.klasy;

public class Cwiczenie {
	private String nazwa;
	private int serie;
	private int powtorzenia;
	private String link;
	
	public Cwiczenie(){

	}
	
	public Cwiczenie(String nazwa, int serie, int powtorzenia, String link) {
		super();
		this.nazwa = nazwa;
		this.serie = serie;
		this.powtorzenia = powtorzenia;
		this.link = link;
	}

	public String getNazwa() {
		return nazwa;
	}

	public int getSerie() {
		return serie;
	}

	public int getPowtorzenia() {
		return powtorzenia;
	}

	public String getLink() {
		return link;
	}
		
}
