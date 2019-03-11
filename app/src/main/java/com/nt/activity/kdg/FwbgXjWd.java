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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
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
public class FwbgXjWd extends FrameActivity {

	private TextView tv_curr;
	private EditText et_xxdz, et_bst, et_vtm, et_bp, et_pr, et_bz;
	private Button confirm, cancel;
	private Spinner spinner_sf, spinner_ds, spinner_qx, spinner_hb,
			spinner_wdmc;
	private ArrayList<Map<String, String>> data_sf, data_ds, data_qx, data_hb,
			data_wdmc, data_xj;
	private Map<String, ArrayList<String>> filemap;
	private Map<String, Object> itemmap;
	private String[] from;
	private int[] to;
	private SimpleAdapter adapter;
	private String sfbm, dsbm, qxbm, wdbm, hbbm, errorMsg = "",type,zbh="";
	private LinearLayout ll_show_xj,ll_xj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_fwbgxjwd);
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

		et_xxdz = (EditText) findViewById(R.id.et_xxdz);
		et_bst = (EditText) findViewById(R.id.et_bst);
		et_vtm = (EditText) findViewById(R.id.et_vtm);
		et_bp = (EditText) findViewById(R.id.et_bp);
		et_pr = (EditText) findViewById(R.id.et_pr);
		et_bz = (EditText) findViewById(R.id.et_bz);

		confirm = (Button) findViewById(R.id.confirm);
		cancel = (Button) findViewById(R.id.cancel);

		spinner_sf = (Spinner) findViewById(R.id.spinner_sf);
		spinner_ds = (Spinner) findViewById(R.id.spinner_ds);
		spinner_qx = (Spinner) findViewById(R.id.spinner_qx);
		spinner_hb = (Spinner) findViewById(R.id.spinner_hb);
		spinner_wdmc = (Spinner) findViewById(R.id.spinner_wdmc);

		ll_xj = (LinearLayout) findViewById(R.id.ll_xj);
		ll_show_xj = (LinearLayout) findViewById(R.id.ll_show_xj);

		from = new String[] { "id", "name" };
		to = new int[] { R.id.bm, R.id.name };

		filemap = new HashMap<String, ArrayList<String>>();
	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());
		type = getIntent().getStringExtra("type");
		if("m_wdxjbg_sc".equals(type)||"m_wdxjbg_xg".equals(type)){
			itemmap = ServiceReportCache.getObjectdata()
					.get(ServiceReportCache.getIndex());
			zbh = itemmap.get("zbh").toString();
			
			ll_xj.setVisibility(View.VISIBLE);
			ll_show_xj.setVisibility(View.VISIBLE);
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
					onBackPressed();
					break;
				case R.id.confirm:
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
					wdbm = data_wdmc
							.get(spinner_wdmc.getSelectedItemPosition()).get(
									"id");
					if ("".equals(wdbm)) {
						toastShowMessage("请选择网点名称");
						return;
					}

					if (!isNotNull(et_xxdz)) {
						toastShowMessage("请录入详细地址");
						return;
					}
					
					if (!isNotNull(et_bst)) {
						et_bst.setText("0");
					}
					if (!isNotNull(et_vtm)) {
						et_bst.setText("0");
					}
					if (!isNotNull(et_bp)) {
						et_bst.setText("0");
					}
					if (!isNotNull(et_pr)) {
						et_bst.setText("0");
					}
					if(type!=null){
						for (int i = 0; i < ll_show_xj.getChildCount(); i++) {
							LinearLayout ll = (LinearLayout) ll_show_xj
									.getChildAt(i);
							if (ll.getChildAt(1) instanceof LinearLayout) {
								ll = (LinearLayout) ll.getChildAt(1);
								if (ll.getChildAt(0) instanceof RadioGroup) {
									RadioGroup rg = (RadioGroup) ll
											.getChildAt(0);
									RadioButton rb = (RadioButton) rg
											.findViewById(rg
													.getCheckedRadioButtonId());
									if (rb == null) {
										toastShowMessage("巡检内容中选择项不能为空！");
										return;
									}
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
				}
			}
		};

		topBack.setOnClickListener(onClickListener);
		cancel.setOnClickListener(onClickListener);
		confirm.setOnClickListener(onClickListener);

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

						getWebService("gethb");
					}
				});
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		spinner_hb.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				hbbm = data_hb.get(position).get("id");
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
				et_xxdz.setText(data_wdmc.get(position).get("xxdz"));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

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
							"uf_json_setdata", zbh, "c#_PAD_KDG_XJ_GDCZP");
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

					if(type!=null){
						String sqlid = "_PAD_SHGL_TDXJ_FWBG_XJNR";
						String cs = "";
						if("m_wdxjbg_sc".equals(type)){
							sqlid = "_PAD_SHGL_TDXJ_FWBG_XJNR";
						}else if("m_wdxjbg_xg".equals(type)){
							sqlid = "_PAD_SHGL_TDXJ_FWBG_XGNR";
							cs = zbh+"*"+zbh+"*"+zbh;
						}
						jsonObject = callWebserviceImp.getWebServerInfo(
								sqlid, cs, "uf_json_getdata",
								this);
						flag = jsonObject.getString("flag");
						data_xj = new ArrayList<Map<String, String>>();
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
								data_xj.add(item);
							}
						} else {
							errorMsg = "获取巡检信息失败";
							Message msg = new Message();
							msg.what = Constant.FAIL;
							handler.sendMessage(msg);
						}
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
		
		if ("gethb".equals(s)) {
			try {
				data_hb = new ArrayList<Map<String, String>>();
				Map<String, String> item = new HashMap<String, String>();
				item.put("id", "");
				item.put("name", "");
				data_hb.add(item);
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SHGL_JCSJ_HYXX", "", "uf_json_getdata", this);
				String flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						item = new HashMap<String, String>();
						item.put("id", temp.getString("ccd"));
						item.put("name", temp.getString("ccdnm"));
						data_hb.add(item);
					}
				}
				Message msg = new Message();
				msg.what = Constant.NUM_9;
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
						"_PAD_SHGL_WDMC_QXHB", qxbm+"*"+hbbm, "uf_json_getdata", this);
				String flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						item = new HashMap<String, String>();
						item.put("id", temp.getString("wdbm"));
						item.put("name", temp.getString("wdmc"));
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

		if ("submit".equals(s)) {

			try {
				String str = DataCache.getinition().getUserId()+ "*PAM*";
				str += sfbm + "*PAM*";
				str += dsbm + "*PAM*";
				str += qxbm + "*PAM*";
				str += hbbm + "*PAM*";
				str += wdbm + "*PAM*";
				str += et_xxdz.getText().toString() + "*PAM*";
				str += et_bst.getText().toString() + "*PAM*";
				str += et_vtm.getText().toString() + "*PAM*";
				str += et_bp.getText().toString() + "*PAM*";
				str += et_pr.getText().toString() + "*PAM*";
				str += et_bz.getText().toString() + "*PAM*";
				str += zbh;// zbh

				String sqlid = "";
				if(type==null){
					sqlid = "c#_PAD_XJGL_TWDSBXJ";
				}else {
					if("m_wdxjbg_sc".equals(type)){
						sqlid = "c#_PAD_XJGL_TWDBG_SC";
					}else{
						sqlid = "c#_PAD_XJGL_TWDBG_XG";
					}
					
					String cs_xj = "";
					for (int i = 0; i < ll_show_xj.getChildCount(); i++) {
						LinearLayout ll = (LinearLayout) ll_show_xj.getChildAt(i);
						if (ll.getChildAt(1) instanceof LinearLayout) {
							ll = (LinearLayout) ll.getChildAt(1);
							if (ll.getChildAt(0) instanceof RadioGroup) {
								RadioGroup rg = (RadioGroup) ll.getChildAt(0);
								RadioButton rb = (RadioButton) rg.findViewById(rg
										.getCheckedRadioButtonId());
								if (rb == null) {
									cs_xj += "#@##@#0";
								} else {
									String che = rb.getId() == R.id.rb_1 ? "1"
											: "2";
									cs_xj += rb.getText().toString() + "#@#" + che
											+ "#@#" + rb.getTag().toString();
								}
								cs_xj += "#^#";
							} else if (ll.getChildAt(0) instanceof EditText) {
								EditText et = (EditText) ll.getChildAt(0);
								CheckBox cb = (CheckBox) ll.getChildAt(1);
								String che = cb.isChecked() ? "1" : "2";
								cs_xj += et.getText().toString() + "#@#" + che
										+ "#@#" + et.getTag().toString();
								cs_xj += "#^#";
							}
						}

					}
					if (!"".equals(cs_xj)) {
						cs_xj = cs_xj.substring(0, cs_xj.length() - 3);
					}
					str += "*PAM*"+cs_xj;
				}

				JSONObject json = this.callWebserviceImp.getWebServerInfo(
						sqlid, str, "", "", "uf_json_setdata2",
						this);
				String flag = json.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					if (filemap.size() > 0) {
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

	@SuppressLint("ResourceAsColor")
	private void loadXjxx(ArrayList<Map<String, String>> data, LinearLayout ll) {

		String errormsg = "";
		try {
			if (data.size() > 0) {
				ll_show_xj.setVisibility(View.VISIBLE);
				findViewById(R.id.ll_xj).setVisibility(View.VISIBLE);
			}
			for (int i = 0; i < data.size(); i++) {
				Map<String, String> map = data.get(i);
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

				} else if ("5".equals(type)) { // 数值
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.include_xj_text, null);
					TextView tv_name = (TextView) view
							.findViewById(R.id.tv_name);
					EditText et_val = (EditText) view.findViewById(R.id.et_val);
					et_val.setInputType(InputType.TYPE_CLASS_NUMBER);
					et_val.setTag(map.get("mxh"));
					et_val.setText(tzz);
					et_val.setHint(title);
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
				adapter = new SimpleAdapter(FwbgXjWd.this, data_sf,
						R.layout.spinner_item, from, to);
				spinner_sf.setAdapter(adapter);
				if(itemmap!=null){
					setSpinnerVal(spinner_sf,data_sf,itemmap.get("sfbm").toString());
				}
				
				if(type!=null){
					loadXjxx(data_xj, ll_show_xj);
				}
				break;
			case Constant.NUM_7:
				adapter = new SimpleAdapter(FwbgXjWd.this, data_ds,
						R.layout.spinner_item, from, to);
				spinner_ds.setAdapter(adapter);
				if(itemmap!=null){
					setSpinnerVal(spinner_ds,data_ds,itemmap.get("dsbm").toString());
				}
				break;
			case Constant.NUM_8:
				adapter = new SimpleAdapter(FwbgXjWd.this, data_qx,
						R.layout.spinner_item, from, to);
				spinner_qx.setAdapter(adapter);
				if(itemmap!=null){
					setSpinnerVal(spinner_qx,data_qx,itemmap.get("qxbm").toString());
				}
				break;
			case Constant.NUM_9:
				adapter = new SimpleAdapter(FwbgXjWd.this, data_hb,
						R.layout.spinner_item, from, to);
				spinner_hb.setAdapter(adapter);

				if(itemmap!=null){
					setSpinnerVal(spinner_hb,data_hb,itemmap.get("ywhybm").toString());
				}
				break;
			case Constant.NUM_10:
				adapter = new SimpleAdapter(FwbgXjWd.this, data_wdmc,
						R.layout.spinner_item, from, to);
				spinner_wdmc.setAdapter(adapter);
				if(itemmap!=null){
					setSpinnerVal(spinner_wdmc,data_wdmc,itemmap.get("wdbm").toString());
					et_xxdz.setText(itemmap.get("xxdz").toString());
					et_bst.setText(itemmap.get("zgsl").toString());
					et_vtm.setText(itemmap.get("kzsz2").toString());
					et_bp.setText(itemmap.get("kzsz1").toString());
					et_pr.setText(itemmap.get("fgsl").toString());
					et_bz.setText(itemmap.get("bz").toString());
					itemmap = null;
				}
				break;
			case Constant.NUM_11:

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
			}

		}

	};
}