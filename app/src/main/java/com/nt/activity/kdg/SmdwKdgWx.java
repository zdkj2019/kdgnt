package com.nt.activity.kdg;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.f;
import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.activity.main.MainActivity;
import com.nt.activity.util.BaiduMapActivity;
import com.nt.cache.DataCache;
import com.nt.cache.ServiceReportCache;
import com.nt.common.Constant;
import com.nt.utils.Config;
import com.nt.utils.DataUtil;
import com.nt.utils.ImageUtil;
import com.nt.zxing.CaptureActivity;

/**
 * 快递柜-上门定位
 * 
 * @author zdkj
 *
 */
public class SmdwKdgWx extends FrameActivity {

	private Spinner spinner_lxdh, spinner_smfs, spinner_gzdl, spinner_gzzl,
			spinner_gzxl,spinner_wgqk;
	private List<Map<String, String>> data_lxdh, data_gzbm, data_all,
			gzbm_2_list, gzbm_3_list, data_smfs,data_wgqk;
	private String[] from;
	private int[] to;
	private EditText et_clgc,et_ycwgbz;
	private TextView tv_time, tv_jd, tv_wd, tv_dz, tv_hb, tv_wdmc, tv_xxdz, tv_gzxx,tv_sblx,tv_bz;
	private Button confirm, cancel;
	private String flag, zbh, msgStr, bzsj, bzr, lxdh;
	private ImageView iv_telphone;
	private BDLocation location;
	private LocationClient mLocClient;
	private BDLocationListener myListener = new MyLocationListener();
	private boolean hasDw = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_kdg_smdw_wx);
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
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_jd = (TextView) findViewById(R.id.tv_jd);
		tv_wd = (TextView) findViewById(R.id.tv_wd);
		tv_dz = (TextView) findViewById(R.id.tv_dz);
		tv_hb = (TextView) findViewById(R.id.tv_hb);
		tv_sblx = (TextView) findViewById(R.id.tv_sblx);
		tv_wdmc = (TextView) findViewById(R.id.tv_wdmc);
		tv_xxdz = (TextView) findViewById(R.id.tv_xxdz);
		tv_gzxx = (TextView) findViewById(R.id.tv_gzxx);
		tv_bz = (TextView) findViewById(R.id.tv_bz);
		iv_telphone = (ImageView) findViewById(R.id.iv_telphone);
		spinner_lxdh = (Spinner) findViewById(R.id.spinner_lxdh);
		spinner_smfs = (Spinner) findViewById(R.id.spinner_smfs);
		et_clgc = (EditText) findViewById(R.id.et_clgc);
		et_ycwgbz = (EditText) findViewById(R.id.et_ycwgbz);
		spinner_gzdl = (Spinner) findViewById(R.id.spinner_gzdl);
		spinner_gzzl = (Spinner) findViewById(R.id.spinner_gzzl);
		spinner_gzxl = (Spinner) findViewById(R.id.spinner_gzxl);
		spinner_wgqk = (Spinner) findViewById(R.id.spinner_wgqk);

		data_gzbm = new ArrayList<Map<String, String>>();
		gzbm_2_list = new ArrayList<Map<String, String>>();
		gzbm_3_list = new ArrayList<Map<String, String>>();
		data_wgqk = new ArrayList<Map<String,String>>();

		from = new String[] { "id", "name" };
		to = new int[] { R.id.bm, R.id.name };
		final Map<String, Object> itemmap = ServiceReportCache.getObjectdata()
				.get(ServiceReportCache.getIndex());

		zbh = itemmap.get("zbh").toString();
		bzsj = itemmap.get("bzsj").toString();
		bzr = itemmap.get("khlxr").toString();
		lxdh = itemmap.get("lxdh").toString();
		tv_hb.setText(itemmap.get("ywhy").toString());
		tv_wdmc.setText(itemmap.get("xqmc").toString());
		tv_sblx.setText(itemmap.get("sblx_mc").toString());
		tv_xxdz.setText("   "+itemmap.get("xxdz").toString());
		tv_gzxx.setText(itemmap.get("gzxx").toString());
		tv_bz.setText(itemmap.get("bz").toString());

		data_smfs = new ArrayList<Map<String, String>>();
		Map<String, String> item = new HashMap<String, String>();
		item.put("id", "");
		item.put("name", "");
		data_smfs.add(item);
		item = new HashMap<String, String>();
		item.put("id", "1");
		item.put("name", "人工上门");
		data_smfs.add(item);
		item = new HashMap<String, String>();
		item.put("id", "2");
		item.put("name", "电话完工");
		data_smfs.add(item);

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
				Config.getExecutorService().execute(new Runnable() {

					@Override
					public void run() {
						getWebService("submitdh");
					}
				});
			}
		});

		mLocClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocClient.registerLocationListener(myListener); // 注册监听函数

		setLocationClientOption();
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
					 if(tv_jd.getText().toString().indexOf("4.9E")!=-1){
						 dialogShowMessage_P("定位失败，请到开阔地重试", null);
						 return;
					 }
					String smfsbm = data_smfs.get(spinner_smfs.getSelectedItemPosition()).get("id");
					if("".equals(smfsbm)){
						toastShowMessage("请选择上门方式");
						return;
					}
					if("2".equals(smfsbm)){//电话完工
						if ("".equals(gzbm_3_list.get(
								spinner_gzxl.getSelectedItemPosition()).get("id"))) {
							toastShowMessage("请选择故障类别！");
							return;
						}
						if (!isNotNull(et_clgc)) {
							toastShowMessage("请录入故障处理过程！");
							return;
						}
						showProgressDialog();
						Config.getExecutorService().execute(new Runnable() {

							@Override
							public void run() {
								getWebService("submitdhwg");
							}
						});
					}else{
						long now = new Date().getTime();
						long sj = DataUtil.StringToDate(bzsj).getTime() + 15 * 60 * 1000;
						if (now < sj) {
							toastShowMessage("时间未到，不能定位。");
							return;
						}
						if (hasDw) {
							showProgressDialog();
							Config.getExecutorService().execute(new Runnable() {

								@Override
								public void run() {
									getWebService("submit");
								}
							});
						} else {
							toastShowMessage("定位中，请稍后......");
						}
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

		spinner_smfs.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 2) {
					findViewById(R.id.ll_gzxx).setVisibility(View.VISIBLE);
					findViewById(R.id.ll_gzxx_content).setVisibility(
							View.VISIBLE);
				} else {
					findViewById(R.id.ll_gzxx).setVisibility(View.GONE);
					findViewById(R.id.ll_gzxx_content).setVisibility(View.GONE);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

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
				if(!"".equals(select_id)){
					// 选择的大类 设置中类
					for (int i = 0; i < data_all.size(); i++) {

						String parent_id = data_all.get(i).get("parent");
						if (parent_id.startsWith(select_id)) {
							// 相等添加到维护厂商显示的list里
							gzbm_2_list.add(data_all.get(i));
						}
					}
				}
				SimpleAdapter adapter = new SimpleAdapter(SmdwKdgWx.this,
						gzbm_2_list, R.layout.spinner_item, from, to);
				spinner_gzzl.setAdapter(adapter);
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
				if(!"".equals(select_id)){
					for (int i = 0; i < data_all.size(); i++) {

						String parent_id = data_all.get(i).get("parent");
						if (parent_id.startsWith(select_id)) {
							// 相等添加到维护厂商显示的list里
							gzbm_3_list.add(data_all.get(i));
						}
					}
				}
				SimpleAdapter adapter = new SimpleAdapter(SmdwKdgWx.this,
						gzbm_3_list, R.layout.spinner_item, from, to);
				spinner_gzxl.setAdapter(adapter);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		};

		spinner_gzdl.setOnItemSelectedListener(onItemSelectedListener_gzdl);// 故障大类
		spinner_gzzl.setOnItemSelectedListener(onItemSelectedListener_gzzl);// 故障中类
		
		findViewById(R.id.iv_baidumap).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						BaiduMapActivity.class);
				intent.putExtra("keyStr", tv_xxdz.getText().toString().trim());
				startActivity(intent);
			}
		});
	}

	@Override
	protected void getWebService(String s) {
		if (s.equals("query")) {
			try {
				data_lxdh = new ArrayList<Map<String, String>>();
				Map<String, String> item = new HashMap<String, String>();
				item.put("id", bzr);
				item.put("name", lxdh);
				data_lxdh.add(item);
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_KDG_BZRLXDH", zbh, "uf_json_getdata", this);
				flag = jsonObject.getString("flag");

				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
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
				
				// 完工情况
				jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_KDG_FWBG_WGQKXL", "", "uf_json_getdata",
						this);
				flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						item = new HashMap<String, String>();
						item.put("name", temp.getString("ccdnm"));
						item.put("id", temp.getString("ccd"));
						data_wgqk.add(item);
					}
				}

				jsonObject = callWebserviceImp.getWebServerInfo("_PAD_SBGZLB",
						"", "uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				data_all = new ArrayList<Map<String, String>>();
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					item = new HashMap<String, String>();
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

		if (s.equals("submitdh")) {// 提交
			try {
				String sql = "update kdg_pgzb set fgsl= (nvl(fgsl,0)+1), kzsj2 = nvl(kzsj2,sysdate),zdrz = nvl(zdrz,'') ||to_char(sysdate,'yyyymmdd hh24:mi:ss')|| ';'  where zbh = '"
						+ zbh + "'";
				JSONObject json = callWebserviceImp.getWebServerInfo("_RZ",
						sql, "", "uf_json_setdata", this);
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;
				handler.sendMessage(msg);
			}
		}

		if (s.equals("submit")) {// 提交
			try {

				String typeStr = "smdy";
				msgStr = "定位成功！";
				String str = zbh + "*PAM*" + DataCache.getinition().getUserId();
				str += "*PAM*";
				str += tv_jd.getText().toString();
				str += "*PAM*";
				str += tv_wd.getText().toString();
				str += "*PAM*";
				str += tv_dz.getText().toString();
				

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
		
		if (s.equals("submitdhwg")) {// 提交
			try {
				String str = zbh + "*PAM*";
				str += ((Map<String, String>) spinner_gzdl.getSelectedItem())
						.get("id");
				str += "*PAM*";
				str += ((Map<String, String>) spinner_gzzl.getSelectedItem())
						.get("id");
				str += "*PAM*";
				str += ((Map<String, String>) spinner_gzxl.getSelectedItem())
						.get("id");
				str += "*PAM*";
				str += et_clgc.getText().toString();
				str += "*PAM*";
				str += data_wgqk.get(spinner_wgqk.getSelectedItemPosition()).get("id");
				str += "*PAM*";
				str += et_ycwgbz.getText().toString();
				
				JSONObject json = this.callWebserviceImp.getWebServerInfo(
						"c#_PAD_KDG_DHWG", str, "", "", "uf_json_setdata2",
						this);
				flag = json.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					msgStr = "提交成功";
					Message msg = new Message();
					msg.what = Constant.SUCCESS;
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
	}

	/*
	 * BDLocationListener接口有2个方法需要实现： 1.接收异步返回的定位结果，参数是BDLocation类型参数。
	 * 2.接收异步返回的POI查询结果，参数是BDLocation类型参数。
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation locations) {
			if (locations == null) {
				return;
			} else {
				location = locations;
				Message msg = new Message();
				msg.what = Constant.NUM_7;// 成功
				handler.sendMessage(msg);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {

		}

		@Override
		public void onConnectHotSpotMessage(String arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

	}

	/**
	 * 设置定位参数包括：定位模式（单次定位，定时定位），返回坐标类型，是否打开GPS等等。
	 */
	private void setLocationClientOption() {

		final LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(1000);// 设置发起定位请求的间隔时间为5000ms
		option.disableCache(true);// 禁止启用缓存定位
		option.setPriority(LocationClientOption.GpsFirst);
		option.setAddrType("all");
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

	@Override
	protected void onDestroy() {
		mLocClient.stop();
		super.onDestroy();
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case Constant.FAIL:
				dialogShowMessage_P("失败，请检查后重试...错误标识：" + flag, null);
				break;
			case Constant.SUCCESS:
				dialogShowMessage_P(msgStr,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								onBackPressed();
							}
						});
				break;
			case Constant.NETWORK_ERROR:
				dialogShowMessage_P(Constant.NETWORK_ERROR_STR, null);
				break;
			case Constant.NUM_6:
				SimpleAdapter adapter = new SimpleAdapter(SmdwKdgWx.this,
						data_lxdh, R.layout.spinner_item, from, to);
				spinner_lxdh.setAdapter(adapter);

				adapter = new SimpleAdapter(SmdwKdgWx.this, data_smfs,
						R.layout.spinner_item, from, to);
				spinner_smfs.setAdapter(adapter);

				adapter = new SimpleAdapter(SmdwKdgWx.this, data_gzbm,
						R.layout.spinner_item, from, to);
				spinner_gzdl.setAdapter(adapter);
				
				adapter = new SimpleAdapter(SmdwKdgWx.this, data_wgqk,
						R.layout.spinner_item, from, to);
				spinner_wgqk.setAdapter(adapter);
				break;
			case Constant.NUM_7:
				tv_time.setText(location.getTime());
				tv_jd.setText("" + location.getLongitude());
				tv_wd.setText("" + location.getLatitude());
				tv_dz.setText("" + location.getAddrStr());
				mLocClient.stop();
				hasDw = true;
				break;
			}

			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		}
	};
}