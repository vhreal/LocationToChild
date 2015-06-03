package com.locationtochild.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmInitReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
        if(action.equals(Intent.ACTION_BOOT_COMPLETED)){
        	// ≥ı ºªØƒ÷÷”
        	System.out.println("start mechine !");
        	AlarmHelper.getInstance().initAlarm(context);
        }
	}

}
