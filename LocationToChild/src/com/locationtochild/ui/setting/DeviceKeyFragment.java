package com.locationtochild.ui.setting;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.locationtochild.ui.R;
import com.locationtochild.ui.setting.SettingFragment.OnSettingItemSelect;
import com.locationtochild.ui.widget.TopTitleBar;
import com.locationtochild.ui.widget.TopTitleBar.OnTopTitleClickListener;
import com.locationtochild.utils.Constants;
import com.locationtochild.utils.Constants.SettingsConstants;

public class DeviceKeyFragment extends Fragment {
	private View mKeyView;
	// 控件
	private EditText mOriginPass;
	private EditText mNewPass;
	private EditText mNewPassCon;
	private Button mKeySubmit;
	private String mOriginPwd;
	// 返回操作
	private OnSettingItemSelect mListener;
	private TopTitleBar mKeyTitle;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 获取初始值
		Bundle bundle = this.getArguments();
		mOriginPwd = bundle.getString("device_key");

		if (container == null)// 如果父容器值为Null,意味着该碎片不可见
			return null;
		mKeyView = (View) inflater.inflate(R.layout.fragment_setting_passwd,
				container, false);
		initView();
		initListener();
		return mKeyView;
	}

	private void initView() {
		mOriginPass = (EditText) mKeyView.findViewById(R.id.setting_key_opass);
		mNewPass = (EditText) mKeyView.findViewById(R.id.setting_key_npass);
		mNewPassCon = (EditText) mKeyView.findViewById(R.id.setting_key_npass1);
		mKeySubmit = (Button) mKeyView.findViewById(R.id.setting_key_submit);
		mKeyTitle = (TopTitleBar) mKeyView.findViewById(R.id.title_setting_key);
	}

	private void initListener() {
		mKeySubmit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 调用密码修改函数
				if(checkInput()){
					mListener.settingMessage(SettingsConstants.DEVICE_KEY,mOriginPass.getText().toString(),mNewPass.getText().toString());
				}
			}
		});
		// 返回上一页
		mKeyTitle.setTopTitleClickListener(new OnTopTitleClickListener() {

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

	// 判断密码是否符合要求
	public boolean checkInput() {
		if (mOriginPass.getText().toString().trim().equals("")) {
			Toast.makeText(getActivity(), "当前密码不能为空！", Toast.LENGTH_SHORT)
					.show();
			return false;
		} else if (mNewPass.getText().toString().trim().equals("")) {
			Toast.makeText(getActivity(), "新密码不能为空！", Toast.LENGTH_SHORT)
					.show();
			return false;
		} else if (!mNewPass.getText().toString().trim()
				.equals(mNewPassCon.getText().toString().trim())) {
			Toast.makeText(getActivity(), "两次输入的新密码需一致！", Toast.LENGTH_SHORT)
					.show();
			return false;
		} else if (mNewPass.getText().toString().trim().length() != 4) {
			Toast.makeText(getActivity(), "所设置的新密码需为4位字母和数字的组合！",
					Toast.LENGTH_SHORT).show();
			return false;
		} else if (!mOriginPass.getText().toString().trim().equals(mOriginPwd)) {
			Toast.makeText(getActivity(), "密码错误！", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
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
