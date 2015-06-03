package com.locationtochild.ui.widget;

import com.locationtochild.ui.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TopTitleBar extends RelativeLayout{

	private View mTopTitle;
	private Context mContext;
	private ImageView mTitleLeft;
	private ImageView mTitleRight;
	private TextView mTitleText;
	private View mLeftLine;
	private View mRightLine;
	
	private TypedArray mType;
	
	private OnTopTitleClickListener mTopTitleListener;
	
	public TopTitleBar(Context context) {
		super(context);
		Log.i("tag", "TopTitleŒﬁ Ù–‘÷µ");
	}
	
	public TopTitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mTopTitle = LayoutInflater.from(mContext).inflate(R.layout.title_bar, null);
		mType = context.obtainStyledAttributes(attrs,R.styleable.TopTitle);
		initView();
		initAttrs();
		mType.recycle();
		setListener();
		LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		addView(mTopTitle,lp);
	}

	private void initView(){
		mTitleLeft = (ImageView)mTopTitle.findViewById(R.id.title_left);
		mTitleRight = (ImageView)mTopTitle.findViewById(R.id.title_right);
		mTitleText = (TextView)mTopTitle.findViewById(R.id.title_text);
		mLeftLine = (View)mTopTitle.findViewById(R.id.left_line);
		mRightLine = (View)mTopTitle.findViewById(R.id.right_line);
	}
	
	private void initAttrs(){
		mTitleText.setText(mType.getString(R.styleable.TopTitle_titleText));
		mTitleLeft.setImageDrawable(mType.getDrawable(R.styleable.TopTitle_leftSrc));
		mTitleRight.setImageDrawable(mType.getDrawable(R.styleable.TopTitle_rightSrc));
	}
	
	private void setListener(){
		mTitleLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mTopTitleListener != null)
					mTopTitleListener.onLeftClick();
			}
		});
		
		mTitleRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mTopTitleListener != null)
					mTopTitleListener.onRightClick();
			}
		});
	}
	
	public void setTopTitleClickListener(OnTopTitleClickListener topListener){
		this.mTopTitleListener = topListener; 
	}
	
	public void setLeftLineVisibility(int vs){
		mLeftLine.setVisibility(vs);
	}
	
	public void setRightLineVisibility(int vs){
		mRightLine.setVisibility(vs);
		Log.i("tag", "RIGHT");
	}

	
	public interface OnTopTitleClickListener{
		void onLeftClick();
		void onRightClick();
	}
}
