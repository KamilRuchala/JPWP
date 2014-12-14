package com.example.rehabilitacja;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.example.rehabilitacja.klasy.UserFunctions;

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
    private String dalej;
    private String OPTION_TAG="todayPlan";
    final Context context = this;
    

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
	                todayPlan();
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
    public void onBackPressed(){ //konieczne poniewaz przy powrocie do menu przy kliknieciu back'a znowu pojawia sie menu i backiem nie mozna zminimalizowac
    	moveTaskToBack(true); 
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
        menu2.add(getString(R.string.menu23));
        
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
	
	private void todayPlan(){
		SharedPreferences sharedpreferences = getSharedPreferences(LogActivity.MyPREFERENCES, Context.MODE_PRIVATE);
		uid=sharedpreferences.getString(KEY_UID, "brak");
		sid=sharedpreferences.getString(KEY_SID, "brak");
		Intent i = new Intent(this,TreningActivity.class);
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
    	moveTaskToBack(true); 
    	MenuActivity.this.finish();
    }
    
    
}

