package com.nt.activity.w;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.nt.R;
import com.nt.activity.FrameActivity;
public class YHKMbActivity extends FrameActivity {

	private TextView tv_gz;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_yhkmb);
		initVariable();
		initView();
		initListeners();

	}

	@Override
	protected void initVariable() {

	}

	@Override
	protected void initView() {
		title.setText("银行卡更改证明模板");
		tv_gz = (TextView) findViewById(R.id.tv_gz);
		Intent intent = getIntent();
		String xb = intent.getStringExtra("xb");
		if("1".equals(xb)){
			tv_gz.setText("签字（按手印）：");
		}
	}

	@Override
	protected void initListeners() {
		topBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.bt_topback:
					onBackPressed();
					break;
				}
			}
		});
	}

	@Override
	protected void getWebService(String s) {

	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			switch (msg.what) {

			}
		};
	};
}
