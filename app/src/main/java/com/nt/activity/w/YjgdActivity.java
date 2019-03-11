package com.nt.activity.w;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.activity.kdg.ListKdg;
import com.nt.activity.util.BaiduMapActivity;
import com.nt.cache.DataCache;
import com.nt.cache.ServiceReportCache;
import com.nt.common.Constant;
import com.nt.utils.Config;

public class YjgdActivity extends FrameActivity {

	private Button confirm, cancel;
	private ImageView iv_telphone, iv_telphone1;
	private RadioGroup rg_1;
	private EditText et_clsm;
	private TextView tv_xxdz;
	private String flag, zbh, msgStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_w_yjgd);
		initVariable();
		initView();
		initListeners();
	}

	@Override
	protected void initVariable() {

		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);
		confirm.setText("确定");
		cancel.setText("取消");

		iv_telphone = (ImageView) findViewById(R.id.iv_telphone);
		iv_telphone1 = (ImageView) findViewById(R.id.iv_telphone1);
		rg_1 = (RadioGroup) findViewById(R.id.rg_1);
		et_clsm = (EditText) findViewById(R.id.et_clsm);
		tv_xxdz = (TextView) findViewById(R.id.tv_6);
	}

	@Override
	protected void initView() {

		title.setText("预警工单");

		final Map<String, Object> itemmap = ServiceReportCache.getObjectdata()
				.get(ServiceReportCache.getIndex());

		zbh = itemmap.get("zbh").toString();
		((TextView) findViewById(R.id.tv_1)).setText(zbh);
		((TextView) findViewById(R.id.tv_2)).setText(itemmap.get("sx")
				.toString());
		((TextView) findViewById(R.id.tv_3)).setText(itemmap.get("ds")
				.toString());
		((TextView) findViewById(R.id.tv_4)).setText(itemmap.get("qy")
				.toString());
		((TextView) findViewById(R.id.tv_5)).setText(itemmap.get("xqmc")
				.toString());
		((TextView) findViewById(R.id.tv_6)).setText("   "+itemmap.get("xxdz")
				.toString());
		((TextView) findViewById(R.id.tv_7)).setText(itemmap.get("khlxr")
				.toString());
		((TextView) findViewById(R.id.tv_8)).setText("   "
				+ itemmap.get("lxdh").toString());
		((TextView) findViewById(R.id.tv_9)).setText(itemmap.get("jbf")
				.toString());
		((TextView) findViewById(R.id.tv_10)).setText("   "
				+ itemmap.get("sjhm").toString());
		((TextView) findViewById(R.id.tv_11)).setText(itemmap.get("gzxx")
				.toString());
		((TextView) findViewById(R.id.tv_12)).setText(itemmap.get("kzzf7")
				.toString());
		((TextView) findViewById(R.id.tv_13)).setText(itemmap.get("sblx")
				.toString());
		((TextView) findViewById(R.id.tv_14)).setText(itemmap.get("sbxh")
				.toString());
		((TextView) findViewById(R.id.tv_15)).setText(itemmap.get("ywhy")
				.toString());
		((TextView) findViewById(R.id.tv_16)).setText(itemmap.get("sflx")
				.toString());
		((TextView) findViewById(R.id.tv_17)).setText(itemmap.get("kzzf1")
				.toString());
		((TextView) findViewById(R.id.tv_18)).setText(itemmap.get("bzsj")
				.toString());
		((TextView) findViewById(R.id.tv_19)).setText(itemmap.get("ddsj")
				.toString());
		((TextView) findViewById(R.id.tv_20)).setText(itemmap.get("wcsj")
				.toString());
		((TextView) findViewById(R.id.tv_21)).setText(itemmap.get("hfpjsj")
				.toString());
		((TextView) findViewById(R.id.tv_22)).setText(itemmap.get("tsyjnr")
				.toString());
		((TextView) findViewById(R.id.tv_23)).setText(itemmap.get("hfsm")
				.toString());
		((TextView) findViewById(R.id.tv_24)).setText(itemmap.get("djzt")
				.toString());
		((TextView) findViewById(R.id.tv_25)).setText(itemmap.get("bz")
				.toString());

		iv_telphone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Call(itemmap.get("lxdh").toString());
			}
		});

		iv_telphone1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Call(itemmap.get("sjhm").toString());
			}
		});

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
				case R.id.cancel:
					onBackPressed();
					break;
				case R.id.confirm:
					if (!isNotNull(et_clsm)) {
						toastShowMessage("请录入处理说明");
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

		topBack.setOnClickListener(onClickListener);
		cancel.setOnClickListener(onClickListener);
		confirm.setOnClickListener(onClickListener);
		
		findViewById(R.id.iv_baidumap).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),BaiduMapActivity.class);
				intent.putExtra("keyStr", tv_xxdz.getText().toString().trim());
				startActivity(intent);
			}
		});
	}

	@Override
	protected void getWebService(String s) {
		if (s.equals("submit")) {// 提交
			try {
				String typeStr = "";
				msgStr = "提交成功";
				int tsjg = rg_1.getCheckedRadioButtonId() == R.id.rb_1_s ? 1
						: 2;
				String clsm = et_clsm.getText().toString().trim();
				String str = zbh + "*PAM*" + DataCache.getinition().getUserId()
						+ "*PAM*" + tsjg
						+ "*PAM*" + clsm;
				JSONObject json = this.callWebserviceImp.getWebServerInfo(
						"c#_PAD_KDG_TSYJ", str, typeStr, typeStr,
						"uf_json_setdata2", this);
				flag = json.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					Message msg = new Message();
					msg.what = Constant.SUCCESS;
					handler.sendMessage(msg);
				} else {
					msgStr = json.getString("msg");
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
				dialogShowMessage_P("失败，" + msgStr, null);
				break;
			case Constant.SUCCESS:
				dialogShowMessage_P(msgStr,
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
			}
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		}
	};
}
