package com.locationtochild.ui;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import com.locationtochild.logic.LocationToChildApplication;
import com.locationtochild.ui.InitSettingActivity.AsyncInitTask;
import com.locationtochild.ui.widget.ExitDialog;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private EditText mPhone;// 手机号
	private EditText mPassword;// 密码
	private Button mLogin;// 登录
	private TextView mRegister;// 注册账号
	private TextView mFindPwd;// 找回密码
	private SharedPreferences mUserInfo;
	private AsyncLoginTask mTask;
	private String mPhoneStr;
	private String mPasswordStr;
	private ProgressDialog mProgress;
	private String mDeviceID;// 消息推送所用的设备编号

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);

		initView();
		setLister();

		LocationToChildApplication.getInstance().addActivity(this);
	}

	private void initView() {
		mPhone = (EditText) findViewById(R.id.login_telephone);
		mPassword = (EditText) findViewById(R.id.login_password);
		mLogin = (Button) findViewById(R.id.login_btn);
		mRegister = (TextView) findViewById(R.id.register_btn);
		mFindPwd = (TextView) findViewById(R.id.find_pwd);
	}

	private void setLister() {
		mLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (inputIsSuccess()) {
					mPhoneStr = mPhone.getText().toString();
					mPasswordStr = mPassword.getText().toString();
					mDeviceID = Secure.getString(getContentResolver(),
							Secure.ANDROID_ID);// 消息推送所用的设备编号
					if (mTask != null
							&& mTask.getStatus() == AsyncInitTask.Status.RUNNING)
						mTask.cancel(true);
					mTask = new AsyncLoginTask();
					mTask.execute(mPhoneStr, mPasswordStr, mDeviceID);
				} else {
					mPhone.setText("");
					mPassword.setText("");
				}
			}
		});

		mRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity.this,
						RegisterActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.roll_up, R.anim.roll);
			}
		});

		mFindPwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
	}

	// 判断用户输入是否正确
	private boolean inputIsSuccess() {

		// 获取用户输入的信息
		String username = mPhone.getText().toString().trim();
		String password = mPassword.getText().toString().trim();
		if ("".equals(username)) {
			// 用户输入用户名为空
			Toast.makeText(LoginActivity.this, "用户名不能为空!", Toast.LENGTH_SHORT)
					.show();
			return false;
		} else if ("".equals(password)) {
			// 密码不能为空
			Toast.makeText(LoginActivity.this, "密码不能为空!", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		return true;
	}

	public class AsyncLoginTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			String code = "";
			try {
				String result = LocationToChildApplication.mHttpUtils.login(
						params[0], params[1], params[2]);
				JSONObject resultCode = new JSONObject(result);
				code = resultCode.getString("code");
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return code;
		}

		@Override
		protected void onPostExecute(String result) {
			stopProgressDialog();
			if (result.equalsIgnoreCase("11000")) {// 登录成功
				mUserInfo = getSharedPreferences("UserInfo", MODE_PRIVATE);

				Editor ed = mUserInfo.edit();
				ed.putString("username", mPhoneStr);
				ed.putString("password", mPasswordStr);
				ed.putBoolean("isLogin", true);
				ed.commit();

				if (!mUserInfo.getBoolean("isSetting", false)) {// 判断用户是否设置成功
					finish();
					Intent intent = new Intent(LoginActivity.this,
							InitSettingActivity.class);
					intent.putExtra("username", mPhoneStr);
					startActivity(intent);
					overridePendingTransition(R.anim.slide_right_in,
							R.anim.slide_left_out);
				} else {
					finish();
					Intent intent = new Intent(LoginActivity.this,
							MainActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.slide_right_in,
							R.anim.slide_left_out);
				}
			} else if (result.equalsIgnoreCase("11010")) {// 此用户不存在
				Toast.makeText(LoginActivity.this, "此用户不存在！",
						Toast.LENGTH_SHORT).show();
				mPhone.setText("");
				mPassword.setText("");
			} else if (result.equalsIgnoreCase("11011")) {// 密码错误
				Toast.makeText(LoginActivity.this, "密码错误！", Toast.LENGTH_SHORT)
						.show();
				mPassword.setText("");
			} else {
				Toast.makeText(LoginActivity.this, "网络错误，请重试！",
						Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			startProgressDialog();
		}
	}

	private void startProgressDialog() {
		if (mProgress == null) {
			mProgress = new ProgressDialog(this, R.style.myProgressDialog);
			mProgress.setMessage("正在登录中...");
		}

		mProgress.show();
	}

	private void stopProgressDialog() {
		if (mProgress != null) {
			mProgress.dismiss();
			mProgress = null;
		}
	}

	@Override
	public void onBackPressed() {
		ExitDialog mExit = new ExitDialog(this);
		mExit.show();
	}

}
