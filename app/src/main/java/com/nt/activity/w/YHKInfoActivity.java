package com.nt.activity.w;

import java.io.File;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.activity.main.MainActivity;
import com.nt.cache.DataCache;
import com.nt.utils.Config;
import com.nt.utils.IDCard;
import com.nt.utils.ImageUtil;

public class YHKInfoActivity extends FrameActivity {

	private EditText et_khm, et_yhkh, et_khh;
	private String khm, yhkh, khh,xm,xb;
	private Button btn_change;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_yhkinfo);
		initVariable();
		initView();
		initListeners();
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

	}

	@Override
	protected void initView() {
		title.setText("银行卡信息");
		et_khm = (EditText) findViewById(R.id.et_khm);
		et_yhkh = (EditText) findViewById(R.id.et_yhkh);
		et_khh = (EditText) findViewById(R.id.et_khh);
		btn_change = (Button) findViewById(R.id.btn_change);
		et_khm.setEnabled(false);
		et_yhkh.setEnabled(false);
		et_khh.setEnabled(false);
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

		btn_change.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(YHKInfoActivity.this,
						YhkxxActivity.class);
				intent.putExtra("type", 1);
				intent.putExtra("khm", et_khm.getText().toString());
				intent.putExtra("yhkh", et_yhkh.getText().toString());
				intent.putExtra("khh", et_khh.getText().toString());
				intent.putExtra("xb", xb);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

	}

	@Override
	protected void getWebService(String s) {
		if ("query".equals(s)) {
			try {
				JSONObject object = callWebserviceImp.getWebServerInfo(
						"_PAD_JCSJ_YHKCX", DataCache.getinition().getUserId(),
						"uf_json_getdata", this);
				if (Integer.parseInt(object.getString("flag")) > 0) {
					JSONArray array = object.getJSONArray("tableA");
					JSONObject temp = array.getJSONObject(0);
					khm = temp.getString("kzzd3");
					yhkh = temp.getString("yhk");
					khh = temp.getString("khyh");
					xm = temp.getString("xm");
					xb = temp.getString("xb");
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = 0;
				handler.sendMessage(msg);
			}
		}

	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 0:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage("查询失败，系统错误！", null, null);
				break;
			case 1:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				et_khm.setText(khm);
				et_yhkh.setText(yhkh);
				et_khh.setText(khh);
				break;
			}
		};
	};
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		intent.putExtra("currType", 5);
		startActivity(intent);
		finish();
	};
}
