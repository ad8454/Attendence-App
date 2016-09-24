package com.example.mytest.attendance;

import java.util.ArrayList;

import com.example.mytest.R;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

public class FullDateAttnAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<FullDateAttnData> list;
	
	public FullDateAttnAdapter(Context c , ArrayList<FullDateAttnData> l){
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
	public View getView(int position, View convertView, ViewGroup parent) {

		final FullDateAttnData fdaData = (FullDateAttnData) getItem(position);
		if(convertView == null){
			LayoutInflater lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = lInflater.inflate(R.layout.fulldate_attn_disp, null);
		}
		
		TextView tViewG = (TextView) convertView.findViewById(R.id.fullDateAttnName);
		tViewG.setText(fdaData.getName()); 
		final ToggleButton tb = (ToggleButton) convertView.findViewById(R.id.fullDateTogglePresent);
		if(fdaData.getIsPresent())
			tb.setChecked(true);
		Log.d("0!"+position,""+fdaData.getIsPresent());
		
		tb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (tb.isChecked())	
					fdaData.setIsPresent(true);
				else
					fdaData.setIsPresent(false);
			}
		});
		if(position % 2 == 0)
			convertView.setBackgroundColor(Color.parseColor("#f4f4f4"));
		else
			convertView.setBackgroundColor(Color.parseColor("#f8f8f8"));
				
		return convertView;		
	}
}