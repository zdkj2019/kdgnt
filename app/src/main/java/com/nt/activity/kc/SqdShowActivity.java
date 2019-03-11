package com.nt.activity.kc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.CharacterStyle;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import com.nt.R;
import com.nt.Parser.JSONObjectParser;
import com.nt.activity.FrameActivity;
import com.nt.activity.main.MainActivity;
import com.nt.cache.DataCache;
import com.nt.cache.ServiceReportCache;
import com.nt.common.Constant;
import com.nt.utils.Config;
import com.nt.utils.DataUtil;

/**
 * 申请单显示
 * 
 * @author hs
 */
@SuppressLint({ "HandlerLeak", "WorldReadableFiles" })
public class SqdShowActivity extends FrameActivity {

	private TextView tv_zbh, tv_sgdh, tv_startdate, tv_gdbz;
	private Button confirm, cancel;
	private LinearLayout ll_mx, ll_mx_item;
	private ArrayList<Map<String, String>> data_mx;
	private String zbh = "", flag = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_sqdshow);
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

		tv_zbh = (TextView) findViewById(R.id.tv_zbh);
		tv_startdate = (TextView) findViewById(R.id.tv_startdate);
		tv_sgdh = (TextView) findViewById(R.id.tv_sgdh);
		tv_gdbz = (TextView) findViewById(R.id.tv_gdbz);
		ll_mx = (LinearLayout) findViewById(R.id.ll_mx);
		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);
	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());
		data_mx = new ArrayList<Map<String, String>>();

		Map<String, String> itemmap = ServiceReportCache.getData().get(
				ServiceReportCache.getIndex());
		zbh = itemmap.get("zbh").toString();
		tv_zbh.setText(zbh);
		tv_startdate.setText(itemmap.get("zdrq").toString());
		tv_sgdh.setText(itemmap.get("sgdh").toString());
		tv_gdbz.setText(itemmap.get("bz").toString());

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

	@SuppressLint("InflateParams")
	@Override
	protected void getWebService(String s) {

		if ("query".equals(s)) {
			try {
				JSONObject jsonObject_mx = callWebserviceImp.getWebServerInfo(
						"_PAD_CCGL_SQDCX2", zbh, "uf_json_getdata", this);
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
							newMap.put("id", temp.get("mxh"));
							newMap.put("ckkfmc_name",
									temp.get("jcsj_sczzjg_kfbm"));
							newMap.put("hpmc_name",
									temp.get("hpgl_jcsj_hpxxb_hpbm"));
							newMap.put("sl", temp.get("ybsl"));
							newMap.put("dqkc", temp.get("kcsl"));
							newMap.put("sbsl", temp.get("sbsl"));
							data.add(newMap);
						}
						data_mx = data;
						Message msg = new Message();
						msg.what = Constant.NUM_6;
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

		if ("submit".equals(s)) {
			try {
				String sql = zbh + "*PAM*" + DataCache.getinition().getUserId();
				String mxStr = "";
				int count = ll_mx.getChildCount();
				for (int i = 0; i < count; i++) {
					Map<String, String> map = data_mx.get(i);
					ll_mx_item = (LinearLayout) ll_mx.getChildAt(i);
					RadioGroup rg_sfzf = (RadioGroup) ll_mx_item
							.findViewById(R.id.rg_sfzf);
					String iszf = "0";
					if (rg_sfzf.getCheckedRadioButtonId() == R.id.rb_s) {
						iszf = "1";
					}
					mxStr = mxStr + map.get("id") + "#@#" + iszf + "#^#";
				}
				if (!"".equals(mxStr)) {
					mxStr = mxStr.substring(0, mxStr.length() - 3);
				}
				sql = sql + "*PAM*" + mxStr;
				flag = this.callWebserviceImp.getWebServerInfo(
						"c#_PAD_YWGL_CCGL_SQZF", sql,
						DataCache.getinition().getUserId(),
						DataCache.getinition().getUserId(), "uf_json_setdata2",
						this).getString("flag");
				if (Integer.parseInt(flag) > 0) {
					Message msg = new Message();
					msg.what = Constant.SUCCESS;
					handler.sendMessage(msg);
				} else {
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

	private Handler handler = new Handler() {
		@SuppressLint("InflateParams")
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
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
				dialogShowMessage_P("提交失败，返回" + flag, null);
				break;
			case Constant.NUM_6:
				ll_mx.removeAllViews();
				for (int i = 0; i < data_mx.size(); i++) {
					ll_mx_item = (LinearLayout) LayoutInflater.from(
							getApplicationContext()).inflate(
							R.layout.listview_sqd_item, null);

					Map<String, String> map = data_mx.get(i);
					((TextView) ll_mx_item.findViewById(R.id.tv_ckkfmc))
							.setText(map.get("ckkfmc_name"));
					((TextView) ll_mx_item.findViewById(R.id.tv_hpmc))
							.setText(map.get("hpmc_name"));
					((TextView) ll_mx_item.findViewById(R.id.tv_sl))
							.setText(map.get("sl"));
					((TextView) ll_mx_item.findViewById(R.id.tv_dqkc))
							.setText(map.get("dqkc"));

					String sbsl = map.get("sbsl");
					if (Integer.parseInt(sbsl) == 0) {
						ll_mx_item.findViewById(R.id.ll_sfzf).setVisibility(
								View.VISIBLE);
					}

					ll_mx_item.findViewById(R.id.ll_dqkc).setVisibility(
							View.VISIBLE);
					ll_mx.addView(ll_mx_item);
				}
				break;
			}
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		}
	};

}
