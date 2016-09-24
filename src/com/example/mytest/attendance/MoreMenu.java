package com.example.mytest.attendance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.mytest.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MoreMenu extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.more_menu);
		
		TextView tv = (TextView) findViewById(R.id.moreMenutv);
		tv.setText(getIntent().getStringExtra("semSec")+"   Details");

		List<Map<String, String>> listData = new ArrayList<Map<String, String>>();
		Map<String, String> map;
		
		map = new HashMap<String, String>(2);
	    map.put("title", "Sort By Attendance");
	    map.put("details", "Sort all the students in the class by their attendance.");
	    listData.add(map);
		
	    map = new HashMap<String, String>(2);
	    map.put("title", "Notify Class");
	    map.put("details", "Send a message to all students through the internet.");
	    listData.add(map);
	    
	    map = new HashMap<String, String>(2);
	    map.put("title", "Synchronize");
	    map.put("details", "Update attendance to the database through the internet.");
	    listData.add(map);
	    
		SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), listData, R.layout.more_menu_disp, new String[] {"title", "details"}, new int[] {R.id.moreMenuTitle, R.id.moreMenuDetails});		
		ListView lv = (ListView) findViewById(R.id.moreMenuListView);
		lv.setAdapter(simpleAdapter);
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int i, long l) {
					if(i==0)
					{
						Intent intent = new Intent(MoreMenu.this,SortAttn.class);
						intent.putExtra("tName",getIntent().getStringExtra("tName"));
						intent.putExtra("semSec", getIntent().getStringExtra("semSec"));
						startActivity(intent);
					}
				}			
		});		
	}
}