package com.nt.utils;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nt.R;

/**
 * 自定义simpleAdapter,带点击选中效果
 * 
 * @author Cheng
 * 
 */
public class MySimpleAdapter extends BaseAdapter {

	private Context mContext;
	private List<Map<String, String>> mdate;
	public static int mposition = -1;
	private int mposition_tmp = -1;
	private int mresource;
	private String[] mfrom;
	private int[] mto;
	private View[] itemViews;
	private LayoutInflater inflater;

	/**
	 * 参见SimpleAdapter
	 * 
	 * @param context
	 * @param data
	 * @param resource
	 * @param from
	 * @param to
	 */
	public MySimpleAdapter(Context context, List<Map<String, String>> data,
			int resource, String[] from, int[] to) {

		itemViews = new View[data.size()];

		mposition = -1;
		this.mContext = context;
		this.mdate = data;
		this.mresource = resource;
		this.mfrom = from;
		this.mto = to;
		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// makeItemView();

	}

	// private View[] makeItemView() {
	//
	// for (int i = 0; i < itemViews.length; i++) {
	// View itemView = inflater.inflate(mresource, null);
	// for (int j = 0; j < mto.length; j++) {
	// // 这里先只做View为TextView的
	// ((TextView) itemView.findViewById(mto[j])).setText(mdate.get(i)
	// .get(mfrom[j]));
	// }
	// itemViews[i] = itemView;
	// }
	//
	// return itemViews;
	// }

	private View MakeItemView(int postion) {

		View itemView = inflater.inflate(mresource, null);
		for (int j = 0; j < mto.length; j++) {
			// 这里先只做View为TextView的
			((TextView) itemView.findViewById(mto[j])).setText(mdate.get(
					postion).get(mfrom[j]));
		}
		itemViews[postion] = itemView;

		return itemView;
	}

	@Override
	public int getCount() {

		return mdate.size();
	}

	@Override
	public Object getItem(int position) {

		return mdate.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int positions, View convertView, ViewGroup parent) {
		// Log.e("dd", positions+"makeItemView");
		// Log.e("dd", mposition+"makeItemView");
		// Log.e("dd", mposition_tmp+"makeItemView");
		if (mposition != -1 && mposition != mposition_tmp) {
			// 不是初始发，且不是滑动（也就剩点击了吧、、我是这么认为的）的时候
			return makeItemView(positions);
		}

		return MakeItemView(positions);

	}

	/**
	 * 为了那点击效果
	 * 
	 * @param positions
	 * @return
	 */
	private View makeItemView(int positions) {
		// Log.e("dd", "makeItemView");
		View convertView = itemViews[positions];
		if (positions == mposition_tmp) {
			// 当getView的位置（position）为上次点击position
			((TextView) convertView.findViewById(R.id.tV_number))
					.setTextColor(Color.parseColor("#ff0190eb"));
			((TextView) convertView.findViewById(R.id.tV_name))
					.setTextColor(Color.parseColor("#ff0190eb"));
			((LinearLayout) convertView.findViewById(R.id.llitem))
					.setBackgroundColor(Color.WHITE);
			if (mposition <= mposition_tmp) {// 点击position<=上次position
				// 更新mposition_tmp
				mposition_tmp = mposition;
			}
		}
		if (positions == mposition) {
			// 当getView的位置（position）为点击position
			// Log.e("dd", "点击位置 =  positions");
			((TextView) convertView.findViewById(R.id.tV_number))
					.setTextColor(Color.WHITE);
			((TextView) convertView.findViewById(R.id.tV_name))
					.setTextColor(Color.WHITE);
			((LinearLayout) convertView.findViewById(R.id.llitem))
					.setBackgroundColor(Color.parseColor("#ff0190eb"));
			if (mposition > mposition_tmp) {
				// 更新mposition_tmp
				mposition_tmp = mposition;
			}
		}
//		return convertView;
		return itemViews[positions];
	}
}
