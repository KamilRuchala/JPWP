


///////// UWAGA! PLIK PRAKTYCZNIE DO WYRZUCENIA! ///////////////////////


package com.example.rehabilitacja.klasy;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.json.JSONObject;

import android.util.Log;
 
public class JSONParser { //tak na prawde nie parsuje jsona ale zwraca odpowiedz serwera, ciezko zrobic zeby w tym miejscu byl elastyczny
 
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
 
    // constructor
    public JSONParser() {
 
    }
 
    public String getJSONFromUrl(String url1, String tag, String id, String pass, String ident) {
    	Log.d("doinback","Making HTTP request");
        // Making HTTP request
    	try{
            String username = id;
            String password = pass;
            String data = URLEncoder.encode("tag", "UTF-8")+ "=" + URLEncoder.encode(tag, "UTF-8");
            data  += URLEncoder.encode("id", "UTF-8")+ "=" + URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("pass", "UTF-8")+ "=" + URLEncoder.encode(password, "UTF-8");
            data += "&" + URLEncoder.encode("ident", "UTF-8")+ "=" + URLEncoder.encode(ident, "UTF-8");
            URL url = new URL(url1);
            URLConnection conn = url.openConnection(); 
            conn.setDoOutput(true); 
            OutputStreamWriter wr = new OutputStreamWriter
            (conn.getOutputStream()); 
            wr.write( data ); 
            wr.flush(); 
            BufferedReader reader = new BufferedReader
            (new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            // Read Server Response
            while((line = reader.readLine()) != null)
            {
               sb.append(line);
               break;
            }
            return sb.toString();
         }catch(Exception e){
            return new String("Exception: " + e.getMessage());
         }
 
    }
}
