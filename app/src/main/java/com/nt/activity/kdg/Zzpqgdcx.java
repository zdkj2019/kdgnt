package com.nt.activity.kdg;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONException;
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
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.activity.main.MainActivity;
import com.nt.cache.DataCache;
import com.nt.utils.Config;

/**
 * 组长片区工单查询
 * 
 * @author zdkj
 *
 */
public class Zzpqgdcx extends FrameActivity {

	private Button confirm, cancel;
	private RadioGroup rg_1, rg_2;
	private String tsid = "", nr = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_zzpqgdcx);
		initVariable();
		initView();
		initListeners();
		if (tsid != null && !"".equals(tsid)) {
			dialogShowMessage_P(nr, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface face, int paramAnonymous2Int) {
					Config.getExecutorService().execute(new Runnable() {

						@Override
						public void run() {
							getWebService("submit");

						}
					});
				}
			});
		}
	}

	@Override
	protected void initVariable() {

		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);
		confirm.setText("确定");
		cancel.setText("取消");

	}

	@Override
	protected void initView() {

		title.setText("组长片区工单查询");
		rg_1 = (RadioGroup) findViewById(R.id.rg_1);
		rg_2 = (RadioGroup) findViewById(R.id.rg_2);

		tsid = getIntent().getStringExtra("tsid");
		nr = getIntent().getStringExtra("nr");
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
					DataCache.getinition().setQueryType(2501);
					Intent intent = new Intent(Zzpqgdcx.this, ListKdg.class);
					intent.putExtra("status",
							rg_1.getCheckedRadioButtonId() == R.id.rb_1_s ? 1
									: 2);
					intent.putExtra("sj",
							rg_2.getCheckedRadioButtonId() == R.id.rb_2_s ? 1
									: 2);
					startActivity(intent);
					break;
				default:
					break;
				}

			}
		};

		topBack.setOnClickListener(backonClickListener);
		cancel.setOnClickListener(backonClickListener);
		confirm.setOnClickListener(backonClickListener);

		rg_1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.rb_1_s) {
					findViewById(R.id.ll_gdsj).setVisibility(View.VISIBLE);
					findViewById(R.id.ll_gdsj_line).setVisibility(View.VISIBLE);
				} else {
					findViewById(R.id.ll_gdsj).setVisibility(View.GONE);
					findViewById(R.id.ll_gdsj_line).setVisibility(View.GONE);
				}
			}
		});
	}

	@Override
	protected void getWebService(String s) {
		if ("submit".equals(s)) {
			try {
				String sql = "update shgl_kdg_xxts set djzt = '2' where tsid ='"
						+ tsid + "'";
				callWebserviceImp.getWebServerInfo("_RZ", sql, DataCache
						.getinition().getUserId(), "uf_json_setdata", this);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			}
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		}
	};

	// @Override
	// public void onBackPressed() {
	// Intent intent = new Intent(this, MainActivity.class);
	// intent.putExtra("currType", 5);
	// startActivity(intent);
	// finish();
	// }

}
