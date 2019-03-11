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

/**
 * 设备信息核对
 * 
 * @author zdkj
 *
 */
public class SbxxpdActivity extends FrameActivity {

	private Button confirm, cancel, btn_sm,btn_zdh;
	private EditText et_zsdz, et_qtycxx, et_zdh;
	private TextView tv_sbbm;
	private JSONObject data;
	private String flag, message, sbbm,zdh,hpbm = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_kdg_sbxxpd);
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

	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());

		btn_sm = (Button) findViewById(R.id.btn_sm);
		btn_zdh = (Button) findViewById(R.id.btn_zdh);
		et_zsdz = (EditText) findViewById(R.id.et_zsdz);
		et_qtycxx = (EditText) findViewById(R.id.et_qtycxx);
		et_zdh = (EditText) findViewById(R.id.et_zdh);
		tv_sbbm = (TextView) findViewById(R.id.tv_sbbm);
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
					if (!isNotNull(et_zdh)) {
						toastShowMessage("请录入终端号");
						return;
					}
					if("".equals(hpbm)){
						toastShowMessage("请查询后提交");
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
					if (isNotNull(et_zdh)) {
						startSm();
					} else {
						toastShowMessage("请录入终端号");
					}
					break;
				case R.id.btn_zdh:
					if (isNotNull(et_zdh)) {
						zdh = et_zdh.getText().toString();
						showProgressDialog();
						Config.getExecutorService().execute(new Runnable() {

							@Override
							public void run() {
								getWebService("query");
							}
						});
					} else {
						toastShowMessage("请录入终端号");
					}
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
		btn_zdh.setOnClickListener(backonClickListener);
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
						"_PAD_KDG_SBXX_HD", zdh, "uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					data = jsonArray.getJSONObject(0);
					Message msg = new Message();
					msg.what = Constant.NUM_6;
					handler.sendMessage(msg);

				} else {
					// flag = jsonObject.getString("msg");
					Message msg = new Message();
					msg.what = Constant.NUM_8;// 失败
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
						"_PAD_KDG_SBXX_HD_PEWM", sbbm,
						"uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag)==1) {
					Message msg = new Message();
					msg.what = Constant.NUM_7;
					handler.sendMessage(msg);
				}else{
					sbbm = "";
					message = "该二维码不正确";
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
				// 先上传设备信息
				message = "提交成功！";
				String str = "";
				// 再提交服务报告
				String typeStr = "sbxxpd";
				str = hpbm+"*PAM*"+sbbm+"*PAM*"+et_zsdz.getText().toString()+"*PAM*"+et_qtycxx.getText().toString();
				JSONObject json = this.callWebserviceImp.getWebServerInfo(
						"c#_WEB_EWM_SBPD_KDG", str, typeStr, typeStr,
						"uf_json_setdata2", this);
				flag = json.getString("flag");

				if (Integer.parseInt(flag) > 0) {

					Message msg = new Message();
					msg.what = Constant.SUCCESS;
					handler.sendMessage(msg);
				} else {
					//flag = json.getString("msg");
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

	private void loadSbxx() {
		try {
			if (data != null) {
				hpbm = data.getString("hpbm");
				((TextView) findViewById(R.id.tv_1)).setText(data.getString("sblx"));
				((TextView) findViewById(R.id.tv_2)).setText(data.getString("sbbm"));
				((TextView) findViewById(R.id.tv_3)).setText(data.getString("simkh"));
				((TextView) findViewById(R.id.tv_4)).setText(data.getString("sf"));
				((TextView) findViewById(R.id.tv_5)).setText(data.getString("ds"));
				((TextView) findViewById(R.id.tv_6)).setText(data.getString("qx"));
				((TextView) findViewById(R.id.tv_7)).setText(data.getString("wdmc"));
				((TextView) findViewById(R.id.tv_8)).setText(data.getString("xxdz"));
				((TextView) findViewById(R.id.tv_9)).setText(data.getString("fgsl"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void resetSbxx(){
		hpbm = "";
		((TextView) findViewById(R.id.tv_1)).setText("");
		((TextView) findViewById(R.id.tv_2)).setText("");
		((TextView) findViewById(R.id.tv_3)).setText("");
		((TextView) findViewById(R.id.tv_4)).setText("");
		((TextView) findViewById(R.id.tv_5)).setText("");
		((TextView) findViewById(R.id.tv_6)).setText("");
		((TextView) findViewById(R.id.tv_7)).setText("");
		((TextView) findViewById(R.id.tv_8)).setText("");
		((TextView) findViewById(R.id.tv_9)).setText("");
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.FAIL:
				dialogShowMessage_P("查询失败，请检查后重试...错误标识：" + flag, null);
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
				loadSbxx();
				break;
			case Constant.NUM_7:
				tv_sbbm.setText(sbbm);
				break;
			case Constant.NUM_9:
				dialogShowMessage_P(message,null);
				break;
			case Constant.NUM_8:
				resetSbxx();
				break;
			}
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		}
	};
//
//	@Override
//	public void onBackPressed() {
//		Intent intent = new Intent(this, MainActivity.class);
//		intent.putExtra("currType", 1);
//		startActivity(intent);
//		finish();
//	}

}
