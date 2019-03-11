package com.nt.activity.esp;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.activity.kdg.SmdwKdg;
import com.nt.activity.login.LoginActivity;
import com.nt.cache.DataCache;
import com.nt.cache.ServiceReportCache;
import com.nt.common.Constant;
import com.nt.utils.Config;
import com.nt.utils.ImageUtil;
import com.nt.zxing.CaptureActivity;

/**
 * 新增设备信息
 * 
 * @author Administrator 20170410
 */
@SuppressLint("HandlerLeak")
public class SheBeiPDActivity extends FrameActivity {

	private TextView tv_pz, tv_bjrz;
	private EditText et_sbidh, et_sbxlh, et_xxdz, et_wdmc;
	private Button confirm, cancel, btn_sm;
	private Spinner spinner_sf, spinner_ds, spinner_qx, spinner_hb,
			spinner_wdmc, spinner_sblx, spinner_sbxh,spinner_lxdh;
	private ImageView iv_telphone;
	private ArrayList<Map<String, String>> data_sf, data_ds, data_qx, data_hb,
			data_wdmc, data_sblx, data_sbxh, data_bjrz,data_lxdh;
	private ArrayList<String> list_photo;
	private JSONObject json_sbxx;
	private String[] from;
	private int[] to;
	private SimpleAdapter adapter;
	private String sfbm, dsbm, qxbm, sbid, wdbm, sblxbm, sbxhbm, hbbm, xxdz,hpbm,sbbm,
			sf_mc,ds_mc,qx_mc,wdbm_mc,errorMsg = "";
	private boolean isxz = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_shebeipd);
		initVariable();
		initView();
		initListeners();
		showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {

				getWebService("getsf");
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
		

		tv_pz = (TextView) findViewById(R.id.tv_pz);
		tv_bjrz = (TextView) findViewById(R.id.tv_bjrz);
		et_sbidh = (EditText) findViewById(R.id.et_sbidh);
		et_sbxlh = (EditText) findViewById(R.id.et_sbxlh);
		et_xxdz = (EditText) findViewById(R.id.et_xxdz);

		confirm = (Button) findViewById(R.id.confirm);
		cancel = (Button) findViewById(R.id.cancel);

		et_wdmc = (EditText) findViewById(R.id.et_wdmc);

		spinner_sf = (Spinner) findViewById(R.id.spinner_sf);
		spinner_ds = (Spinner) findViewById(R.id.spinner_ds);
		spinner_qx = (Spinner) findViewById(R.id.spinner_qx);
		spinner_hb = (Spinner) findViewById(R.id.spinner_hb);
		spinner_wdmc = (Spinner) findViewById(R.id.spinner_wdmc);
		spinner_sblx = (Spinner) findViewById(R.id.spinner_sblx);
		spinner_sbxh = (Spinner) findViewById(R.id.spinner_sbxh);
		spinner_lxdh = (Spinner) findViewById(R.id.spinner_lxdh);
		
		iv_telphone = (ImageView) findViewById(R.id.iv_telphone);
		
		btn_sm = (Button) findViewById(R.id.btn_sm);

		from = new String[] { "id", "name" };
		to = new int[] { R.id.bm, R.id.name };
	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());
		data_bjrz = new ArrayList<Map<String, String>>();
		hpbm = getIntent().getStringExtra("hpbm");
		sbbm = getIntent().getStringExtra("sbbm");
		if(sbbm!=null&&!"".equals(sbbm)){
			sf_mc= getIntent().getStringExtra("sf_mc");
			ds_mc= getIntent().getStringExtra("ds_mc");
			qx_mc= getIntent().getStringExtra("qx_mc");
			wdbm_mc= getIntent().getStringExtra("wdbm_mc");
			xxdz = getIntent().getStringExtra("xxdz");
			isxz = true;
			cancel.setText("无此设备");
		}else{
			hpbm = "";
			sbbm = "";
		}
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
					if(isxz){
						Intent intent1 = new Intent(getApplicationContext(),
								YwsbActivity.class);
						intent1.putExtra("sf_mc", sf_mc);
						intent1.putExtra("ds_mc", ds_mc);
						intent1.putExtra("qx_mc", qx_mc);
						intent1.putExtra("wdbm_mc", wdbm_mc);
						intent1.putExtra("xxdz", xxdz);
						intent1.putExtra("hpbm", hpbm);
						intent1.putExtra("sbbm", sbbm);
						startActivityForResult(intent1, 7);
					}else{
						onBackPressed();
					}
					break;
				case R.id.confirm:
					if (DataCache.getinition().getUserId() == null) {
						dialogShowMessage_P("登陆超时，请重新登陆",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface face,
											int paramAnonymous2Int) {
										Intent intent = new Intent(
												getApplicationContext(),
												LoginActivity.class);
										startActivity(intent);
										finish();
									}
								});
						return;
					}
					if (!isNotNull(et_sbidh)) {
						toastShowMessage("请扫描二维码");
						return;
					}
					if (!isNotNull(et_sbxlh)) {
						toastShowMessage("请录入设备序列号");
						return;
					}
					sblxbm = data_sblx.get(
							spinner_sblx.getSelectedItemPosition()).get("id");
					if ("".equals(sblxbm)) {
						toastShowMessage("请选择设备类型");
						return;
					}
					sbxhbm = data_sbxh.get(
							spinner_sbxh.getSelectedItemPosition()).get("id");
					if ("".equals(sbxhbm)) {
						toastShowMessage("请选择设备型号");
						return;
					}
					sfbm = data_sf.get(spinner_sf.getSelectedItemPosition())
							.get("id");
					if ("".equals(sfbm)) {
						toastShowMessage("请选择省份");
						return;
					}
					dsbm = data_ds.get(spinner_ds.getSelectedItemPosition())
							.get("id");
					if ("".equals(dsbm)) {
						toastShowMessage("请选择地市");
						return;
					}
					qxbm = data_qx.get(spinner_qx.getSelectedItemPosition())
							.get("id");
					if ("".equals(qxbm)) {
						toastShowMessage("请选择区县");
						return;
					}
					hbbm = data_hb.get(spinner_hb.getSelectedItemPosition())
							.get("id");
					if ("".equals(hbbm)) {
						toastShowMessage("请选择行别");
						return;
					}
					wdbm = data_wdmc.get(
							spinner_wdmc.getSelectedItemPosition()).get(
							"id");
					if ("".equals(wdbm)) {
						toastShowMessage("请选择网点名称");
						return;
					}

					if (!isNotNull(et_xxdz)) {
						toastShowMessage("请录入详细地址");
						return;
					}
					showProgressDialog();
					Config.getExecutorService().execute(new Runnable() {

						@Override
						public void run() {

							getWebService("submit");
						}
					});
					break;
				case R.id.btn_sm:
					startSm();
					break;
				case R.id.tv_bjrz:
					Intent intent = new Intent(getApplicationContext(),
							AddPjListActivity.class);
					intent.putExtra("data", data_bjrz);
					startActivityForResult(intent, 8);
					break;
				}
			}
		};

		topBack.setOnClickListener(onClickListener);
		cancel.setOnClickListener(onClickListener);
		confirm.setOnClickListener(onClickListener);
		btn_sm.setOnClickListener(onClickListener);
		tv_bjrz.setOnClickListener(onClickListener);

		spinner_sblx.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				sblxbm = data_sblx.get(position).get("id");
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

		spinner_sf.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				sfbm = data_sf.get(position).get("id");
				Config.getExecutorService().execute(new Runnable() {

					@Override
					public void run() {

						getWebService("getds");
					}
				});
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		spinner_ds.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				dsbm = data_ds.get(position).get("id");
				Config.getExecutorService().execute(new Runnable() {

					@Override
					public void run() {

						getWebService("getqx");
					}
				});
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		spinner_qx.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				qxbm = data_qx.get(position).get("id");
				Config.getExecutorService().execute(new Runnable() {

					@Override
					public void run() {

						getWebService("getwdmc");
					}
				});
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		spinner_wdmc.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				wdbm = data_wdmc.get(position).get("id");
				if (hbbm == null || "".equals(xxdz)) {
					hbbm = data_wdmc.get(position).get("kzzf5");
					if (hbbm != null && !"".equals(hbbm)) {
						for (int i = 0; i < data_hb.size(); i++) {
							if (hbbm.equals(data_hb.get(i).get("id"))) {
								spinner_hb.setSelection(i);
							}
						}
					}
					hbbm = null;
				}

				et_xxdz.setText(data_wdmc.get(position).get("xxdz"));
				if (xxdz != null && !"".equals(xxdz)) {
					et_xxdz.setText(xxdz);
					xxdz = "";
				}
				Config.getExecutorService().execute(new Runnable() {

					@Override
					public void run() {
						getWebService("getlxdh");
					}
				});
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		
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

		tv_pz.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				camera(5, list_photo);
			}
		});
	}

	private void startSm() {
		// 二维码
		Intent intent = new Intent(getApplicationContext(),
				CaptureActivity.class);
		startActivityForResult(intent, 2);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 2 && resultCode == 2 && data != null) {
			// 二维码
			sbid = data.getStringExtra("result").trim();
			showProgressDialog();
			Config.getExecutorService().execute(new Runnable() {

				@Override
				public void run() {
					getWebService("checksbbm");
				}
			});

		}
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

		if (requestCode == 7 && resultCode == 1) {
			onBackPressed();
		}

		if (requestCode == 8 && resultCode == 1) {
			try {
				data_bjrz = (ArrayList<Map<String, String>>) data
						.getSerializableExtra("list");
				String xlhs = "";
				for (int i = 0; i < data_bjrz.size(); i++) {
					Map<String, String> map = data_bjrz.get(i);
					xlhs += map.get("bjlbmc") + "-" + map.get("bjxhmc") + ";";
				}
				tv_bjrz.setText(xlhs);
			} catch (Exception e) {

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
					flag = uploadPic(i + "", sbid, readJpeg(file),
							"c#_PAD_ERP_CCGL_SBZP");
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

		if ("getsf".equals(s)) {
			try {
				data_sf = new ArrayList<Map<String, String>>();
				Map<String, String> item = new HashMap<String, String>();
				item.put("id", "");
				item.put("name", "");
				data_sf.add(item);
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SBGL_SBLR_DQXX1", "", "uf_json_getdata", this);
				String flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						item = new HashMap<String, String>();
						item.put("id", temp.getString("sfbm"));
						item.put("name", temp.getString("sfmc"));
						data_sf.add(item);
					}

					data_sblx = new ArrayList<Map<String, String>>();
					item = new HashMap<String, String>();
					item.put("id", "");
					item.put("name", "");
					data_sblx.add(item);
					jsonObject = callWebserviceImp.getWebServerInfo(
							"_PAD_NT_SBLX", "", "uf_json_getdata", this);
					flag = jsonObject.getString("flag");
					if (Integer.parseInt(flag) > 0) {
						jsonArray = jsonObject.getJSONArray("tableA");
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject temp = jsonArray.getJSONObject(i);
							item = new HashMap<String, String>();
							item.put("id", temp.getString("ccd"));
							item.put("name", temp.getString("ccdnm"));
							data_sblx.add(item);
						}
					} else {
						errorMsg = "获取设备类型失败";
						Message msg = new Message();
						msg.what = Constant.FAIL;
						handler.sendMessage(msg);
					}

					data_hb = new ArrayList<Map<String, String>>();
					item = new HashMap<String, String>();
					item.put("id", "");
					item.put("name", "");
					data_hb.add(item);
					jsonObject = callWebserviceImp.getWebServerInfo(
							"_PAD_SHGL_JCSJ_HYXX", "", "uf_json_getdata", this);
					flag = jsonObject.getString("flag");
					if (Integer.parseInt(flag) > 0) {
						jsonArray = jsonObject.getJSONArray("tableA");
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject temp = jsonArray.getJSONObject(i);
							item = new HashMap<String, String>();
							item.put("id", temp.getString("ccd"));
							item.put("name", temp.getString("ccdnm"));
							data_hb.add(item);
						}
					} else {
						errorMsg = "获取行别信息失败";
						Message msg = new Message();
						msg.what = Constant.FAIL;
						handler.sendMessage(msg);
					}

					Message msg = new Message();
					msg.what = Constant.NUM_6;
					handler.sendMessage(msg);
				} else {
					errorMsg = "获取省份信息失败";
					Message msg = new Message();
					msg.what = Constant.FAIL;
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;// 网络不通
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
						"_PAD_KDG_FWBG_SBXH", sblxbm, "uf_json_getdata", this);
				String flag = jsonObject.getString("flag");

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
				msg.what = Constant.NUM_12;
				handler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;
				handler.sendMessage(msg);
			}
		}

		if ("checksbbm".equals(s)) {
			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SBXXLR_SBCX", sbid, "uf_json_getdata", this);
				String flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					json_sbxx = jsonArray.getJSONObject(0);

					data_bjrz = new ArrayList<Map<String, String>>();
					jsonObject = callWebserviceImp.getWebServerInfo(
							"_PAD_JCSJ_BJLBXX", sbid, "uf_json_getdata", this);
					flag = jsonObject.getString("flag");
					if (Integer.parseInt(flag) > 0) {
						jsonArray = jsonObject.getJSONArray("tableA");
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject temp = jsonArray.getJSONObject(i);
							HashMap<String, String> item = new HashMap<String, String>();
							item.put("bjlbbm", temp.getString("lbbm"));
							item.put("bjlbmc", temp.getString("lbmc"));
							item.put("bjxhbm", temp.getString("xhbm"));
							item.put("bjxhmc", temp.getString("xhmc"));
							data_bjrz.add(item);
						}
					}

					Message msg = new Message();
					msg.what = Constant.NUM_11;
					handler.sendMessage(msg);
				} else {
					json_sbxx = null;
					Message msg = new Message();
					msg.what = Constant.NUM_11;
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}
		}

		if ("getds".equals(s)) {
			try {
				data_ds = new ArrayList<Map<String, String>>();
				Map<String, String> item = new HashMap<String, String>();
				item.put("id", "");
				item.put("name", "");
				data_ds.add(item);
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SBGL_SBLR_DQXX2", sfbm, "uf_json_getdata", this);
				String flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						item = new HashMap<String, String>();
						item.put("id", temp.getString("dsbm"));
						item.put("name", temp.getString("dsmc"));
						data_ds.add(item);
					}
				}
				Message msg = new Message();
				msg.what = Constant.NUM_7;
				handler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}
		}

		if ("getqx".equals(s)) {
			try {
				data_qx = new ArrayList<Map<String, String>>();
				Map<String, String> item = new HashMap<String, String>();
				item.put("id", "");
				item.put("name", "");
				data_qx.add(item);
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SBGL_SBLR_DQXX3", dsbm, "uf_json_getdata", this);
				String flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						item = new HashMap<String, String>();
						item.put("id", temp.getString("qxbm"));
						item.put("name", temp.getString("qxmc"));
						data_qx.add(item);
					}

				}
				Message msg = new Message();
				msg.what = Constant.NUM_8;
				handler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}
		}

		if ("getwdmc".equals(s)) {
			try {
				data_wdmc = new ArrayList<Map<String, String>>();
				Map<String, String> item = new HashMap<String, String>();
				item.put("id", "");
				item.put("name", "");
				data_wdmc.add(item);
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SBGL_SBLR_WDXX", qxbm, "uf_json_getdata", this);
				String flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						item = new HashMap<String, String>();
						item.put("id", temp.getString("wdbm"));
						item.put("name", temp.getString("wdmc"));
						item.put("kzzf5", temp.getString("kzzf5"));
						item.put("xxdz", temp.getString("xxdz"));
						data_wdmc.add(item);
					}

				}
				Message msg = new Message();
				msg.what = Constant.NUM_10;
				handler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}
		}

		if (s.equals("getlxdh")) {
			try {
				data_lxdh = new ArrayList<Map<String, String>>();
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SBGL_WDLXR", wdbm, "uf_json_getdata", this);
				String flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, String> item = new HashMap<String, String>();
						item.put("id", temp.getString("lxr"));
						item.put("name", temp.getString("lxrdh"));
						data_lxdh.add(item);
					}
				}else{
					Map<String, String> item = new HashMap<String, String>();
					item.put("id", "");
					item.put("name", "");
					data_lxdh.add(item);
				}
				Message msg = new Message();
				msg.what = Constant.NUM_13;
				handler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;
				handler.sendMessage(msg);
			}
		}
		
		if ("submit".equals(s)) {

			try {
				String wdmc = wdbm;
				String str = "";
				str += et_sbxlh.getText().toString() + "*PAM*";
				str += sblxbm + "*PAM*";
				str += sbxhbm + "*PAM*";
				str += sfbm + "*PAM*";
				str += dsbm + "*PAM*";
				str += qxbm + "*PAM*";
				str += hbbm + "*PAM*";
				str += wdmc + "*PAM*";
				str += et_xxdz.getText().toString() + "*PAM*";
				str += sbid + "*PAM*";
				str += DataCache.getinition().getUserId() + "*PAM*";
				str += hpbm + "*PAM*";

				String bjrzs = "";
				for (int i = 0; i < data_bjrz.size(); i++) {
					Map<String, String> map = data_bjrz.get(i);
					bjrzs = map.get("bjxhbm") + "#@#" + map.get("bjxhmc")
							+ "#@#" + map.get("bjlbbm");
					bjrzs += "#^#";
				}
				if (!"".equals(bjrzs)) {
					bjrzs = bjrzs.substring(0, bjrzs.length() - 3);
				}
				str += bjrzs;
				JSONObject json = this.callWebserviceImp.getWebServerInfo(
						"c#_PAD_KDG_SBXXLY2", str, "", "", "uf_json_setdata2",
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

	private void getSbxx() {
		json_sbxx = new JSONObject();
		try {
			Intent intent = getIntent();
			json_sbxx.put("sbid", "");
			json_sbxx.put("sbbm", intent.getStringExtra("sbbm"));
			json_sbxx.put("sblx", intent.getStringExtra("sblx"));
			json_sbxx.put("sbxh", intent.getStringExtra("sbxh"));
			json_sbxx.put("sf", intent.getStringExtra("sf"));
			json_sbxx.put("ds", intent.getStringExtra("ds"));
			json_sbxx.put("qx", intent.getStringExtra("qx"));
			json_sbxx.put("wdbm", intent.getStringExtra("wdbm"));
			json_sbxx.put("wdbm_mc", "");
			json_sbxx.put("xxdz", intent.getStringExtra("xxdz"));
			json_sbxx.put("ywhb", intent.getStringExtra("ywhb"));
			json_sbxx.put("sfxz", intent.getStringExtra("sfxz"));
			loadSbxx();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void loadSbxx() {
		try {
			if (json_sbxx == null) {
				et_sbidh.setText(sbid);
			} else {
				et_sbidh.setText(json_sbxx.getString("sbid"));
				et_sbxlh.setText(json_sbxx.getString("sbbm"));
				sblxbm = json_sbxx.getString("sblx");
				sbxhbm = json_sbxx.getString("sbxh");
				for (int i = 0; i < data_sblx.size(); i++) {
					if (sblxbm.equals(data_sblx.get(i).get("id"))) {
						spinner_sblx.setSelection(i);
					}
				}
				sfbm = json_sbxx.getString("sf");
				dsbm = json_sbxx.getString("ds");
				qxbm = json_sbxx.getString("qx");
				wdbm = json_sbxx.getString("wdbm");
				String wdmcbmmc = json_sbxx.getString("wdbm_mc");
				xxdz = json_sbxx.getString("xxdz");
				hbbm = json_sbxx.getString("ywhb");
				for (int i = 0; i < data_sf.size(); i++) {
					if (sfbm.equals(data_sf.get(i).get("id"))) {
						spinner_sf.setSelection(i);
					}
				}
				for (int i = 0; i < data_hb.size(); i++) {
					if (hbbm.equals(data_hb.get(i).get("id"))) {
						spinner_hb.setSelection(i);
					}
				}
				et_wdmc.setText(wdmcbmmc);
				et_xxdz.setText(xxdz);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void loadBjxx() {
		String xlhs = "";
		for (int i = 0; i < data_bjrz.size(); i++) {
			Map<String, String> map = data_bjrz.get(i);
			xlhs += map.get("bjlbmc") + "-" + map.get("bjxhmc") + ";";
		}
		tv_bjrz.setText(xlhs);
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
								onBackPressed();
							}
						});
				break;
			case Constant.NUM_6:
				adapter = new SimpleAdapter(SheBeiPDActivity.this, data_sf,
						R.layout.spinner_item, from, to);
				spinner_sf.setAdapter(adapter);
				adapter = new SimpleAdapter(SheBeiPDActivity.this, data_sblx,
						R.layout.spinner_item, from, to);
				spinner_sblx.setAdapter(adapter);
				adapter = new SimpleAdapter(SheBeiPDActivity.this, data_hb,
						R.layout.spinner_item, from, to);
				spinner_hb.setAdapter(adapter);
				getSbxx();
				break;
			case Constant.NUM_7:
				adapter = new SimpleAdapter(SheBeiPDActivity.this, data_ds,
						R.layout.spinner_item, from, to);
				spinner_ds.setAdapter(adapter);
				if (dsbm != null && !"".equals(dsbm)) {
					for (int i = 0; i < data_ds.size(); i++) {
						if (dsbm.equals(data_ds.get(i).get("id"))) {
							spinner_ds.setSelection(i);
						}
					}
				}
				break;
			case Constant.NUM_8:
				adapter = new SimpleAdapter(SheBeiPDActivity.this, data_qx,
						R.layout.spinner_item, from, to);
				spinner_qx.setAdapter(adapter);
				if (qxbm != null && !"".equals(qxbm)) {
					for (int i = 0; i < data_qx.size(); i++) {
						if (qxbm.equals(data_qx.get(i).get("id"))) {
							spinner_qx.setSelection(i);
						}
					}
				}
				break;
			case Constant.NUM_9:
				adapter = new SimpleAdapter(SheBeiPDActivity.this, data_hb,
						R.layout.spinner_item, from, to);
				spinner_hb.setAdapter(adapter);
				break;
			case Constant.NUM_10:
				adapter = new SimpleAdapter(SheBeiPDActivity.this, data_wdmc,
						R.layout.spinner_item, from, to);
				spinner_wdmc.setAdapter(adapter);
				if (wdbm != null && !"".equals(wdbm)) {
					for (int i = 0; i < data_wdmc.size(); i++) {
						if (wdbm.equals(data_wdmc.get(i).get("id"))) {
							spinner_wdmc.setSelection(i);
						}
					}
				}
				break;
			case Constant.NUM_11:
				loadSbxx();
				loadBjxx();
				break;
			case Constant.NUM_12:
				adapter = new SimpleAdapter(SheBeiPDActivity.this, data_sbxh,
						R.layout.spinner_item, from, to);
				spinner_sbxh.setAdapter(adapter);
				if (sbxhbm != null && !"".equals(sbxhbm)) {
					for (int i = 0; i < data_sbxh.size(); i++) {
						if (sbxhbm.equals(data_sbxh.get(i).get("id"))) {
							spinner_sbxh.setSelection(i);
						}
					}
				}
				break;
			case Constant.NUM_13:
				SimpleAdapter adapter = new SimpleAdapter(SheBeiPDActivity.this,
						data_lxdh, R.layout.spinner_item, from, to);
				spinner_lxdh.setAdapter(adapter);
				break;
			}

		}

	};
}