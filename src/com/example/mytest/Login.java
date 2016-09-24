package com.example.mytest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.mytest.attendance.MyDB;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
	SharedPreferences pref;
	EditText user,pwd;
	Button bon;
	String login_data;
	String username,password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		pref=getSharedPreferences("login_data",MODE_PRIVATE);
		if(pref.getInt("login",0)==1)
		{
			Intent i = new Intent(Login.this, MainActivity.class);
			startActivity(i);
			finish();
		}
		setContentView(R.layout.login);
		user = (EditText) findViewById(R.id.user);
		pwd = (EditText) findViewById(R.id.pwd);
		bon = (Button) findViewById(R.id.login);
		
		bon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				username=user.getText().toString();
				MyDB localdb = new MyDB(getBaseContext(), "empcode"+username+".db", null, 1);	//check sharedpref if allowing multiple users
	        	localdb.createSubjectTable();
	        	localdb.createStudentTable();
	        	localdb.insertSubject("ICT 101", "Computer Architecture", "3", "A");
	        	localdb.insertSubject("ICT 302", "Database Systems", "5", "B");
	        	String[] temp = {"71","72","73","74","75","76","77","78","82","84","85"};
	        	String[] temp2 ={"Student One","Student Two","Student Three","Student Four","Student Five","Student Six","Student Seven","Student Eight","Student With A Very Long Name","Student With An Even Longer Name Than The Previous One","Student Eleven"};
	        	localdb.createAttendanceTable("A3", temp);
	        	localdb.insertStudentDetails(temp2, temp, "A3");
	        	getSharedPreferences("login_data", MODE_PRIVATE).edit().putString("username",username).putString("password",password).putInt("login", 1).commit();
	        	Intent i = new Intent(Login.this, MainActivity.class);
				startActivity(i);
				finish();
				
				
				/*bon.setEnabled(false);
				username=user.getText().toString();
				password=pwd.getText().toString();
				login_network lg=new login_network();
				lg.execute(username,password);*/
				
			}
		});
	}
	
	protected class login_network extends AsyncTask<String,Void,Boolean>
	{
		String username,password;
		HttpParams hParams;
		HttpClient hClient;
		HttpResponse response;  
		HttpEntity entity;
		JSONObject jobj;
		String validation;
		StringEntity sEntity;
		@Override
		protected Boolean doInBackground(String... params) {
			username=params[0];
			password=params[1];
		
			jobj=new JSONObject();
			try {
				jobj.put("user",2);				
				jobj.put("username",username);
				jobj.put("password",password);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			hParams=new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(hParams, 3000);
			hClient= new DefaultHttpClient(hParams);
	        String url = "http://10.0.2.2/ict/api.php";
	        HttpPost request=new HttpPost(url);
	       
	        try
	        {
	        	sEntity=new StringEntity(jobj.toString(),HTTP.UTF_8);
	        	sEntity.setContentType("application/json");
	        	request.setEntity(sEntity);
	        	response=hClient.execute(request);
	        	entity=response.getEntity();
	        	validation=EntityUtils.toString(entity);
	        	
	        	Log.d("first",validation);		        	
	        	
	        	JSONArray jarr = new JSONArray(validation); 
	        	jobj = new JSONObject();
	        	JSONObject jres;
	        	MyDB localdb = new MyDB(getBaseContext(), "empcode"+username+".db", null, 1);	//check sharedpref if allowing multiple users
	        	localdb.createSubjectTable();
	        	localdb.createStudentTable();
	        	for(int i=0; i<jarr.length(); i++)
	        	{
	        		jres = jarr.getJSONObject(i);
	        		jobj.put("user", 3);
	        		jobj.put("brcode", jres.getString("brcode"));
	        		jobj.put("sem", jres.getString("sem"));
	        		jobj.put("sec", jres.getString("sec"));
	        		
	        		localdb.insertSubject(jres.getString("subcode"), jres.getString("subname"), jres.getString("sem"), jres.getString("sec"));
		        	String tName = jres.getString("sec") + jres.getString("sem");	//store SecSem coz of sqlite table name restrictions
	        		sEntity=new StringEntity(jobj.toString(),HTTP.UTF_8);
		        	sEntity.setContentType("application/json");
		        	request.setEntity(sEntity);
		        	response=hClient.execute(request);
		        	entity=response.getEntity();
		        	validation=EntityUtils.toString(entity);
		        	
		        	Log.d("i:"+i,validation);
		        	
		        	JSONArray jname = new JSONArray(validation);
		        	String name[] = new String[jname.length()];
		        	String reg[] = new String[jname.length()];
		        	for(int j=0; j<jname.length(); j++)
		        	{
		        		jres = jname.getJSONObject(j);
		        		reg[j] = jres.getString("reg");
		        		name[j] = jres.getString("name");
		        		Log.d("j:"+j,""+reg[j]+" "+name[j]);
		        	}
		        	Log.d("tname",tName);
		        	localdb.createAttendanceTable(tName, reg);
		        	localdb.insertStudentDetails(name, reg, tName);		//tName used for unique sec-sem
	        	}
	        	localdb.close();
	        	return true;
	        }
	        catch(Exception e)
	        {
	        	e.printStackTrace();
	        }
	        return false;
		}
		protected void onPostExecute(Boolean b)
		{
			if(b.booleanValue()==true)
			{
				getSharedPreferences("login_data", MODE_PRIVATE).edit().putString("username",username).putString("password",password).putInt("login", 1).commit();
	        	Intent i = new Intent(Login.this, MainActivity.class);
				startActivity(i);
				finish();
			}
			else
			{
				Toast.makeText(Login.this, "Incorrect ID/Password", Toast.LENGTH_LONG).show();
				bon.setEnabled(true);
			}
		}
	}
}
