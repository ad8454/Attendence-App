package com.example.mytest.attendance;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mytest.R;

public class ViewAllDates extends FragmentActivity {
	
	private static String id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.view_all_class_list);
		TextView tv = (TextView) findViewById(R.id.viewAllCtv);
		tv.setText(getIntent().getStringExtra("semSec")+"   Lectures"); 
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		customOnResume();
	}
	
	public void customOnResume(){
		new ViewAllBG().execute(getIntent().getStringExtra("tName"));
	}
	
	private class ViewAllBG extends AsyncTask<String, Void, Void>{

		protected ArrayList<ViewAllData> list = new ArrayList<ViewAllData>();
		
		@Override
		protected Void doInBackground(String... params) {
			
			MyDB localdb = new MyDB(getBaseContext(), "empcode"+getSharedPreferences("login_data",MODE_PRIVATE).getString("username", "imposter")+".db", null, 1);
			Cursor c = localdb.getDates(params[0]);
			int i=c.getCount();
			if(c!= null && c.moveToLast())
    		do
    		{
    			ViewAllData vad = new ViewAllData();
    			vad.setDate(c.getString(c.getColumnIndex("date")));
    			vad.setIndex(""+(i--)+".");
    			vad.setId(c.getString(c.getColumnIndex("id")));
    			Log.d(c.getString(c.getColumnIndex("id")),c.getString(c.getColumnIndex("date")));
    			list.add(vad);
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
			ListView lv = (ListView) findViewById(R.id.viewAllListView);			
			final ViewAllAdapter vaAdapter = new ViewAllAdapter(ViewAllDates.this, list);
			lv.setAdapter(vaAdapter);
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

			        ViewAllData vad = new ViewAllData();
					vad = list.get(position);				
					DialogList dl = new DialogList();
					dl.setParams(vad.getDate(), vad.getId(), getIntent().getStringExtra("tName"));
					dl.show(getSupportFragmentManager(), null);
				}
			});
		}
	}

	public static class DialogList extends DialogFragment {
		
		private String fragDate;
		private String tName;
		
		public void setParams(String d, String i, String t){
			fragDate = d;
			id = i;
			tName = t;
		}
		
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        String items[] = {"View Attendance" , "Edit Date" , "Delete"};
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setTitle(fragDate).setItems(items, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int i) {
					if(i == 0){
						Log.d("log",id);
						Intent intent = new Intent(getActivity(), FullDateAttendance.class);
						intent.putExtra("id", id);
						intent.putExtra("date", fragDate);
						intent.putExtra("tName", tName);
						startActivity(intent);
					}
					else
						((ViewAllDates) getActivity()).callSecondFrag(i);						
				}
			});

	        return builder.create();
	    }
	}
	
	private void callSecondFrag(int i){
		if(i == 1){
			DialogFragment newFragment = new PickDate();
	        newFragment.show(getSupportFragmentManager(), null);
		}
		else if(i == 2){
			ConfirmDelete cd = new ConfirmDelete();
			cd.show(getSupportFragmentManager(), null);
		}
	}
	
	public static class ConfirmDelete extends DialogFragment{
		
		@Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setTitle("Delete?")
	        		.setMessage("This will erase all the attendance taken for the day.")
	        		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	        			public void onClick(DialogInterface dialog, int id) {
	        				goDelete();
	        			}
	        		})
	        		.setNegativeButton("No", new DialogInterface.OnClickListener() {
	        			public void onClick(DialogInterface dialog, int id) {
	                	   
	        			}
	        		});
	        return builder.create();
	    }
		
		public void goDelete(){
			new DeleteDateBG().execute();
			((ViewAllDates) getActivity()).customOnResume();
		}
		
		private class DeleteDateBG extends AsyncTask<Void, Void, Void>{

			@Override
			protected Void doInBackground(Void... params) {
				
				MyDB localdb = new MyDB(getActivity().getBaseContext(), "empcode"+getActivity().getBaseContext().getSharedPreferences("login_data",MODE_PRIVATE).getString("username", "imposter")+".db", null, 1);
				localdb.deleteDate(id, getActivity().getIntent().getStringExtra("tName"));
	        	localdb.close();
				return null;
			}
		}		
	}	
	
	public static class PickDate extends DialogFragment implements DatePickerDialog.OnDateSetListener{
		
		private String newDate;
		
		public PickDate(){
			getActivity();
		}
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Calendar calendar = Calendar.getInstance();
			int yy = calendar.get(Calendar.YEAR);
			int mm = calendar.get(Calendar.MONTH);
			int dd = calendar.get(Calendar.DAY_OF_MONTH);
			DatePickerDialog dpd = new DatePickerDialog(getActivity(), this, yy, mm, dd);
			dpd.setTitle("Edit Date");
			return dpd; 
		}
		
		public void onDateSet(DatePicker view, int yy, int mm, int dd) {			
			mm++;
			if(dd<10)
				newDate = "0"+dd+"/";
			else
				newDate = dd+"/";
			
			if(mm<10)
				newDate += "0"+mm+"/"+yy;
			else
				newDate += mm+"/"+yy;
			

			new EditDateBG().execute();
			Log.d("date", newDate);
		
			((ViewAllDates) getActivity()).customOnResume();
		}
		
		private class EditDateBG extends AsyncTask<Void, Void, Void>{

			@Override
			protected Void doInBackground(Void... params) {
				
				MyDB localdb = new MyDB(getActivity().getBaseContext(), "empcode"+getActivity().getBaseContext().getSharedPreferences("login_data",MODE_PRIVATE).getString("username", "imposter")+".db", null, 1);
				localdb.modifyDate(id, getActivity().getIntent().getStringExtra("tName"), newDate);
	        	localdb.close();
				return null;
			}
		}		
	}	
}