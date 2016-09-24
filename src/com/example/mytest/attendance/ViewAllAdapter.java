package com.example.mytest.attendance;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mytest.R;

public class ViewAllAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<ViewAllData> list;
	
	public ViewAllAdapter(Context c , ArrayList<ViewAllData> l){
		context = c;
		list = l;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final ViewAllData vaData = (ViewAllData) getItem(position);
		if(convertView == null){
			LayoutInflater lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = lInflater.inflate(R.layout.view_all_final_disp, null);
		}
		
		TextView tvIndex = (TextView) convertView.findViewById(R.id.viewAllfinalIndex);
		tvIndex.setText(vaData.getIndex());
		TextView tViewG = (TextView) convertView.findViewById(R.id.viewAllfinalDate);
		tViewG.setText(vaData.getDate()); 
		
		if(position % 2 == 0)
			convertView.setBackgroundColor(Color.parseColor("#f4f4f4"));
		else
			convertView.setBackgroundColor(Color.parseColor("#f8f8f8"));
		
		return convertView;		
	}
}