package com.example.rehabilitacja;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.rehabilitacja.klasy.UserFunctions;


public class LogActivity extends ActionBarActivity {
	
	EditText id, pass;
	Button zaloguj;
	final Context context = this;
	
	private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR_MSG = "error_msg";
    private static String KEY_UID = "uid";
    private static String KEY_SID = "sid";
    
    public static String uid = null;
    public static String sid = null; // uid nalezy zapisac w sesji, TO DO na pozniej
    
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
	
	private class nowyWatek extends AsyncTask<String,Void,String>{

		final Dialog dialog = new Dialog(context);
		Button zamknij;
		
		@Override
		protected void onPreExecute(){
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    	dialog.setContentView(R.layout.activity_login_dialog);
	    	//dialog.setTitle("Logowanie");
	    	zamknij = (Button) dialog.findViewById(R.id.zamknijButton);
	    	zamknij.setVisibility(View.INVISIBLE);
	    	
	    	dialog.show();	
	    	
		}
		
		@Override
		protected String doInBackground(String... arg0){
			
			
			JSONObject json = UserFunctions.loginUser(arg0[0], arg0[1]);
			try {

                if ("1".equals(json.getString(KEY_SUCCESS))) { 
                    uid = json.getString(KEY_UID);
                    sid = json.getString(KEY_SID);
                    Editor editor = sharedpreferences.edit();
                    editor.putString(KEY_UID, uid);
                    editor.putString(KEY_SID, sid);
                    editor.commit();
                    return "OK";
                }
                else if ("-1".equals(json.getString(KEY_SUCCESS))) {
                	String error_log=json.getString(KEY_ERROR_MSG);
                    return error_log;
                }
                else{
                    String error_log=json.getString(KEY_ERROR_MSG);
                    return error_log;
                }
                
            } catch (JSONException e) {
                return "Blad aplikacji";
            }
            
		}
		
		@Override
		protected void onPostExecute(String result){
			// result=android.text.Html.fromHtml(result).toString();
			Log.d("debuggowanie programu", result);
			
			if("OK".equals(result)){
				dialog.dismiss();
				dalej();  // logowanie udane => przejscie do kolejnej aktywnosci
				finish(); // zakoncz aktywnosc logowania
			}
			else{
				
				ProgressBar pasek=(ProgressBar) dialog.findViewById(R.id.progressBar1);
				TextView tekst = (TextView) dialog.findViewById(R.id.pleaseWait);
				pasek.setVisibility(View.INVISIBLE);
				
				tekst.setText(result);
				tekst.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
				tekst.setTextSize(20);
								
				zamknij.setVisibility(View.VISIBLE);
				zamknij.setOnClickListener(new View.OnClickListener(){
		     	@Override
	            public void onClick(View v) {
		     		dialog.dismiss();
		        }
				});
				
			}
		}
	}
	
	private void dalej() {
		Intent i = new Intent(this,MenuActivity.class);
		startActivity(i);
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        id=(EditText) findViewById(R.id.editId);
		pass=(EditText) findViewById(R.id.editPass);
		
		id.setText("12345");
		pass.setText("miszcz");
		
        zaloguj=(Button) findViewById(R.id.buttonZaloguj);
        zaloguj.setOnClickListener(new View.OnClickListener(){
        	@Override
            public void onClick(View v) {
        		loginPost();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.log, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void loginPost(){
        String username = id.getText().toString();
        String password = pass.getText().toString();
        new nowyWatek().execute(username,password);
     }
    
    @Override
    protected void onResume() {
       sharedpreferences=getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
       if (sharedpreferences.contains(KEY_UID))
       {
	       if(sharedpreferences.contains(KEY_SID)){
	    	   Intent i = new Intent(this,MenuActivity.class);
	    	   startActivity(i);
	       }
       }
       super.onResume();
    }
    
}
