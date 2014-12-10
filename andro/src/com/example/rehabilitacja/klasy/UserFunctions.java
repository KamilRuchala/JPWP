package com.example.rehabilitacja.klasy;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
 
public class UserFunctions {
     
    private static JSONParser jsonParser;
     
    // Testing in localhost using wamp or xampp 
    // use http://10.0.2.2/ to connect to your localhost ie http://localhost/
    private static String URL = "http://192.168.0.16/test/index2.php";
    
    private static String login_tag = "login";
    private static String dzienny_plan = "dzienny_plan";
     
    // constructor
    public UserFunctions(){
        jsonParser = new JSONParser();
    }
     
    /**
     * function make Login Request
     * */
    public static JSONObject loginUser(String id, String pass){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        String ident="0";
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("id", id));
        params.add(new BasicNameValuePair("pass", pass));
        params.add(new BasicNameValuePair("ident", ident));
        Log.d("doinback",params.toString()); // tu jeszcze jest ok, chyba cos parser leci  w kule
        String json = jsonParser.getJSONFromUrl(URL, params); // to juz nie wchodzi
        
        JSONObject jObj = null;
        
     // try parse the string to a JSON object
        try {
        	jObj = new JSONObject(json);            
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        
        return jObj;
        // Log.e("JSON", json.toString());
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
    
    public static JSONObject getTodayPlan(String uid){
	 // Building Parameters
	    List<NameValuePair> params = new ArrayList<NameValuePair>();
	    params.add(new BasicNameValuePair("tag", dzienny_plan));
	    params.add(new BasicNameValuePair("uid", uid));
	    String json = jsonParser.getJSONFromUrl(URL, params);
        JSONObject jObj = null;
        // pasuje tego stringa zesplitowac wzgledem kropek, pozniej petelka i liczenie elementow; pasuje zwrocic tablice bo prawie zawsze bedzie >1 jsonow
        return jObj;
    }
    
}
