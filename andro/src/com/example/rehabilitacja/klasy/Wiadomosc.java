package com.example.rehabilitacja.klasy;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.util.Log;

public class Wiadomosc {
	private String data;
	private String title;
	private String tag;
	private String tresc;
	
	public Wiadomosc(String data1, String title) {
		super();
		DateFormat df = new SimpleDateFormat("yyyy-M-dd HH:mm");
		Date data = null;
		try {
			data = df.parse(data1);
		} catch (ParseException e) {
			Log.e("error","parsing data error");
		}
		
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		
		int act_year = calendar.get(Calendar.YEAR);
		
		Calendar newCalendar = Calendar.getInstance();
		newCalendar.setTime(data);
		int year = newCalendar.get(Calendar.YEAR);
		DateFormat df1 = new SimpleDateFormat("M-dd HH:mm ");
		
		if(year == act_year){
			data1 = df1.format(newCalendar.getTime());
		}
		
		this.data = data1;
		this.title = title;
		this.tag = "";
		this.tresc = "";
	}
	
	public Wiadomosc(String data1, String title, String tag, String tresc) {
		super();
		DateFormat df = new SimpleDateFormat("yyyy-M-dd HH:mm");
		Date data = null;
		try {
			data = df.parse(data1);
		} catch (ParseException e) {
			Log.e("error","parsing data error");
		}
		
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		
		int act_year = calendar.get(Calendar.YEAR);
		
		Calendar newCalendar = Calendar.getInstance();
		newCalendar.setTime(data);
		int year = newCalendar.get(Calendar.YEAR);
		DateFormat df1 = new SimpleDateFormat("M-dd HH:mm");
		
		if(year == act_year){
			data1 = df1.format(newCalendar.getTime());
		}
		
		this.data = data1;
		this.title = title;
		this.tag = tag;
		this.tresc = tresc;
	}

	public String getData() {
		return data;
	}

	public String getTitle() {
		return title;
	}
	
	public String getTag() {
		return tag;
	}
	
	public String getTresc() {
		return tresc;
	}
}
