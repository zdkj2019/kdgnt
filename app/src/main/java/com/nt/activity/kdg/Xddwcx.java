package com.nt.activity.kdg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.activity.kc.KccxActivity;
import com.nt.activity.kdg.SmdwXj.MyLocationListener;
import com.nt.activity.main.MainActivity;
import com.nt.cache.DataCache;
import com.nt.cache.ServiceReportCache;
import com.nt.common.Constant;
import com.nt.utils.Config;

/**
 * 快递柜-选单定位
 * 
 * @author zdkj
 *
 */
public class Xddwcx extends FrameActivity {

	private ListView listView;
	private SimpleAdapter adapter;
	private String[] from;
	private int[] to;
	private List<Map<String, Object>> data;
	private BDLocation location;
	private LocationClient mLocClient;
	private BDLocationListener myListener = new MyLocationListener();
	private boolean hasDw = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_kdg_xddwcx);
		initVariable();
		initView();
		initListeners();
		showProgressDialog();
	}

	@Override
	protected void initVariable() {

	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());
		listView = (ListView) findViewById(R.id.listView);
		from = new String[] { "textView1", "num", "title" };
		to = new int[] { R.id.textView1, R.id.tv_1, R.id.tv_2 };

		mLocClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocClient.registerLocationListener(myListener); // 注册监听函数

		setLocationClientOption();
	}

	@Override
	protected void initListeners() {
		//
		OnClickListener backonClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.bt_topback:
					onBackPressed();
					break;

				default:
					break;
				}

			}
		};

		topBack.setOnClickListener(backonClickListener);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String wdbm = (String) data.get(position).get("id");
				Intent intent = new Intent(getApplicationContext(),
						XddwList.class);
				intent.putExtra("status", wdbm);
				startActivity(intent);

			}
		});
	}

	@Override
	protected void getWebService(String s) {
		if ("query".equals(s)) {
			try {
				data = new ArrayList<Map<String, Object>>();
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_XJ_CXFJ_XQ",
						location.getLongitude() + "*" + location.getLatitude()
								+ "*" + location.getLongitude() + "*"
								+ location.getLatitude(), "uf_json_getdata",
						this);
				String flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, Object> item = new HashMap<String, Object>();
						item.put("id", temp.getString("wdbm"));
						item.put("title", temp.getString("wdmc"));
						item.put("num", "");
						String yh = "";
						try {
							yh = temp.getString("ywhy_bm");
						} catch (Exception e) {
							
						}
						item.put("textView1", getListItemIcon(yh));
						data.add(item);
					}

					Message msg = new Message();
					msg.what = Constant.SUCCESS;
					handler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = Constant.FAIL;
					handler.sendMessage(msg);
				}

			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;
				handler.sendMessage(msg);
			}
		}

	}

	/*
	 * BDLocationListener接口有2个方法需要实现： 1.接收异步返回的定位结果，参数是BDLocation类型参数。
	 * 2.接收异步返回的POI查询结果，参数是BDLocation类型参数。
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation locations) {
			if (locations == null) {
				return;
			} else {
				location = locations;
				Message msg = new Message();
				msg.what = Constant.NUM_7;// 成功
				handler.sendMessage(msg);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {

		}

		@Override
		public void onConnectHotSpotMessage(String arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

	}

	/**
	 * 设置定位参数包括：定位模式（单次定位，定时定位），返回坐标类型，是否打开GPS等等。
	 */
	private void setLocationClientOption() {

		final LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(1000);// 设置发起定位请求的间隔时间为5000ms
		option.disableCache(true);// 禁止启用缓存定位
		option.setPriority(LocationClientOption.GpsFirst);
		option.setAddrType("all");
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.SUCCESS:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				adapter = new SimpleAdapter(Xddwcx.this, data,
						R.layout.listview_item_kcxx, from, to);
				listView.setAdapter(adapter);
				break;
			case Constant.FAIL:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P("查询失败,没有数据", null);
				break;
			case Constant.NETWORK_ERROR:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P(Constant.NETWORK_ERROR_STR, null);
				break;
			case Constant.NUM_7:
				mLocClient.stop();
				hasDw = true;
				Config.getExecutorService().execute(new Runnable() {

					@Override
					public void run() {
						getWebService("query");
					}
				});
				break;
			}

		}
	};

//	@Override
//	public void onBackPressed() {
//		Intent intent = new Intent(this, MainActivity.class);
//		intent.putExtra("currType", 1);
//		startActivity(intent);
//		finish();
//	}

}
