package com.example.mytest.attendance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.mytest.R;

public class SortAttn extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sort_attn);
		new SortBG().execute(getIntent().getStringExtra("tName"));
	}
		
	private class SortBG extends AsyncTask<String,Void,Void>{
		
		protected List<Map<String, String>> listData = new ArrayList<Map<String, String>>();
		
		@Override
		protected Void doInBackground(String... params) {
			String totalClasses = null;
			MyDB localdb = new MyDB(getBaseContext(), "empcode"+getSharedPreferences("login_data",MODE_PRIVATE).getString("username", "imposter")+".db", null, 1);
			Cursor c = localdb.getStudentDetails(params[0]);
			
	    	if (c != null && c.moveToFirst())
		    {
	    		Map<String, String> map;
	    		Cursor cAttended = localdb.getClassesAttended(params[0]);
	    		if(cAttended!= null && cAttended.moveToFirst())
	    		do
	    		{
	    			totalClasses = cAttended.getString(cAttended.getColumnIndex("tot"));
	    			if(totalClasses == null)
	    				totalClasses = "0";
	    			map = new HashMap<String, String>(2);
	    			String sreg = c.getString(c.getColumnIndex("sreg"));
	    		    map.put("name", sreg+"   "+(c.getString(c.getColumnIndex("sname"))));
	    		    
	    		    String cAttendance = cAttended.getString(cAttended.getColumnIndex("reg"+sreg));
		            if(cAttendance == null)	
		            	cAttendance = "0";
		            
		            map.put("attn", cAttendance);	
		            
	    		    listData.add(map);
	    		    
	    		}while(c.moveToNext());
		    }		    	
        	localdb.close();
        	
        	for(int i=0; i<listData.size()-1; i++)
        	{
        		for(int j=i+1; j<listData.size(); j++)
        		{
        			if(Integer.parseInt(listData.get(i).get("attn")) > Integer.parseInt(listData.get(j).get("attn")))
        			{
        				Map<String, String> temp = listData.get(j);
        				listData.set(j, listData.get(i));
        				listData.set(i, temp);
        			}
        		}
        		Map<String, String> temp = listData.get(i);
        		if(temp.get("attn") == "0")	    		            	
	            	temp.put("attn", "0  out of "+totalClasses+"     0%");
	            else	
	            	temp.put("attn", temp.get("attn")+" out of "+totalClasses+"     "+(Integer.parseInt(temp.get("attn"))*100/Integer.parseInt(totalClasses))+"%");
        	}
        	Map<String, String> temp = listData.get(listData.size()-1);
    		if(temp.get("attn") == "0")	    		            	
            	temp.put("attn", "0  out of "+totalClasses+"     0%");
            else	
            	temp.put("attn", temp.get("attn")+" out of "+totalClasses+"     "+(Integer.parseInt(temp.get("attn"))*100/Integer.parseInt(totalClasses))+"%");
        	
			return null;
		}
		
		protected void onPostExecute(Void v){
			SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), listData, R.layout.sort_attn_disp, new String[] {"name", "attn"}, new int[] {R.id.sortAttnName, R.id.sortAttnAttn});		
			ListView lv = (ListView) findViewById(R.id.sortAttnListView);
			lv.setAdapter(simpleAdapter);
		}
	}
}
