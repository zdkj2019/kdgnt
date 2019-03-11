package com.nt.activity.w;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.activity.main.MainActivity;
import com.nt.cache.DataCache;
import com.nt.common.Constant;
import com.nt.utils.Config;
import com.nt.utils.IDCard;
import com.nt.utils.ImageUtil;

public class YhkxxActivity extends FrameActivity {

	private Spinner spinner_yh;
	private TextView tv_yykmb;
	private EditText et_khm, et_yhkh, et_fh, et_zh, et_flc, et_yhkpz,
			et_yhkggzm;
	private ImageView image_yhkpz, image_yhkggzm;
	private String filename, zbh, flag, xb;
	private File yhk_file = null, yhk_file2 = null;
	private int photonum = 1, type = 0;
	private LinearLayout ll_yykggzm;
	private Button confirm, cancel;

	private List list;
	private List<Map<String, String>> list_data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_yhk);
		initVariable();
		initView();
		initListeners();
		showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				getWebService("getyk");
			}
		});
	}

	@Override
	protected void initVariable() {

	}

	@Override
	protected void initView() {

		spinner_yh = (Spinner) findViewById(R.id.spinner_yh);
		tv_yykmb = (TextView) findViewById(R.id.tv_yykmb);
		et_khm = (EditText) findViewById(R.id.et_khm);
		et_fh = (EditText) findViewById(R.id.et_fh);
		et_zh = (EditText) findViewById(R.id.et_zh);
		et_flc = (EditText) findViewById(R.id.et_flc);
		et_yhkh = (EditText) findViewById(R.id.et_yhkh);
		et_yhkpz = (EditText) findViewById(R.id.et_yhkpz);
		image_yhkpz = (ImageView) findViewById(R.id.image_yhkpz);
		ll_yykggzm = (LinearLayout) findViewById(R.id.ll_yykggzm);
		et_yhkggzm = (EditText) findViewById(R.id.et_yhkggzm);
		image_yhkggzm = (ImageView) findViewById(R.id.image_yhkggzm);

		confirm = (Button) findViewById(R.id.confirm);
		cancel = (Button) findViewById(R.id.cancel);

		title.setText("银行卡信息完善");
		list = new ArrayList();
		list_data = new ArrayList<Map<String, String>>();
		Intent intent = getIntent();
		type = intent.getIntExtra("type", 0);
		if (type == 1) {
			ll_yykggzm.setVisibility(View.VISIBLE);
			tv_yykmb.setVisibility(View.VISIBLE);
			tv_yykmb.setText(Html.fromHtml("<u>" + "银行卡更改证明模板" + "</u>"));
		} else {
			ll_yykggzm.setVisibility(View.GONE);
			tv_yykmb.setVisibility(View.GONE);
		}

		et_khm.setText(intent.getStringExtra("khm"));
		et_yhkh.setText(intent.getStringExtra("yhkh"));
		xb = intent.getStringExtra("xb");
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
				case R.id.confirm:
					addYhk();
					break;
				case R.id.cancel:
					onBackPressed();
					break;
				case R.id.et_yhkpz:
					photonum = 1;
					camera();
					break;
				case R.id.et_yhkggzm:
					photonum = 2;
					camera();
					break;
				case R.id.tv_yykmb:
					Intent intent = new Intent(YhkxxActivity.this,
							YHKMbActivity.class);
					intent.putExtra(xb, xb);
					startActivity(intent);
					break;
				}
			}
		};
		topBack.setOnClickListener(onClickListener);
		confirm.setOnClickListener(onClickListener);
		cancel.setOnClickListener(onClickListener);
		et_yhkpz.setOnClickListener(onClickListener);
		et_yhkggzm.setOnClickListener(onClickListener);
		tv_yykmb.setOnClickListener(onClickListener);

		et_yhkh.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					String yhkh = et_yhkh.getText().toString();
					String yhkh_new = "";
					if (!"".equals(yhkh)) {
						yhkh = yhkh.replace(" ", "");
						if (IDCard.isNumeric(yhkh)) {
							for (int i = 0; i < yhkh.length(); i++) {
								if (i % 4 == 0) {
									yhkh_new += " ";
								}
								yhkh_new += yhkh.charAt(i);
							}
							yhkh_new = yhkh_new.substring(1, yhkh_new.length());
							et_yhkh.setText(yhkh_new);
						} else {
							et_yhkh.setText("");
							Toast.makeText(getApplicationContext(), "银行卡号不正确",
									1).show();
						}

					}
				}

			}
		});
	}

	@Override
	protected void getWebService(String s) {
		if (s.equals("getyk")) {
			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SJ_KHYHMC", "", "uf_json_getdata", this);
				JSONArray jsonArray = jsonObject.getJSONArray("tableA");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject obj = jsonArray.getJSONObject(i);
					Map<String, String> map = new HashMap<String, String>();
					map.put("id", obj.getString("zbh"));
					map.put("name", obj.getString("yhmc"));
					list_data.add(map);
					list.add(obj.getString("yhmc"));
				}
				Message msg = new Message();
				msg.what = 1;//
				handler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = -1;//
				handler.sendMessage(msg);
			}
		}

		if (s.equals("yhk_back")) {
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
				Message localMessage1 = new Message();
				localMessage1.what = 0;// 网络连接出错，你检查你的网络设置
				handler.sendMessage(localMessage1);
			}
		}

	}

	private void submitData(final String khm, final String yhkh,
			final String yhmc) {
		showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {
			@Override
			public void run() {
				try {
					String sql = "update CCGL_YGB set yhk = '" + yhkh
							+ "',khyh = '" + yhmc + "',kzzd3 = '" + khm
							+ "' where pym = '"
							+ DataCache.getinition().getUserId() + "'";
					if (type == 1) {
						sql = "update CCGL_YGB set xgkh = '" + yhkh
								+ "',xgkhyh = '" + yhmc + "',xgkhr = '" + khm
								+ "',ykkzt = '1',xgyhksj=sysdate where pym = '"
								+ DataCache.getinition().getUserId() + "'";
					}
					JSONObject object = callWebserviceImp.getWebServerInfo(
							"c#_PAD_ESP_ZC", sql, "0000", "1",
							"uf_json_setdata2", getApplicationContext());
					flag = object.getString("flag");
					if (Integer.parseInt(flag) > 0) {
						Message msg = new Message();
						msg.what = 3;// 完成
						handler.sendMessage(msg);
					} else {
						Message msg = new Message();
						msg.what = 0;//
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Message msg = new Message();
					msg.what = 0;//
					handler.sendMessage(msg);
				}

			}
		});

	}

	private void returnData() {
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				try {
					String sql = "update CCGL_YGB set yhk = '',khyh = '',kzzd1 = '' where pym = '"
							+ DataCache.getinition().getUserId() + "'";
					JSONObject object = callWebserviceImp.getWebServerInfo(
							"c#_PAD_ESP_ZC", sql, "0000", "1",
							"uf_json_setdata2", getApplicationContext());
					String flag = object.getString("flag");
					if (Integer.parseInt(flag) > 0) {
						Message msg = new Message();
						msg.what = 6;// 完成
						handler.sendMessage(msg);
					} else {
						Message msg = new Message();
						msg.what = 6;//
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Message msg = new Message();
					msg.what = 6;//
					handler.sendMessage(msg);
				}

			}
		});

	}

	private void addYhk() {
		String khm = et_khm.getText().toString();
		String yhkh = et_yhkh.getText().toString();
		String yh = spinner_yh.getSelectedItem().toString();
		String fh = et_fh.getText().toString();
		String zh = et_zh.getText().toString();
		String flc = et_flc.getText().toString();

		if ("".equals(khm)) {
			Toast.makeText(getApplicationContext(), "开户名不能为空", 1).show();
			return;
		}
		if ("".equals(yhkh)) {
			Toast.makeText(getApplicationContext(), "银行卡号不能为空", 1).show();
			return;
		}
		if ("".equals(fh)) {
			Toast.makeText(getApplicationContext(), "分行不能为空", 1).show();
			return;
		}
		if ("".equals(zh)) {
			Toast.makeText(getApplicationContext(), "支行不能为空", 1).show();
			return;
		}
		if (yhk_file == null) {
			Toast.makeText(getApplicationContext(), "银行卡扫描件不能为空", 1).show();
			return;
		}
		if (type == 1) {
			if (yhk_file2 == null) {
				Toast.makeText(getApplicationContext(), "银行卡更改证明不能为空", 1)
						.show();
				return;
			}
		}
		String flcmc = "".equals(flc) ? "" : flc + "分理处";
		String yhmc = yh + "银行" + fh + "分行" + zh + "支行" + flcmc;
		submitData(khm, yhkh, yhmc);
	}

	private void camera() {
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
			toastShowMessage("没有储存卡，不能拍照");
			return;
		}
		filename = String.valueOf(System.currentTimeMillis()).trim()
				.substring(4);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
				Environment.getExternalStorageDirectory(), filename + ".jpg")));
		startActivityForResult(intent, 1);

	}

	@SuppressLint("ShowToast")
	@SuppressWarnings("unused")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
			// 读pic
			String filepath = Environment.getExternalStorageDirectory()
					.toString() + "/" + filename + ".jpg";
			try {
				File file = new File(filepath);
				if (file == null) {
					Toast.makeText(getApplicationContext(), "图片不存在！", 1).show();
					return;
				}
				// 压缩图片到100K
				filepath = ImageUtil.compressAndGenImage(ImageUtil.ratio(filepath,getScreenWidth(),getScreenHeight()), 200, "jpg");
				if (photonum == 1) {
					yhk_file = new File(filepath);
					image_yhkpz.setImageBitmap(ImageUtil.getBitmap(filepath));
					image_yhkpz.setVisibility(View.VISIBLE);
					et_yhkpz.setText("重新拍照");
				} else if (photonum == 2) {
					yhk_file2 = new File(filepath);
					image_yhkggzm.setImageBitmap(ImageUtil.getBitmap(filepath));
					image_yhkggzm.setVisibility(View.VISIBLE);
					et_yhkggzm.setText("重新拍照");
				}

			} catch (IOException e) {
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), "压缩图片尺寸失败，请调低像素后重新拍照",
						1).show();
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), "压缩图片大小失败，请调低像素后重新拍照",
						1).show();
			}
		} else {
			Toast.makeText(getApplicationContext(), "拍照失败", 1).show();
		}
	}

	private void upload() {
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				try {
					boolean flag = uploadPic("", readJpeg(yhk_file),
							"uf_json_setdata");
					if (flag) {
						if (type == 1) {
							flag = uploadPic("", readJpeg(yhk_file2),
									"uf_json_setdata");
							if (flag) {
								Message msg = new Message();
								msg.what = 5;
								handler.sendMessage(msg);
							} else {
								Message msg = new Message();
								msg.what = 4;
								handler.sendMessage(msg);
							}
						} else {
							Message msg = new Message();
							msg.what = 5;
							handler.sendMessage(msg);
						}

					} else {
						Message msg = new Message();
						msg.what = 4;
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Message msg = new Message();
					msg.what = 4;
					handler.sendMessage(msg);
				}
			}
		});
	}

	private boolean uploadPic(final String orderNumbers, final byte[] data1,
			final String methed) throws Exception {

		if (data1 != null && orderNumbers != null) {
			JSONObject json = callWebserviceImp.getWebServerInfo2_pic(
					"c#_PAD_ESP_ZCMX", "0001", DataCache.getinition()
							.getUserId() + "_yhk", "0001", data1,
					"uf_json_setdata2_p11", getApplicationContext());
			String flag = json.getString("flag");
			if ("1".equals(flag)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case -1:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P("网络错误,请检查网络连接是否正常！",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								finish();
							}
						});
				break;
			case 0:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P("失败，请检查网络连接是否正常...错误标识：" + flag, null);
				break;
			case 1:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				ArrayAdapter<Map<String, String>> yhk_adapter = new ArrayAdapter<Map<String, String>>(
						getApplicationContext(), R.layout.spinner_item_yhk,
						list);
				yhk_adapter
						.setDropDownViewResource(R.layout.spinner_dropdown_item);
				spinner_yh.setAdapter(yhk_adapter);
				break;
			case 3:
				upload();
				break;
			case 4:
				returnData();
				break;
			case 5:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				if (type == 0) {
					dialogShowMessage_P("提交成功，银行卡信息已完善！",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface face,
										int paramAnonymous2Int) {
									DataCache.getinition().setHasYHK(true);
									setResult(1);
									finish();
								}
							});
				} else {
					dialogShowMessage_P("提交成功，我们的工作人员会在24小时内联系您进行确认！",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface face,
										int paramAnonymous2Int) {
									finish();
								}
							});
				}

				break;
			case 6:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P("提交失败，上传图片失败！", null);
				break;
			}
		}
	};

}
