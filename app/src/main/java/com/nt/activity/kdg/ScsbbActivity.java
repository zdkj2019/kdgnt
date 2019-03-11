package com.nt.activity.kdg;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.activity.main.MainActivity;
import com.nt.cache.DataCache;
import com.nt.cache.ServiceReportCache;
import com.nt.common.Constant;
import com.nt.utils.Config;
import com.nt.utils.ImageUtil;
import com.nt.zxing.CaptureActivity;

public class ScsbbActivity extends FrameActivity {

	private Button confirm, cancel, btn_sm;
	private EditText et_zgxlh, et_sbbm, et_zdh, et_simkh, et_fg1, et_fg2,
			et_fg3, et_fg4, et_fg5, et_fg6, et_fg7, et_fg8;
	private LinearLayout ll_fg_1, ll_fg_2, ll_fg_3, ll_fg_4, ll_fg_5, ll_fg_6,
			ll_fg_7, ll_fg_8;
	private String[] from;
	private int[] to;
	private ArrayList<Map<String, String>> data_zp;
	private Spinner spinner_fgsjazs, sp_sssh;
	private String flag, message, sbbm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_w_scsbb);
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

		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);

	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());

		btn_sm = (Button) findViewById(R.id.btn_sm);
		et_zgxlh = (EditText) findViewById(R.id.et_zgxlh);
		et_sbbm = (EditText) findViewById(R.id.et_sbbm);
		et_zdh = (EditText) findViewById(R.id.et_zdh);
		sp_sssh = (Spinner) findViewById(R.id.sp_sssh);
		spinner_fgsjazs = (Spinner) findViewById(R.id.spinner_fgsjazs);
		et_simkh = (EditText) findViewById(R.id.et_simkh);
		et_fg1 = (EditText) findViewById(R.id.et_fg1);
		et_fg2 = (EditText) findViewById(R.id.et_fg2);
		et_fg3 = (EditText) findViewById(R.id.et_fg3);
		et_fg4 = (EditText) findViewById(R.id.et_fg4);
		et_fg5 = (EditText) findViewById(R.id.et_fg5);
		et_fg6 = (EditText) findViewById(R.id.et_fg6);
		et_fg7 = (EditText) findViewById(R.id.et_fg7);
		et_fg8 = (EditText) findViewById(R.id.et_fg8);

		ll_fg_1 = (LinearLayout) findViewById(R.id.ll_fg_1);
		ll_fg_2 = (LinearLayout) findViewById(R.id.ll_fg_2);
		ll_fg_3 = (LinearLayout) findViewById(R.id.ll_fg_3);
		ll_fg_4 = (LinearLayout) findViewById(R.id.ll_fg_4);
		ll_fg_5 = (LinearLayout) findViewById(R.id.ll_fg_5);
		ll_fg_6 = (LinearLayout) findViewById(R.id.ll_fg_6);
		ll_fg_7 = (LinearLayout) findViewById(R.id.ll_fg_7);
		ll_fg_8 = (LinearLayout) findViewById(R.id.ll_fg_8);

		from = new String[] { "id", "name" };
		to = new int[] { R.id.bm, R.id.name };

		hideAll();

		List list = new ArrayList<String>();
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");
		list.add("6");
		list.add("7");
		list.add("8");
		ArrayAdapter adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_fgsjazs.setAdapter(adapter);
	}

	@Override
	protected void initListeners() {
		//
		OnClickListener backonClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.bt_topback:
					onBackPressed();
					break;
				case R.id.cancel:
					onBackPressed();
					break;
				case R.id.confirm:
					boolean f = true;
					int sl = spinner_fgsjazs.getSelectedItemPosition() + 1;
					for (int i = 1; i < sl; i++) {
						if (i == 1) {
							if (!isNotNull(et_fg1)) {
								f = false;
							}
						}
						if (i == 2) {
							if (!isNotNull(et_fg2)) {
								f = false;
							}
						}
						if (i == 3) {
							if (!isNotNull(et_fg3)) {
								f = false;
							}
						}
						if (i == 4) {
							if (!isNotNull(et_fg4)) {
								f = false;
							}
						}
						if (i == 5) {
							if (!isNotNull(et_fg5)) {
								f = false;
							}
						}
						if (i == 6) {
							if (!isNotNull(et_fg6)) {
								f = false;
							}
						}
						if (i == 7) {
							if (!isNotNull(et_fg7)) {
								f = false;
							}
						}
						if (i == 8) {
							if (!isNotNull(et_fg8)) {
								f = false;
							}
						}
					}
					if (!isNotNull(et_sbbm) || !isNotNull(et_zdh)
							|| !isNotNull(et_simkh) || !f) {
						toastShowMessage("设备各项信息不能为空！");
						return;
					}
					showProgressDialog();
					Config.getExecutorService().execute(new Runnable() {

						@Override
						public void run() {
							getWebService("submit");
						}
					});
					break;
				case R.id.btn_sm:
					startSm();
					break;
				default:
					break;
				}

			}
		};

		topBack.setOnClickListener(backonClickListener);
		cancel.setOnClickListener(backonClickListener);
		confirm.setOnClickListener(backonClickListener);
		btn_sm.setOnClickListener(backonClickListener);

		spinner_fgsjazs.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				hideAll();
				String str = spinner_fgsjazs.getSelectedItem().toString();
				int num = Integer.parseInt(str);
				if (num > 0 && num < 9) {
					for (int i = 0; i < num; i++) {
						if (i == 0) {
							ll_fg_1.setVisibility(View.VISIBLE);
							findViewById(R.id.v_1).setVisibility(View.VISIBLE);
						}
						if (i == 1) {
							ll_fg_2.setVisibility(View.VISIBLE);
							findViewById(R.id.v_2).setVisibility(View.VISIBLE);
						}
						if (i == 2) {
							ll_fg_3.setVisibility(View.VISIBLE);
							findViewById(R.id.v_3).setVisibility(View.VISIBLE);
						}
						if (i == 3) {
							ll_fg_4.setVisibility(View.VISIBLE);
							findViewById(R.id.v_4).setVisibility(View.VISIBLE);
						}
						if (i == 4) {
							ll_fg_5.setVisibility(View.VISIBLE);
							findViewById(R.id.v_5).setVisibility(View.VISIBLE);
						}
						if (i == 5) {
							ll_fg_6.setVisibility(View.VISIBLE);
							findViewById(R.id.v_6).setVisibility(View.VISIBLE);
						}
						if (i == 6) {
							ll_fg_7.setVisibility(View.VISIBLE);
							findViewById(R.id.v_7).setVisibility(View.VISIBLE);
						}
						if (i == 7) {
							ll_fg_8.setVisibility(View.VISIBLE);
							findViewById(R.id.v_8).setVisibility(View.VISIBLE);
						}
					}
				} else {
					toastShowMessage("副柜安装数为1-8之间的数字！");
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

	}

	private void startSm() {
		// 二维码
		Intent intent = new Intent(getApplicationContext(),
				CaptureActivity.class);
		startActivityForResult(intent, 2);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 2 && resultCode == 2 && data != null) {
			// 二维码
			sbbm = data.getStringExtra("result").trim();
			showProgressDialog();
			Config.getExecutorService().execute(new Runnable() {

				@Override
				public void run() {
					getWebService("checksbbm");
				}
			});

		}
	}

	@Override
	protected void getWebService(String s) {

		if (s.equals("query")) {// 提交
			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_KDG_SBXXLR_SSKH", "", "uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				data_zp = new ArrayList<Map<String, String>>();
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, String> item = new HashMap<String, String>();
						item.put("id", temp.getString("khjgbm"));
						item.put("name", temp.getString("khjgmc"));
						data_zp.add(item);
					}

					Message msg = new Message();
					msg.what = Constant.NUM_6;
					handler.sendMessage(msg);

				} else {
					// flag = jsonObject.getString("msg");
					Message msg = new Message();
					msg.what = Constant.NUM_6;// 失败
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;
				handler.sendMessage(msg);
			}
		}

		if (s.equals("checksbbm")) {
			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_KDG_SFCZ_WEM", sbbm, "uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					Message msg = new Message();
					msg.what = Constant.NUM_7;
					handler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = Constant.NUM_9;
					handler.sendMessage(msg);
				}

			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;
				handler.sendMessage(msg);
			}
		}

		if (s.equals("submit")) {// 提交
			try {
				String cs = "";
				// 先上传设备信息
				message = "提交成功！";
				String str = "";
				String zfgStr = "";
				zfgStr += et_zdh.getText().toString();
				zfgStr += "#@#";
				zfgStr += et_simkh.getText().toString();
				zfgStr += "#@#";
				zfgStr += et_zgxlh.getText().toString();
				zfgStr += "#@#";
				zfgStr += et_zgxlh.getTag().toString();
				zfgStr += "#^#";
				for (int i = 0; i < spinner_fgsjazs.getSelectedItemPosition() + 1; i++) {
					if (i == 0) {
						zfgStr += "";
						zfgStr += "#@#";
						zfgStr += "";
						zfgStr += "#@#";
						zfgStr += et_fg1.getText().toString();
						zfgStr += "#@#";
						zfgStr += et_fg1.getTag().toString();
						zfgStr += "#^#";
					}
					if (i == 1) {
						zfgStr += "";
						zfgStr += "#@#";
						zfgStr += "";
						zfgStr += "#@#";
						zfgStr += et_fg2.getText().toString();
						zfgStr += "#@#";
						zfgStr += et_fg2.getTag().toString();
						zfgStr += "#^#";
					}
					if (i == 2) {
						zfgStr += "";
						zfgStr += "#@#";
						zfgStr += "";
						zfgStr += "#@#";
						zfgStr += et_fg3.getText().toString();
						zfgStr += "#@#";
						zfgStr += et_fg3.getTag().toString();
						zfgStr += "#^#";
					}
					if (i == 3) {
						zfgStr += "";
						zfgStr += "#@#";
						zfgStr += "";
						zfgStr += "#@#";
						zfgStr += et_fg4.getText().toString();
						zfgStr += "#@#";
						zfgStr += et_fg4.getTag().toString();
						zfgStr += "#^#";
					}
					if (i == 4) {
						zfgStr += "";
						zfgStr += "#@#";
						zfgStr += "";
						zfgStr += "#@#";
						zfgStr += et_fg5.getText().toString();
						zfgStr += "#@#";
						zfgStr += et_fg5.getTag().toString();
						zfgStr += "#^#";
					}
					if (i == 5) {
						zfgStr += "";
						zfgStr += "#@#";
						zfgStr += "";
						zfgStr += "#@#";
						zfgStr += et_fg6.getText().toString();
						zfgStr += "#@#";
						zfgStr += et_fg6.getTag().toString();
						zfgStr += "#^#";
					}
					if (i == 6) {
						zfgStr += "";
						zfgStr += "#@#";
						zfgStr += "";
						zfgStr += "#@#";
						zfgStr += et_fg7.getText().toString();
						zfgStr += "#@#";
						zfgStr += et_fg7.getTag().toString();
						zfgStr += "#^#";
					}
					if (i == 7) {
						zfgStr += "";
						zfgStr += "#@#";
						zfgStr += "";
						zfgStr += "#@#";
						zfgStr += et_fg8.getText().toString();
						zfgStr += "#@#";
						zfgStr += et_fg8.getTag().toString();
						zfgStr += "#^#";
					}
				}
				if (!"".equals(zfgStr)) {
					zfgStr = zfgStr.substring(0, zfgStr.length() - 3);
				}

				// 再提交服务报告
				String typeStr = "sbxxlr";
				str = sbbm
						+ "*PAM*"
						+ ((Map<String, String>) sp_sssh.getSelectedItem())
								.get("id") + "*PAM*" + zfgStr;
				JSONObject json = this.callWebserviceImp.getWebServerInfo(
						"c#_PAD_KDG_SBXXLY", str, typeStr, typeStr,
						"uf_json_setdata2", this);
				flag = json.getString("flag");

				if (Integer.parseInt(flag) > 0) {

					Message msg = new Message();
					msg.what = Constant.SUCCESS;
					handler.sendMessage(msg);
				} else {
					flag = json.getString("msg");
					Message msg = new Message();
					msg.what = Constant.FAIL;
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

	private void hideAll() {
		ll_fg_1.setVisibility(View.GONE);
		findViewById(R.id.v_1).setVisibility(View.GONE);
		ll_fg_2.setVisibility(View.GONE);
		findViewById(R.id.v_2).setVisibility(View.GONE);
		ll_fg_3.setVisibility(View.GONE);
		findViewById(R.id.v_3).setVisibility(View.GONE);
		ll_fg_4.setVisibility(View.GONE);
		findViewById(R.id.v_4).setVisibility(View.GONE);
		ll_fg_5.setVisibility(View.GONE);
		findViewById(R.id.v_5).setVisibility(View.GONE);
		ll_fg_6.setVisibility(View.GONE);
		findViewById(R.id.v_6).setVisibility(View.GONE);
		ll_fg_7.setVisibility(View.GONE);
		findViewById(R.id.v_7).setVisibility(View.GONE);
		ll_fg_8.setVisibility(View.GONE);
		findViewById(R.id.v_8).setVisibility(View.GONE);

	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.FAIL:
				dialogShowMessage_P("失败，请检查后重试...错误标识：" + flag, null);
				break;
			case Constant.SUCCESS:
				dialogShowMessage_P(message,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								onBackPressed();
							}
						});
				break;
			case Constant.NETWORK_ERROR:
				dialogShowMessage_P(Constant.NETWORK_ERROR_STR, null);
				break;
			case Constant.NUM_6:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				SimpleAdapter adapter = new SimpleAdapter(ScsbbActivity.this,
						data_zp, R.layout.spinner_item, from, to);
				sp_sssh.setAdapter(adapter);
				break;
			case Constant.NUM_7:
				et_sbbm.setText(sbbm);
				break;
			case Constant.NUM_9:
				message = "设备编码" + sbbm + " 不存在！";
				et_sbbm.setText("");
				dialogShowMessage_P(message,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
							}
						});
				break;
			}
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		}
	};

//	@Override
//	public void onBackPressed() {
//		Intent intent = new Intent(this, MainActivity.class);
//		intent.putExtra("currType", 1);
//		startActivity(intent);
//		finish();
//	}

}
