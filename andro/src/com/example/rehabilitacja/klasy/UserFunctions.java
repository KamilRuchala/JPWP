package com.example.rehabilitacja.klasy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
 
public class UserFunctions {
     
     
    // Testing in localhost using wamp or xampp 
    // use http://10.0.2.2/ to connect to your localhost ie http://localhost/
    private static String URL = "http://192.168.0.16/test/index2.php";
    
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    
    private static String login_tag = "login";
    private static String dzienny_plan = "dzienny_plan";
    private static String dane_ogolne = "dane_ogolne";
     
    // constructor
    public UserFunctions(){
    }
    
    @SuppressWarnings("finally")
	public static String getServerResponse(List<NameValuePair> params){
    	// Making HTTP request
        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(URL);
            httpPost.setEntity(new UrlEncodedFormEntity(params));
 
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
 
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        finally{
        	return json;
        }
    }
    
     
    /**
     * function make Login Request
     * */
    public static JSONObject loginUser(String id, String pass){
    	// Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("id", id));
        params.add(new BasicNameValuePair("pass", pass));
        params.add(new BasicNameValuePair("ident", "0"));
        String json =  getServerResponse(params);
        // return json
        // Log.e("JSON", json.toString());
        
        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);            
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
 
        // return JSON String
        return jObj;
    }

     
    /**
     * Function get Login status
     * */
    public boolean isUserLoggedIn(Context context){
       
        
        return false;
    }
     
    /**
     * Function to logout user
     * Reset Database
     * */
    public boolean logoutUser(Context context){
        
        return true;
    }
    
    /**
     * my functions
     * */
    
    public static JSONObject userData(String uid){
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
	    params.add(new BasicNameValuePair("tag", dane_ogolne));
	    params.add(new BasicNameValuePair("uid", uid));
	    String json =  getServerResponse(params);
        // return json
        // Log.e("JSON", json.toString());
        
        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);            
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
 
        // return JSON String
        return jObj;
    }
    
    
    public static JSONObject getTodayPlan(String uid){
	 // Building Parameters
	    List<NameValuePair> params = new ArrayList<NameValuePair>();
	    params.add(new BasicNameValuePair("tag", dzienny_plan));
	    params.add(new BasicNameValuePair("uid", uid));
	    //String json = jsonParser.getJSONFromUrl(URL, params);
        JSONObject jObj = null;
        // pasuje tego stringa zesplitowac wzgledem kropek, pozniej petelka i liczenie elementow; pasuje zwrocic tablice bo prawie zawsze bedzie >1 jsonow
        return jObj;
    }
    
}
