package com.example.mytest.attendance;

import java.util.ArrayList;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mytest.R;

public class AttendanceMain extends FragmentActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.attendance_main);
		new ListViewBG().execute();
	}
	
	private class ListViewBG extends AsyncTask<Void, Void, ArrayList<String>>{
		
		protected Cursor c;
		
		@Override
		protected ArrayList<String> doInBackground(Void... params) {
			
			MyDB localdb = new MyDB(getBaseContext(), "empcode"+getSharedPreferences("login_data",MODE_PRIVATE).getString("username", "imposter")+".db", null, 1);
			c = localdb.getSubject();
			ArrayList<String>s = new ArrayList<String>();
			
			if (c != null && c.moveToFirst()) {
				do {
				      s.add("  "+c.getString(c.getColumnIndex("sem"))+" "+c.getString(c.getColumnIndex("sec"))+"  -  "+c.getString(c.getColumnIndex("subname")));
				}while (c.moveToNext());	             
	       }
			localdb.close();
			return s;
		}
		
		protected void onPostExecute(ArrayList<String> als){

			ArrayAdapter<String> arrAdapter = new ArrayAdapter<String>(getBaseContext(), R.layout.disp_class_name, als);
			
			ListView lv = (ListView) findViewById(R.id.attnMainListView1);
			lv.setAdapter(arrAdapter);
			
			lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View v, int i, long l) {
					if (c != null && c.moveToFirst()) {
						while (i>0) {
							c.moveToNext();
							i--;
						}
						Intent intent = new Intent(AttendanceMain.this,AttendanceTabActivity.class);
						intent.putExtra("tName", c.getString(c.getColumnIndex("sec"))+c.getString(c.getColumnIndex("sem")));
						intent.putExtra("semSec", c.getString(c.getColumnIndex("sem"))+" "+c.getString(c.getColumnIndex("sec")));
						startActivity(intent);
					}
				}
			});			
		}
	}
}







/*bon = (Button) findViewById(R.id.login);

bon.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		DialogList dl = new DialogList();
		dl.initItems(3);
		dl.addItems("This"); dl.addItems("Is"); dl.addItems("Working");
		//dl.show(new FragmentManager, null);
		dl.show(getSupportFragmentManager(), null);
	}
});*/

	
	/*public static class DialogList extends DialogFragment {
		
		public DialogList(){
			getActivity();
		}

		private String items[];
		private int i;
		
		public void initItems(int no){
			items = new String[no];
			i = 0;
		}
		
		public void addItems(String s){
			items[i++] = s;
		}
		
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        final ArrayList<Integer> selectedItems = new ArrayList<Integer>();
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setTitle("Select Class").setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
	                   @Override
	                   public void onClick(DialogInterface dialog, int check, boolean isChecked) {
	                       if (isChecked)
	                           selectedItems.add(check);
	                       else if (selectedItems.contains(check)) 
	                           selectedItems.remove(Integer.valueOf(check));	                       
	                   }
	               })
	               .setPositiveButton("Add", new DialogInterface.OnClickListener() {
	                   @Override
	                   public void onClick(DialogInterface dialog, int id) {
	                	   ArrayList<String> itemsToAdd = new ArrayList<String>();
	                	   itemsToAdd.add("itemsToAdd");
	                	   
	                	   Iterator<Integer> it = selectedItems.iterator();
	                	   while(it.hasNext())	                	   
	                	       itemsToAdd.add(items[it.next()]);
	                	   
	                	   String param[] = itemsToAdd.toArray(new String[itemsToAdd.size()]);
	                	   new AttendanceMain().new NetBG().execute(param);
	                	   
	                   }
	               })
	               .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	                   @Override
	                   public void onClick(DialogInterface dialog, int id) {
	                      // ...
	                   }
	               });

	        return builder.create();
	    }
	}

}*/
