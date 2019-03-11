package com.nt.activity.w;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.activity.login.LoginActivity;
import com.nt.activity.main.MainActivity;
import com.nt.cache.DataCache;
import com.nt.cache.ServiceReportCache;
import com.nt.common.Constant;
import com.nt.utils.Config;

/**
 * 消息管理列表
 * 
 * @author
 */
@SuppressLint("HandlerLeak")
public class XxglActivity extends FrameActivity {

	private String flag;
	private ListView listView;
	private SimpleAdapter adapter;
	private List<Map<String, String>> data;
	private String[] from;
	private int[] to;
	private SharedPreferences spf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_dispatchinginformationreceiving);
		initVariable();
		initView();
		initListeners();
		
	}

	@Override
	protected void initVariable() {

		listView = (ListView) findViewById(R.id.listView);
		

		from = new String[] { "textView1", "id", "num", "title", "xrsj",
				"yfbs" };
		to = new int[] { R.id.textView1, R.id.tv_0, R.id.tv_1, R.id.tv_2,
				R.id.tv_3, R.id.tv_4 };

	}

	@Override
	protected void initView() {
		title.setText("消息中心");
		spf = getSharedPreferences("loginsp", LoginActivity.MODE_PRIVATE);
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
					int num = DataCache.getinition().getZzxx_num();
					if (num > 0) {
						DataCache.getinition().setZzxx_num(num - 1);
					}

					Intent intent = new Intent(XxglActivity.this,
							XxglShowActivity.class);
					startActivity(intent);
				}

			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {

				getWebService("query");
			}
		});
	}

	@Override
	protected void getWebService(String s) {

		if ("query".equals(s) && !backboolean) {
			try {
				data = new ArrayList<Map<String, String>>();
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_XXZX_TSXXCX", spf.getString("userId", ""),
						"uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				JSONArray jsonArray = jsonObject.getJSONArray("tableA");
				if (Integer.parseInt(flag) > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, String> item = new HashMap<String, String>();
						item.put("num","");
						String yh = "";
						try {
							yh = temp.getString("ywhy_bm");
						} catch (Exception e) {
							
						}
						item.put("textView1", getListItemIcon(yh)+"");
						item.put("id", temp.getString("id"));
						item.put("userid", temp.getString("userid"));
						item.put("xrsj", temp.getString("xrsj"));
						item.put("num", "");
						item.put("message", temp.getString("message"));
						item.put("yfbs", temp.getString("yfbs"));
						item.put("title", temp.getString("title"));

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
				msg.what =Constant.NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}

		} else {
			// 直接加载
			Message msg = new Message();
			msg.what = Constant.SUCCESS;// 成功
			handler.sendMessage(msg);
		}

	}

	private String formatData(String val) {
		try {
			val = Double.valueOf(val).toString() + "0";
		} catch (Exception e) {
			e.printStackTrace();
		}

		return val;
	}

	private class CurrAdapter extends SimpleAdapter {

		public CurrAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);
		}

		@Override
		public View getView(final int position, View convertView,
				final ViewGroup parent) {
			final View view = super.getView(position, convertView, parent);

			TextView tv_4 = (TextView) view.findViewById(R.id.tv_4);
			TextView tv_2 = (TextView) view.findViewById(R.id.tv_2);
			if ("1".equals(tv_4.getText().toString().trim())) {
				TextPaint tp = tv_2.getPaint();
				tp.setFakeBoldText(true);
			}
			return view;
		}

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.NETWORK_ERROR:
				dialogShowMessage_P(Constant.NETWORK_ERROR_STR,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								onBackPressed();
							}

						});
				break;

			case Constant.SUCCESS:
				adapter = new CurrAdapter(XxglActivity.this,
						ServiceReportCache.getData(),
						R.layout.listview_item_zyxx, from, to);
				listView.setAdapter(adapter);
				break;

			case Constant.FAIL:
				dialogShowMessage_P("你查询的时间段内，没有消息。",
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
