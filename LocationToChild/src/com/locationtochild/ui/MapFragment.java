package com.locationtochild.ui;

import com.locationtochild.ui.R;
import com.locationtochild.ui.widget.TopTitleBar;
import com.locationtochild.ui.widget.TopTitleBar.OnTopTitleClickListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MapFragment extends Fragment{
	
	private View mMapView;
	private TopTitleBar mMapTitle;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(container == null)
			return null;
		
		mMapView = (View)inflater.inflate(R.layout.fragment_map, container, false);
		initView();
		setListener();
		return mMapView;
	}
	
	private void initView(){
		mMapTitle = (TopTitleBar)mMapView.findViewById(R.id.title_map);
		
	}
	
	private void setListener(){
		mMapTitle.setTopTitleClickListener(new OnTopTitleClickListener() {
			
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

	
}
