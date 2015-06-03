package com.locationtochild.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.locationtochild.logic.LocationToChildApplication;
import com.locationtochild.logic.model.LocationBean;
import com.locationtochild.utils.Constants.MessageType;
import android.telephony.SmsManager;
import android.util.Log;

public class MessageUtils {
	// 短信服务类调用短信接口发送短信
	private SmsManager mSmsManager;
	private static MessageUtils mMessageUtil;
	// 初始化mPhoneNum
	private String mWatchNum = LocationToChildApplication.mWatchNumber;
	private String mWatchPwd = LocationToChildApplication.mWatchPassword;

	public synchronized static MessageUtils getInstance() {
		if (mMessageUtil == null)
			mMessageUtil = new MessageUtils();
		return mMessageUtil;
	}

	private MessageUtils(){
		mSmsManager = SmsManager.getDefault();
	}
	
	/**
	 * 获取当前的位置信息
	 */
	public void getPosition() {
		sendMessage(mWatchNum, "988"+mWatchPwd);
	}

	/**
	 * 设置一个亲情号 
	 * @param qQPhoneNum
	 */
	public void setQQPhoneNum(String... qQPhoneNum) {
		String temp = null ;
		String flag="";
		for(int i = 0 ; i < qQPhoneNum.length ; i++){
			temp += qQPhoneNum[i]+"#";
		}
		// 2014-1-7lichen修改，亲情号码设置修改无bug
		switch (qQPhoneNum.length) {
		case 1:
			flag="##";
			break;
		case 2:
			flag="#";
			break;
		case 3:
			flag="";
			break;
		default:
			break;
		}
		String nQQPhone = "#711#" + temp + flag + mWatchPwd + "##";
		sendMessage(mWatchNum, nQQPhone);
	}

	/**
	 * 设置一个中心号
	 * 
	 * @param centerPhoneNum 中心号码            
	 * */
	public void setCenterPhoneNum(String centerPhoneNum) {
		String nCPhone = "#710#" + centerPhoneNum + "#"+mWatchPwd+"##";
		sendMessage(mWatchNum, nCPhone);
	}

	/**
	 * 设置定位的上传模式信息
	 * @param int timeSpan 定位时间间隔
	 * */
	public void setLocationModel(String timeSpan, String sendTimes) {
		String nLocation = "#730#" + timeSpan + "#" + sendTimes + "#"+mWatchPwd+"##";
		sendMessage(mWatchNum, nLocation);
	}

	/**
	 * 设置GPS是否开启
	 * @param boolean isOn GPS是否开启
	 * */
	public void setLocationOn(boolean isOn) {
		if (isOn) {
			setLocationModel(180+"", 1+"");
		} else {
			setLocationModel(0+"", 0+"");
		}
	}

	/**
	 * 取消电子围栏
	 * 
	 * @throws 待定
	 * */
	public void setWallOff() {
		sendMessage(mWatchNum, "#760#"+mWatchPwd+"##");
	}

	/**
	 * 设置电子围栏
	 *@param scale为半径  timeSpan为时间间隔 
	 * */
	public void setWallOn(String scale, String timeSpan, String longitude,
			String latitude) {
		String nWall = "#751#" + scale + "#" + timeSpan + "#" + longitude
				+ "N#" + latitude + "E#"+mWatchPwd+"##";
		sendMessage(mWatchNum, nWall);
	}

	/**
	 * 密码修改
	 * @param  nkey 新密码  okey 旧密码
	 * @param msgText
	 */
	public void setKeyWord(String okey,String nkey){
		String changeKey="#770#"+nkey+"#"+okey+"##";
		sendMessage(mWatchNum,changeKey);
	}
	
	// 发送消息
	public void sendMessage(String phoneNum, String msgText) {
		// 设置接收回执
		mSmsManager.sendTextMessage(mWatchNum, null, msgText,
				LocationToChildApplication.mSendPI,
				LocationToChildApplication.mDeliverPI);
	}
	
