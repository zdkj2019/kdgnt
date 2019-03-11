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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nt.R;
import com.nt.Parser.JSONObjectParser;
import com.nt.activity.FrameActivity;
import com.nt.cache.DataCache;
import com.nt.common.Constant;
import com.nt.utils.Config;

/**
 * 库存盘点
 * 
 * @author hs
 */
@SuppressLint({ "HandlerLeak", "WorldReadableFiles" })
public class KcpdActivity extends FrameActivity {

	private EditText edit_gdbz, edit_sgdh;
	private Button confirm, cancel, btn_addmx;
	private LinearLayout ll_mx, ll_mx_item;
	private ArrayList<Map<String, String>> data_mx;
	private String flag = "",errorMsg="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_kcpd);
		initVariable();
		initView();
		initListeners();
		showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {

				getWebService("queryJx");
			}
		});
		
	}

	@Override
	protected void initVariable() {

		edit_gdbz = (EditText) findViewById(R.id.edit_gdbz);
		edit_sgdh = (EditText) findViewById(R.id.edit_sgdh);
		ll_mx = (LinearLayout) findViewById(R.id.ll_mx);
		btn_addmx = (Button) findViewById(R.id.btn_addmx);
		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);
	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());
	}

	@SuppressLint("NewApi")
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

		btn_addmx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(KcpdActivity.this,
						PdmxActivity.class);
				startActivityForResult(intent, 1);

			}
		});

		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showProgressDialog();
				Config.getExecutorService().execute(new Runnable() {

					@Override
					public void run() {
						getWebService("submit");

					}
				});
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == 1 && data != null) {
			boolean result = data.getBooleanExtra("result", false);
			if (result) {
				Map<String, String> map = DataCache.getinition().getMap();
				boolean flag = true;
				for (int i = 0; i < data_mx.size(); i++) {
					Map<String, String> m = data_mx.get(i);
					if (m.get("kfbm").equals(map.get("kfbm"))
							&& m.get("cwbm").equals(map.get("cwbm"))) {
						flag = false;
					}
				}
				if (flag) {
					data_mx.add(map);
					DataCache.getinition().setMap(null);
					Message msg = new Message();
					msg.what = Constant.NUM_6;
					handler.sendMessage(msg);
				} else {
					toastShowMessage("新增明细失败,该库房仓位数据已存在！");
				}

			} else {
				toastShowMessage("新增明细失败,数据转换错误！");
			}
		}
	}

	@SuppressLint("InflateParams")
	@Override
	protected void getWebService(String s) {
		
		if ("queryJx".equals(s)) {
			try {
				String userid = DataCache.getinition().getUserId();
				JSONObject jsonObject_mx = callWebserviceImp.getWebServerInfo(
						"_PAD_KFPD_TX", userid,
						"uf_json_getdata", this);
				flag = jsonObject_mx.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray array = jsonObject_mx.getJSONArray("tableA");
					JSONObject obj = array.getJSONObject(0);
					int sl = Integer.parseInt(obj.getString("sl"));
					if(sl>0){
						Message msg = new Message();
						msg.what = Constant.NUM_7;
						handler.sendMessage(msg);
					}
				}
				Message msg = new Message();
				msg.what = Constant.NUM_8;
				handler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;
				handler.sendMessage(msg);
			}
		}

		if ("query".equals(s)) {
			try {
				data_mx = new ArrayList<Map<String, String>>();
				String userid = DataCache.getinition().getUserId();
				JSONObject jsonObject_mx = callWebserviceImp.getWebServerInfoBlob(
						"_PAD_CCGL_PDCX1", userid + "*" + userid,
						"uf_json_getdata_blob", this);
				flag = jsonObject_mx.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					try {
						JSONArray array = jsonObject_mx.getJSONArray("tableA");
						data_mx = (ArrayList<Map<String, String>>) JSONObjectParser
								.jsonToListByJson(array);
						ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
						for (int i = 0; i < data_mx.size(); i++) {
							Map<String, String> newMap = new HashMap<String, String>();
							Map<String, String> temp = data_mx.get(i);
							newMap.put("mxh", temp.get("mxh"));
							newMap.put("kfbm", temp.get("kfbm"));
							newMap.put("cwbm", temp.get("cwbm"));
							newMap.put("hpbm", temp.get("hpbm"));
							newMap.put("kfmc", temp.get("kfmc"));
							newMap.put("cwmc", temp.get("cwmc"));
							newMap.put("hpmc", temp.get("hpmc"));
							newMap.put("dqkc", temp.get("dqkc"));
							newMap.put("spsl", temp.get("spsl"));
							data.add(newMap);
						}
						data_mx = data;
						Message msg = new Message();
						msg.what = Constant.NUM_6;
						handler.sendMessage(msg);
					} catch (Exception e) {
						e.printStackTrace();
						errorMsg="明细数据解析错误";
						Message msg = new Message();
						msg.what = Constant.FAIL;// 解析出错
						handler.sendMessage(msg);
					}
				} else {
					errorMsg="没有明细数据";
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

		if ("submit".equals(s)) {
			try {
				String sql = DataCache.getinition().getUserId() + "*PAM*"
						+ edit_sgdh.getText().toString() + "*PAM*"
						+ edit_gdbz.getText().toString();
				String mxStr = "";
				int count = ll_mx.getChildCount();
				for (int i = 0; i < count; i++) {
					Map<String, String> map = data_mx.get(i);
					ll_mx_item = (LinearLayout) ll_mx.getChildAt(i);
					EditText et_sjsl = (EditText) ll_mx_item
							.findViewById(R.id.et_sjsl);
					mxStr = mxStr + map.get("kfbm") + "#@#" + map.get("cwbm")
							+ "#@#" + map.get("hpbm") + "#@#" + map.get("dqkc")
							+ "#@#" + et_sjsl.getText().toString().trim()
							+ "#^#";
				}
				if (!"".equals(mxStr)) {
					mxStr = mxStr.substring(0, mxStr.length() - 3);
				}
				sql = sql + "*PAM*" + mxStr;
				flag = this.callWebserviceImp.getWebServerInfo(
						"c#_PAD_CCGL_HPGL_KCPD", sql,
						DataCache.getinition().getUserId(),
						DataCache.getinition().getUserId(), "uf_json_setdata2",
						this).getString("flag");
				if (Integer.parseInt(flag) > 0) {
					Message msg = new Message();
					msg.what = Constant.SUCCESS;
					handler.sendMessage(msg);
				} else {
					errorMsg="提交失败";
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

	OnLongClickListener onLongClickListener = new OnLongClickListener() {

		@Override
		public boolean onLongClick(final View v) {
			dialogShowMessage("确定要删除该明细？",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface face,
								int paramAnonymous2Int) {
							TextView tv_id = (TextView) v
									.findViewById(R.id.tv_id);
							data_mx.remove(Integer.parseInt(tv_id.getText()
									.toString()));
							Message msg = new Message();
							msg.what = Constant.NUM_6;
							handler.sendMessage(msg);
						}
					}, null);
			return false;
		}
	};

	private Handler handler = new Handler() {
		@SuppressLint("InflateParams")
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			switch (msg.what) {
			case Constant.NETWORK_ERROR:
				dialogShowMessage_P("网络连接错误，请检查您的网络是否正常", null);
				break;
			case Constant.SUCCESS:
				dialogShowMessage_P("提交成功",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								onBackPressed();
							}
						});
				break;
			case Constant.FAIL:
				dialogShowMessage_P(errorMsg, null);
				break;
			case Constant.NUM_6:
				ll_mx.removeAllViews();
				for (int i = 0; i < data_mx.size(); i++) {
					ll_mx_item = (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.listview_kfpd_item, null);
					final EditText et_sjsl = (EditText) ll_mx_item.findViewById(R.id.et_sjsl);
					Map<String, String> map = data_mx.get(i);
					final String dqkc = map.get("dqkc");
					final String kfmc = map.get("kfmc");
					final String cwmc = map.get("cwmc");
					final String hpmc = map.get("hpmc");
					((TextView) ll_mx_item.findViewById(R.id.tv_id)).setText(i
							+ "");
					((TextView) ll_mx_item.findViewById(R.id.tv_kfmc))
							.setText(map.get("kfmc"));
					((TextView) ll_mx_item.findViewById(R.id.tv_kfbm))
							.setText(map.get("kfbm"));
					((TextView) ll_mx_item.findViewById(R.id.tv_cwmc))
							.setText(map.get("cwmc"));
					((TextView) ll_mx_item.findViewById(R.id.tv_cwbm))
							.setText(map.get("cwbm"));
					((TextView) ll_mx_item.findViewById(R.id.tv_hpmc))
							.setText(map.get("hpmc"));
					((TextView) ll_mx_item.findViewById(R.id.tv_hpbm))
							.setText(map.get("hpbm"));
					((TextView) ll_mx_item.findViewById(R.id.tv_dqkc))
							.setText(dqkc);
					et_sjsl.setText(map.get("spsl"));
					et_sjsl.setOnFocusChangeListener(new OnFocusChangeListener() {
						
						@Override
						public void onFocusChange(View v, boolean hasFocus) {
							if(!hasFocus){
								if(isNotNull(et_sjsl)){
									if(Integer.parseInt(et_sjsl.getText().toString())!=Integer.parseInt(dqkc)){
										dialogShowMessage(kfmc+"-"+cwmc+"-"+hpmc+"当前库存和实际数量不一致", new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface face,
													int paramAnonymous2Int) {
												face.dismiss();
											}
										}, new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface face,
													int paramAnonymous2Int) {
												face.dismiss();
											}
										});
									}
								}
							}
						}
					});
					ll_mx_item.setOnLongClickListener(onLongClickListener);
					ll_mx.addView(ll_mx_item);
				}
				break;
			case Constant.NUM_7:
				dialogShowMessage("本周已完成了对账，是否再次对账？", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface face,
							int paramAnonymous2Int) {
						face.dismiss();
						showProgressDialog();
						Config.getExecutorService().execute(new Runnable() {

							@Override
							public void run() {

								getWebService("query");
							}
						});
					}
				},new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface face,
							int paramAnonymous2Int) {
						face.dismiss();
						onBackPressed();
					}
				});
				break;
			case Constant.NUM_8:
				showProgressDialog();
				Config.getExecutorService().execute(new Runnable() {

					@Override
					public void run() {

						getWebService("query");
					}
				});
				break;
			case Constant.NUM_9:
				
				break;
			}
			
		}
	};

}
