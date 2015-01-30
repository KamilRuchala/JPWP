/**
 * Klasa opisujaca cwiczenie
 * @author Kamil
 */

package com.example.rehabilitacja.klasy;

public class Cwiczenie {
	/**
	 *  nazwa cwiczenia
	 */
	private String nazwa;
	/**
	 *  serie cwiczen
	 */
	private String serie;
	/**
	 *  powtorzenia na serie
	 */
	private String powtorzenia;
	/**
	 *  link youtube do cwiczenia
	 */
	private String link;
	
	public Cwiczenie(){

	}
	/**
	 *  Konstruktor 
	 *  @param nazwa
	 *  @param serie
	 *  @param powtorzenia
	 *  @param link
	 */
	public Cwiczenie(String nazwa, String serie, String powtorzenia, String link) {
		super();
		this.nazwa = nazwa;
		this.serie = serie;
		this.powtorzenia = powtorzenia;
		this.link = link;
	}
	
	/**
	 *  Przeladowany konstruktor (bez linku do cwiczenia)
	 *  @param nazwa
	 *  @param serie
	 *  @param powtorzenia
	 */
	public Cwiczenie(String nazwa, String serie, String powtorzenia) {
		super();
		this.nazwa = nazwa;
		this.serie = serie;
		this.powtorzenia = powtorzenia;
		this.link = "";
	}
	
	/** 
	 *  @return nazwa cwiczenia
	 */
	public String getNazwa() {
		return nazwa;
	}
	
	/** 
	 *  @return serie cwiczenia
	 */
	public String getSerie() {
		return serie;
	}

	/** 
	 *  @return powtorzenia na serie cwiczenia
	 */
	public String getPowtorzenia() {
		return powtorzenia;
	}

	/** 
	 *  @return link do cwiczenia
	 */
	public String getLink() {
		return link;
	}
		
}
