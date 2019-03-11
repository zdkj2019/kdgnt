package com.nt.activity.kdg;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.activity.main.MainActivity;
import com.nt.cache.DataCache;
import com.nt.cache.ServiceReportCache;
import com.nt.common.Constant;
import com.nt.utils.Config;
import com.nt.utils.ImageUtil;

/**
 * 快递柜-安装条件确认
 * 
 * @author zdkj
 *
 */
public class DhjcKdg extends FrameActivity {

	private Button confirm, cancel;
	private String flag, zbh, type = "1", message;
	private LinearLayout ll_show, ll_show_other;
	private ArrayList<Map<String, String>> data_zp;
	private Map<String, String> map_jcnr;
	private int img_num = 0;
	private TextView tv_curr;
	private String filename;
	private List<File> filelist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_kdg_dhjc);
		initVariable();
		initView();
		initListeners();
		showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				getWebService("query");
			}
		});
		// if("（暂停）".equals(ztzt)){
		// confirm.setOnClickListener(null);
		// dialogShowMessage("该工单处于暂停状态，不能提交！", null, null);
		// }
	}

	@Override
	protected void initVariable() {

		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);

	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());
		ll_show = (LinearLayout) findViewById(R.id.ll_show);
		ll_show_other = (LinearLayout) findViewById(R.id.ll_show_other);
		final Map<String, Object> itemmap = ServiceReportCache.getObjectdata()
				.get(ServiceReportCache.getIndex());

		zbh = itemmap.get("zbh").toString();
		((TextView) findViewById(R.id.tv_1)).setText(zbh);
		((TextView) findViewById(R.id.tv_2)).setText(itemmap.get("sx")
				.toString());
		((TextView) findViewById(R.id.tv_3)).setText(itemmap.get("qy")
				.toString());
		((TextView) findViewById(R.id.tv_4)).setText(itemmap.get("xqmc")
				.toString());
		((TextView) findViewById(R.id.tv_5)).setText(itemmap.get("xxdz")
				.toString());
		((TextView) findViewById(R.id.tv_6)).setText(itemmap.get("gzxx")
				.toString());
		((TextView) findViewById(R.id.tv_7)).setText(itemmap.get("ywlx")
				.toString());
		((TextView) findViewById(R.id.tv_8)).setText(itemmap.get("bz")
				.toString());
		filelist = new ArrayList<File>();

	}

	@Override
	protected void initListeners() {
		//
		OnClickListener backonClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.bt_topback:
					onBackPressed();
					break;
				case R.id.cancel:
					// dialogShowMessage("确认拒绝接单？",new
					// DialogInterface.OnClickListener() {
					// public void onClick(DialogInterface face,int
					// paramAnonymous2Int) {
					// showProgressDialog();
					// Config.getExecutorService().execute(new Runnable() {
					//
					// @Override
					// public void run() {
					// type = "2";
					// getWebService("submit");
					// }
					// });
					// }
					// } ,null);
					onBackPressed();
					break;
				case R.id.confirm:
					for (int i = 0; i < ll_show.getChildCount(); i++) {
						LinearLayout ll = (LinearLayout) ll_show.getChildAt(i);
						if (ll.getChildAt(1) instanceof LinearLayout) {
							ll = (LinearLayout) ll.getChildAt(1);
							if (ll.getChildAt(0) instanceof RadioGroup) {
								RadioGroup rg = (RadioGroup) ll.getChildAt(0);
								if (rg.getCheckedRadioButtonId() == -1) {
									dialogShowMessage_P("各项信息不能为空，请选择", null);
									return;
								}
							}
						}
					}

					for (int i = 0; i < ll_show_other.getChildCount(); i++) {
						LinearLayout ll = (LinearLayout) ll_show_other.getChildAt(i);
						if (ll.getChildAt(1) instanceof LinearLayout) {
							ll = (LinearLayout) ll.getChildAt(1);
							if (ll.getChildAt(0) instanceof RadioGroup) {
								RadioGroup rg = (RadioGroup) ll.getChildAt(0);
								if (rg.getCheckedRadioButtonId() == -1) {
									dialogShowMessage_P("各项信息不能为空，请选择", null);
									return;
								}
							}
						}
					}
					showProgressDialog();
					Config.getExecutorService().execute(new Runnable() {

						@Override
						public void run() {
							upload();
						}
					});
					break;
				default:
					break;
				}

			}
		};

		topBack.setOnClickListener(backonClickListener);
		cancel.setOnClickListener(backonClickListener);
		confirm.setOnClickListener(backonClickListener);
	}


	@SuppressLint("ResourceAsColor")
	private void loadXX() {
		View view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
		RadioButton rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		RadioButton rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("达标");
		rb_2.setText("不达标");
		tv_name.setText("电力");
		if ("1".equals(map_jcnr.get("azzb_dl").toString())) {
			rb_1.setChecked(true);
		} else {
			rb_2.setChecked(true);
		}
		ll_show.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("达标");
		rb_2.setText("不达标");
		tv_name.setText("网络");
		if ("1".equals(map_jcnr.get("azzb_wl").toString())) {
			rb_1.setChecked(true);
		} else {
			rb_2.setChecked(true);
		}
		ll_show.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("达标");
		rb_2.setText("不达标");
		tv_name.setText("物业批准");
		if ("1".equals(map_jcnr.get("azzb_wypz").toString())) {
			rb_1.setChecked(true);
		} else {
			rb_2.setChecked(true);
		}
		ll_show.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("达标");
		rb_2.setText("不达标");
		tv_name.setText("基础设置");
		if ("1".equals(map_jcnr.get("azzb_jcsz").toString())) {
			rb_1.setChecked(true);
		} else {
			rb_2.setChecked(true);
		}
		ll_show.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("达标");
		rb_2.setText("不达标");
		tv_name.setText("环境测评");
		if ("1".equals(map_jcnr.get("azzb_hjcp").toString())) {
			rb_1.setChecked(true);
		} else {
			rb_2.setChecked(true);
		}
		ll_show.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_text, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		EditText et_val = (EditText) view.findViewById(R.id.et_val);
		et_val.setTag("安装准备说明");
		et_val.setHint("安装准备说明");
		et_val.setHintTextColor(R.color.gray);
		et_val.setText(map_jcnr.get("azzb_sm").toString());
		tv_name.setText("安装准备说明");
		ll_show.addView(view);
	}

	@Override
	protected void getWebService(String s) {
		if (s.equals("query")) {// 提交
			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SHGL_KDG_DHJC_AZD", zbh + "*" + zbh + "*" + zbh,
						"uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				data_zp = new ArrayList<Map<String, String>>();
				map_jcnr = new HashMap<String, String>();
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, String> item = new HashMap<String, String>();
						item.put("tzlmc", temp.getString("tzlmc"));
						item.put("kzzf1", temp.getString("kzzf1"));
						item.put("kzsz1", temp.getString("kzsz1"));
						item.put("str", temp.getString("str"));
						item.put("tzz", temp.getString("tzz"));
						item.put("mxh", temp.getString("mxh"));
						data_zp.add(item);
					}
				}

				jsonObject = callWebserviceImp
						.getWebServerInfo("_PAD_SHGL_KDG_AZ_DHJC_A", zbh,
								"uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						map_jcnr.put("azzb_dl", temp.getString("azzb_dl"));
						map_jcnr.put("azzb_wl", temp.getString("azzb_wl"));
						map_jcnr.put("azzb_wypz", temp.getString("azzb_wypz"));
						map_jcnr.put("azzb_jcsz", temp.getString("azzb_jcsz"));
						map_jcnr.put("azzb_hjcp", temp.getString("azzb_hjcp"));
						map_jcnr.put("azzb_sm", temp.getString("azzb_sm"));
					}
				}
				Message msg = new Message();
				msg.what = Constant.NUM_6;
				handler.sendMessage(msg);

			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;
				handler.sendMessage(msg);
			}
		}

		if (s.equals("submit")) {// 提交
			try {
				String cs = "";
				for (int i = 0; i < ll_show.getChildCount(); i++) {
					LinearLayout ll = (LinearLayout) ll_show.getChildAt(i);
					if (ll.getChildAt(1) instanceof LinearLayout) {
						ll = (LinearLayout) ll.getChildAt(1);
						if (ll.getChildAt(0) instanceof RadioGroup) {
							RadioGroup rg = (RadioGroup) ll.getChildAt(0);
							RadioButton rb = (RadioButton) rg.findViewById(rg
									.getCheckedRadioButtonId());
							if (rg.getCheckedRadioButtonId() == R.id.rb_1) {
								cs += "1";
							} else {
								cs += "0";
							}
							cs += "*PAM*";
						}else if (ll.getChildAt(0) instanceof EditText) {
							EditText et = (EditText) ll.getChildAt(0);
							cs += et.getText().toString();
							cs += "*PAM*";
						}
					} 

				}
				for (int i = 0; i < ll_show_other.getChildCount(); i++) {
					LinearLayout ll = (LinearLayout) ll_show_other
							.getChildAt(i);
					if (ll.getChildAt(1) instanceof LinearLayout) {
						ll = (LinearLayout) ll.getChildAt(1);
						if (ll.getChildAt(0) instanceof RadioGroup) {
							RadioGroup rg = (RadioGroup) ll.getChildAt(0);
							RadioButton rb = (RadioButton) rg.findViewById(rg
									.getCheckedRadioButtonId());
							cs += rb.getText().toString();
							cs += "*PAM*";
						}else if (ll.getChildAt(0) instanceof EditText) {
							EditText et = (EditText) ll.getChildAt(0);
							cs += et.getText().toString();
							cs += "*PAM*";
						}
					} 

				}
				if (!"".equals(cs)) {
					cs = cs.substring(0, cs.length() - 5);
				}

				String typeStr = "dhjc";
				String str = zbh + "*PAM*" + DataCache.getinition().getUserId();
				str += "*PAM*";
				str += cs;
				message = "提交成功";
				JSONObject json = this.callWebserviceImp.getWebServerInfo(
						"c#_PAD_KDG_ALL", str, typeStr, typeStr,
						"uf_json_setdata2", this);
				flag = json.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					Message msg = new Message();
					msg.what = Constant.SUCCESS;
					handler.sendMessage(msg);
				} else {
					flag = json.getString("msg");
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
	
	private void camera() {
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
			toastShowMessage("没有储存卡，不能拍照");
			return;
		}
		filename = String.valueOf(System.currentTimeMillis()).trim().substring(4);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
				Environment.getExternalStorageDirectory(), filename + ".jpg")));
		startActivityForResult(intent, 1);

	}

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
				loadImg(filepath);
			} catch (IOException e) {
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), "压缩图片尺寸失败，请调低像素后重新拍照",
						1).show();
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), "压缩图片大小失败，请调低像素后重新拍照",
						1).show();
			}

		}
	}

	private void upload() {
		if (img_num == 0) {
			Message msg = new Message();
			msg.what = 12;
			handler.sendMessage(msg);
			return;
		}
		if (filelist.size() != img_num) {
			Message msg = new Message();
			msg.what = 14;
			handler.sendMessage(msg);
			return;
		}
		try {
			boolean flag = true;
			for (int i = 0; i < filelist.size(); i++) {
				if (flag) {
					flag = uploadPic(i + 1 + "", readJpeg(filelist.get(i)),
							"uf_json_setdata");
				} else {
					flag = false;
					break;
				}
			}
			if (flag) {
				Message msg = new Message();
				msg.what = 12;
				handler.sendMessage(msg);
			} else {
				Message msg = new Message();
				msg.what = 13;
				handler.sendMessage(msg);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Message msg = new Message();
			msg.what = 13;
			handler.sendMessage(msg);
		}

	}

	private boolean uploadPic(final String orderNumbers, final byte[] data1,
			final String methed) throws Exception {

		if (data1 != null && orderNumbers != null) {
			JSONObject json = callWebserviceImp.getWebServerInfo2_pic(
					"c#_PAD_KDG_AZ_DHJCZP", orderNumbers, zbh + "*1", "0001",
					data1, "uf_json_setdata2_p11", getApplicationContext());
			String flag = json.getString("flag");
			if ("1".equals(flag)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	private void loadImg(String filepath) {
		try {
			File file = new File(filepath);
			filepath = ImageUtil.compressAndGenImage(ImageUtil.ratio(filepath,getScreenWidth(),getScreenHeight()), 200, "jpg");
			int num = Integer.parseInt(tv_curr.getTag().toString());
			View parent = (View) tv_curr.getParent();
			ImageView image_1 = (ImageView) parent.findViewById(R.id.image_1);
			image_1.setImageBitmap(ImageUtil.getBitmap(filepath));
			image_1.setVisibility(View.VISIBLE);
			if ("重新拍照".equals(tv_curr.getText().toString())) {
				filelist.remove(num);
			}
			tv_curr.setText("重新拍照");
			filelist.add(num, file);
		} catch (Exception e) {

		}

	}

	@SuppressLint("ResourceAsColor")
	protected void LoadSbxx(ArrayList<Map<String, String>> data, LinearLayout ll) {
		if (data.size() == 0) {
			ll_show_other.setVisibility(View.GONE);
			findViewById(R.id.ll_show_other_title).setVisibility(View.GONE);
			return;
		}
		try {
			img_num = 0;
			for (int i = 0; i < data.size(); i++) {
				Map<String, String> map = data.get(i);
				String title = map.get("tzlmc");
				String type = map.get("kzzf1");
				String content = map.get("str");
				String tzz = map.get("tzz");
				View view = null;
				if ("2".equals(type)) {// 输入
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.include_xj_text, null);
					TextView tv_name = (TextView) view
							.findViewById(R.id.tv_name);
					EditText et_val = (EditText) view.findViewById(R.id.et_val);
					et_val.setText(tzz);
					tv_name.setText(title);
				} else if ("1".equals(type)) {// 选择
					String[] contents = content.split(",");
					if (contents.length == 2) {
						view = LayoutInflater.from(getApplicationContext())
								.inflate(R.layout.include_xj_aqfx, null);
						TextView tv_name = (TextView) view
								.findViewById(R.id.tv_name);
						RadioButton rb_1 = (RadioButton) view
								.findViewById(R.id.rb_1);
						RadioButton rb_2 = (RadioButton) view
								.findViewById(R.id.rb_2);
						rb_1.setText(contents[0]);
						rb_2.setText(contents[1]);
						if (tzz.equals(contents[0])) {
							rb_1.setChecked(true);
						} else if (tzz.equals(contents[1])){
							rb_2.setChecked(true);
						}
						tv_name.setText(title);
					} else if (contents.length == 3) {
						view = LayoutInflater.from(getApplicationContext())
								.inflate(R.layout.include_xj_type_2, null);
						TextView tv_name = (TextView) view
								.findViewById(R.id.tv_name);
						RadioButton rb_1 = (RadioButton) view
								.findViewById(R.id.rb_1);
						RadioButton rb_2 = (RadioButton) view
								.findViewById(R.id.rb_2);
						RadioButton rb_3 = (RadioButton) view
								.findViewById(R.id.rb_3);
						rb_1.setText(contents[0]);
						rb_2.setText(contents[1]);
						rb_3.setText(contents[2]);
						if (tzz.equals(contents[0])) {
							rb_1.setChecked(true);
						} else if (tzz.equals(contents[1])) {
							rb_2.setChecked(true);
						} else if (tzz.equals(contents[2])){
							rb_3.setChecked(true);
						}
						tv_name.setText(title);
					}
				} else if ("3".equals(type)) {// 图片
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.include_xj_pz, null);
					TextView tv_name = (TextView) view
							.findViewById(R.id.tv_name);
					tv_name.setText(title);
					TextView tv_1 = (TextView) view.findViewById(R.id.tv_1);
					tv_1.setTag(map.get("mxh"));
					tv_1.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							tv_curr = (TextView) v;
							camera();
						}
					});
				} else if ("4".equals(type)) { // 日期
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.include_xj_text, null);
					TextView tv_name = (TextView) view
							.findViewById(R.id.tv_name);
					final EditText et_val = (EditText) view
							.findViewById(R.id.et_val);
					et_val.setFocusable(false);
					et_val.setText(tzz);
					et_val.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							dateDialog(et_val);
						}
					});
					tv_name.setText(title);
				} else if ("5".equals(type)) { // 数值
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.include_xj_text, null);
					TextView tv_name = (TextView) view
							.findViewById(R.id.tv_name);
					EditText et_val = (EditText) view.findViewById(R.id.et_val);
					et_val.setInputType(InputType.TYPE_CLASS_NUMBER);
					et_val.setText(tzz);
					tv_name.setText(title);
					
				}
				ll.addView(view);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.FAIL:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P("失败，请检查后重试...错误标识：" + flag, null);
				break;
			case Constant.SUCCESS:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P(message,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								Intent intent  = getIntent();
								setResult(-1, intent);
								finish();
							}
						});
				break;
			case Constant.NETWORK_ERROR:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P(Constant.NETWORK_ERROR_STR, null);
				break;
			case Constant.NUM_6:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				loadXX();
				LoadSbxx(data_zp, ll_show_other);
				break;
			case 12:
				Config.getExecutorService().execute(new Runnable() {

					@Override
					public void run() {
						getWebService("submit");
					}
				});
				break;
			case 13:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P("提交失败,上传图片失败！", null);
				break;
			case 14:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P("请拍照！", null);
				break;
			}
		}
	};
//
//	@Override
//	public void onBackPressed() {
//		Intent intent = new Intent(this, MainActivity.class);
//		// intent.putExtra("currType", 2);
//		startActivity(intent);
//		finish();
//	}

}