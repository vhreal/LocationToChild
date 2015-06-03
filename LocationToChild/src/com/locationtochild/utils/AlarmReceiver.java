package com.locationtochild.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.format.Time;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (AlarmHelper.ALARM_ALERT_ACTION.equals(intent.getAction())) {
			Time t=new Time();
			t.setToNow();
			Toast.makeText(context, "the time is "+t.hour+":"+t.minute, Toast.LENGTH_LONG).show();
			/*
			 * 利用多线程或者服务进行以下操作
			 * */
			// 根据时间对闹钟进行操作，下一次提醒
			String time;
			if(t.hour<10){
				time="0"+t.hour+":";
			}
			else {
				time=""+t.hour+":";
			}
			if(t.minute<10){
				AlarmHelper.getInstance().setNextAlarm(context,time+"0"+t.minute);
			}
			else{
				AlarmHelper.getInstance().setNextAlarm(context,time+t.minute);
			}
			// 进行下一次的闹钟提醒
			MessageUtils.getInstance().getPosition();
		}
	}

}
