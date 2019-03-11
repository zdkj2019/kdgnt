package com.nt.activity.w;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.nt.R;
import com.nt.activity.FrameActivity;

public class LyActivity extends FrameActivity{

	private LinearLayout ly_add, ly_query;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_ly);
		initView();
		initListeners();
	}

	@Override
	protected void initVariable() {

	}

	@Override
	protected void initView() {
		ly_add = (LinearLayout) findViewById(R.id.ly_add);
		ly_query = (LinearLayout) findViewById(R.id.ly_query);
		title.setText("留言");
	}

	@Override
	protected void initListeners() {
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.ly_add:
					Intent intent0 = new Intent(LyActivity.this,
							LyaddActivity.class);
					startActivity(intent0);
					break;
				case R.id.ly_query:
					Intent intent1 = new Intent(LyActivity.this,
							LyqueryActivity.class);
					startActivity(intent1);
					break;
				case R.id.bt_topback:
					onBackPressed();
					break;
				default:
					break;
				}

			}
		};
		ly_add.setOnClickListener(onClickListener);
		ly_query.setOnClickListener(onClickListener);
		topBack.setOnClickListener(onClickListener);
	}

	@Override
	protected void getWebService(String s) {
		// TODO Auto-generated method stub

	}

}
