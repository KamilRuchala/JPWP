package com.example.rehabilitacja.klasy;

public class Pacjent {
	private String imie;
	private String nazwisko;
	private String uraz_nazwa;
	private int etap;
	private int dlugosc_leczenia;
	private int postep;
	
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

	public String getImie() {
		return imie;
	}

	public String getNazwisko() {
		return nazwisko;
	}

	public String getUraz_nazwa() {
		return uraz_nazwa;
	}

	public int getEtap() {
		return etap;
	}

	public int getDlugosc_leczenia() {
		return dlugosc_leczenia;
	}

	public int getPostep() {
		return postep;
	}
	
	
	
}
