package com.nt.activity.js;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

import com.nt.R;
import com.nt.Parser.JSONObjectParser;
import com.nt.activity.FrameActivity;
import com.nt.activity.main.MainActivity;
import com.nt.cache.DataCache;
import com.nt.cache.ServiceReportCache;
import com.nt.common.Constant;
import com.nt.utils.Config;

public class FyqrActivity extends FrameActivity {
	
	private ArrayList<Map<String, String>> data_xq;
	private JSONObjectParser jsonObjectParser;
	private List<Map<String, String>> data;
	private ListView listView;
	private SimpleAdapter adapter;
	private String[] from;
	private int[] to;
	private String flag;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_fyqr_list);
		initVariable();
		initView();
		initListeners();
		
		if (!backboolean) {
			showProgressDialog();
		}
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {

				getWebService("query");
			}
		});
	}

	@Override
	protected void initVariable() {
		jsonObjectParser = new JSONObjectParser(getApplicationContext());
		listView = (ListView) findViewById(R.id.listView);
		data = new ArrayList<Map<String, String>>();
		from = new String[] { "bzr", "zbh", "timemy", "datemy" };
		to = new int[] { R.id.yytmy, R.id.pgdhmy, R.id.timemy, R.id.datemy };

	}

	@Override
	protected void initView() {
		title.setText(DataCache.getinition().getTitle());
		
	}

	@Override
	protected void initListeners() {
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.bt_topback:
					Intent intent = new Intent(getApplicationContext(), MainActivity.class);
					intent.putExtra("currType", 4);
					startActivity(intent);
					finish();
					break;
				}
			}
		};

		topBack.setOnClickListener(onClickListener);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int arg2,
					long id) {
				ServiceReportCache.setIndex(arg2);
				if (arg2 >= 0) {
					Intent intent = new Intent(getApplicationContext(),
							FyqrCompleteActivity.class);
					startActivity(intent);
//					finish();
				}

			}
		});
	}

	@Override
	protected void getWebService(String s) {
		
		if ("query".equals(s) && !backboolean) {
			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo("_PAD_JBFFYQR",
						DataCache.getinition().getUserId(),
						"uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				JSONArray jsonArray = jsonObject.getJSONArray("tableA");

				if (Integer.parseInt(flag) > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, String> item = new HashMap<String, String>();
						String timeff = temp.getString("shgl_ywgl_fwbgszb_bzsj");
						timeff = timeff.substring(2);
						item.put("bzrlxdh", temp.getString("shgl_ywgl_fwbgszb_bzrlxdh"));
						item.put("khbm", temp.getString("main_jcsj_khjgb_khbm"));
						item.put("pgbm", temp.getString("zzjgb_pz_pgbm"));
						item.put("fy2", temp.getString("shgl_ywgl_fwbgszb_fy2"));
						item.put("fy1", temp.getString("shgl_ywgl_fwbgszb_fy1"));
						item.put("fwgcs", temp.getString("shgl_ywgl_fwbgszb_fwgcs"));
						item.put("kzzd5", temp.getString("kzzd5"));
						item.put("xgsj", temp.getString("shgl_ywgl_fwbgszb_xgsj"));
						item.put("djzt", temp.getString("shgl_ywgl_fwbgszb_djzt"));
						item.put("jdsj", temp.getString("shgl_ywgl_fwbgszb_jdsj"));
						item.put("gzxx", temp.getString("shgl_ywgl_fwbgszb_gzxx"));
						item.put("wcsj", temp.getString("shgl_ywgl_fwbgszb_wcsj"));
						item.put("cfsj", temp.getString("shgl_ywgl_fwbgszb_cfsj"));
						item.put("slsj", temp.getString("shgl_ywgl_fwbgszb_slsj"));
						item.put("bzsj", temp.getString("shgl_ywgl_fwbgszb_bzsj"));
						item.put("bzr", temp.getString("ccgl_wlsjb_bzr"));
						item.put("khxxdz", temp.getString("shgl_ywgl_fwbgszb_khxxdz"));
						item.put("kdzh", "shgl_ywgl_fwbgszb_kdzh");
						item.put("zbh", temp.getString("shgl_ywgl_fwbgszb_zbh"));
						item.put("ds", temp.getString("main_jcsj_dqb_ds"));
						item.put("clfs", temp.getString("clfs"));
						item.put("timemy",// 时间
								mdateformat(1, timeff));

						item.put("datemy",// 年月日
								mdateformat(0, timeff));
						data.add(item);
					}
					ServiceReportCache.setData(data);
					Message msg = new Message();
					msg.what = Constant.SUCCESS;// 成功
					handler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = Constant.FAIL;// 失败
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}

		} else {
			// 直接加载
			Message msg = new Message();
			msg.what = Constant.SUCCESS;// 成功
			handler.sendMessage(msg);
		}
	}
	
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		intent.putExtra("currType", 3);
		startActivity(intent);
		finish();
	}
	
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.NETWORK_ERROR:
				dialogShowMessage_P(Constant.NETWORK_ERROR_STR, null);
				break;

			case Constant.SUCCESS:
				adapter = new SimpleAdapter(getApplicationContext(),
						ServiceReportCache.getData(),
						R.layout.listview_dispatchinginformationreceiving_item,
						from, to);
				listView.setAdapter(adapter);
				break;

			case Constant.FAIL:
				dialogShowMessage_P("你查询的时间段内，没有费用需要确认",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								onBackPressed();
							}

						});
				break;

			}
			if (!backboolean) {
				progressDialog.dismiss();
			}
		}

	};

}
