package com.nt.activity.kdg;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.utils.Config;

public class SelectZfg extends FrameActivity {

	private Button confirm, cancel;
	private ListView listView1;
	private SimpleAdapter adapter;
	private String[] from;
	private int[] to;
	private EditText et_search;
	private List<Map<String, String>> mdata;
	private ArrayList<Map<String, String>> data_xq;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_choosezfg);
		initVariable();
		initView();
		initListeners();
	}

	@Override
	protected void initVariable() {

		confirm = (Button) findViewById(R.id.confirm);
		cancel = (Button) findViewById(R.id.cancel);

	}

	@Override
	protected void initView() {
		title.setText("主副柜选择");
		listView1 = (ListView) findViewById(R.id.listview1);
		et_search = (EditText) findViewById(R.id.et_search);
		mdata = new ArrayList<Map<String, String>>();
		from = new String[] { "zfg", "sbbm", "hpbm", "mxh" };
		to = new int[] { R.id.zfg, R.id.sbbm, R.id.hpbm, R.id.mxh };
		Intent intent = getIntent();
		data_xq = (ArrayList<Map<String, String>>) intent
				.getSerializableExtra("data_zfg");

		textchange("", "sbbm");
	}

	protected void initListeners() {

		et_search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(final CharSequence s, int start,
					int before, int count) {
				textchange(s.toString(), "sbbm");
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

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
					boolean haszg = false;
					boolean hasfg = false;
					for (int i = 0; i < listView1.getChildCount(); i++) {
						LinearLayout ll = (LinearLayout) listView1
								.getChildAt(i);
						CheckBox sb = (CheckBox) ll.getChildAt(0);
						if (sb.isChecked()) {
							TextView tv = (TextView) ll.getChildAt(1);
							if ("主柜".equals(tv.getText().toString())) {
								if (!haszg) {
									haszg = true;
									list.add(mdata.get(i));
								} else {
									toastShowMessage("只能选择一个");
									return;
								}
							} else {
								if (!hasfg) {
									hasfg = true;
									list.add(mdata.get(i));
								} else {
									toastShowMessage("只能选择一个");
									return;
								}
							}

						}
					}
					Intent intent = getIntent();
					intent.putExtra("data", list);
					setResult(4, intent);
					finish();
				} catch (Exception e) {

				}

			}
		});
		
		listView1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
				list.add(mdata.get(position));
				Intent intent = getIntent();
				intent.putExtra("data", list);
				setResult(4, intent);
				finish();
//				LinearLayout ll = (LinearLayout) view;
//				CheckBox sb = (CheckBox) ll.getChildAt(0);
//				if (sb.isChecked()) {
//					sb.setChecked(false);
//				}else{
//					sb.setChecked(true);
//				}
			}
		});
	}

	/**
	 * 搜索
	 * 
	 * @param pipei
	 *            匹配map的哪个字段
	 * @param str
	 *            要匹配的字段
	 * @param date
	 *            匹配的数据
	 * @return 匹配的date
	 */
	public List<Map<String, String>> querycache(String pipei, String str,
			List<Map<String, String>> data2) {
		List<Map<String, String>> matchered;
		if (str == null || "".equals(str)) {
			return data2;
		}
		String regex = ".*" + str + ".*";
		Pattern pattern = Pattern.compile(regex);
		matchered = new LinkedList<Map<String, String>>();

		for (int i = 0; i < data2.size(); i++) {
			Map<String, String> map2 = data2.get(i);
			Matcher matcher = pattern.matcher(map2.get(pipei));
			if (matcher.find()) {
				matchered.add(map2);
			}
		}
		return matchered;
	}

	/**
	 * 
	 * @param s
	 *            模糊查询的字段
	 * @param string2
	 *            查询数据库返回的字段
	 */
	private void textchange(final String s, final String string2) {

		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {

				mdata = querycache(string2, s, data_xq);

				if (mdata != null) {
					Message message = new Message();
					message.what = 1;
					hander.sendMessage(message);
				}
			}
		});
	}

	private Handler hander = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			adapter = new SimpleAdapter(getApplicationContext(), mdata,
					R.layout.listview_item_zfg, from, to);
			listView1.setAdapter(adapter);

		}

	};

//	@Override
//	public void onBackPressed() {
//		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
//		Intent intent = getIntent();
//		intent.putExtra("data", list);
//		setResult(4, intent);
//		finish();
//	}

	@Override
	protected void getWebService(String s) {

	}
}
