package com.locationtochild.ui.widget;

import com.locationtochild.ui.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TabHost;

public class AnimationTabHost extends TabHost{
	
	private Animation slideLeftIn;
	private Animation slideLeftOut;
	private Animation slideRightIn;
	private Animation slideRightOut;

	public AnimationTabHost(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		slideLeftIn = AnimationUtils.loadAnimation(context,
				R.anim.slide_left_in);
		slideLeftOut = AnimationUtils.loadAnimation(context,
				R.anim.slide_left_out);
		slideRightIn = AnimationUtils.loadAnimation(context,
				R.anim.slide_right_in);
		slideRightOut = AnimationUtils.loadAnimation(context,
				R.anim.slide_right_out);
	}

	public AnimationTabHost(Context context) {
		this(context, null);
	}
	
	@Override
	public void setCurrentTab(int index) {
		// TODO Auto-generated method stub
		int currentTabId = getCurrentTab();
		View currentTabView = getCurrentTabView();
		
		// 定义画出屏幕动画
		if (currentTabView != null) {// 第一次进入该函数时候为空
			if (index > currentTabId) {
				getCurrentView().startAnimation(slideLeftOut);
			} else if (index < currentTabId) {
				getCurrentView().startAnimation(slideRightOut);
			}
		}
		
		// 定义进入屏幕动画
		if (currentTabView != null) {
			if (index > currentTabId) {
				getCurrentView().startAnimation(slideLeftIn);
			} else if (index < currentTabId) {
				getCurrentView().startAnimation(slideRightIn);
			}
		}

	}
}
