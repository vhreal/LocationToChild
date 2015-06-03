package com.locationtochild.logic;

import java.util.LinkedList;
import java.util.List;

import com.locationtochild.db.DBUtils;
import com.locationtochild.utils.HttpUtils;

import android.app.Activity;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;

public class LocationToChildApplication extends Application {

	private static LocationToChildApplication mInstance;
	private List<Activity> activityList = new LinkedList<Activity>();
	public static String mWatchNumber;//手表电话号码
	public static String mWatchPassword;//手表查询密码
	public static PendingIntent mSendPI;
	public static PendingIntent mDeliverPI;
	public static HttpUtils mHttpUtils;
	public static String mMessageResult = "";//解析短信后返回的字符串
	public static DBUtils mDBUtils;
	
	@Override
	public void onCreate() {
		mWatchNumber = getSharedPreferences("UserInfo", MODE_PRIVATE).getString("watch", "init");
		mWatchPassword = getSharedPreferences(mWatchNumber, MODE_PRIVATE).getString("password", "0000");
		mSendPI = PendingIntent.getBroadcast(getApplicationContext(), 0,
				new Intent("SMS_SEND_ACTIOIN"), 0);
		mDeliverPI = PendingIntent.getBroadcast(getApplicationContext(), 0,
				new Intent("SMS_DELIVERED_ACTIOIN"), 0);
		mHttpUtils = HttpUtils.getInstance();
		mDBUtils = new DBUtils(this);
	}
	
	public static LocationToChildApplication getInstance() {
		if (null == mInstance)
			mInstance = new LocationToChildApplication();
		return mInstance;
	}
	
	//将activity添加到容器里面
	public void addActivity(Activity activity)
	{
		activityList.add(activity);
	}
	
	//遍历所有activity并finish
	public void exit()
	{
		for(Activity activity:activityList)
		{
			activity.finish();
		}
		System.exit(0);
	}

	
	
}
