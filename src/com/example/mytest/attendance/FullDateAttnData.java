package com.example.mytest.attendance;

public class FullDateAttnData {

	private String name;
	private boolean present;
	
	public void setName(String n){
		name = n;
	}

	public String getName(){
		return name;
	}
	
	public void setIsPresent(boolean b){
		present = b;
	}
	
	public boolean getIsPresent(){
		return present;
	}
}
