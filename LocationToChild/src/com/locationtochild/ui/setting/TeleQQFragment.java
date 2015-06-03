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

public class TeleQQFragment extends Fragment {
	private View mTelephoneView;
	// 界面控件
	private EditText mTelephone1Text;
	private EditText mTelephone2Text;
	private EditText mTelephone3Text;
	private Button mTeleSubmit;
	// back操作
	private OnSettingItemSelect mListener;
	private TopTitleBar mTeleQQTitle;
	// 获取亲情号码
	private String mQQOne;
	private String mQQTwo;
	private String mQQThree;
	private int mQQnum;
	private String []mNum=new String[3];;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 获取初始值
		Bundle bundle = this.getArguments();
		mQQOne = bundle.getString("qq_one");
		mQQTwo = bundle.getString("qq_two");
		mQQThree = bundle.getString("qq_three");

		if (container == null)// 如果父容器值为Null,意味着该碎片不可见
			return null;
		mTelephoneView = (View) inflater.inflate(
				R.layout.fragment_setting_telepqq, container, false);
		initView();
		initListener();
		initData();
		return mTelephoneView;
	}

	private void initView() {
		mTelephone1Text = (EditText) mTelephoneView
				.findViewById(R.id.setting_teleqq_one);
		mTelephone2Text = (EditText) mTelephoneView
				.findViewById(R.id.setting_teleqq_two);
		mTelephone3Text = (EditText) mTelephoneView
				.findViewById(R.id.setting_teleqq_three);
		mTeleSubmit = (Button) mTelephoneView
				.findViewById(R.id.setting_teleqq_submit);
		mTeleQQTitle = (TopTitleBar) mTelephoneView
				.findViewById(R.id.title_setting_teleqq);
	}

	private void initListener() {
		mTeleSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 保存当前修改操作
				if(!checkInput()){
					return;
				}
				if(mQQnum==1){
					mListener.settingMessage(SettingsConstants.FAMILY_NUMBER, "one", mNum[0]);
				}else if(mQQnum==2){
					mListener.settingMessage(SettingsConstants.FAMILY_NUMBER, "two", mNum[0], mNum[1]);
				}else if(mQQnum==3){
					mListener.settingMessage(SettingsConstants.FAMILY_NUMBER, "three", mNum[0], mNum[1],mNum[2]);
				}
			}
		});
		// 返回上一页
		mTeleQQTitle.setTopTitleClickListener(new OnTopTitleClickListener() {
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

	// 初始化亲情号码信息,使用网络来进行同步管理
	private void initData() {
		mTelephone1Text.setText(mQQOne);
		mTelephone2Text.setText(mQQTwo);
		mTelephone3Text.setText(mQQThree);
	}

	// 判断输入数据
	private boolean checkInput() {
		mQQnum=0;
		if (mTelephone1Text.getText().toString().trim().length() == 11) {
			mNum[mQQnum]=mTelephone1Text.getText().toString();
			mQQnum++;
		} else if (mTelephone1Text.getText().toString().trim().length() > 0) {
			Toast.makeText(this.getActivity(), "请输全亲情号码1", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		if (mTelephone2Text.getText().toString().trim().length() == 11) {
			mNum[mQQnum]=mTelephone2Text.getText().toString();
			if(mQQnum<1){
				Toast.makeText(this.getActivity(), "请按顺序输入亲情号码", Toast.LENGTH_SHORT)
				.show();
			}
			mQQnum++;
		} else if (mTelephone2Text.getText().toString().trim().length() > 0) {
			Toast.makeText(this.getActivity(), "请输全亲情号码2", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		if (mTelephone3Text.getText().toString().trim().length() == 11) {
			mNum[mQQnum]=mTelephone3Text.getText().toString();
			if(mQQnum<2){
				Toast.makeText(this.getActivity(), "请按顺序输入亲情号码", Toast.LENGTH_SHORT)
				.show();
			}
			mQQnum++;
		} else if (mTelephone3Text.getText().toString().trim().length() > 0) {
			Toast.makeText(this.getActivity(), "请输全亲情号码3", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		if (mQQnum<1) {
			Toast.makeText(this.getActivity(), "至少需要输入一个亲情号码",
					Toast.LENGTH_SHORT).show();
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
