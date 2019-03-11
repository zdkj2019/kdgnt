package com.nt.activity.w;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.activity.main.MainActivity;
import com.nt.cache.DataCache;
import com.nt.utils.DataUtil;

/**
 * 服务咨询师擦好像-筛选条件
 * 
 * @author zdkj
 *
 */
public class Fwzxscx extends FrameActivity {

	private Button confirm, cancel;
	private TextView tv_1, tv_2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_w_fwzxscx);
		initVariable();
		initView();
		initListeners();
	}

	@Override
	protected void initVariable() {
		tv_1 = (TextView) findViewById(R.id.tv_1);
		tv_2 = (TextView) findViewById(R.id.tv_2);
		tv_1.setText(DataUtil.toDataString("yyyy-MM-dd"));
		tv_2.setText(DataUtil.toDataString("yyyy-MM-dd"));
		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);
		confirm.setText("确定");
		cancel.setText("取消");

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
				case R.id.cancel:
					onBackPressed();
					break;
				case R.id.confirm:
					Intent intent = new Intent(Fwzxscx.this, FwzxscxList.class);
					intent.putExtra("start_time", tv_1.getText().toString());
					intent.putExtra("end_time", tv_2.getText().toString());
					startActivity(intent);
					break;
				case R.id.tv_1:
					showDateSelector(tv_1);
					break;
				case R.id.tv_2:
					showDateSelector(tv_2);
					break;
				default:
					break;
				}

			}
		};

		topBack.setOnClickListener(backonClickListener);
		cancel.setOnClickListener(backonClickListener);
		confirm.setOnClickListener(backonClickListener);
		tv_1.setOnClickListener(backonClickListener);
		tv_2.setOnClickListener(backonClickListener);
	}

	@Override
	protected void getWebService(String s) {

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

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("currType", 1);
		startActivity(intent);
		finish();
	}

}
