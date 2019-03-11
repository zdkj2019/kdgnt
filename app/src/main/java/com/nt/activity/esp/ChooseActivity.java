package com.nt.activity.esp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.common.Constant;
import com.nt.utils.Config;

public class ChooseActivity extends FrameActivity {

	private ListView listView;
	private EditText et_search;
	private List<Map<String, String>> data;
	private ArrayList<Map<String, String>> data_all;
	private String[] from;
	private int[] to;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_yytbz);
		initVariable();
		initView();
		initListeners();
	}

	@SuppressWarnings("unchecked")
	protected void initVariable() {

		listView = (ListView) findViewById(R.id.listView1);
		et_search = (EditText) findViewById(R.id.et_search);
		data = new ArrayList<Map<String, String>>();

		from = new String[] { "name", "id" };
		to = new int[] { R.id.tV_name, R.id.tV_number };

		Intent intent = getIntent();

		data_all = (ArrayList<Map<String, String>>) intent
				.getSerializableExtra("data");
	}

	protected void initView() {

		textchange("");
	}

	protected void initListeners() {

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (position >= 0) {
					Intent intent = getIntent();
					intent.putExtra("name", data.get(position).get("name"));
					intent.putExtra("id", data.get(position).get("id"));
					setResult(1, intent);
					finish();
				}
			}
		});

		et_search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(final CharSequence s, int start,
					int before, int count) {
				textchange(s.toString());
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

	/**
	 * 
	 * @param s
	 *            模糊查询的字段
	 * @param string2
	 *            查询数据库返回的字段
	 */
	private void textchange(final String s) {

		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				data = new LinkedList<Map<String, String>>();
				if (s == null || "".equals(s)) {
					data = data_all;
				} else {
					String regex = ".*" + s + ".*";
					Pattern pattern = Pattern.compile(regex);
					for (int i = 0; i < data_all.size(); i++) {
						Map<String, String> map2 = data_all.get(i);
						Matcher matcher = pattern.matcher(map2.get("name"));
						if (matcher.find()) {
							data.add(map2);
						}
					}
				}

				if (data != null) {
					Message message = new Message();
					message.what = Constant.SUCCESS;
					hander.sendMessage(message);
				}
			}
		});
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	protected void getWebService(String s) {
		// TODO Auto-generated method stub

	}

	@SuppressLint("HandlerLeak")
	private Handler hander = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.SUCCESS:
				SimpleAdapter adapter = new SimpleAdapter(
						getApplicationContext(), data,
						R.layout.definedspinner2_item, from, to);
				listView.setAdapter(adapter);
				break;

			default:
				break;
			}

		}

	};

}