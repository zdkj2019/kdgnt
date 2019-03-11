package com.nt.activity.w;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.activity.login.LoginActivity;
import com.nt.activity.main.MainActivity;
import com.nt.cache.DataCache;
import com.nt.utils.Config;

public class ChangePasswordActivity extends FrameActivity {

	private EditText et_ops, et_nps, et_snps;
	private TextView et_users;
	private Button confirm, cancel;
	private String loginps, nps, ops;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_changepassword);
		initVariable();
		initView();
		initListeners();

	}

	@Override
	protected void initVariable() {

		et_users = (TextView) findViewById(R.id.et_users);
		et_ops = (EditText) findViewById(R.id.et_ops);
		et_nps = (EditText) findViewById(R.id.et_nps);
		et_snps = (EditText) findViewById(R.id.et_snps);
		confirm = ((Button) findViewById(R.id.confirm).findViewById(
				R.id.confirm));
		cancel = ((Button) findViewById(R.id.cancel).findViewById(R.id.cancel));
	}

	@Override
	protected void initView() {
		title.setText(DataCache.getinition().getTitle());
		loginps = getSharedPreferences("loginsp",
				LoginActivity.MODE_PRIVATE).getString("userPs", "");
		String userstext = "(" + DataCache.getinition().getUserId() + ")"
				+ DataCache.getinition().getUsername();
		et_users.setText(userstext);

	}

	@Override
	protected void initListeners() {
		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				ops = et_ops.getText().toString();
				nps = et_nps.getText().toString();
				String snps = et_snps.getText().toString();
				if (!ops.equals(loginps)) {
					// 旧密码错
					toastShowMessage("原密码输入错误");

				} else if (nps.equals("") || nps == null) {
					// 新密码为空
					toastShowMessage("请输入新密码");

				} else if (snps.equals("") || snps == null) {
					// 新密码确认为空
					toastShowMessage("请再次输入新密码");

				} else if (!nps.equals(snps)) {
					// 两次密码不同
					toastShowMessage("两次输入的密码不同");

				} else {
					// toastShowMessage("submit");
					showProgressDialog();
					Config.getExecutorService().execute(new Runnable() {

						@Override
						public void run() {

							getWebService("submit");
						}
					});

				}
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();

			}
		});
		topBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();

			}
		});
	}

	@Override
	protected void getWebService(String s) {
		try {
			String flag = callWebserviceImp.getWebServerInfo("_PAD_XGMM", "ok",
					DataCache.getinition().getUserId(), ops + "*" + nps,
					"uf_json_setdata2", this).getString("flag");

			if (Integer.parseInt(flag) > 0) {
				Message localMessage2 = new Message();
				localMessage2.what = 1;// 完成
				handler.sendMessage(localMessage2);

			} else {
				Message localMessage3 = new Message();
				localMessage3.what = -1;
				handler.sendMessage(localMessage3);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Message localMessage1 = new Message();
			localMessage1.what = 0;// 网络连接出错
			handler.sendMessage(localMessage1);
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case -1:
				dialogShowMessage_P("密码修改失败", null);
				break;
			case 1:
				dialogShowMessage_P("修改密码成功，请重新登陆",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
								startActivity(intent);
								finish();
							}
						});

				break;
			case 0:
				dialogShowMessage_P("网络异常，请稍后重试...", null);
				break;

			}
			progressDialog.dismiss();
		}
	};

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		intent.putExtra("currType", 5);
		startActivity(intent);
		finish();
	}
}
