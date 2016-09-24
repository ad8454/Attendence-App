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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytest.R;

public class FullAttendance extends Activity {

	protected String reg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.full_attendance);
		Intent intent = getIntent(); 
		reg = intent.getStringExtra("reg");
		TextView tv = (TextView) findViewById(R.id.fullAttnName);
		tv.setText(intent.getStringExtra("name"));
		new FullAttnBG().execute(reg);
	}
	
	private class FullAttnBG extends AsyncTask<String, Void, Void>{

		protected ArrayList<FullAttnData> list = new ArrayList<FullAttnData>();
		
		@Override
		protected Void doInBackground(String... params) {
			
			MyDB localdb = new MyDB(getBaseContext(), "empcode"+getSharedPreferences("login_data",MODE_PRIVATE).getString("username", "imposter")+".db", null, 1);
			Cursor c = localdb.getFullAttendanceByReg(params[0]);
			if(c!= null && c.moveToLast())
    		do
    		{
    			FullAttnData fad = new FullAttnData();
    			fad.setDate(c.getString(c.getColumnIndex("date")));
    			if(c.getString(c.getColumnIndex("reg"+params[0])).equals("1"))
    				fad.setIsPresent(true);
    			else
    				fad.setIsPresent(false);
    			list.add(fad);
    		}
    		while(c.moveToPrevious());
			else
			{
				Log.d("error","error");			//handle errors!!
			}
			localdb.close();
			return null;
		}
		
		protected void onPostExecute(Void v){

			ListView lv = (ListView) findViewById(R.id.fullAttnListView);			
			final FullAttnAdapter faAdapter = new FullAttnAdapter(FullAttendance.this, list);
			lv.setAdapter(faAdapter);
			
			final Button b = (Button) findViewById(R.id.fullAttnSave);
			if(faAdapter.getCount()==0)
				b.setEnabled(false);
			
			b.setOnClickListener(new OnClickListener() {

				ArrayList<String> present = new ArrayList<String>();

				@SuppressWarnings("unchecked")
				@Override
				public void onClick(View v) {
					b.setEnabled(false);
					for(int i = faAdapter.getCount()-1; i>=0; i--)
					{
						if(list.get(i).isModified)
						{
							if(list.get(i).getIsPresent())
								present.add("1");
							else
								present.add("0");
						}
						else
							present.add("2");
					}
					new FullAttnSaveBG().execute(present);
				}
			});			
		}		
	}
	
	private class FullAttnSaveBG extends AsyncTask<ArrayList<String>, Void, Void>{
		
		@Override
		protected Void doInBackground(ArrayList<String>...params) {
			MyDB localdb = new MyDB(getBaseContext(), "empcode"+getSharedPreferences("login_data",MODE_PRIVATE).getString("username", "imposter")+".db", null, 1);
			localdb.modifyAttendanceByReg(reg, params[0]);
        	localdb.close();
			return null;
		}
		
		protected void onPostExecute(Void v){
			Toast.makeText(FullAttendance.this, "   Attendance Modified   ", Toast.LENGTH_LONG).show();
			finish();
		}
	}
	
}