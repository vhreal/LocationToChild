package com.locationtochild.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class OneSettings extends View{

	private View mOne;
	private EditText mWatch;
	private Context mContext;
	
	public OneSettings(Context context) {
		super(context);
		mContext = context;
		mOne = LayoutInflater.from(mContext).inflate(R.layout.settings_one, null);
		mWatch = (EditText)mOne.findViewById(R.id.watch_number);
	}
	
	public View getView(){
		return mOne;
	}

	public String getEditText(){
		return mWatch.getText().toString();
	}
}
