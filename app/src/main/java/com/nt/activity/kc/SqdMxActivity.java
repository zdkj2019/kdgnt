package com.nt.activity.kc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nt.R;
import com.nt.Parser.JSONObjectParser;
import com.nt.activity.FrameActivity;
import com.nt.activity.esp.ChooseActivity;
import com.nt.activity.main.MainActivity;
import com.nt.cache.DataCache;
import com.nt.common.Constant;
import com.nt.utils.Config;

/**
 * 申请单 明细
 * 
 * @author cheng
 */
@SuppressLint({ "HandlerLeak", "WorldReadableFiles" })
public class SqdMxActivity extends FrameActivity {

	private EditText edit_ckkfmc, edit_hpmc, edit_sl;
	private ArrayList<Map<String, String>> data_kfck, data_hp; // 所属片区缓存数据
	private String flag, chooseType, currkf;
	private Button confirm, cancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_sqdmx);
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
		edit_ckkfmc = (EditText) findViewById(R.id.edit_ckkfmc);

		edit_hpmc = (EditText) findViewById(R.id.edit_hpmc);
		edit_sl = (EditText) findViewById(R.id.edit_sl);

		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);

		data_kfck = new ArrayList<Map<String, String>>();
		data_hp = new ArrayList<Map<String, String>>();

		edit_ckkfmc.setTag("");
		edit_hpmc.setTag("");

	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());
	}

	@Override
	protected void initListeners() {

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
				if (isNotNull(edit_ckkfmc) && isNotNull(edit_hpmc)
						&& isNotNull(edit_sl)) {
					if (Integer.parseInt(edit_sl.getText().toString()) == 0) {
						toastShowMessage("数量不能为0");
						return;
					}

					Map<String, String> map = new HashMap<String, String>();
					map.put("ckkfmc_id", "id_"
							+ edit_ckkfmc.getTag().toString());// json//
																// 转0开头的字符串会自动去掉0
					map.put("ckkfmc_name", edit_ckkfmc.getText().toString());
					map.put("hpmc_id", "id_" + edit_hpmc.getTag().toString());
					map.put("hpmc_name", edit_hpmc.getText().toString());
					map.put("sl", edit_sl.getText().toString());

					DataCache.getinition().setMap(map);
					Intent intent = getIntent();
					intent.putExtra("result", true);
					setResult(1, intent);
					finish();
				} else {
					toastShowMessage("各项信息不能为空，请录入完整！");
				}

			}
		});

		// 出库库房
		edit_ckkfmc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				chooseType = "ckkfmc";
				Intent intent = new Intent(SqdMxActivity.this,
						ChooseActivity.class);
				intent.putExtra("data", data_kfck);
				startActivityForResult(intent, 1);
			}
		});
		edit_hpmc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if("".equals(edit_ckkfmc.getText().toString())){
					toastShowMessage("请选择库房名称");
					return;
				}
				chooseType = "hpmc";
				showProgressDialog();
				Config.getExecutorService().execute(new Runnable() {

					@Override
					public void run() {

						getWebService("gethp");
					}
				});
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == 1 && data != null) {
			if ("ckkfmc".equals(chooseType)) {
				edit_ckkfmc.setTag(data.getStringExtra("id"));
				edit_ckkfmc.setText(data.getStringExtra("name"));
				currkf = data.getStringExtra("id");
			} else if ("hpmc".equals(chooseType)) {
				edit_hpmc.setText(data.getStringExtra("name"));
				edit_hpmc.setTag(data.getStringExtra("id"));
			}
		}
	}

	@Override
	protected void getWebService(String s) {

		if ("query".equals(s)) {
			try {
				JSONObject jsonObject_kfck = callWebserviceImp
						.getWebServerInfo("_PAD_KFCK_KF", DataCache
								.getinition().getUserId(), "uf_json_getdata",
								this);
				flag = jsonObject_kfck.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray array = jsonObject_kfck.getJSONArray("tableA");
					data_kfck = (ArrayList<Map<String, String>>) JSONObjectParser
							.jsonToListByJson(array);
					ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
					for (int i = 0; i < data_kfck.size(); i++) {
						Map<String, String> newMap = new HashMap<String, String>();
						Map<String, String> temp = data_kfck.get(i);
						newMap.put("id", temp.get("zzjgbm"));
						newMap.put("name", temp.get("zzjgmc"));
						data.add(newMap);
					}
					data_kfck = data;
					Message msg = new Message();
					msg.what = Constant.SUCCESS;// 解析成功
					handler.sendMessage(msg);

				} else {
					Message msg = new Message();
					msg.what = Constant.FAIL;// 解析出错
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.FAIL;// 解析出错
				handler.sendMessage(msg);
			}
		}

		if ("gethp".equals(s)) {
			try {
				JSONObject jsonObject_hp = callWebserviceImp.getWebServerInfo(
						"_PAD_CCGL_SQDLR_HPXX", currkf, "uf_json_getdata", this);
				flag = jsonObject_hp.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					try {
						JSONArray array = jsonObject_hp.getJSONArray("tableA");
						data_hp = (ArrayList<Map<String, String>>) JSONObjectParser
								.jsonToListByJson(array);
						ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
						for (int i = 0; i < data_hp.size(); i++) {
							Map<String, String> newMap = new HashMap<String, String>();
							Map<String, String> temp = data_hp.get(i);
							newMap.put("id", temp.get("hpgl_jcsj_hpxxb_hpbm"));
							newMap.put("name", temp.get("hpgl_jcsj_hpxxb_hpmc"));
							newMap.put("dw", temp.get("hpgl_jcsj_hpxxb_jldw"));
							newMap.put("dqkc", temp.get("dqkc"));
							data.add(newMap);
						}
						data_hp = data;
						Message msg = new Message();
						msg.what = Constant.SUCCESS;// 解析成功
						handler.sendMessage(msg);
					} catch (Exception e) {
						e.printStackTrace();
						Message msg = new Message();
						msg.what = Constant.FAIL;// 解析出错
						handler.sendMessage(msg);
					}
				} else {
					Message msg = new Message();
					msg.what = Constant.FAIL;// 解析出错
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

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.NETWORK_ERROR:
				dialogShowMessage_P("网络连接错误，请检查您的网络是否正常", null);
				break;
			case Constant.SUCCESS:
				if ("hpmc".equals(chooseType)) {
					Intent intent = new Intent(SqdMxActivity.this,
							ChooseActivity.class);
					intent.putExtra("data", data_hp);
					startActivityForResult(intent, 1);
				}
				break;
			case Constant.FAIL:
				dialogShowMessage_P("初始化数据失败", null);
				break;
			}
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		}
	};

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

}
