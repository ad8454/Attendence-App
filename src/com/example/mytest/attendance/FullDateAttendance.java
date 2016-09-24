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

public class FullDateAttendance extends Activity {
	
	private ArrayList<String> reg = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.full_date_attn);
		Intent intent = getIntent(); 
		TextView tv = (TextView) findViewById(R.id.fullDateName);
		tv.setText(intent.getStringExtra("date"));
		new FullDateAttnBG().execute(getIntent().getStringExtra("id"), getIntent().getStringExtra("tName"));
	}
	
	private class FullDateAttnBG extends AsyncTask<String, Void, Void>{

		protected ArrayList<FullDateAttnData> list = new ArrayList<FullDateAttnData>();
		
		@Override
		protected Void doInBackground(String... params) {
			
			MyDB localdb = new MyDB(getBaseContext(), "empcode"+getSharedPreferences("login_data",MODE_PRIVATE).getString("username", "imposter")+".db", null, 1);
			Cursor c = localdb.getFullAttendanceById(Integer.parseInt(params[0]), params[1]);
			Cursor cName = localdb.getStudentDetails(params[1]);
			if(c.getCount() == 1 && c.moveToFirst() && cName!= null && cName.moveToFirst())
    		do
    		{
    			String tReg = cName.getString(cName.getColumnIndex("sreg"));
    			reg.add(tReg);
    			FullDateAttnData fdad = new FullDateAttnData();
    			fdad.setName(tReg+"   "+cName.getString(cName.getColumnIndex("sname")));
    			if(c.getString(c.getColumnIndex("reg"+tReg)).equals("1"))
    				fdad.setIsPresent(true);
    			else
    				fdad.setIsPresent(false);
    			list.add(fdad);
    		}
    		while(cName.moveToNext());
			else
			{
				Log.d("error","error");			//handle errors!!
			}
			localdb.close();
			return null;
		}
		
		protected void onPostExecute(Void v){

			ListView lv = (ListView) findViewById(R.id.fullDateListView);			
			final FullDateAttnAdapter fdaAdapter = new FullDateAttnAdapter(FullDateAttendance.this, list);
			lv.setAdapter(fdaAdapter);
			
			final Button b = (Button) findViewById(R.id.fullDateSave);
			if(fdaAdapter.getCount()==0)
				b.setEnabled(false);
			
			b.setOnClickListener(new OnClickListener() {

				ArrayList<String> present = new ArrayList<String>();

				@SuppressWarnings("unchecked")
				@Override
				public void onClick(View v) {
					b.setEnabled(false);
					for(int i = 0; i<fdaAdapter.getCount(); i++)
					{
						if(list.get(i).getIsPresent())
							present.add("1");
						else
							present.add("0");
					}
					new FullDateAttnSaveBG().execute(present);
				}
			});			
		}		
	}
	
	private class FullDateAttnSaveBG extends AsyncTask<ArrayList<String>, Void, Void>{
		
		@Override
		protected Void doInBackground(ArrayList<String>...params) {
			MyDB localdb = new MyDB(getBaseContext(), "empcode"+getSharedPreferences("login_data",MODE_PRIVATE).getString("username", "imposter")+".db", null, 1);
			localdb.modifyAttendanceById(reg, params[0], getIntent().getStringExtra("id"), getIntent().getStringExtra("tName"));
        	localdb.close();
			return null;
		}
		
		protected void onPostExecute(Void v){
			Toast.makeText(FullDateAttendance.this, "   Attendance Modified   ", Toast.LENGTH_LONG).show();
			finish();
		}
	}
	
}