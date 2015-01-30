package com.example.rehabilitacja.klasy;

/**
 * Klasa opisujaca Pacjenta
 * @author Kamil
 */
public class Pacjent {
	private String imie;
	private String nazwisko;
	private String uraz_nazwa;
	private int etap;
	private int dlugosc_leczenia;
	private int postep;
	
	/**
	 * Konstruktor pacjenta
	 * @param imie
	 * @param nazwisko
	 * @param uraz_nazwa
	 * @param etap
	 * @param dlugosc_leczenia
	 * @param postep
	 */
	public Pacjent(String imie, String nazwisko, String uraz_nazwa, int etap,
			int dlugosc_leczenia, int postep) {
		super();
		this.imie = imie;
		this.nazwisko = nazwisko;
		this.uraz_nazwa = uraz_nazwa;
		this.etap = etap;
		this.dlugosc_leczenia = dlugosc_leczenia;
		this.postep = postep;
	}
	
	/**
	 * @return imie pacjenta
	 */
	public String getImie() {
		return imie;
	}
	/**
	 * @return nazwisko pacjenta
	 */
	public String getNazwisko() {
		return nazwisko;
	}
	/**
	 * @return uraz pacjenta
	 */
	public String getUraz_nazwa() {
		return uraz_nazwa;
	}
	/**
	 * @return etap leczenia pacjenta
	 */
	public int getEtap() {
		return etap;
	}
	/**
	 * @return przewidywana dlugosc leczenia pacjenta
	 */
	public int getDlugosc_leczenia() {
		return dlugosc_leczenia;
	}
	/**
	 * @return postep leczenia pacjenta
	 */
	public int getPostep() {
		return postep;
	}
	
	
	
}
