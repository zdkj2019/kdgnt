package com.nt.activity.js;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.activity.main.MainActivity;
import com.nt.activity.w.YhkxxActivity;
import com.nt.cache.DataCache;
import com.nt.cache.ServiceReportCache;
import com.nt.common.Constant;
import com.nt.utils.Config;

public class FyqrCompleteActivity extends FrameActivity {

	private TextView text_ds, text_khbm, text_bzr, text_gzxx, text_clfs,
			text_kzzd5, text_bzsj, text_wcsj, text_fy1, text_fy2;

	private Button confirm, cancel;

	private String flag, zbh;
	private boolean hasYhk = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_fyqrcomplete);
		initVariable();
		initView();
		initListeners();
		showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				getWebService("yhk");
			}
		});
	}

	@Override
	protected void initVariable() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initView() {
		text_ds = (TextView) findViewById(R.id.text_ds);
		text_khbm = (TextView) findViewById(R.id.text_khbm);
		text_bzr = (TextView) findViewById(R.id.text_bzr);
		text_gzxx = (TextView) findViewById(R.id.text_gzxx);
		text_clfs = (TextView) findViewById(R.id.text_clfs);
		text_kzzd5 = (TextView) findViewById(R.id.text_kzzd5);
		text_bzsj = (TextView) findViewById(R.id.text_bzsj);
		text_wcsj = (TextView) findViewById(R.id.text_wcsj);
		text_fy1 = (TextView) findViewById(R.id.text_fy1);
		text_fy2 = (TextView) findViewById(R.id.text_fy2);

		confirm = (Button) findViewById(R.id.confirm);
		cancel = (Button) findViewById(R.id.cancel);

		title.setText(DataCache.getinition().getTitle());

		Map<String, String> itemmap = ServiceReportCache.getData().get(
				ServiceReportCache.getIndex());
		zbh = itemmap.get("zbh");
		text_ds.setText(itemmap.get("ds"));
		text_khbm.setText(itemmap.get("khbm"));
		text_bzr.setText(itemmap.get("bzr"));
		text_gzxx.setText(itemmap.get("gzxx"));
		text_clfs.setText(itemmap.get("clfs"));
		text_kzzd5.setText(itemmap.get("kzzd5"));
		text_bzsj.setText(itemmap.get("bzsj"));
		text_wcsj.setText(itemmap.get("wcsj"));
		text_fy1.setText((itemmap.get("fy1").length() < 5 ? Double
				.parseDouble(itemmap.get("fy1")) : Double.parseDouble(itemmap
				.get("fy1").substring(0, 6)))
				+ "");
		text_fy2.setText((itemmap.get("fy2").length() < 5 ? Double
				.parseDouble(itemmap.get("fy2")) : Double.parseDouble(itemmap
				.get("fy2").substring(0, 6)))
				+ "");
	}

	@Override
	protected void initListeners() {
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
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

		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!hasYhk) {
					dialogShowMessage("银行卡信息为完善，去完善 ？",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface face,
										int paramAnonymous2Int) {
									Intent intent = new Intent(
											getApplicationContext(),
											YhkxxActivity.class);
									intent.putExtra("zbh", zbh);
									startActivityForResult(intent, 1);
								}
							}, null);
				} else {
					dialogShowMessage("确认接受 ？",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface face,
										int paramAnonymous2Int) {
									showProgressDialog();
									Config.getExecutorService().execute(
											new Runnable() {
												@Override
												public void run() {
													getWebService("submit");
												}
											});
								}
							}, null);
				}

			}
		});

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) { // resultCode为回传的标记，我在B中回传的是RESULT_OK
		case 1:
			hasYhk = true;
			break;
		default:
			break;
		}
	}

	@Override
	protected void getWebService(String s) {

		if (s.equals("yhk")) {
			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_YHK_CX", DataCache.getinition().getUserId(),
						"uf_json_getdata", this);
				JSONArray jsonArray = jsonObject.getJSONArray("tableA");
				jsonObject = (JSONObject) jsonArray.get(0);
				if (!"1".equals(jsonObject.getString("yhk"))) {
					hasYhk = true;
				}
				Message msg = new Message();
				msg.what = 2;//
				handler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = 6;//
				handler.sendMessage(msg);
			}
		}

		if (s.equals("submit")) {// 提交
			try {
				String sql = "update shgl_ywgl_fwbgszb set kzzd4=1 where zbh='"
						+ zbh + "'";
				Log.e("dd", sql.toString());

				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_RZ", sql, DataCache.getinition().getUserId(),
						"uf_json_setdata", this);
				flag = jsonObject.getString("flag");

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
				msg.what = Constant.NETWORK_ERROR;// 网络连接出错，你检查你的网络设置
				handler.sendMessage(msg);
			}

		}

	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case -1:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P("获取基础数据失败", null);
				break;
			case 1:
				dialogShowMessage_P("接收成功",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								onBackPressed();
							}
						});

				break;
			case 0:
				dialogShowMessage_P("失败，请检查后重试...错误标识：" + flag, null);
				break;
			case 2:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				break;

			case 6:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P("网络错误，请检查网络连接是否正常！",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								finish();
							}
						});
				break;

			}

		}
	};

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		intent.putExtra("currType", 3);
		startActivity(intent);
		finish();
	}
}
