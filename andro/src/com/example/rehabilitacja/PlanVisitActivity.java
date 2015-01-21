package com.example.rehabilitacja;
//http://pastebin.com/yNqtKAKR
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rehabilitacja.klasy.GridviewAdapter;
import com.example.rehabilitacja.klasy.UserFunctions;
import com.example.rehabilitacja.klasy.Wizyta;

public class PlanVisitActivity extends ActionBarActivity implements OnClickListener {
	private static final String tag = "MyCalendarActivity";
	
	private GridviewAdapter mAdapter;
	private GridView gridView;
	private ArrayList<String> listDays;
	private TextView currentMonth;
	private Button selectedDayMonthYearButton;
	private ImageView prevMonth;
	private ImageView nextMonth;
	private GridView calendarView;
	private GridCellAdapter adapter;
	private Calendar _calendar;
	@SuppressLint("NewApi")
	private int month, year;
	@SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi" })
	private final DateFormat dateFormatter = new DateFormat();
	private static final String dateTemplate = "MMMM yyyy";
	
	private Context context = this;
	private String error_msg = null;
	private boolean error = false;
	private static String KEY_UID = "uid";
    private static String KEY_SID = "sid";
    private String uid, sid;
	private static JSONObject jObj = null;
	private List<Wizyta> visitsList = new ArrayList<Wizyta>();
	private List<Wizyta> visitDaysInMonth;
	private Dialog new_dialog;
	private Button zaplanuj;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plan_visit);
		zaplanuj = (Button) findViewById(R.id.zaplanuj_wizytke);
		zaplanuj.setVisibility(View.INVISIBLE);
		Bundle bb = getIntent().getExtras();
		uid = bb.getString(KEY_UID);
		sid = bb.getString(KEY_SID);
		
		new nowyWatek().execute();
		
	}

	/**
	 * 
	 * @param month
	 * @param year
	 */
	private void setGridCellAdapterToDate(int month, int year) {
		adapter = new GridCellAdapter(getApplicationContext(),
				R.id.calendar_day_gridcell, month, year);
		_calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
		currentMonth.setText(DateFormat.format(dateTemplate,
				_calendar.getTime()));
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		if (v == prevMonth) {
			if (month <= 1) {
				month = 12;
				year--;
			} else {
				month--;
			}
			Log.d(tag, "Setting Prev Month in GridCellAdapter: " + "Month: "
					+ month + " Year: " + year);
			setGridCellAdapterToDate(month, year);
		}
		if (v == nextMonth) {
			if (month > 11) {
				month = 1;
				year++;
			} else {
				month++;
			}
			Log.d(tag, "Setting Next Month in GridCellAdapter: " + "Month: "
					+ month + " Year: " + year);
			setGridCellAdapterToDate(month, year);
		}

	}

	@Override
	public void onDestroy() {
		Log.d(tag, "Destroying View ...");
		super.onDestroy();
	}

	// Inner Class
	public class GridCellAdapter extends BaseAdapter implements OnClickListener {
		private final Context _context;

		private final List<String> list;
		private static final int DAY_OFFSET = 1;
		private final String[] weekdays = new String[] { "Sun", "Mon", "Tue",
				"Wed", "Thu", "Fri", "Sat" };
		private final String[] months = { "January", "February", "March",
				"April", "May", "June", "July", "August", "September",
				"October", "November", "December" };
		private final int[] daysOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30,
				31, 30, 31 };
		private int daysInMonth;
		private int currentDayOfMonth;
		private int currentWeekDay;
		private Button gridcell;
		private TextView num_events_per_day;
		private final HashMap<String, Integer> eventsPerMonthMap;

		// Days in Current Month
		public GridCellAdapter(Context context, int textViewResourceId,
				int month, int year) {
			super();
			this._context = context;
			this.list = new ArrayList<String>();
	
			Calendar calendar = Calendar.getInstance();
			setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
			setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
			
			Log.e("sss",Integer.toString(month) + " " + Integer.toString(year));
			visitDaysInMonth = new ArrayList<Wizyta>(); 
			for(int i=0; i<visitsList.size();i++){
				if(visitsList.get(i).getYear() == year && visitsList.get(i).getMonth() == month){
					visitDaysInMonth.add(visitsList.get(i));
				}
			}
			printMonth(month, year);

			// Find Number of Events
			eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
		}

		private String getMonthAsString(int i) {
			return months[i];
		}

		@SuppressWarnings("unused")
		private String getWeekDayAsString(int i) {
			return weekdays[i];
		}

		private int getNumberOfDaysOfMonth(int i) {
			return daysOfMonth[i];
		}

		public String getItem(int position) {
			return list.get(position);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		/**
		 * Prints Month
		 * 
		 * @param mm
		 * @param yy
		 */
		private void printMonth(int mm, int yy) {
			int trailingSpaces = 0;
			int daysInPrevMonth = 0;
			int prevMonth = 0;
			int prevYear = 0;
			int nextMonth = 0;
			int nextYear = 0;

			int currentMonth = mm - 1;
			//String currentMonthName = getMonthAsString(currentMonth);
			daysInMonth = getNumberOfDaysOfMonth(currentMonth);

			GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);

			if (currentMonth == 11) {
				prevMonth = currentMonth - 1;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				nextMonth = 0;
				prevYear = yy;
				nextYear = yy + 1;
			
			} else if (currentMonth == 0) {
				prevMonth = 11;
				prevYear = yy - 1;
				nextYear = yy;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				nextMonth = 1;
	
			} else {
				prevMonth = currentMonth - 1;
				nextMonth = currentMonth + 1;
				nextYear = yy;
				prevYear = yy;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
			}

			int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
			trailingSpaces = currentWeekDay;

			if (cal.isLeapYear(cal.get(Calendar.YEAR)))
				if (mm == 2)
					++daysInMonth;
				else if (mm == 3)
					++daysInPrevMonth;

			// Trailing Month days
			for (int i = 0; i < trailingSpaces; i++) {
				list.add(String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET)
		        + i) + "-GREY" + "-" + getMonthAsString(prevMonth) + "-" + prevYear);
			}

			// Current Month Days
			int size = visitDaysInMonth.size();
			if (size > 0){
				for (int i = 1; i <= daysInMonth; i++) {
					for(int j=0; j < size;j++){
						if (i == getCurrentDayOfMonth() && i != visitDaysInMonth.get(j).getDay()) {
							list.add(String.valueOf(i) + "-BLUE" + "-"
									+ getMonthAsString(currentMonth) + "-" + yy);
							break;
						}
						else if (i == getCurrentDayOfMonth() && i == visitDaysInMonth.get(j).getDay()) {
							list.add(String.valueOf(i) + "-BLUEe" + "-"
									+ getMonthAsString(currentMonth) + "-" + yy);
							break;
						}
						else if (i != getCurrentDayOfMonth() && i == visitDaysInMonth.get(j).getDay()) {
							list.add(String.valueOf(i) + "-WHITEe" + "-"
									+ getMonthAsString(currentMonth) + "-" + yy);
							break;
						}
						if (j == visitDaysInMonth.size() - 1) {
							list.add(String.valueOf(i) + "-WHITE" + "-"
									+ getMonthAsString(currentMonth) + "-" + yy);
						}
					}
				}
			}
			else{
				for (int i = 1; i <= daysInMonth; i++) {
					if (i == getCurrentDayOfMonth()) {
						list.add(String.valueOf(i) + "-BLUE" + "-"
								+ getMonthAsString(currentMonth) + "-" + yy);
					}
					else{
						list.add(String.valueOf(i) + "-WHITE" + "-"
								+ getMonthAsString(currentMonth) + "-" + yy);
					}
				}
			}

			// Leading Month days
			for (int i = 0; i < list.size() % 7; i++) {
				list.add(String.valueOf(i + 1) + "-GREY" + "-"
						+ getMonthAsString(nextMonth) + "-" + nextYear);
			}
		}

		/**
		 * NOTE: YOU NEED TO IMPLEMENT THIS PART Given the YEAR, MONTH, retrieve
		 * ALL entries from a SQLite database for that month. Iterate over the
		 * List of All entries, and get the dateCreated, which is converted into
		 * day.
		 * 
		 * @param year
		 * @param month
		 * @return
		 */
		private HashMap<String, Integer> findNumberOfEventsPerMonth(int year,
				int month) {
			HashMap<String, Integer> map = new HashMap<String, Integer>();

			return map;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			if (row == null) {
				LayoutInflater inflater = (LayoutInflater) _context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.screen_gridcell, parent, false);
			}

			// Get a reference to the Day gridcell
			gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);
			gridcell.setOnClickListener(this);

			// ACCOUNT FOR SPACING

			String[] day_color = list.get(position).split("-");
			String theday = day_color[0];
			String themonth = day_color[2];
			String theyear = day_color[3];
			if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null)) {
				if (eventsPerMonthMap.containsKey(theday)) {
					num_events_per_day = (TextView) row
							.findViewById(R.id.num_events_per_day);
					Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
					num_events_per_day.setText(numEvents.toString());
				}
			}

			// Set the Day GridCell
			gridcell.setText(theday);
			gridcell.setTag(theday + "-" + themonth + "-" + theyear);


			if (day_color[1].equals("GREY")) {
				gridcell.setTextColor(getResources()
						.getColor(R.color.lightgray));
			}
			if (day_color[1].equals("WHITE")) {
				gridcell.setTextColor(getResources().getColor(
						R.color.black));
			}
			if (day_color[1].equals("BLUE")) {
				gridcell.setTextColor(getResources().getColor(R.color.orrange));
				gridcell.setTypeface(null, Typeface.BOLD);
			}
			if (day_color[1].equals("BLUEe")) {
				gridcell.setTextColor(getResources().getColor(R.color.orrange));
				gridcell.setTypeface(null, Typeface.BOLD);
				gridcell.setBackgroundColor(Color.YELLOW);
			}
			if (day_color[1].equals("WHITEe")) {
				gridcell.setTextColor(getResources().getColor(R.color.orrange));
				gridcell.setTypeface(null, Typeface.BOLD);
				gridcell.setBackgroundColor(Color.YELLOW);
			}
			return row;
		}

		@Override
		public void onClick(View view) {
			String date_month_year = (String) view.getTag();
			String[] tablica = date_month_year.split("-");
			int miesiac = 0;
			String data = tablica[0] + "-";
            for(int i=0;i<months.length;i++){
            	if(months[i].equals(tablica[1])){
            		miesiac = i+1;
            		data = data + Integer.toString(i+1) + "-";
            		break;
            	}
            }
            
            data = data + tablica[2];
            if(visitDaysInMonth.get(0).getMonth() == miesiac){
            	for(int j=0;j<visitDaysInMonth.size();j++){
            		if(Integer.parseInt(tablica[0]) == visitDaysInMonth.get(j).getDay()){
            			String hour = Integer.toString(visitDaysInMonth.get(j).getGodz());
            			if(hour.length()<2) hour = "0" + hour;
            			String minutes = Integer.toString(visitDaysInMonth.get(j).getMin());
            			if(minutes.length()<2) minutes = "0" + minutes;
            			newDialog(data, hour +":"+minutes, visitDaysInMonth.get(j).getInfo());
            			break;
            		}
            	}
            }
			selectedDayMonthYearButton.setText("Wybrano Date: " + data);
			zaplanuj.setVisibility(View.VISIBLE);
			Log.e("Selected date", date_month_year);
		}

		public int getCurrentDayOfMonth() {
			return currentDayOfMonth;
		}

		private void setCurrentDayOfMonth(int currentDayOfMonth) {
			this.currentDayOfMonth = currentDayOfMonth;
		}

		public void setCurrentWeekDay(int currentWeekDay) {
			this.currentWeekDay = currentWeekDay;
		}

		public int getCurrentWeekDay() {
			return currentWeekDay;
		}
	}
	
	public void prepareList(){
		listDays = new ArrayList<String>();
		 
        listDays.add("Nd");
        listDays.add("Pn");
        listDays.add("Wt");
        listDays.add("Sr");
        listDays.add("Cz");
        listDays.add("Pt");
        listDays.add("Sb");
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
			String odpowiedz = UserFunctions.getVisitPlan(uid, sid);
			
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
					Log.e("tutt","tu je ok");
					visitsList.add(new Wizyta(jObj.getString("data"), jObj.getString("uwagi")));
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
				prepareCalendar();
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
	
	private void prepareCalendar(){
		prepareList();
		_calendar = Calendar.getInstance(Locale.getDefault());
		month = _calendar.get(Calendar.MONTH) + 1;
		year = _calendar.get(Calendar.YEAR);

		selectedDayMonthYearButton = (Button) this
				.findViewById(R.id.selectedDayMonthYear);
		selectedDayMonthYearButton.setText("Wybrano Date: ");

		prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
		prevMonth.setOnClickListener(this);

		currentMonth = (TextView) this.findViewById(R.id.currentMonth);
		currentMonth.setText(DateFormat.format(dateTemplate,
				_calendar.getTime()));

		nextMonth = (ImageView) this.findViewById(R.id.nextMonth);
		nextMonth.setOnClickListener(this);

		calendarView = (GridView) this.findViewById(R.id.calendar);
		
		 
        // prepared arraylist and passed it to the Adapter class
        mAdapter = new GridviewAdapter(this,listDays);
 
        // Set custom adapter to gridview
        gridView = (GridView) findViewById(R.id.gridView1);
        gridView.setAdapter(mAdapter);
 

		// Initialised
		adapter = new GridCellAdapter(getApplicationContext(),
				R.id.calendar_day_gridcell, month, year);
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);
	}
	
	private void newDialog(String data, String godzina1, String opis){
		new_dialog = new Dialog(context);
		new_dialog.setTitle(data);
		new_dialog.setContentView(R.layout.calendar_event);
		TextView godzina = (TextView) new_dialog.findViewById(R.id.godzina);
		godzina.setText(godzina1);
		TextView desc = (TextView) new_dialog.findViewById(R.id.opis);
		desc.setText(opis);
		Button ok = (Button) new_dialog.findViewById(R.id.close);
    	new_dialog.show();
    	ok.setOnClickListener(new View.OnClickListener(){
        	@Override
            public void onClick(View v) {
        		new_dialog.dismiss();
        	}
    	});
	}
 }