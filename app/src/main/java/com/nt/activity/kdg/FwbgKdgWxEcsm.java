package com.nt.activity.kdg;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

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
 * 快递柜-服务报告
 * 
 * @author zdkj 20170407
 *
 */
@SuppressLint("ResourceAsColor")
public class FwbgKdgWxEcsm extends FrameActivity {

	private Button confirm, cancel;
	private String flag, zbh, msgStr, zdh, zbh_xj, xzpj_str, sbxhbm, dlbm,
			zlbm, xlbm, sblx, sbxh, clgcs, sbxlhs, bzr, lxdh, ywlx2, ds,
			ywhy_bm, ewm = "", hpbm = "",wxts="", rgfy,wcqkbm;
	private CheckBox cb_xzpj;
	private Spinner spinner_gzdl, spinner_gzzl, spinner_gzxl, spinner_sblx,
			spinner_sbxh, spinner_smyy, spinner_lxdh,spinner_wgqk;
	private TextView tv_xzpj;
	private EditText et_smyy, et_rgsfbj, et_bjsfbz, et_wxts, et_gzclgc,
			et_sbxlhs,et_ycwgbz;
	private RadioGroup rg_0;
	private ArrayList<Map<String, String>> data_gzbm, data_all, gzbm_2_list,
			gzbm_3_list, data_zp, data_xj, data_load_yhpj, data_sblx,
			data_sbxh, data_smyy, data_xzpj, data_lxdh,data_wgqk;
	private String[] from;
	private int[] to;
	private ArrayList<String> data_sbxlh, data_sbxlh_search;
	private Map<String, String> data_oldValue;
	private TextView tv_curr, tv_xxdz,tv_pjtp;
	private ImageView iv_telphone;
	private LinearLayout ll_show, ll_show_xj;
	private Map<String, ArrayList<String>> filemap, filemap_xj;
	private boolean isFirst = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_kdg_fwbgwx);
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
		ll_show_xj = (LinearLayout) findViewById(R.id.ll_show_xj);
		spinner_gzdl = (Spinner) findViewById(R.id.spinner_gzdl);
		spinner_gzzl = (Spinner) findViewById(R.id.spinner_gzzl);
		spinner_gzxl = (Spinner) findViewById(R.id.spinner_gzxl);
		cb_xzpj = (CheckBox) findViewById(R.id.cb_xzpj);
		tv_xzpj = (TextView) findViewById(R.id.tv_xzpj);
		tv_xxdz = (TextView) findViewById(R.id.tv_ywlx);
		tv_pjtp = (TextView) findViewById(R.id.tv_pjtp);
		spinner_sblx = (Spinner) findViewById(R.id.spinner_sblx);
		spinner_sbxh = (Spinner) findViewById(R.id.spinner_sbxh);
		spinner_smyy = (Spinner) findViewById(R.id.spinner_smyy);
		spinner_lxdh = (Spinner) findViewById(R.id.spinner_lxdh);
		spinner_wgqk = (Spinner) findViewById(R.id.spinner_wgqk);
		et_smyy = (EditText) findViewById(R.id.et_smyy);
		et_rgsfbj = (EditText) findViewById(R.id.et_rgsfbj);
		et_bjsfbz = (EditText) findViewById(R.id.et_bjsfbz);
		et_wxts = (EditText) findViewById(R.id.et_wxts);
		et_gzclgc = (EditText) findViewById(R.id.et_gzclgc);
		et_sbxlhs = (EditText) findViewById(R.id.et_sbxlhs);
		et_ycwgbz = (EditText) findViewById(R.id.et_ycwgbz);
		rg_0 = (RadioGroup) findViewById(R.id.rg_0);
		iv_telphone = (ImageView) findViewById(R.id.iv_telphone);

		from = new String[] { "id", "name" };
		to = new int[] { R.id.bm, R.id.name };
		data_gzbm = new ArrayList<Map<String, String>>();
		gzbm_2_list = new ArrayList<Map<String, String>>();
		gzbm_3_list = new ArrayList<Map<String, String>>();
		filemap = new HashMap<String, ArrayList<String>>();
		filemap_xj = new HashMap<String, ArrayList<String>>();
		data_sbxh = new ArrayList<Map<String, String>>();
		data_xzpj = new ArrayList<Map<String, String>>();
		data_wgqk = new ArrayList<Map<String, String>>();
		final Map<String, Object> itemmap = ServiceReportCache.getObjectdata()
				.get(ServiceReportCache.getIndex());

		zbh_xj = "";
		zbh = itemmap.get("zbh").toString();
		zdh = itemmap.get("zdh").toString();
		bzr = itemmap.get("khlxr").toString();
		lxdh = itemmap.get("lxdh").toString();
		ywlx2 = itemmap.get("ywlx2").toString();
		ds = itemmap.get("ds").toString();
		wcqkbm = itemmap.get("wcqkbm").toString();
		ywhy_bm = itemmap.get("ywhy_bm").toString();
		et_ycwgbz.setText(itemmap.get("kzzf2").toString());
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
		((TextView) findViewById(R.id.tv_sblx)).setText(itemmap.get("gzxx")
				.toString());
		((TextView) findViewById(R.id.tv_gzxx)).setText(itemmap.get("ywlx")
				.toString());
		((TextView) findViewById(R.id.tv_sfyj)).setText("1".equals(itemmap.get(
				"dygdh1").toString()) ? "是" : "否");
		((TextView) findViewById(R.id.tv_hy)).setText(itemmap.get("ywhy")
				.toString());
		((TextView) findViewById(R.id.tv_sflx)).setText(itemmap.get("axdh")
				.toString());
		((TextView) findViewById(R.id.tv_btgyy)).setText(itemmap.get("fwnr")
				.toString());
		((TextView) findViewById(R.id.tv_bz)).setText(itemmap.get("bz")
				.toString());
		((TextView) findViewById(R.id.tv_sbsx)).setText(itemmap.get("sflx")
				.toString());
		findViewById(R.id.ll_btgyy).setVisibility(View.GONE);

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

					if ("".equals(gzbm_3_list.get(
							spinner_gzxl.getSelectedItemPosition()).get("id"))) {
						toastShowMessage("请选择故障类别！");
						return;
					}
					if ("".equals(data_sblx.get(
							spinner_sblx.getSelectedItemPosition()).get("id"))) {
						toastShowMessage("请选择设备类型！");
						return;
					}
					if ("".equals(data_sbxh.get(
							spinner_sbxh.getSelectedItemPosition()).get("id"))) {
						toastShowMessage("请选择设备型号！");
						return;
					}
					if (!isNotNull(et_gzclgc)) {
						toastShowMessage("请录入故障处理过程！");
						return;
					}
					if (!isNotNull(et_wxts)) {
						toastShowMessage("请录入维修台数！");
						return;
					}
					if (Integer.parseInt(et_wxts.getText().toString().trim()) == 0) {
						toastShowMessage("请录入维修台数！");
						return;
					}
					if (!isNotNull(et_sbxlhs)) {
						toastShowMessage("请录入设备序列号！");
						return;
					}
					for (int i = 0; i < ll_show_xj.getChildCount(); i++) {
						LinearLayout ll = (LinearLayout) ll_show_xj
								.getChildAt(i);
						if (ll.getChildAt(1) instanceof LinearLayout) {
							ll = (LinearLayout) ll.getChildAt(1);
							if (ll.getChildAt(0) instanceof RadioGroup) {
								RadioGroup rg = (RadioGroup) ll.getChildAt(0);
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
					if (cb_xzpj.isChecked()) {
						boolean flag_pj = false;
						for (String mxh : filemap.keySet()) {
							if(mxh.equals(zbh)){
								flag_pj = true;
							}
						}
						if(!flag_pj){
							toastShowMessage("请上传配件照片！");
							return;
						}
						if (data_xzpj == null || data_xzpj.size() == 0) {
							toastShowMessage("请选择备件！");
							return;
						} else {
							showProgressDialog();
							Config.getExecutorService().execute(new Runnable() {

								@Override
								public void run() {
									getWebService("submit");
								}
							});
						}
					} else {
						dialogShowMessage("本次服务未更换配件，是否继续提交？", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								
								showProgressDialog();
								Config.getExecutorService().execute(new Runnable() {

									@Override
									public void run() {
										getWebService("submit");
									}
								});
							}
						},null);
					}
					break;
				default:
					break;
				}

			}
		};

		topBack.setOnClickListener(backonClickListener);
		cancel.setOnClickListener(backonClickListener);
		confirm.setOnClickListener(backonClickListener);
		
		tv_pjtp.setTag(zbh);
		tv_pjtp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tv_curr = (TextView) v;
				ArrayList<String> list = filemap.get(zbh);
				camera(1, list);
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

		OnItemSelectedListener onItemSelectedListener_gzdl = new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				String select_id = data_gzbm.get(arg2).get("id");
				gzbm_2_list.clear();
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", "");
				map.put("name", "");
				gzbm_2_list.add(map);
				if (!"".equals(select_id)) {
					// 选择的大类 设置中类
					for (int i = 0; i < data_all.size(); i++) {

						String parent_id = data_all.get(i).get("parent");
						if (parent_id.startsWith(select_id)) {
							// 相等添加到维护厂商显示的list里
							gzbm_2_list.add(data_all.get(i));
						}
					}
				}
				SimpleAdapter adapter = new SimpleAdapter(FwbgKdgWxEcsm.this,
						gzbm_2_list, R.layout.spinner_item, from, to);
				spinner_gzzl.setAdapter(adapter);

				if (zlbm != null) {
					for (int i = 0; i < gzbm_2_list.size(); i++) {
						map = gzbm_2_list.get(i);
						if (zlbm.equals(map.get("id"))) {
							spinner_gzzl.setSelection(i);
							zlbm = null;
							break;
						}
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		};

		OnItemSelectedListener onItemSelectedListener_gzzl = new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				String select_id = gzbm_2_list.get(arg2).get("id");
				gzbm_3_list.clear();
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", "");
				map.put("name", "");
				gzbm_3_list.add(map);
				if (!"".equals(select_id)) {
					for (int i = 0; i < data_all.size(); i++) {

						String parent_id = data_all.get(i).get("parent");
						if (parent_id.startsWith(select_id)) {
							// 相等添加到维护厂商显示的list里
							gzbm_3_list.add(data_all.get(i));
						}
					}
				}
				SimpleAdapter adapter = new SimpleAdapter(FwbgKdgWxEcsm.this,
						gzbm_3_list, R.layout.spinner_item, from, to);
				spinner_gzxl.setAdapter(adapter);

				if (xlbm != null) {
					for (int i = 0; i < gzbm_3_list.size(); i++) {
						map = gzbm_3_list.get(i);
						if (xlbm.equals(map.get("id"))) {
							spinner_gzxl.setSelection(i);
							xlbm = null;
							break;
						}
					}
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		};

		spinner_gzdl.setOnItemSelectedListener(onItemSelectedListener_gzdl);// 故障类别
																			// 大类
		spinner_gzzl.setOnItemSelectedListener(onItemSelectedListener_gzzl);// 故障类别
																			// 中类

		spinner_gzxl.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				data_xzpj = new ArrayList<Map<String, String>>();
				tv_xzpj.setText("");
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		tv_xzpj.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("".equals(gzbm_3_list.get(
						spinner_gzxl.getSelectedItemPosition()).get("id"))) {
					toastShowMessage("请选择故障类别！");
					return;
				}

				String dlid = ((Map<String, String>) spinner_gzdl
						.getSelectedItem()).get("id");
				String zlid = ((Map<String, String>) spinner_gzzl
						.getSelectedItem()).get("id");
				String xlid = ((Map<String, String>) spinner_gzxl
						.getSelectedItem()).get("id");
				Intent intent = new Intent(getApplicationContext(),
						AddPartsList.class);
				intent.putExtra("data", data_xzpj);
				intent.putExtra("ds", ds);
				intent.putExtra("dlid", dlid);
				intent.putExtra("zlid", zlid);
				intent.putExtra("xlid", xlid);
				intent.putExtra("zbh", zbh);
				intent.putExtra("ywhy_bm", ywhy_bm);
				startActivityForResult(intent, 3);
			}

		});

