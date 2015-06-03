package com.locationtochild.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.locationtochild.ui.widget.TopTitleBar;
import com.locationtochild.ui.widget.TopTitleBar.OnTopTitleClickListener;
import com.locationtochild.utils.Constants.MoreDetail;

public class MoreFragment extends Fragment {
	// 定义布局文件
	private RelativeLayout mMoreAbout;
	private RelativeLayout mMoreHelp;
	private RelativeLayout mMoreBack;
	private View mMoreView;
	private TopTitleBar mMoreTitle;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null)
			return null;
		mMoreView = (View) inflater.inflate(R.layout.fragment_more, null);
		initView();
		setListener();
		return mMoreView;
	}

	private void initView() {
		mMoreTitle = (TopTitleBar) mMoreView.findViewById(R.id.title_more);
		mMoreAbout = (RelativeLayout) mMoreView
				.findViewById(R.id.more_about_layout);
		mMoreBack = (RelativeLayout) mMoreView
				.findViewById(R.id.more_feedback_layout);
		mMoreHelp = (RelativeLayout) mMoreView
				.findViewById(R.id.more_help_layout);
	}

	private void setListener() {
		mMoreAbout.setOnClickListener(click_more);
		mMoreBack.setOnClickListener(click_more);
		mMoreHelp.setOnClickListener(click_more);
		mMoreTitle.setTopTitleClickListener(new OnTopTitleClickListener() {

			@Override
			public void onRightClick() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLeftClick() {
				// TODO Auto-generated method stub

			}
		});
	}

	// 跳转到相应的界面
	private OnClickListener click_more = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.more_about_layout:
				changedTo(MoreDetail.MORE_ABOUT);
				break;
			case R.id.more_feedback_layout:
				changedTo(MoreDetail.MORE_BACK);
				break;
			case R.id.more_help_layout:
				changedTo(MoreDetail.MORE_HELP);
				break;
			default:
				break;
			}
		}
	};

	private void changedTo(int tag) {
		// 跳转到细节片段中
		Intent intent=new Intent();
		intent.putExtra("TAG", tag);
		intent.setClass(getActivity(),MoreDetailActivity.class);
		startActivity(intent);
	}
}
