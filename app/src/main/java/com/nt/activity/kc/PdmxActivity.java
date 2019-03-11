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
 * 外部调拨出库 明细
 * 
 * @author cheng
 */
@SuppressLint({ "HandlerLeak", "WorldReadableFiles" })
public class PdmxActivity extends FrameActivity {

	private EditText edit_kfmc, edit_cwmc,edit_hpmc, edit_sl;
	private TextView tv_dqkc;
	private ArrayList<Map<String, String>> data_kfck,data_cwck,data_hp; // 所属片区缓存数据
	private String flag, chooseType, currkfck, currcw;
	private Button confirm, cancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_pdmx);
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
		edit_kfmc = (EditText) findViewById(R.id.edit_kfmc);
		edit_cwmc = (EditText) findViewById(R.id.edit_cwmc);
		edit_hpmc = (EditText) findViewById(R.id.edit_hpmc);
		edit_sl = (EditText) findViewById(R.id.edit_sl);
		tv_dqkc = (TextView) findViewById(R.id.tv_dqkc);
		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);

		data_kfck = new ArrayList<Map<String, String>>();
		data_cwck = new ArrayList<Map<String, String>>();
		data_hp = new ArrayList<Map<String, String>>();

		edit_kfmc.setTag("");
		edit_cwmc.setTag("");
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
				if (isNotNull(edit_kfmc) && isNotNull(edit_cwmc)
						&& isNotNull(edit_hpmc) && isNotNull(edit_sl)) {
					if (Integer.parseInt(edit_sl.getText().toString()) == 0) {
						toastShowMessage("数量不能为0");
						return;
					}
					if (Integer.parseInt(edit_sl.getText().toString()) > Integer
							.parseInt(tv_dqkc.getText().toString())) {
						toastShowMessage("数量不能大于当前库存数量");
						return;
					}

					Map<String, String> map = new HashMap<String, String>();
					map.put("kfbm",edit_kfmc.getTag().toString());// json
																// 转0开头的字符串会自动去掉0
					map.put("kfmc", edit_kfmc.getText().toString());
					map.put("cwbm",edit_cwmc.getTag().toString());
					map.put("cwmc", edit_cwmc.getText().toString());
					map.put("hpbm", edit_hpmc.getTag().toString());
					map.put("hpmc", edit_hpmc.getText().toString());
					map.put("sl", edit_sl.getText().toString());
					map.put("dqkc", tv_dqkc.getText() == null ? "0" : tv_dqkc
							.getText().toString());
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
		edit_kfmc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				chooseType = "kfmc";
				Intent intent = new Intent(PdmxActivity.this,
						ChooseActivity.class);
				intent.putExtra("data", data_kfck);
				startActivityForResult(intent, 1);
			}
		});
		// 出库仓位
		edit_cwmc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				chooseType = "cwmc";
				currkfck = edit_kfmc.getTag().toString();
				if ("".equals(currkfck)) {
					toastShowMessage("请选择出库库房！");
					return;
				}
				showProgressDialog();
				Config.getExecutorService().execute(new Runnable() {

					@Override
					public void run() {

						getWebService("getcwck");
					}
				});
			}
		});

		edit_hpmc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				chooseType = "hpmc";
				currcw = edit_cwmc.getTag().toString();
				if ("".equals(currcw)) {
					toastShowMessage("请选择出库仓位！");
					return;
				}
				if (!backboolean) {
					showProgressDialog();
				}
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
			if ("kfmc".equals(chooseType)) {
				edit_kfmc.setTag(data.getStringExtra("id"));
				edit_kfmc.setText(data.getStringExtra("name"));
			} else if ("cwmc".equals(chooseType)) {
				edit_cwmc.setTag(data.getStringExtra("id"));
				edit_cwmc.setText(data.getStringExtra("name"));
			}else if ("hpmc".equals(chooseType)) {
				edit_hpmc.setText(data.getStringExtra("name"));
				edit_hpmc.setTag(data.getStringExtra("id"));
				for (int i = 0; i < data_hp.size(); i++) {
					Map<String, String> temp = data_hp.get(i);
					if (data.getStringExtra("id").equals(temp.get("id"))) {
						tv_dqkc.setText(temp.get("dqkc"));
					}
				}
			}
		}
	}

	@Override
	protected void getWebService(String s) {

		if ("query".equals(s)) {
			try {
				JSONObject jsonObject_kfck = callWebserviceImp
						.getWebServerInfo("_PAD_KFXQ", DataCache
								.getinition().getUserId(), "uf_json_getdata",
								this);
				flag = jsonObject_kfck.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					try {
						JSONArray array = jsonObject_kfck
								.getJSONArray("tableA");
						data_kfck = (ArrayList<Map<String, String>>) JSONObjectParser
								.jsonToListByJson(array);
						ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
						for (int i = 0; i < data_kfck.size(); i++) {
							Map<String, String> newMap = new HashMap<String, String>();
							Map<String, String> temp = data_kfck.get(i);
							newMap.put("id", temp.get("kfbm"));
							newMap.put("name", temp.get("kfmc"));
							data.add(newMap);
						}
						data_kfck = data;
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

		if ("getcwck".equals(s)) {
			try {
				JSONObject jsonObject_cw = callWebserviceImp.getWebServerInfo(
						"_PAD_CWXQ", DataCache.getinition().getUserId()
								+ "*" + currkfck, "uf_json_getdata",
						this);
				flag = jsonObject_cw.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					try {
						JSONArray array = jsonObject_cw.getJSONArray("tableA");
						data_cwck = (ArrayList<Map<String, String>>) JSONObjectParser
								.jsonToListByJson(array);
						ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
						for (int i = 0; i < data_cwck.size(); i++) {
							Map<String, String> newMap = new HashMap<String, String>();
							Map<String, String> temp = data_cwck.get(i);
							newMap.put("id", temp.get("kfbm"));
							newMap.put("name", temp.get("kfmc"));
							data.add(newMap);
						}
						data_cwck = data;
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


		if ("gethp".equals(s)) {
			try {
				JSONObject jsonObject_hp = callWebserviceImp.getWebServerInfo(
						"_PAD_DBCKHPXX1", DataCache.getinition().getUserId()
								+ "*" + currcw + "*" + currkfck,
						"uf_json_getdata", this);
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
							newMap.put("id", temp.get("hpgl_kfgl_dqkcb_hpbm"));
							newMap.put("name", temp.get("hpmc"));
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
				if ("cwmc".equals(chooseType)) {
					Intent intent = new Intent(PdmxActivity.this,
							ChooseActivity.class);
					intent.putExtra("data", data_cwck);
					startActivityForResult(intent, 1);
				}else if ("hpmc".equals(chooseType)) {
					Intent intent = new Intent(PdmxActivity.this,
							ChooseActivity.class);
					intent.putExtra("data", data_hp);
					startActivityForResult(intent, 1);
				}
				break;
			case Constant.FAIL:
				dialogShowMessage_P("查询失败，没有数据", null);
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
