

package com.example.rehabilitacja.klasy;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.rehabilitacja.R;
/**
 * Klasa opisujaca adapter GridView zastosowany w kalendarzu
 * @author Kamil
 */
public class GridviewAdapter extends BaseAdapter
{
    private ArrayList<String> listDays;
    private Activity activity;
 
    public GridviewAdapter(Activity activity,ArrayList<String> listCountry) {
        super();
        this.listDays = listCountry;
        this.activity = activity;
    }
 
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listDays.size();
    }
 
    @Override
    public String getItem(int position) {
        // TODO Auto-generated method stub
        return listDays.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }
 
    public static class ViewHolder
    {
        public ImageView imgViewFlag;
        public TextView txtViewTitle;
    }
 
    @SuppressLint("InflateParams") @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder view;
        LayoutInflater inflator = activity.getLayoutInflater();
 
        if(convertView==null)
        {
            view = new ViewHolder();
            convertView = inflator.inflate(R.layout.gridview_row, null);
 
            view.txtViewTitle = (TextView) convertView.findViewById(R.id.textView1);
 
            convertView.setTag(view);
        }
        else
        {
            view = (ViewHolder) convertView.getTag();
        }
 
        view.txtViewTitle.setText(listDays.get(position));
 
        return convertView;
    }
}
