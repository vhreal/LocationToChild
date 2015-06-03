package com.locationtochild.ui.widget;

import com.locationtochild.logic.LocationToChildApplication;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

public class ExitDialog extends Dialog{
	
	private Context mContext;
	private Builder mExit;
	public ExitDialog(Context context) {
		super(context);
		mContext = context;
		mExit = new AlertDialog.Builder(mContext);
		DialogSettings();
	}
	
	private void DialogSettings() {
		mExit.setTitle("确认退出吗?");
		mExit.setIcon(android.R.drawable.ic_dialog_info);
		mExit.setPositiveButton("退出", mExitListener);
		mExit.setNegativeButton("取消", mExitListener);
	}
	
	public void show(){
		mExit.show();
	}
	
	OnClickListener mExitListener = new OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {

			switch(which){
			case -1://确定
				LocationToChildApplication.getInstance().exit();
				break;
			case -2://消失
				dialog.cancel();
				break;
			}
		}
	};
}
