package com.nt.activity.register;

import com.nt.R;
import com.nt.activity.BaseActivity;
import com.nt.activity.login.LoginActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class RegisterCompleteActivity extends BaseActivity {

	private Button btn_next;
	private TextView tv_loginid, tv_loginpwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_complete);
		initView();
		initListner();
	}

	private void initView() {
		btn_next = (Button) findViewById(R.id.btn_next);
		tv_loginid = (TextView) findViewById(R.id.tv_loginid);
		tv_loginpwd = (TextView) findViewById(R.id.tv_loginpwd);
		Intent intent = getIntent();
		tv_loginid.setText("您的手机号码");
		tv_loginpwd.setText("身份证后六位");
	}

	private void initListner() {
		btn_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dialogShowMessage("请牢记您的账号和密码，我们的工作人员会在24小时内联系您！",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent(
										getApplicationContext(),
										LoginActivity.class);
								startActivity(intent);
								finish();
							}
						}, null);
			}
		});
	}
}
