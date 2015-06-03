package com.locationtochild.ui.setting;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.locationtochild.ui.MainActivity;
import com.locationtochild.ui.R;
import com.locationtochild.ui.setting.SettingFragment.OnSettingItemSelect;
import com.locationtochild.ui.widget.TopTitleBar;
import com.locationtochild.ui.widget.TopTitleBar.OnTopTitleClickListener;
import com.locationtochild.utils.Constants;

public class TeleDeviceFragment extends Fragment {
	private View mDeviceTeleView;
	private EditText mDeviceTeleText;
	private Button mDeviceTeleSubmit;
	// 设置back事件
	private OnSettingItemSelect mListener;
	private TopTitleBar mDeviceTeleTitle;
	// 获取旧的设备号码
	private String mOldTeleDeviceNum;
	private TextView mOldTeleDeviceText;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {// savedInstanceState为保存的状态包
		// 获取初始值
		Bundle bundle = this.getArguments();
		mOldTeleDeviceNum = bundle.getString("tele_device");
		
		if (container == null)// 如果父容器值为Null,意味着该碎片不可见
			return null;
		mDeviceTeleView = (View) inflater.inflate(
				R.layout.fragment_setting_teledevice, container, false);
		initView();
		setListener();
		initData();
		return mDeviceTeleView;
	}

	private void initView() {
		mOldTeleDeviceText = (TextView) mDeviceTeleView
				.findViewById(R.id.setting_teledevice_text);
		mDeviceTeleText = (EditText) mDeviceTeleView
				.findViewById(R.id.setting_teledevice_edit);
		mDeviceTeleSubmit = (Button) mDeviceTeleView
				.findViewById(R.id.setting_teledevice_submit);
		mDeviceTeleTitle = (TopTitleBar) mDeviceTeleView
				.findViewById(R.id.title_setting_teledevice);
	}

	private void setListener() {
		mDeviceTeleSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (checkTelePhone()) {
					// 显示提示框提示用户将进行下一步的配置
					
					// 修改用户配置项，引导用户重新配置相关信息
					SharedPreferences sp = getActivity().getSharedPreferences("UserInfo", 0);
					sp.edit().putBoolean("isSetting", false).commit();
					Intent intent=new Intent();
					intent.setClass(getActivity(), MainActivity.class);
					startActivity(intent);
				}
			}
		});

		// 返回上一页
		mDeviceTeleTitle
				.setTopTitleClickListener(new OnTopTitleClickListener() {
					@Override
					public void onRightClick() {
						// TODO Auto-generated method stub
					}

					@Override
					public void onLeftClick() {
						// TODO Auto-generated method stub
						mListener.onItemSelected(Constants.SETTING_MAIN, true);
					}
				});
	}

	// 验证设备手机号是否符合规格
	public boolean checkTelePhone() {
		if (mDeviceTeleText.getText().toString().trim().length() != 11) {
			Toast.makeText(this.getActivity(), "请输入合法的设备号码", Toast.LENGTH_LONG)
			.show();
			return false;
		} else if(mDeviceTeleText.getText().toString().trim().equals(mOldTeleDeviceNum)){
			Toast.makeText(this.getActivity(), "新的设备号与原设备号相同", Toast.LENGTH_LONG)
			.show();
			return false;
		}
		else 
			return true;
	}

	// 设备号码，可以存放到本地配置文件中
	public void initData() {
		if(mOldTeleDeviceNum.equals("")){
			mOldTeleDeviceText.setText("未设置设备号码");
			mOldTeleDeviceText.setGravity(Gravity.CENTER_HORIZONTAL);
		}
		else 
			mOldTeleDeviceText.setText(mOldTeleDeviceNum);
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
