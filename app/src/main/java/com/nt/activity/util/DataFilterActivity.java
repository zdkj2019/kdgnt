package com.nt.activity.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.common.Constant;
import com.nt.utils.Config;

/**
 * 数据筛选
 * 
 * @author zdkj
 *
 */
public class DataFilterActivity extends FrameActivity {

	private ListView listview;
	private EditText et_search;
	private List<Map<String, String>> data, data_all;
	private String[] from;
	private int[] to;
	private String name = "";

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
		from = new String[] { "id", "name" };
		to = new int[] { R.id.tv_id, R.id.tv_name };

		Intent intent = getIntent();

		data_all = (ArrayList<Map<String, String>>) intent
				.getSerializableExtra("data");
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}

	protected void initListeners() {

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (position >= 0) {
					Intent intent = getIntent();
					Map<String, String> map = data.get(position);
					intent.putExtra("str", new JSONObject(map).toString());
					setResult(1, intent);
					finish();
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

						getWebService("getdata");
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
			data = new ArrayList<Map<String, String>>();
			try {
				for (int i = 0; i < data_all.size(); i++) {
					Map<String, String> map = data_all.get(i);
					if (map.get("name").indexOf(name) != -1) {
						data.add(map);
					}
				}
				Message msg = new Message();
				msg.what = Constant.SUCCESS;
				hander.sendMessage(msg);
			} catch (Exception e) {
				Message msg = new Message();
				msg.what = Constant.SUCCESS;
				hander.sendMessage(msg);
			}
		}

	}

	private Handler hander = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.SUCCESS:
				SimpleAdapter adapter = new SimpleAdapter(
						getApplicationContext(), data,
						R.layout.listview_item_filterdata, from, to);
				listview.setAdapter(adapter);
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
