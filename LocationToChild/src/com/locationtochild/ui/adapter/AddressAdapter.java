package com.locationtochild.ui.adapter;

import java.util.List;
import com.locationtochild.logic.model.LocationBean;
import com.locationtochild.ui.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AddressAdapter extends BaseAdapter{
	
	private List<LocationBean> mLocList;
	private Context mContext;

	public AddressAdapter (Context context, List<LocationBean> lb){
		mContext = context;
		mLocList = lb;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mLocList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mLocList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ItemHolder holder = null;
		LocationBean location = mLocList.get(position);
		if( convertView == null){
			holder = new ItemHolder();
			convertView = (View)LayoutInflater.from(mContext).inflate(R.layout.item_address, null);
			holder.mAddressStr = (TextView)convertView.findViewById(R.id.address_text);
			holder.mTime = (TextView)convertView.findViewById(R.id.address_time);
			convertView.setTag(holder);
		}else{
			holder = (ItemHolder) convertView.getTag();
		}
		holder.mAddressStr.setText(location.getAddressStr());
		holder.mTime.setText(location.getTime());
		return convertView;
	}
	
	public final class ItemHolder{
		public TextView mAddressStr;//地理位置信息
		public TextView mTime;
	}
	
}
