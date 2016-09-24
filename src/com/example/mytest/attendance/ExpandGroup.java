package com.example.mytest.attendance;

import java.util.ArrayList;

public class ExpandGroup {

	private String rno;
	private String name;
	private boolean i;
	private ArrayList<ExpandChild> items;
	
	public String getName(){
		return name;
	}
	
	public void setName(String s){
		name=s;
	}
	
	public String getRno(){
		return rno;
	}
	
	public void setRno(String s){
		rno=s;
	}
	
	public void setChkBox(boolean in){
		i=in;
	}
	
	public boolean getChkBox(){
		return i;
	}
	
	public ArrayList<ExpandChild> getItems(){
		return items;
	}
	
	public void setItems(ArrayList<ExpandChild> i){
		items=i;
	}
	
}
