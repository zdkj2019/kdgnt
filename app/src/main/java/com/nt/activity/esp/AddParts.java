package com.nt.activity.esp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.activity.util.DataFilterActivity;
import com.nt.common.Constant;
import com.nt.utils.Config;

/**
 * 新增配件
 * 
 * @author zdkj
 *
 */
public class AddParts extends FrameActivity {

	private TextView tv_xl,tv_dj;
	private EditText et_sl;
	private RadioGroup rg_0;
	private ArrayList<Map<String, String>> data, data_bjlb;
	private Button confirm, cancel;
	private String msgStr,ds,ywhy_bm,pjid,pjmc,pjfy,sfhs_bm,zbh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_addpartsadd);
		initVariable();
		initView();
		initListeners();
	}

	@Override
	protected void initVariable() {
		data = new ArrayList<Map<String, String>>();
		Intent intent = getIntent();
		ds = intent.getStringExtra("ds");
		zbh = intent.getStringExtra("zbh");
		ywhy_bm = intent.getStringExtra("ywhy_bm");
		data = (ArrayList<Map<String, String>>) intent
				.getSerializableExtra("data");
		data_bjlb = (ArrayList<Map<String, String>>) intent
				.getSerializableExtra("bjlb");
	}

	@Override
	protected void initView() {
		tv_xl = (TextView) findViewById(R.id.tv_xl);
		rg_0 = (RadioGroup) findViewById(R.id.rg_0);
		et_sl = (EditText) findViewById(R.id.et_sl);
		tv_dj = (TextView) findViewById(R.id.tv_dj);
		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);

		title.setText("新增配件");
	}

	@Override
	protected void initListeners() {
		OnClickListener onClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		};

		topBack.setOnClickListener(onClickListener);
		cancel.setOnClickListener(onClickListener);

		confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!isNotNull(tv_xl)) {
					toastShowMessage("请选择配件小类");
					return;
				}
				if (!isNotNull(et_sl)) {
					toastShowMessage("请录入数量");
					return;
				}
				if (!isNotNull(tv_dj)) {
					toastShowMessage("请录入单价");
					return;
				}
				if ("0".equals(et_sl.getText().toString())) {
					toastShowMessage("数量不能为0");
					return;
				}
//				if ("0".equals(tv_dj.getText().toString())) {
//					toastShowMessage("单价不能为0");
//					return;
//				}
				String sfhs = rg_0.getCheckedRadioButtonId()==R.id.rb_0?"是":"否";
				
				Intent intent = getIntent();
				ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
				Map<String, String> map = new HashMap<String, String>();
				map.put("hpbm", tv_xl.getTag().toString());
				map.put("hpmc", tv_xl.getText().toString());
				map.put("sl", et_sl.getText().toString());
				map.put("dj", tv_dj.getText().toString());
				map.put("sfhs", sfhs);
				list.add(map);
				intent.putExtra("list", list);
				setResult(1, intent);
				finish();
			}
		});

		tv_xl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent1 = new Intent(getApplicationContext(),
						DataFilterActivity.class);
				intent1.putExtra("data", data_bjlb);
				startActivityForResult(intent1, 1);
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 1 && resultCode == 1 && data != null) {
			String str = data.getStringExtra("str");
			try {
				JSONObject json = new JSONObject(str);
				pjid = json.get("id").toString();
				pjmc = json.get("name").toString();
				sfhs_bm = json.get("sfhs_bm").toString();
				Config.getExecutorService().execute(new Runnable() {

					@Override
					public void run() {

						getWebService("getdj");
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void getWebService(String s) {
		if ("getdj".equals(s)) {
			try {
				String str = ds+"*"+ywhy_bm+"*"+pjid+"*"+zbh;
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_NT_WX_FWBG_CXBJFY", str, "uf_json_getdata",
						getApplicationContext());
				String flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					JSONObject temp = jsonArray.getJSONObject(0);
					pjfy = temp.getString("fy1");
				} else {
					pjfy = "0";
				}
				Message msg = new Message();
				msg.what = Constant.SUCCESS;
				handler.sendMessage(msg);
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
				tv_xl.setText(pjmc);
				tv_xl.setTag(pjid);
				tv_dj.setText(pjfy);
				RadioButton rb_0 = (RadioButton) rg_0.findViewById(R.id.rb_0);
				RadioButton rb_1 = (RadioButton) rg_0.findViewById(R.id.rb_1);
				if("1".equals(sfhs_bm)){//是
					rb_0.setChecked(true);
				}else{
					rb_1.setChecked(true);
				}
				break;
			case Constant.FAIL:
				dialogShowMessage_P("失败，" + msgStr, null);
				break;

			}
			if (progressDialog != null) {
				progressDialog.dismiss();
			}

		}

	};

}
