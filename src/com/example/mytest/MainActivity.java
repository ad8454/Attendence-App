package com.example.mytest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.example.mytest.attendance.AttendanceMain;

public class MainActivity extends Activity {

	protected Button attendance;
	//,timetable,notice,sync;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		attendance = (Button) findViewById(R.id.main_attendance);
		/*timetable = (LinearLayout) findViewById(R.id.timetable);
		notice = (LinearLayout) findViewById(R.id.notice);
		sync = (LinearLayout) findViewById(R.id.sync);*/
		
		attendance.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,AttendanceMain.class);					
				startActivity(intent);
			}
		});
	}
	
	/*private class NetBG extends AsyncTask<String, Void, String>{
		@Override
		protected String doInBackground(String... params) {
			
			return "";
			
			JSONObject json = new JSONObject();
			try {				
				json.put("user", 1);
			} catch (JSONException e) {
				Log.d("json", "put");
				e.printStackTrace();
			}
			
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
			HttpClient client = new DefaultHttpClient(httpParams);
			HttpPost req = new HttpPost("http://10.0.2.2/ict/api.php");//?json=%7B%22201%22:0,%22202%22:1,%22203%22:0%7D");
			Log.d("lests","asd");
			
			StringEntity entity = null;
			try {
				entity = new StringEntity(json.toString(), HTTP.UTF_8);
			} catch (UnsupportedEncodingException e1) {
				Log.d("here","watcha");
				e1.printStackTrace();
			}
			entity.setContentType("application/json");
			req.setEntity(entity);

			req.setHeader("json",json.toString());
			try {
				HttpResponse res = client.execute(req);
				HttpEntity hEntity = res.getEntity();
				Log.d("got","it");
				if(hEntity!=null)
				{
					String eu = EntityUtils.toString(hEntity);
					return eu;
				}
				else
					Log.d("entity","null");
				
			} catch (ConnectTimeoutException e) {
				Log.d("res","response");	
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "fail";
		}
		
		protected void onPostExecute(String eu){
			if(eu.equals("fail"))
			{
				Log.d("fail","fail");
				Toast.makeText(getApplicationContext(), "  Network Connection Unavailable  ", Toast.LENGTH_LONG).show();
			}
			else
			{
				if(!fExist)
				{
					Log.d("file","w");
					FileOutputStream outputStream;
					try {
						outputStream = openFileOutput("myTest", Context.MODE_PRIVATE);
						outputStream.write(eu.getBytes());
						outputStream.close();
					} catch (Exception e) {
						Log.d("exception","lookkk");
						e.printStackTrace();
					}
				}
				Intent intent = new Intent(MainActivity.this,DispNames.class);
				intent.putExtra("json", eu);
				if(fRead)
					intent.putExtra("file", true);					
				startActivity(intent);
			}
		}
	}*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}