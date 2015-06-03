package com.locationtochild.utils;

import java.util.Calendar;
import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.locationtochild.db.DBUtils;
import com.locationtochild.logic.model.TimeAlarm;

public class AlarmHelper {
	private static AlarmHelper mAlarmHelper;
	private DBUtils mDbUtil;
	private List<TimeAlarm> mAlarmList;
	private static String[] mDay = { "星期一 ", " 星期二 ", " 星期三 ", " 星期四 ",
			" 星期五 ", " 星期六 ", " 星期天 " };

	public static final String ALARM_ALERT_ACTION = "com.locationtochild.ALARM_ALERT";

	private AlarmHelper() {

	}

	public synchronized static AlarmHelper getInstance() {
		if (mAlarmHelper == null)
			mAlarmHelper = new AlarmHelper();
		return mAlarmHelper;
	}

	// 获取初始的提醒列表
	public List<TimeAlarm> getAlarmList(Context context) {
		if (mAlarmList == null) {
			mDbUtil = new DBUtils(context);
			mAlarmList = mDbUtil.getAlarmData();
			// 初始化闹钟的描述信息
			for (int i = 0; i < mAlarmList.size(); i++) {
				mAlarmList.get(i).setDescription(
						initDescription(mAlarmList.get(i).getDayOfWeek()));
			}
		}
		return mAlarmList;
	}

	// 初始化闹钟提醒
	public void initAlarm(Context context) {
		System.out.println("init the alarm when launched phone ");
		// 遍历进行闹钟添加
		if (mAlarmList == null) {
			mAlarmList = getAlarmList(context);
		}
		for (int i = 0; i < mAlarmList.size(); i++) {
			if (mAlarmList.get(i).getIsOn()) {
				openAlarm(i, context);
			}
		}
	}

	// 判断闹钟是否重复
	public boolean hasExist(Context context,String alarmTime){
		System.out.println("coming hasexist ");
		if (mDbUtil == null) {
			mDbUtil = new DBUtils(context);
		}
		// 插入数据到数据库中，同步到提醒列表
		int num = mDbUtil.hasExistAlarm(alarmTime);
		if (num > 0) {
			return true;
		}
		else 
			return false;
	}
	
	// 添加提醒
	public void addAlarm(TimeAlarm alarm, Context context) {
		if (mDbUtil == null) {
			mDbUtil = new DBUtils(context);
		}
		// 插入数据到数据库中，同步到提醒列表
		int id = mDbUtil.insertIntoAlarm(alarm);
		if (id < 0) {
			// 插入数据失败
			System.out.println("insert error");
			return;
		}
		alarm.setId(id);
		alarm.setDescription(initDescription(alarm.getDayOfWeek()));
		mAlarmList.add(alarm);
		System.out.println("call the add alarm id is " + id);
		// 计算下次提醒的日期
		long timeInMillis = calculateAlarm(alarm);
		// 添加提醒
		enableAlert(context, id, timeInMillis);
	}

	// 修改提醒内容
	public void updateAlarm(int position, Context context, boolean flag) {
		if (mDbUtil == null) {
			mDbUtil = new DBUtils(context);
		}
		mAlarmList.get(position).setDescription(
				initDescription(mAlarmList.get(position).getDayOfWeek()));
		mDbUtil.updateAlarm(mAlarmList.get(position));
		if (flag && mAlarmList.get(position).getIsOn()) {
			System.out.println("change the alarm on ");
			// 开启闹钟
			openAlarm(position, context);
		}
	}

	// 删除提醒
	public void removeAlarm(int position, Context context) {
		if (mAlarmList.get(position).getIsOn()) {
			System.out.println("delete alarm--------->disable the alarm ");
			// disableAlarm(context, position);
		}
		if (mDbUtil == null) {
			mDbUtil = new DBUtils(context);
		}
		int id = mAlarmList.get(position).getId();
		mDbUtil.deleteFromAlarm(id);
		mAlarmList.remove(position);
		System.out.println("remove alarm id" + id + " the position is "
				+ position);
	}

	// 开启提醒闹钟
	public void openAlarm(int position, Context context) {
		// 计算下次提醒的日期
		long timeInMillis = calculateAlarm(mAlarmList.get(position));
		// 添加提醒
		enableAlert(context, mAlarmList.get(position).getId(), timeInMillis);
	}

