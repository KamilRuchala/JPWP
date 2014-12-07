package com.example.rehabilitacja;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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


public class LogActivity extends ActionBarActivity {
	
	EditText id, pass;
	TextView textView1;
	Button zaloguj;
	final Context context = this;
	
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
			try{
				
				String username = (String)arg0[0];
	            String password = (String)arg0[1];
	            String link="http://192.168.0.16/test/index.php";
	            String data  = URLEncoder.encode("id", "UTF-8") 
	            + "=" + URLEncoder.encode(username, "UTF-8");
	            data += "&" + URLEncoder.encode("pass", "UTF-8") 
	            + "=" + URLEncoder.encode(password, "UTF-8");
	            URL url = new URL(link);
	            URLConnection conn = url.openConnection(); 
	            conn.setDoOutput(true); 
	            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream()); 
	            wr.write( data ); 
	            wr.flush(); 
	            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            // Read Server Response
	            while((line = reader.readLine()) != null)
	            {
	               sb.append(line);
	               break;
	            }
	            return sb.toString();
			}catch(Exception e){
				return new String("Exception: " + e.getMessage());
	      }
		}
		
		@Override
		protected void onPostExecute(String result){
			result=android.text.Html.fromHtml(result).toString();
			if("dupa".equals(result)==false){
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
			else{ //Logowanie zakonczone sukcesem - przejscie do kolejnej intencji- TO DO
				dialog.dismiss();
				dalej();
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
