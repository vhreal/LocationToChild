package com.locationtochild.logic.model;

public class TimeAlarm {
	private int id;
	private boolean isOn;
	private String time;
	private int dayOfWeek;
	private String description;
	
	public TimeAlarm(){
	}
	public TimeAlarm(int id,String time,boolean isOn,int dayOfWeek){
		this.id=id;
		this.time=time;
		this.isOn=isOn;
		this.dayOfWeek=dayOfWeek;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean getIsOn() {
		return isOn;
	}
	public void setIsOn(boolean isOn) {
		this.isOn = isOn;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public void setDescription(String description){
		this.description=description;
	}
	public String getDescription() {
		return description;
	}
}
