package com.nt.activity.kdg;

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
import android.widget.TextView;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.cache.DataCache;
import com.nt.cache.ServiceReportCache;
import com.nt.common.Constant;
import com.nt.utils.Config;
import com.nt.zxing.CaptureActivity;

/**
 * 快递柜-服务报告-扫描二维码
 * 
 * @author zdkj
 *
 */
public class SbxxlrKdg extends FrameActivity {

	private String ewm,msgStr,zbh;
	private TextView tv_sbxh,tv_sbbm,tv_sblx,tv_wdmc,tv_sshy;
	private EditText et_ewm;
	private Button btn_sm,confirm,cancel;
	private JSONObject json;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_sbewm);
		initVariable();
		initView();
		initListeners();

		startSm();
	}

	@Override
	protected void initVariable() {
		final Map<String, Object> itemmap = ServiceReportCache.getObjectdata()
				.get(ServiceReportCache.getIndex());

		zbh = itemmap.get("zbh").toString();
		title.setText("扫描二维码");
	}

	@Override
	protected void initView() {

		
		tv_sbxh = (TextView) findViewById(R.id.tv_sbxh);
		tv_sbbm = (TextView) findViewById(R.id.tv_sbbm);
		tv_sblx = (TextView) findViewById(R.id.tv_sblx);
		tv_wdmc = (TextView) findViewById(R.id.tv_wdmc);
		tv_sshy = (TextView) findViewById(R.id.tv_sshy);
		
		et_ewm = (EditText) findViewById(R.id.et_ewm);
		btn_sm = (Button) findViewById(R.id.btn_sm);
		
		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);
		confirm.setText("确定");
		cancel.setText("取消");
		
		
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
					if(json==null||!isNotNull(et_ewm)){
						dialogShowMessage_P("请扫描二维码", null);
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
					json = null;
					startSm();
					break;
				}
			}
		};

		topBack.setOnClickListener(onClickListener);
		confirm.setOnClickListener(onClickListener);
		cancel.setOnClickListener(onClickListener);
		btn_sm.setOnClickListener(onClickListener);
	}
	
	private void startSm(){
		// 二维码
		Intent intent = new Intent(getApplicationContext(),
				CaptureActivity.class);
		startActivityForResult(intent, 2);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0 && resultCode == 0) {
			Intent intent = new Intent(getApplicationContext(),
					CaptureActivity.class);
			startActivityForResult(intent, 2);
		} else if (requestCode == 2 && resultCode == 2) {
			// 二维码
			ewm = data.getStringExtra("result");
			showProgressDialog();
			Config.getExecutorService().execute(new Runnable() {

				@Override
				public void run() {
					getWebService("_PAD_YWGL_SMEWM");
				}
			});
		}
	}

	@Override
	protected void getWebService(String s) {
		
		if ("_PAD_YWGL_SMEWM".equals(s)) {

			JSONObject jsonObject = null;
			try {
				jsonObject = callWebserviceImp.getWebServerInfo("_PAD_SHGL_KDG_EWMSB",
						ewm+"*"+zbh, "uf_json_getdata", this);
				String flag = jsonObject.getString("flag");
				if(Integer.parseInt(flag)==1){
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					jsonObject = (JSONObject) jsonArray.get(0);
					json = jsonObject;
					Message msg = new Message();
					msg.what = Constant.NUM_6;
					handler.sendMessage(msg);
				}else{
					msgStr = "信息不匹配，请联系客服";
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

		if ("submit".equals(s)) {

			try {
				String cs = zbh;
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo("_PAD_SHGL_KDG_EWMDWCS", cs,"uf_json_getdata",this);
				String f = jsonObject.getString("flag");
				if (Integer.parseInt(f) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					jsonObject = (JSONObject) jsonArray.get(0);
					String sfcs = jsonObject.getString("sfcs");
					cs = zbh+"*PAM*"+DataCache.getinition().getUserId()+"*PAM*"+ewm+"*PAM*"+sfcs;
					jsonObject = callWebserviceImp.getWebServerInfo("c#_PAD_YWGL_SMEWM", cs,"","","uf_json_setdata2",this);
					f = jsonObject.getString("flag");
					if (Integer.parseInt(f) > 0) {
						if("1".equals(sfcs)){
							msgStr = "距定位时间超过10分钟，请重新定位";
						}else{
							msgStr = "提交成功";
						}
						Message msg = new Message();
						msg.what = Constant.SUBMIT_SUCCESS;
						handler.sendMessage(msg);
					} else {
						msgStr = jsonObject.getString("msg");
						Message msg = new Message();
						msg.what = Constant.FAIL;
						handler.sendMessage(msg);
					}
					
				} else {
					Message msg = new Message();
					msg.what = Constant.FAIL;
					handler.sendMessage(msg);
				}
				

			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;// 网络连接出错，你检查你的网络设置
				handler.sendMessage(msg);
			}
		}

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			switch (msg.what) {
			case Constant.NETWORK_ERROR:
				dialogShowMessage_P(Constant.NETWORK_ERROR_STR, null);
				break;
			case Constant.FAIL:
				dialogShowMessage_P(msgStr, null);
				break;
			case Constant.SUBMIT_SUCCESS:
				dialogShowMessage_P(msgStr,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								onBackPressed();
							}
						});
				break;
			case Constant.NUM_6:
				try{
					et_ewm.setText(ewm);
					tv_sbxh.setText(json.getString("sbxh1"));
					tv_sbbm.setText(json.getString("sbbm1"));
					tv_sblx.setText(json.getString("sblx1"));
					tv_wdmc.setText(json.getString("wdmc1"));
					tv_sshy.setText(json.getString("ywhy1"));
				}catch(Exception e){
					
				}
				break;
			case Constant.NUM_7:
				dialogShowMessage_P(msgStr,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								onBackPressed();
							}
						});
				break;
			}

		}

	};
}
