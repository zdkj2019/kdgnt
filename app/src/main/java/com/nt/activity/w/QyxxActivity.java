package com.nt.activity.w;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.activity.main.MainActivity;
import com.nt.cache.DataCache;
import com.nt.utils.Config;

public class QyxxActivity extends FrameActivity {

	private List<Map<String, String>> data;

	private List<Map<String, String>> list_all;

	private LinearLayout ll_qyxx;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_qyxx);
		initVariable();
		initView();
		initListeners();
		showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				getWebService("query");
			}
		});
	}

	@Override
	protected void initVariable() {

	}

	@Override
	protected void initView() {
		ll_qyxx = (LinearLayout) findViewById(R.id.ll_qyxx);
		title.setText("区域及价格");
		data = new ArrayList<Map<String, String>>();
	}

	@Override
	protected void initListeners() {
		topBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.bt_topback:
					onBackPressed();
					break;
				}
			}
		});

	}

	@Override
	protected void getWebService(String s) {
		if ("query".equals(s)) {
			try {
				JSONObject object = callWebserviceImp.getWebServerInfo(
						"_PAD_RY_PQFY", DataCache.getinition().getUserId(),
						"uf_json_getdata", this);
				if (Integer.parseInt(object.getString("flag")) > 0) {
					JSONArray array = object.getJSONArray("tableA");

					for (int i = 0; i < array.length(); i++) {
						Map<String, String> map = new HashMap<String, String>();
						JSONObject temp = array.getJSONObject(i);
						map.put("ywlx", wxaz.get(Integer.parseInt(temp
								.getString("ywlx"))-1));// 业务类型
						map.put("dqmc", temp.getString("dqmc"));// 地区名称
						map.put("t24", formatJg(temp.getString("t24")));// t24
						map.put("tbz", formatJg(temp.getString("tbz")));// tbz
						map.put("fymc", temp.getString("fymc"));// 费用类型
						map.put("t16", formatJg(temp.getString("t16")));// t16
						map.put("t48", formatJg(temp.getString("t48")));// t48
						map.put("hplbmc", temp.getString("hplbmc"));// 设备类型
						data.add(map);
					}
					list_all = new ArrayList<Map<String, String>>();
					List<String> list_qy = new ArrayList<String>();
					List<String> list_sbzl = new ArrayList<String>();
					for (int i = 0; i < data.size(); i++) {
						Map<String, String> m = data.get(i);
						boolean f_qy = true;
						boolean f_zbzl = true;
						for (int j = 0; j < list_qy.size(); j++) {
							if (m.get("dqmc").equals(list_qy.get(j))) {
								f_qy = false;
							}
						}
						if (f_qy) {
							list_qy.add(m.get("dqmc"));
						}
						for (int j = 0; j < list_sbzl.size(); j++) {
							if (m.get("hplbmc").equals(list_sbzl.get(j))) {
								f_zbzl = false;
							}
						}
						if (f_zbzl) {
							list_sbzl.add(m.get("hplbmc"));
						}
					}

					for (int i = 0; i < list_qy.size(); i++) {
						String qy = list_qy.get(i);
						for (int j = 0; j < list_sbzl.size(); j++) {
							String sblx = list_sbzl.get(j);
							Map<String, String> m = new HashMap<String, String>();
							m.put("qy", qy);
							m.put("sblx", sblx);
							boolean f = false;
							for (int k = 0; k < data.size(); k++) {
								Map<String, String> d = data.get(k);
								if (d.get("dqmc").equals(qy)
										&& d.get("hplbmc").equals(sblx)) {
									f = true;
									if ("00001".equals(d.get("fymc"))) {
										if ("维修".equals(d.get("ywlx"))) {
											m.put("cq_wx_t16", d.get("t16"));
											m.put("cq_wx_t24", d.get("t24"));
											m.put("cq_wx_t48", d.get("t48"));
										}
										if ("安装".equals(d.get("ywlx"))) {
											m.put("cq_az", d.get("t16"));
										}
									}
									if ("00002".equals(d.get("fymc"))) {
										if ("维修".equals(d.get("ywlx"))) {
											m.put("xz_wx_t16", d.get("t16"));
											m.put("xz_wx_t24", d.get("t24"));
											m.put("xz_wx_t48", d.get("t48"));
										}
										if ("安装".equals(d.get("ywlx"))) {
											m.put("xz_az", d.get("t16"));
										}
									}
								}
							}
							if (f) {
								list_all.add(m);
							}
						}
					}
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = 2;
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = 0;
				handler.sendMessage(msg);
			}
		}
	}

	private String formatJg(String val) {
		try {
			if ("0".equals(val)) {
				return "0.00";
			} else {
				val = val.substring(0, 10);
				double d = Double.parseDouble(val);
				return d + "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "0.00";
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				dialogShowMessage("查询失败，请检查网络连接是否正常！", null, null);
				break;
			case 1:
				try {
					for (int i = 0; i < list_all.size(); i++) {
						View view = LayoutInflater
								.from(getApplicationContext()).inflate(
										R.layout.activity_qyxx_table, null);
						TextView v0 = (TextView) view.findViewById(R.id.tv_dq);
						TextView v1 = (TextView) view
								.findViewById(R.id.tv_sbzl);
						TextView v2 = (TextView) view
								.findViewById(R.id.tv_cq_wx_t16);
						TextView v3 = (TextView) view
								.findViewById(R.id.tv_cq_wx_t24);
						TextView v4 = (TextView) view
								.findViewById(R.id.tv_cq_wx_t48);
						TextView v5 = (TextView) view
								.findViewById(R.id.tv_cq_az);
						TextView v6 = (TextView) view
								.findViewById(R.id.tv_xz_wx_t16);
						TextView v7 = (TextView) view
								.findViewById(R.id.tv_xz_wx_t24);
						TextView v8 = (TextView) view
								.findViewById(R.id.tv_xz_wx_t48);
						TextView v9 = (TextView) view
								.findViewById(R.id.tv_xz_az);

						Map<String, String> map = list_all.get(i);
						v0.setText(map.get("qy"));
						v1.setText(map.get("sblx"));
						v2.setText(map.get("cq_wx_t16"));
						v3.setText(map.get("cq_wx_t24"));
						v4.setText(map.get("cq_wx_t48"));
						v5.setText(map.get("cq_az"));
						v6.setText(map.get("xz_wx_t16"));
						v7.setText(map.get("xz_wx_t24"));
						v8.setText(map.get("xz_wx_t48"));
						v9.setText(map.get("xz_az"));
						ll_qyxx.addView(view);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 2:
				TextView textView = new TextView(getApplicationContext());
				LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				lp.setMargins(16, 8, 8, 8);
				textView.setLayoutParams(lp);
				textView.setText("账号未绑定服务区域");
				ll_qyxx.addView(textView);
				break;
			}
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		};
	};

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		intent.putExtra("currType", 5);
		startActivity(intent);
		finish();
	};
}
