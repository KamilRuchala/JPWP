package com.example.rehabilitacja;

import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MessageActivity extends ActionBarActivity {

	private String error_msg = null;
	private String title = null;
	private boolean error = false;
	private Context context = this;
	private static String KEY_UID = "uid";
    private static String KEY_SID = "sid";
    private static String KEY_TITLE = "title";
	
	private String uid, sid;
	private static JSONObject jObj = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		
		Bundle bb = getIntent().getExtras();
		uid = bb.getString(KEY_UID);
		sid = bb.getString(KEY_SID);
		title = bb.getString(KEY_TITLE);
	
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
}
