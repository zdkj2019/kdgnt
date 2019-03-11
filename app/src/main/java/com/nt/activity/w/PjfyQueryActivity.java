package com.nt.activity.w;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.cache.DataCache;
import com.nt.common.Constant;
import com.nt.utils.Config;

public class PjfyQueryActivity extends FrameActivity {

	private ListView listview;
	private EditText et_search;
	private List<Map<String, String>> data, data_all;
	private String[] from;
	private int[] to;
	private String name = "",hbbm,qxbm,errorMsg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_datafilter);

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

	@SuppressWarnings("unchecked")
	protected void initVariable() {

		listview = (ListView) findViewById(R.id.listview);
		et_search = (EditText) findViewById(R.id.et_search);
		data = new ArrayList<Map<String, String>>();
		data_all = new ArrayList<Map<String, String>>();
		from = new String[] { "id", "name" };
		to = new int[] { R.id.tv_id, R.id.tv_name };

		Intent intent = getIntent();
		
		hbbm = intent.getStringExtra("hbbm");
		qxbm = intent.getStringExtra("qxbm");

		
	}

	@Override
	protected void initView() {
		title.setText("配件费用");

	}

	protected void initListeners() {

//		listview.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//
//				if (position >= 0) {
//					Intent intent = getIntent();
//					intent.putExtra("position", position);
//					setResult(1, intent);
//					finish();
//				}
//			}
//		});

		et_search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(final CharSequence s, int start,
					int before, int count) {
				name = s.toString();
				Config.getExecutorService().execute(new Runnable() {

					@Override
					public void run() {

						filter();
					}
				});
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		topBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	@Override
	protected void getWebService(String s) {
		if ("getdata".equals(s)) {
			try {
				String userid = DataCache.getinition().getUserId();
				String cs = qxbm+"*"+hbbm+"*"+userid+"*"+userid;
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_JCJS_BJFBZCX", cs, "uf_json_getdata", this);
				String flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, String> item = new HashMap<String, String>();
						item.put("id", temp.getString("bjbm"));
						item.put("name", temp.getString("fy1"));
						data_all.add(item);
					}
					Message msg = new Message();
					msg.what = Constant.NUM_6;
					handler.sendMessage(msg);
				} else {
					errorMsg="没有数据";
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

	}
	
	private void filter(){

		data = new ArrayList<Map<String, String>>();
		try {
			Map<String, String> item = new HashMap<String, String>();
			item.put("id", "配件名称");
			item.put("name","价格");
			data.add(item);
			for (int i = 0; i < data_all.size(); i++) {
				Map<String, String> map = data_all.get(i);
				if (map.get("id").indexOf(name) != -1) {
					data.add(map);
				}
			}
			Message msg = new Message();
			msg.what = Constant.SUCCESS;
			handler.sendMessage(msg);
		} catch (Exception e) {
			Message msg = new Message();
			msg.what = Constant.SUCCESS;
			handler.sendMessage(msg);
		}
	
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.SUCCESS:
				SimpleAdapter adapter = new SimpleAdapter(
						getApplicationContext(), data,
						R.layout.listview_item_pjfyquery, from, to);
				listview.setAdapter(adapter);
				break;
			case Constant.NETWORK_ERROR:
				dialogShowMessage_P("网络连接出错，请检查你的网络设置", null);
				break;
			case Constant.FAIL:
				dialogShowMessage_P("失败，" + errorMsg, null);
				break;
			case Constant.NUM_6:
				filter();
				break;
			default:
				break;
			}
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		}

	};
}
