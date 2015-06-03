package com.locationtochild.ui.setting;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.locationtochild.ui.R;
import com.locationtochild.ui.setting.SettingFragment.OnSettingItemSelect;
import com.locationtochild.ui.widget.TopTitleBar;
import com.locationtochild.ui.widget.TopTitleBar.OnTopTitleClickListener;
import com.locationtochild.utils.Constants;
import com.locationtochild.utils.Constants.SettingsConstants;

public class TeleCenterFragment extends Fragment {
	private final int TEXTNORMAL = 0xff585b5b;
	private final int TEXTSELECT = 0xff36B9AF;
	private View mTeleCenterView;
	private TextView mTeleQQOne;
	private TextView mTeleQQTwo;
	private TextView mTeleQQThree;
	private Button mTeleCenterSubmit;

	private OnSettingItemSelect mListener;
	private TopTitleBar mTeleCenterTitle;
	// 通过网络获取当前中心号码显示
	private TextView mOldTeleCenterText;
	private String mOldTeleCenterNum;
	private String mQQOneNum;
	private String mQQTwoNum;
	private String mQQThreeNum;
	private int mCheckId;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {// savedInstanceState为保存的状态包
		// 获取初始值
		Bundle bundle = this.getArguments();
		mOldTeleCenterNum = bundle.getString("tele_center");
		mQQOneNum = bundle.getString("qq_one");
		mQQTwoNum = bundle.getString("qq_two");
		mQQThreeNum = bundle.getString("qq_three");

		if (container == null)
			return null;
		mTeleCenterView = (View) inflater.inflate(
				R.layout.fragment_setting_telecenter, container, false);
		initView();
		setListener();
		initData();
		return mTeleCenterView;
	}

	private void initView() {
		mOldTeleCenterText = (TextView) mTeleCenterView
				.findViewById(R.id.setting_telecenter_old);
		mTeleQQOne = (TextView) mTeleCenterView
				.findViewById(R.id.telecenter_qq_one);
		mTeleQQTwo = (TextView) mTeleCenterView
				.findViewById(R.id.telecenter_qq_two);
		mTeleQQThree = (TextView) mTeleCenterView
				.findViewById(R.id.telecenter_qq_three);
		mTeleCenterSubmit = (Button) mTeleCenterView
				.findViewById(R.id.setting_telecenter_submit);
		mTeleCenterTitle = (TopTitleBar) mTeleCenterView
				.findViewById(R.id.title_setting_telecenter);
	}

	private void setListener() {
		// 设置号码单击事件
		mTeleQQOne.setOnClickListener(choose_tele);
		mTeleQQTwo.setOnClickListener(choose_tele);
		mTeleQQThree.setOnClickListener(choose_tele);

		mTeleCenterSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mCheckId < 1) {
					Toast.makeText(getActivity(), "请选择要切换的中心号码",
							Toast.LENGTH_SHORT).show();
					return;
				}
				String centerTele = null;
				switch (mCheckId) {
				case 1:
					centerTele = mQQOneNum;
					break;
				case 2:
					centerTele = mQQTwoNum;
					break;
				case 3:
					centerTele = mQQThreeNum;
					break;
				default:
					break;
				}
				if (mOldTeleCenterNum.equals(centerTele)) {
					Toast.makeText(getActivity(), "您选择的号码已经是中心号码！",
							Toast.LENGTH_LONG).show();
					return;
				}
				mListener.settingMessage(SettingsConstants.CENTRE_NUMBER,centerTele);
				// 调用程序设置中心号码
				System.out.println("submit center_tele"+centerTele);
			}
		});
		// 返回上一页
		mTeleCenterTitle
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

	// 设置单击事件
	private View.OnClickListener choose_tele = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v.getId() == R.id.telecenter_qq_one && mCheckId != 1) {
				mTeleQQOne.setTextColor(TEXTSELECT);
				mTeleQQTwo.setTextColor(TEXTNORMAL);
				mTeleQQThree.setTextColor(TEXTNORMAL);
				mCheckId = 1;
			} else if (v.getId() == R.id.telecenter_qq_two && mCheckId != 2) {
				mTeleQQOne.setTextColor(TEXTNORMAL);
				mTeleQQTwo.setTextColor(TEXTSELECT);
				mTeleQQThree.setTextColor(TEXTNORMAL);
				mCheckId = 2;
			} else if (v.getId() == R.id.telecenter_qq_three && mCheckId != 3) {
				mTeleQQOne.setTextColor(TEXTNORMAL);
				mTeleQQTwo.setTextColor(TEXTNORMAL);
				mTeleQQThree.setTextColor(TEXTSELECT);
				mCheckId = 3;
			}
		}
	};

	// 通过网络获取中心号码的初始信息
	public void initData() {
		mCheckId = 0;
		int flag = 0;
		if (mQQOneNum.equals("")) {
			mTeleQQOne.setVisibility(View.GONE);
		} else {
			if (mQQOneNum.equals(mOldTeleCenterNum)) {
				mCheckId = 1;
				mTeleQQOne.setTextColor(TEXTSELECT);
			}
			mTeleQQOne.setText(mQQOneNum);
			flag = flag + 1;
		}
		if (mQQTwoNum.equals("")) {
			mTeleQQTwo.setVisibility(View.GONE);
		} else {
			if (mQQTwoNum.equals(mOldTeleCenterNum)) {
				mTeleQQTwo.setTextColor(TEXTSELECT);
				mCheckId = 2;
			}
			mTeleQQTwo.setText(mQQTwoNum);
			flag = flag + 1;
		}
		if (mQQThreeNum.equals("")) {
			mTeleQQThree.setVisibility(View.GONE);
		} else {
			if (mQQThreeNum.equals(mOldTeleCenterNum)) {
				mTeleQQThree.setTextColor(TEXTSELECT);
				mCheckId = 3;
			}
			mTeleQQThree.setText(mQQThreeNum);
			flag = flag + 1;
		}
		if (flag < 1 || (flag == 1 && mCheckId > 0)) {
			// 设置按钮背景
			mTeleCenterSubmit.setBackgroundColor(getResources().getColor(
					R.color.theme_color));
			mTeleCenterSubmit.setOnClickListener(null);
			mTeleCenterSubmit.setText("设置多个亲情号即可更改中心号码");
		}
		// 获取当前中心号码，并显示到界面中
		if (mCheckId < 1) {
			mOldTeleCenterText.setText("未设置中心号码");
			mOldTeleCenterText.setGravity(Gravity.CENTER_HORIZONTAL);
		} else {
			mOldTeleCenterText.setText("当前中心号码：" + mOldTeleCenterNum);
		}
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
