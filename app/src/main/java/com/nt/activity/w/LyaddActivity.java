package com.nt.activity.w;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.cache.DataCache;
import com.nt.common.Constant;
import com.nt.utils.Config;

public class LyaddActivity extends FrameActivity {

	private Spinner ly_type;
	private EditText ly_content;
	private Button confirm, cancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_lyadd);
		initView();
		initListeners();
	}

	@Override
	protected void initVariable() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initView() {
		title.setText("新增留言");
		ly_type = (Spinner) findViewById(R.id.ly_type);
		ly_content = (EditText) findViewById(R.id.ly_content);
		confirm = (Button) findViewById(R.id.confirm);
		cancel = (Button) findViewById(R.id.cancel);

		List list = new ArrayList<String>();
		list.add("意见");
		list.add("投诉");
		list.add("其他");
		ArrayAdapter adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ly_type.setAdapter(adapter);
	}

	@Override
	protected void initListeners() {
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.confirm:
					if (isNotNull(ly_content)) {
						showProgressDialog();
						Config.getExecutorService().execute(new Runnable() {

							@Override
							public void run() {
								getWebService("submit");
							}
						});
					} else {
						toastShowMessage("请录入留言内容");
					}
					break;

				case R.id.cancel:
					onBackPressed();
					break;
				case R.id.bt_topback:
					onBackPressed();
					break;
				}

			}
		};
		topBack.setOnClickListener(onClickListener);
		cancel.setOnClickListener(onClickListener);
		confirm.setOnClickListener(onClickListener);

	}

	@Override
	protected void getWebService(String s) {
		if ("submit".equals(s)) {
			try {
				String sqlid = "c#_PAD_APPXX_LYLR";
				int type = ly_type.getSelectedItemPosition() + 1;
				String cs = DataCache.getinition().getUserId() + "*PAM*"
						+ ly_content.getText().toString() + "*PAM*" + type;
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						sqlid, cs, "sblr", "sblr", "uf_json_setdata2", this);
				String flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {

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
				dialogShowMessage_P("保存成功！",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								onBackPressed();
							}
						});
				break;
			case Constant.FAIL:
				dialogShowMessage_P("保存失败！", null);
				break;
			}
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		}

	};

}
