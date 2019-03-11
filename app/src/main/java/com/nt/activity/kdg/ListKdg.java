package com.nt.activity.kdg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.activity.esp.ScxjsbxgActivity;
import com.nt.activity.esp.SheBeiPDActivity;
import com.nt.activity.kc.KccxShowActivity;
import com.nt.activity.kc.KcpdActivity;
import com.nt.activity.kc.ThcxShowActivity;
import com.nt.activity.login.LoginActivity;
import com.nt.activity.w.CcgdActivity;
import com.nt.activity.w.Pqzgjk;
import com.nt.activity.w.YjgdActivity;
import com.nt.cache.DataCache;
import com.nt.cache.ServiceReportCache;
import com.nt.common.Constant;
import com.nt.utils.Config;
import com.nt.utils.DataUtil;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;

/**
 * 快递柜-通用List
 * 
 * @author zdkj
 *
 */
public class ListKdg extends FrameActivity {

	private String flag;
	private ListView listView;
	private SimpleAdapter adapter;
	private List<Map<String, Object>> datalist;
	private String[] from;
	private int[] to;
	private int queryType = DataCache.getinition().getQueryType();
	private String cs, status = "1";
	private SharedPreferences spf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_dispatchinginformationreceiving);
		initVariable();
		initView();
		initListeners();
		

	}

	@Override
	protected void initVariable() {

		listView = (ListView) findViewById(R.id.listView);
		from = new String[] { "textView1", "faultuser", "zbh", "timemy",
				"datemy", "ztzt" };
		to = new int[] { R.id.textView1, R.id.yytmy, R.id.pgdhmy, R.id.timemy,
				R.id.datemy, R.id.ztzt };
		spf = getSharedPreferences("loginsp", LoginActivity.MODE_PRIVATE);
	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());
	}

	@Override
	protected void initListeners() {
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.bt_topback:
					onBackPressed();
					finish();
					break;

				}
			}
		};

		topBack.setOnClickListener(onClickListener);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				ServiceReportCache.setIndex(position);
				if (position >= 0) {
					Intent intent = null;
					if (queryType == 201) {
						intent = new Intent(ListKdg.this, JdxyKdg.class);
					} else if (queryType == 203) {
						intent = new Intent(ListKdg.this, SmdwKdg.class);
					} else if (queryType == 204) {
						intent = new Intent(ListKdg.this, FwbgKdg.class);
					} else if (queryType == 2041) {
						intent = new Intent(ListKdg.this, FwbgXgKdg.class);
					} else if (queryType == 2042) {
						intent = new Intent(ListKdg.this, FwbgKdgEcsm.class);
					} else if (queryType == 205) {
						intent = new Intent(ListKdg.this, JqgdcxShowKdg.class);
					} else if (queryType == 2301) {
						intent = new Intent(ListKdg.this, JdxyXj.class);
					} else if (queryType == 2302) {
						intent = new Intent(ListKdg.this, SmdwXj.class);
					} else if (queryType == 2303) {
						intent = new Intent(ListKdg.this, FwbgXj.class);
					} else if (queryType == 2304) {
						intent = new Intent(ListKdg.this, FwbgXgXj.class);
					} else if (queryType == 2305) {
						intent = new Intent(ListKdg.this, SmdwXj.class);
					} else if (queryType == 2306) {
						intent = new Intent(ListKdg.this, FwbgXjWd.class);
						intent.putExtra("type", "m_wdxjbg_sc");
					} else if (queryType == 2307) {
						intent = new Intent(ListKdg.this, FwbgXjWd.class);
						intent.putExtra("type", "m_wdxjbg_xg");
					} else if (queryType == 2101) {
						intent = new Intent(ListKdg.this, DhjcKdg.class);
					} else if (queryType == 2201) {
						intent = new Intent(ListKdg.this, JdxyKdgWx.class);
					} else if (queryType == 2202) {
						intent = new Intent(ListKdg.this, SmdwKdgWx.class);
					} else if (queryType == 2203) {
						intent = new Intent(ListKdg.this, FwbgKdgWx.class);
					} else if (queryType == 2204) {
						intent = new Intent(ListKdg.this, ZzzpKdgWx.class);
					} else if (queryType == 2205) {
						intent = new Intent(ListKdg.this, FwbgXgKdgWx.class);
					} else if (queryType == 2206) {
						intent = new Intent(ListKdg.this, FwbgKdgWxEcsm.class);
					} else if (queryType == 2207) {
						intent = new Intent(ListKdg.this, SbxxlrKdg.class);
					} else if (queryType == 2208) {
						intent = new Intent(ListKdg.this, PjxgKdgWx.class);
					} else if (queryType == 2209) {
						intent = new Intent(ListKdg.this, P2cslr.class);
					} else if (queryType == 2501) {
						intent = new Intent(ListKdg.this, Pqzgjk.class);
					} else if (queryType == 206) {
						intent = new Intent(ListKdg.this, YjgdActivity.class);
					} else if (queryType == 207) {
						intent = new Intent(ListKdg.this, CcgdActivity.class);
					}else if (queryType == 2801) {
						intent = new Intent(ListKdg.this, ThcxShowActivity.class);
					}else if (queryType == 2802) {
						intent = new Intent(ListKdg.this, KcpdActivity.class);
					}else if (queryType == 2901) {
						intent = new Intent(ListKdg.this, ScxjsbxgActivity.class);
					}
					startActivityForResult(intent, 1);
				}

			}
		});

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {

				getWebService("query");
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
			
		}
	}

	@Override
	protected void getWebService(String s) {

		if ("query".equals(s)) {
			try {
				datalist = new ArrayList<Map<String, Object>>();
				String sqlid = "";
				int queryType = DataCache.getinition().getQueryType();
				String userid = spf.getString("userId", "");
				cs = userid;
				if (queryType == 201) {
					sqlid = "_PAD_SHGL_KDG_AZ_JDXY";
				} else if (queryType == 202) {
					sqlid = "";
				} else if (queryType == 203) {
					sqlid = "_PAD_SHGL_KDG_AZ_SMDW";
				} else if (queryType == 204) {
					sqlid = "_PAD_SHGL_KDG_AZ_FWBG";
				} else if (queryType == 2041) {
					sqlid = "_PAD_KDGAZ_FWBG_XG";
				} else if (queryType == 2042) {
					sqlid = "_PAD_SHGL_KDG_AZ_ECSM";
				} else if (queryType == 205) {
					status = getIntent().getStringExtra("status");
					if ("1".equals(status)) { // 已完工
						sqlid = "_PAD_SHGL_KDG_GDALL";
						cs = userid + "*" + userid;
					} else {// 未完工
						sqlid = "_PAD_SHGL_KDG_GDALL_A";
						cs = userid + "*" + userid;
					}
				} else if (queryType == 2301) {
					sqlid = "_PAD_KDG_XJ_JDXY";
				} else if (queryType == 2302) {
					sqlid = "_PAD_KDG_XJ_SMDW";
				} else if (queryType == 2303) {
					sqlid = "_PAD_KDG_XJ_FWBG";
				} else if (queryType == 2304) {
					sqlid = "_PAD_KDG_XJ_FWBG_XG";
				} else if (queryType == 2305) {
					sqlid = "_PAD_KDG_XJ_SMDW_ZD";
					cs = getIntent().getStringExtra("status");
				} else if (queryType == 2306) {
					sqlid = "_PAD_SHGL_TDXJ_FWBG_SC";
				} else if (queryType == 2307) {
					sqlid = "_PAD_SHGL_TDXJ_FWBG_XG";
				} else if (queryType == 2101) {
					sqlid = "_PAD_SHGL_KDG_AZ_DHJC";
				} else if (queryType == 2201) {
					sqlid = "_PAD_SHGL_KDG_WX_JDXY";
				} else if (queryType == 2202) {
					sqlid = "_PAD_SHGL_KDG_WX_SMDW";
				} else if (queryType == 2203) {
					sqlid = "_PAD_SHGL_KDG_WX_FWBG";
				} else if (queryType == 2204) {
					cs = userid + "*" + userid;
					sqlid = "_PAD_SHGL_KDG_ZZZP";
				} else if (queryType == 2205) {
					sqlid = "_PAD_KDGAZ_FWBG_XG2";
				} else if (queryType == 2206) {
					cs = userid;
					sqlid = "_PAD_SHGL_KDG_WX_ECSM";
				} else if (queryType == 2207) {
					cs = userid;
					sqlid = "_PAD_SHGL_KDG_EWMSM";
				} else if (queryType == 2208) {
					cs = userid;
					sqlid = "_PAD_KDG_BJXG_GDXX";
				} else if (queryType == 2209) {
					cs = userid;
					sqlid = "_PAD_SHGL_GDCSYYLY_P2";
				} else if (queryType == 2501) {

					int status = getIntent().getIntExtra("status", 1);
					int sj = getIntent().getIntExtra("sj", 1);
					if (status == 1) {// 已完工
						sqlid = "_PAD_KDG_ZZPQGD_YWG";
						if (sj == 1) {// 本月
							cs = userid + "*" + userid + "*" + userid + "*"
									+ userid + "*0*0";
						} else {// 上月
							cs = userid + "*" + userid + "*" + userid + "*"
									+ userid + "*-1*-1";
						}

					} else {// 未完工
						sqlid = "_PAD_KDG_ZZPQGD_WWG";
						cs = userid + "*" + userid + "*" + userid + "*"
								+ userid;
					}

				} else if (queryType == 206) {
					sqlid = "_PAD_YWCX_TSYJ";
					cs = userid;
				} else if (queryType == 207) {
					sqlid = "_PAD_SHGL_CCGDCX";
					cs = userid;
				} else if (queryType == 2801) {
					sqlid = "_PAD_DBCK_THCX1";
					cs = userid+"*"+userid;
				} else if (queryType == 2802) {
					sqlid = "_PAD_CCGL_KFPD_ZB";
					cs = userid;
				} else if (queryType == 2901) {
					sqlid = "_PAD_CCGL_SCXJ_NSCH";
					cs = userid;
				}

				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						sqlid, cs, "uf_json_getdata", this);
				flag = jsonObject.getString("flag");

				JSONArray jsonArray = jsonObject.getJSONArray("tableA");
				if (Integer.parseInt(flag) > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, Object> item = new HashMap<String, Object>();
						String timeff = "";
						String yh = "";
						if (queryType == 201) {
							try {
								yh = temp.getString("ywhy_bm");
							} catch (Exception e) {
							}
							item.put("textView1", getListItemIcon(yh));
							timeff = temp.getString("bzsj");
							item.put("bzsj", timeff);
							item.put("faultuser", temp.getString("xqmc")
									+ "   " + temp.getString("xxdz"));
							item.put("zbh", temp.getString("zbh"));
							item.put("sx", temp.getString("sx"));
							item.put("qy", temp.getString("qy"));
							item.put("djzt", temp.getString("djzt"));
							item.put("xqmc", temp.getString("xqmc"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("gzxx", temp.getString("gzxx"));
							item.put("ywlx", temp.getString("ywlx"));
							item.put("ywlx2", temp.getString("ywlx2"));
							item.put("zdh", temp.getString("zdh"));
							item.put("bz", temp.getString("bz"));
							item.put("khlxr", temp.getString("khlxr"));
							item.put("lxdh", temp.getString("lxdh"));
							item.put("sf", temp.getString("sf"));
							item.put("sflx", temp.getString("sflx"));
						}
						if (queryType == 203) {
							try {
								yh = temp.getString("ywhy_bm");
							} catch (Exception e) {
							}
							item.put("textView1", getListItemIcon(yh));
							timeff = temp.getString("bzsj");
							item.put("bzsj", timeff);
							item.put("faultuser", temp.getString("xqmc")
									+ "   " + temp.getString("xxdz"));
							item.put("zbh", temp.getString("zbh"));
							item.put("sx", temp.getString("sx"));
							item.put("qy", temp.getString("qy"));
							item.put("djzt", temp.getString("djzt"));
							item.put("xqmc", temp.getString("xqmc"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("gzxx", temp.getString("gzxx"));
							item.put("ywlx", temp.getString("ywlx"));
							item.put("ywlx2", temp.getString("ywlx2"));
							item.put("zdh", temp.getString("zdh"));
							item.put("bz", temp.getString("bz"));
							item.put("sbewm", temp.getString("sbewm"));
							item.put("lxdh", temp.getString("lxdh"));
							item.put("ywhy", temp.getString("ywhy"));
							item.put("khlxr", temp.getString("khlxr"));
							item.put("yysj", temp.getString("yysj"));
						}
						
						if (queryType == 204) {
							try {
								yh = temp.getString("ywhy_bm");
							} catch (Exception e) {
							}
							item.put("textView1", getListItemIcon(yh));
							timeff = temp.getString("bzsj");
							item.put("bzsj", timeff);
							item.put("faultuser", temp.getString("xqmc")
									+ "   " + temp.getString("xxdz"));
							item.put("zbh", temp.getString("zbh"));
							item.put("sx", temp.getString("sx"));
							item.put("qy", temp.getString("qy"));
							item.put("djzt", temp.getString("djzt"));
							item.put("xqmc", temp.getString("xqmc"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("gzxx", temp.getString("gzxx"));
							item.put("ywlx", temp.getString("ywlx"));
							item.put("ywlx2", temp.getString("ywlx2"));
							item.put("zdh", temp.getString("zdh"));
							item.put("bz", temp.getString("bz"));
							item.put("sfecgd", temp.getString("sfecgd"));
							item.put("sbewm", temp.getString("sbewm"));
							item.put("dygdh", temp.getString("dygdh"));
							item.put("fwnr", temp.getString("fwnr"));
							item.put("sblx", temp.getString("sblx"));
							item.put("sbxh", temp.getString("sbxh"));
							item.put("sf", temp.getString("sf"));
							item.put("ywhy", temp.getString("ywhy"));
							item.put("sflx", temp.getString("sflx"));
							item.put("lxdh", temp.getString("lxdh"));
							item.put("khlxr", temp.getString("khlxr"));
							item.put("gxh", temp.getString("gxh"));
							item.put("jgh", temp.getString("jgh"));
							item.put("anlx2", temp.getString("anlx2"));
						}
						if (queryType == 2042) {
							try {
								yh = temp.getString("ywhy_bm");
							} catch (Exception e) {
							}
							item.put("textView1", getListItemIcon(yh));
							timeff = temp.getString("bzsj");
							item.put("bzsj", timeff);
							item.put("faultuser", temp.getString("xqmc")
									+ "   " + temp.getString("xxdz"));
							item.put("zbh", temp.getString("zbh"));
							item.put("sx", temp.getString("sx"));
							item.put("qy", temp.getString("qy"));
							item.put("djzt", temp.getString("djzt"));
							item.put("xqmc", temp.getString("xqmc"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("gzxx", temp.getString("gzxx"));
							item.put("ywlx", temp.getString("ywlx"));
							item.put("ywlx2", temp.getString("ywlx2"));
							item.put("zdh", temp.getString("zdh"));
							item.put("bz", temp.getString("bz"));
							item.put("sfecgd", temp.getString("sfecgd"));
							item.put("sbewm", temp.getString("sbewm"));
							item.put("dygdh", temp.getString("dygdh"));
							item.put("fwnr", temp.getString("fwnr"));
							item.put("sblx", temp.getString("sblx"));
							item.put("sbxh", temp.getString("sbxh"));
							item.put("sf", temp.getString("sf"));
							item.put("ywhy", temp.getString("ywhy"));
							item.put("sflx", temp.getString("sflx"));
							item.put("lxdh", temp.getString("lxdh"));
							item.put("khlxr", temp.getString("khlxr"));
						}
						if (queryType == 2041) {
							try {
								yh = temp.getString("ywhy_bm");
							} catch (Exception e) {
							}
							item.put("textView1", getListItemIcon(yh));
							timeff = temp.getString("bzsj");
							item.put("bzsj", timeff);
							item.put("faultuser", temp.getString("xqmc")
									+ "   " + temp.getString("xxdz"));
							item.put("zbh", temp.getString("zbh"));
							item.put("sx", temp.getString("sx"));
							item.put("qy", temp.getString("qy"));
							item.put("djzt", temp.getString("djzt"));
							item.put("xqmc", temp.getString("xqmc"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("gzxx", temp.getString("gzxx"));
							item.put("ywlx", temp.getString("ywlx"));
							item.put("ywlx2", temp.getString("ywlx2"));
							item.put("zdh", temp.getString("zdh"));
							item.put("bz", temp.getString("bz"));
							item.put("sfecgd", temp.getString("sfecgd"));
							item.put("sbewm", temp.getString("sbewm"));
							item.put("dygdh", temp.getString("dygdh"));
							item.put("fwnr", temp.getString("fwnr"));
							item.put("sblx", temp.getString("sblx"));
							item.put("sbxh", temp.getString("sbxh"));
							item.put("sf", temp.getString("sf"));
							item.put("ywhy", temp.getString("ywhy"));
//							item.put("sflx", temp.getString("sflx"));
							item.put("lxdh", temp.getString("lxdh"));
							item.put("khlxr", temp.getString("khlxr"));
							item.put("gxh", temp.getString("gxh"));
							item.put("jgh", temp.getString("jgh"));
							item.put("anlx2", temp.getString("anlx2"));
						}
						if (queryType == 2203 || queryType == 2205
								|| queryType == 2206) {
							try {
								yh = temp.getString("ywhy_bm");
							} catch (Exception e) {
							}
							item.put("textView1", getListItemIcon(yh));
							timeff = temp.getString("bzsj");
							item.put("bzsj", timeff);
							item.put("faultuser", temp.getString("xqmc")
									+ "   " + temp.getString("xxdz"));
							item.put("zbh", temp.getString("zbh"));
							item.put("sx", temp.getString("sx"));
							item.put("qy", temp.getString("qy"));
							item.put("djzt", temp.getString("djzt"));
							item.put("xqmc", temp.getString("xqmc"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("gzxx", temp.getString("gzxx"));
							item.put("ywlx", temp.getString("ywlx"));
							item.put("ywlx2", temp.getString("ywlx2"));
							item.put("zdh", temp.getString("zdh"));
							item.put("bz", temp.getString("bz"));
							item.put("fwnr", temp.getString("fwnr"));
							item.put("sf", temp.getString("sf"));
							item.put("ds", temp.getString("ds"));
							item.put("ywhy", temp.getString("ywhy"));
							item.put("ywhy_bm", temp.getString("ywhy_bm"));
							item.put("sblx", temp.getString("sblx"));
							item.put("sbxh", temp.getString("sbxh"));
							item.put("sflx", temp.getString("sflx"));
							item.put("lxdh", temp.getString("lxdh"));
							item.put("khlxr", temp.getString("khlxr"));
							item.put("axdh", temp.getString("axdh"));
							item.put("kzzf2", temp.getString("kzzf2"));
							item.put("wcqkbm", temp.getString("wcqkbm"));
							item.put("dygdh1", temp.getString("dygdh1"));
						}
						if(queryType == 2205){
							String sfcs = temp.getString("sfhf");
							if("2".equals(sfcs)){
								item.put("sfcs", "1");
							}else{
								item.put("sfcs", "0");
							}
							
						}
						
						if (queryType == 2204) {
							try {
								yh = temp.getString("ywhy_bm");
							} catch (Exception e) {
							}
							item.put("textView1", getListItemIcon(yh));
							timeff = temp.getString("bzsj");
							item.put("bzsj", timeff);
							item.put("faultuser", temp.getString("xqmc")
									+ "   " + temp.getString("xxdz"));
							item.put("zbh", temp.getString("zbh"));
							item.put("sx", temp.getString("sx"));
							item.put("qy", temp.getString("qy"));
							item.put("djzt", temp.getString("djzt"));
							item.put("xqmc", temp.getString("xqmc"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("gzxx", temp.getString("gzxx"));
							item.put("ywlx", temp.getString("ywlx"));
							item.put("ywlx2", temp.getString("ywlx2"));
							item.put("zdh", temp.getString("zdh"));
							item.put("bz", temp.getString("bz"));
							item.put("ds", temp.getString("ds"));
							item.put("sf", temp.getString("sf"));
							item.put("fwnr", temp.getString("fwnr"));
						}
						if (queryType == 2202) {
							try {
								yh = temp.getString("ywhy_bm");
							} catch (Exception e) {
							}
							item.put("textView1", getListItemIcon(yh));
							timeff = temp.getString("bzsj");
							item.put("bzsj", timeff);
							item.put("faultuser", temp.getString("xqmc")
									+ "   " + temp.getString("xxdz"));
							item.put("zbh", temp.getString("zbh"));
							item.put("sx", temp.getString("sx"));
							item.put("qy", temp.getString("qy"));
							item.put("djzt", temp.getString("djzt"));
							item.put("xqmc", temp.getString("xqmc"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("gzxx", temp.getString("gzxx"));
							item.put("ywlx", temp.getString("ywlx"));
							item.put("ywlx2", temp.getString("ywlx2"));
							item.put("zdh", temp.getString("zdh"));
							item.put("bz", temp.getString("bz"));
							item.put("sbewm", temp.getString("sbewm"));
							item.put("lxdh", temp.getString("lxdh"));
							item.put("ywhy", temp.getString("ywhy"));
							item.put("khlxr", temp.getString("khlxr"));
							item.put("sblx_mc", temp.getString("sblx_mc"));
						}
						if (queryType == 2201) {
							try {
								yh = temp.getString("ywhy_bm");
							} catch (Exception e) {
							}
							item.put("textView1", getListItemIcon(yh));
							timeff = temp.getString("bzsj");
							item.put("bzsj", timeff);
							item.put("faultuser", temp.getString("xqmc")
									+ "   " + temp.getString("xxdz"));
							item.put("zbh", temp.getString("zbh"));
							item.put("sx", temp.getString("sx"));
							item.put("qy", temp.getString("qy"));
							item.put("djzt", temp.getString("djzt"));
							item.put("xqmc", temp.getString("xqmc"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("gzxx", temp.getString("gzxx"));
							item.put("ywlx", temp.getString("ywlx"));
							item.put("ywlx2", temp.getString("ywlx2"));
							item.put("zdh", temp.getString("zdh"));
							item.put("bz", temp.getString("bz"));
							item.put("khlxr", temp.getString("khlxr"));
							item.put("lxdh", temp.getString("lxdh"));
							item.put("sf", temp.getString("sf"));
							item.put("sflx", temp.getString("sflx"));
							item.put("axdh", temp.getString("axdh"));
						}
						if (queryType == 2207) {
							try {
								yh = temp.getString("ywhy_bm");
							} catch (Exception e) {
							}
							item.put("textView1", getListItemIcon(yh));
							timeff = temp.getString("bzsj");
							item.put("bzsj", timeff);
							item.put("faultuser", temp.getString("xqmc")
									+ "   " + temp.getString("xxdz"));
							item.put("zbh", temp.getString("zbh"));
							item.put("sx", temp.getString("sx"));
							item.put("qy", temp.getString("qy"));
							item.put("djzt", temp.getString("djzt"));
							item.put("xqmc", temp.getString("xqmc"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("gzxx", temp.getString("gzxx"));
							item.put("ywlx", temp.getString("ywlx"));
							item.put("ywlx2", temp.getString("ywlx2"));
							item.put("zdh", temp.getString("zdh"));
							item.put("bz", temp.getString("bz"));
							item.put("khlxr", temp.getString("khlxr"));
							item.put("lxdh", temp.getString("lxdh"));
							item.put("sf", temp.getString("sf"));
							item.put("sflx", temp.getString("sflx"));
						}
						if (queryType == 2208) {
							try {
								yh = temp.getString("ywhy_bm");
							} catch (Exception e) {
							}
							item.put("textView1", getListItemIcon(yh));
							timeff = temp.getString("bzsj");
							item.put("bzsj", timeff);
							item.put("faultuser", temp.getString("xqmc")
									+ "   " + temp.getString("xxdz"));
							item.put("zbh", temp.getString("zbh"));
							item.put("sx", temp.getString("sx"));
							item.put("qy", temp.getString("qy"));
							item.put("djzt", temp.getString("djzt"));
							item.put("xqmc", temp.getString("xqmc"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("gzxx", temp.getString("gzxx"));
							item.put("ywlx", temp.getString("ywlx"));
							item.put("ywlx2", temp.getString("ywlx2"));
							item.put("sf", temp.getString("sf"));
							item.put("ywhy", temp.getString("ywhy"));
							item.put("ywhy_bm", temp.getString("ywhy_bm"));
							item.put("clgc", temp.getString("clgc"));
							item.put("ds", temp.getString("ds"));
							item.put("sflx", temp.getString("sflx"));
							item.put("sblx_mc", temp.getString("sblx_mc"));
							item.put("kzzf3_mc", temp.getString("kzzf3_mc"));
							item.put("kzzf4_mc", temp.getString("kzzf4_mc"));
							item.put("kzzf5_mc", temp.getString("kzzf5_mc"));
							item.put("sbxh_mc", temp.getString("sbxh_mc"));
							item.put("wcqkmc", temp.getString("wcqkmc"));
							item.put("kzzf2", temp.getString("kzzf2"));
							item.put("pjnsbz", temp.getString("pjnsbz"));
							item.put("pjthyy", temp.getString("pjthyy"));
							item.put("fy1", temp.getString("fy1"));
							item.put("fy2", temp.getString("fy2"));
							String sfcs = temp.getString("pjnsbz");
							if("2".equals(sfcs)){
								item.put("sfcs", "1");
							}else{
								item.put("sfcs", "0");
							}
						}
						
						if (queryType == 2209) {
							try {
								yh = temp.getString("ywhy_bm");
							} catch (Exception e) {
							}
							item.put("textView1", getListItemIcon(yh));
							timeff = temp.getString("bzsj");
							item.put("bzsj", timeff);
							item.put("faultuser", temp.getString("wdmc")
									+ "   " + temp.getString("xxdz"));
							item.put("zbh", temp.getString("zbh"));
							item.put("sx", temp.getString("sx"));
							item.put("qy", temp.getString("qy"));
							item.put("djzt", temp.getString("djzt"));
							item.put("xqmc", temp.getString("wdmc"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("gzxx", temp.getString("gzxx"));
							item.put("ywlx", temp.getString("ywlx"));
							item.put("ywhy", temp.getString("ywhy"));
							item.put("slsj", temp.getString("slsj"));
							item.put("ddsj", temp.getString("ddsj"));
							item.put("wcsj", temp.getString("wcsj"));
							item.put("sblx", temp.getString("sblx"));
							item.put("bz", temp.getString("bz"));
							item.put("sf", temp.getString("sf"));
							item.put("csyymc", temp.getString("csyymc"));
							item.put("csyybm", temp.getString("csyybm"));
							item.put("sfcs", temp.getString("sfcs"));
						}
						if (queryType == 2501) {
							try {
								yh = temp.getString("ywhy_bm");
							} catch (Exception e) {
							}
							item.put("textView1", getListItemIcon(yh));
							timeff = temp.getString("bzsj");
							item.put("bzsj", timeff);
							item.put("faultuser", temp.getString("xqmc")
									+ "   " + temp.getString("xxdz"));
							item.put("zbh", temp.getString("zbh"));
							item.put("sx", temp.getString("sx"));
							item.put("qy", temp.getString("qy"));
							item.put("djzt", temp.getString("djzt"));
							item.put("xqmc", temp.getString("xqmc"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("gzxx", temp.getString("gzxx"));
							item.put("ywlx", temp.getString("ywlx"));
							item.put("ywlx2", temp.getString("ywlx2"));
							item.put("zdh", temp.getString("zdh"));
							item.put("bz", temp.getString("bz"));
							item.put("jbf", temp.getString("jbf"));
							item.put("jbfdh", temp.getString("jbfdh"));
							item.put("sfcs", temp.getString("cstxqk"));
							item.put("csyylx", temp.getString("csyylx"));
							item.put("csnr", temp.getString("csnr"));
							item.put("scsj", temp.getString("scsj"));
							item.put("ddsj", temp.getString("ddsj"));
							item.put("sfwzzm", temp.getString("sfwzzm"));
							item.put("wcsj", temp.getString("wcsj"));
							item.put("sf", temp.getString("sf"));
							item.put("ywhy", temp.getString("ywhy"));
							item.put("sflx", temp.getString("sflx"));
							item.put("sblx_mc", temp.getString("sblx_mc"));
							item.put("sbxh_mc", temp.getString("sbxh_mc"));
							item.put("wxts", temp.getString("wxts"));
							item.put("wgfs", temp.getString("wgfs"));
							item.put("axdh", temp.getString("axdh"));
							item.put("rgfy", temp.getString("fy1"));
							item.put("pjfy", temp.getString("fy2"));
						}
						if (queryType == 205) {
							try {
								yh = temp.getString("ywhy_bm");
							} catch (Exception e) {
							}
							item.put("textView1", getListItemIcon(yh));
							timeff = temp.getString("bzsj");
							item.put("bzsj", timeff);
							item.put("faultuser", temp.getString("xqmc")
									+ "   " + temp.getString("xxdz"));
							item.put("zbh", temp.getString("zbh"));
							item.put("sx", temp.getString("sx"));
							item.put("qy", temp.getString("qy"));
							item.put("djzt", temp.getString("djzt"));
							item.put("xqmc", temp.getString("xqmc"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("gzxx", temp.getString("gzxx"));
							item.put("ywlx", temp.getString("ywlx"));
							item.put("ywlx2", temp.getString("ywlx2"));
							item.put("zdh", temp.getString("zdh"));
							item.put("bz", temp.getString("bz"));
							item.put("sf", temp.getString("sf"));
							item.put("ywhy", temp.getString("ywhy"));
							item.put("sflx", temp.getString("sflx"));
							item.put("sblx_mc", temp.getString("sblx_mc"));
							item.put("sbxh_mc", temp.getString("sbxh_mc"));
							item.put("wxts", temp.getString("wxts"));
							item.put("wgfs", temp.getString("wgfs"));
							item.put("wgsj", temp.getString("wcsj"));
							item.put("axdh", temp.getString("axdh"));
							item.put("rgfy", temp.getString("fy1"));
							item.put("pjfy", temp.getString("fy2"));
						}
						
						if (queryType == 206) {
							try {
								yh = temp.getString("ywhy_bm");
							} catch (Exception e) {
							}
							item.put("textView1", getListItemIcon(yh));
							timeff = temp.getString("bzsj");
							item.put("bzsj", timeff);
							item.put("faultuser", temp.getString("xqmc")
									+ "   " + temp.getString("xxdz"));
							item.put("zbh", temp.getString("zbh"));
							item.put("sx", temp.getString("sx"));
							item.put("ds", temp.getString("ds"));
							item.put("qy", temp.getString("qy"));
							item.put("djzt", temp.getString("djzt"));
							item.put("xqmc", temp.getString("xqmc"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("gzxx", temp.getString("gzxx"));
							item.put("ywlx", temp.getString("ywlx"));
							item.put("bz", temp.getString("bz"));
							item.put("khlxr", temp.getString("khlxr"));
							item.put("lxdh", temp.getString("lxdh"));
							item.put("jbf", temp.getString("jbf"));
							item.put("sjhm", temp.getString("sjhm"));
							item.put("kzzf7", temp.getString("kzzf7"));
							item.put("sblx", temp.getString("sblx"));
							item.put("sbxh", temp.getString("sbxh"));
							item.put("ywhy", temp.getString("ywhy"));
							item.put("sflx", temp.getString("sflx"));
							item.put("kzzf1", temp.getString("kzzf1"));
							item.put("tsyjnr", temp.getString("tsyjnr"));
							item.put("hfsm", temp.getString("hfsm"));
							item.put("ddsj", temp.getString("ddsj"));
							item.put("wcsj", temp.getString("wcsj"));
							item.put("hfpjsj", temp.getString("hfpjsj"));

						}
						if (queryType == 207) {
							try {
								yh = temp.getString("ywhy_bm");
							} catch (Exception e) {
							}
							item.put("textView1", getListItemIcon(yh));
							timeff = temp.getString("bzsj");
							item.put("bzsj", timeff);
							item.put("faultuser", temp.getString("nr"));
							item.put("zbh", temp.getString("zbh"));
							item.put("shen", temp.getString("shen"));
							item.put("ds", temp.getString("ds"));
							item.put("qx", temp.getString("qx"));
							item.put("djzt", temp.getString("djzt"));
							item.put("xqmc", temp.getString("xqmc"));
							item.put("jbf", temp.getString("jbf"));
						}
						
						if (queryType == 2801) {
							try {
								yh = temp.getString("ywhy_bm");
							} catch (Exception e) {
							}
							item.put("textView1", getListItemIcon(yh));
							timeff = temp.getString("zdrq");
							item.put("bzsj", timeff);
							item.put("faultuser", temp.getString("sgdh"));
							item.put("zbh", temp.getString("zbh"));
							item.put("sgdh", temp.getString("sgdh"));
							item.put("bz", temp.getString("bz"));
						}
						
						if (queryType == 2802) {
							try {
								yh = temp.getString("ywhy_bm");
							} catch (Exception e) {
							}
							item.put("textView1", getListItemIcon(yh));
							item.put("rq", temp.getString("rq"));
							item.put("faultuser", temp.getString("sgdh"));
							item.put("zbh", temp.getString("zbh"));
							item.put("sgdh", temp.getString("sgdh"));
							item.put("bz", temp.getString("bz"));
						}
						
						if (queryType == 2901) {
							try {
								yh = temp.getString("ywhb");
							} catch (Exception e) {
							}
							item.put("textView1", getListItemIcon(yh));
							timeff = DataUtil.toDataString("yyyy-MM-dd hh:mm:ss");
							item.put("bzsj", timeff);
							item.put("faultuser", temp.getString("wdbm_mc"));
							item.put("zbh", temp.getString("sbbm"));
							item.put("sbid", temp.getString("sbid"));
							item.put("sbbm", temp.getString("sbbm"));
							item.put("sblx", temp.getString("sblx"));
							item.put("sblx_mc", temp.getString("sblx_mc"));
							item.put("ywhb", temp.getString("ywhb"));
							item.put("ywhb_mc", temp.getString("ywhb_mc"));
							item.put("sbxh", temp.getString("sbxh"));
							item.put("sbxh_mc", temp.getString("sbxh_mc"));
							item.put("sf", temp.getString("sf"));
							item.put("sf_mc", temp.getString("sf_mc"));
							item.put("ds", temp.getString("ds"));
							item.put("ds_mc", temp.getString("ds_mc"));
							item.put("qx", temp.getString("qx"));
							item.put("qx_mc", temp.getString("qx_mc"));
							item.put("wdbm", temp.getString("wdbm"));
							item.put("wdbm_mc", temp.getString("wdbm_mc"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("sfxz", temp.getString("sfxz"));
							item.put("kzzf10", temp.getString("kzzf10"));
						}
						
						if (queryType == 2306) {
							try {
								yh = temp.getString("ywhy_bm");
							} catch (Exception e) {
							}
							item.put("textView1", getListItemIcon(yh));
							item.put("faultuser", temp.getString("xxdz"));
							item.put("zbh", temp.getString("zbh"));
							item.put("sfbm", temp.getString("sfbm"));
							item.put("dsbm", temp.getString("dsbm"));
							item.put("qxbm", temp.getString("qxbm"));
							item.put("ywhybm", temp.getString("ywhybm"));
							item.put("wdbm", temp.getString("wdbm"));
							item.put("sfmc", temp.getString("sfmc"));
							item.put("dsmc", temp.getString("dsmc"));
							item.put("qxmc", temp.getString("qxmc"));
							item.put("ywhymc", temp.getString("ywhymc"));
							item.put("wdmc", temp.getString("wdmc"));
							item.put("bz", temp.getString("bz"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("zgsl", temp.getString("zgsl"));
							item.put("fgsl", temp.getString("fgsl"));
							item.put("kzsz1", temp.getString("kzsz1"));
							item.put("kzsz2", temp.getString("kzsz2"));
						}
						
						if (queryType == 2307) {
							try {
								yh = temp.getString("ywhy_bm");
							} catch (Exception e) {
							}
							item.put("textView1", getListItemIcon(yh));
							item.put("faultuser", temp.getString("xxdz"));
							item.put("zbh", temp.getString("zbh"));
							item.put("sfbm", temp.getString("sfbm"));
							item.put("dsbm", temp.getString("dsbm"));
							item.put("qxbm", temp.getString("qxbm"));
							item.put("ywhybm", temp.getString("ywhybm"));
							item.put("wdbm", temp.getString("wdbm"));
							item.put("sfmc", temp.getString("sfmc"));
							item.put("dsmc", temp.getString("dsmc"));
							item.put("qxmc", temp.getString("qxmc"));
							item.put("ywhymc", temp.getString("ywhymc"));
							item.put("wdmc", temp.getString("wdmc"));
							item.put("bz", temp.getString("bz"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("zgsl", temp.getString("zgsl"));
							item.put("fgsl", temp.getString("fgsl"));
							item.put("kzsz1", temp.getString("kzsz1"));
							item.put("kzsz2", temp.getString("kzsz2"));
						}
						
						if(queryType == 2306 || queryType == 2307 || queryType == 2802){
							item.put("timemy", "");
							item.put("datemy", "");
						}else{
							timeff = timeff.substring(2);
							item.put("timemy", mdateformat(1, timeff));// 时间
							item.put("datemy", mdateformat(0, timeff));// 年月日
						}
						
						datalist.add(item);
					}
					ServiceReportCache.setObjectdata(datalist);
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
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}
		} else {
			Message msg = new Message();
			msg.what = Constant.FAIL;
			handler.sendMessage(msg);
		}

	}

	private class CurrAdapter extends SimpleAdapter {

		public CurrAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);

		}

		@SuppressLint("ResourceAsColor")
		@Override
		public View getView(final int position, View convertView,
				final ViewGroup parent) {
			final View view = super.getView(position, convertView, parent);
			try {
				Map<String, Object> item = datalist.get(position);
				String sfcs = (String) item.get("sfcs");
				if ("1".equals(sfcs)) {
					view.setBackgroundResource(R.color.red);
				}else {
					view.setBackgroundResource(R.color.white);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return view;
		}
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.NETWORK_ERROR:
				dialogShowMessage_P(Constant.NETWORK_ERROR_STR, null);
				break;
			case Constant.SUCCESS:
				adapter = new CurrAdapter(ListKdg.this,
						ServiceReportCache.getObjectdata(),
						R.layout.listview_dispatchinginformationreceiving_item,
						from, to);
				listView.setAdapter(adapter);
				break;
			case Constant.FAIL:
				dialogShowMessage_P("你查询的时间段内，没有数据",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								onBackPressed();
							}
						});
				break;
			}
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		}

	};

}
