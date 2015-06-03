package com.locationtochild.ui.setting;

import static com.locationtochild.utils.Constants.SETTING_DEVICEKEY;
import static com.locationtochild.utils.Constants.SETTING_TELECENTER;
import static com.locationtochild.utils.Constants.SETTING_TELEDEVICE;
import static com.locationtochild.utils.Constants.SETTING_TELEQQ;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.widget.Toast;

import com.locationtochild.logic.LocationToChildApplication;
import com.locationtochild.ui.MainActivity;
import com.locationtochild.ui.R;
import com.locationtochild.ui.widget.MyProgressDialog;
import com.locationtochild.utils.MessageUtils;
import com.locationtochild.utils.Constants.SettingsConstants;

public class SettingActivity extends FragmentActivity implements
		SettingFragment.OnSettingItemSelect {
	public Intent mIntentSI;
	// 定义碎片变量
	private SettingFragment mSetFragment;
	private Fragment mTailFragment;
	// 设置片段管理器
	private FragmentManager mSettingFrgManager;
	// 设置片段转换器
	private FragmentTransaction mSettingFrgTran;
	// 获取设置参数
	private boolean mGpsOn;
	private boolean mWallOn;
	private String mTeleCenter;
	private String mTeleQQOne;
	private String mTeleQQTwo;
	private String mTeleQQThree;
	private String mTeleDevice;
	private String mDeviceKye;
	// 获取电子围栏的状态
	private String mScale;
	private String mTimeSpan;
	private String mLongitude;
	private String mLatitude;
	// 传递参数
	private Bundle mBundle;
	// 进行异步处理
	private AsyncSettingTask mSettingTask;
	private boolean mRunning = false;
	private SharedPreferences mWatchInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setting_main);
		// 获取配置信息
		getSettingParm();
		System.out.println("the setting param is get !");
		mBundle = new Bundle();
		// 初始化片段为设置主界面
		if (findViewById(R.id.setting_fragment_container) != null) {
			if (savedInstanceState != null) {
				return;
			}
			initSettingFragment();
		}
	}

	public void initSettingFragment() {
		mSetFragment = new SettingFragment();
		mBundle.putBoolean("gps", mGpsOn);
		mBundle.putBoolean("wall", mWallOn);
		mSetFragment.setArguments(mBundle);
		mSettingFrgManager = getSupportFragmentManager();
		mSettingFrgTran = mSettingFrgManager.beginTransaction();
		mSettingFrgTran.add(R.id.setting_fragment_container, mSetFragment)
				.commit();
	}

	// 获取配置参数
	public void getSettingParm() {
		SharedPreferences userInfo = getSharedPreferences("UserInfo",
				MODE_PRIVATE);
		mTeleDevice = userInfo.getString("watch", "");
		System.out.println("the device num is " + mTeleDevice);
		if (mTeleDevice.equals("")) {
			Toast.makeText(getApplicationContext(), "无法获取号码信息",
					Toast.LENGTH_LONG).show();
			// 跳转到主页面进行设置操作界面
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		}
		mWatchInfo = getSharedPreferences(mTeleDevice, MODE_PRIVATE);
		mGpsOn = mWatchInfo.getBoolean("GpsIsOpen", true);
		mWallOn = mWatchInfo.getBoolean("WallIsOpen", false);
		mTeleCenter = mWatchInfo.getString("Centre", "18710726977");
		mTeleQQOne = mWatchInfo.getString("QQOne", "18710726977");
		mTeleQQTwo = mWatchInfo.getString("QQTwo", "");
		mTeleQQThree = mWatchInfo.getString("QQThree", "");
		mDeviceKye = mWatchInfo.getString("password", "0000");
		// 获取电子围栏参数
		mScale = mWatchInfo.getString("Scale", "500");
		mTimeSpan = mWatchInfo.getString("TimeSpan", "5");
		mLatitude = mWatchInfo.getString("Latitude", "116.302251");
		mLongitude = mWatchInfo.getString("Longitude", "39.9890077");
	}

	// fragment切换
	public void changeFragment(int TAG, boolean flag) {
		// 跳转页面管理
		mSettingFrgManager = getSupportFragmentManager();
		mSettingFrgTran = mSettingFrgManager.beginTransaction();
		// 若flag为true则返回到主设置界面
		if (flag) {
			// 测试进入细节页之后是否会销毁
			mSettingFrgTran.setCustomAnimations(R.anim.slide_right_in,
					R.anim.slide_right_out);
			mSettingFrgTran.replace(R.id.setting_fragment_container,
					mSetFragment);
		} else {
			// 处理标签进行fragment新建
			dealTag(TAG);
			mSettingFrgTran.setCustomAnimations(R.anim.slide_left_in,
					R.anim.slide_left_out);
			mSettingFrgTran.addToBackStack(null);
			mSettingFrgTran.replace(R.id.setting_fragment_container,
					mTailFragment);
		}
		mSettingFrgTran.commit();
	}

	// fragment更新
	public void updateFragment(int TAG) {
		// 跳转页面管理
		mSettingFrgManager = getSupportFragmentManager();
		mSettingFrgTran = mSettingFrgManager.beginTransaction();
		if (TAG < 0) {
			mSettingFrgManager.popBackStack();
		}
		// 更新主设置页面
		mSetFragment = new SettingFragment();
		mBundle.putBoolean("gps", mGpsOn);
		mBundle.putBoolean("wall", mWallOn);
		mSetFragment.setArguments(mBundle);
		mSettingFrgTran.replace(R.id.setting_fragment_container, mSetFragment);
		if (TAG > 0) {
			dealTag(TAG);
			mSettingFrgTran.addToBackStack(null);
			mSettingFrgTran.replace(R.id.setting_fragment_container,
					mTailFragment);
		}
		mSettingFrgTran.commit();
	}

	public void dealTag(int TAG) {
		// 否则跳转到细节界面
		switch (TAG) {
		case SETTING_DEVICEKEY:
			mTailFragment = new DeviceKeyFragment();
			mBundle.putString("device_key", mDeviceKye);
			mTailFragment.setArguments(mBundle);
			break;
		case SETTING_TELEQQ:
			mTailFragment = new TeleQQFragment();
			mBundle.putString("qq_one", mTeleQQOne);
			mBundle.putString("qq_two", mTeleQQTwo);
			mBundle.putString("qq_three", mTeleQQThree);
			mTailFragment.setArguments(mBundle);
			break;
		case SETTING_TELECENTER:
			mTailFragment = new TeleCenterFragment();
			mBundle.putString("tele_center", mTeleCenter);
			mBundle.putString("qq_one", mTeleQQOne);
			mBundle.putString("qq_two", mTeleQQTwo);
			mBundle.putString("qq_three", mTeleQQThree);
			mTailFragment.setArguments(mBundle);
			break;
		case SETTING_TELEDEVICE:
			mTailFragment = new TeleDeviceFragment();
			mBundle.putString("tele_device", mTeleDevice);
			mTailFragment.setArguments(mBundle);
			break;
		}
	}

	// 实现接口的调用
	@Override
	public void onItemSelected(int TAG, boolean flag) {
		// TODO Auto-generated method stub
		changeFragment(TAG, flag);
	}

	@Override
	public void settingMessage(String... params) {
		// TODO Auto-generated method stub
		System.out.println("setting message ");
		mSettingTask = new AsyncSettingTask();
		mSettingTask.execute(params);
	}

	// 异步处理类
	public class AsyncSettingTask extends AsyncTask<String, Integer, String> {

		private MyProgressDialog mProgressDialog;

		@Override
		protected String doInBackground(String... params) {
			String type = params[0];
			String result = ""; // 返回到主线程的结果
			if (type.equalsIgnoreCase(SettingsConstants.GPS_STATE)) {// 设置GPS开关
				mRunning = true;
				boolean flag = false;
				System.out.println("click gps in setting ");
				mGpsOn = flag;
				if (params[1].equalsIgnoreCase("true")) {
					flag = true;
				}
				MessageUtils.getInstance().setLocationOn(flag);
				result = waitMessageResult();
			} else if (type.equalsIgnoreCase(SettingsConstants.WALL_STATE)) {// 设置WALL开关
				mRunning = true;
				System.out.println("click wall in setting ");
				if (params[1].equalsIgnoreCase("true")) {
					mWallOn = true;
					MessageUtils.getInstance().setWallOn(mScale, mTimeSpan,
							mLongitude, mLatitude);
				} else {
					mWallOn = false;
					MessageUtils.getInstance().setWallOff();
				}
				result = waitMessageResult();
			} else if (type.equalsIgnoreCase(SettingsConstants.FAMILY_NUMBER)) {// 设置与手表绑定的亲情号
				mRunning = true;
				mTeleQQOne = params[2];
				if (params[1].equalsIgnoreCase("one")) {
					mTeleQQTwo = "";
					mTeleQQThree = "";
					MessageUtils.getInstance().setQQPhoneNum(params[2]);
				} else if (params[1].equalsIgnoreCase("two")) {
					mTeleQQTwo = params[3];
					mTeleQQThree = "";
					MessageUtils.getInstance().setQQPhoneNum(params[2],
							params[3]);
				} else if (params[1].equalsIgnoreCase("three")) {
					mTeleQQTwo = params[3];
					mTeleQQThree = params[4];
					MessageUtils.getInstance().setQQPhoneNum(params[2],
							params[3], params[4]);
				}
				result = waitMessageResult();
			} else if (type.equalsIgnoreCase(SettingsConstants.CENTRE_NUMBER)) {// 设置与手表绑定的中心号码
				mRunning = true;
				mTeleCenter = params[1];
				MessageUtils.getInstance().setCenterPhoneNum(params[1]);
				result = waitMessageResult();
			} else if (type.equalsIgnoreCase(SettingsConstants.DEVICE_KEY)) {// 设置手表密码
				System.out.println(params[1] + " new pass " + params[2]);
				mDeviceKye = params[2];
				mRunning = true;
				MessageUtils.getInstance().setKeyWord(params[1], params[2]);
				result = waitMessageResult();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			stopProgressDialog();
			boolean success = false;
			int tag = -1;
			if (result.contains("亲情号")) {
				if (result.contains("设置成功")) {
					Toast.makeText(SettingActivity.this, "亲情号设置成功",
							Toast.LENGTH_SHORT).show();
					mWatchInfo.edit().putString("QQOne", mTeleQQOne).commit();
					mWatchInfo.edit().putString("QQTwo", mTeleQQTwo).commit();
					mWatchInfo.edit().putString("QQThree", mTeleQQThree)
							.commit();
					success = true;
					tag = SETTING_TELEQQ;
					// 上传到网络

				} else {
					Toast.makeText(SettingActivity.this, "亲情号设置失败，请重试!",
							Toast.LENGTH_SHORT).show();
					mTeleQQOne = mWatchInfo.getString("QQOne", "");
					mTeleQQTwo = mWatchInfo.getString("QQTwo", "");
					mTeleQQThree = mWatchInfo.getString("QQThree", "");
				}
			} else if (result.contains("中心号码")) {
				if (result.contains("设置成功")) {
					Toast.makeText(SettingActivity.this, "中心号码设置成功",
							Toast.LENGTH_SHORT).show();
					mWatchInfo.edit().putString("Centre", mTeleCenter).commit();
					success = true;
					tag = SETTING_TELECENTER;
					// 上传到网络
				} else {
					Toast.makeText(SettingActivity.this, "中心号码设置失败，请重试!",
							Toast.LENGTH_SHORT).show();
					mTeleCenter = mWatchInfo.getString("Centre", "");
				}
			} else if (result.contains("GPS")) {
				if (result.contains("设置成功")) {
					Toast.makeText(SettingActivity.this, "GPS切换成功",
							Toast.LENGTH_SHORT).show();
					System.out
							.println("the state gps------------------------------>"
									+ mGpsOn);
					mWatchInfo.edit().putBoolean("GpsIsOpen", mGpsOn).commit();
					success = true;
				} else {
					Toast.makeText(SettingActivity.this, "GPS切换失败，请重试!",
							Toast.LENGTH_SHORT).show();
					mGpsOn = mWatchInfo.getBoolean("GpsIsOpen", true);
				}
			} else if (result.contains("电子围栏")) {
				if (result.contains("设置成功")) {
					Toast.makeText(SettingActivity.this, "电子围栏切换成功",
							Toast.LENGTH_SHORT).show();
					System.out
							.println("the state wall----------------------------->"
									+ mWallOn);
					mWatchInfo.edit().putBoolean("WallIsOpen", mWallOn)
							.commit();
					success = true;
				} else {
					Toast.makeText(SettingActivity.this, "电子围栏切换失败，请重试!",
							Toast.LENGTH_SHORT).show();
					mWallOn = mWatchInfo.getBoolean("WallIsOpen", false);
				}
			} else if (result.contains("密码修改")) {
				if (result.contains("设置成功")) {
					Toast.makeText(SettingActivity.this, "密码修改成功",
							Toast.LENGTH_SHORT).show();
					mWatchInfo.edit().putString("password", mDeviceKye)
							.commit();
					success = true;
					tag = SETTING_DEVICEKEY;
				} else {
					Toast.makeText(SettingActivity.this, "密码修改失败，请重试!",
							Toast.LENGTH_SHORT).show();
					mDeviceKye = mWatchInfo.getString("password", "0000");
				}
			} else {
				Toast.makeText(SettingActivity.this, "wait返回为空",
						Toast.LENGTH_SHORT).show();
			}
			if (success) {
				updateFragment(tag);
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			startProgressDialog();
		}

		private void startProgressDialog() {
			if (mProgressDialog == null) {
				mProgressDialog = MyProgressDialog
						.createDialog(SettingActivity.this);
				mProgressDialog.setMessage("正在发送，请耐心等待...");
			}
			mProgressDialog.show();
		}

		private void stopProgressDialog() {
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
				mProgressDialog = null;
			}
		}

		private String waitMessageResult() {
			String result = "";
			while (mRunning) {
				result = LocationToChildApplication.mMessageResult;
				if (result.contains("设置成功") || result.contains("设置失败")) {
					LocationToChildApplication.mMessageResult = "";
					break;
				}
			}
			mRunning = false;
			return result;
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

}
