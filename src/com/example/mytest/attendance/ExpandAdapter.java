package com.example.mytest.attendance;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mytest.R;

public class ExpandAdapter extends BaseExpandableListAdapter{
	
	private Context context;
	private ArrayList<ExpandGroup> groups;
	
	public ExpandAdapter(Context c , ArrayList<ExpandGroup> g){
		context = c;
		groups = g;		
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		ArrayList<ExpandChild> chList = groups.get(groupPosition).getItems();
		return chList.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		final ExpandGroup eGroup = (ExpandGroup) getGroup(groupPosition);
		ExpandChild eChild = (ExpandChild) getChild(groupPosition , childPosition);
		if(convertView == null){
			LayoutInflater lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = lInflater.inflate(R.layout.expand_child, null);
		}
		TextView tViewC = (TextView) convertView.findViewById(R.id.tViewChild);
		tViewC.setText(eChild.getName());
		TextView tViewT = (TextView) convertView.findViewById(R.id.tViewTag);
		tViewT.setText(eChild.getTag());	
		ImageView more = (ImageView) convertView.findViewById(R.id.more);		
		more.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, FullAttendance.class);
				intent.putExtra("reg", eGroup.getRno());
				intent.putExtra("name", eGroup.getName());
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		ArrayList<ExpandChild> chList = groups.get(groupPosition).getItems();
		return chList.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return groups.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		final ExpandGroup eGroup = (ExpandGroup) getGroup(groupPosition);
		if(convertView == null){
			LayoutInflater lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = lInflater.inflate(R.layout.expand_group, null);
		}
		
		TextView tViewG = (TextView) convertView.findViewById(R.id.tViewGroup);
		tViewG.setText(eGroup.getName());
		TextView tViewR = (TextView) convertView.findViewById(R.id.tViewRno);
		tViewR.setText(eGroup.getRno()); 
		final CheckBox cb = (CheckBox) convertView.findViewById(R.id.chkBox);
		cb.setChecked(eGroup.getChkBox());Log.d("0!"+groupPosition,""+eGroup.getChkBox());
		final int x = groupPosition;
		cb.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if (cb.isChecked())
				{	eGroup.setChkBox(true);Log.d("0!"+x,"yo");}
				else
				{	eGroup.setChkBox(false);Log.d("0!"+x,"noyo");}
			}
		});
		if(groupPosition % 2 == 0)
			convertView.setBackgroundColor(Color.parseColor("#f4f4f4"));
		else
			convertView.setBackgroundColor(Color.parseColor("#f8f8f8"));
		
		return convertView;
	}
	
	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}
