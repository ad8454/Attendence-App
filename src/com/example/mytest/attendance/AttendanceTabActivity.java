package com.example.mytest.attendance;

import com.example.mytest.R;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class AttendanceTabActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.attendance_tab_disp);
		
		TabHost th = getTabHost();	
		
		TabSpec lectureSpec = th.newTabSpec("Lectures");        
        lectureSpec.setIndicator("",getResources().getDrawable(R.drawable.ic_menu_agenda));
        Intent lectureIntent = new Intent(this, ViewAllDates.class);
        lectureIntent.putExtra("tName", getIntent().getStringExtra("tName"));
        lectureIntent.putExtra("semSec", getIntent().getStringExtra("semSec"));
        lectureIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        lectureSpec.setContent(lectureIntent);
        
        TabSpec markAttnSpec = th.newTabSpec("markAttn");        
        markAttnSpec.setIndicator("",getResources().getDrawable(R.drawable.ic_menu_allfriends));
        Intent markAttnIntent = new Intent(this, DispNames.class);
        markAttnIntent.putExtra("tName", getIntent().getStringExtra("tName"));
        markAttnIntent.putExtra("semSec", getIntent().getStringExtra("semSec"));
        markAttnIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        markAttnSpec.setContent(markAttnIntent);
        
        TabSpec moreSpec = th.newTabSpec("more");        
        moreSpec.setIndicator("",getResources().getDrawable(R.drawable.ic_menu_view));
        Intent moreIntent = new Intent(this, MoreMenu.class);
        moreIntent.putExtra("tName", getIntent().getStringExtra("tName"));
        moreIntent.putExtra("semSec", getIntent().getStringExtra("semSec"));
        moreIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        moreSpec.setContent(moreIntent);
        
        th.addTab(lectureSpec);
        th.addTab(markAttnSpec);
        th.addTab(moreSpec);
        
        th.setCurrentTab(1);												//causes tab 0 to load first :((
	}
}
