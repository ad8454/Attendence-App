package com.example.mytest.attendance;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.mytest.R;

public class FullAttnAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<FullAttnData> list;
	
	public FullAttnAdapter(Context c , ArrayList<FullAttnData> l){
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

		final FullAttnData faData = (FullAttnData) getItem(position);
		if(convertView == null){
			LayoutInflater lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = lInflater.inflate(R.layout.fullattn_disp, null);
		}
		
		TextView tViewG = (TextView) convertView.findViewById(R.id.fullAttnDate);
		tViewG.setText(faData.getDate()); 
		final ToggleButton tb = (ToggleButton) convertView.findViewById(R.id.togglePresent);
		if(faData.getIsPresent())
			tb.setChecked(true);
		Log.d("0!"+position,""+faData.getIsPresent());
		
		tb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				faData.isModified^=true;
				if (tb.isChecked())	
					faData.setIsPresent(true);
				else
					faData.setIsPresent(false);
			}
		});
		if(position % 2 == 0)
			convertView.setBackgroundColor(Color.parseColor("#f4f4f4"));
		else
			convertView.setBackgroundColor(Color.parseColor("#f8f8f8"));
		return convertView;		
	}
}