//		cb_xzpj.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView,
//					boolean isChecked) {
//				if (!isChecked) {
//					et_bjsfbz.setText("0");
//				}
//
//			}
//		});

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
				if (position == 1 || position == 4) {
					et_wxts.setText(1 + "");
					et_wxts.setEnabled(false);
					et_wxts.setTextColor(getResources().getColor(R.color.gray));
					et_sbxlhs.setEnabled(false);
				} else {
					et_wxts.setText(0 + "");
					et_wxts.setEnabled(true);
					et_wxts.setTextColor(getResources().getColor(
							R.color.dlg_text_color));
					//data_sbxlh = new ArrayList<String>();
					ewm = "";
					hpbm = "";
					// et_rgsfbj.setText("0");
					et_sbxlhs.setEnabled(true);
				}
				if (isFirst){
					et_wxts.setText(wxts);
					wxts = "";
				} else{
					et_sbxlhs.setText("");
				}
				isFirst = false;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		et_sbxlhs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isNotNull(et_wxts)) {
					toastShowMessage("请录入维修台数");
					return;
				}
				int num = Integer.parseInt(et_wxts.getText().toString());
				if (num > 50 || num < 1) {
					toastShowMessage("维修台数为1-50之间的数字");
					return;
				}
				Intent intent = new Intent(getApplicationContext(), Sbxlh.class);
				intent.putExtra("num", num);
				intent.putStringArrayListExtra("data", data_sbxlh);
				intent.putStringArrayListExtra("data_search", data_sbxlh_search);
				startActivityForResult(intent, 5);
				et_sbxlhs.setText("");
			}
		});

		findViewById(R.id.iv_baidumap).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getApplicationContext(),
								BaiduMapActivity.class);
						intent.putExtra("keyStr", tv_xxdz.getText().toString()
								.trim());
						startActivity(intent);
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
		if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
			ArrayList<String> list = data.getStringArrayListExtra("imglist");
			if (list != null) {
				loadImg_xj(list);
			}
		}
		if (requestCode == 3 && resultCode == 1 && data != null) {
			try {
				data_xzpj = (ArrayList<Map<String, String>>) data
						.getSerializableExtra("list");
				String xlhs = "";
				int pjzj = 0; // 配件总价
				for (int i = 0; i < data_xzpj.size(); i++) {
					Map<String, String> map = data_xzpj.get(i);
					xlhs += map.get("hpmc") + "-" + map.get("sl") + "-"
							+ map.get("dj") + ";";
					pjzj += Integer.parseInt(map.get("sl"))
							* Integer.parseInt(map.get("dj"));
				}
				tv_xzpj.setText(xlhs);
				if (pjzj != 0) {
					et_bjsfbz.setText(pjzj + "");
				}
			} catch (Exception e) {

			}
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
	}

	@Override
	protected void getWebService(String s) {
		if (s.equals("query")) {// 提交
			try {
				data_load_yhpj = new ArrayList<Map<String, String>>();
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SBGZLB", "", "uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				data_all = new ArrayList<Map<String, String>>();
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					Map<String, String> item = new HashMap<String, String>();
					item.put("id", "");
					item.put("name", "");
					data_gzbm.add(item);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						item = new HashMap<String, String>();
						String id = temp.getString("gzbm");
						String sjbm = temp.getString("sjlb");
						item.put("id", id);
						item.put("name", temp.getString("gzmc"));
						item.put("parent", sjbm);
						if ("00".equals(sjbm)) {
							data_gzbm.add(item);
						}
						data_all.add(item);
					}

					// 已换配件信息
					jsonObject = callWebserviceImp.getWebServerInfo(
							"_PAD_KDG_FWBG_YHBJCX", zbh, "uf_json_getdata",
							this);
					flag = jsonObject.getString("flag");
					if (Integer.parseInt(flag) > 0) {
						jsonArray = jsonObject.getJSONArray("tableA");
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject temp = jsonArray.getJSONObject(i);
							item = new HashMap<String, String>();
							item.put("hpmc", temp.getString("hpmc"));
							item.put("sl", temp.getString("sl"));
							data_load_yhpj.add(item);
						}
					}
					
					// 完工情况
					jsonObject = callWebserviceImp.getWebServerInfo(
							"_PAD_KDG_FWBG_WGQKXL", "", "uf_json_getdata",
							this);
					flag = jsonObject.getString("flag");
					if (Integer.parseInt(flag) > 0) {
						jsonArray = jsonObject.getJSONArray("tableA");
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject temp = jsonArray.getJSONObject(i);
							item = new HashMap<String, String>();
							item.put("name", temp.getString("ccdnm"));
							item.put("id", temp.getString("ccd"));
							data_wgqk.add(item);
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
						Message msg = new Message();
						msg.what = Constant.FAIL;
						handler.sendMessage(msg);
					}

					data_smyy = new ArrayList<Map<String, String>>();
					item = new HashMap<String, String>();
					item.put("id", "");
					item.put("name", "");
					data_smyy.add(item);
					// 上门原因
					jsonObject = callWebserviceImp.getWebServerInfo(
							"_PAD_KDG_FWBG_ECSMYY", ywlx2, "uf_json_getdata",
							this);
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

					// 人工费用
					jsonObject = callWebserviceImp.getWebServerInfo(
							"_PAD_NT_WX_FWBG_CXRGFY", zbh, "uf_json_getdata",
							this);
					flag = jsonObject.getString("flag");
					if (Integer.parseInt(flag) > 0) {
						jsonArray = jsonObject.getJSONArray("tableA");
						JSONObject temp = jsonArray.getJSONObject(0);
						rgfy = temp.getString("fy1");

					} else {
						rgfy = "0";
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
							item.put("path", temp.getString("path"));
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
					// msgStr = jsonObject.getString("msg");
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

		if (s.equals("getXj")) {
			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SHGL_KDG_FWBG_WXSXJ",
						zdh + "*" + zdh + "*" + zbh, "uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				data_xj = new ArrayList<Map<String, String>>();
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, String> item = new HashMap<String, String>();
						zbh_xj = temp.getString("zbh");
						item.put("tzlmc", temp.getString("tzlmc"));
						item.put("kzzf1", temp.getString("kzzf1"));
						item.put("kzsz1", temp.getString("kzsz1"));
						item.put("str", temp.getString("str"));
						item.put("mxh", temp.getString("mxh"));
						item.put("tzz", temp.getString("tzz"));
						item.put("kzzf4", temp.getString("kzzf4"));
						data_xj.add(item);
					}
					if ("".equals(zbh_xj)) {
						zbh_xj = zbh;
					}
					Message msg = new Message();
					msg.what = Constant.NUM_7;
					handler.sendMessage(msg);

				} else {
					// flag = jsonObject.getString("msg");
					Message msg = new Message();
					msg.what = Constant.NUM_7;
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
					data_oldValue.put("yysm", temp.getString("ecsmyymc"));
					data_oldValue.put("sblx", temp.getString("sblx"));
					data_oldValue.put("sbxh", temp.getString("sbxh"));
					data_oldValue.put("wxts", temp.getString("wxts"));
					data_oldValue.put("zdh", temp.getString("zdh"));
					data_oldValue.put("rgf", temp.getString("rgf"));
					data_oldValue.put("bjf", temp.getString("bjf"));
					data_oldValue.put("sbewm", temp.getString("sbewm"));
					data_oldValue.put("hpbm", temp.getString("hpbm"));
					Message msg = new Message();
					msg.what = Constant.NUM_9;
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
				str += cb_xzpj.isChecked() ? 1 : 2;
				str += "*PAM*";
				// 新增配件
				String xzpj_str = "";
				try {
					if (data_xzpj != null && data_xzpj.size() > 0) {
						for (int i = 0; i < data_xzpj.size(); i++) {
							Map<String, String> map = data_xzpj.get(i);
							String sfhs = "是".equals(map.get("sfhs")) ? "1"
									: "2";
							xzpj_str = xzpj_str + map.get("hpbm") + "#@#"
									+ map.get("sl") + "#@#" + map.get("dj")
									+ "#@#" + sfhs;
							xzpj_str = xzpj_str + "#^#";
						}
						xzpj_str = xzpj_str.substring(0, xzpj_str.length() - 3);
					} else {
						xzpj_str += "0";
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				str = str + xzpj_str;
				str += "*PAM*";
				str += cs;
				str += "*PAM*";
				str += ((Map<String, String>) spinner_gzdl.getSelectedItem())
						.get("id");
				str += "*PAM*";
				str += ((Map<String, String>) spinner_gzzl.getSelectedItem())
						.get("id");
				str += "*PAM*";
				str += ((Map<String, String>) spinner_gzxl.getSelectedItem())
						.get("id");

				clgcs = et_gzclgc.getText().toString().trim();
				sbxlhs = "";
				if (data_sbxlh != null) {
					for (int i = 0; i < data_sbxlh.size(); i++) {
						sbxlhs += data_sbxlh.get(i) + ";";
					}
					if (!"".equals(sbxlhs)) {
						sbxlhs = sbxlhs.substring(0, sbxlhs.length() - 1);
					}
				}

				str += "*PAM*";
				str += clgcs;
				str += "*PAM*";
				str += et_wxts.getText().toString();
				str += "*PAM*";
				str += sbxlhs;
				str += "*PAM*";
				str += data_sblx.get(spinner_sblx.getSelectedItemPosition())
						.get("id");
				str += "*PAM*";
				str += data_sbxh.get(spinner_sbxh.getSelectedItemPosition())
						.get("id");
				float rgsfbj = 0;
				float bjsfbz = 0;
				if (!"".equals(et_rgsfbj.getText().toString())) {
					rgsfbj = Float.parseFloat(et_rgsfbj.getText().toString());
				}
				if (!"".equals(et_bjsfbz.getText().toString())) {
					bjsfbz = Float.parseFloat(et_bjsfbz.getText().toString());
				}
				str += "*PAM*";
				str += rgsfbj;
				str += "*PAM*";
				str += bjsfbz;
				str += "*PAM*";
				str += data_wgqk.get(spinner_wgqk.getSelectedItemPosition())
						.get("id");;
				str += "*PAM*";
				str += et_ycwgbz.getText().toString();

				if ("".equals(zbh_xj)) {
					zbh_xj = zbh;
				}

				String str_xj = "";
				if ("".equals(cs_xj)) {

				} else {
					str_xj = zbh_xj + "*PAM*"
							+ DataCache.getinition().getUserId() + "*PAM*"
							+ cs_xj;
				}

				JSONObject json = this.callWebserviceImp.getWebServerInfo(
						"c#_PAD_kDG_FWBG_WXDXJ", str + "*sql*" + str_xj,
						DataCache.getinition().getUserId(), typeStr,
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
				upload_xj();
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

	private void upload_xj() {
		try {
			boolean flag = true;
			List<Map<String, String>> filelist = new ArrayList<Map<String, String>>();
			for (String mxh : filemap_xj.keySet()) {
				List<String> filepathlist = filemap_xj.get(mxh);
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
							"uf_json_setdata", zbh_xj, "c#_PAD_KDG_XJ_GDCZP");
					file.delete();
				} else {
					flag = false;
					break;
				}
			}
			if (flag) {
				uploadPj();
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
	
	private void uploadPj() {
		try {
			boolean flag = true;
			List<Map<String, String>> filelist = new ArrayList<Map<String, String>>();
			for (String mxh : filemap.keySet()) {
				if(mxh.equals(zbh)){
					List<String> filepathlist = filemap.get(mxh);
					for (int j = 0; j < filepathlist.size(); j++) {
						Map<String, String> map = new HashMap<String, String>();
						map.put("mxh", mxh);
						map.put("num", j + "");
						map.put("filepath", filepathlist.get(j));
						filelist.add(map);
					}
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
					flag = uploadPic(num, mxh, readJpeg(file),
							"uf_json_setdata", zbh, "c#_KDG_BJZP");
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
			if (data.size() == 0) {
				findViewById(R.id.ll_show_title).setVisibility(View.GONE);
				findViewById(R.id.ll_show).setVisibility(View.GONE);
			} else {
				findViewById(R.id.ll_show_title).setVisibility(View.VISIBLE);
				findViewById(R.id.ll_show).setVisibility(View.VISIBLE);
			}
			for (int i = 0; i < data.size(); i++) {
				Map<String, String> map = data.get(i);
				String title = map.get("tzlmc");
				errormsg = title;
				String type = map.get("kzzf1");
				String content = map.get("str");
				final String tzz = map.get("tzz") == null ? "" : map.get("tzz")
						.toString();
				String kzzf4 = map.get("kzzf4");
				final String path = map.get("path");
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
					// CheckBox cb = (CheckBox) view.findViewById(R.id.cb_0);
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
					// CheckBox cb = (CheckBox) view.findViewById(R.id.cb_0);
					// cb.setVisibility(View.VISIBLE);
					// if ("1".equals(kzzf4)) {
					// cb.setChecked(true);
					// }
				} else if ("3".equals(type)) {// 图片
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.include_xj_pz, null);
					Button bt_wj = (Button) view.findViewById(R.id.bt_wj);
					if(!"".equals(path)){
						bt_wj.setVisibility(View.VISIBLE);
					}
					TextView tv_name = (TextView) view
							.findViewById(R.id.tv_name);
					tv_name.setText(title+"(已上传"+content+"张)");
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
					bt_wj.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							String[] paths = path.split(",");
							ArrayList<String> arraylist = new ArrayList<String>();
							for(int i=0;i<paths.length;i++){
								arraylist.add(Constant.ImgPath+tzz+"/"+paths[i]);
							}
							Intent intent = new Intent(FwbgKdgWxEcsm.this,ShowImgActivity.class);
							intent.putStringArrayListExtra("imageUrls", arraylist);
							startActivity(intent);
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
					// CheckBox cb = (CheckBox) view.findViewById(R.id.cb_0);
					// cb.setVisibility(View.VISIBLE);
					// if ("1".equals(kzzf4)) {
					// cb.setChecked(true);
					// }
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
					// CheckBox cb = (CheckBox) view.findViewById(R.id.cb_0);
					// cb.setVisibility(View.VISIBLE);
					// if ("1".equals(kzzf4)) {
					// cb.setChecked(true);
					// }
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

	@SuppressLint({ "ResourceAsColor", "InflateParams" })
	protected void LoadSbxx_xj(ArrayList<Map<String, String>> data,
			LinearLayout ll) {
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
					tv_name.setText(title+"(已上传"+content+"张)");
					TextView tv_1 = (TextView) view.findViewById(R.id.tv_1);
					final String mxh = map.get("mxh");
					tv_1.setTag(mxh);
					tv_1.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							tv_curr = (TextView) v;
							ArrayList<String> list = filemap_xj.get(mxh);
							camera(2, list);
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

	private void loadImg_xj(final ArrayList<String> list) {
		try {
			String mxh = tv_curr.getTag().toString();
			if (list.size() > 0) {
				tv_curr.setText("继续选择");
				tv_curr.setBackgroundResource(R.drawable.btn_normal_yellow);
			} else {
				tv_curr.setText("选择图片");
				tv_curr.setBackgroundResource(R.drawable.btn_normal);
			}
			filemap_xj.put(mxh, list);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void loadYhpj() {
		LinearLayout ll = (LinearLayout) findViewById(R.id.ll_yhpj_content);
		if (data_load_yhpj.size() > 0) {
			ll.setVisibility(View.VISIBLE);
			findViewById(R.id.ll_yhpj).setVisibility(View.VISIBLE);
		} else {
			ll.setVisibility(View.GONE);
			findViewById(R.id.ll_yhpj).setVisibility(View.GONE);
		}
		for (int i = 0; i < data_load_yhpj.size(); i++) {
			Map<String, String> map = data_load_yhpj.get(i);
			View view = LayoutInflater.from(getApplicationContext()).inflate(
					R.layout.include_xj_type_5, null);
			TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
			TextView tv_val = (TextView) view.findViewById(R.id.tv_val);
			tv_name.setText("货品/数量：");
			tv_val.setText(map.get("hpmc") + "/" + map.get("sl"));
			ll.addView(view);
		}

	}

	private void loadOldValue(Map<String, String> data_oldValue) {
		dlbm = data_oldValue.get("kzzf3");
		zlbm = data_oldValue.get("kzzf4");
		xlbm = data_oldValue.get("kzzf5");
		for (int i = 0; i < data_gzbm.size(); i++) {
			Map<String, String> map = data_gzbm.get(i);
			if (dlbm.equals(map.get("id"))) {
				spinner_gzdl.setSelection(i);
			}
		}
		hpbm = data_oldValue.get("hpbm");

		for (int i = 0; i < data_smyy.size(); i++) {
			Map<String, String> map = data_smyy.get(i);
			if (data_oldValue.get("ecsmyy").equals(map.get("id"))) {
				spinner_smyy.setSelection(i);
			}
		}

		et_smyy.setText(data_oldValue.get("yysm"));
		clgcs = data_oldValue.get("clgc");
		et_gzclgc.setText(clgcs);
		sbxlhs = data_oldValue.get("zdh");
		if (sbxlhs != null && !"".equals(sbxlhs)) {
			data_sbxlh = new ArrayList<String>();
			String[] sbxlh = sbxlhs.split(";");
			for (int i = 0; i < sbxlh.length; i++) {
				data_sbxlh.add(sbxlh[i]);
			}
			if (sbxlhs.length() > 15) {
				sbxlhs = sbxlhs.substring(0, 14) + "...";
			}

		}
		et_sbxlhs.setText(sbxlhs);
		isFirst = true;
		wxts = data_oldValue.get("wxts");
		sblx = data_oldValue.get("sblx");
		sbxh = data_oldValue.get("sbxh");
		for (int i = 0; i < data_sblx.size(); i++) {
			Map<String, String> map = data_sblx.get(i);
			if (sblx.equals(map.get("id"))) {
				spinner_sblx.setSelection(i);
			}
		}
		 //et_rgsfbj.setText(data_oldValue.get("rgf"));
		 //et_bjsfbz.setText(data_oldValue.get("bjf"));
		findViewById(R.id.ll_ecsm).setVisibility(View.GONE);
		findViewById(R.id.ll_ecsm_content).setVisibility(View.GONE);
		if ("K000001".equals(sblx)||"K000004".equals(sblx)) {
			et_rgsfbj.setEnabled(false);
			et_bjsfbz.setEnabled(false);
			spinner_sblx.setEnabled(false);
			spinner_sbxh.setEnabled(false);
			et_wxts.setEnabled(false);
			et_sbxlhs.setEnabled(false);
			et_sbxlhs.setTextColor(R.color.gray);
			et_rgsfbj.setTextColor(R.color.gray);
			et_bjsfbz.setTextColor(R.color.gray);
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
				dialogShowMessage_P("提交成功",
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
				SimpleAdapter adapter = new SimpleAdapter(FwbgKdgWxEcsm.this,
						data_gzbm, R.layout.spinner_item, from, to);
				spinner_gzdl.setAdapter(adapter);

				adapter = new SimpleAdapter(FwbgKdgWxEcsm.this, data_smyy,
						R.layout.spinner_item, from, to);
				spinner_smyy.setAdapter(adapter);

				adapter = new SimpleAdapter(FwbgKdgWxEcsm.this, data_sblx,
						R.layout.spinner_item, from, to);
				spinner_sblx.setAdapter(adapter);
				
				adapter = new SimpleAdapter(FwbgKdgWxEcsm.this, data_wgqk,
						R.layout.spinner_item, from, to);
				spinner_wgqk.setAdapter(adapter);
				for (int i = 0; i < data_wgqk.size(); i++) {
					Map<String, String> map = data_wgqk.get(i);
					if (wcqkbm.equals(map.get("id"))) {
						spinner_wgqk.setSelection(i);
						wcqkbm = null;
						break;
					}
				}

				adapter = new SimpleAdapter(FwbgKdgWxEcsm.this, data_lxdh,
						R.layout.spinner_item, from, to);
				spinner_lxdh.setAdapter(adapter);
				et_rgsfbj.setText(rgfy);
				LoadSbxx(data_zp, ll_show);
				loadYhpj();
				Config.getExecutorService().execute(new Runnable() {

					@Override
					public void run() {
						getWebService("getXj");
					}
				});
				Config.getExecutorService().execute(new Runnable() {

					@Override
					public void run() {
						getWebService("getOldValue");
					}
				});
				break;
			case Constant.NUM_7:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}

				LoadSbxx_xj(data_xj, ll_show_xj);
				break;
			case Constant.NUM_8:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				SimpleAdapter adapter1 = new SimpleAdapter(FwbgKdgWxEcsm.this,
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

				loadOldValue(data_oldValue);
				break;
			case 12:
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
			case 13:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P("服务报告提交成功,图片上传失败,请到服务报告修改模块中修改！",
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
