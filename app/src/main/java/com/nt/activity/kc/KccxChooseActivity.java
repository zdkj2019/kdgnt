package com.nt.activity.kc;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.activity.kdg.JqgdcxKdg;
import com.nt.activity.kdg.ListKdg;
import com.nt.cache.DataCache;

public class KccxChooseActivity extends FrameActivity {

	private Button confirm, cancel;
	private RadioGroup rg_1,rg_2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_kdg_kccxchoose);
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

	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());
		rg_1 = (RadioGroup) findViewById(R.id.rg_1);
		rg_2 = (RadioGroup) findViewById(R.id.rg_2);
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
					Intent intent = new Intent(KccxChooseActivity.this, KccxActivity.class);
					String rg1 = rg_1.getCheckedRadioButtonId() == R.id.rb_1_s ? "1": "2";
					String rg2 = rg_2.getCheckedRadioButtonId() == R.id.rb_2_s ? "1": "2";
					intent.putExtra("rg1",rg1);
					intent.putExtra("rg2",rg2);
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
}