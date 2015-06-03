package com.locationtochild.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.locationtochild.logic.model.TimeAlarm;
import com.locationtochild.ui.R;
import com.locationtochild.utils.AlarmHelper;

// 设置提醒的适配器
public class AlarmSettingAdapter extends BaseAdapter {
	private static final String OPEN = "开";
	private static final String CLOSE = "关";
	private Context mContext;
	private List<TimeAlarm> mAlarmList;
	private LinearLayout mLinearParent;
	private ImageView mAlterOnFlag;
	private TextView mIsOnText;
	private TextView mIsOffText;

	public AlarmSettingAdapter(Context context, List<TimeAlarm> alarmList) {
		System.out.println("coming into adapter!");
		mContext = context;
		mAlarmList = alarmList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mAlarmList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mAlarmList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TimeAlarm entity = mAlarmList.get(position);
		boolean isOn = entity.getIsOn();
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_alarm_settings, null);
			holder.alarmTime = (TextView) convertView
					.findViewById(R.id.alarm_time);
			holder.alarmContent = (TextView) convertView
					.findViewById(R.id.alarm_content);
			holder.isOnOff=(LinearLayout)convertView.findViewById(R.id.alarm_ischoose);
			if (!isOn) {
				mIsOnText=(TextView)convertView.findViewById(R.id.alarm_ison);
				mIsOffText=(TextView)convertView.findViewById(R.id.alarm_isoff);
				mIsOnText.setText("");
				mIsOffText.setText(OPEN);
				holder.isOnOff.setBackgroundResource(R.drawable.switch_bg_white);
				mAlterOnFlag = (ImageView) convertView
						.findViewById(R.id.setting_image_falg);
				mAlterOnFlag.setImageResource(R.drawable.image_flag_greg);
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// 设置初始状态
		holder.alarmTime.setText(entity.getTime());
		holder.alarmContent.setText(entity.getDescription());
		// 设置闹钟的开关单击事件
		holder.isOnOff.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mIsOnText=(TextView)v.findViewById(R.id.alarm_ison);
				mIsOffText=(TextView)v.findViewById(R.id.alarm_isoff);
				mLinearParent = (LinearLayout) v.getParent().getParent();
				mAlterOnFlag = (ImageView) mLinearParent
						.findViewById(R.id.setting_image_falg);
				if (!mAlarmList.get(position).getIsOn()) {	
					mIsOnText.setText(CLOSE);
					mIsOffText.setText("");
					v.setBackgroundResource(R.drawable.switch_bg_red);
					mAlterOnFlag.setImageResource(R.drawable.image_flag_blue);
				} else {
					mIsOnText.setText("");
					mIsOffText.setText(OPEN);
					v.setBackgroundResource(R.drawable.switch_bg_white);
					mAlterOnFlag.setImageResource(R.drawable.image_flag_greg);
				}
				AlarmHelper.getInstance().changeAlarm(position, mContext);
			}
		});
		
		return convertView;
	}

	static class ViewHolder {
		public TextView alarmTime;
		public TextView alarmContent;
		public LinearLayout isOnOff;
	}

}
