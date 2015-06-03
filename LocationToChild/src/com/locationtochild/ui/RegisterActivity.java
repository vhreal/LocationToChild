package com.locationtochild.ui;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import com.locationtochild.logic.LocationToChildApplication;
import com.locationtochild.ui.InitSettingActivity.AsyncInitTask;
import com.locationtochild.ui.widget.TopTitleBar;
import com.locationtochild.ui.widget.TopTitleBar.OnTopTitleClickListener;
import com.locationtochild.utils.Constants.UserConstants;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {

	private EditText mPhone;
	private EditText mPassword;
	private EditText mAgainPwd;
	private EditText mEmail;
	private TopTitleBar mRegisterTitle;
	private Button mRegister;
	private AsyncRegisterTask mTask;
	private ProgressDialog mProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);

		initView();
		setListener();
		
		LocationToChildApplication.getInstance().addActivity(this);
	}

	private void initView() {
		mPhone = (EditText) findViewById(R.id.register_telephone);
		mPassword = (EditText) findViewById(R.id.register_password);
		mAgainPwd = (EditText) findViewById(R.id.register_againpwd);
		mEmail = (EditText) findViewById(R.id.register_email);
		mRegisterTitle = (TopTitleBar) findViewById(R.id.title_register);
		mRegister = (Button) findViewById(R.id.register_btn);
		mRegisterTitle.setLeftLineVisibility(View.VISIBLE);
	}

	private void setListener() {
		mRegisterTitle.setTopTitleClickListener(new OnTopTitleClickListener() {

			@Override
			public void onRightClick() {

			}

			@Override
			public void onLeftClick() {
				finish();
				overridePendingTransition(R.anim.roll, R.anim.roll_down);
			}
		});

		mRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				verifyFormat();
			}
		});
	}

	private void verifyFormat() {
		String phone = mPhone.getText().toString();
		String password = mPassword.getText().toString();
		String again = mAgainPwd.getText().toString();
		String email = mEmail.getText().toString();
		if ("".equals(phone))
			Toast.makeText(this, "手机号不能为空!", Toast.LENGTH_SHORT).show();
		else if ("".equals(password))
			Toast.makeText(this, "密码不能为空!", Toast.LENGTH_SHORT).show();
		else if ("".equals(again))
			Toast.makeText(this, "验证密码不能为空!", Toast.LENGTH_SHORT).show();
		else if ("".equals(email))
			Toast.makeText(this, "验证邮箱不能为空!", Toast.LENGTH_SHORT).show();
		else {
			if (!pwdIsSame(password, again)) {
				Toast.makeText(this, "两次输入密码不一致!", Toast.LENGTH_SHORT).show();
				mPassword.setText("");
				mAgainPwd.setText("");
			} else if (!isEmailMode(email)) {
				Toast.makeText(this, "邮箱格式错误!", Toast.LENGTH_SHORT).show();
				mPassword.setText("");
				mAgainPwd.setText("");
				mEmail.setText("");
			} else {// 执行异步注册
				if (mTask != null
						&& mTask.getStatus() == AsyncInitTask.Status.RUNNING)
					mTask.cancel(true);
				mTask = new AsyncRegisterTask();
				mTask.execute(UserConstants.REGISTER, phone, password, email);
			}
		}
	}

	private boolean pwdIsSame(String first, String second) {
		if (first.equals(second)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isEmailMode(String emailStr) {
		String emailMode = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(emailMode);
		Matcher m = p.matcher(emailStr);
		return m.matches();
	}

	public class AsyncRegisterTask extends AsyncTask<String, Integer, Object> {

		@Override
		protected Object doInBackground(String... params) {
			String code = "";
			if (params[0].equalsIgnoreCase(UserConstants.REGISTER)) {
				try {
					String result = LocationToChildApplication.mHttpUtils
							.register(params[1], params[2], params[3]);
					JSONObject resultCode = new JSONObject(result);
					code = resultCode.getString("code");
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return code;
		}

		@Override
		protected void onPostExecute(Object result) {
			stopProgressDialog();
			Log.i("tag", "register------>" + result);
			String code = result.toString();
			if (code.equalsIgnoreCase("10000")) {
				Toast.makeText(RegisterActivity.this, "注册成功！",
						Toast.LENGTH_SHORT).show();
				finish();
				overridePendingTransition(R.anim.roll, R.anim.roll_down);
			} else if (code.equalsIgnoreCase("10010")) {// 已被注册
				Toast.makeText(RegisterActivity.this, "账号已被注册！",
						Toast.LENGTH_SHORT).show();
				mPhone.setText("");
				mPassword.setText("");
				mAgainPwd.setText("");
				mEmail.setText("");
			} else if (code.equalsIgnoreCase("10011")) {
				Toast.makeText(RegisterActivity.this, "注册失败，请重试！",
						Toast.LENGTH_SHORT).show();
			} else {//连接超时&请求超时
				Toast.makeText(RegisterActivity.this, "网络连接出现错误，请重试！",
						Toast.LENGTH_SHORT).show();
			}

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			startProgressDialog();
		}

	}

	private void startProgressDialog() {
		if (mProgress == null) {
			mProgress = new ProgressDialog(this, R.style.myProgressDialog);
			mProgress.setMessage("正在注册...");
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
		finish();
		overridePendingTransition(R.anim.roll, R.anim.roll_down);
	}

}
