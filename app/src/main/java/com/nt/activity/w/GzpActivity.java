package com.nt.activity.w;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.cache.DataCache;
import com.nt.common.Constant;
import com.nt.utils.Config;

/**
 * 人员信息
 * 
 * @author zdkj
 *
 */
public class GzpActivity extends FrameActivity {

	private TextView tv_name, tv_bm, tv_zw, tv_gh;
	private String name, bm, zw, gh;
	private SharedPreferences spf;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_w_ryxx);
		initVariable();
		initView();
		initListeners();
	}

	@Override
	protected void initVariable() {
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_bm = (TextView) findViewById(R.id.tv_bm);
		tv_zw = (TextView) findViewById(R.id.tv_zw);
		tv_gh = (TextView) findViewById(R.id.tv_gh);
		spf = getSharedPreferences("gzpxx", GzpLrActivity.MODE_PRIVATE);
		tv_name.setText(spf.getString("username", ""));
		tv_gh.setText(spf.getString("gh", ""));
	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());

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
				default:
					break;
				}

			}
		};

		topBack.setOnClickListener(backonClickListener);
	}

	@Override
	protected void getWebService(String s) {
		
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.NETWORK_ERROR:
				dialogShowMessage_P(Constant.NETWORK_ERROR_STR, null);
				break;
			case Constant.SUCCESS:
				
				break;
			case Constant.FAIL:
				dialogShowMessage_P("查询失败", null);
				break;
			}
			if (!backboolean) {
				progressDialog.dismiss();
			}
		}

	};

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

}
