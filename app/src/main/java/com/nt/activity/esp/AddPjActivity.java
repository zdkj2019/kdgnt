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
import android.widget.TextView;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.activity.util.DataFilterActivity;
import com.nt.common.Constant;
import com.nt.utils.Config;

/**
 * 新增备件
 * 
 * @author zdkj
 *
 */
public class AddPjActivity extends FrameActivity {

	private String msgStr,bjlbbm,bjlbmc;
	private int position;
	private Button confirm, cancel;
	private ArrayList<Map<String, String>> data,data_bjxh;
	private TextView tv_bjxh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_addpj);
		initVariable();
		initView();
		initListeners();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initVariable() {

		tv_bjxh = (TextView) findViewById(R.id.tv_bjxh);
		cancel = (Button) findViewById(R.id.cancel);
		confirm = (Button) findViewById(R.id.confirm);
		title.setText("新增备件");

		Intent intent = getIntent();
		data = (ArrayList<Map<String, String>>) intent
				.getSerializableExtra("data");
		bjlbbm = data.get(0).get("bjlbbm");
		bjlbmc = data.get(0).get("bjlbmc");
		position = intent.getIntExtra("position",-1);
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
					if(!isNotNull(tv_bjxh)){
						toastShowMessage("请选择备件型号");
						return;
					}
					Intent intent = getIntent();
					ArrayList<Map<String, String>> list = new ArrayList<Map<String,String>>();
					Map<String, String> map = new HashMap<String, String>();
					map.put("bjlbmc", bjlbmc);
					map.put("bjlbbm", bjlbbm);
					map.put("bjxhmc", tv_bjxh.getText().toString());
					map.put("bjxhbm", tv_bjxh.getTag().toString());
					list.add(map);
					intent.putExtra("list", list);
					intent.putExtra("position", position);
					setResult(1, intent);
					finish();
					break;
				case R.id.cancel:
					onBackPressed();
					break;
				case R.id.bt_topback:
					onBackPressed();
					break;
				case R.id.tv_bjxh:
					showProgressDialog();
					Config.getExecutorService().execute(new Runnable() {

						@Override
						public void run() {

							getWebService("getbjxh");
						}
					});

					break;
				default:
					break;
				}

			}
		};

		topBack.setOnClickListener(onClickListener);
		confirm.setOnClickListener(onClickListener);
		cancel.setOnClickListener(onClickListener);
		tv_bjxh.setOnClickListener(onClickListener);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 2 && resultCode == 1 && data != null) {
			int position = data.getIntExtra("position", -1);
			Map<String, String> map = data_bjxh.get(position);
			tv_bjxh.setText(map.get("name"));
			tv_bjxh.setTag(map.get("id"));
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void getWebService(String s) {

		if ("getbjxh".equals(s)) {
			try {
				data_bjxh = new ArrayList<Map<String, String>>();
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_JCSJ_BJLB2", bjlbbm, "uf_json_getdata",
						getApplicationContext());
				String flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						HashMap<String, String> item = new HashMap<String, String>();
						item.put("id", temp.getString("hplbbm"));
						item.put("name", temp.getString("hplbmc"));
						data_bjxh.add(item);
					}
					Message msg = new Message();
					msg.what = Constant.SUCCESS;
					handler.sendMessage(msg);
				} else {
					msgStr = "获取备件型号失败";
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

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.NETWORK_ERROR:
				dialogShowMessage_P(Constant.NETWORK_ERROR_STR, null);
				break;

			case Constant.SUCCESS:
				Intent intent2 = new Intent(getApplicationContext(),
						DataFilterActivity.class);
				intent2.putExtra("data", data_bjxh);
				startActivityForResult(intent2, 2);
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
