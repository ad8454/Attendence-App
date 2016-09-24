package com.example.mytest.attendance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDB extends SQLiteOpenHelper {

	public MyDB(Context context, String db, CursorFactory factory, int version) {
		super(context, db, null, version);

	}
	
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		Log.d("db","oncreate");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	
	public void createSubjectTable(){
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "CREATE TABLE IF NOT EXISTS subject ( id INTEGER PRIMARY KEY AUTOINCREMENT , subcode TEXT , subname TEXT , sem TEXT , sec TEXT );";
		db.execSQL(query);
		Log.d("db",query);
	}	
	
	public void insertSubject(String subcode, String subname, String sem, String sec){
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "INSERT INTO subject VALUES ( NULL , '"+subcode+"' , '"+subname+"' , '"+sem+"' , '"+sec+"' );";
		db.execSQL(query);
		Log.d("db",query);
	}
	
	public Cursor getSubject(){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(" SELECT subname , sem , sec FROM subject ", null);
		return c;
	}
	
	public void createAttendanceTable(String table, String reg[]){
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "CREATE TABLE IF NOT EXISTS "+table+" ( id INTEGER PRIMARY KEY AUTOINCREMENT , date TEXT ";
		for(int i=0; i<reg.length; i++)
		{
			query+=", reg"+reg[i]+" NUMERIC ";
		}
		query+=");";
		db.execSQL(query);
		Log.d("db",query);
	}
	
	public Cursor getClassesAttended(String tName){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cTemp = db.rawQuery("SELECT * from "+tName+" ; ", null);		// think of something better!!
		String reg[] = cTemp.getColumnNames();
		String query = "SELECT COUNT(id) AS tot , SUM("+reg[2]+") AS "+reg[2]+" ";	
		for(int i=3; i<reg.length; i++)
		{
			query+=", SUM("+reg[i]+") AS "+reg[i]+" ";
		}
		query+="FROM "+tName+" ;";		
		Log.d("db",query);
		Cursor c = db.rawQuery(query, null);
		return c;
	}
	
	@SuppressLint("SimpleDateFormat")
	public void markAttendance( String tName, ArrayList<String> als){
		SQLiteDatabase db = this.getWritableDatabase();
		Date d = new Date();
		String date = new SimpleDateFormat("dd/MM/yyyy").format(d);
		String query = "INSERT INTO "+tName+" VALUES ( NULL , '"+date+"' ";
		for(int i=0; i<als.size(); i++)
		{
			query+=", '"+als.get(i)+"' ";
		}
		query+=");";
		db.execSQL(query);	
		Log.d("db",query);
	}
	
	public void createStudentTable(){
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "CREATE TABLE IF NOT EXISTS student ( id INTEGER PRIMARY KEY AUTOINCREMENT , sname TEXT , sreg TEXT , sclass TEXT );";
		db.execSQL(query);
		Log.d("db",query);
	}
	
	public void insertStudentDetails(String sname[], String sreg[], String sclass){
		SQLiteDatabase db = this.getWritableDatabase();
		for(int i=0; i<sname.length; i++){
			String query = "INSERT INTO student VALUES ( NULL , '"+sname[i]+"' , '"+sreg[i]+"' , '"+sclass+"' );";
			db.execSQL(query);	
			Log.d("db",query);
		}
	}
	
	public Cursor getStudentDetails(String sclass){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(" SELECT sname , sreg FROM student WHERE sclass = '"+sclass+"' ", null);
		return c;
	}
	
	public Cursor getFullAttendanceByReg(String sreg){				//can remove cTemp by passing tName!!
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cTemp = db.rawQuery("SELECT sclass FROM student WHERE sreg = '"+sreg+"' ; ", null);
		cTemp.moveToFirst();
		String tName = cTemp.getString(0);
		Cursor c = db.rawQuery(" SELECT date , reg"+sreg+" FROM "+tName+" ", null);
		return c;
	}
	
	public Cursor getFullAttendanceById(int id, String tName){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(" SELECT * FROM "+tName+" WHERE id = '"+id+"' ", null);
		return c;
	}
	
	public void modifyAttendanceByReg(String sreg, ArrayList<String> als){			//can remove cTemp by passing tName!!
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cTemp = db.rawQuery("SELECT sclass FROM student WHERE sreg = '"+sreg+"' ; ", null);
		cTemp.moveToFirst();
		String tName = cTemp.getString(0);
		cTemp = db.rawQuery("SELECT id FROM "+tName+" ", null);
		cTemp.moveToFirst();
		int i=0;
		do
		{
			if(Integer.parseInt(als.get(i))<2)
			{	db.execSQL("UPDATE "+tName+" SET reg"+sreg+" = "+als.get(i)+" WHERE id = "+cTemp.getString(0)+" ");Log.d("db","UPDATE "+tName+" SET reg"+sreg+" = "+als.get(i)+" WHERE id = "+cTemp.getString(0)+" ");
			}
			i++;
		}while(cTemp.moveToNext());
	}
	
	public void modifyAttendanceById(ArrayList<String> reg, ArrayList<String> als, String id, String tName){			//can remove cTemp by passing tName!!
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "UPDATE "+tName+" SET reg"+reg.get(0)+" = '"+als.get(0)+"' ";
		int i=1;
		do	
		{
			query+=", reg"+reg.get(i)+" = '"+als.get(i)+"' ";
			i++;
		}
		while(reg.size()>i);
		query+="WHERE id = '"+id+"' ;";
		db.execSQL(query);
		Log.d("db",query);
	}
	
	public Cursor getDates(String tName){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery("SELECT id , date FROM "+tName+" ; ", null);
		return c;
	}
	
	public void modifyDate(String id, String tName, String date){
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "UPDATE "+tName+" SET date = '"+date+"' WHERE id = '"+id+"' ; ";
		db.execSQL(query);
		Log.d("db",query);
	}
	
	public void deleteDate(String id, String tName){
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "DELETE FROM "+tName+" WHERE id = '"+id+"' ; ";
		db.execSQL(query);
		Log.d("db",query);		
	}
}