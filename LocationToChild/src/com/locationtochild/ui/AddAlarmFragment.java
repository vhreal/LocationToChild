package com.locationtochild.ui;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.locationtochild.logic.model.TimeAlarm;
import com.locationtochild.utils.AlarmHelper;

public class AddAlarmFragment extends Fragment {
	private View mAddAlarmView;
	private static int WEEKEND = 2;
	private static int WORKDAY = 1;
	private static int FREE = 0;
	private static int WORKDAYNUM = 31;
	private static int WEEKENDNUM = 96;
	private int mFlag;
	private TimeAlarm mTimeAlarm;
	private int mPosition = -1;
	private boolean[] mArray;
	private static String mFree = "自定义";
	private static String mWorkday = "工作日";
	private static String mWorkend = "周末";
	// 设置显示的日期
	private LinearLayout mTimeShow;
	private TextView mTimeText;
	private RadioGroup mRadioAlarmType;
	private TextView mAlarmType;
	// 设置周期选择
	private TableLayout mTypeTable;
	private ImageView mImageMon;
	private ImageView mImageTus;
	private ImageView mImageWed;
	private ImageView mImageThu;
	private ImageView mImageFri;
	private ImageView mImageSat;
	private ImageView mImageSun;
	// 设置保存和取消按钮
	private Button mSubmit;
	private Button mCancel;
	private Button mDelete;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null)// 如果父容器值为Null,意味着该碎片不可见
			return null;
		mPosition = getArguments().getInt("position");
		System.out.println("the position is " + mPosition);
		mAddAlarmView = (View) inflater.inflate(R.layout.fragment_add_alarm,
				container, false);
		initView();
		// 初始化数据信息
		initData();
		initListener();
		return mAddAlarmView;
	}

	public void initView() {
		mTimeShow = (LinearLayout) mAddAlarmView
				.findViewById(R.id.add_alarm_first);
		mTimeText = (TextView) mAddAlarmView.findViewById(R.id.alarm_time_text);
		mAlarmType = (TextView) mAddAlarmView
				.findViewById(R.id.alarm_type_text);
		mTypeTable = (TableLayout) mAddAlarmView
				.findViewById(R.id.add_alarm_table);
		// 周期选择
		mImageMon = (ImageView) mAddAlarmView.findViewById(R.id.add_alarm_mon);
		mImageTus = (ImageView) mAddAlarmView.findViewById(R.id.add_alarm_tus);
		mImageWed = (ImageView) mAddAlarmView.findViewById(R.id.add_alarm_wen);
		mImageThu = (ImageView) mAddAlarmView.findViewById(R.id.add_alarm_thu);
		mImageFri = (ImageView) mAddAlarmView.findViewById(R.id.add_alarm_fri);
		mImageSat = (ImageView) mAddAlarmView.findViewById(R.id.add_alarm_sat);
		mImageSun = (ImageView) mAddAlarmView.findViewById(R.id.add_alarm_sun);
		// 提醒类型
		mRadioAlarmType = (RadioGroup) mAddAlarmView
				.findViewById(R.id.alarm_type_radio);
		// 保存取消
		mSubmit = (Button) mAddAlarmView.findViewById(R.id.add_alarm_submit);
		mCancel = (Button) mAddAlarmView.findViewById(R.id.add_alarm_cancel);
		mDelete = (Button) mAddAlarmView.findViewById(R.id.add_alarm_delete);
	}

	public void initListener() {
		mTimeShow.setOnClickListener(choose_time);
		// 日期选择函数
		mImageMon.setOnClickListener(choose_day);
		mImageTus.setOnClickListener(choose_day);
		mImageWed.setOnClickListener(choose_day);
		mImageThu.setOnClickListener(choose_day);
		mImageFri.setOnClickListener(choose_day);
		mImageSat.setOnClickListener(choose_day);
		mImageSun.setOnClickListener(choose_day);
		// 选择提醒类型
		mRadioAlarmType.setOnCheckedChangeListener(choose_type);
		// 保存和取消事件
		mSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 获取提醒的条目信息，添加到对应的目录。包括周期数组、是否开启、时间序列、描述
				// --------------------------------------修改
				// 初始化提醒的整形数据
				boolean flag = true;
				int dayOfWeek = computeDayOfWeek();
				if (dayOfWeek == 0) {
					Toast.makeText(getActivity(), "请选择提醒周期", Toast.LENGTH_LONG)
							.show();
					return;
				}
				if (mTimeAlarm.getTime().equals(mTimeText.getText().toString())
						&& mTimeAlarm.getDayOfWeek() == dayOfWeek) {
					flag = false;
					System.out.println("no changing the data");
				}
				mTimeAlarm.setDayOfWeek(dayOfWeek);
				mTimeAlarm.setTime(mTimeText.getText().toString());
				if (AlarmHelper.getInstance().hasExist(getActivity(),mTimeAlarm.getTime())) {
					showDialog();
					return;
				}
				// 更新数据的描述
				if (mPosition < 0) {
					mTimeAlarm.setIsOn(true);
					AlarmHelper.getInstance().addAlarm(mTimeAlarm,
							getActivity());
				} else {
					AlarmHelper.getInstance().updateAlarm(mPosition,
							getActivity(), flag);
				}
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
		mCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
		// 删除事件
		mDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlarmHelper.getInstance().removeAlarm(mPosition, getActivity());
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
	}

	// 显示点击反馈后的提示框
	private void showDialog() {
		new AlertDialog.Builder(getActivity())
				.setTitle("重复提醒")
				.setMessage("此提醒已存在！")
				.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								chooseTimeFun();
							}
						}).show();
	}

	// 初始化数据信息
	public void initData() {
		mArray = new boolean[7];
		if (mPosition >= 0) {
			// 获取修改对象
			mTimeAlarm = AlarmHelper.getInstance().getAlarmList(getActivity())
					.get(mPosition);
			mTimeText.setText(mTimeAlarm.getTime());
			// 判断是类型是否为自定义,并初始化图标
			dealArray(mTimeAlarm.getDayOfWeek());
		} else {
			// 初始化新的对象
			mTimeAlarm = new TimeAlarm();
			mTimeAlarm.setTime("08:00");
			mFlag = WORKDAY;
		}
		if (mFlag != FREE) {
			mTypeTable.setVisibility(View.INVISIBLE);
		}
	}

	// 处理初始化的数组信息
	private void dealArray(int dayOfWeek) {
		// 初始化日期的选择类型
		if (dayOfWeek == WORKDAYNUM) {
			mFlag = WORKDAY;
		} else if (dayOfWeek == WEEKENDNUM) {
			mFlag = WEEKEND;
		} else {
			mFlag = FREE;
		}
		if (mFlag == FREE) {
			mRadioAlarmType.check(R.id.radio_free);
			setArrayList(dayOfWeek);
		} else if (mFlag == WEEKEND) {
			mRadioAlarmType.check(R.id.radio_weekend);
		}
	}

	// 根据日期数据初始化日期数组
	public void setArrayList(int dayOfWeek) {
		for (int i = 0; i < mArray.length; i++) {
			if ((dayOfWeek >> i) % 2 == 1) {
				mArray[i] = true;
				setChecked(i, true);
			}
		}
	}

	// 根据数组初始化周期选择图标
	public void setChecked(int position, boolean flag) {
		int id = 0;
		switch (position) {
		case 0:
			id = R.id.add_alarm_mon;
			break;
		case 1:
			id = R.id.add_alarm_tus;
			break;
		case 2:
			id = R.id.add_alarm_wen;
			break;
		case 3:
			id = R.id.add_alarm_thu;
			break;
		case 4:
			id = R.id.add_alarm_fri;
			break;
		case 5:
			id = R.id.add_alarm_sat;
			break;
		case 6:
			id = R.id.add_alarm_sun;
			break;
		default:
			break;
		}
		if (id > 0) {
			ImageView firstIn = (ImageView) mAddAlarmView.findViewById(id);
			if (flag)
				firstIn.setImageResource(R.drawable.ocheck_type_day);
			else {
				firstIn.setImageResource(R.drawable.check_type_day);
			}
		}
	}

	// 选择时间
	private OnClickListener choose_time = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			System.out.println("click time show picker");
			chooseTimeFun();
		}
	};
	// 
	private void chooseTimeFun(){
		int hour = Integer.parseInt(mTimeText.getText().toString()
				.substring(0, 2));
		int mini = Integer.parseInt(mTimeText.getText().toString()
				.substring(3));
		new TimePickerDialog(getActivity(), timePickerListen, hour, mini,
				true).show();
	}

	// 日期选择类
	TimePickerDialog.OnTimeSetListener timePickerListen = new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			String hour;
			if (hourOfDay < 10) {
				hour = "0" + hourOfDay + ":";
			} else {
				hour = "" + hourOfDay + ":";
			}
			if (minute < 10) {
				mTimeText.setText(hour + "0" + minute);
			} else {
				mTimeText.setText(hour + minute);
			}
		}
	};

	// 选择类型显示到选择周期栏
	private RadioGroup.OnCheckedChangeListener choose_type = new RadioGroup.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			if (checkedId == R.id.radio_free) {
				mTypeTable.setVisibility(View.VISIBLE);
				mAlarmType.setText(mFree);
				mFlag = FREE;
			} else if (checkedId == R.id.radio_weekend) {
				mTypeTable.setVisibility(View.INVISIBLE);
				mAlarmType.setText(mWorkend);
				mFlag = WEEKEND;
			} else {
				mTypeTable.setVisibility(View.INVISIBLE);
				mAlarmType.setText(mWorkday);
				mFlag = WORKDAY;
			}
		}
	};

	// 自定义选择提醒周期
	private OnClickListener choose_day = new OnClickListener() {
		@Override
		public void onClick(View v) {
			ImageView image = (ImageView) v;
			boolean flag = false;
			int i = -1;
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.add_alarm_mon:
				i = 0;
				if (mArray[i]) {
					flag = true;
				}
				break;
			case R.id.add_alarm_tus:
				i = 1;
				if (mArray[i]) {
					flag = true;
				}
				break;
			case R.id.add_alarm_wen:
				i = 2;
				if (mArray[i]) {
					flag = true;
				}
				break;
			case R.id.add_alarm_thu:
				i = 3;
				if (mArray[i]) {
					flag = true;
				}
				break;
			case R.id.add_alarm_fri:
				i = 4;
				if (mArray[i]) {
					flag = true;
				}
				break;
			case R.id.add_alarm_sat:
				i = 5;
				if (mArray[i]) {
					flag = true;
				}
				break;
			case R.id.add_alarm_sun:
				i = 6;
				if (mArray[i]) {
					flag = true;
				}
				break;
			default:
				break;
			}
			if (i >= 0) {
				if (flag) {
					image.setImageResource(R.drawable.check_type_day);
					mArray[i] = false;
				} else {
					image.setImageResource(R.drawable.ocheck_type_day);
					mArray[i] = true;
				}
			}
		}
	};

	// 获取提醒周期
	private int computeDayOfWeek() {
		int sum = 0;
		if (mFlag == WEEKEND) {
			sum = WEEKENDNUM;
		} else if (mFlag == WORKDAY) {
			sum = WORKDAYNUM;
		} else {
			for (int i = 0; i < mArray.length; i++) {
				if (mArray[i]) {
					sum = sum + (int) (Math.pow(2, i));
				}
			}
		}
		System.out.println("the day of week is " + sum);
		return sum;
	}

}
