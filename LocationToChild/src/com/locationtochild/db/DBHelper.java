package com.locationtochild.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "create table if not exists address_info"
			+ "(_id integer PRIMARY KEY AUTOINCREMENT, time vchar(30),"
			+ " longitude double, latitude double,address vchar(40))";
		db.execSQL(sql);
		// 建表，存放消息提醒信息
		sql = "create table if not exists alarm_info"
				+ "(_id integer PRIMARY KEY AUTOINCREMENT,alarm_time vchar(10),is_on integer,"
				+ "time_span integer)";
		db.execSQL(sql);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
