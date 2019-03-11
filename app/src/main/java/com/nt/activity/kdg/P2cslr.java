package com.nt.activity.kdg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.activity.util.BaiduMapActivity;
import com.nt.cache.DataCache;
import com.nt.cache.ServiceReportCache;
import com.nt.common.Constant;
import com.nt.utils.Config;

/**
 * 片区工单查询
 * 
 * @author zdkj
 *
 */
public class P2cslr extends FrameActivity {

	private Button confirm, cancel;
	private String flag, zbh, type = "1", message;
	private ArrayList<Map<String, String>> data_csyy;
	private ArrayList<String> list_csyy;
	private Spinner spinner_csyy;
	private EditText et_csnr;
	private TextView tv_xxdz;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_p2cslr);
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
		tv_xxdz = (TextView) findViewById(R.id.tv_5);
		spinner_csyy = (Spinner) findViewById(R.id.spinner_csyy);
		et_csnr = (EditText) findViewById(R.id.et_csnr);
	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());

		final Map<String, Object> itemmap = ServiceReportCache.getObjectdata()
				.get(ServiceReportCache.getIndex());

		zbh = itemmap.get("zbh").toString();
		((TextView) findViewById(R.id.tv_1)).setText(zbh);
		((TextView) findViewById(R.id.tv_2)).setText(itemmap.get("sx")
				.toString());
		((TextView) findViewById(R.id.tv_3)).setText(itemmap.get("qy")
				.toString());
		((TextView) findViewById(R.id.tv_4)).setText(itemmap.get("xqmc")
				.toString());
		((TextView) findViewById(R.id.tv_5)).setText("   "+itemmap.get("xxdz")
				.toString());
		((TextView) findViewById(R.id.tv_6)).setText(itemmap.get("gzxx")
				.toString());
		((TextView) findViewById(R.id.tv_7)).setText(itemmap.get("ywlx")
				.toString());
		((TextView) findViewById(R.id.tv_9)).setText(itemmap.get("ywhy")
				.toString());
		((TextView) findViewById(R.id.tv_10)).setText("1".equals(itemmap.get("sfcs")
				.toString())?"是":"否");
		((TextView) findViewById(R.id.tv_11)).setText(itemmap.get("csyymc")
				.toString());
		((TextView) findViewById(R.id.tv_12)).setText(itemmap.get("csyybm")
				.toString());
		((TextView) findViewById(R.id.tv_13)).setText(itemmap.get("bzsj")
				.toString());
		((TextView) findViewById(R.id.tv_14)).setText(itemmap.get("ddsj")
				.toString());
		((TextView) findViewById(R.id.tv_15)).setText(itemmap.get("wcsj")
				.toString());
		((TextView) findViewById(R.id.tv_16)).setText(itemmap.get("sf")
				.toString());
		((TextView) findViewById(R.id.tv_17)).setText(itemmap.get("bz")
				.toString());
		
		findViewById(R.id.iv_baidumap).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						BaiduMapActivity.class);
				intent.putExtra("keyStr", tv_xxdz.getText().toString().trim());
				startActivity(intent);
			}
		});
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
					if ("".equals(data_csyy.get(
							spinner_csyy.getSelectedItemPosition())
							.get("csyy"))) {
						toastShowMessage("请选择超时原因！");
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
				default:
					break;
				}

			}
		};

		topBack.setOnClickListener(backonClickListener);
		cancel.setOnClickListener(backonClickListener);
		confirm.setOnClickListener(backonClickListener);

	}

	@Override
	protected void getWebService(String s) {
		if ("query".equals(s)) {
			try {
				data_csyy = new ArrayList<Map<String, String>>();
				list_csyy = new ArrayList<String>();
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SHGL_GDCSYYLY_CSYY", "", "uf_json_getdata", this);
				flag = jsonObject.getString("flag");

				Map<String, String> item = new HashMap<String, String>();
				item.put("csyy", "");
				item.put("yymc", "");
				list_csyy.add("");
				data_csyy.add(item);

				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						item = new HashMap<String, String>();
						item.put("csyy", temp.getString("csyybm"));
						item.put("yymc", temp.getString("yymc"));
						list_csyy.add(temp.getString("yymc"));
						data_csyy.add(item);
					}
				}
				
				Message msg = new Message();
				msg.what = Constant.NUM_6;
				handler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;
				handler.sendMessage(msg);
			}
		}

		if (s.equals("submit")) {// 提交
			try {
				String typeStr = "zzzp";
				message = "保存成功";
				String str = zbh
						+ "*PAM*"
						+ ((Map<String, String>) data_csyy.get(spinner_csyy
								.getSelectedItemPosition())).get("csyy")
						+ "*PAM*" + et_csnr.getText().toString() ;
				JSONObject json = this.callWebserviceImp.getWebServerInfo(
						"c#_PAD_KDG_P2CSLY", str, typeStr, typeStr,
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
				ArrayAdapter adapter1 = new ArrayAdapter<String>(P2cslr.this,
						android.R.layout.simple_spinner_item, list_csyy);
				adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner_csyy.setAdapter(adapter1);
				break;
			}
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		}
	};
}
