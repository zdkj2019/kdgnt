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
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.hardware.camera2.params.RggbChannelVector;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView.OnEditorActionListener;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.activity.esp.AddPartsList;
import com.nt.activity.main.MainActivity;
import com.nt.cache.DataCache;
import com.nt.cache.ServiceReportCache;
import com.nt.common.Constant;
import com.nt.utils.Config;
import com.nt.utils.ImageUtil;
import com.nt.zxing.CaptureActivity;

/**
 * 巡检-服务报告
 * 
 * @author zdkj
 *
 */
public class FwbgXj extends FrameActivity {

	private Button confirm, cancel;
	private String flag, zbh, message, type;
	private TextView tv_curr;
	private ArrayList<Map<String, String>> data_zp;
	private LinearLayout ll_show;
	private Map<String, ArrayList<String>> filemap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_xj_fwbg);
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
		confirm.setText("保存");
		cancel.setText("确认完成");

	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());
		ll_show = (LinearLayout) findViewById(R.id.ll_show);

		filemap = new HashMap<String, ArrayList<String>>();

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
		((TextView) findViewById(R.id.tv_5)).setText(itemmap.get("bzsj")
				.toString());
		((TextView) findViewById(R.id.tv_6)).setText(itemmap.get("gzxx")
				.toString());
		((TextView) findViewById(R.id.tv_7)).setText(itemmap.get("zdh")
				.toString());
		((TextView) findViewById(R.id.tv_8)).setText(itemmap.get("xxdz")
				.toString());
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
					type = "2";
					for (int i = 0; i < ll_show.getChildCount(); i++) {
						LinearLayout ll = (LinearLayout) ll_show.getChildAt(i);
						Map<String, String> map = data_zp.get(i);

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
							getWebService("submit");
						}
					});
					break;
				case R.id.confirm:
					type = "1";
					// for (int i = 0; i < ll_show.getChildCount(); i++) {
					// LinearLayout ll = (LinearLayout) ll_show.getChildAt(i);
					// Map<String, String> map = data_zp.get(i);
					// String kzzf3 = map.get("kzzf3");
					//
					// if (ll.getChildAt(1) instanceof EditText) {
					// if ("1".equals(kzzf3)) { // 1表示必填
					// EditText et = (EditText) ll.getChildAt(1);
					// String tag = et.getTag().toString();
					// if (!isNotNull(et)) {
					// dialogShowMessage_P(tag + "不能为空，请录入", null);
					// return;
					// }
					// }
					// } else if (ll.getChildAt(1) instanceof LinearLayout) {
					// ll = (LinearLayout) ll.getChildAt(1);
					// if (ll.getChildAt(0) instanceof RadioGroup) {
					// RadioGroup rg = (RadioGroup) ll.getChildAt(0);
					// if (rg.getCheckedRadioButtonId() == -1) {
					// dialogShowMessage_P("各项信息不能为空，请选择", null);
					// return;
					// }
					// }
					// }
					//
					// }
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

		topBack.setOnClickListener(backonClickListener);
		cancel.setOnClickListener(backonClickListener);
		confirm.setOnClickListener(backonClickListener);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
			ArrayList<String> list = data.getStringArrayListExtra("imglist");
			loadImg(list);
		}
	}

	@Override
	protected void getWebService(String s) {
		if (s.equals("query")) {// 提交
			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_KDG_XJ_FWBG_MXCX", zbh + "*" + zbh + "*" + zbh,
						"uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				data_zp = new ArrayList<Map<String, String>>();
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, String> item = new HashMap<String, String>();
						item.put("tzlmc", temp.getString("tzlmc"));
						item.put("kzzf1", temp.getString("kzzf1"));
						item.put("kzsz1", temp.getString("kzsz1"));
						item.put("kzzf3", temp.getString("kzzf3"));
						item.put("str", temp.getString("str"));
						item.put("tzz", temp.getString("tzz"));
						item.put("mxh", temp.getString("mxh"));
						data_zp.add(item);
					}

					Message msg = new Message();
					msg.what = Constant.NUM_6;
					handler.sendMessage(msg);

				} else {
					// flag = jsonObject.getString("msg");
					Message msg = new Message();
					msg.what = Constant.NUM_6;// 失败
					handler.sendMessage(msg);
				}
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
							String che = "1";
							cs += et.getText().toString() + "#@#" + che + "#@#"
									+ et.getTag().toString();
							cs += "#^#";
						}
					}

				}
				// 再提交服务报告
				String typeStr = "fwbg";
				if ("2".equals(type)) {
					typeStr = "fwbg_qr";
				}
				String str = zbh + "*PAM*" + DataCache.getinition().getUserId();
				str += "*PAM*";
				str += cs;

				JSONObject json = this.callWebserviceImp.getWebServerInfo(
						"c#_PAD_KDG_XJ_ALL", str, typeStr, typeStr,
						"uf_json_setdata2", this);
				flag = json.getString("flag");

				if (Integer.parseInt(flag) > 0) {
					upload();
					// Message msg = new Message();
					// msg.what = Constant.SUCCESS;
					// handler.sendMessage(msg);
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
					filepath = ImageUtil.compressAndGenImage(ImageUtil.ratio(filepath,getScreenWidth(),getScreenHeight()), 200, "jpg");
					File file = new File(filepath);
					// toastShowMessage("开始上传第" + (i + 1) + "/" + filenum +
					// "张图片");
					flag = uploadPic(num, mxh, readJpeg(file),
							"uf_json_setdata", zbh, "c#_PAD_KDG_XJ_GDCZP");
					file.delete();
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

	@SuppressLint("ResourceAsColor")
	protected void LoadSbxx(ArrayList<Map<String, String>> data, LinearLayout ll) {
		String errormsg = "";
		try {
			for (int i = 0; i < data.size(); i++) {
				Map<String, String> map = data.get(i);
				String title = map.get("tzlmc");
				errormsg = title;
				String type = map.get("kzzf1");
				String tzz = map.get("tzz") == null ? "" : map.get("tzz")
						.toString();
				String content = map.get("str");
				View view = null;
				if ("2".equals(type)) {// 输入
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.include_xj_text, null);
					TextView tv_name = (TextView) view
							.findViewById(R.id.tv_name);
					EditText et_val = (EditText) view.findViewById(R.id.et_val);
					et_val.setText(tzz);
					et_val.setTag(map.get("mxh"));
					et_val.setHint(title);
					et_val.setHintTextColor(R.color.gray);
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
					et_val.setText(tzz);
					et_val.setTag(map.get("mxh"));
					et_val.setHintTextColor(R.color.gray);
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
					et_val.setTag(map.get("mxh"));
					et_val.setHintTextColor(R.color.gray);
					tv_name.setText(title);

				}

				ll.addView(view);
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
				dialogShowMessage_P("失败，请检查后重试...错误标识：" + flag, null);
				break;
			case Constant.SUCCESS:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				message = "提交成功";
				dialogShowMessage_P(message,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								onBackPressed();
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
				LoadSbxx(data_zp, ll_show);
				break;
			case Constant.NUM_7:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				break;
			case Constant.NUM_8:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				break;
			case Constant.NUM_9:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P(message,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
							}
						});
				break;
			case 11:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P("仓位权限错误，请联系管理员", null);
				break;
			case 12:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P("服务报告提交成功",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								Intent intent = getIntent();
								setResult(-1, intent);
								finish();
							}
						});
				break;
			case 13:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P("服务报告提交成功,图片上传失败,请到服务报告修改模块中修改！",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								Intent intent = getIntent();
								setResult(-1, intent);
								finish();
							}
						});
				break;
			case 14:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P("请拍照！", null);
				break;
			case 15:

				break;
			}

		}
	};
	//
	// @Override
	// public void onBackPressed() {
	// Intent intent = new Intent(this, MainActivity.class);
	// intent.putExtra("currType", 1);
	// startActivity(intent);
	// finish();
	// }

}
