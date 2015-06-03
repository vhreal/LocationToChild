package com.locationtochild.ui.setting;

import static com.locationtochild.utils.Constants.SETTING_DEVICEKEY;
import static com.locationtochild.utils.Constants.SETTING_TELECENTER;
import static com.locationtochild.utils.Constants.SETTING_TELEDEVICE;
import static com.locationtochild.utils.Constants.SETTING_TELEQQ;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.locationtochild.ui.MainActivity;
import com.locationtochild.ui.R;
import com.locationtochild.ui.widget.TopTitleBar;
import com.locationtochild.ui.widget.TopTitleBar.OnTopTitleClickListener;
import com.locationtochild.utils.Constants.SettingsConstants;

public class SettingFragment extends Fragment {
	// listener
	private OnSettingItemSelect mListener;
	private View mSettingView;
	// 设置控件
	private RelativeLayout mGpsLayout;
	private RelativeLayout mWallLayout;
	private RelativeLayout mDeviceKeyLayout;
	private RelativeLayout mTeleQQLayout;
	private RelativeLayout mTeleCenterLayout;
	private RelativeLayout mTeleDeviceLayout;
	// gps相关
	private boolean mGpsOn;
	private LinearLayout mGpsBg;
	private TextView mGpsOnText;
	private TextView mGpsOffText;
	// wall相关
	private boolean mWallOn;
	private LinearLayout mWallBg;
	private TextView mWallOnText;
	private TextView mWallOffText;

	// 跳转到首页
	private TopTitleBar mSettingTitle;

	@Override
	public void onCreate(Bundle savedInstanceState) {// 此回调获取传入的已保存包，它会尽可能早地创建一个后台线程来获取此碎片将需要的数据
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle bundle = this.getArguments();
		mWallOn = bundle.getBoolean("wall");
		mGpsOn = bundle.getBoolean("gps");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {// savedInstanceState为保存的状态包
		if (container == null)// 如果父容器值为Null,意味着该碎片不可见
			return null;
		mSettingView = (View) inflater.inflate(R.layout.fragment_setting_main,
				container, false);
		initView();
		initData();
		setListener();
		return mSettingView;
	}

	private void initView() {
		mSettingTitle = (TopTitleBar) mSettingView
				.findViewById(R.id.title_setting);
		// initial view
		mGpsLayout = (RelativeLayout) mSettingView
				.findViewById(R.id.setting_gps_layout);
		mWallLayout = (RelativeLayout) mSettingView
				.findViewById(R.id.setting_wall_layout);
		mDeviceKeyLayout = (RelativeLayout) mSettingView
				.findViewById(R.id.setting_devicekey_layout);
		mTeleQQLayout = (RelativeLayout) mSettingView
				.findViewById(R.id.setting_teleqq_layout);
		mTeleCenterLayout = (RelativeLayout) mSettingView
				.findViewById(R.id.setting_telecenter_layout);
		mTeleDeviceLayout = (RelativeLayout) mSettingView
				.findViewById(R.id.setting_teledevice_layout);
		// initial tag
		mDeviceKeyLayout.setTag(SETTING_DEVICEKEY);
		mTeleQQLayout.setTag(SETTING_TELEQQ);
		mTeleCenterLayout.setTag(SETTING_TELECENTER);
		mTeleDeviceLayout.setTag(SETTING_TELEDEVICE);
		// gps about
		mGpsBg = (LinearLayout) mSettingView
				.findViewById(R.id.setting_gps_ischoose);
		mGpsOnText = (TextView) mSettingView
				.findViewById(R.id.setting_gps_ison);
		mGpsOffText = (TextView) mSettingView
				.findViewById(R.id.setting_gps_isoff);
		// wall about
		mWallBg = (LinearLayout) mSettingView
				.findViewById(R.id.setting_wall_ischoose);
		mWallOnText = (TextView) mSettingView
				.findViewById(R.id.setting_wall_ison);
		mWallOffText = (TextView) mSettingView
				.findViewById(R.id.setting_wall_isoff);
	}

	// 从网络获取GPS与WALL的状态
	private void initData() {
		if (!mGpsOn) {
			changeGpsState(false);
		}
		if (!mWallOn) {
			changeWallState(false);
		}
	}

	private void setListener() {
		// 本次页面的操作
		mGpsLayout.setOnClickListener(select_switch_item);
		mWallLayout.setOnClickListener(select_switch_item);
		// 点击进入到细节操作
		mDeviceKeyLayout.setOnClickListener(select_item);
		mTeleQQLayout.setOnClickListener(select_item);
		mTeleCenterLayout.setOnClickListener(select_item);
		mTeleDeviceLayout.setOnClickListener(select_item);
		mSettingTitle.setTopTitleClickListener(new OnTopTitleClickListener() {
			@Override
			public void onRightClick() {
				// TODO Auto-generated method stub
			}

			@Override
			public void onLeftClick() {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getActivity(), MainActivity.class);
				startActivity(intent);
				getActivity().finish();
			}
		});

	}

	// 进入细节操作
	private OnClickListener select_item = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int tag = (Integer) v.getTag();
			// false代表着进入到细节操作！
			mListener.onItemSelected(tag, false);
		}
	};

	// 点击开关的操作
	private OnClickListener select_switch_item = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// GPS与电子围栏的单击事件,同步数据。
			String state="false";
			switch (v.getId()) {
			case R.id.setting_gps_layout:
				System.out.println("gps click");
				if(!mGpsOn){
					state="true";
				}
				mListener.settingMessage(SettingsConstants.GPS_STATE,state);
				break;
			case R.id.setting_wall_layout:
				System.out.println("wall click");
				if(!mWallOn){
					state="true";
				}
				mListener.settingMessage(SettingsConstants.WALL_STATE,state);
				break;
			default:
				break;
			}
		}
	};

	// GPS状态切换
	private void changeGpsState(boolean flag) {
		if (flag) {
			mGpsBg.setBackgroundResource(R.drawable.switch_bg_red);
			mGpsOnText.setText("关");
			mGpsOffText.setText("");
		} else {
			mGpsBg.setBackgroundResource(R.drawable.switch_bg_white);
			mGpsOnText.setText("");
			mGpsOffText.setText("开");
		}
	}

	// 电子围栏状态切换
	private void changeWallState(boolean flag) {
		if (flag) {
			mWallBg.setBackgroundResource(R.drawable.switch_bg_red);
			mWallOnText.setText("关");
			mWallOffText.setText("");
		} else {
			mWallBg.setBackgroundResource(R.drawable.switch_bg_white);
			mWallOnText.setText("");
			mWallOffText.setText("开");
		}
	}

	public interface OnSettingItemSelect {
		public void onItemSelected(int TAG, boolean flag);
		public void settingMessage(String... params);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			mListener = (OnSettingItemSelect) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ "must implement onSettingItemClick!");
		}
	}

}
