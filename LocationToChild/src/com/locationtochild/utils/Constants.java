package com.locationtochild.utils;

/**
 * 常量类
 */
public class Constants {
	
	public static class SettingsConstants{
		public final static String WATCH_NUMBER = "watch_number";
		public final static String FAMILY_NUMBER = "family_number";
		public final static String CENTRE_NUMBER = "centre_number";
		public final static String GPS_STATE = "gps_state";
		public final static String WALL_STATE = "wall_state";
		public final static String DEVICE_KEY = "device_key";
	}
	public static class MoreDetail{
		public final static int MORE_ABOUT=2000;
		public final static int MORE_HELP=2001;
		public final static int MORE_BACK=2002;
	}
	
	public static class UserConstants{
		public final static String LOGIN = "login" ;
		public final static String REGISTER = "register";
	}
	
	public static class MessageType{
		public static final int MESSAGE_QQ=711;//亲情号指令
		public static final int MESSAGE_CENTER=710;//中心号码
		public static final int MESSAGE_TIME_SAPN=730;//GPS开关时间间隔
		public static final int MESSAGE_WALL=751; //设置电子围栏
		public static final int MESSAGE_WALL_CANCEL=760; //取消电子围栏
		public static final int MESSAGE_DEVICE_KEY=770; //设备密码
	}
	
	// 设置标签
	public static final int SETTING_MAIN = 1100;
	public static final int SETTING_DEVICEKEY = 1111;
	public static final int SETTING_TELEQQ = 1112;
	public static final int SETTING_TELECENTER = 1113;
	public static final int SETTING_TELEDEVICE = 1114;

}
