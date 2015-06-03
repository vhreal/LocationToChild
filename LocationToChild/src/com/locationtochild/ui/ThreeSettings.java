package com.locationtochild.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class ThreeSettings extends View{
	
	private View mThree;
	private EditText mCentre;

	public ThreeSettings(Context context) {
		super(context);
		mThree = LayoutInflater.from(context).inflate(R.layout.settings_three, null);
		mCentre = (EditText)mThree.findViewById(R.id.centre_number);
	}
	
	public View getView(){
		return mThree;
	}
	
	public String getCentre(){
		return mCentre.getText().toString();
	}

}
