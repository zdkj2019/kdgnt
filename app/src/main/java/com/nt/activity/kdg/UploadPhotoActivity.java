package com.nt.activity.kdg;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.activity.main.MainActivity;
import com.nt.cache.DataCache;
import com.nt.common.Constant;
import com.nt.utils.Config;
import com.nt.utils.ImageUtil;

public class UploadPhotoActivity extends FrameActivity {

	private TextView tv_pz;
	private Button confirm, cancel;
	private ArrayList<String> list_photo;
	private String errorMsg,zbh;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_uploadphoto);
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
		
	}

	@Override
	protected void initView() {
		title.setText("上传巡检照片");
		tv_pz = (TextView) findViewById(R.id.tv_pz);
		zbh = getIntent().getStringExtra("zbh");
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
					if(list_photo==null||list_photo.size()==0){
						toastShowMessage("请选择照片");
						return;
					}
					showProgressDialog();
					Config.getExecutorService().execute(new Runnable() {

						@Override
						public void run() {
							upload();
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
					flag = uploadPic(i + "", zbh, readJpeg(file),
							"c#_PAD_ERP_CCGL_SBZP_AZ");
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
		// TODO Auto-generated method stub
		
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
								Intent intent = new Intent(
										getApplicationContext(),
										MainActivity.class);
								startActivity(intent);
								finish();
							}
						});
				break;

			}

		}

	};

}