	/**
	 * 根据不同指令解析短信
	 * @param message 通过广播接收到的短信内容
	 */
	public String startParse(String message){
		String result = "";
		if(!message.equalsIgnoreCase("")){
			if((message.charAt(0)+"").equalsIgnoreCase("&")){
				result = parseSettingMessage(message);
			}else{
				Log.i("tag", "收到查询位置短信");
				result = parseLocationMessage(message);
			}
		}else{
			Log.i("tag", "开始解析------->短信内容为空");//测试专用
		}
		
		return result;
	}
	
	/**
	 * 解析各种设置指令，并根据内容得出是否设置成功？
	 * @param message 接收到的短信字符串
	 * @return result 转换为设置结果说明字符串
	 */
	private String parseSettingMessage(String message){
		String result = "";
		int type = Integer.parseInt(message.substring(1, 4));
		switch(type){
		case MessageType.MESSAGE_QQ:
			result = "亲情号";
			break;
		case MessageType.MESSAGE_CENTER:
			result = "中心号码";
			break;
		case MessageType.MESSAGE_WALL:
			result = "电子围栏";
			break;
		case MessageType.MESSAGE_WALL_CANCEL:
			result = "取消电子围栏";
			break;
		case MessageType.MESSAGE_TIME_SAPN:
			result = "GPS";
			break;
		case MessageType.MESSAGE_DEVICE_KEY:
			result = "密码修改";
			break;
		default:
			result = "未知的指令";
		}
		result += getSettingResult(message);
		return result;
	}
	
	/**
	 * 解析位置查询、电子围栏、SOS求救短信。
	 * @param messageBody 收到短信主体
	 * @return result 展示结果;
	 */
	private String parseLocationMessage(String messageBody){
		String result = "";
		LocationBean location = new LocationBean();
		int i = messageBody.length() - 3;
		Log.i("tag", "i的位置----->"+i);
		for(int j = 0; j < messageBody.length(); j++){
			Log.i("tag", "字符---->"+j+":"+messageBody.charAt(j));
		}
		
		Log.i("tag", "开始解析位置----"+messageBody.charAt(i));
		Log.i("tag", "boolean------"+Character.isDigit(messageBody.charAt(i)));
		if (Character.isDigit(messageBody.charAt(i))) {
			// 利用三个空格进行短信的解析
			Log.i("tag", "最后一位是数字");
			int count = 2;
			int[] device = new int[3];
			while (i >= 0 && count >= 0) {
				if ((messageBody.charAt(i)+"").equals(" ")) {
					Log.i("tag", "i的位置---->"+i);
					device[count] = i;
					count--;
				}
				i--;
			}
			Log.i("tag", messageBody.substring(device[2] + 1)+"-"+messageBody.substring(device[1] + 1,device[2])+"----"+messageBody.substring(0, device[0]));
			location.setLongtitudeStr(Double.parseDouble(messageBody.substring(device[2] + 1)));
			location.setLatitudeStr(Double.parseDouble(messageBody.substring(device[1] + 1,device[2])));
			location.setAddressStr(messageBody.substring(0, device[0]));
			location.setTime(getNowtime());
			LocationToChildApplication.mDBUtils.insertAddresstoDB(location);//将解析后的插入到本地数据库
			result = "位置查询成功";
		}else if(messageBody.equalsIgnoreCase("位置查询超时，请稍后再试")){
			result = "位置查询超时，请稍后再试";
		}
		return result;
	}
	
	/**
	 * 通过匹配，得到设置后的结果
	 * @param message 短信内容
	 * @return 返回设置成功或者失败
	 */
	private String getSettingResult(String message){
		String success="设置成功";
		String failure="设置失败";
		Matcher matcher = Pattern.compile(success).matcher(message);
		if(matcher.find())
			return success;
		else
			return failure+",请重试！";
	}
	
	/**
	 * 得到当前的时间，格式yyyy-MM-dd HH:mm:ss
	 * 
	 * @return 当前时间字符串
	 */
	private String getNowtime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String date = formatter.format(curDate);
		return date;
	}
}
