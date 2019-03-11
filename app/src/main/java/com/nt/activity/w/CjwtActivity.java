package com.nt.activity.w;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.cache.DataCache;

public class CjwtActivity extends FrameActivity {

	private LinearLayout cjwt_kdg, cjwt_pos, cjwt_lbj, cjwt_js, cjwt_app,
			cjwt_qt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_cjwt);
		initView();
		initListeners();
	}

	@Override
	protected void initVariable() {

	}

	@Override
	protected void initView() {
		cjwt_kdg = (LinearLayout) findViewById(R.id.cjwt_kdg);
		cjwt_pos = (LinearLayout) findViewById(R.id.cjwt_pos);
		cjwt_lbj = (LinearLayout) findViewById(R.id.cjwt_lbj);
		cjwt_js = (LinearLayout) findViewById(R.id.cjwt_js);
		cjwt_app = (LinearLayout) findViewById(R.id.cjwt_app);
		cjwt_qt = (LinearLayout) findViewById(R.id.cjwt_qt);
		title.setText("常见问题");
	}

	@Override
	protected void initListeners() {
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.cjwt_kdg:
					Intent intent0 = new Intent(CjwtActivity.this,
							CjwtShowActivity.class);
					intent0.putExtra("type", 1);
					startActivity(intent0);
					break;
				case R.id.cjwt_pos:
					Intent intent1 = new Intent(CjwtActivity.this,
							CjwtShowActivity.class);
					intent1.putExtra("type", 2);
					startActivity(intent1);
					break;
				case R.id.cjwt_lbj:
					Intent intent2 = new Intent(CjwtActivity.this,
							CjwtShowActivity.class);
					intent2.putExtra("type", 3);
					startActivity(intent2);
					break;
				case R.id.cjwt_js:
					Intent intent3 = new Intent(CjwtActivity.this,
							CjwtShowActivity.class);
					intent3.putExtra("type", 4);
					startActivity(intent3);
					break;
				case R.id.cjwt_app:
					Intent intent4 = new Intent(CjwtActivity.this,
							CjwtShowActivity.class);
					intent4.putExtra("type", 5);
					startActivity(intent4);
					break;
				case R.id.cjwt_qt:
					Intent intent5 = new Intent(CjwtActivity.this,
							CjwtShowActivity.class);
					intent5.putExtra("type", 6);
					startActivity(intent5);
					break;
				case R.id.bt_topback:
					onBackPressed();
					break;
				default:
					break;
				}

			}
		};
		cjwt_kdg.setOnClickListener(onClickListener);
		cjwt_pos.setOnClickListener(onClickListener);
		cjwt_lbj.setOnClickListener(onClickListener);
		cjwt_js.setOnClickListener(onClickListener);
		cjwt_app.setOnClickListener(onClickListener);
		cjwt_qt.setOnClickListener(onClickListener);
		topBack.setOnClickListener(onClickListener);
	}

	@Override
	protected void getWebService(String s) {
		// TODO Auto-generated method stub

	}

}
