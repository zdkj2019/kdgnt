package com.nt.activity.esp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.cache.DataCache;
import com.nt.common.Constant;
import com.nt.utils.Config;

/**
 * 新增备件列表
 * 
 * @author zdkj
 *
 */
public class AddPjListActivity extends FrameActivity {

	private String msgStr;
	private Button confirm, cancel;
	private ArrayList<Map<String, String>> dataAll;
	private String[] from;
	private int[] to;
	private ListView listView;
	private SimpleAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_addpjlist);
		initVariable();
		initView();
		initListeners();
		if (dataAll.size() == 0) {
			showProgressDialog();
			Config.getExecutorService().execute(new Runnable() {

				@Override
				public void run() {

					getWebService("getbjlb");
				}
			});
		} else {
			Message msg = new Message();
			msg.what = Constant.SUCCESS;
			handler.sendMessage(msg);
		}
	}

	@Override
	protected void initVariable() {

		from = new String[] { "bjlbmc", "bjxhmc" };
		to = new int[] { R.id.tv_1, R.id.tv_2 };
		cancel = (Button) findViewById(R.id.cancel);
		confirm = (Button) findViewById(R.id.confirm);
		listView = (ListView) findViewById(R.id.listView);
		title.setText("新增备件");

		Intent intent = getIntent();
		dataAll = (ArrayList<Map<String, String>>) intent
				.getSerializableExtra("data");
	}

	@Override
	protected void initView() {

	}

	@Override
	protected void initListeners() {
		OnClickListener onClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.confirm:
					Intent intent = getIntent();
					intent.putExtra("list", dataAll);
					setResult(1, intent);
					finish();
					break;
				case R.id.cancel:
					onBackPressed();
					break;
				case R.id.bt_topback:
					onBackPressed();
					break;
				default:
					break;
				}

			}
		};

		topBack.setOnClickListener(onClickListener);
		confirm.setOnClickListener(onClickListener);
		cancel.setOnClickListener(onClickListener);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 3 && resultCode == 1 && data != null) {
			ArrayList<Map<String, String>> list = (ArrayList<Map<String, String>>) data
					.getSerializableExtra("list");
			int position = data.getIntExtra("position", 0);
			dataAll.set(position, list.get(0));
			Message msg = new Message();
			msg.what = Constant.SUCCESS;
			handler.sendMessage(msg);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void getWebService(String s) {

		if ("getbjlb".equals(s)) {
			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_JCSJ_BJLB1", "", "uf_json_getdata",
						getApplicationContext());
				String flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						HashMap<String, String> item = new HashMap<String, String>();
						item.put("bjlbmc", temp.getString("hplbmc"));
						item.put("bjlbbm", temp.getString("hplbbm"));
						item.put("bjxhmc", "");
						item.put("bjxhbm", "");
						dataAll.add(item);
					}
					Message msg = new Message();
					msg.what = Constant.SUCCESS;
					handler.sendMessage(msg);
				} else {
					msgStr = "获取备件类别失败";
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
		}
	}

	private class MyAdapter extends SimpleAdapter {

		public MyAdapter(Context context, List<? extends Map<String, ?>> data,
				int resource, String[] from, int[] to) {
			super(context, data, resource, from, to);

		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = super.getView(position, convertView, parent);
			ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);
			iv_close.setVisibility(View.VISIBLE);
			iv_close.setImageResource(R.drawable.edit);
			iv_close.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Map<String, String> map = dataAll.get(position);
					ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
					list.add(map);
					Intent intent = new Intent(getApplicationContext(),
							AddPjActivity.class);
					intent.putExtra("data", list);
					intent.putExtra("position", position);
					startActivityForResult(intent, 3);
				}
			});

			return view;
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
				adapter = new MyAdapter(getApplicationContext(), dataAll,
						R.layout.listview_item_addpj, from, to);
				listView.setAdapter(adapter);
				break;
			case Constant.FAIL:
				dialogShowMessage_P("失败，" + msgStr, null);
				break;

			}
			if (progressDialog != null) {
				progressDialog.dismiss();
			}

		}

	};

}
