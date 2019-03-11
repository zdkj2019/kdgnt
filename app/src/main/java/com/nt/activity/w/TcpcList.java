package com.nt.activity.w;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.activity.esp.SheBeiPDActivity;
import com.nt.cache.DataCache;
import com.nt.common.Constant;
import com.nt.dodowaterfall.MarqueeTextView;
import com.nt.utils.Config;

/**
 * 同城普查查询 list
 * 
 * @author zdkj
 *
 */
public class TcpcList extends FrameActivity {

	private String qxbm;
	private EditText et_search;
	private ListView listView;
	private List<Map<String, String>> data_all, data;
	private String[] from;
	private int[] to;
	private String name = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_dispatchinginformationreceiving);
		initVariable();
		initView();
		initListeners();
		showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {

				getWebService("getdata");
			}
		});

	}

	@Override
	protected void initVariable() {

		listView = (ListView) findViewById(R.id.listView);
		et_search = (EditText) findViewById(R.id.et_search);
		from = new String[] { "textView1", "faultuser", "zbh", "timemy",
				"datemy" };
		to = new int[] { R.id.textView1, R.id.yytmy, R.id.pgdhmy, R.id.timemy,
				R.id.datemy };
	}

	@Override
	protected void initView() {
		title.setText(DataCache.getinition().getTitle());
		findViewById(R.id.ll_filter).setVisibility(View.VISIBLE);
		qxbm = getIntent().getStringExtra("qxbm");

	}

	@Override
	protected void initListeners() {

		topBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position >= 0) {
					Map<String, String> m = data.get(position);
					Intent intent = null;
					intent = new Intent(TcpcList.this, TcpcSbmxList.class);
					intent.putExtra("sf", m.get("sf").toString());
					intent.putExtra("ds", m.get("ds").toString());
					intent.putExtra("qx", m.get("qx").toString());
					intent.putExtra("wdbmwd", m.get("wdbmwd").toString());
					intent.putExtra("xxdz", m.get("xxdz").toString());
					intent.putExtra("ywhy", m.get("ywhy").toString());
					startActivityForResult(intent, 3);
				}

			}
		});

		et_search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(final CharSequence s, int start,
					int before, int count) {
				name = s.toString();
				Config.getExecutorService().execute(new Runnable() {

					@Override
					public void run() {

						filterData();
					}
				});

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 3 && resultCode == Activity.RESULT_OK) {
			showProgressDialog();
			Config.getExecutorService().execute(new Runnable() {

				@Override
				public void run() {

					getWebService("getdata");
				}
			});
		}
	}

	private void filterData() {

		data = new ArrayList<Map<String, String>>();
		try {
			if (name == null || "".equals(name)) {
				data = data_all;
			} else {
				for (int i = 0; i < data_all.size(); i++) {
					Map<String, String> map = data_all.get(i);
					if (map.get("faultuser").indexOf(name) != -1) {
						data.add(map);
					}
				}
			}
			Message msg = new Message();
			msg.what = Constant.SUCCESS;
			handler.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
			Message msg = new Message();
			msg.what = Constant.SUCCESS;
			handler.sendMessage(msg);
		}

	}

	@Override
	protected void getWebService(String s) {
		if ("getdata".equals(s)) {

			try {
				data_all = new ArrayList<Map<String, String>>();
				String sqlid = "_PAD_SBGL_TCOC";
				String cs = qxbm;
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						sqlid, cs, "uf_json_getdata", this);
				String flag = jsonObject.getString("flag");

				JSONArray jsonArray = jsonObject.getJSONArray("tableA");
				if (Integer.parseInt(flag) > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, String> item = new HashMap<String, String>();
						String yh = "";
						try {
							yh = temp.getString("ywhy");
						} catch (Exception e) {

						}
						item.put("textView1", getListItemIcon(yh) + "");
						item.put("zbh", temp.getString("xxdz"));
						item.put("faultuser", temp.getString("wdbmwd_mc"));
						item.put("sf", temp.getString("sf"));
						item.put("ds", temp.getString("ds"));
						item.put("qx", temp.getString("qx"));
						item.put("wdbmwd", temp.getString("wdbmwd"));
						item.put("xxdz", temp.getString("xxdz"));
						item.put("ywhy", temp.getString("ywhy"));
						item.put("timemy", "需巡检数："+temp.getString("sl1"));
						item.put("datemy", "已巡检数："+temp.getString("sl2"));
						data_all.add(item);
					}
					data = data_all;
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

	@Override
	public void onBackPressed() {
		finish();
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.NETWORK_ERROR:
				dialogShowMessage_P(Constant.NETWORK_ERROR_STR, null);
				break;
			case Constant.SUCCESS:
				SimpleAdapter adapter = new SimpleAdapter(
						getApplicationContext(), data,
						R.layout.listview_item_tcpclist,
						from, to);
				listView.setAdapter(adapter);
				break;
			case Constant.FAIL:
				dialogShowMessage_P("你查询的时间段内，没有数据",
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
