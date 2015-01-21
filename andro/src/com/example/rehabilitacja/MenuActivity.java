package com.example.rehabilitacja;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;

public class MenuActivity extends ActionBarActivity {
	
	MyExpandableAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    List<Integer> listHeaderIcons;
    HashMap<String, List<String>> listDataChild;
    
    private static String KEY_UID = "uid";
    private static String KEY_SID = "sid";
    private String uid;
    private String sid;
    final Context context = this;
    private int backButtonCount = 0;
    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		 // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
 
        // preparing list data
        prepareListData();
 
        listAdapter = new MyExpandableAdapter(this, listDataHeader, listDataChild, listHeaderIcons);
 
        // setting list adapter
        expListView.setAdapter(listAdapter);
        
        expListView.setOnChildClickListener(new OnChildClickListener() {
        	 
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            	if(getResources().getString(R.string.menu11).equals(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition))){
	                todayPlan("today");
	            }
            	else if(getResources().getString(R.string.menu12).equals(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition))){
	                todayPlan("tommorow");
	            }
            	else if(getResources().getString(R.string.menu13).equals(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition))){
	                weekPlan();
	            }
            	else if(getResources().getString(R.string.menu21).equals(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition))){
	                showCalendar();
	            }
            	else if(getResources().getString(R.string.menu22).equals(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition))){
	                planVisit();
	            }
            	else if(getResources().getString(R.string.menu31).equals(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition))){
	                messenger();
	            }
            	else if(getResources().getString(R.string.menu33).equals(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition))){
	                sms();
	            }
            	else if(getResources().getString(R.string.menu61).equals(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition))){
            		logoutUser(v);
            	}
            	return false;
            }
        });
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
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
	    if(backButtonCount >= 1)
	    {
	        Intent intent = new Intent(Intent.ACTION_MAIN);
	        intent.addCategory(Intent.CATEGORY_HOME);
	        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        startActivity(intent);
	    }
	    else
	    {
	        Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
	        backButtonCount++;
	    }
	}
	
	private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
 
        // Adding header data
        listDataHeader.add(getString(R.string.menu1));
        listDataHeader.add(getString(R.string.menu2));
        listDataHeader.add(getString(R.string.menu3));
        listDataHeader.add(getString(R.string.menu4));
        listDataHeader.add(getString(R.string.menu5));
        listDataHeader.add(getString(R.string.menu6));
        listDataHeader.add(getString(R.string.menu7));
 
        // Adding child data
        List<String> menu1 = new ArrayList<String>();
        menu1.add(getString(R.string.menu11));
        menu1.add(getString(R.string.menu12));
        menu1.add(getString(R.string.menu13));
        
        List<String> menu2 = new ArrayList<String>();
        menu2.add(getString(R.string.menu21));
        menu2.add(getString(R.string.menu22));
        
        List<String> menu3 = new ArrayList<String>();
        menu3.add(getString(R.string.menu31));
        menu3.add(getString(R.string.menu32));
        menu3.add(getString(R.string.menu33));
        
        List<String> menu4 = new ArrayList<String>();
        menu4.add(getString(R.string.menu41));
        
        List<String> menu5 = new ArrayList<String>();
        menu5.add(getString(R.string.menu51));
        menu5.add(getString(R.string.menu52));
        
        List<String> menu6 = new ArrayList<String>();
        menu6.add(getString(R.string.menu61));
        menu6.add(getString(R.string.menu62));
        menu6.add(getString(R.string.menu63));
        
        List<String> menu7 = new ArrayList<String>();
        menu7.add(getString(R.string.menu71));
        menu7.add(getString(R.string.menu72));
        
 
       
        listDataChild.put(listDataHeader.get(0), menu1); // Header, Child data, wszystko do haszmapy
        listDataChild.put(listDataHeader.get(1), menu2);
        listDataChild.put(listDataHeader.get(2), menu3);
        listDataChild.put(listDataHeader.get(3), menu4);
        listDataChild.put(listDataHeader.get(4), menu5);
        listDataChild.put(listDataHeader.get(5), menu6);
        listDataChild.put(listDataHeader.get(6), menu7);
        
        // Adding menu icons
        
        listHeaderIcons = new ArrayList<Integer>();
        listHeaderIcons.add(R.drawable.hantle);
        listHeaderIcons.add(R.drawable.kalendarz);
        listHeaderIcons.add(R.drawable.chmurka);
        listHeaderIcons.add(R.drawable.historia);
        listHeaderIcons.add(R.drawable.dane);
        listHeaderIcons.add(R.drawable.klucz_1);
        listHeaderIcons.add(R.drawable.info);
    }
	
	private void todayPlan(String operation_tag){
		SharedPreferences sharedpreferences = getSharedPreferences(LogActivity.MyPREFERENCES, Context.MODE_PRIVATE);
		uid=sharedpreferences.getString(KEY_UID, "brak");
		sid=sharedpreferences.getString(KEY_SID, "brak");
		Intent i = new Intent(this,TreningActivity.class);
		i.putExtra(KEY_UID, uid);
		i.putExtra(KEY_SID, sid);
		i.putExtra("tag",operation_tag);
 	   	startActivity(i);
	}
	
	private void weekPlan(){
		SharedPreferences sharedpreferences = getSharedPreferences(LogActivity.MyPREFERENCES, Context.MODE_PRIVATE);
		uid=sharedpreferences.getString(KEY_UID, "brak");
		sid=sharedpreferences.getString(KEY_SID, "brak");
		Intent i = new Intent(this,WeekPlanActivity.class);
		i.putExtra(KEY_UID, uid);
		i.putExtra(KEY_SID, sid);
 	   	startActivity(i);
	}
	
	private void showCalendar(){
		SharedPreferences sharedpreferences = getSharedPreferences(LogActivity.MyPREFERENCES, Context.MODE_PRIVATE);
		uid=sharedpreferences.getString(KEY_UID, "brak");
		sid=sharedpreferences.getString(KEY_SID, "brak");
		Intent i = new Intent(this,CalendarActivity.class);
		i.putExtra(KEY_UID, uid);
		i.putExtra(KEY_SID, sid);
 	   	startActivity(i);
	}
	
	private void planVisit(){
		SharedPreferences sharedpreferences = getSharedPreferences(LogActivity.MyPREFERENCES, Context.MODE_PRIVATE);
		uid=sharedpreferences.getString(KEY_UID, "brak");
		sid=sharedpreferences.getString(KEY_SID, "brak");
		Intent i = new Intent(this,PlanVisitActivity.class);
		i.putExtra(KEY_UID, uid);
		i.putExtra(KEY_SID, sid);
 	   	startActivity(i);
	}
	
	private void messenger(){
		SharedPreferences sharedpreferences = getSharedPreferences(LogActivity.MyPREFERENCES, Context.MODE_PRIVATE);
		uid=sharedpreferences.getString(KEY_UID, "brak");
		sid=sharedpreferences.getString(KEY_SID, "brak");
		Intent i = new Intent(this,MessageBoxActivity.class);
		i.putExtra(KEY_UID, uid);
		i.putExtra(KEY_SID, sid);
 	   	startActivity(i);
	}
	
	private void sms(){
		SharedPreferences sharedpreferences = getSharedPreferences(LogActivity.MyPREFERENCES, Context.MODE_PRIVATE);
		uid=sharedpreferences.getString(KEY_UID, "brak");
		sid=sharedpreferences.getString(KEY_SID, "brak");
		Intent i = new Intent(this,SmsActivity.class);
		i.putExtra(KEY_UID, uid);
		i.putExtra(KEY_SID, sid);
 	   	startActivity(i);
	}
	
	
	
	 /**
     * Function to logout user
     * Reset Database
     * */
    public void logoutUser(View view){
    	SharedPreferences sharedpreferences = getSharedPreferences(LogActivity.MyPREFERENCES, Context.MODE_PRIVATE);
    	Editor editor = sharedpreferences.edit();
    	editor.clear();
    	editor.commit();
    	android.os.Process.killProcess(android.os.Process.myPid());
    	moveTaskToBack(true); 
    	MenuActivity.this.finish();
    }
    
    
}

