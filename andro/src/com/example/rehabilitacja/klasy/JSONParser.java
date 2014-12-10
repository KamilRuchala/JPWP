package com.example.rehabilitacja.klasy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
 
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
 
import android.util.Log;
 
public class JSONParser { //tak na prawde nie parsuje jsona ale zwraca odpowiedz serwera, ciezko zrobic zeby w tym miejscu byl elastyczny
 
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
 
    // constructor
    public JSONParser() {
 
    }
 
    public String getJSONFromUrl(String url, List<NameValuePair> params) {
    	Log.d("doinback","Making HTTP request");
        // Making HTTP request
        try {
        	
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params));
 
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
            
 
        } catch (UnsupportedEncodingException e) {
        	Log.d("wyjatek1","dupastotysiecy");
        } catch (ClientProtocolException e) {
        	Log.d("wyjatek2","dupastotysiecy");
        } catch (IOException e) {
        	Log.d("wyjatek3","dupastotysiecy");
        }
 
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
            Log.e("JSON", json);
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // return server response
        return json;
 
    }
}
