package com.nt.activity.register;

import com.nt.R;
import com.nt.activity.FrameActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

public class RegisterActivity extends FrameActivity {


	private RadioGroup radiogroup;
	private Button button_next;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		initView();
		initListener();
	}
	
	protected void initView(){
		radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
		button_next = (Button) findViewById(R.id.button_next);
	};
	
	private void initListener(){
		button_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int num = radiogroup.getCheckedRadioButtonId();
				int type = 0;
				if(num==-1){
					Toast.makeText(getApplicationContext(), "请选择合作类型", 1).show();
					return;
				}else if(num==R.id.rb1){
					type = 1;
				}else if(num==R.id.rb2){
					type = 2;
				}
				Intent intent = new Intent(getApplicationContext(),RegisterConfirmActivity.class);
				intent.putExtra("type", type);
				startActivity(intent);
			}
		});
		
	}

	@Override
	protected void initVariable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initListeners() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void getWebService(String s) {
		// TODO Auto-generated method stub
		
	}
}
