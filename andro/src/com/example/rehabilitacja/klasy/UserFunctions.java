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
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
 
public class UserFunctions {
     
     
    // Testing in localhost using wamp or xampp 
    // use http://10.0.2.2/ to connect to your localhost ie http://localhost/
    private static String URL = "http://192.168.1.27/test/index2.php";
    
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    
    private static String login_tag = "login";
    private static String dzienny_plan = "dzienny_plan";
    private static String tommorow_plan = "jutrzejszy_plan";
    private static String week_plan = "week_plan";
    private static String message_box = "message_box";
    private static String messages = "messages_by_title";
    private static String store_message = "store_message";
    private static String doctor_number = "doctor_number";
    //private static String dane_ogolne = "dane_ogolne";
     
    // constructor
    public UserFunctions(){
    }
    
    @SuppressWarnings("finally")
	public static String getServerResponse(List<NameValuePair> params){
    	// Making HTTP request
        try {
        	HttpParams httpParameters = new BasicHttpParams();
        	int timeoutConnection = 5000;
        	HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        	int timeoutSocket = 5000;
        	HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        	
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(URL);
            httpPost.setEntity(new UrlEncodedFormEntity(params));
 
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
 
        } catch (UnsupportedEncodingException e) {
        	return "blad";
        } catch (ClientProtocolException e) {
        	return "blad";
        } catch (IOException e) {
        	return "blad";
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
            Log.e("dupcia",json);
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
    @SuppressWarnings("finally")
	public static JSONObject loginUser(String id, String pass){
    	// Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("id", id));
        params.add(new BasicNameValuePair("pass", pass));
        params.add(new BasicNameValuePair("ident", "0"));
        String json =  getServerResponse(params);
        Log.d("test",json);
        if("blad".equals(json)){ //jesli wystapil blad polaczenia
        	JSONObject obj = new JSONObject();
        	try {
				obj.put("success", "-1");
				obj.put("error_msg", "Serwer niedostepny");
				
			} catch (JSONException e) {
				obj=null;
			}
        	finally{
        		return obj;
        	}
        }
        
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
     * my functions
     * */
    
    
    
    /**
     * Function to logout user's today plan
     * Reset Database
     * */
    public static String getTodayPlan(String uid, String sid){
	 // Building Parameters
	    List<NameValuePair> params = new ArrayList<NameValuePair>();
	    params.add(new BasicNameValuePair("tag", dzienny_plan));
	    params.add(new BasicNameValuePair("uid", uid));
	    params.add(new BasicNameValuePair("sid", sid));
	    return getServerResponse(params);
    }
    
    public static String getTommorowPlan(String uid, String sid){
   	 // Building Parameters
   	    List<NameValuePair> params = new ArrayList<NameValuePair>();
   	    params.add(new BasicNameValuePair("tag", tommorow_plan));
   	    params.add(new BasicNameValuePair("uid", uid));
   	    params.add(new BasicNameValuePair("sid", sid));
   	    return getServerResponse(params);
    }
    
    public static String getWeekPlan(String uid, String sid){
      	 // Building Parameters
      	    List<NameValuePair> params = new ArrayList<NameValuePair>();
      	    params.add(new BasicNameValuePair("tag", week_plan));
      	    params.add(new BasicNameValuePair("uid", uid));
      	    params.add(new BasicNameValuePair("sid", sid));
      	    return getServerResponse(params);
    }
    
    public static String getMessageBox(String uid, String sid, String page_nr){
     	 // Building Parameters
     	    List<NameValuePair> params = new ArrayList<NameValuePair>();
     	    params.add(new BasicNameValuePair("tag", message_box));
     	    params.add(new BasicNameValuePair("uid", uid));
     	    params.add(new BasicNameValuePair("sid", sid));
     	    params.add(new BasicNameValuePair("page_nr", page_nr));
     	    return getServerResponse(params);
   }
    
   public static String getMessageByTitle(String uid, String sid, String title){
    	 // Building Parameters
    	    List<NameValuePair> params = new ArrayList<NameValuePair>();
    	    params.add(new BasicNameValuePair("tag", messages));
    	    params.add(new BasicNameValuePair("uid", uid));
    	    params.add(new BasicNameValuePair("sid", sid));
    	    params.add(new BasicNameValuePair("title", title));
    	    return getServerResponse(params);
  }
   
   public static String storeMessage(String uid, String sid, String title, String tresc, String data){
  	 // Building Parameters
  	    List<NameValuePair> params = new ArrayList<NameValuePair>();
  	    params.add(new BasicNameValuePair("tag", store_message));
  	    params.add(new BasicNameValuePair("uid", uid));
  	    params.add(new BasicNameValuePair("sid", sid));
  	    params.add(new BasicNameValuePair("title", title));
  	    params.add(new BasicNameValuePair("tresc", tresc));
  	    params.add(new BasicNameValuePair("data", data));
  	    params.add(new BasicNameValuePair("user_tag", "0"));
  	    return getServerResponse(params);
   }
   
   public static String getPhoneNumber(String uid, String sid){
	  	 // Building Parameters
	  	    List<NameValuePair> params = new ArrayList<NameValuePair>();
	  	    params.add(new BasicNameValuePair("tag", doctor_number));
	  	    params.add(new BasicNameValuePair("uid", uid));
	  	    params.add(new BasicNameValuePair("sid", sid));
	  	    return getServerResponse(params);
	}
    
}
