package com.nt.activity.w;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.cache.DataCache;

public class HelpActivity extends FrameActivity {

	private LinearLayout help_glzd, help_cjwt, help_ly;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_help);
		initView();
		initListeners();
	}

	@Override
	protected void initVariable() {

	}

	@Override
	protected void initView() {
		help_glzd = (LinearLayout) findViewById(R.id.help_glzd);
		help_cjwt = (LinearLayout) findViewById(R.id.help_cjwt);
		help_ly = (LinearLayout) findViewById(R.id.help_ly);

		title.setText(DataCache.getinition().getTitle());
	}

	@Override
	protected void initListeners() {
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.help_glzd:
					Intent intent0 = new Intent(HelpActivity.this,
							GlzdActivity.class);
					startActivity(intent0);
					break;
				case R.id.help_cjwt:
					Intent intent1 = new Intent(HelpActivity.this,
							CjwtActivity.class);
					startActivity(intent1);
					break;
				case R.id.help_ly:
					Intent intent2 = new Intent(HelpActivity.this,
							LyActivity.class);
					startActivity(intent2);
					break;
				case R.id.bt_topback:
					onBackPressed();
					break;
				default:
					break;
				}

			}
		};

		help_glzd.setOnClickListener(onClickListener);
		help_cjwt.setOnClickListener(onClickListener);
		help_ly.setOnClickListener(onClickListener);
		topBack.setOnClickListener(onClickListener);
	}

	@Override
	protected void getWebService(String s) {
		// TODO Auto-generated method stub

	}

}