	// 切换提醒闹钟
	public void changeAlarm(int position, Context context) {
		if (mDbUtil == null) {
			mDbUtil = new DBUtils(context);
		}
		int id = mAlarmList.get(position).getId();
		System.out.println("the before alarm is "
				+ mAlarmList.get(position).getIsOn());
		mDbUtil.manageAlarmOn(id, mAlarmList.get(position).getIsOn());

		if (mAlarmList.get(position).getIsOn()) {
			System.out.println("cancel alarm--------->disable the alarm ");
			disableAlarm(context, id);
			mAlarmList.get(position).setIsOn(false);
		} else {
			System.out.println("add alarm--------->add the alarm ");
			// 计算下次提醒的日期
			long timeInMillis = calculateAlarm(mAlarmList.get(position));
			// 添加提醒
			enableAlert(context, id, timeInMillis);
			mAlarmList.get(position).setIsOn(true);
		}

	}

	// 调用提醒管理取消提醒------------------------------------------>test
	public void disableAlarm(Context context, int id) {
		System.out.println("cancel alarm meme !");
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(ALARM_ALERT_ACTION);
		PendingIntent cancel = PendingIntent.getBroadcast(context, id, intent,
				0);
		am.cancel(cancel);
	}

	// 调用提醒管理添加提醒----------------------------------------->test
	private void enableAlert(Context context, int id, long atTimeInMillis) {
		System.out.println("add alarm meme !");
		// 添加提醒
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(ALARM_ALERT_ACTION);
		PendingIntent sender = PendingIntent.getBroadcast(context, id, intent,
				0);
		am.set(AlarmManager.RTC_WAKEUP, atTimeInMillis, sender);
	}

	// 计算下次提醒的时刻
	static long calculateAlarm(TimeAlarm alarm) {
		// 获取当前时间
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());

		int nowHour = c.get(Calendar.HOUR_OF_DAY);
		int nowMinute = c.get(Calendar.MINUTE);
		// 解析当前设置的提醒时间
		int alarmHour = Integer.parseInt(alarm.getTime().substring(0, 2));
		int alarmMin = Integer.parseInt(alarm.getTime().substring(3));
		// 如果设置的时间大于当前时间，则将时间进行后移
		if (alarmHour < nowHour || alarmHour == nowHour
				&& alarmMin <= nowMinute) {
			c.add(Calendar.DAY_OF_YEAR, 1);
			System.out.println("the time is < now time !");
		}
		c.set(Calendar.HOUR_OF_DAY, alarmHour);
		c.set(Calendar.MINUTE, alarmMin);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		// 获取下一次闹钟时间
		int addDays = getNextAlarm(c, alarm.getDayOfWeek());
		if (addDays > 0)
			c.add(Calendar.DAY_OF_WEEK, addDays);
		return c.getTimeInMillis();
	}

	// 获取下一次的提醒时间 ----------------------------需测试
	public static int getNextAlarm(Calendar c, int dayOfWeek) {
		// 切换时间到正常的日期，利用calendar得到的星期一代表数字为2
		int today = (c.get(Calendar.DAY_OF_WEEK) + 5) % 7;
		int day = 0;
		int dayCount = 0;
		// 对数据进行移位操作
		int yi;
		for (; dayCount < 7; dayCount++) {
			day = (today + dayCount) % 7;
			// 对数据进行移位操作，之后对2取余判断当前日期是否选择
			yi = dayOfWeek >> day;
			if (yi % 2 > 0) {
				break;
			}
		}
		System.out.println("the daycount is " + dayCount);
		return dayCount;
	}

	public void setNextAlarm(Context context,String time){
		if (mDbUtil == null) {
			mDbUtil = new DBUtils(context);
		}
		TimeAlarm alarm=mDbUtil.getIdByTime(time);
		if(alarm!=null){
			System.out.println("set next alarm meme !Helper !");
			// 计算下次提醒的日期
			long timeInMillis = calculateAlarm(alarm);
			// 添加提醒
			enableAlert(context, alarm.getId(), timeInMillis);
		}
	}
	
	/**
	 * 初始化提醒的周期数据转化为字符串
	 * 
	 * @param dayOfWeek
	 */
	public String initDescription(int dayOfWeek) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 7; i++) {
			if ((dayOfWeek >> i) % 2 == 1) {
				sb.append(mDay[i]);
			}
		}
		return sb.toString();
	}
}
