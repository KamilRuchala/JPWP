package com.example.rehabilitacja;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rehabilitacja.klasy.UserFunctions;

public class SmsActivity extends ActionBarActivity {
	
	private String uid, sid, number;
	private static JSONObject jObj = null;
	private String error_msg = null;
	private boolean error = false;
	private Context context = this;
	private EditText tresc;
	private Button send;
	private TextView sms_number;
	SmsManager smsManager = null;
	ArrayList<String> fragmenty = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sms);
		Bundle bb = getIntent().getExtras();
		uid=bb.getString("uid");
		sid=bb.getString("sid");
		tresc = (EditText) findViewById(R.id.editSMS);
		send = (Button) findViewById(R.id.wyslij);
		sms_number = (TextView) findViewById(R.id.sms_numb);
		new nowyWatek().execute();
		smsManager = SmsManager.getDefault();
		send.setOnClickListener(new View.OnClickListener(){
        	@Override
            public void onClick(View v) {
        		String text = tresc.getText().toString();
        		fragmenty = smsManager.divideMessage(text);
        		smsManager.sendMultipartTextMessage(number, null , fragmenty, null, null);
            }
        });
		
		tresc.setOnKeyListener(new OnKeyListener() {
		    @Override
		    public boolean onKey(View v, int keyCode, KeyEvent event) {
		        if (event.getAction()!=KeyEvent.ACTION_DOWN)
		            return false;
		        if(keyCode == KeyEvent.KEYCODE_ENTER ){
		            return true;
		        }
		        if (event.getAction()==KeyEvent.ACTION_DOWN){
		        	licznikSMS();
		            return false;
		        }
		        return false;
		    }
		});
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sms, menu);
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
	
	private class nowyWatek extends AsyncTask<Void,Void,Void>{

		final Dialog dialog = new Dialog(context);
		Button zamknij;
		TextView pleaseWait;
		
		@Override
		protected void onPreExecute(){
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    	dialog.setContentView(R.layout.activity_login_dialog);
	    	zamknij = (Button) dialog.findViewById(R.id.zamknijButton);
	    	zamknij.setVisibility(View.INVISIBLE);
	    	pleaseWait = (TextView) dialog.findViewById(R.id.pleaseWait);
	    	pleaseWait.setText("Loading");
	    	dialog.show();	
	    	
		}
		
		@Override
		protected Void doInBackground(Void... params){
			String odpowiedz = UserFunctions.getPhoneNumber(uid, sid);
			
			try {
				jObj = new JSONObject(odpowiedz); 
				if("0".equals(jObj.getString("success"))){
					error = true;
					error_msg = jObj.getString("error_msg");
					return null;
				}
				else{
					number = jObj.getString("number");
				}
	        } catch (JSONException e) {
	            Log.e("JSONParserTreningAct", "Error parsing data " + e.toString());
	        }
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result){
			if(!error){
				dialog.dismiss();
			}
			else{
				ProgressBar pasek=(ProgressBar) dialog.findViewById(R.id.progressBar1);
				TextView tekst = (TextView) dialog.findViewById(R.id.pleaseWait);
				pasek.setVisibility(View.INVISIBLE);
				RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
				
				p.addRule(RelativeLayout.BELOW, R.id.image);
				p.addRule(RelativeLayout.CENTER_IN_PARENT, R.id.image);
				
				//tekst.setLayoutParams(p);
				tekst.setText(error_msg);
				tekst.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
				tekst.setTextSize(20);
								
				zamknij.setVisibility(View.VISIBLE);
				zamknij.setOnClickListener(new View.OnClickListener(){
		     	@Override
	            public void onClick(View v) {
		     		dialog.dismiss();
		     		Intent i = new Intent(context,MenuActivity.class);
					startActivity(i);
		        }
				});
			}
		}
	}
	
	private void licznikSMS(){
		String text = tresc.getText().toString();
		fragmenty = smsManager.divideMessage(text);
		sms_number.setText("Liczba wiadomosci: " + Integer.toString(fragmenty.size()));
		
	}
}
