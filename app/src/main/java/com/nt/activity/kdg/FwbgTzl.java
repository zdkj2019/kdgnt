package com.nt.activity.kdg;

import java.io.File;
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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.activity.esp.AddPartsList;
import com.nt.activity.util.BaiduMapActivity;
import com.nt.cache.DataCache;
import com.nt.cache.ServiceReportCache;
import com.nt.common.Constant;
import com.nt.utils.Config;
import com.nt.utils.ImageUtil;

/**
 * 快递柜-服务报告 特征量
 * 
 * @author zdkj
 *
 */
public class FwbgTzl extends FrameActivity {

	private Button confirm, cancel;
	private String flag, zbh, msgStr, djzt, frontStr, backStr, sfxg;
	private ArrayList<Map<String, String>> data_zp;
	private List<String> list_type;// 安装内容 类型
	private Map<String, ArrayList<String>> filemap;
	private LinearLayout ll_show;
	private TextView tv_curr, tv_dlmc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_kdg_fwbgtzl);
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
		ll_show = (LinearLayout) findViewById(R.id.ll_show);
		tv_dlmc = (TextView) findViewById(R.id.tv_dlmc);
	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());
		Intent intent = getIntent();
		zbh = intent.getStringExtra("zbh");
		djzt = intent.getStringExtra("djzt");
		sfxg = intent.getStringExtra("sfxg");
		frontStr = intent.getStringExtra("frontStr");
		backStr = intent.getStringExtra("backStr");
		data_zp = (ArrayList<Map<String, String>>) intent
				.getSerializableExtra("data");
		list_type = new ArrayList<String>();
		for (int i = 0; i < data_zp.size(); i++) {
			Map<String, String> map = data_zp.get(i);
			boolean isadd = false;
			for (int j = 0; j < list_type.size(); j++) {
				if (map.get("dlbm").equals(list_type.get(j))) {
					isadd = true;
				}
			}
			if (!isadd) {
				list_type.add(map.get("dlbm"));
			}
		}
		loadSbxx(data_zp, ll_show);
	}

	@Override
	protected void initListeners() {
		//
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
				default:
					break;
				}

			}
		};

		topBack.setOnClickListener(onClickListener);
		cancel.setOnClickListener(onClickListener);
		confirm.setOnClickListener(onClickListener);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
			ArrayList<String> list = data.getStringArrayListExtra("imglist");
			if (list != null) {
				loadImg(list);
			}
		}

	}

	@Override
	protected void getWebService(String s) {
		if (s.equals("submit")) {
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
							if (rb == null) {
								cs += "#@##@#0";
							} else {
								String che = rb.getId() == R.id.rb_1 ? "1"
										: "2";
								cs += rb.getText().toString() + "#@#" + che
										+ "#@#" + rb.getTag().toString();
							}
							cs += "#^#";
						} else if (ll.getChildAt(0) instanceof EditText) {
							EditText et = (EditText) ll.getChildAt(0);
							CheckBox cb = (CheckBox) ll.getChildAt(1);
							String che = cb.isChecked() ? "1" : "2";
							cs += et.getText().toString() + "#@#" + che + "#@#"
									+ et.getTag().toString();
							cs += "#^#";
						}
					}

				}
				if (!"".equals(cs)) {
					cs = cs.substring(0, cs.length() - 3);
				}

				String str = "";
				String typeStr = "";
				if (list_type.size() > 0) {
					str = zbh + "*PAM*" + cs;
					typeStr = "az_fwbg_xy";
				} else {
					str = frontStr + cs + backStr;
					if ("1".equals(sfxg)) {
						typeStr = "az_fwbg_xg";
					} else {
						typeStr = "fwbg_az";
					}

				}

				JSONObject json = this.callWebserviceImp
						.getWebServerInfo("c#_PAD_KDG_ALL", str, DataCache
								.getinition().getUserId(), typeStr,
								"uf_json_setdata2", this);
				flag = json.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					upload();
					// Message msg = new Message();
					// msg.what = Constant.SUCCESS;
					// handler.sendMessage(msg);
				} else {
					msgStr = json.getString("msg");
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

	private void upload() {
		try {
			boolean flag = true;
			List<Map<String, String>> filelist = new ArrayList<Map<String, String>>();
			for (String mxh : filemap.keySet()) {
				List<String> filepathlist = filemap.get(mxh);
				for (int j = 0; j < filepathlist.size(); j++) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("mxh", mxh);
					map.put("num", j + "");
					map.put("filepath", filepathlist.get(j));
					filelist.add(map);
				}
			}
			int filenum = filelist.size();
			for (int i = 0; i < filenum; i++) {
				Map<String, String> map = filelist.get(i);
				if (flag) {
					String mxh = map.get("mxh");
					String filepath = map.get("filepath");
					String num = map.get("num");
					filepath = filepath.substring(7, filepath.length());
					// 压缩图片到100K
					filepath = ImageUtil.compressAndGenImage(ImageUtil.ratio(
							filepath, getScreenWidth(), getScreenHeight()),
							200, "jpg");
					File file = new File(filepath);
					// toastShowMessage("开始上传第" + (i + 1) + "/" + filenum +
					// "张图片");
					flag = uploadPic(num, mxh, readJpeg(file),
							"uf_json_setdata", zbh, "c#_PAD_KDGAZ_FWBG_ZPXG");
					file.delete();
				} else {
					flag = false;
					break;
				}
			}
			if (flag) {
				Message msg = new Message();
				msg.what = Constant.NUM_12;
				handler.sendMessage(msg);
			} else {
				Message msg = new Message();
				msg.what = Constant.NUM_13;
				handler.sendMessage(msg);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Message msg = new Message();
			msg.what = Constant.NUM_13;
			handler.sendMessage(msg);
		}

	}

	@SuppressLint({ "ResourceAsColor", "InflateParams" })
	protected void loadSbxx(ArrayList<Map<String, String>> data, LinearLayout ll) {
		String errormsg = "";
		try {
			filemap = new HashMap<String, ArrayList<String>>();
			ll.removeAllViews();
			String dlbm = list_type.get(0);
			String dlmc = "";
			for (int i = 0; i < data.size(); i++) {
				Map<String, String> map = data.get(i);
				dlmc = map.get("dlmc");
				if (dlbm.equals(map.get("dlbm"))) {
					String title = map.get("tzlmc");
					errormsg = title;
					String type = map.get("kzzf1");
					String content = map.get("str");
					String tzz = map.get("tzz") == null ? "" : map.get("tzz")
							.toString();
					String kzzf4 = map.get("kzzf4");
					View view = null;
					if ("2".equals(type)) {// 输入
						view = LayoutInflater.from(getApplicationContext())
								.inflate(R.layout.include_xj_text, null);
						TextView tv_name = (TextView) view
								.findViewById(R.id.tv_name);
						EditText et_val = (EditText) view
								.findViewById(R.id.et_val);
						et_val.setText(tzz);
						et_val.setTag(map.get("mxh"));
						et_val.setHint(title);
						et_val.setHintTextColor(R.color.gray);
						tv_name.setText(title);
						// CheckBox cb = (CheckBox)
						// view.findViewById(R.id.cb_0);
						// cb.setVisibility(View.VISIBLE);
						// if ("1".equals(kzzf4)) {
						// cb.setChecked(true);
						// }
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
							rb_1.setTag(map.get("mxh"));
							rb_2.setTag(map.get("mxh"));
							if (tzz.equals(contents[0])) {
								rb_1.setChecked(true);
							} else if (tzz.equals(contents[1])) {
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
							rb_1.setTag(map.get("mxh"));
							rb_2.setTag(map.get("mxh"));
							rb_3.setTag(map.get("mxh"));
							if (tzz.equals(contents[0])) {
								rb_1.setChecked(true);
							} else if (tzz.equals(contents[1])) {
								rb_2.setChecked(true);
							} else if (tzz.equals(contents[2])) {
								rb_3.setChecked(true);
							}
							tv_name.setText(title);

						}
						// CheckBox cb = (CheckBox)
						// view.findViewById(R.id.cb_0);
						// cb.setVisibility(View.VISIBLE);
						// if ("1".equals(kzzf4)) {
						// cb.setChecked(true);
						// }
					} else if ("3".equals(type)) {// 图片
						view = LayoutInflater.from(getApplicationContext())
								.inflate(R.layout.include_xj_pz, null);
						TextView tv_name = (TextView) view
								.findViewById(R.id.tv_name);
						tv_name.setText(title);
						TextView tv_1 = (TextView) view.findViewById(R.id.tv_1);
						final String mxh = map.get("mxh");
						tv_1.setTag(mxh);
						tv_1.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								tv_curr = (TextView) v;
								ArrayList<String> list = filemap.get(mxh);
								camera(1, list);
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
						et_val.setTag(map.get("mxh"));
						et_val.setText(tzz);
						et_val.setHint(title);
						et_val.setHintTextColor(R.color.gray);
						et_val.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								dateDialog(et_val);
							}
						});
						tv_name.setText(title);
						// CheckBox cb = (CheckBox)
						// view.findViewById(R.id.cb_0);
						// cb.setVisibility(View.VISIBLE);
						// if ("1".equals(kzzf4)) {
						// cb.setChecked(true);
						// }
					} else if ("5".equals(type)) { // 数值
						view = LayoutInflater.from(getApplicationContext())
								.inflate(R.layout.include_xj_text, null);
						TextView tv_name = (TextView) view
								.findViewById(R.id.tv_name);
						EditText et_val = (EditText) view
								.findViewById(R.id.et_val);
						et_val.setInputType(InputType.TYPE_CLASS_NUMBER);
						et_val.setTag(map.get("mxh"));
						et_val.setText(tzz);
						et_val.setHint(title);
						et_val.setHintTextColor(R.color.gray);
						tv_name.setText(title);
						// CheckBox cb = (CheckBox)
						// view.findViewById(R.id.cb_0);
						// cb.setVisibility(View.VISIBLE);
						// if ("1".equals(kzzf4)) {
						// cb.setChecked(true);
						// }
					}

					ll.addView(view);
				}
			}
			list_type.remove(0);
			tv_dlmc.setText(dlmc);
			if (list_type.size() > 0) {
				confirm.setText("下一步");
			} else {
				confirm.setText("确定");
			}
		} catch (Exception e) {
			dialogShowMessage_P("数据错误:" + errormsg + ",选项数据类型不匹配,请联系管理员修改",
					null);
			e.printStackTrace();
		}
	}

	private void loadImg(final ArrayList<String> list) {
		try {
			String mxh = tv_curr.getTag().toString();
			if (list.size() > 0) {
				tv_curr.setText("继续选择");
				tv_curr.setBackgroundResource(R.drawable.btn_normal_yellow);
			} else {
				tv_curr.setText("选择图片");
				tv_curr.setBackgroundResource(R.drawable.btn_normal);
			}
			filemap.put(mxh, list);
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
				dialogShowMessage_P(msgStr, null);
				break;
			case Constant.SUCCESS:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P("提交成功,请上传巡检照片",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								Intent intent = new Intent(
										getApplicationContext(),
										UploadPhotoActivity.class);
								intent.putExtra("zbh", zbh);
								startActivity(intent);
							}
						});
				break;
			case Constant.NETWORK_ERROR:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P(Constant.NETWORK_ERROR_STR, null);
				break;
			case Constant.NUM_12:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				if (list_type.size() == 0) {
					dialogShowMessage_P("提交成功,请上传巡检照片",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface face,
										int paramAnonymous2Int) {
									Intent intent = new Intent(
											getApplicationContext(),
											UploadPhotoActivity.class);
									intent.putExtra("zbh", zbh);
									startActivity(intent);
									finish();
								}
							});
				} else {
					loadSbxx(data_zp, ll_show);
				}
				break;
			case Constant.NUM_13:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P("服务报告提交成功,图片上传失败！",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								onBackPressed();
							}
						});
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

}
