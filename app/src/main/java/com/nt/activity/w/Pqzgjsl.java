package com.nt.activity.w;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.activity.kdg.JqgdcxKdg;
import com.nt.activity.kdg.ListKdg;
import com.nt.activity.main.MainActivity;
import com.nt.cache.DataCache;

public class Pqzgjsl extends FrameActivity {

	private Button confirm,cancel;
	private EditText tv_1,tv_2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_w_pqzgjsl);
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
		tv_1 = (EditText) findViewById(R.id.tv_1);
		tv_2 = (EditText) findViewById(R.id.tv_2);
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
					if(isNotNull(tv_1)&&isNotNull(tv_2)){
						DataCache.getinition().setTitle("片区主管及时率");
						Intent intent = new Intent(Pqzgjsl.this, PqzgjslShow.class);
						intent.putExtra("status", tv_1.getText().toString()+"*"+tv_2.getText().toString());
						startActivity(intent);
					}else{
						toastShowMessage("请选择查询时间");
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
		
		tv_1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDateSelector(tv_1);
			}
		});
		
		tv_2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDateSelector(tv_2);
			}
		});
	}

	@Override
	protected void getWebService(String s) {
		
		
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			}
			if(progressDialog!=null){
				progressDialog.dismiss();
			}
		}
	};

}
