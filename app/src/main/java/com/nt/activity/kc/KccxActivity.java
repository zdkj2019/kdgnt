package com.nt.activity.kc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.activity.main.MainActivity;
import com.nt.cache.DataCache;
import com.nt.cache.ServiceReportCache;
import com.nt.common.Constant;
import com.nt.utils.Config;

/**
 * 当前库存查询
 * 
 * @author
 */
@SuppressLint("HandlerLeak")
public class KccxActivity extends FrameActivity {

	private String flag, name;
	private EditText et_search;
	private ListView listView;
	private SimpleAdapter adapter;
	private List<Map<String, Object>> data, currdata;
	private String[] from;
	private int[] to;
	private String rg1,rg2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_dispatchinginformationreceiving);
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

		listView = (ListView) findViewById(R.id.listView);

		et_search = (EditText) findViewById(R.id.et_search);
		et_search.setHint("请输入商品名称");
		from = new String[] { "textView1", "num", "title" };
		to = new int[] { R.id.textView1, R.id.tv_1, R.id.tv_2 };
		
		rg1 = getIntent().getStringExtra("rg1");
		rg2 = getIntent().getStringExtra("rg2");

		findViewById(R.id.ll_filter).setVisibility(View.VISIBLE);
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
					onBackPressed();
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

					Intent intent = new Intent(KccxActivity.this,
							KccxShowActivity.class);
					startActivity(intent);
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
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}

	@Override
	protected void getWebService(String s) {

		if ("query".equals(s) && !backboolean) {
			try {
				data = new ArrayList<Map<String, Object>>();
				String userid = DataCache.getinition().getUserId();
				String cs,sqlid;
				if("1".equals(rg2)){
					sqlid = "_PAD_HPKF_DQBGZ";
					cs = userid+"*"+rg1;
				}else{
					sqlid = "_PAD_HPKF_DQBGZ1";
					cs = userid+"*"+rg1;
				}
				JSONObject jsonObject = callWebserviceImp.getWebServerInfoBlob(
						sqlid, cs, "uf_json_getdata_blob", this);
				flag = jsonObject.getString("flag");
				JSONArray jsonArray = jsonObject.getJSONArray("tableA");
				if (Integer.parseInt(flag) > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, Object> item = new HashMap<String, Object>();
						item.put("num", "");
						item.put("textView1", getListItemIcon(""));
						item.put("kfmc", temp.getString("kfmc"));// 库房名称
						item.put("cwmc", temp.getString("cwmc"));// 仓位名称
						item.put("hpmc", temp.getString("hpmc"));// 货品名称
						item.put("hpbm", temp.getString("hpbm"));// 货品编码
						item.put("ggxh", temp.getString("ggxh"));// 规格型号
						item.put("sqjc", temp.getString("sqjc"));// 上期结存
						item.put("dqkc", temp.getString("dqkc"));// 当前库存
						item.put("title", temp.getString("cwmc")
								+ "-" + temp.getString("hpmc")
								+ "-" + temp.getString("dqkc"));// 单位

						data.add(item);
					}
					currdata = data;
					ServiceReportCache.setObjectdata(currdata);
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

		if ("getdata".equals(s)) {
			currdata = new ArrayList<Map<String, Object>>();
			try {
				for (int i = 0; i < data.size(); i++) {
					Map<String, Object> map = data.get(i);
					if (((String) map.get("title")).indexOf(name) != -1) {
						currdata.add(map);
					}
				}
				ServiceReportCache.setObjectdata(currdata);
				Message msg = new Message();
				msg.what = Constant.SUCCESS;
				handler.sendMessage(msg);
			} catch (Exception e) {
				Message msg = new Message();
				msg.what = Constant.SUCCESS;
				handler.sendMessage(msg);
			}
		}

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
				adapter = new SimpleAdapter(KccxActivity.this,
						ServiceReportCache.getObjectdata(),
						R.layout.listview_item_kcxx, from, to);
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
