package com.nt.activity.w;

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
import com.nt.cache.DataCache;
import com.nt.cache.ServiceReportCache;
/**
 * 服务咨询师查询显示
 * @author zdkj
 *
 */
public class FwzxscxShow extends FrameActivity {

	private Button confirm,cancel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_w_fwzxscxshow);
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

		Map<String, Object> itemmap = ServiceReportCache.getObjectdata().get(ServiceReportCache.getIndex());

		String zbh = itemmap.get("zbh").toString();
		((TextView) findViewById(R.id.tv_zbh)).setText(zbh);
		((TextView) findViewById(R.id.tv_1)).setText(itemmap.get("tv_1").toString());
		((TextView) findViewById(R.id.tv_2)).setText(itemmap.get("tv_2").toString());
		((TextView) findViewById(R.id.tv_3)).setText(itemmap.get("tv_3").toString());
		((TextView) findViewById(R.id.tv_4)).setText(itemmap.get("tv_4").toString());
		((TextView) findViewById(R.id.tv_5)).setText(itemmap.get("tv_5").toString());
		((TextView) findViewById(R.id.tv_6)).setText(itemmap.get("tv_6").toString());
		((TextView) findViewById(R.id.tv_7)).setText(itemmap.get("tv_7").toString());
		((TextView) findViewById(R.id.tv_8)).setText(itemmap.get("tv_8").toString());
		((TextView) findViewById(R.id.tv_9)).setText(itemmap.get("tv_9").toString());
		((TextView) findViewById(R.id.tv_10)).setText(itemmap.get("tv_10").toString());
		((TextView) findViewById(R.id.tv_11)).setText(itemmap.get("tv_11").toString());
		((TextView) findViewById(R.id.tv_12)).setText(itemmap.get("tv_12").toString());

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
					onBackPressed();
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

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

}
