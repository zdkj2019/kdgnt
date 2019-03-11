package com.nt.activity.esp;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.cache.DataCache;
import com.nt.common.Constant;
import com.nt.utils.Config;
import com.nt.utils.ImageUtil;

/**
 * 无此设备
 * 
 * @author Administrator 20170410
 */
@SuppressLint("HandlerLeak")
public class YwsbActivity extends FrameActivity {

	private TextView tv_pz, tv_sf, tv_ds, tv_qx, tv_wdmc, tv_xxdz;
	private EditText et_bz;
	private Button confirm, cancel;
	private ArrayList<String> list_photo;
	private String hpbm, sbbm, errorMsg = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_ywsb);
		initVariable();
		initView();
		initListeners();

	}

	@Override
	protected void initVariable() {
		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);
		confirm.setText("确定");
		cancel.setText("取消");

		tv_pz = (TextView) findViewById(R.id.tv_pz);

		tv_sf = (TextView) findViewById(R.id.tv_sf);
		tv_ds = (TextView) findViewById(R.id.tv_ds);
		tv_qx = (TextView) findViewById(R.id.tv_qx);
		tv_wdmc = (TextView) findViewById(R.id.tv_wdmc);
		tv_xxdz = (TextView) findViewById(R.id.tv_xxdz);
		et_bz = (EditText) findViewById(R.id.et_bz);

		confirm = (Button) findViewById(R.id.confirm);
		cancel = (Button) findViewById(R.id.cancel);
	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());
		Intent intent = getIntent();
		hpbm = intent.getStringExtra("hpbm");
		sbbm = intent.getStringExtra("sbbm");
		tv_sf.setText(getIntent().getStringExtra("sf_mc"));
		tv_ds.setText(getIntent().getStringExtra("ds_mc"));
		tv_qx.setText(getIntent().getStringExtra("qx_mc"));
		tv_wdmc.setText(getIntent().getStringExtra("wdbm_mc"));
		tv_xxdz.setText(getIntent().getStringExtra("xxdz"));
	}

	@Override
	protected void initListeners() {
		OnClickListener onClickListener = new OnClickListener() {

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
					showProgressDialog();
					Config.getExecutorService().execute(new Runnable() {

						@Override
						public void run() {

							getWebService("submit");
						}
					});
					break;
				}
			}
		};

		topBack.setOnClickListener(onClickListener);
		cancel.setOnClickListener(onClickListener);
		confirm.setOnClickListener(onClickListener);

		tv_pz.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				camera(5, list_photo);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 5 && resultCode == Activity.RESULT_OK) {
			list_photo = data.getStringArrayListExtra("imglist");
			if (list_photo != null) {
				if (list_photo.size() > 0) {
					tv_pz.setText("继续选择");
					tv_pz.setBackgroundResource(R.drawable.btn_normal_yellow);
				} else {
					tv_pz.setText("选择图片");
					tv_pz.setBackgroundResource(R.drawable.btn_normal);
				}
			}

		}
	}

	private void upload() {
		try {
			boolean flag = true;
			for (int i = 0; i < list_photo.size(); i++) {
				if (flag) {
					String filepath = list_photo.get(i);
					filepath = filepath.substring(7, filepath.length());
					// 压缩图片到100K
					filepath = ImageUtil.compressAndGenImage(ImageUtil.ratio(filepath,getScreenWidth(),getScreenHeight()), 200, "jpg");
					File file = new File(filepath);
					flag = uploadPic(i + "", hpbm, readJpeg(file),
							"c#_PAD_SBGL_WDWSBZP");
					file.delete();
				} else {
					flag = false;
					break;
				}
			}
			if (flag) {
				Message msg = new Message();
				msg.what = Constant.SUCCESS;
				handler.sendMessage(msg);
			} else {
				errorMsg = "上传图片失败";
				Message msg = new Message();
				msg.what = Constant.FAIL;
				handler.sendMessage(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Message msg = new Message();
			msg.what = Constant.NETWORK_ERROR;
			handler.sendMessage(msg);
		}

	}

	protected boolean uploadPic(String num, String mxh, final byte[] data1,
			String sqlid) throws Exception {

		if (data1 != null && mxh != null) {
			JSONObject json = callWebserviceImp.getWebServerInfo2_pic(sqlid,
					num, mxh, "0001", data1, "uf_json_setdata2_p11",
					getApplicationContext());
			String flag = json.getString("flag");
			if ("1".equals(flag)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	@Override
	protected void getWebService(String s) {

		if ("submit".equals(s)) {

			try {

				String str = "";
				str += hpbm + "*PAM*";
				str += sbbm + "*PAM*";
				str += DataCache.getinition().getUserId() + "*PAM*";
				str += et_bz.getText().toString();

				JSONObject json = this.callWebserviceImp.getWebServerInfo(
						"c#_PAD_SBGL_WDWSB", str, "", "", "uf_json_setdata2",
						this);
				String flag = json.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					if (list_photo != null) {
						upload();
					} else {
						Message msg = new Message();
						msg.what = Constant.SUCCESS;
						handler.sendMessage(msg);
					}
				} else {
					errorMsg = "提交信息失败，" + json.getString("msg");
					Message msg = new Message();
					msg.what = Constant.FAIL;
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;
				handler.sendMessage(msg);
			}
		}

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			switch (msg.what) {
			case Constant.FAIL:
				dialogShowMessage_P("失败，" + errorMsg, null);
				break;
			case Constant.NETWORK_ERROR:
				dialogShowMessage_P("网络连接出错，请检查你的网络设置", null);
				break;
			case Constant.SUCCESS:
				dialogShowMessage_P("提交成功！",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								Intent intent = getIntent();
								setResult(1, intent);
								finish();
							}
						});
				break;

			}

		}

	};
}