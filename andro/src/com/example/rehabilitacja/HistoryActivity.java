package com.example.rehabilitacja;

import java.util.ArrayList;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rehabilitacja.klasy.UserFunctions;
import com.example.rehabilitacja.klasy.Wizyta;

public class HistoryActivity extends ActionBarActivity {
	
	private String uid, sid;
	private int id = 5;
	private boolean error = false;
	private String error_msg = null;
	private static JSONObject jObj = null;
	private final Context context = this;
	private static String KEY_UID = "uid";
    private static String KEY_SID = "sid";
	
	private List<Wizyta> lista = new ArrayList<Wizyta>();
	private LinearLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 //Remove title bar
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);

	    //Remove notification bar
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_history);
		
		Bundle bb = getIntent().getExtras();
		uid = bb.getString(KEY_UID);
		sid = bb.getString(KEY_SID);
		layout = (LinearLayout) findViewById(R.id.lejalt);
		new nowyWatek().execute();
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
			String odpowiedz = UserFunctions.getHistory(uid, sid);
			
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
					
					lista.add(new Wizyta(jObj.getString("data"), jObj.getString("uwagi")));
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
		for(int i=0; i < lista.size(); i++){
			TextView tytul = new TextView(context);
	        tytul.setText(lista.get(i).getYear() + "-" + lista.get(i).getMonth() + "-" + lista.get(i).getDay());
	        tytul.setId(id);
	        id++;
	        //tytul.setBackgroundResource(R.drawable.btn_green_matte);
	        tytul.setTextAppearance(context, R.style.naglowekTreningWeek);
	        tytul.setGravity(Gravity.CENTER_HORIZONTAL);
	        
	        ((LinearLayout) layout).addView(tytul);
	        
	        TextView opis = new TextView(context);
	        opis.setText(lista.get(i).getInfo());
	        opis.setId(id);
	        id++;
	        opis.setTextAppearance(context, R.style.messageBoxDateContent);
	        
	        ((LinearLayout) layout).addView(opis);
	        
	        TextView pasek = new TextView(context);
	        pasek.setText(lista.get(i).getYear() + "-" + lista.get(i).getMonth() + "-" + lista.get(i).getDay());
	        pasek.setId(id);
	        id++;
	        pasek.setBackgroundResource(R.drawable.btn_green_matte);
	        pasek.setGravity(Gravity.CENTER_HORIZONTAL);
	        ProgressBar progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
	        ((LinearLayout) layout).addView(progressBar);
		}
	}
}
