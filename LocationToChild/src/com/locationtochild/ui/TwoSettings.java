package com.locationtochild.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class TwoSettings extends View{

	private View mTwo;
	private EditText mFamilyOne;
	private EditText mFamilyTwo;
	private EditText mFamilyThree;
	
	public TwoSettings(Context context) {
		super(context);
		mTwo = LayoutInflater.from(context).inflate(R.layout.settings_two, null);
		mFamilyOne = (EditText)mTwo.findViewById(R.id.family_one);
		mFamilyTwo = (EditText)mTwo.findViewById(R.id.family_two);
		mFamilyThree = (EditText)mTwo.findViewById(R.id.family_three);
	}
	
	public View getView(){
		return mTwo;
	}
	
	public String getFamilyOne(){
		return mFamilyOne.getText().toString();
	}
	
	public String getFamilyTwo(){
		return mFamilyTwo.getText().toString();
	}
	
	public String getFamilyThree(){
		return mFamilyThree.getText().toString();
	}

}
