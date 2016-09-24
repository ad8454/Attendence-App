package com.example.mytest.attendance;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.mytest.MainActivity;
import com.example.mytest.R;

public class DispNames extends Activity {
	
	protected String tName;
	protected ArrayList<ExpandGroup> listG = new ArrayList<ExpandGroup>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dispnames);
		
		Intent intent = getIntent();		
		tName = intent.getStringExtra("tName");
		new DispBG().execute(tName);		
	}

	private class DispBG extends AsyncTask<String,Void,Void>{
		
		@Override
		protected Void doInBackground(String... params) {
			MyDB localdb = new MyDB(getBaseContext(), "empcode"+getSharedPreferences("login_data",MODE_PRIVATE).getString("username", "imposter")+".db", null, 1);
			Cursor c = localdb.getStudentDetails(params[0]);
	
	    	ArrayList<ExpandChild> listC;
	    	
	    	if (c != null && c.moveToFirst())
		    {
	    		Cursor cAttended = localdb.getClassesAttended(tName);
	    		if(cAttended!= null && cAttended.moveToFirst())
	    		do
	    		{
	    			String totalClasses = cAttended.getString(cAttended.getColumnIndex("tot"));
	    			if(totalClasses == null)
	    				totalClasses = "0";
		    		ExpandGroup eg = new ExpandGroup();
		            listC = new ArrayList<ExpandChild>();
	
		            String sreg = c.getString(c.getColumnIndex("sreg"));
	    			eg.setRno(sreg);
	    			eg.setName(c.getString(c.getColumnIndex("sname")));
	    			eg.setChkBox(false);
		            
		            ExpandChild ec = new ExpandChild();
		            ec.setTag("Classes Attended:");
		            String cAttendance = cAttended.getString(cAttended.getColumnIndex("reg"+sreg));
		            if(cAttendance == null)
		            {	
		            	cAttendance = "0";
		            	ec.setName(cAttendance+" out of "+totalClasses+"     0%");
		            }
		            else
		            	ec.setName(cAttendance+" out of "+totalClasses+"     "+(Integer.parseInt(cAttendance)*100/Integer.parseInt(totalClasses))+"%");
		           
		            listC.add(ec);
		            
		            eg.setItems(listC);
		            listG.add(eg);
		    	}
	    		while(c.moveToNext());	
		    }
	    	else
	    	{
	    		Log.d("errror","errorr");	//handle errors!!
	    	}
	    	
        	localdb.close();
        	
			return null;
		}
		
		protected void onPostExecute(Void v){
			
			final ExpandableListView elv = (ExpandableListView) findViewById(R.id.expandableListView1);			
			final ExpandAdapter eAdapter = new ExpandAdapter(DispNames.this, listG);
			elv.setAdapter(eAdapter);
			
			final CheckBox cb = (CheckBox) findViewById(R.id.selAll);
			cb.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					for(int i=0; i<eAdapter.getGroupCount(); i++){
						if(cb.isChecked())
							listG.get(i).setChkBox(true);
						else
							listG.get(i).setChkBox(false);
					}
					elv.setAdapter(eAdapter);
				}
			});
			
			final Button b = (Button) findViewById(R.id.saveAttn);
			b.setOnClickListener(new OnClickListener() {

				ArrayList<String> present = new ArrayList<String>();
				
				@SuppressWarnings("unchecked")
				@Override
				public void onClick(View v) {
					b.setEnabled(false);
					for(int i=0;i<eAdapter.getGroupCount();i++)
					{
						if(listG.get(i).getChkBox())
							present.add("1");
						else
							present.add("0");
					}
					
					new MarkAttnBG().execute(present);
				}
			});
		}
	}
	
	private class MarkAttnBG extends AsyncTask<ArrayList<String>, Void, Void>{
		
		@Override
		protected Void doInBackground(ArrayList<String>...params) {
			MyDB localdb = new MyDB(getBaseContext(), "empcode"+getSharedPreferences("login_data",MODE_PRIVATE).getString("username", "imposter")+".db", null, 1);
			localdb.markAttendance(tName, params[0]);
        	localdb.close();
			return null;
		}
		
		protected void onPostExecute(Void v){
			Toast.makeText(DispNames.this, "   Attendance Saved   ", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(DispNames.this, MainActivity.class);
			startActivity(intent);
			finish();
		}
	}
}