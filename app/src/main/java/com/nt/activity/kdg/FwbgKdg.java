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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
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
import com.nt.zxing.CaptureActivity;

/**
 * 快递柜-服务报告
 * 
 * @author zdkj
 *
 */
public class FwbgKdg extends FrameActivity {

	private Button confirm, cancel, btn_sm, btn_sm_ywm;
	private String flag, zbh, msgStr, sbxhbm, sblx, sbxh, sbxlhs, bzr, lxdh,
			djzt, sbid, frontStr, backStr,ywlx2;
	private Spinner spinner_sblx, spinner_sbxh, spinner_smyy, spinner_lxdh;
	private EditText et_smyy, et_gzclgc, et_sbxlhs, et_sbidh;
	private RadioGroup rg_0;
	private ArrayList<Map<String, String>> data_zp, data_sblx, data_sbxh,
			data_smyy, data_lxdh;
	private Map<String, String> data_oldValue;
	private String[] from;
	private int[] to;
	private ArrayList<String> data_sbxlh, data_sbxlh_search;
	private TextView tv_curr, tv_xxdz;
	private LinearLayout ll_show;
	private ImageView iv_telphone;
	private Map<String, ArrayList<String>> filemap;
	private boolean isecsm = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_kdg_fwbg);
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

		title.setText(DataCache.getinition().getTitle());
		ll_show = (LinearLayout) findViewById(R.id.ll_show);
		tv_xxdz = (TextView) findViewById(R.id.tv_ywlx);
		spinner_sblx = (Spinner) findViewById(R.id.spinner_sblx);
		spinner_sbxh = (Spinner) findViewById(R.id.spinner_sbxh);
		spinner_smyy = (Spinner) findViewById(R.id.spinner_smyy);
		spinner_lxdh = (Spinner) findViewById(R.id.spinner_lxdh);
		et_smyy = (EditText) findViewById(R.id.et_smyy);
		et_gzclgc = (EditText) findViewById(R.id.et_gzclgc);
		et_sbxlhs = (EditText) findViewById(R.id.et_sbxlhs);
		et_sbidh = (EditText) findViewById(R.id.et_sbidh);
		rg_0 = (RadioGroup) findViewById(R.id.rg_0);
		iv_telphone = (ImageView) findViewById(R.id.iv_telphone);

		btn_sm = (Button) findViewById(R.id.btn_sm);
		btn_sm_ywm = (Button) findViewById(R.id.btn_sm_ywm);

		from = new String[] { "id", "name" };
		to = new int[] { R.id.bm, R.id.name };
		filemap = new HashMap<String, ArrayList<String>>();
		data_sbxh = new ArrayList<Map<String, String>>();
		final Map<String, Object> itemmap = ServiceReportCache.getObjectdata()
				.get(ServiceReportCache.getIndex());

		zbh = itemmap.get("zbh").toString();
		sblx = itemmap.get("sblx").toString();
		sbxh = itemmap.get("sbxh").toString();
		bzr = itemmap.get("khlxr").toString();
		lxdh = itemmap.get("lxdh").toString();
		djzt = itemmap.get("djzt").toString();
		ywlx2 = itemmap.get("ywlx2").toString();
		((TextView) findViewById(R.id.tv_zbh)).setText(zbh);
		((TextView) findViewById(R.id.tv_sf)).setText(itemmap.get("sf")
				.toString());
		((TextView) findViewById(R.id.tv_axdh)).setText(itemmap.get("sx")
				.toString());
		((TextView) findViewById(R.id.tv_xqmc)).setText(itemmap.get("qy")
				.toString());
		((TextView) findViewById(R.id.tv_xxdz)).setText(itemmap.get("xqmc")
				.toString());
		((TextView) findViewById(R.id.tv_ywlx)).setText("   "
				+ itemmap.get("xxdz").toString());
		((TextView) findViewById(R.id.tv_gzxx)).setText(itemmap.get("ywlx")
				.toString());
		((TextView) findViewById(R.id.tv_hy)).setText(itemmap.get("ywhy")
				.toString());
		((TextView) findViewById(R.id.tv_gxh)).setText(itemmap.get("gxh")
				.toString());
		((TextView) findViewById(R.id.tv_jgh)).setText(itemmap.get("jgh")
				.toString());
		((TextView) findViewById(R.id.tv_azlx2)).setText(itemmap.get("anlx2")
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
					onBackPressed();
					break;
				case R.id.confirm:
					if (rg_0.getCheckedRadioButtonId() == -1) {
						toastShowMessage("请选择是否二次上门！");
					} else {
						if (rg_0.getCheckedRadioButtonId() == R.id.rb_1) {
							isecsm = true;
							if ("".equals(data_smyy.get(
									spinner_smyy.getSelectedItemPosition())
									.get("id"))) {
								toastShowMessage("请选择上门原因！");
								return;
							}
							if (!isNotNull(et_smyy)) {
								toastShowMessage("请录入原因说明！");
								return;
							}
							showProgressDialog();
							Config.getExecutorService().execute(new Runnable() {

								@Override
								public void run() {
									getWebService("submit");
								}
							});
						} else {
							isecsm = false;
							if (!isNotNull(et_sbidh)) {
								toastShowMessage("请录入设备ID号！");
								return;
							}
							if (!isNotNull(et_sbxlhs)) {
								toastShowMessage("请录入设备序列号！");
								return;
							}
							if ("".equals(data_sblx.get(
									spinner_sblx.getSelectedItemPosition())
									.get("id"))) {
								toastShowMessage("请选择设备类型！");
								return;
							}
							if ("".equals(data_sbxh.get(
									spinner_sbxh.getSelectedItemPosition())
									.get("id"))) {
								toastShowMessage("请选择设备型号！");
								return;
							}
//							if (!isNotNull(et_gzclgc)) {
//								toastShowMessage("请录入处理过程！");
//								return;
//							}
							showProgressDialog();
							Config.getExecutorService().execute(new Runnable() {

								@Override
								public void run() {
									getWebService("submit");
								}
							});
						}
					}

					break;
				case R.id.btn_sm:
					Intent intent = new Intent(getApplicationContext(),
							CaptureActivity.class);
					startActivityForResult(intent, 2);
					break;
				case R.id.btn_sm_ywm:
					Intent intent1 = new Intent(getApplicationContext(),
							CaptureActivity.class);
					startActivityForResult(intent1, 4);
					break;
				default:
					break;
				}

			}
		};

		topBack.setOnClickListener(backonClickListener);
		cancel.setOnClickListener(backonClickListener);
		confirm.setOnClickListener(backonClickListener);
		btn_sm.setOnClickListener(backonClickListener);
		btn_sm_ywm.setOnClickListener(backonClickListener);

		iv_telphone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String lxdh = data_lxdh.get(
						spinner_lxdh.getSelectedItemPosition()).get("name");
				if ("".equals(lxdh)) {
					toastShowMessage("请选择联系电话！");
					return;
				}
				Call(lxdh);
			}
		});

		spinner_sblx.setOnItemSelectedListener(new OnItemSelectedListener() {

			@SuppressLint("ResourceAsColor")
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				sbxhbm = data_sblx.get(position).get("id");
				// showProgressDialog();
				Config.getExecutorService().execute(new Runnable() {

					@Override
					public void run() {
						getWebService("getsbxh");
					}
				});
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		findViewById(R.id.iv_baidumap).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						BaiduMapActivity.class);
				intent.putExtra("keyStr", tv_xxdz.getText().toString()
						.trim());
				startActivity(intent);
			}
		});
		
		rg_0.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId==R.id.rb_1){
					confirm.setText("确定");
				}else{
					if (data_zp.size() > 0) {
						confirm.setText("下一步");
					}else{
						confirm.setText("确定");
					}
					spinner_smyy.setSelection(0);
					et_smyy.setText("");
				}
				
			}
		});
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
		if (requestCode == 2 && resultCode == 2 && data != null) {
			// 二维码
			sbid = data.getStringExtra("result").trim();
			et_sbidh.setText(sbid);
		}
		if (requestCode == 4 && resultCode == 2 && data != null) {
			// 条形码
			sbxlhs = data.getStringExtra("result").trim();
			et_sbxlhs.setText(sbxlhs);
		}
		if (requestCode == 5 && resultCode == Activity.RESULT_OK) {
			try {
				data_sbxlh = data.getStringArrayListExtra("data_sbxlh");
				boolean f = true;
				String xlhs = "";
				for (int i = 0; i < data_sbxlh.size(); i++) {
					if ("".equals(data_sbxlh.get(i))) {
						f = false;
						break;
					} else {
						xlhs += data_sbxlh.get(i) + ";";
					}
				}
				if (f) {
					if (xlhs.length() > 15) {
						xlhs = xlhs.substring(0, 14) + "...";
					}
					et_sbxlhs.setText(xlhs);
				} else {
					et_sbxlhs.setText("");
				}

			} catch (Exception e) {
				et_sbxlhs.setText("");
			}

		}
		if (requestCode == 7 && resultCode == Activity.RESULT_OK) {
			onBackPressed();
		}
	}

	@Override
	protected void getWebService(String s) {
		if (s.equals("query")) {// 提交
			try {
				data_sblx = new ArrayList<Map<String, String>>();
				HashMap<String, String> item = new HashMap<String, String>();
				item.put("id", "");
				item.put("name", "");
				data_sblx.add(item);
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_NT_SBLX", "", "uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						item = new HashMap<String, String>();
						item.put("id", temp.getString("ccd"));
						item.put("name", temp.getString("ccdnm"));
						data_sblx.add(item);
					}

					// 获取查询设备序列号
					data_sbxlh_search = new ArrayList<String>();
					jsonObject = callWebserviceImp
							.getWebServerInfo("_PAD_KDG_FWBG_SBXLH", zbh,
									"uf_json_getdata", this);
					flag = jsonObject.getString("flag");
					if (Integer.parseInt(flag) > 0) {
						jsonArray = jsonObject.getJSONArray("tableA");
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject temp = jsonArray.getJSONObject(i);
							data_sbxlh_search.add(temp.getString("sbbm"));
						}
					}

					data_lxdh = new ArrayList<Map<String, String>>();
					item = new HashMap<String, String>();
					item.put("id", bzr);
					item.put("name", lxdh);
					data_lxdh.add(item);
					jsonObject = callWebserviceImp.getWebServerInfo(
							"_PAD_KDG_BZRLXDH", zbh, "uf_json_getdata", this);
					flag = jsonObject.getString("flag");

					if (Integer.parseInt(flag) > 0) {
						jsonArray = jsonObject.getJSONArray("tableA");
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject temp = jsonArray.getJSONObject(i);
							if (!bzr.equals(temp.getString("lxr"))) {
								item = new HashMap<String, String>();
								item.put("id", temp.getString("lxr"));
								item.put("name", temp.getString("lxrdh"));
								data_lxdh.add(item);
							}
						}
					}

					data_smyy = new ArrayList<Map<String, String>>();
					item = new HashMap<String, String>();
					item.put("id", "");
					item.put("name", "");
					data_smyy.add(item);
					// 上门原因
					jsonObject = callWebserviceImp
							.getWebServerInfo("_PAD_KDG_FWBG_ECSMYY", ywlx2,
									"uf_json_getdata", this);
					flag = jsonObject.getString("flag");
					if (Integer.parseInt(flag) > 0) {
						jsonArray = jsonObject.getJSONArray("tableA");
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject temp = jsonArray.getJSONObject(i);
							item = new HashMap<String, String>();
							item.put("id", temp.getString("ccd"));
							item.put("name", temp.getString("ccdnm"));
							data_smyy.add(item);
						}
					} else {
						msgStr = "获取二次上门原因失败";
						Message msg = new Message();
						msg.what = Constant.FAIL;// 失败
						handler.sendMessage(msg);
					}

					jsonObject = callWebserviceImp.getWebServerInfo(
							"_PAD_SHGL_KDG_FWBG_AZD", zbh + "*" + zbh + "*"
									+ zbh, "uf_json_getdata", this);
					flag = jsonObject.getString("flag");
					data_zp = new ArrayList<Map<String, String>>();
					if (Integer.parseInt(flag) > 0) {
						jsonArray = jsonObject.getJSONArray("tableA");
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject temp = jsonArray.getJSONObject(i);
							item = new HashMap<String, String>();
							item.put("tzlmc", temp.getString("tzlmc"));
							item.put("kzzf1", temp.getString("kzzf1"));
							item.put("kzsz1", temp.getString("kzsz1"));
							item.put("str", temp.getString("str"));
							item.put("mxh", temp.getString("mxh"));
							item.put("tzz", temp.getString("tzz"));
							item.put("kzzf4", temp.getString("kzzf4"));
							item.put("dlbm", temp.getString("dlbm"));
							item.put("dlmc", temp.getString("dlmc"));
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
				} else {
					msgStr = "获取设备类型失败";
					Message msg = new Message();
					msg.what = Constant.FAIL;// 失败
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;
				handler.sendMessage(msg);
			}
		}

		if (s.equals("getOldValue")) {
			try {
				data_oldValue = new HashMap<String, String>();
				JSONObject jsonObject = callWebserviceImp
						.getWebServerInfo("_PAD_SHGL_KDG_FWBG_YTFH", zbh,
								"uf_json_getdata", this);
				flag = jsonObject.getString("flag");

				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					JSONObject temp = jsonArray.getJSONObject(0);
					data_oldValue.put("kzzf3", temp.getString("kzzf3"));
					data_oldValue.put("kzzf4", temp.getString("kzzf4"));
					data_oldValue.put("kzzf5", temp.getString("kzzf5"));
					data_oldValue.put("clgc", temp.getString("clgc"));
					data_oldValue.put("ecsmyy", temp.getString("ecsmyy"));
					data_oldValue.put("sblx", temp.getString("sblx"));
					data_oldValue.put("sbxh", temp.getString("sbxh"));
					data_oldValue.put("wxts", temp.getString("wxts"));
					data_oldValue.put("zdh", temp.getString("zdh"));
					data_oldValue.put("rgf", temp.getString("rgf"));
					data_oldValue.put("bjf", temp.getString("bjf"));
					data_oldValue.put("sbewm", temp.getString("sbewm"));
					Message msg = new Message();
					msg.what = Constant.NUM_10;
					handler.sendMessage(msg);
				} else {
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

		if (s.equals("getsbxh")) {
			try {
				data_sbxh = new ArrayList<Map<String, String>>();
				Map<String, String> item = new HashMap<String, String>();
				item.put("id", "");
				item.put("name", "");
				data_sbxh.add(item);
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_KDG_FWBG_SBXH", sbxhbm, "uf_json_getdata", this);
				flag = jsonObject.getString("flag");

				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						item = new HashMap<String, String>();
						item.put("id", temp.getString("xhbm"));
						item.put("name", temp.getString("sbxh"));
						data_sbxh.add(item);
					}

				}

				Message msg = new Message();
				msg.what = Constant.NUM_8;
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
				// String cs = "";
				// for (int i = 0; i < ll_show.getChildCount(); i++) {
				// LinearLayout ll = (LinearLayout) ll_show.getChildAt(i);
				// if (ll.getChildAt(1) instanceof LinearLayout) {
				// ll = (LinearLayout) ll.getChildAt(1);
				// if (ll.getChildAt(0) instanceof RadioGroup) {
				// RadioGroup rg = (RadioGroup) ll.getChildAt(0);
				// RadioButton rb = (RadioButton) rg.findViewById(rg
				// .getCheckedRadioButtonId());
				// if (rb == null) {
				// cs += "#@##@#0";
				// } else {
				// String che = rb.getId() == R.id.rb_1 ? "1"
				// : "2";
				// cs += rb.getText().toString() + "#@#" + che
				// + "#@#" + rb.getTag().toString();
				// }
				// cs += "#^#";
				// } else if (ll.getChildAt(0) instanceof EditText) {
				// EditText et = (EditText) ll.getChildAt(0);
				// CheckBox cb = (CheckBox) ll.getChildAt(1);
				// String che = cb.isChecked() ? "1" : "2";
				// cs += et.getText().toString() + "#@#" + che + "#@#"
				// + et.getTag().toString();
				// cs += "#^#";
				// }
				// }
				//
				// }
				// if (!"".equals(cs)) {
				// cs = cs.substring(0, cs.length() - 3);
				// }
				
				
				// 再提交服务报告
				String str = "";
				String typeStr = "fwbg_az";
				str = zbh + "*PAM*" + DataCache.getinition().getUserId();
				str += "*PAM*";
				str += rg_0.getCheckedRadioButtonId() == R.id.rb_1 ? 1 : 2;
				str += "*PAM*";
				str += data_smyy.get(spinner_smyy.getSelectedItemPosition())
						.get("id");
				str += "*PAM*";
				str += et_smyy.getText().toString();
				str += "*PAM*";

				frontStr = str;
				str = "";

				str += "*PAM*";
				str += et_gzclgc.getText().toString();
				str += "*PAM*";
				str += et_sbidh.getText().toString();
				str += "*PAM*";
				str += et_sbxlhs.getText().toString();
				str += "*PAM*";
				str += data_sblx.get(spinner_sblx.getSelectedItemPosition())
						.get("id");
				str += "*PAM*";
				str += data_sbxh.get(spinner_sbxh.getSelectedItemPosition())
						.get("id");
				str += "*PAM*";
				str += djzt;

				backStr = str;
				if (data_zp.size() == 0 || rg_0.getCheckedRadioButtonId() == R.id.rb_1) {// 没有安装内容或者是二次上门 直接提交
					str = frontStr + "" + backStr;
					JSONObject json = this.callWebserviceImp.getWebServerInfo(
							"c#_PAD_KDG_ALL", str, DataCache.getinition()
									.getUserId(), typeStr, "uf_json_setdata2",
							this);
					flag = json.getString("flag");
					if (Integer.parseInt(flag) > 0) {
						Message msg = new Message();
						msg.what = Constant.SUCCESS;
						handler.sendMessage(msg);
					} else {
						msgStr = json.getString("msg");
						Message msg = new Message();
						msg.what = Constant.FAIL;
						handler.sendMessage(msg);
					}
				} else {
					Message msg = new Message();
					msg.what = Constant.NUM_9;
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
							"uf_json_setdata", zbh, "c#_PAD_KDGAZ_FWBG_ZPXG");
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

	@SuppressLint({ "ResourceAsColor", "InflateParams" })
	protected void LoadSbxx(ArrayList<Map<String, String>> data, LinearLayout ll) {
		String errormsg = "";
		try {
			if (data.size() > 0) {
				confirm.setText("下一步");
			}
			// if (data.size() == 0) {
			// findViewById(R.id.ll_show_title).setVisibility(View.GONE);
			// findViewById(R.id.ll_show).setVisibility(View.GONE);
			// } else {
			// findViewById(R.id.ll_show_title).setVisibility(View.VISIBLE);
			// findViewById(R.id.ll_show).setVisibility(View.VISIBLE);
			// }
			// for (int i = 0; i < data.size(); i++) {
			// Map<String, String> map = data.get(i);
			// String title = map.get("tzlmc");
			// errormsg = title;
			// String type = map.get("kzzf1");
			// String content = map.get("str");
			// String tzz = map.get("tzz") == null ? "" : map.get("tzz")
			// .toString();
			// String kzzf4 = map.get("kzzf4");
			// View view = null;
			// if ("2".equals(type)) {// 输入
			// view = LayoutInflater.from(getApplicationContext())
			// .inflate(R.layout.include_xj_text, null);
			// TextView tv_name = (TextView) view
			// .findViewById(R.id.tv_name);
			// EditText et_val = (EditText) view.findViewById(R.id.et_val);
			// et_val.setText(tzz);
			// et_val.setTag(map.get("mxh"));
			// et_val.setHint(title);
			// et_val.setHintTextColor(R.color.gray);
			// tv_name.setText(title);
			// CheckBox cb = (CheckBox) view.findViewById(R.id.cb_0);
			// cb.setVisibility(View.VISIBLE);
			// if ("1".equals(kzzf4)) {
			// cb.setChecked(true);
			// }
			// } else if ("1".equals(type)) {// 选择
			// String[] contents = content.split(",");
			// if (contents.length == 2) {
			// view = LayoutInflater.from(getApplicationContext())
			// .inflate(R.layout.include_xj_aqfx, null);
			// TextView tv_name = (TextView) view
			// .findViewById(R.id.tv_name);
			// RadioButton rb_1 = (RadioButton) view
			// .findViewById(R.id.rb_1);
			// RadioButton rb_2 = (RadioButton) view
			// .findViewById(R.id.rb_2);
			// rb_1.setText(contents[0]);
			// rb_2.setText(contents[1]);
			// rb_1.setTag(map.get("mxh"));
			// rb_2.setTag(map.get("mxh"));
			// if (tzz.equals(contents[0])) {
			// rb_1.setChecked(true);
			// } else if (tzz.equals(contents[1])) {
			// rb_2.setChecked(true);
			// }
			// tv_name.setText(title);
			//
			// } else if (contents.length == 3) {
			// view = LayoutInflater.from(getApplicationContext())
			// .inflate(R.layout.include_xj_type_2, null);
			// TextView tv_name = (TextView) view
			// .findViewById(R.id.tv_name);
			// RadioButton rb_1 = (RadioButton) view
			// .findViewById(R.id.rb_1);
			// RadioButton rb_2 = (RadioButton) view
			// .findViewById(R.id.rb_2);
			// RadioButton rb_3 = (RadioButton) view
			// .findViewById(R.id.rb_3);
			// rb_1.setText(contents[0]);
			// rb_2.setText(contents[1]);
			// rb_3.setText(contents[2]);
			// rb_1.setTag(map.get("mxh"));
			// rb_2.setTag(map.get("mxh"));
			// rb_3.setTag(map.get("mxh"));
			// if (tzz.equals(contents[0])) {
			// rb_1.setChecked(true);
			// } else if (tzz.equals(contents[1])) {
			// rb_2.setChecked(true);
			// } else if (tzz.equals(contents[2])) {
			// rb_3.setChecked(true);
			// }
			// tv_name.setText(title);
			//
			// }
			// CheckBox cb = (CheckBox) view.findViewById(R.id.cb_0);
			// cb.setVisibility(View.VISIBLE);
			// if ("1".equals(kzzf4)) {
			// cb.setChecked(true);
			// }
			// } else if ("3".equals(type)) {// 图片
			// view = LayoutInflater.from(getApplicationContext())
			// .inflate(R.layout.include_xj_pz, null);
			// TextView tv_name = (TextView) view
			// .findViewById(R.id.tv_name);
			// tv_name.setText(title);
			// TextView tv_1 = (TextView) view.findViewById(R.id.tv_1);
			// final String mxh = map.get("mxh");
			// tv_1.setTag(mxh);
			// tv_1.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// tv_curr = (TextView) v;
			// ArrayList<String> list = filemap.get(mxh);
			// camera(1, list);
			// }
			// });
			//
			// } else if ("4".equals(type)) { // 日期
			// view = LayoutInflater.from(getApplicationContext())
			// .inflate(R.layout.include_xj_text, null);
			// TextView tv_name = (TextView) view
			// .findViewById(R.id.tv_name);
			// final EditText et_val = (EditText) view
			// .findViewById(R.id.et_val);
			// et_val.setFocusable(false);
			// et_val.setTag(map.get("mxh"));
			// et_val.setText(tzz);
			// et_val.setHint(title);
			// et_val.setHintTextColor(R.color.gray);
			// et_val.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// dateDialog(et_val);
			// }
			// });
			// tv_name.setText(title);
			// CheckBox cb = (CheckBox) view.findViewById(R.id.cb_0);
			// cb.setVisibility(View.VISIBLE);
			// if ("1".equals(kzzf4)) {
			// cb.setChecked(true);
			// }
			// } else if ("5".equals(type)) { // 数值
			// view = LayoutInflater.from(getApplicationContext())
			// .inflate(R.layout.include_xj_text, null);
			// TextView tv_name = (TextView) view
			// .findViewById(R.id.tv_name);
			// EditText et_val = (EditText) view.findViewById(R.id.et_val);
			// et_val.setInputType(InputType.TYPE_CLASS_NUMBER);
			// et_val.setTag(map.get("mxh"));
			// et_val.setText(tzz);
			// et_val.setHint(title);
			// et_val.setHintTextColor(R.color.gray);
			// tv_name.setText(title);
			// CheckBox cb = (CheckBox) view.findViewById(R.id.cb_0);
			// cb.setVisibility(View.VISIBLE);
			// if ("1".equals(kzzf4)) {
			// cb.setChecked(true);
			// }
			// }
			//
			// ll.addView(view);
			// }
		} catch (Exception e) {
			dialogShowMessage_P("数据错误:" + errormsg + ",选项数据类型不匹配,请联系管理员修改",
					null);
			e.printStackTrace();
		}
	}

	private void loadOldValue(Map<String, String> data_oldValue) {

		for (int i = 0; i < data_smyy.size(); i++) {
			Map<String, String> map = data_smyy.get(i);
			if (data_oldValue.get("ecsmyy").equals(map.get("id"))) {
				spinner_smyy.setSelection(i);
			}
		}

		et_smyy.setText(data_oldValue.get("yysm"));
		et_sbidh.setText(data_oldValue.get("sbewm"));
		et_gzclgc.setText(data_oldValue.get("clgc"));
		sbxlhs = data_oldValue.get("zdh");
		if (!"".equals(sbxlhs)) {
			data_sbxlh = new ArrayList<String>();
			String[] sbxlh = sbxlhs.split(";");
			for (int i = 0; i < sbxlh.length; i++) {
				data_sbxlh.add(sbxlh[i]);
			}
			if (sbxlhs.length() > 15) {
				sbxlhs = sbxlhs.substring(0, 14) + "...";
			}
			et_sbxlhs.setText(sbxlhs);
		}
		sblx = data_oldValue.get("sblx");
		sbxh = data_oldValue.get("sbxh");
		for (int i = 0; i < data_sblx.size(); i++) {
			Map<String, String> map = data_sblx.get(i);
			if (sblx.equals(map.get("id"))) {
				spinner_sblx.setSelection(i);
			}
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
				if(isecsm){
					dialogShowMessage_P("提交成功",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface face,
										int paramAnonymous2Int) {
									onBackPressed();
								}
							});
				}else{
					dialogShowMessage_P("提交成功,请上传巡检照片",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface face,
										int paramAnonymous2Int) {
									Intent intent = new Intent(getApplicationContext(),UploadPhotoActivity.class);
									intent.putExtra("zbh", zbh);
									startActivity(intent);
									finish();
								}
							});
				}
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
				SimpleAdapter adapter = new SimpleAdapter(FwbgKdg.this,
						data_smyy, R.layout.spinner_item, from, to);
				spinner_smyy.setAdapter(adapter);

				adapter = new SimpleAdapter(FwbgKdg.this, data_sblx,
						R.layout.spinner_item, from, to);
				spinner_sblx.setAdapter(adapter);

				adapter = new SimpleAdapter(FwbgKdg.this, data_lxdh,
						R.layout.spinner_item, from, to);
				spinner_lxdh.setAdapter(adapter);

				LoadSbxx(data_zp, ll_show);
				if (sblx != null) {
					for (int i = 0; i < data_sblx.size(); i++) {
						Map<String, String> map = data_sblx.get(i);
						if (sblx.equals(map.get("id"))) {
							spinner_sblx.setSelection(i);
							sblx = null;
							break;
						}
					}
				}
				Config.getExecutorService().execute(new Runnable() {

					@Override
					public void run() {
						getWebService("getOldValue");
					}
				});
				break;
			case Constant.NUM_8:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				SimpleAdapter adapter1 = new SimpleAdapter(FwbgKdg.this,
						data_sbxh, R.layout.spinner_item, from, to);
				spinner_sbxh.setAdapter(adapter1);
				if (sbxh != null) {
					for (int i = 0; i < data_sbxh.size(); i++) {
						Map<String, String> map = data_sbxh.get(i);
						if (sbxh.equals(map.get("id"))) {
							spinner_sbxh.setSelection(i);
							sbxh = null;
							break;
						}
					}
				}
				break;
			case Constant.NUM_9:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				Intent intent = new Intent(getApplicationContext(),
						FwbgTzl.class);
				intent.putExtra("zbh", zbh);
				intent.putExtra("djzt", djzt);
				intent.putExtra("data", data_zp);
				intent.putExtra("frontStr", frontStr);
				intent.putExtra("backStr", backStr);
				startActivityForResult(intent, 7);
				break;
			case Constant.NUM_10:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}

				loadOldValue(data_oldValue);
				break;
			case Constant.NUM_12:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P("服务报告提交成功",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								onBackPressed();
							}
						});
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
