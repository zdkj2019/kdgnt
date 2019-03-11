package com.nt.activity.w;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.activity.login.LoginActivity;
import com.nt.cache.DataCache;
import com.nt.common.Constant;
import com.nt.utils.Config;
import com.nt.utils.IDCard;

/**
 * 工作牌信息录入
 * 
 * @author zdkj
 *
 */
public class GzpLrActivity extends FrameActivity {

	private EditText et_name, et_gh;
	private Button confirm, cancel;
	private SharedPreferences spf;
	private SharedPreferences.Editor spfe;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_w_ryxxlr);
		initVariable();
		initView();
		initListeners();
	}

	@SuppressLint("WorldReadableFiles")
	@SuppressWarnings("deprecation")
	@Override
	protected void initVariable() {
		et_name = (EditText) findViewById(R.id.et_name);
		et_gh = (EditText) findViewById(R.id.et_gh);
		confirm = (Button) findViewById(R.id.confirm);
		cancel = (Button) findViewById(R.id.cancel);
		spf = getSharedPreferences("gzpxx", GzpLrActivity.MODE_PRIVATE);
		et_name.setText(spf.getString("username", ""));
		et_gh.setText(spf.getString("gh", ""));
	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());

	}

	@Override
	protected void initListeners() {
		//
		OnClickListener backonClickListener = new OnClickListener() {

			@SuppressLint("ShowToast")
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.bt_topback:
					onBackPressed();
					break;
				case R.id.confirm:
					try {
						if (isNotNull(et_name) && isNotNull(et_gh)) {
							String ret = IDCard.IDCardValidate(et_gh
									.getText().toString().trim());
							if (!"".equals(ret)) {
								Toast.makeText(getApplicationContext(), ret, 1)
										.show();
								return;
							}
							spfe = spf.edit();
							spfe.putString("username", et_name.getText().toString().trim());
							spfe.putString("gh", et_gh.getText().toString().trim());
							spfe.commit();
							Message msg = new Message();
							msg.what = Constant.SUCCESS;
							handler.sendMessage(msg);
						}else{
							Message msg = new Message();
							msg.what = Constant.FAIL;
							handler.sendMessage(msg);
						}
					} catch (Exception e) {
						e.printStackTrace();
						Message msg = new Message();
						msg.what = Constant.FAIL;
						handler.sendMessage(msg);
					}

					break;
				case R.id.cancel:
					onBackPressed();
					break;
				default:
					break;
				}

			}
		};
		confirm.setOnClickListener(backonClickListener);
		cancel.setOnClickListener(backonClickListener);
		topBack.setOnClickListener(backonClickListener);
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
				dialogShowMessage_P("网络连接出错，请检查你的网络设置", null);
				break;
			case Constant.SUCCESS:
				dialogShowMessage_P("保存成功", null);
				break;
			case Constant.FAIL:
				dialogShowMessage_P("身份证验证失败", null);
				break;
			}
		}

	};

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

}
