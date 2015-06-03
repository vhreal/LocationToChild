package com.locationtochild.ui;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import com.locationtochild.logic.LocationToChildApplication;
import com.locationtochild.ui.InitSettingActivity.AsyncInitTask;
import com.locationtochild.ui.widget.MyProgressDialog;
import com.locationtochild.utils.HttpUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * 将所设置的信息上传到服务器
 * @author Administrator
 *
 */
public class SynchronousActivity extends Activity{
	
	private TextView mTip;
	private Button mAgain;
	private String mQQOne;
	private String mQQTwo;
	private String mQQThree;
	private String mCentre;
	private AsyncUpdateTask mTask;
	private String mTag = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sync);
		
		mTip = (TextView)findViewById(R.id.sync_tip);
		mAgain = (Button)findViewById(R.id.sync_btn);
		
		mAgain.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mTag.equalsIgnoreCase("InitSetting")){
					if(mTask != null && mTask.getStatus() == AsyncInitTask.Status.RUNNING)
						mTask.cancel(true);
					mTask = new AsyncUpdateTask();
					mTask.execute("InitSetting", "3", mQQOne, mQQTwo, mQQThree, mCentre);
				}
			}
		});
		
		whereIsIntent();
	}
	
	private void whereIsIntent(){
		Intent intent = getIntent();
		String where = intent.getStringExtra("where");
		Log.i("tag", "接收到where");
		if(where.equalsIgnoreCase("InitSetting")){
			Log.i("tag", "开始执行从本地取数据，准备上传");
			SharedPreferences sp = getSharedPreferences(LocationToChildApplication.mWatchNumber, MODE_PRIVATE);
			mQQOne = sp.getString("QQOne", "");
			mQQTwo = sp.getString("QQTwo", "");
			mQQThree = sp.getString("QQThree", "");
			mCentre = sp.getString("Centre", "");
			mTask = new AsyncUpdateTask();
			mTask.execute("InitSetting", "3", mQQOne, mQQTwo, mQQThree, mCentre);
		}else{
			
		}
	}
	
	public class AsyncUpdateTask extends AsyncTask<String, Integer, String[]>{

		private MyProgressDialog mProgressDialog;
		@Override
		protected String[] doInBackground(String... params) {
			String[] result = new String[3];
			result[0] = params[0];
			if(result[0].equalsIgnoreCase("InitSetting")){
				String[] qq = new String[3];
				qq[0] = params[2];
				qq[1] = params[3];
				qq[2] = params[4];
				String watch = getSharedPreferences("UserInfo", MODE_PRIVATE).getString("watch", "");
				try {
					
					String resultOne = HttpUtils.getInstance().postQQNumber(watch, "3", qq);
					String resultTwo = HttpUtils.getInstance().postCentreNumber(watch, params[5]);
					Log.i("tag", resultOne+"------"+resultTwo);
					JSONObject jo = new JSONObject(resultOne);
					String codeOne = jo.getString("code");
					jo = new JSONObject(resultTwo);
					String codeTwo = jo.getString("code");
					if(codeOne.equalsIgnoreCase("21000")&&codeOne.equalsIgnoreCase("22000")){
						result[1] = "success";
					}else if(codeOne.equalsIgnoreCase("21010")||codeTwo.equalsIgnoreCase("22010")){
						result[1] = "failure";
					}else{
						result[1] = "internet";//网络异常
					}
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
			}
			return result;
		}

		@Override
		protected void onPostExecute(String[] result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			stopProgressDialog();
			if(result[0].equalsIgnoreCase("InitSetting")){
				if(result[1].equalsIgnoreCase("success")){
					Log.i("tag", "上传成功!");
					Intent intent = new Intent(SynchronousActivity.this, MainActivity.class);
					startActivity(intent);
				}else if(result[1].equalsIgnoreCase("failure")){
					mTip.setText("上传失败，请检查网络再试！");
					mAgain.setVisibility(View.VISIBLE);
					mTag = result[0];
				}else{
					mTip.setText("网络发生异常，请检查网络再试！");
					mAgain.setVisibility(View.VISIBLE);
					mTag = result[0];
				}
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			startProgressDialog();
		}
		
		private void startProgressDialog(){  
	        if (mProgressDialog == null){  
	        	mProgressDialog = MyProgressDialog.createDialog(SynchronousActivity.this);
	            mProgressDialog.setMessage("正在发送，请耐心等待...");  
	        }   
	        mProgressDialog.show();  
	    }  
	      
	    private void stopProgressDialog(){  
	        if (mProgressDialog != null){  
	            mProgressDialog.dismiss();  
	            mProgressDialog = null;  
	        }  
	    }
		
	}
	
}
