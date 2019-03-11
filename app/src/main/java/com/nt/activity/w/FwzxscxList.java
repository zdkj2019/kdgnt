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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
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
 * 服务咨询师查询 list
 * 
 * @author zdkj
 *
 */
public class FwzxscxList extends FrameActivity {

	private String flag;
	private ListView listView;
	private SimpleAdapter adapter;
	private List<Map<String, Object>> data;
	private String[] from;
	private int[] to;
	private String start_time, end_time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_dispatchinginformationreceiving);
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

		listView = (ListView) findViewById(R.id.listView);
		data = new ArrayList<Map<String, Object>>();
		from = new String[] { "textView1", "faultuser", "zbh", "timemy",
				"datemy" };
		to = new int[] { R.id.textView1, R.id.yytmy, R.id.pgdhmy, R.id.timemy,
				R.id.datemy };

	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());
		Intent intent = getIntent();
		start_time = intent.getStringExtra("start_time");
		end_time = intent.getStringExtra("end_time");
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
					Intent intent = null;
					intent = new Intent(FwzxscxList.this, FwzxscxShow.class);
					startActivity(intent);
				}

			}
		});

	}

	@Override
	protected void getWebService(String s) {

		if ("query".equals(s) && !backboolean) {
			try {
				String sqlid = "_PAD_ZXGCS_CX";
				String cs = DataCache.getinition().getUserId() + "*"
						+ start_time + "*" + end_time + "*"
						+ DataCache.getinition().getUserId() + "*" + start_time
						+ "*" + end_time;
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						sqlid, cs, "uf_json_getdata", this);
				flag = jsonObject.getString("flag");

				JSONArray jsonArray = jsonObject.getJSONArray("tableA");
				if (Integer.parseInt(flag) > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, Object> item = new HashMap<String, Object>();
						String timeff = temp.getString("kdg_pgzb_bzsj");
						timeff = timeff.substring(2);
						String yh = "";
						try {
							yh = temp.getString("ywhy_bm");
						} catch (Exception e) {
							
						}
						item.put("textView1", getListItemIcon(yh));
						item.put("zbh", temp.getString("kdg_pgzb_zbh"));
						item.put("faultuser", temp.getString("kdg_pgzb_xxdz"));
						item.put("tv_1", temp.getString("zzjgb_pz_fbf"));
						item.put("tv_2", temp.getString("hpgl_jcsj_hplbb_sbmc"));
						item.put("tv_3", temp.getString("main_jcsj_dqb_sen"));
						item.put("tv_4", temp.getString("main_jcsj_khjgb_qu"));
						item.put("tv_5", temp.getString("ccgl_wlsjb_xiq"));
						item.put("tv_6", temp.getString("ywlx"));
						item.put("tv_7", temp.getString("kdg_pgzb_gzxx"));
						item.put("tv_8", temp.getString("kdg_pgzb_xxdz"));
						item.put("tv_9", temp.getString("kdg_pgzb_bz"));
						item.put("tv_10", temp.getString("ccgl_ygb_xm"));
						item.put("tv_11", temp.getString("ccgl_ygb_lxdh"));
						item.put("tv_12", timeff);
						item.put("timemy", mdateformat(1, timeff));// 时间
						item.put("datemy", mdateformat(0, timeff));// 年月日
						data.add(item);
					}
					ServiceReportCache.setObjectdata(data);
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
			Message msg = new Message();
			msg.what = Constant.FAIL;
			handler.sendMessage(msg);
		}

	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("currType", 1);
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
				adapter = new SimpleAdapter(FwzxscxList.this,
						ServiceReportCache.getObjectdata(),
						R.layout.listview_dispatchinginformationreceiving_item,
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
