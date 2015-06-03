package com.locationtochild.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.locationtochild.logic.model.LocationBean;
import com.locationtochild.logic.model.TimeAlarm;


public class DBUtils {
	private Context mContext;
	private DBHelper dbHelper;
	
	/**创建应用本地数据库**/
	public DBUtils (Context context){
		mContext = context;
		dbHelper = new DBHelper(mContext, "locationtochild.db", null, 1);
	}
	
	/**关闭数据库**/
	private void closeDB(){
		dbHelper.close();
	}
	
	//==========================有关地址短消息本地数据库操作==========================//
	
	/**
	 * 将LocationBean对象包含信息添加到数据库
	 */
	public void insertAddresstoDB (LocationBean location){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("time", location.getTime());
		cv.put("longitude", location.getLongtitudeStr());
		cv.put("latitude", location.getLatitudeStr());
		cv.put("address", location.getAddressStr());
		db.insert("address_info", null, cv);
		Log.i("tag", "数据插入成功");
		closeDB();
	}
	
	/**
	 * 获取当前address_info表最后一条数据的id号
	 * @return address_info表中最后一条数据的id号
	 */
	public int getLastAddressID(){
		int i = 0;
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query("address_info", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			cursor.moveToLast();
			i = cursor.getInt(0);
		}
		cursor.close();
		closeDB();
		return i;
	}
	
	/**
	 * 返回所有的address_info信息
	 * @return List<LocationBean> 位置信息链表
	 */
	public List<LocationBean> getAllAddressInfo(){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		List<LocationBean> addressList = new ArrayList<LocationBean>();
		Cursor cursor = db.query("address_info", null, null, null, null, null, "_id");
		if(cursor.moveToLast()){
			do{
				LocationBean addressInfo = new LocationBean();
				addressInfo.setId(cursor.getInt(0));
				addressInfo.setTime(cursor.getString(1));
				addressInfo.setLongtitudeStr(cursor.getDouble(2));
				addressInfo.setLatitudeStr(cursor.getDouble(3));
				addressInfo.setAddressStr(cursor.getString(4));
				addressList.add(addressInfo);
			}while(cursor.moveToPrevious());
		}
		return addressList;
	}
	
	/**
	 * 获取提醒信息
	 * 
	 * @param
	 * @return
	 */
	public List<TimeAlarm> getAlarmData() {
		List<TimeAlarm> alarmList = new ArrayList<TimeAlarm>();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query("alarm_info", null, null, null, null, null,null);
		cursor.moveToFirst();
		for (int i = 0; i < cursor.getCount(); i++) {
			TimeAlarm alarm = new TimeAlarm();
			// 获取id
			alarm.setId(cursor.getInt(0));
			alarm.setTime(cursor.getString(1));
			if (cursor.getInt(2) > 0)
				alarm.setIsOn(true);
			else
				alarm.setIsOn(false);
			alarm.setDayOfWeek(cursor.getInt(3));
			alarmList.add(alarm);
			cursor.moveToNext();
		}
		return alarmList;
	}

	/**
	 * 判断提醒重复
	 * 
	 * @param data
	 */
	public int hasExistAlarm(String alarmTime){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String strSql="select * from alarm_info where alarm_time = '"+alarmTime+"'";
		Cursor cur=db.rawQuery(strSql, null);
		System.out.println("the size is ------------------------------------>"+cur.getCount());
		return cur.getCount();
	}
	
	/**
	 * 向表alarm_info中插入一个数据
	 * 
	 * @param data
	 */
	public int insertIntoAlarm(TimeAlarm data) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("alarm_time", data.getTime());
		if(data.getIsOn()){
			cv.put("is_on", 1);
		}
		else 
			cv.put("is_on", 0);
		cv.put("time_span", data.getDayOfWeek());
		long rowId=db.insert("alarm_info", null, cv);
		int id=-1;
		if(rowId>0){
			// 获取当前插入到提醒列表的ID信息
			String strSql="select max(_id) from alarm_info";
			Cursor cur=db.rawQuery(strSql, null);
			cur.moveToFirst();
			id=cur.getInt(0);
			System.out.println("the insert id is "+id);
		}
		closeDB();
		return id;
	}

	/**
	 * 向表alarm_info中删除一个数据
	 * 
	 * @param _id
	 */
	public void deleteFromAlarm(int id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String where = "_id = " + id;
		db.delete("alarm_info", where, null);
		System.out.println("Delete row ID--->" + id);
	}

	/**
	 * 向表alarm_info中更新一条数据
	 * 
	 * @param _id data
	 */
	public void updateAlarm(TimeAlarm data) {
		int id=data.getId();
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues cv=new ContentValues();
		cv.put("alarm_time", data.getTime());
		cv.put("time_span", data.getDayOfWeek());
		String where="_id ="+data.getId();
		db.update("alarm_info", cv, where, null);
		closeDB();
		System.out.println("Update row ID--->" + id);
	}

	/**
	 * 向表alarm_info中更新开启和关闭变量
	 * 
	 * @param _id data
	 */
	// -------------------------------------->test
	public void manageAlarmOn(int id,boolean flag){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int isOn=0;
		if(!flag){
			isOn=1;
		}
		String sql="update alarm_info set is_on="+isOn+" where _id ="+id;
		db.execSQL(sql);
		closeDB();
		System.out.println("the alarm is changed to "+isOn);
	}
	
	/**
	 * 向表alarm_info中更新开启和关闭变量
	 * 
	 * @param _id data
	 */
	// -------------------------------------->test
	public TimeAlarm getIdByTime(String time){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql="select * from alarm_info where alarm_time='"+time+"' and is_on = 1";
		Cursor cursor=db.rawQuery(sql,null);
		TimeAlarm alarm=null;
		if(cursor.getCount()>0){
			cursor.moveToFirst();
			alarm = new TimeAlarm();
			// 获取id
			alarm.setId(cursor.getInt(0));
			alarm.setTime(cursor.getString(1));
			alarm.setDayOfWeek(cursor.getInt(3));
		}
		closeDB();
		return alarm;
	}
}
