package com.nt.activity.kdg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.common.Constant;
import com.nt.utils.Config;
import com.nt.zxing.CaptureActivity;

/**
 * 维修服务报告 设备序列号
 * 
 * @author zdkj
 *
 */
public class Sbxlh extends FrameActivity {

	private Button confirm, cancel;
	private LinearLayout ll_sbxlh;
	private ArrayList<String> listdata;
	private String[] data_sbxlh_search;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_sbxlh);
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
		ll_sbxlh = (LinearLayout) findViewById(R.id.ll_sbxlh);
	}

	@SuppressLint("InflateParams")
	@Override
	protected void initView() {
		title.setText("设备序列号");
		int num = getIntent().getIntExtra("num", 0);
		ArrayList<String> data = getIntent().getStringArrayListExtra("data_search");
		data_sbxlh_search =(String[])data.toArray(new String[data.size()]);  
		data = getIntent().getStringArrayListExtra("data");
		listdata = new ArrayList<String>();
		if (data == null) {
			for (int i = 0; i < num; i++) {
				listdata.add("");
			}
		} else {
			if (num <= data.size()) {
				for (int i = 0; i < num; i++) {
					listdata.add(data.get(i));
				}
			}
			if (num > data.size()) {
				for (int i = 0; i < data.size(); i++) {
					listdata.add(data.get(i));
				}
				for (int i = data.size(); i < num; i++) {
					listdata.add("");
				}
			}
		}

		for (int i = 0; i < listdata.size(); i++) {
			View view = LayoutInflater.from(getApplicationContext()).inflate(
					R.layout.include_sbxlh, null);
			TextView tv_xlh = (TextView) view.findViewById(R.id.tv_xlh);
			tv_xlh.setText("序列号" + (i + 1) + "：");
			AutoCompleteTextView act_content = (AutoCompleteTextView) view
					.findViewById(R.id.act_content);
			act_content.setText(listdata.get(i));
			act_content.setAdapter(new ArrayAdapter<String>(
					getApplicationContext(),
					android.R.layout.simple_dropdown_item_1line, data_sbxlh_search));
			ll_sbxlh.addView(view);
		}
	}

	@Override
	protected void initListeners() {
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.bt_topback:
					Intent intent  = getIntent();
					intent.putStringArrayListExtra("data_sbxlh", listdata);
					setResult(-1, intent);
					finish();
					break;
				case R.id.confirm:
					int num = ll_sbxlh.getChildCount();
					String xlhs = "";
					for (int i = 0; i < num; i++) {
						View view = ll_sbxlh.getChildAt(i);
						AutoCompleteTextView act_content = (AutoCompleteTextView) view
								.findViewById(R.id.act_content);
						String xlh = act_content.getText().toString().trim();
						if ("".equals(xlh)) {
							toastShowMessage("序列号" + (i + 1) + "不能为空，请录入完整");
							return;
						}
						if (xlhs.indexOf(xlh) != -1) {
							toastShowMessage("序列号" + (i + 1) + "已存在，请重新录入");
							return;
						}
						xlhs += xlh + ",";
					}
					if(!"".equals(xlhs)){
						xlhs = xlhs.substring(0,xlhs.length()-1);
					}
					String[] values = xlhs.split(",");
					listdata = new ArrayList<String>();
					for(int i=0;i<values.length;i++){
						listdata.add(values[i]);
					}
					Intent intent1  = getIntent();
					intent1.putStringArrayListExtra("data_sbxlh", listdata);
					setResult(-1, intent1);
					finish();
					break;
				case R.id.cancel:
					Intent intent2  = getIntent();
					intent2.putStringArrayListExtra("data_sbxlh", listdata);
					setResult(-1, intent2);
					finish();
					break;
				}
			}
		};

		topBack.setOnClickListener(onClickListener);
		confirm.setOnClickListener(onClickListener);
		cancel.setOnClickListener(onClickListener);

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
				dialogShowMessage_P("失败，没有数据",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								onBackPressed();
							}
						});
				break;
			}
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		}

	};

}
