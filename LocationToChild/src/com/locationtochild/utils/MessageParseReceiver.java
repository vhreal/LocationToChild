package com.locationtochild.utils;

import com.locationtochild.logic.LocationToChildApplication;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class MessageParseReceiver extends BroadcastReceiver{
	private static final String mSmsUri = "android.provider.Telephony.SMS_RECEIVED";
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("tag", "MessageParseReceiver---start");
		if (intent.getAction().equals(mSmsUri)) {
			Bundle bundle = intent.getExtras();
			Log.i("tag", "MessageParseReceiver---open");
			if (bundle != null) {
				String messageBody = null;
				String messageSource = null;
				// 通过pdus获得接收到的所有短信消息，获取短信内容
				Object[] pdus = (Object[]) bundle.get("pdus");
				// 构建短信数组对象
				SmsMessage[] smg = new SmsMessage[pdus.length];
				for (int i = 0; i < pdus.length; i++) {
					smg[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				}
				String watchNum = "+86"+LocationToChildApplication.mWatchNumber;
				Log.i("tag", "LocationToChildApplication.mWatchNumber"+watchNum);
				for (SmsMessage cursmg : smg) {
					messageSource = cursmg.getDisplayOriginatingAddress();
					Log.i("tag", "messageSource------>"+messageSource);
					messageBody = cursmg.getMessageBody();
					if(watchNum.equalsIgnoreCase(messageSource)){
						LocationToChildApplication.mMessageResult = MessageUtils.getInstance().startParse(messageBody);
						Log.i("tag", "location------>"+LocationToChildApplication.mMessageResult);
						continue;
					}
				}	
			}
		}
		
	}
	
	

}
