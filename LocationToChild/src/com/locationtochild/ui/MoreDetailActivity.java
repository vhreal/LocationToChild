package com.locationtochild.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.locationtochild.ui.widget.TopTitleBar;
import com.locationtochild.ui.widget.TopTitleBar.OnTopTitleClickListener;
import com.locationtochild.utils.Constants.MoreDetail;

public class MoreDetailActivity extends Activity {
	private Button mSubmitBack;
	private EditText mBackText;
	private TopTitleBar mTitleDetailBar;
	private int mTag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Bundle bundle = getIntent().getExtras();
		mTag = bundle.getInt("TAG");
		System.out.println("------------------------------------------------>"+mTag);
		// mTag = getIntent().getIntExtra("TAG", MoreDetail.MORE_ABOUT);
		switch (mTag) {
		case MoreDetail.MORE_ABOUT:
			setContentView(R.layout.fragment_more_about);
			mTitleDetailBar = (TopTitleBar)findViewById(R.id.title_more_about);
			break;
		case MoreDetail.MORE_BACK:
			setContentView(R.layout.fragment_more_back);
			mTitleDetailBar = (TopTitleBar)findViewById(R.id.title_more_back);
			initBack();
			break;
		case MoreDetail.MORE_HELP:
			setContentView(R.layout.fragment_more_help);
			mTitleDetailBar = (TopTitleBar)findViewById(R.id.title_more_help);
			break;
		default:
			break;
		}

		setListener();
	}

	// 设置提交操作
	private void initBack() {
		mBackText=(EditText)findViewById(R.id.more_feedback_text);
		mSubmitBack = (Button) findViewById(R.id.more_feedback_submit);
		mSubmitBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mBackText.getText().toString().trim().length()<1){
					mBackText.setHint("请输入您的反馈信息");
					return;
				}
				// 跳出提示框
				showDialog();
			}
		});
	}

	// 显示点击反馈后的提示框
	private void showDialog() {
		new AlertDialog.Builder(this)
				.setTitle("反馈成功")
				.setPositiveButton("感谢您的反馈！",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								finish();
							}
						}).show();
	}

	// 设置顶部事件监听
	private void setListener() {

		mTitleDetailBar.setTopTitleClickListener(new OnTopTitleClickListener() {

			@Override
			public void onRightClick() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLeftClick() {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
