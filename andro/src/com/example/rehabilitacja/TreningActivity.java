package com.example.rehabilitacja;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.example.rehabilitacja.klasy.Cwiczenie;
import com.example.rehabilitacja.klasy.UserFunctions;

public class TreningActivity extends ActionBarActivity {
	
	final Context context = this;
	private static int id = 5;
	private List<Cwiczenie> lista = new ArrayList<Cwiczenie>();
	private String uid;
	private String sid;
	private static JSONObject jObj = null;
	private String operation_tag = null;
	LinearLayout linearLayout;
	
	private boolean error = false;
	private boolean freeTime = false;
	private String error_key = null;
	
	private List<Integer> buttonsId = new ArrayList<Integer>();
	private List<String> links = new ArrayList<String>();
	
	OnClickListener clicks=new OnClickListener() {

	    @Override
	    public void onClick(View v) {
	    	
	    	int id1 = v.getId();
	    	for(int a=0; a<links.size();a++){
		    	if(id1 == buttonsId.get(a)){
		    		String link = new String("vnd.youtube:"+(String)links.get(a));
		    		Uri u = Uri.parse(link);
		    		Intent i = new Intent(Intent.ACTION_VIEW,u);
		    		startActivity(i);
		    	}
	    	}
	    }
	};
		
	@SuppressLint("InflateParams") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trening);
		linearLayout = (LinearLayout) findViewById(R.id.linearLay);
		
		Bundle bb = getIntent().getExtras();
		uid=bb.getString("uid");
		sid=bb.getString("sid");
		operation_tag=bb.getString("tag");
		Log.e("coje",sid);
		new nowyWatek().execute();
			
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.trening, menu);
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
	
	private void rest(){
		TextView przerwa = new TextView(this);
        przerwa.setText("");
        przerwa.setId(id);
        id++;
        przerwa.setGravity(Gravity.CENTER_HORIZONTAL);

        ((LinearLayout) linearLayout).addView(przerwa);
        
        TextView przerwa2 = new TextView(this);
        przerwa2.setText("");
        przerwa2.setId(id);
        id++;
        przerwa2.setGravity(Gravity.CENTER_HORIZONTAL);

        ((LinearLayout) linearLayout).addView(przerwa2);
        
        TextView przerwa3 = new TextView(this);
        przerwa3.setText("");
        przerwa3.setId(id);
        id++;
        przerwa3.setGravity(Gravity.CENTER_HORIZONTAL);

        ((LinearLayout) linearLayout).addView(przerwa3);
        
        TextView tytul = new TextView(this);
        tytul.setText("Dzien odpoczynku");
        tytul.setId(id);
        id++;
        tytul.setBackgroundResource(R.drawable.btn_blue_matte);
        tytul.setTextAppearance(this, R.style.naglowekTrening);
        tytul.setGravity(Gravity.CENTER_HORIZONTAL);
        
        ((LinearLayout) linearLayout).addView(tytul);
	}
	
	private void dodajCwiczenie(){
		for(int i = 0; i < lista.size(); i++){
	        TextView tytul = new TextView(this);
	        tytul.setText("Cwiczenie "+Integer.toString(i+1));
	        tytul.setId(id);
	        id++;
	        tytul.setBackgroundResource(R.drawable.btn_green_matte);
	        tytul.setTextAppearance(this, R.style.naglowekTrening);
	        tytul.setGravity(Gravity.CENTER_HORIZONTAL);
	        
	        ((LinearLayout) linearLayout).addView(tytul);
	        
	        TextView przerwa = new TextView(this);
	        przerwa.setText("");
	        przerwa.setId(id);
	        id++;
	        przerwa.setGravity(Gravity.CENTER_HORIZONTAL);
	
	        ((LinearLayout) linearLayout).addView(przerwa);
	        
	        TextView nazwaCwiczenia = new TextView(this);
	        nazwaCwiczenia.setText( (String)lista.get(i).getNazwa());
	        nazwaCwiczenia.setId(id);
	        id++;
	        nazwaCwiczenia.setTextAppearance(this, R.style.planTreningu);
	        nazwaCwiczenia.setGravity(Gravity.CENTER_HORIZONTAL);
	
	        ((LinearLayout) linearLayout).addView(nazwaCwiczenia);
	        
	        TextView serie = new TextView(this);
	        serie.setText("("+(String)lista.get(i).getSerie()+" Serie)");
	        serie.setId(id);
	        id++;
	        serie.setTextAppearance(this, R.style.planTreninguSmall);
	        serie.setGravity(Gravity.CENTER_HORIZONTAL);
	
	        ((LinearLayout) linearLayout).addView(serie);
	        
	        TextView przerwa2 = new TextView(this);
	        przerwa2.setText("");
	        przerwa2.setId(id);
	        id++;
	        przerwa2.setGravity(Gravity.CENTER_HORIZONTAL);
	
	        ((LinearLayout) linearLayout).addView(przerwa2);
	        
	        int liczba_serii = Integer.parseInt((String)lista.get(i).getSerie());
	        for(int j=1;j<=liczba_serii;j++){
		        TextView d = new TextView(this);
		        d.setText(Integer.toString(j)+" Seria : "+(String)lista.get(i).getPowtorzenia()+" powtorzen");
		        d.setId(id);
		        id++;
		        d.setTextAppearance(this, R.style.planTreninguMedium);
		        ((LinearLayout) linearLayout).addView(d);
	        }
	        
	        //d.setGravity(Gravity.RIGHT); dla przycisku
	        
	        
	        RelativeLayout.LayoutParams buttonParams = 
	                new RelativeLayout.LayoutParams(
	                    RelativeLayout.LayoutParams.WRAP_CONTENT, 
	                    RelativeLayout.LayoutParams.WRAP_CONTENT);

	        buttonParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
	        buttonParams.addRule(RelativeLayout.CENTER_VERTICAL);
	        
	        buttonParams.addRule(RelativeLayout.ALIGN_RIGHT);
	        Button myButton = new Button(this);
			myButton.setText("Jak poprawnie wykonac");
			myButton.setId(id);
			buttonsId.add(id);
			links.add((String) lista.get(i).getLink());
			myButton.setOnClickListener(clicks);
	        id++;
	        myButton.setBackgroundResource(R.drawable.btn_blue_matte);
	        
	        RelativeLayout rl = new RelativeLayout(context);

	        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams
	                (LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

	        rl.addView(myButton, lp);
	        ((LinearLayout) linearLayout).addView(rl);
	        
	        TextView przerwa3 = new TextView(this);
	        przerwa3.setText("");
	        przerwa3.setId(id);
	        id++;
	        przerwa3.setGravity(Gravity.CENTER_HORIZONTAL);
	        ((LinearLayout) linearLayout).addView(przerwa3);
	        
		}
		id=5;
	}
	
	private class nowyWatek extends AsyncTask<Void,Void,Void>{

		final Dialog dialog = new Dialog(context);
		Button zamknij;
		TextView pleaseWait;
		
		@Override
		protected void onPreExecute(){
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    	dialog.setContentView(R.layout.activity_login_dialog);
	    	//dialog.setTitle("Logowanie");
	    	zamknij = (Button) dialog.findViewById(R.id.zamknijButton);
	    	zamknij.setVisibility(View.INVISIBLE);
	    	pleaseWait = (TextView) dialog.findViewById(R.id.pleaseWait);
	    	pleaseWait.setText("Loading");
	    	dialog.show();	
	    	
		}
		
		@Override
		protected Void doInBackground(Void... params){
			String odpowiedz = null;
			if("today".equals(operation_tag)){
				odpowiedz = UserFunctions.getTodayPlan(uid, sid);
			}
			else{
				odpowiedz = UserFunctions.getTommorowPlan(uid, sid);
			}
			
			String[] tablica = odpowiedz.split("-");
			Log.e("odpowiedz", Integer.toString(tablica.length));
			for(String tmp : tablica){
				try {
					jObj = new JSONObject(tmp); 
		        } catch (JSONException e) {
		            Log.e("JSONParserTreningAct", "Error parsing data " + e.toString());
		            break;
		        }
				
				try {
					if("0".equals(jObj.getString("success"))){
						error = true;
						error_key = jObj.getString("error_msg");
						return null;
					}
					else if("free".equals(jObj.getString("nazwa"))){
						freeTime = true;
						return null;
					}
					lista.add(new Cwiczenie(jObj.getString("nazwa"),jObj.getString("serie"),jObj.getString("powtorzenia"),jObj.getString("link")));
				} catch (JSONException e) {
					Log.e("JSON Parser", "Error parsing data " + e.toString());
				}
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result){
			if(error == false && !freeTime){
				dodajCwiczenie();
				dialog.dismiss();
			}
			else if(freeTime){
				rest();
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
				tekst.setText(error_key);
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
	/*
	private void startVideo(String videoID) {
		 // default youtube app
		 Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoID));
		 List<ResolveInfo> list = getPackageManager().queryIntentActivities(i, PackageManager.MATCH_DEFAULT_ONLY);
		 if (list.size() == 0) {
		  // default youtube app not present or doesn't conform to the standard we know
		  // use our own activity
		  i = new Intent(getApplicationContext(), YouTube.class);
		  i.putExtra("VIDEO_ID", videoID);
		 }
		 startActivity(i);
		}
		*/
}
