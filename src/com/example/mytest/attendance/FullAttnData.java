package com.example.mytest.attendance;

public class FullAttnData {
	
	private String date;
	private boolean present;
	public boolean isModified = false;
	
	public void setDate(String d){
		date = d;
	}

	public String getDate(){
		return date;
	}
	
	public void setIsPresent(boolean b){
		present = b;
	}
	
	public boolean getIsPresent(){
		return present;
	}
}
