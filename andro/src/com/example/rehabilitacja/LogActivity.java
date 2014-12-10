package com.example.rehabilitacja;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rehabilitacja.klasy.UserFunctions;


public class LogActivity extends ActionBarActivity {
	
	EditText id, pass;
	TextView textView1;
	Button zaloguj;
	final Context context = this;
	
	private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    private static String KEY_ERROR_MSG = "error_msg";
    private static String KEY_UID = "uid";
    private static String KEY_ID = "id";
    
    public static String uid = null; // uid nalezy zapisac w sesji, TO DO na pozniej
	
	private class nowyWatek extends AsyncTask<String,Void,String>{

		final Dialog dialog = new Dialog(context);
		Button zamknij;
		
		@Override
		protected void onPreExecute(){
	     	
	    	dialog.setContentView(R.layout.activity_login_dialog);
	    	//dialog.setTitle("Logowanie");
	    	zamknij = (Button) dialog.findViewById(R.id.zamknijButton);
	    	zamknij.setVisibility(View.INVISIBLE);
	    	
	    	dialog.show();	
	    	
		}
		
		@Override
		protected String doInBackground(String... arg0){
			
			
			JSONObject json = UserFunctions.loginUser(arg0[0], arg0[1]); // tu jest problem
			
			return "OK";
			/*
			try {
                if (json.getString(KEY_SUCCESS) == "1") { // tu moze byc problem bo key_success nie jest stringiem
                    uid=json.getString(KEY_UID);
                    return "OK";
                }
                else{
                        // Error in login
                    return "NO";
                }
                
            } catch (JSONException e) {
                return "BLAD";
            }
            */
		}
		
		@Override
		protected void onPostExecute(String result){
			// result=android.text.Html.fromHtml(result).toString();
			Log.d("debuggowanie programu", result);
			textView1.setText(result);
			
			if("OK".equals(result)){
				dialog.dismiss();
				dalej();  // logowanie udane => przejscie do kolejnej aktywnosci
				finish(); // zakoncz aktywnosc logowania
			}
			else{
				ProgressBar pasek=(ProgressBar) dialog.findViewById(R.id.progressBar1);
				TextView tekst = (TextView) dialog.findViewById(R.id.pleaseWait);
				pasek.setVisibility(View.INVISIBLE);
				RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
				
				p.addRule(RelativeLayout.BELOW, R.id.image);
				p.addRule(RelativeLayout.CENTER_IN_PARENT, R.id.image);
				
				tekst.setLayoutParams(p);
				tekst.setText("Blad logowania");
				tekst.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
				tekst.setTextSize(30);
								
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
        
        textView1=(TextView) findViewById(R.id.textView1);
        id=(EditText) findViewById(R.id.editId);
		pass=(EditText) findViewById(R.id.editPass);
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
    
    public void loginSuccess(String a){
    	// TO DO new intent
    }
    
}
