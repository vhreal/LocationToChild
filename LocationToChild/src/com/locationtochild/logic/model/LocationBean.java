package com.locationtochild.logic.model;

public class LocationBean {
	
	private double longtitudeStr;
	private double latitudeStr;
	private String addressStr;
	private String time;
	private int id;
	
	public LocationBean (){}
	
	public LocationBean (int id, double longtitude, double latitude, String address, String time){
		this.longtitudeStr = longtitude;
		this.latitudeStr = latitude;
		this.addressStr = address;
		this.time = time;
		this.id = id;
	}

	public String getAddressStr() {
		return addressStr;
	}

	public void setAddressStr(String addressStr) {
		this.addressStr = addressStr;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public double getLongtitudeStr() {
		return longtitudeStr;
	}

	public void setLongtitudeStr(double longtitudeStr) {
		this.longtitudeStr = longtitudeStr;
	}

	public double getLatitudeStr() {
		return latitudeStr;
	}

	public void setLatitudeStr(double latitudeStr) {
		this.latitudeStr = latitudeStr;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
}
