package com.nt.activity.w;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.cache.DataCache;
import com.nt.common.Constant;
import com.nt.utils.Config;

public class BzfyActivity extends FrameActivity {


	private Button confirm, cancel;
	private Spinner spinner_sf, spinner_ds, spinner_qx,spinner_hb;
	private ArrayList<Map<String, String>> data_sf, data_ds, data_qx, data_all,data_hb;
	private String[] from;
	private int[] to;
	private SimpleAdapter adapter;
	private String sfbm, dsbm, qxbm, errorMsg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_bzfy);
		initVariable();
		initView();
		initListeners();
		showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {

				getWebService("getsf");
			}
		});
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {

				getWebService("gethb");
			}
		});
	}

	@Override
	protected void initVariable() {

		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);
		confirm.setText("人工费用");
		cancel.setText("标准费用");

	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());
		spinner_sf = (Spinner) findViewById(R.id.spinner_sf);
		spinner_ds = (Spinner) findViewById(R.id.spinner_ds);
		spinner_qx = (Spinner) findViewById(R.id.spinner_qx);
		spinner_hb = (Spinner) findViewById(R.id.spinner_hb);
		from = new String[] { "id", "name" };
		to = new int[] { R.id.bm, R.id.name };
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
				case R.id.cancel:
					qxbm = data_qx.get(spinner_qx.getSelectedItemPosition()).get("id");
					if ("".equals(qxbm)) {
						toastShowMessage("请选择区县");
						return;
					}
					String hbbm = data_hb.get(spinner_hb.getSelectedItemPosition()).get("id");
					if ("".equals(hbbm)) {
						toastShowMessage("请选择行别");
						return;
					}
					Intent intent = new Intent(getApplicationContext(),
							PjfyQueryActivity.class);
					intent.putExtra("qxbm", qxbm);
					intent.putExtra("hbbm", hbbm);
					startActivity(intent);
					break;
				case R.id.confirm:
					qxbm = data_qx.get(spinner_qx.getSelectedItemPosition())
							.get("id");
					if ("".equals(qxbm)) {
						toastShowMessage("请选择区县");
						return;
					}
					hbbm = data_hb.get(spinner_hb.getSelectedItemPosition())
							.get("id");
					if ("".equals(hbbm)) {
						toastShowMessage("请选择行别");
						return;
					}
					intent = new Intent(getApplicationContext(),
							RgfyQueryActivity.class);
					intent.putExtra("qxbm", qxbm);
					intent.putExtra("hbbm", hbbm);
					startActivity(intent);
					break;
				default:
					break;
				}

			}
		};

		topBack.setOnClickListener(backonClickListener);
		cancel.setOnClickListener(backonClickListener);
		confirm.setOnClickListener(backonClickListener);

		spinner_sf.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				sfbm = data_sf.get(position).get("id");
				Config.getExecutorService().execute(new Runnable() {

					@Override
					public void run() {

						getWebService("getds");
					}
				});
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		spinner_ds.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				dsbm = data_ds.get(position).get("id");
				Config.getExecutorService().execute(new Runnable() {

					@Override
					public void run() {

						getWebService("getqx");
					}
				});
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	protected void getWebService(String s) {
		if ("getsf".equals(s)) {
			try {
				data_sf = new ArrayList<Map<String, String>>();
				Map<String, String> item = new HashMap<String, String>();
				item.put("id", "");
				item.put("name", "");
				data_sf.add(item);
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SBGL_SBLR_DQXX1", "", "uf_json_getdata", this);
				String flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						item = new HashMap<String, String>();
						item.put("id", temp.getString("sfbm"));
						item.put("name", temp.getString("sfmc"));
						data_sf.add(item);
					}
					Message msg = new Message();
					msg.what = Constant.NUM_6;
					handler.sendMessage(msg);
				} else {
					errorMsg = "获取省份信息失败";
					Message msg = new Message();
					msg.what = Constant.FAIL;
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}
		}

		if ("getds".equals(s)) {
			try {
				data_ds = new ArrayList<Map<String, String>>();
				Map<String, String> item = new HashMap<String, String>();
				item.put("id", "");
				item.put("name", "");
				data_ds.add(item);
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SBGL_SBLR_DQXX2", sfbm, "uf_json_getdata", this);
				String flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						item = new HashMap<String, String>();
						item.put("id", temp.getString("dsbm"));
						item.put("name", temp.getString("dsmc"));
						data_ds.add(item);
					}
				}

				Message msg = new Message();
				msg.what = Constant.NUM_7;
				handler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}
		}

		if ("getqx".equals(s)) {
			try {
				data_qx = new ArrayList<Map<String, String>>();
				Map<String, String> item = new HashMap<String, String>();
				item.put("id", "");
				item.put("name", "");
				data_qx.add(item);
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SBGL_SBLR_DQXX3", dsbm, "uf_json_getdata", this);
				String flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						item = new HashMap<String, String>();
						item.put("id", temp.getString("qxbm"));
						item.put("name", temp.getString("qxmc"));
						data_qx.add(item);
					}

				}

				Message msg = new Message();
				msg.what = Constant.NUM_8;
				handler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}
		}
		
		if("gethb".equals(s)){
			try {
				data_hb = new ArrayList<Map<String, String>>();
				Map<String, String> item = new HashMap<String, String>();
				item.put("id", "");
				item.put("name", "");
				data_hb.add(item);
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SHGL_JCSJ_HYXX", "", "uf_json_getdata", this);
				String flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						item = new HashMap<String, String>();
						item.put("id", temp.getString("ccd"));
						item.put("name", temp.getString("ccdnm"));
						data_hb.add(item);
					}

				}

				Message msg = new Message();
				msg.what = Constant.NUM_9;
				handler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.FAIL:
				dialogShowMessage_P("失败，" + errorMsg, null);
				break;
			case Constant.NETWORK_ERROR:
				dialogShowMessage_P("网络连接出错，请检查你的网络设置", null);
				break;
			case Constant.SUCCESS:
				dialogShowMessage_P("提交成功！",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								onBackPressed();
							}
						});
				break;
			case Constant.NUM_6:
				adapter = new SimpleAdapter(BzfyActivity.this, data_sf,
						R.layout.spinner_item, from, to);
				spinner_sf.setAdapter(adapter);
				break;
			case Constant.NUM_7:
				adapter = new SimpleAdapter(BzfyActivity.this, data_ds,
						R.layout.spinner_item, from, to);
				spinner_ds.setAdapter(adapter);
				break;
			case Constant.NUM_8:
				adapter = new SimpleAdapter(BzfyActivity.this, data_qx,
						R.layout.spinner_item, from, to);
				spinner_qx.setAdapter(adapter);
				break;
			case Constant.NUM_9:
				adapter = new SimpleAdapter(BzfyActivity.this, data_hb,
						R.layout.spinner_item, from, to);
				spinner_hb.setAdapter(adapter);
				break;
			}
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		}
	};

}
