package com.nt.activity.kdg;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.activity.main.MainActivity;
import com.nt.activity.util.BaiduMapActivity;
import com.nt.cache.DataCache;
import com.nt.cache.ServiceReportCache;

/**
 * 快递柜-近期工单查询-数据展示
 * 
 * @author zdkj
 *
 */
public class JqgdcxShowKdg extends FrameActivity {

	private Button cancel;
	private TextView tv_xxdz;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_kdg_jqgdcxshow);
		initVariable();
		initView();
		initListeners();
	}

	@Override
	protected void initVariable() {

		cancel = (Button) findViewById(R.id.cancel);
		cancel.setText("返回");

	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());
		tv_xxdz =(TextView) findViewById(R.id.tv_xxdz);
		
		Map<String, Object> itemmap = ServiceReportCache.getObjectdata().get(
				ServiceReportCache.getIndex());

		String zbh = itemmap.get("zbh").toString();
		((TextView) findViewById(R.id.tv_zbh)).setText(zbh);
		((TextView) findViewById(R.id.tv_axdh)).setText(itemmap.get("sx")
				.toString());
		((TextView) findViewById(R.id.tv_qy)).setText(itemmap.get("qy")
				.toString());
		((TextView) findViewById(R.id.tv_xqmc)).setText(itemmap.get("xqmc")
				.toString());
		((TextView) findViewById(R.id.tv_xxdz)).setText("   "+itemmap.get("xxdz")
				.toString());
		((TextView) findViewById(R.id.tv_gzxx)).setText(itemmap.get("gzxx")
				.toString());
		((TextView) findViewById(R.id.tv_ywlx)).setText(itemmap.get("ywlx")
				.toString());
		((TextView) findViewById(R.id.tv_ds)).setText(itemmap.get("bz")
				.toString());

		((TextView) findViewById(R.id.tv_sf)).setText(itemmap.get("sf")
				.toString());
		((TextView) findViewById(R.id.tv_hb)).setText(itemmap.get("ywhy")
				.toString());
		((TextView) findViewById(R.id.tv_sflx)).setText(itemmap.get("axdh")
				.toString());
		((TextView) findViewById(R.id.tv_sbsx)).setText(itemmap.get("sflx")
				.toString());
		((TextView) findViewById(R.id.tv_rgfy)).setText(itemmap.get("rgfy")
				.toString());
		((TextView) findViewById(R.id.tv_pjfy)).setText(itemmap.get("pjfy")
				.toString());
		((TextView) findViewById(R.id.tv_sblx)).setText(itemmap.get("sblx_mc")
				.toString());
		((TextView) findViewById(R.id.tv_sbxh)).setText(itemmap.get("sbxh_mc")
				.toString());
		((TextView) findViewById(R.id.tv_wxts)).setText(itemmap.get("wxts")
				.toString());

		((TextView) findViewById(R.id.tv_wgfs)).setText(itemmap.get("wgfs")
				.toString());
		((TextView) findViewById(R.id.tv_wgsj)).setText(itemmap.get("wgsj")
				.toString());
		
		findViewById(R.id.iv_baidumap).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						BaiduMapActivity.class);
				intent.putExtra("keyStr", tv_xxdz.getText().toString().trim());
				startActivity(intent);
			}
		});

	}

	@Override
	protected void initListeners() {
		//
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
				default:
					break;
				}

			}
		};

		topBack.setOnClickListener(onClickListener);
		cancel.setOnClickListener(onClickListener);
	}

	@Override
	protected void getWebService(String s) {

	}
	//
	// @Override
	// public void onBackPressed() {
	// super.onBackPressed();
	// finish();
	// }

}
