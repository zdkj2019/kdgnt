package com.nt.activity.w;

import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.common.Constant;

public class SetParams extends FrameActivity {

	private TextView tv_val;
	private EditText et_val;
	private Button confirm, cancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_params);
		initVariable();
		initView();
		initListeners();
	}

	@Override
	protected void initVariable() {
		tv_val = (TextView) findViewById(R.id.tv_val);
		et_val = (EditText) findViewById(R.id.et_val);
		confirm = (Button) findViewById(R.id.confirm);
		cancel = (Button) findViewById(R.id.cancel);
		title.setText("参数设置");
	}

	@Override
	protected void initView() {
		tv_val.setText(Constant.STM_WEBSERVICE_URL_dx);
		et_val.setText(Constant.STM_WEBSERVICE_URL_dx);
	}

	@Override
	protected void initListeners() {
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.confirm:
					if("".equals(et_val.getText().toString())){
						toastShowMessage("请录入新的wenservice地址");
					}else{
						Constant.STM_WEBSERVICE_URL_dx = et_val.getText().toString();
						dialogShowMessage("修改成功", null, null);
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
		confirm.setOnClickListener(onClickListener);
		cancel.setOnClickListener(onClickListener);
		topBack.setOnClickListener(onClickListener);
	}

	@Override
	protected void getWebService(String s) {
		// TODO Auto-generated method stub

	}

}
