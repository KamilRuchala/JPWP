package com.example.rehabilitacja;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rehabilitacja.klasy.UserFunctions;
import com.example.rehabilitacja.klasy.Wiadomosc;

public class MessageActivity extends ActionBarActivity {

	private String error_msg = null;
	private String title = null;
	private boolean error = false;
	private Context context = this;
	private static String KEY_UID = "uid";
    private static String KEY_SID = "sid";
    private static String KEY_TITLE = "title";
    private static int id = 50;
	
	private String uid, sid;
	private static JSONObject jObj = null;
	
	private LinearLayout layout;
	private List<Wiadomosc> messages_list = new ArrayList<Wiadomosc>();
	private EditText message_to_send;
	private Button send;
	private ScrollView scroll;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		
		Bundle bb = getIntent().getExtras();
		uid = bb.getString(KEY_UID);
		sid = bb.getString(KEY_SID);
		title = bb.getString(KEY_TITLE);
		
		layout = (LinearLayout) findViewById(R.id.linlay);
		scroll = (ScrollView) findViewById(R.id.scrollView1);
		new nowyWatek().execute();
		
		message_to_send = (EditText) findViewById(R.id.editText1);
		send = (Button) findViewById(R.id.button1);
		send.setOnClickListener(new View.OnClickListener(){
        	@Override
            public void onClick(View v) {
        		String text = message_to_send.getText().toString();
        		if(text.matches("\\w+")){
        			sendMessage(text);
        		}
        		else{
        			Toast.makeText(getApplicationContext(), "Napisz cos!", Toast.LENGTH_LONG).show();
        		}
            }
        });
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.message, menu);
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
	
	@Override
	public void onBackPressed()
	{
		Intent i = new Intent(this,MessageBoxActivity.class);
		startActivity(i);
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
			String odpowiedz = UserFunctions.getMessageByTitle(uid, sid, title);
			
			String[] message_titles = odpowiedz.split("--");
			try {
				jObj = new JSONObject(message_titles[0]); 
				if("0".equals(jObj.getString("success"))){
					error = true;
					error_msg = jObj.getString("error_msg");
					return null;
				}
	        } catch (JSONException e) {
	           // Log.e("JSONParserTreningAct", "Error parsing data " + e.toString());
	        }
			
			for(int i = 1;i<message_titles.length;i++){
				String tmp = message_titles[i];
				try {
					jObj = new JSONObject(tmp);
					messages_list.add(new Wiadomosc(jObj.getString("data"), title, jObj.getString("tag"), jObj.getString("tresc")));
		        } catch (JSONException e) {
		            Log.e("JSONParserTreningAct", "Error parsing data " + e.toString());
		            break;
		        }
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result){
			if(!error){
				prepareLayout();
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
	
	private void prepareLayout(){
		for(int i = 0; i < messages_list.size(); i++){
			TextView data = new TextView(this);
			data.setText(messages_list.get(i).getData());
	        data.setId(id);
	        id++;
	        data.setTextAppearance(getApplicationContext(),R.style.message_data_style);
	        data.setGravity(Gravity.CENTER_HORIZONTAL);
	        ((LinearLayout) layout).addView(data);
	        
	        TextView wiadomosc = new TextView(this);
			wiadomosc.setText(messages_list.get(i).getTresc());
			wiadomosc.setTextAppearance(getApplicationContext(),R.style.message_style);
			wiadomosc.setId(id);
	        id++;
	        RelativeLayout rl = new RelativeLayout(context);
	        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams
	                (LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	        if("0".equals(messages_list.get(i).getTag())){
	        	wiadomosc.setBackgroundResource(R.drawable.speech_bubble_green);
	        	lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
	        }
	        else{
	        	wiadomosc.setBackgroundResource(R.drawable.speech_bubble_orange);
	        	lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	        }
	        rl.addView(wiadomosc, lp);
	        ((LinearLayout) layout).addView(rl);
		}
		scroll.post(new Runnable() {            
		    @Override
		    public void run() {
		           scroll.fullScroll(View.FOCUS_DOWN);              
		    }
		});
		
	}
	
	private void sendMessage(String tresc){
		// trzeba dodac do bazy w nowym watku
		String data1 = getDate();
		new nowyWatek2().execute(tresc, data1);
		Log.e("data",data1);
		if(!error){
			data1 = getDateWithoutSec();
			Log.e("data",data1);
			TextView data = new TextView(this);
			data.setText(data1);
	        data.setId(id);
	        id++;
	        data.setTextAppearance(getApplicationContext(),R.style.message_data_style);
	        data.setGravity(Gravity.CENTER_HORIZONTAL);
	        ((LinearLayout) layout).addView(data);
	        
	        TextView wiadomosc = new TextView(this);
			wiadomosc.setText(tresc);
			wiadomosc.setTextAppearance(getApplicationContext(),R.style.message_style);
			wiadomosc.setId(id);
	        id++;
	        RelativeLayout rl = new RelativeLayout(context);
	        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams
	                (LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	  
	        wiadomosc.setBackgroundResource(R.drawable.speech_bubble_green);
	        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
	        rl.addView(wiadomosc, lp);
	        ((LinearLayout) layout).addView(rl);
		}
	}
	
	private String getDate(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date today = Calendar.getInstance().getTime();
		String reportDate = dateFormat.format(today);
		return reportDate;
	}
	
	private String getDateWithoutSec(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date today = Calendar.getInstance().getTime();
		String reportDate = dateFormat.format(today);
		return reportDate;
	}
	
	private class nowyWatek2 extends AsyncTask<String,String,Void>{

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
		protected Void doInBackground(String... params){
			String tresc = params[0];
			String data = params[1];
			String odpowiedz = UserFunctions.storeMessage(uid, sid, title, tresc, data);
			
			try {
				jObj = new JSONObject(odpowiedz); 
				if("0".equals(jObj.getString("success"))){
					error = true;
					error_msg = jObj.getString("error_msg");
					return null;
				}
	        } catch (JSONException e) {
	           // Log.e("JSONParserTreningAct", "Error parsing data " + e.toString());
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
}
