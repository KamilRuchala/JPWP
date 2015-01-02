package com.example.rehabilitacja.klasy;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

public class Wizyta {
	private String data;
	private String info;
	private int year;
	private int month;
	private int day;
	
	public Wizyta(String data1, String info) {
		super();
		DateFormat df = new SimpleDateFormat("yyyy-M-dd");
		Date data = null;
		try {
			data = df.parse(data1);
		} catch (ParseException e) {
			Log.e("error","parsing data error");
		}
		
		String data2 = df.format(data);
		String[] tmp = data2.split("-");
		Log.e("wizytaclass",tmp[0]);
		
		this.data = data1;
		this.info = info;
		this.year = Integer.parseInt(tmp[0]);
		this.month = Integer.parseInt(tmp[1]);
		this.day = Integer.parseInt(tmp[2]);
	}

	public String getData() {
		return data;
	}

	public String getInfo() {
		return info;
	}

	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public int getDay() {
		return day;
	}
	
}
