package com.nt.activity.kc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.CharacterStyle;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.activity.main.MainActivity;
import com.nt.cache.DataCache;
import com.nt.cache.ServiceReportCache;
import com.nt.common.Constant;
import com.nt.utils.Config;
import com.nt.utils.DataUtil;

/**
 * 库存查询展示
 * 
 * @author hs
 */
@SuppressLint({ "HandlerLeak", "WorldReadableFiles" })
public class ThcxShowActivity extends FrameActivity {

	private String zbh;
	private Map<String, Object> map;
	private Button confirm, cancel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_thxxshow);
		initVariable();
		initView();
		initListeners();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {

				getWebService("query");
			}
		});
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
		Map<String, Object> itemmap = ServiceReportCache.getObjectdata().get(
				ServiceReportCache.getIndex());
		zbh = itemmap.get("zbh").toString();
	}

	@Override
	protected void initListeners() {
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
				}
				
			}
		};
		topBack.setOnClickListener(backonClickListener);
		cancel.setOnClickListener(backonClickListener);
		confirm.setOnClickListener(backonClickListener);
	}

	@Override
	protected void getWebService(String s) {
		if("query".equals(s)){
			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_DBCK_THCX2", zbh,
						"uf_json_getdata", this);
				String flag = jsonObject.getString("flag");
				JSONArray jsonArray = jsonObject.getJSONArray("tableA");
				if (Integer.parseInt(flag) > 0) {
					JSONObject temp = jsonArray.getJSONObject(0);
					map = new HashMap<String, Object>();
					map.put("mxh", temp.getString("mxh"));
					map.put("zbh", temp.getString("zbh"));
					map.put("kfmc1", temp.getString("kfmc1"));
					map.put("cwmc1", temp.getString("cwmc1"));
					map.put("kfmc2", temp.getString("kfmc2"));
					map.put("cwmc2", temp.getString("cwmc2"));
					map.put("hpmc", temp.getString("hpmc"));
					map.put("sl", temp.getString("sl"));
					map.put("zssl", temp.getString("zssl"));
					map.put("jldw", temp.getString("jldw"));
					map.put("bz", temp.getString("bz"));
					map.put("dqkc", temp.getString("dqkc"));
					Message msg = new Message();
					msg.what = Constant.SUCCESS;// 成功
					handler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = Constant.FAIL;// 失败
					handler.sendMessage(msg);
				}

			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}
		}
		
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
				((TextView) findViewById(R.id.tv_1)).setText(map.get("kfmc1").toString());
				((TextView) findViewById(R.id.tv_2)).setText(map.get("cwmc1").toString());
				((TextView) findViewById(R.id.tv_3)).setText(map.get("kfmc2").toString());
				((TextView) findViewById(R.id.tv_4)).setText(map.get("cwmc2").toString());
				((TextView) findViewById(R.id.tv_5)).setText(map.get("hpmc").toString());
				((TextView) findViewById(R.id.tv_6)).setText(map.get("jldw").toString());
				((TextView) findViewById(R.id.tv_7)).setText(map.get("sl").toString());
				((TextView) findViewById(R.id.tv_8)).setText(map.get("zssl").toString());
				((TextView) findViewById(R.id.tv_9)).setText(map.get("dqkc").toString());
				((TextView) findViewById(R.id.tv_10)).setText(map.get("bz").toString());
				break;

			case Constant.FAIL:
				dialogShowMessage_P("你查询的时间段内，没有数据",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								onBackPressed();
							}

						});
				break;

			}
			if (!backboolean) {
				progressDialog.dismiss();
			}
		}

	};
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}
