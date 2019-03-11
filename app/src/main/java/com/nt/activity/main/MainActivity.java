package com.nt.activity.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.android.hms.agent.HMSAgent;
import com.huawei.android.hms.agent.common.handler.ConnectHandler;
import com.huawei.android.hms.agent.push.handler.GetTokenHandler;
import com.jauker.widget.BadgeView;
import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.activity.esp.SheBeiPDActivity;
import com.nt.activity.kc.KccxChooseActivity;
import com.nt.activity.kc.KcpdActivity;
import com.nt.activity.kc.KcxxActivity;
import com.nt.activity.kc.SqdActivity;
import com.nt.activity.kc.SqdcxkListActivity;
import com.nt.activity.kc.WbdbckActivity;
import com.nt.activity.kc.WbdbrkListActivity;
import com.nt.activity.kdg.DhjcKdg;
import com.nt.activity.kdg.FwbgKdg;
import com.nt.activity.kdg.FwbgKdgEcsm;
import com.nt.activity.kdg.FwbgKdgWx;
import com.nt.activity.kdg.FwbgKdgWxEcsm;
import com.nt.activity.kdg.FwbgXj;
import com.nt.activity.kdg.FwbgXjWd;
import com.nt.activity.kdg.JdxyKdg;
import com.nt.activity.kdg.JdxyKdgWx;
import com.nt.activity.kdg.JdxyXj;
import com.nt.activity.kdg.JqgdcxKdg;
import com.nt.activity.kdg.ListKdg;
import com.nt.activity.kdg.SbxxlrKdg;
import com.nt.activity.kdg.SbxxpdActivity;
import com.nt.activity.kdg.SmdwKdg;
import com.nt.activity.kdg.SmdwKdgWx;
import com.nt.activity.kdg.SmdwXj;
import com.nt.activity.kdg.Xddwcx;
import com.nt.activity.kdg.Zzpqgdcx;
import com.nt.activity.kdg.ZzzpKdgWx;
import com.nt.activity.login.LoginActivity;
import com.nt.activity.notification.Ccgd;
import com.nt.activity.notification.Yjtx;
import com.nt.activity.w.BzfyActivity;
import com.nt.activity.w.ChangePasswordActivity;
import com.nt.activity.w.Fwzxscx;
import com.nt.activity.w.GzpActivity;
import com.nt.activity.w.GzpLrActivity;
import com.nt.activity.w.HelpActivity;
import com.nt.activity.w.JszlActivity;
import com.nt.activity.w.Pqzgjsl;
import com.nt.activity.w.QyxxActivity;
import com.nt.activity.w.TcpcActivity;
import com.nt.activity.w.XxglActivity;
import com.nt.activity.w.YHKInfoActivity;
import com.nt.cache.DataCache;
import com.nt.cache.ServiceReportCache;
import com.nt.common.Constant;
import com.nt.dodowaterfall.MarqueeTextView;
import com.nt.utils.AppUtils;
import com.nt.utils.Config;
import com.tencent.android.tpush.XGCustomPushNotificationBuilder;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

@SuppressLint("ResourceAsColor")
public class MainActivity extends FrameActivity implements OnClickListener {

	private LinearLayout linear_sy_top, tab_bottom_esp, tab_bottom_kdg,
			tab_bottom_kc, tab_bottom_js, tab_bottom_w;
	private ImageView sy_top_1, sy_top_2;
	private int num = 1;
	private List<String> menu;
	private BadgeView badgeView_sy, badgeView_zyxx,badgeView_rk, badgeView_kc, badgeView_fk, badgeView_wx, badgeView_wxbgxg, badgeView_wxpjxg, badgeView_wxp2lr;
	private TextView tishi, tv_esp, tv_kdg, tv_kc, tv_js, tv_w, tv_ry;
	private ImageView img_esp, img_kdg, img_kc, img_js, img_w;
	private MarqueeTextView tv_marquee;
	private String ts_num_msg_kdg = "", zbh, token;
	private int currType = 1; // 当前类型：1.esp 2.快递柜 3.结算 4.库存
	private Long mExitTime = -2000l;

	private List<Map<String, Object>> esp_list, kdg_list, kc_list, js_list,
			w_list, kfzyxx_list,wx_list;
	private GridView gridview;
	private List<Map<String, Object>> data_list;
	private SimpleAdapter sim_adapter;
	private String[] from;
	private int[] to;

	private ListView listview;
	private SimpleAdapter adapter_listview;
	private List<Map<String, Object>> data_listview;
	private String[] from_listview;
	private int[] to_listview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initView();
		initListeners();
		changeBackground();
		// runChangeService();
		sy_top_1.setVisibility(View.GONE);
		sy_top_2.setVisibility(View.GONE);
		runServices();
		tv_marquee.setText(DataCache.getinition().getTsxx());

	}

	@SuppressLint("NewApi")
	private void runServices() {
		try {
			XGCustomPushNotificationBuilder build = new XGCustomPushNotificationBuilder();

			build.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.strum));
			build.setDefaults(Notification.DEFAULT_LIGHTS); 
			build.setFlags(Notification.FLAG_AUTO_CANCEL); 

			// 设置自定义通知layout,通知背景等可以在layout里设置

			build.setLayoutId(R.layout.notification);

			// 设置自定义通知内容id

			build.setLayoutTextId(R.id.content);

			// 设置自定义通知标题id

			build.setLayoutTitleId(R.id.title);

			// 设置自定义通知图片资源

			build.setLayoutIconDrawableId(R.drawable.logo);

			// 设置状态栏的通知小图标

			// build.setIcon(R.drawable.right);

			// 设置时间id

			build.setLayoutTimeId(R.id.time);

			// 若不设定以上自定义layout，又想简单指定通知栏图片资源

//			build.setNotificationLargeIcon(R.drawable.logo);

			// 客户端保存build_id
			
			String brand = android.os.Build.BRAND;
			
			if("Xiaomi".equals(brand)){
				//打开第三方推送
				XGPushConfig.enableOtherPush(getApplicationContext(), true);
				//开启小米推送
				XGPushConfig.setMiPushAppId(getApplicationContext(), "2882303761517778341");
				XGPushConfig.setMiPushAppKey(getApplicationContext(), "5811777826341");
			}
			
			
			if("HUAWEI".equals(brand)){
				HMSAgent.init(this);
				HMSAgent.connect(this, new ConnectHandler() {
				    @Override
				    public void onConnect(int rst) {
				        //toastShowMessage("HMS connect end:" + rst);
				    }
				});    
				 HMSAgent.Push.getToken(new GetTokenHandler() {
						@Override
						public void onResult(int rst) {
							//toastShowMessage("get token: end" + rst);
							
						}
				    }); 
				//华为推送
				XGPushConfig.enableOtherPush(getApplicationContext(), true);
				//XGPushConfig.setHuaweiDebug(true);
			}
			
			if("Meizu".equals(brand)){
				XGPushConfig.enableOtherPush(getApplicationContext(), true);
				XGPushConfig.setMzPushAppId(getApplicationContext(), "1000352");
				XGPushConfig.setMzPushAppKey(getApplicationContext(), "1bf368da39774583aad800ab3eee5a1c");
			}
			

			XGPushManager.setDefaultNotificationBuilder(this, build);

			XGPushManager.registerPush(this, new XGIOperateCallback() {
				@Override
				public void onSuccess(Object data, int flag) {
					// token在设备卸载重装的时候有可能会变
					Log.d("TPush", "注册成功，设备token为：" + data);
					token = (String) data;
					Config.getExecutorService().execute(new Runnable() {

						@Override
						public void run() {
							getWebService("updateToken");
						}
					});
				}

				@Override
				public void onFail(Object data, int errCode, String msg) {
					Log.d("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
					token = "";
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		

//		 long now = SystemClock.elapsedRealtime();
//		 // 工单超时提醒
//		 Intent intent = new Intent(getApplicationContext(), CsyyService.class);
//		 PendingIntent pi = PendingIntent.getService(this, 0, intent, 0);
//		 AlarmManager alarmManager = (AlarmManager)
//		 getSystemService(ALARM_SERVICE);
//		 alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, now, 30 * 60 *
//		 1000,
//		 pi);
//		 // 催单提醒
//		 intent = new Intent(getApplicationContext(), CdtxService.class);
//		 pi = PendingIntent.getService(this, 0, intent, 0);
//		 alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//		 alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, now, 5 * 60 *
//		 1000,
//		 pi);
//		 // 接单提醒
//		 intent = new Intent(getApplicationContext(), JdtxService.class);
//		 pi = PendingIntent.getService(this, 0, intent, 0);
//		 alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//		 alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, now, 60 * 1000,
//		 pi);
//		
//		 // 预警提醒
//		 intent = new Intent(getApplicationContext(), PatrolService.class);
//		 pi = PendingIntent.getService(this, 0, intent, 0);
//		 alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//		 alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, now,
//		 12 * 60 * 60 * 1000, pi);
	}

	@Override
	protected void initView() {

		badgeView_sy = new BadgeView(getApplicationContext());
		badgeView_zyxx = new BadgeView(getApplicationContext());
		badgeView_rk = new BadgeView(getApplicationContext());
		badgeView_kc = new BadgeView(getApplicationContext());
		badgeView_fk = new BadgeView(getApplicationContext());
		badgeView_wx = new BadgeView(getApplicationContext());
		badgeView_wxbgxg = new BadgeView(getApplicationContext());
		badgeView_wxpjxg = new BadgeView(getApplicationContext());
		badgeView_wxp2lr = new BadgeView(getApplicationContext());
		tishi = (TextView) findViewById(R.id.tishi);
		tv_ry = (TextView) findViewById(R.id.tv_ry);
		linear_sy_top = (LinearLayout) findViewById(R.id.linear_sy_top);
		tab_bottom_esp = (LinearLayout) findViewById(R.id.tab_bottom_esp);
		tab_bottom_kdg = (LinearLayout) findViewById(R.id.tab_bottom_kdg);
		tab_bottom_kc = (LinearLayout) findViewById(R.id.tab_bottom_kc);
		tab_bottom_js = (LinearLayout) findViewById(R.id.tab_bottom_js);
		tab_bottom_w = (LinearLayout) findViewById(R.id.tab_bottom_w);
		tv_marquee = (MarqueeTextView) findViewById(R.id.tv_marquee);
		sy_top_1 = (ImageView) findViewById(R.id.sy_top_1);
		sy_top_2 = (ImageView) findViewById(R.id.sy_top_2);
		img_esp = (ImageView) tab_bottom_esp.findViewById(R.id.img_esp);
		tv_esp = (TextView) tab_bottom_esp.findViewById(R.id.tv_esp);
		img_kdg = (ImageView) tab_bottom_kdg.findViewById(R.id.img_kdg);
		tv_kdg = (TextView) tab_bottom_kdg.findViewById(R.id.tv_kdg);
		img_kc = (ImageView) tab_bottom_kc.findViewById(R.id.img_kc);
		tv_kc = (TextView) tab_bottom_kc.findViewById(R.id.tv_kc);
		img_js = (ImageView) tab_bottom_js.findViewById(R.id.img_js);
		tv_js = (TextView) tab_bottom_js.findViewById(R.id.tv_js);
		img_w = (ImageView) tab_bottom_w.findViewById(R.id.img_w);
		tv_w = (TextView) tab_bottom_w.findViewById(R.id.tv_w);

		gridview = (GridView) findViewById(R.id.gridview);
		from = new String[] { "id", "img", "name", "type" };
		to = new int[] { R.id.menu_id, R.id.menu_img, R.id.menu_name,
				R.id.menu_type };

		listview = (ListView) findViewById(R.id.listview);

		from_listview = new String[] { "textView1", "faultuser", "zbh",
				"timemy", "datemy", "ztzt" };
		to_listview = new int[] { R.id.textView1, R.id.yytmy, R.id.pgdhmy,
				R.id.timemy, R.id.datemy, R.id.ztzt };

		initMenus();

		tv_ry.setText(DataCache.getinition().getUsername());
		Intent intent = getIntent();
		currType = intent.getIntExtra("currType", 1);
		zbh = intent.getStringExtra("zbh");
		if (zbh != null && !"".equals(zbh)) {
			Config.getExecutorService().execute(new Runnable() {

				@Override
				public void run() {
					// 催单提醒 修改状态
					getWebService("cdtx");
				}
			});
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		listview = (ListView) findViewById(R.id.listview);
		loadMenus();
	}

	@Override
	protected void initListeners() {

		sy_top_1.setOnClickListener(this);
		sy_top_2.setOnClickListener(this);
		linear_sy_top.setOnClickListener(this);
		tv_marquee.setOnClickListener(this);
		tab_bottom_kdg.setOnClickListener(this);
		tab_bottom_esp.setOnClickListener(this);
		tab_bottom_kc.setOnClickListener(this);
		tab_bottom_js.setOnClickListener(this);
		tab_bottom_w.setOnClickListener(this);

		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TextView tv = (TextView) view.findViewById(R.id.menu_id);
				String menu_type = tv.getText().toString();
				turnTo(menu_type);
			}
		});

		gridview.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) { // 准备滚动
					//badgeView_sy.setTargetView(null);
					badgeView_rk.setTargetView(null);
					badgeView_kc.setTargetView(null);
					badgeView_fk.setTargetView(null);
					badgeView_zyxx.setTargetView(null);
					//badgeView_wx.setTargetView(null);
					badgeView_wxbgxg.setTargetView(null);
					badgeView_wxpjxg.setTargetView(null);
					badgeView_wxp2lr.setTargetView(null);
				} else if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {// 滚动结束
					int count = gridview.getChildCount();
					for (int j = 0; j < count; j++) {
						View v = gridview.getChildAt(j);
						LinearLayout l = (LinearLayout) v
								.findViewById(R.id.ll_menu);
						TextView tv_id = (TextView) l
								.findViewById(R.id.menu_id);
						String menu = tv_id.getText().toString();

						if (kfzyxx_list.size() > 0 && currType==5) {
							if ("m_pad_kfdbrc_zyxx".equals(menu)) {
								for (int i = 0; i < kfzyxx_list.size(); i++) {
									Map<String, Object> map = kfzyxx_list
											.get(i);
									if (menu.equals(map.get("menu"))) {
										badgeView_zyxx.setTargetView(l);
										badgeView_zyxx.setBadgeMargin(0, 15, 10, 0);// 设置边界
										badgeView_zyxx.setTextSize(6);
										badgeView_zyxx.setText("");
									}
								}
								
							}
							if ("m_pad_wbdbrk".equals(menu)) {
								for (int i = 0; i < kfzyxx_list.size(); i++) {
									Map<String, Object> map = kfzyxx_list
											.get(i);
									if (menu.equals(map.get("menu"))) {
										badgeView_rk.setTargetView(l);
										badgeView_rk.setBadgeMargin(0, 15, 10,
												0);// 设置边界
										badgeView_rk.setTextSize(6);
										badgeView_rk.setText("");
									}
								}
							}
							if ("m_pad_ckdqkc".equals(menu)) {
								for (int i = 0; i < kfzyxx_list.size(); i++) {
									Map<String, Object> map = kfzyxx_list
											.get(i);
									if (menu.equals(map.get("menu"))) {
										badgeView_kc.setTargetView(l);
										badgeView_kc.setBadgeMargin(0, 15, 10,
												0);// 设置边界
										badgeView_kc.setTextSize(6);
										badgeView_kc.setText("");
									}
								}
							}
							if ("m_pad_pjfk".equals(menu)) {
								for (int i = 0; i < kfzyxx_list.size(); i++) {
									Map<String, Object> map = kfzyxx_list
											.get(i);
									if (menu.equals(map.get("menu"))) {
										badgeView_fk.setTargetView(l);
										badgeView_fk.setBadgeMargin(0, 15, 10,
												0);// 设置边界
										badgeView_fk.setTextSize(6);
										badgeView_fk.setText("");
									}
								}
							}
						}
						if (wx_list.size() > 0 && currType==3) {	
							if ("m_kdg_pjxg".equals(menu)) {
								for (int i = 0; i < wx_list.size(); i++) {
									Map<String, Object> map = wx_list
											.get(i);
									if (menu.equals(map.get("menu"))) {
										badgeView_wxpjxg.setTargetView(l);
										badgeView_wxpjxg.setBadgeMargin(0, 15, 10,
												0);// 设置边界
										badgeView_wxpjxg.setTextSize(6);
										badgeView_wxpjxg.setText("");
									}
								}
							}
							if ("m_pad_kdg_fwbgxg_wx".equals(menu)) {
								for (int i = 0; i < wx_list.size(); i++) {
									Map<String, Object> map = wx_list
											.get(i);
									if (menu.equals(map.get("menu"))) {
										badgeView_wxbgxg.setTargetView(l);
										badgeView_wxbgxg.setBadgeMargin(0, 15, 10,
												0);// 设置边界
										badgeView_wxbgxg.setTextSize(6);
										badgeView_wxbgxg.setText("");
									}
								}
							}
							if ("m_pad_p2gd".equals(menu)) {
								for (int i = 0; i < wx_list.size(); i++) {
									Map<String, Object> map = wx_list
											.get(i);
									if (menu.equals(map.get("menu"))) {
										badgeView_wxp2lr.setTargetView(l);
										badgeView_wxp2lr.setBadgeMargin(0, 15, 10,
												0);// 设置边界
										badgeView_wxp2lr.setTextSize(6);
										badgeView_wxp2lr.setText("");
									}
								}
							}
						}

					}
				}

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

			}
		});

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				try {
					Map<String, Object> map = data_listview.get(position);
					String djzt = (String) map.get("djzt");
					String ywlx = (String) map.get("ywlx");
					Intent intent = null;
					if ("安装".equals(ywlx)) {
						if ("1".equals(djzt) || "1.2".equals(djzt)
								|| "1.5".equals(djzt)) {
							DataCache.getinition().setTitle("组长转派（安装）");
							intent = new Intent(MainActivity.this,
									ZzzpKdgWx.class);
						} else if ("2".equals(djzt)) {
							DataCache.getinition().setTitle("接单响应（安装）");
							intent = new Intent(MainActivity.this,
									JdxyKdg.class);
						} else if ("3".equals(djzt)) {
							DataCache.getinition().setTitle("上门定位（安装）");
							intent = new Intent(MainActivity.this,
									SmdwKdg.class);
						} else if ("4".equals(djzt)) {
							DataCache.getinition().setTitle("服务报告（安装）");
							intent = new Intent(MainActivity.this,
									FwbgKdg.class);
						} else if ("5".equals(djzt)) {
							DataCache.getinition().setTitle("二次上门（安装）");
							intent = new Intent(MainActivity.this,
									FwbgKdgEcsm.class);
						} else if ("6".equals(djzt)) {
							DataCache.getinition().setTitle("安装条件确认（安装）");
							intent = new Intent(MainActivity.this,
									DhjcKdg.class);
						}
					} else if ("维修".equals(ywlx)) {
						if ("1".equals(djzt) || "1.5".equals(djzt)) {
							DataCache.getinition().setTitle("组长转派（维修）");
							intent = new Intent(MainActivity.this,
									ZzzpKdgWx.class);
						} else if ("2".equals(djzt)) {
							DataCache.getinition().setTitle("接单响应（维修）");
							intent = new Intent(MainActivity.this,
									JdxyKdgWx.class);
						} else if ("3".equals(djzt)) {
							DataCache.getinition().setTitle("上门定位（维修）");
							intent = new Intent(MainActivity.this,
									SmdwKdgWx.class);
						} else if ("4".equals(djzt)) {
							DataCache.getinition().setTitle("服务报告（维修）");
							intent = new Intent(MainActivity.this,
									FwbgKdgWx.class);
						} else if ("4.5".equals(djzt)) {
							DataCache.getinition().setTitle("扫描二维码（维修）");
							intent = new Intent(MainActivity.this,
									SbxxlrKdg.class);
						} else if ("5".equals(djzt)) {
							DataCache.getinition().setTitle("二次上门（维修）");
							intent = new Intent(MainActivity.this,
									FwbgKdgWxEcsm.class);
						}
					} else if ("巡检".equals(ywlx)) {
						if ("1".equals(djzt)) {
							DataCache.getinition().setTitle("接单响应（巡检）");
							intent = new Intent(MainActivity.this, JdxyXj.class);
						} else if ("2".equals(djzt)) {
							DataCache.getinition().setTitle("上门定位（巡检）");
							intent = new Intent(MainActivity.this, SmdwXj.class);
						} else if ("3".equals(djzt)) {
							DataCache.getinition().setTitle("服务报告（巡检）");
							intent = new Intent(MainActivity.this, FwbgXj.class);
						} else if ("4".equals(djzt)) {
							DataCache.getinition().setTitle("组长转派（巡检）");
							intent = new Intent(MainActivity.this,
									ZzzpKdgWx.class);
						}
					}
					ServiceReportCache.setObjectdata(data_listview);
					ServiceReportCache.setIndex(position);
					startActivityForResult(intent, 1);
				} catch (Exception e) {
				}

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
			// Config.getExecutorService().execute(new Runnable() {
			//
			// @Override
			// public void run() {
			//
			// getWebService("query");
			// }
			// });
		}
	}

	private void runChangeService() {
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(5000);
					if (num < 3) {
						num = num + 1;
					} else {
						num = 1;
					}
					Message msg = new Message();
					msg.what = Constant.NUM_8;
					handler.sendMessage(msg);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sy_top_1:
			num = 1;
			changeBackground();
			break;
		case R.id.sy_top_2:
			num = 2;
			changeBackground();
			break;
		case R.id.linear_sy_top:
			gotoOtherPage();
			break;
		case R.id.tv_marquee:
			dialogShowMessage_P(tv_marquee.getText().toString(), null);
			break;
		case R.id.tab_bottom_esp:
			currType = 1;
			setMenus();
			break;
		case R.id.tab_bottom_kdg:
			currType = 2;
			setMenus();
			break;
		case R.id.tab_bottom_js:
			currType = 3;
			setMenus();
			break;
		case R.id.tab_bottom_kc:
			currType = 4;
			setMenus();
			break;
		case R.id.tab_bottom_w:
			currType = 5;
			setMenus();
			break;
		default:
			break;
		}

	}

	private void initMenus() {
		esp_list = new ArrayList<Map<String, Object>>();
		Map<String, Object> esp_map = new HashMap<String, Object>();
		esp_map.put("id", "m_pad_yytbz");
		esp_map.put("name", "故障申报");
		esp_map.put("img", R.drawable.menu_esp_fwbg);
		esp_list.add(esp_map);
		esp_map = new HashMap<String, Object>();
		esp_map.put("id", "m_pad_gdqd");
		esp_map.put("name", "电子抢单");
		esp_map.put("img", R.drawable.menu_esp_fwbg);
		esp_list.add(esp_map);
		esp_map = new HashMap<String, Object>();
		esp_map.put("id", "m_pad_jdxy");
		esp_map.put("name", "接单响应");
		esp_map.put("img", R.drawable.menu_esp_jdxy);
		esp_list.add(esp_map);
		esp_map = new HashMap<String, Object>();
		esp_map.put("id", "m_pad_jjjd");
		esp_map.put("name", "拒绝接单");
		esp_map.put("img", R.drawable.menu_esp_jjjd);
		esp_list.add(esp_map);
		esp_map = new HashMap<String, Object>();
		esp_map.put("id", "m_pad_smddqr");
		esp_map.put("name", "上门定位");
		esp_map.put("img", R.drawable.menu_esp_smdw);
		esp_list.add(esp_map);
		esp_map = new HashMap<String, Object>();
		esp_map.put("id", "m_pad_fwbg");
		esp_map.put("name", "服务报告");
		esp_map.put("img", R.drawable.menu_esp_fwbg);
		esp_list.add(esp_map);
		esp_map = new HashMap<String, Object>();
		esp_map.put("id", "m_pad_zzfwbg");
		esp_map.put("name", "纸质报告");
		esp_map.put("img", R.drawable.menu_esp_zzbgsc);
		esp_list.add(esp_map);
		esp_map = new HashMap<String, Object>();
		esp_map.put("id", "m_pad_pgdcx");
		esp_map.put("name", "近期工单查询");
		esp_map.put("img", R.drawable.menu_esp_jqgdcx);
		esp_list.add(esp_map);
		// esp_map = new HashMap<String, Object>();
		// esp_map.put("id", "m_pad_sblr");
		// esp_map.put("name", "设备录入");
		// esp_map.put("img", R.drawable.menu_esp_xxlr);
		// esp_list.add(esp_map);
		esp_map = new HashMap<String, Object>();
		esp_map.put("id", "m_pad_fwzxscx");
		esp_map.put("name", "服务咨询师查询");
		esp_map.put("img", R.drawable.menu_esp_fwzxs);
		esp_list.add(esp_map);

		kdg_list = new ArrayList<Map<String, Object>>();
		Map<String, Object> kdg_map = new HashMap<String, Object>();
		kdg_map.put("id", "m_pad_kdg_jdxy_az");
		kdg_map.put("name", "接单响应");
		kdg_map.put("img", R.drawable.menu_esp_jdxy);
		kdg_list.add(kdg_map);
		// kdg_map = new HashMap<String, Object>();
		// kdg_map.put("id", "m_kdg_jjgd_kuaidigui");
		// kdg_map.put("name", "拒绝接单");
		// kdg_map.put("img", R.drawable.menu_esp_jjjd);
		// kdg_list.add(kdg_map);
		kdg_map = new HashMap<String, Object>();
		kdg_map.put("id", "m_pad_kdg_smdw_az");
		kdg_map.put("name", "上门定位");
		kdg_map.put("img", R.drawable.menu_esp_smdw);
		kdg_list.add(kdg_map);
		kdg_map = new HashMap<String, Object>();
		kdg_map.put("id", "m_pad_kdg_fwbg_az");
		kdg_map.put("name", "服务报告");
		kdg_map.put("img", R.drawable.menu_esp_fwbg);
		kdg_list.add(kdg_map);
		kdg_map = new HashMap<String, Object>();
		kdg_map.put("id", "m_pad_kdg_fwbgxg_az");
		kdg_map.put("name", "服务报告修改");
		kdg_map.put("img", R.drawable.menu_esp_fwbg);
		kdg_list.add(kdg_map);
		kdg_map = new HashMap<String, Object>();
		kdg_map.put("id", "m_pad_kdg_ecsm_az");
		kdg_map.put("name", "二次上门");
		kdg_map.put("img", R.drawable.menu_kdg_dhjc);
		kdg_list.add(kdg_map);

		kdg_map = new HashMap<String, Object>();
		kdg_map.put("id", "m_pad_kdg_dhjrs_az");
		kdg_map.put("name", "安装条件确认");
		kdg_map.put("img", R.drawable.menu_kdg_dhjc);
		kdg_list.add(kdg_map);

		// kdg_map = new HashMap<String, Object>();
		// kdg_map.put("id", "m_zh_xjdingwei");
		// kdg_map.put("name", "巡检定位");
		// kdg_map.put("img", R.drawable.menu_kdg_smdw);
		// kdg_list.add(kdg_map);
		// kdg_map = new HashMap<String, Object>();
		// kdg_map.put("id", "m_zh_sjxj");
		// kdg_map.put("name", "巡检服务报告");
		// kdg_map.put("img", R.drawable.menu_kdg_fwbg);
		// kdg_list.add(kdg_map);
		// kdg_map = new HashMap<String, Object>();
		// kdg_map.put("id", "m_pad_kdg_dhjrs_az");
		// kdg_map.put("name", "安装条件确认");
		// kdg_map.put("img", R.drawable.menu_kdg_dhjc);
		// kdg_list.add(kdg_map);

		kc_list = new ArrayList<Map<String, Object>>();
		Map<String, Object> kc_map = new HashMap<String, Object>();
		kc_map.put("id", "m_pad_kdg_xj_jdxy");
		kc_map.put("name", "接单响应");
		kc_map.put("img", R.drawable.menu_kc_dqkccx);
		kc_list.add(kc_map);
		kc_map = new HashMap<String, Object>();
		kc_map.put("id", "m_pad_kdg_xj_smdw");
		kc_map.put("name", "上门定位");
		kc_map.put("img", R.drawable.menu_kc_wbdbrk);
		kc_list.add(kc_map);

		kc_map = new HashMap<String, Object>();
		kc_map.put("id", "m_kdg_xj_xddw");
		kc_map.put("name", "巡检附近设备");
		kc_map.put("img", R.drawable.menu_esp_smdw);
		kc_list.add(kc_map);
		kc_map = new HashMap<String, Object>();
		kc_map.put("id", "m_pad_kdg_xj_fwbg");
		kc_map.put("name", "服务报告");
		kc_map.put("img", R.drawable.menu_kc_wbdbck);
		kc_list.add(kc_map);
		kc_map = new HashMap<String, Object>();
		kc_map.put("id", "m_pad_kdg_xj_fwbgxg");
		kc_map.put("name", "服务报告修改");
		kc_map.put("img", R.drawable.menu_esp_fwbg);
		kc_list.add(kc_map);
		kc_map = new HashMap<String, Object>();
		kc_map.put("id", "m_wdxjfwbg");
		kc_map.put("name", "网点巡检");
		kc_map.put("img", R.drawable.menu_esp_fwbg);
		kc_list.add(kc_map);
		kc_map = new HashMap<String, Object>();
		kc_map.put("id", "m_wdxjbg_sc");
		kc_map.put("name", "巡检报告上传");
		kc_map.put("img", R.drawable.menu_esp_fwbg);
		kc_list.add(kc_map);
		kc_map = new HashMap<String, Object>();
		kc_map.put("id", "m_wdxjbg_xg");
		kc_map.put("name", "巡检报告修改");
		kc_map.put("img", R.drawable.menu_esp_fwbg);
		kc_list.add(kc_map);

		js_list = new ArrayList<Map<String, Object>>();
		Map<String, Object> js_map = new HashMap<String, Object>();
		js_map.put("id", "m_pad_kdg_jdxy_wx");
		js_map.put("name", "接单响应");
		js_map.put("img", R.drawable.menu_js_dqyskcx);
		js_list.add(js_map);
		js_map = new HashMap<String, Object>();
		js_map.put("id", "m_pad_kdg_smdw_wx");
		js_map.put("name", "上门定位");
		js_map.put("img", R.drawable.menu_js_yslszcx);
		js_list.add(js_map);
		js_map = new HashMap<String, Object>();
		js_map.put("id", "m_pad_smewm_wx");
		js_map.put("name", "扫描二维码");
		js_map.put("img", R.drawable.menu_js_fyqr);
		js_list.add(js_map);
		js_map = new HashMap<String, Object>();
		js_map.put("id", "m_pad_kdg_fwbg_wx");
		js_map.put("name", "服务报告");
		js_map.put("img", R.drawable.menu_js_fyqr);
		js_list.add(js_map);
		js_map = new HashMap<String, Object>();
		js_map.put("id", "m_pad_kdg_ecsm_wx");
		js_map.put("name", "二次上门");
		js_map.put("img", R.drawable.menu_esp_fwbg);
		js_list.add(js_map);
		js_map = new HashMap<String, Object>();
		js_map.put("id", "m_pad_kdg_fwbgxg_wx");
		js_map.put("name", "服务报告修改");
		js_map.put("img", R.drawable.menu_esp_fwbg);
		js_list.add(js_map);
		js_map = new HashMap<String, Object>();
		js_map.put("id", "m_kdg_pjxg");
		js_map.put("name", "配件修改");
		js_map.put("img", R.drawable.menu_esp_fwbg);
		js_list.add(js_map);
		js_map = new HashMap<String, Object>();
		js_map.put("id", "m_pad_p2gd");
		js_map.put("name", " P2超时原因录入");
		js_map.put("img", R.drawable.menu_kdg_jdxy);
		js_list.add(js_map);

		Map<String, Object> w_map = new HashMap<String, Object>();
		w_list = new ArrayList<Map<String, Object>>();
		w_map = new HashMap<String, Object>();
		w_map.put("id", "m_pad_kdg_jqgdxz");
		w_map.put("name", "近期工单查询");
		w_map.put("img", R.drawable.menu_esp_jqgdcx);
		w_list.add(w_map);

		w_map = new HashMap<String, Object>();
		w_map.put("id", "m_pad_kdg_zzzp_wx");
		w_map.put("name", "组长转派");
		w_map.put("img", R.drawable.menu_kdg_jdxy);
		w_list.add(w_map);

		
		w_map = new HashMap<String, Object>();
		w_map.put("id", "m_pad_kdg_pqzgjk");
		w_map.put("name", " 片区工单查询");
		w_map.put("img", R.drawable.menu_kdg_jdxy);
		w_list.add(w_map);

		w_map = new HashMap<String, Object>();
		w_map.put("id", "m_pad_kdg_pqzgjsl");
		w_map.put("name", "片区主管及时率");
		w_map.put("img", R.drawable.menu_kdg_jdxy);
		w_list.add(w_map);

		w_map = new HashMap<String, Object>();
		w_map.put("id", "m_pad_sblr");
		w_map.put("name", "新增设备信息");
		w_map.put("img", R.drawable.menu_kdg_dhjc);
		w_list.add(w_map);
		w_map = new HashMap<String, Object>();
		w_map.put("id", "m_sbgl_scxjsb_xg");
		w_map.put("name", "巡检设备修改");
		w_map.put("img", R.drawable.menu_kdg_jdxy);
		w_list.add(w_map);
		w_map = new HashMap<String, Object>();
		w_map.put("id", "m_kdg_xing_sbxxpd");
		w_map.put("name", "设备信息核对");
		w_map.put("img", R.drawable.menu_kdg_dhjc);
		w_list.add(w_map);

		w_map = new HashMap<String, Object>();
		w_map.put("id", "m_pad_qyjjg");
		w_map.put("name", "区域及价格");
		w_map.put("img", R.drawable.menu_w_qyyjg);
		w_list.add(w_map);
		w_map = new HashMap<String, Object>();
		w_map.put("id", "m_pad_yhkxx");
		w_map.put("name", "银行卡信息");
		w_map.put("img", R.drawable.menu_w_yhk);
		w_list.add(w_map);
		w_map = new HashMap<String, Object>();
		w_map.put("id", "m_pad_zyxx");
		w_map.put("name", "消息中心");
		w_map.put("img", R.drawable.menu_w_zyxx);
		w_list.add(w_map);
		w_map = new HashMap<String, Object>();
		w_map.put("id", "m_pad_ryxx");
		w_map.put("name", "人员信息");
		w_map.put("img", R.drawable.menu_w_gzz);
		w_list.add(w_map);
		w_map = new HashMap<String, Object>();
		w_map.put("id", "m_pad_ryxxlr");
		w_map.put("name", "人员信息录入");
		w_map.put("img", R.drawable.menu_w_gzzlr);
		w_list.add(w_map);

		w_map = new HashMap<String, Object>();
		w_map.put("id", "m_pad_ckdqkc");
		w_map.put("name", "当前库存");
		w_map.put("img", R.drawable.menu_kc_dqkccx);
		w_list.add(w_map);
		w_map = new HashMap<String, Object>();
		w_map.put("id", "m_pad_cksqd");
		w_map.put("name", "申请单录入");
		w_map.put("img", R.drawable.menu_w_qyyjg);
		w_list.add(w_map);
		w_map = new HashMap<String, Object>();
		w_map.put("id", "m_pad_cksqdcx");
		w_map.put("name", "申请单查询作废");
		w_map.put("img", R.drawable.menu_w_qyyjg);
		w_list.add(w_map);
		w_map = new HashMap<String, Object>();
		w_map.put("id", "m_pad_wbdbrk");
		w_map.put("name", "外部调拨入库");
		w_map.put("img", R.drawable.menu_kc_wbdbrk);
		w_list.add(w_map);
		w_map = new HashMap<String, Object>();
		w_map.put("id", "m_pad_pjfk");
		w_map.put("name", "配件返库");
		w_map.put("img", R.drawable.menu_kc_wbdbrk);
		w_list.add(w_map);
		w_map = new HashMap<String, Object>();
		w_map.put("id", "m_pad_wbdbck");
		w_map.put("name", "外部调拨出库");
		w_map.put("img", R.drawable.menu_kc_wbdbck);
		w_list.add(w_map);

		w_map = new HashMap<String, Object>();
		w_map.put("id", "m_pad_thck");
		w_map.put("name", "调拨差异查询");
		w_map.put("img", R.drawable.menu_w_jszl);
		w_list.add(w_map);

		w_map = new HashMap<String, Object>();
		w_map.put("id", "m_pad_kfdbrc_zyxx");
		w_map.put("name", "库房重要消息");
		w_map.put("img", R.drawable.menu_w_zyxx);
		w_list.add(w_map);

		w_map = new HashMap<String, Object>();
		w_map.put("id", "m_pad_ccgl_kfpd");
		w_map.put("name", "库房盘点");
		w_map.put("img", R.drawable.menu_w_qyyjg);
		w_list.add(w_map);

		w_map = new HashMap<String, Object>();
		w_map.put("id", "m_pad_jszl");
		w_map.put("name", "技术资料");
		w_map.put("img", R.drawable.menu_w_jszl);
		w_list.add(w_map);

		w_map = new HashMap<String, Object>();
		w_map.put("id", "m_pad_tcpc");
		w_map.put("name", "同城首次巡检");
		w_map.put("img", R.drawable.menu_w_jszl);
		w_list.add(w_map);

		w_map = new HashMap<String, Object>();
		w_map.put("id", "m_pad_tsyj");
		w_map.put("name", "预警工单");
		w_map.put("img", R.drawable.menu_w_help);
		w_list.add(w_map);

		w_map = new HashMap<String, Object>();
		w_map.put("id", "m_nt_ccgdcx");
		w_map.put("name", "超长工单");
		w_map.put("img", R.drawable.menu_w_help);
		w_list.add(w_map);

		w_map = new HashMap<String, Object>();
		w_map.put("id", "m_jcsj_bzfy");
		w_map.put("name", "标准费用查询");
		w_map.put("img", R.drawable.menu_w_jszl);
		w_list.add(w_map);

		w_map = new HashMap<String, Object>();
		w_map.put("id", "m_pad_xgmm");
		w_map.put("name", "修改密码");
		w_map.put("img", R.drawable.menu_w_xgmm);
		w_list.add(w_map);

	}

	private void loadMenus() {

		menu = DataCache.getinition().getMenu();
		if (menu == null || menu.isEmpty()) {
			skipActivity(LoginActivity.class);
			finish();
			return;
		}
		setMenus();
	}

	private void setMenus() {

		
		badgeView_rk.setTargetView(null);
		badgeView_kc.setTargetView(null);
		badgeView_fk.setTargetView(null);
		
		
		badgeView_wxbgxg.setTargetView(null);
		badgeView_wxpjxg.setTargetView(null);
		badgeView_wxp2lr.setTargetView(null);
		
		if (currType == 1) {
			
			showProgressDialog();
			Config.getExecutorService().execute(new Runnable() {

				@Override
				public void run() {

					getWebService("query");
				}
			});
			Config.getExecutorService().execute(new Runnable() {

				@Override
				public void run() {

					getWebService("wwgd");
				}
			});
			tv_esp.setTextColor(getResources().getColor(R.color.blue));
			tv_kdg.setTextColor(getResources().getColor(R.color.qianhui));
			tv_js.setTextColor(getResources().getColor(R.color.qianhui));
			tv_kc.setTextColor(getResources().getColor(R.color.qianhui));
			tv_w.setTextColor(getResources().getColor(R.color.qianhui));
			img_esp.setBackgroundResource(R.drawable.btn_esp_up);
			img_kdg.setBackgroundResource(R.drawable.btn_kdg_down);
			img_js.setBackgroundResource(R.drawable.btn_js_down);
			img_kc.setBackgroundResource(R.drawable.btn_kc_down);
			img_w.setBackgroundResource(R.drawable.btn_w_down);
			loadEspMenus();
		} else if (currType == 2) {
			tv_kdg.setTextColor(getResources().getColor(R.color.blue));
			tv_esp.setTextColor(getResources().getColor(R.color.qianhui));
			tv_js.setTextColor(getResources().getColor(R.color.qianhui));
			tv_kc.setTextColor(getResources().getColor(R.color.qianhui));
			tv_w.setTextColor(getResources().getColor(R.color.qianhui));
			img_esp.setBackgroundResource(R.drawable.btn_esp_down);
			img_kdg.setBackgroundResource(R.drawable.btn_kdg_up);
			img_js.setBackgroundResource(R.drawable.btn_js_down);
			img_kc.setBackgroundResource(R.drawable.btn_kc_down);
			img_w.setBackgroundResource(R.drawable.btn_w_down);
			loadKdgMenus();
		} else if (currType == 3) {
			badgeView_wx.setTargetView(null);
			tv_js.setTextColor(getResources().getColor(R.color.blue));
			tv_kc.setTextColor(getResources().getColor(R.color.qianhui));
			tv_kdg.setTextColor(getResources().getColor(R.color.qianhui));
			tv_esp.setTextColor(getResources().getColor(R.color.qianhui));
			tv_w.setTextColor(getResources().getColor(R.color.qianhui));
			img_js.setBackgroundResource(R.drawable.btn_js_up);
			img_kc.setBackgroundResource(R.drawable.btn_kc_down);
			img_esp.setBackgroundResource(R.drawable.btn_esp_down);
			img_kdg.setBackgroundResource(R.drawable.btn_kdg_down);
			img_w.setBackgroundResource(R.drawable.btn_w_down);
			loadJsMenus();
		} else if (currType == 4) {
			tv_js.setTextColor(getResources().getColor(R.color.qianhui));
			tv_kc.setTextColor(getResources().getColor(R.color.blue));
			tv_kdg.setTextColor(getResources().getColor(R.color.qianhui));
			tv_esp.setTextColor(getResources().getColor(R.color.qianhui));
			tv_w.setTextColor(getResources().getColor(R.color.qianhui));
			img_js.setBackgroundResource(R.drawable.btn_js_down);
			img_kc.setBackgroundResource(R.drawable.btn_kc_up);
			img_esp.setBackgroundResource(R.drawable.btn_esp_down);
			img_kdg.setBackgroundResource(R.drawable.btn_kdg_down);
			img_w.setBackgroundResource(R.drawable.btn_w_down);
			loadKcMenus();
		} else if (currType == 5) {
			badgeView_sy.setTargetView(null);
			tv_js.setTextColor(getResources().getColor(R.color.qianhui));
			tv_kc.setTextColor(getResources().getColor(R.color.qianhui));
			tv_kdg.setTextColor(getResources().getColor(R.color.qianhui));
			tv_esp.setTextColor(getResources().getColor(R.color.qianhui));
			tv_w.setTextColor(getResources().getColor(R.color.blue));
			img_js.setBackgroundResource(R.drawable.btn_js_down);
			img_kc.setBackgroundResource(R.drawable.btn_kc_down);
			img_esp.setBackgroundResource(R.drawable.btn_esp_down);
			img_kdg.setBackgroundResource(R.drawable.btn_kdg_down);
			img_w.setBackgroundResource(R.drawable.btn_w_up);
			loadWMenus();
		}
	}

	private void loadEspMenus() {
		gridview.setVisibility(View.GONE);
		listview.setVisibility(View.VISIBLE);

		// data_list = new ArrayList<Map<String, Object>>();
		//
		// for (int i = 0; i < esp_list.size(); i++) {
		// Map<String, Object> map = esp_list.get(i);
		// for (int k = 0; k < menu.size(); k++) {
		// if (map.get("id").equals(menu.get(k))) {
		// //map.put("type", "esp");
		// data_list.add(map);
		// break;
		// }
		// }
		// }
		//
		// sim_adapter = new CurrAdapter(this, data_list, R.layout.include_menu,
		// from, to);
		// // 配置适配器
		// gridview.setAdapter(sim_adapter);
	}

	private void loadKdgMenus() {
		gridview.setVisibility(View.VISIBLE);
		listview.setVisibility(View.GONE);

		// menu.add("m_pad_kdg_fwbgxg_az");

		data_list = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < kdg_list.size(); i++) {
			Map<String, Object> map = kdg_list.get(i);
			for (int k = 0; k < menu.size(); k++) {
				if (map.get("id").equals(menu.get(k))) {
					// map.put("type", "kdg");
					data_list.add(map);
					break;
				}
			}
		}

		sim_adapter = new CurrKdgAdapter(this, data_list,
				R.layout.include_menu, from, to);
		// 配置适配器
		gridview.setAdapter(sim_adapter);

	}

	private void loadKcMenus() {

		gridview.setVisibility(View.VISIBLE);
		listview.setVisibility(View.GONE);

		data_list = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < kc_list.size(); i++) {
			Map<String, Object> map = kc_list.get(i);
			for (int k = 0; k < menu.size(); k++) {
				if (map.get("id").equals(menu.get(k))) {
					// map.put("type", "kc");
					data_list.add(map);
					break;
				}
			}
		}

		sim_adapter = new CurrKdgAdapter(this, data_list,
				R.layout.include_menu, from, to);
		// 配置适配器
		gridview.setAdapter(sim_adapter);

	}

	private void loadJsMenus() {
		gridview.setVisibility(View.VISIBLE);
		listview.setVisibility(View.GONE);
		data_list = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < js_list.size(); i++) {
			Map<String, Object> map = js_list.get(i);
			for (int k = 0; k < menu.size(); k++) {
				if (map.get("id").equals(menu.get(k))) {
					// map.put("type", "js");
					data_list.add(map);
					break;
				}
			}
		}

		sim_adapter = new CurrKdgAdapter(this, data_list,
				R.layout.include_menu, from, to);
		// 配置适配器
		gridview.setAdapter(sim_adapter);
	}

	private void loadWMenus() {
		gridview.setVisibility(View.VISIBLE);
		listview.setVisibility(View.GONE);

		menu.add("m_pad_xgmm");

		// menu.add("m_pad_ckdqkc");
		// menu.add("m_pad_wbdbrk");
		// menu.add("m_pad_wbdbck");

		// menu.add("m_pad_tcpc");
		// menu.add("m_pad_kdg_pqzgjk");
		// menu.add("m_pad_kdg_pqzgjsl");
		// menu.add("m_pad_ryxxlr");

		// menu.add("m_pad_help");
		// menu.add("m_pad_jszl");

		data_list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < w_list.size(); i++) {
			Map<String, Object> map = w_list.get(i);
			for (int k = 0; k < menu.size(); k++) {
				if (map.get("id").equals(menu.get(k))) {
					data_list.add(map);
					break;
				}
			}
		}

		sim_adapter = new CurrKdgAdapter(this, data_list,
				R.layout.include_menu, from, to);
		// 配置适配器
		gridview.setAdapter(sim_adapter);
	}

	private void turnTo(String menu_type) {

		if ("m_pad_sblr".equals(menu_type)) {
			skipActivity(SheBeiPDActivity.class);
			DataCache.getinition().setTitle("新增设备信息");
			return;
		} else if ("m_sbgl_scxjsb_xg".equals(menu_type)) {
			skipActivity(ListKdg.class);
			DataCache.getinition().setQueryType(2901);
			DataCache.getinition().setTitle("巡检设备修改");
			return;
		} else if ("m_pad_fwzxscx".equals(menu_type)) {
			skipActivity(Fwzxscx.class);
			DataCache.getinition().setTitle("服务咨询师查询");
			return;
		} else if ("m_pad_kdg_jdxy_az".equals(menu_type)) {
			skipActivity(ListKdg.class);
			DataCache.getinition().setQueryType(201);
			DataCache.getinition().setTitle("接单响应（安装）");
			return;
		} else if ("m_kdg_jjgd_kuaidigui".equals(menu_type)) {
			skipActivity(ListKdg.class);
			DataCache.getinition().setQueryType(202);
			DataCache.getinition().setTitle("拒绝接单");
			return;
		} else if ("m_pad_kdg_smdw_az".equals(menu_type)) {
			skipActivity(ListKdg.class);
			DataCache.getinition().setQueryType(203);
			DataCache.getinition().setTitle("上门定位（安装）");
			return;
		} else if ("m_pad_kdg_fwbg_az".equals(menu_type)) {
			skipActivity(ListKdg.class);
			DataCache.getinition().setQueryType(204);
			DataCache.getinition().setTitle("服务报告（安装）");
			return;
		} else if ("m_pad_kdg_ecsm_az".equals(menu_type)) {
			skipActivity(ListKdg.class);
			DataCache.getinition().setQueryType(2042);
			DataCache.getinition().setTitle("二次上门（安装）");
			return;
		} else if ("m_pad_kdg_fwbgxg_az".equals(menu_type)) {
			skipActivity(ListKdg.class);
			DataCache.getinition().setQueryType(2041);
			DataCache.getinition().setTitle("服务报告修改（安装）");
			return;
		} else if ("m_pad_kdg_jqgdxz".equals(menu_type)) {
			skipActivity(JqgdcxKdg.class);
			DataCache.getinition().setTitle("近期工单查询");
			return;
		} else if ("m_zh_xjjiedan".equals(menu_type)) {
			skipActivity(ListKdg.class);
			DataCache.getinition().setQueryType(208);
			DataCache.getinition().setTitle("巡检接单");
			return;
		} else if ("m_zh_xjdingwei".equals(menu_type)) {
			skipActivity(ListKdg.class);
			DataCache.getinition().setQueryType(209);
			DataCache.getinition().setTitle("巡检定位");
			return;
		} else if ("m_zh_sjxj".equals(menu_type)) {
			skipActivity(ListKdg.class);
			DataCache.getinition().setQueryType(210);
			DataCache.getinition().setTitle("巡检服务报告");
			return;
		} else if ("m_pad_kdg_dhjrs_az".equals(menu_type)) {
			skipActivity(ListKdg.class);
			DataCache.getinition().setQueryType(2101);
			DataCache.getinition().setTitle("安装条件确认");
			return;
		} else if ("m_pad_kdg_jdxy_wx".equals(menu_type)) {
			skipActivity(ListKdg.class);
			DataCache.getinition().setQueryType(2201);
			DataCache.getinition().setTitle("接单响应(维修)");
			return;
		} else if ("m_pad_kdg_smdw_wx".equals(menu_type)) {
			skipActivity(ListKdg.class);
			DataCache.getinition().setQueryType(2202);
			DataCache.getinition().setTitle("上门定位(维修)");
			return;
		} else if ("m_pad_smewm_wx".equals(menu_type)) {
			skipActivity(ListKdg.class);
			DataCache.getinition().setQueryType(2207);
			DataCache.getinition().setTitle("扫描二维码(维修)");
			return;
		} else if ("m_pad_kdg_fwbg_wx".equals(menu_type)) {
			skipActivity(ListKdg.class);
			DataCache.getinition().setQueryType(2203);
			DataCache.getinition().setTitle("服务报告(维修)");
			return;
		} else if ("m_pad_kdg_ecsm_wx".equals(menu_type)) {
			skipActivity(ListKdg.class);
			DataCache.getinition().setQueryType(2206);
			DataCache.getinition().setTitle("二次上门(维修)");
			return;
		} else if ("m_pad_kdg_fwbgxg_wx".equals(menu_type)) {
			skipActivity(ListKdg.class);
			DataCache.getinition().setQueryType(2205);
			DataCache.getinition().setTitle("服务报告修改（维修）");
			return;
		} else if ("m_kdg_pjxg".equals(menu_type)) {
			skipActivity(ListKdg.class);
			DataCache.getinition().setQueryType(2208);
			DataCache.getinition().setTitle("配件修改（维修）");
			return;
		} else if ("m_pad_p2gd".equals(menu_type)) {
			skipActivity(ListKdg.class);
			DataCache.getinition().setQueryType(2209);
			DataCache.getinition().setTitle("P2超时原因录入");
			return;
		} else if ("m_kdg_xing_sbxxpd".equals(menu_type)) {
			skipActivity(SbxxpdActivity.class);
			DataCache.getinition().setTitle("设备信息核对");
			return;
		} else if ("m_pad_kdg_zzzp_wx".equals(menu_type)) {
			skipActivity(ListKdg.class);
			DataCache.getinition().setQueryType(2204);
			DataCache.getinition().setTitle("组长转派");
			return;
		} else if ("m_pad_kdg_xj_jdxy".equals(menu_type)) {
			skipActivity(ListKdg.class);
			DataCache.getinition().setQueryType(2301);
			DataCache.getinition().setTitle("接单响应(巡检)");
			return;
		} else if ("m_pad_kdg_xj_smdw".equals(menu_type)) {
			skipActivity(ListKdg.class);
			DataCache.getinition().setQueryType(2302);
			DataCache.getinition().setTitle("上门定位(巡检)");
			return;
		} else if ("m_kdg_xj_xddw".equals(menu_type)) {
			skipActivity(Xddwcx.class);
			DataCache.getinition().setTitle("巡检附近设备(巡检)");
			return;
		} else if ("m_wdxjfwbg".equals(menu_type)) {
			skipActivity(FwbgXjWd.class);
			DataCache.getinition().setTitle("网点巡检");
			return;
		} else if ("m_wdxjbg_sc".equals(menu_type)) {
			skipActivity(ListKdg.class);
			DataCache.getinition().setQueryType(2306);
			DataCache.getinition().setTitle("巡检报告上传");
			return;
		} else if ("m_wdxjbg_xg".equals(menu_type)) {
			skipActivity(ListKdg.class);
			DataCache.getinition().setQueryType(2307);
			DataCache.getinition().setTitle("巡检报告修改");
			return;
		} else if ("m_pad_kdg_pqzgjk".equals(menu_type)) {
			skipActivity(Zzpqgdcx.class);
			DataCache.getinition().setTitle("片区工单查询");
			return;
		} else if ("m_pad_kdg_pqzgjsl".equals(menu_type)) {
			skipActivity(Pqzgjsl.class);
			DataCache.getinition().setTitle("片区主管及时率");
			return;
		} else if ("m_pad_kdg_xj_fwbg".equals(menu_type)) {
			skipActivity(ListKdg.class);
			DataCache.getinition().setQueryType(2303);
			DataCache.getinition().setTitle("服务报告(巡检)");
			return;
		} else if ("m_pad_kdg_xj_fwbgxg".equals(menu_type)) {
			skipActivity(ListKdg.class);
			DataCache.getinition().setQueryType(2304);
			DataCache.getinition().setTitle("服务报告修改(巡检)");
			return;
		} else if ("m_pad_qyjjg".equals(menu_type)) {
			skipActivity(QyxxActivity.class);
			DataCache.getinition().setTitle("区域信息");
			return;
		} else if ("m_pad_yhkxx".equals(menu_type)) {
			skipActivity(YHKInfoActivity.class);
			DataCache.getinition().setTitle("银行卡信息");
			return;
		} else if ("m_pad_zyxx".equals(menu_type)) {
			skipActivity(XxglActivity.class);
			DataCache.getinition().setTitle("消息中心");
			return;
		} else if ("m_pad_xgmm".equals(menu_type)) {
			skipActivity(ChangePasswordActivity.class);
			DataCache.getinition().setTitle("修改密码");
			return;
		} else if ("m_pad_ryxx".equals(menu_type)) {
			skipActivity(GzpActivity.class);
			DataCache.getinition().setTitle("工作证");
			return;
		} else if ("m_pad_ryxxlr".equals(menu_type)) {
			skipActivity(GzpLrActivity.class);
			DataCache.getinition().setTitle("工作证录入");
			return;
		} else if ("m_pad_ckdqkc".equals(menu_type)) {
			skipActivity(KccxChooseActivity.class);
			DataCache.getinition().setTitle("库存-当前库存查询");
			return;
		} else if ("m_pad_wbdbrk".equals(menu_type)) {
			skipActivity(WbdbrkListActivity.class);
			DataCache.getinition().setTitle("库存-外部调拨入库");
			return;
		} else if ("m_pad_wbdbck".equals(menu_type)) {
			skipActivity(WbdbckActivity.class);
			DataCache.getinition().setTitle("库存-外部调拨出库");
			return;
		} else if ("m_pad_pjfk".equals(menu_type)) {
			skipActivity(WbdbckActivity.class);
			DataCache.getinition().setTitle("库存-配件返库");
			return;
		} else if ("m_pad_cksqd".equals(menu_type)) {
			skipActivity(SqdActivity.class);
			DataCache.getinition().setTitle("库存-申请单录入");
			return;
		} else if ("m_pad_cksqdcx".equals(menu_type)) {
			skipActivity(SqdcxkListActivity.class);
			DataCache.getinition().setTitle("库存-申请单查询作废");
			return;
		} else if ("m_pad_thck".equals(menu_type)) {
			skipActivity(ListKdg.class);
			DataCache.getinition().setQueryType(2801);
			DataCache.getinition().setTitle("库存-调拨差异查询");
			return;
		} else if ("m_pad_ccgl_kfpd".equals(menu_type)) {
			skipActivity(KcpdActivity.class);
			DataCache.getinition().setTitle("库房盘点");
			return;
		} else if ("m_pad_kfdbrc_zyxx".equals(menu_type)) {
			skipActivity(KcxxActivity.class);
			DataCache.getinition().setTitle("库房重要消息 ");
			return;
		} else if ("m_pad_help".equals(menu_type)) {
			skipActivity(HelpActivity.class);
			DataCache.getinition().setTitle("帮助");
			return;
		} else if ("m_pad_jszl".equals(menu_type)) {
			skipActivity(JszlActivity.class);
			DataCache.getinition().setTitle("技术资料");
			return;
		} else if ("m_pad_tcpc".equals(menu_type)) {
			skipActivity(TcpcActivity.class);
			DataCache.getinition().setTitle("同城首次巡检");
			return;
		} else if ("m_jcsj_bzfy".equals(menu_type)) {
			skipActivity(BzfyActivity.class);
			DataCache.getinition().setTitle("标准费用查询");
			return;
		} else if ("m_pad_tsyj".equals(menu_type)) {
			skipActivity(Yjtx.class);
			DataCache.getinition().setTitle("预警工单");
			return;
		} else if ("m_nt_ccgdcx".equals(menu_type)) {
			skipActivity(Ccgd.class);
			DataCache.getinition().setTitle("超长工单");
			return;
		}

	}

	private void gotoOtherPage() {
		// String url = "http://www.baidu.com";
		// switch (num) {
		// case 1:
		// url = "http://www.baidu.com";
		// break;
		// case 2:
		// url = "http://www.sina.com";
		// break;
		// case 3:
		// url = "http://china.nba.com/boxscore/";
		// break;
		// default:
		// break;
		// }
		// Uri uri = Uri.parse(url);
		// Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		// startActivity(intent);
	}

	private void changeBackground() {
		sy_top_1.setBackgroundResource(R.drawable.btn_sy_point_up);
		sy_top_2.setBackgroundResource(R.drawable.btn_sy_point_up);
		switch (num) {
		case 1:
			sy_top_1.setBackgroundResource(R.drawable.btn_sy_point_down);
			linear_sy_top.setBackgroundResource(R.drawable.a);
			break;
		case 2:
			sy_top_2.setBackgroundResource(R.drawable.btn_sy_point_down);
			linear_sy_top.setBackgroundResource(R.drawable.b);
			break;
		default:
			break;
		}

	}

	@Override
	protected void initVariable() {

	}

	protected void getWebService(String s) {
		if ("query".equals(s)) {
			try {
				String sqlid = "_PAD_SHGL_KDG_GDALL_WWG";
				String cs = DataCache.getinition().getUserId() + "*"
						+ DataCache.getinition().getUserId();
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						sqlid, cs, "uf_json_getdata", this);
				String flag = jsonObject.getString("flag");
				data_listview = new ArrayList<Map<String, Object>>();
				JSONArray jsonArray = jsonObject.getJSONArray("tableA");
				if (Integer.parseInt(flag) > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, Object> item = new HashMap<String, Object>();
						String timeff = "";
						item.put("textView1",
								getListItemIcon(temp.getString("ywhy_bm")));
						timeff = temp.getString("bzsj");
						item.put("bzsj", timeff);
						item.put("faultuser", temp.getString("xqmc") + "   "
								+ temp.getString("xxdz"));
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
						item.put("ds", temp.getString("ds"));
						item.put("fwnr", temp.getString("fwnr"));
						item.put("sflx", temp.getString("sflx"));
						item.put("ywhy", temp.getString("ywhy"));
						item.put("ywhy_bm", temp.getString("ywhy_bm"));
						item.put("sblx", temp.getString("sblx"));
						item.put("sblx_mc", temp.getString("sblx_mc"));
						item.put("kzzf2", temp.getString("kzzf2"));
						item.put("sbxh", temp.getString("sbxh"));
						item.put("wxts", temp.getString("wxts"));
						item.put("khlxr", temp.getString("khlxr"));
						item.put("yysj", temp.getString("yysj"));
						item.put("lxdh", temp.getString("lxdh"));
						item.put("dygdh", temp.getString("dygdh"));
						item.put("sfecgd", temp.getString("sfecgd"));
						item.put("sbewm", temp.getString("sbewm"));
						item.put("axdh", temp.getString("axdh"));
						item.put("gxh", temp.getString("gxh"));
						item.put("jgh", temp.getString("jgh"));
						item.put("anlx2", temp.getString("anlx2"));
						
						item.put("wcqkbm", temp.getString("wcqkbm"));
						
						item.put("dygdh1", temp.getString("dygdh1"));
						
						item.put("timemy", temp.getString("ywlx"));
						item.put("datemy", temp.getString("djzt2"));
						data_listview.add(item);
					}

				}
				ServiceReportCache.setObjectdata(data_listview);
				Message msg = new Message();
				msg.what = Constant.SUCCESS;// 成功
				handler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}
		}
		if ("wwgd".equals(s)) {
			try {
				// 查询库房重要消息
				try {
					kfzyxx_list = new ArrayList<Map<String, Object>>();
					String userid = DataCache.getinition().getUserId();
					String cs = userid + "*" + userid + "*" + userid + "*"
							+ userid + "*" + userid;
					JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
							"_PAD_KFGL_ZYXX", cs, "uf_json_getdata", this);
					String flag = jsonObject.getString("flag");
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					if (Integer.parseInt(flag) > 0) {
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject temp = jsonArray.getJSONObject(i);
							Map<String, Object> item = new HashMap<String, Object>();
							item.put("menu", temp.getString("meun"));
							kfzyxx_list.add(item);
						}
						Map<String, Object> item = new HashMap<String, Object>();
						item.put("menu", "m_pad_kfdbrc_zyxx");
						kfzyxx_list.add(item);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				try {
					wx_list = new ArrayList<Map<String, Object>>();
					String userid = DataCache.getinition().getUserId();
					String cs = userid + "*" + userid + "*" + userid;
					JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
							"_PAD_SHGL_WX_DWCSXTJ_MX", cs, "uf_json_getdata", this);
					String flag = jsonObject.getString("flag");
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					if (Integer.parseInt(flag) > 0) {
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject temp = jsonArray.getJSONObject(i);
							Map<String, Object> item = new HashMap<String, Object>();
							item.put("menu", temp.getString("meun"));
							wx_list.add(item);
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
				}

				String userid = DataCache.getinition().getUserId();
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SHGL_KDG_WWGDS", userid + "*" + userid,
						"uf_json_getdata", this);
				String flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					String txzd = "";
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						txzd = temp.getString("wwgd");

					}
					AppUtils.sendBadgeNumber(this, "");// 清空
					AppUtils.sendBadgeNumber(this, txzd);// 设置角标显示
					ts_num_msg_kdg = "您有" + txzd + "个未完成工单";
					Message msg = new Message();
					msg.what = Constant.NUM_7;// 成功
					handler.sendMessage(msg);
				} else {
					ts_num_msg_kdg = "您有0个未完成工单";
					Message msg = new Message();
					msg.what = Constant.NUM_7;// 成功
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}
		}

		if ("cdtx".equals(s)) {
			try {
				String sql = "update kdg_cdxxb set  sfyd = '1'  where zbh = '"
						+ zbh + "'";
				JSONObject json = callWebserviceImp.getWebServerInfo("_RZ",
						sql, "", "", "uf_json_setdata", this);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if ("updateToken".equals(s)) {
			try {
				String bbh = getVersion();
				String brand = android.os.Build.BRAND;
				String cs = DataCache.getinition().getUserId() + "*PAM*"
						+ token + "*PAM*" + bbh + "*PAM*" + brand;
				JSONObject json = callWebserviceImp.getWebServerInfo(
						"c#_PAD_IOS_TOKEN", cs, "0001", "0001",
						"uf_json_setdata2", this);
				System.out.println(json);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressLint("NewApi")
	private void showLoginMsg(final Map<String, String> map) {

		View view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_popupwin, null);
		WebView webview = (WebView) view.findViewById(R.id.webview);
		webview.loadUrl(map.get("bz").toString());
		AlertDialog.Builder builder = new Builder(this);
		builder.setCancelable(false);
		builder.setView(view);
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface face, int paramAnonymous2Int) {

				if ("0".equals(map.get("xxzt"))) {
					Config.getExecutorService().execute(new Runnable() {

						@Override
						public void run() {
							try {
								String sql = "insert into esp_sjxx_zjb (rybm,xxbm,lrsj,xxzt) values ("
										+ "'"
										+ DataCache.getinition().getUserId()
										+ "',"
										+ "'"
										+ map.get("zbh")
										+ "',"
										+ "sysdate," + "'1')";
								callWebserviceImp.getWebServerInfo("_RZ", sql,
										DataCache.getinition().getUserId(),
										"uf_json_setdata",
										getApplicationContext());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}
				map.put("xxzt", "1");
				DataCache.getinition().setLogin_show_map(map);
			}
		});
		builder.create().show();
	}

	/**
	 * 重写回退键
	 */
	@Override
	public void onBackPressed() {
		if ((System.currentTimeMillis() - mExitTime) > 2000) {
			Toast.makeText(getApplicationContext(), "再按一次退出程序",
					Toast.LENGTH_SHORT).show();
			mExitTime = System.currentTimeMillis();
		} else {
			Intent intent = new Intent(getApplicationContext(),
					LoginActivity.class);
			startActivity(intent);
			finish();
		}
	}

	private class CurrKdgAdapter extends SimpleAdapter {

		public CurrKdgAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);
		}

		@Override
		public View getView(final int position, View convertView,
				final ViewGroup parent) {
			final View view = super.getView(position, convertView, parent);
			final LinearLayout l = (LinearLayout) view
					.findViewById(R.id.ll_menu);
			if (position % 4 == 0) {
				l.setBackgroundResource(R.drawable.menu_esp_yellow);
			} else if (position % 4 == 1) {
				l.setBackgroundResource(R.drawable.menu_esp_red);
			} else if (position % 4 == 2) {
				l.setBackgroundResource(R.drawable.menu_esp_green);
			} else if (position % 4 == 3) {
				l.setBackgroundResource(R.drawable.menu_esp_blue);
			}

			TextView tv_id = (TextView) view.findViewById(R.id.menu_id);
			String menu = tv_id.getText().toString();

			if (kfzyxx_list.size() > 0 && currType==5) {
				if ("m_pad_kfdbrc_zyxx".equals(menu)) {
					for (int i = 0; i < kfzyxx_list.size(); i++) {
						Map<String, Object> map = kfzyxx_list.get(i);
						if (menu.equals(map.get("menu"))) {
							badgeView_zyxx.setTargetView(l);
							badgeView_zyxx.setBadgeMargin(0, 15, 10, 0);// 设置边界
							badgeView_zyxx.setTextSize(6);
							badgeView_zyxx.setText("");
						}
					}
					
				}
				if ("m_pad_wbdbrk".equals(menu)) {
					for (int i = 0; i < kfzyxx_list.size(); i++) {
						Map<String, Object> map = kfzyxx_list.get(i);
						if (menu.equals(map.get("menu"))) {
							badgeView_rk.setTargetView(l);
							badgeView_rk.setBadgeMargin(0, 15, 10, 0);// 设置边界
							badgeView_rk.setTextSize(6);
							badgeView_rk.setText("");
						}
					}
				}
				if ("m_pad_ckdqkc".equals(menu)) {
					for (int i = 0; i < kfzyxx_list.size(); i++) {
						Map<String, Object> map = kfzyxx_list.get(i);
						if (menu.equals(map.get("menu"))) {
							badgeView_kc.setTargetView(l);
							badgeView_kc.setBadgeMargin(0, 15, 10, 0);// 设置边界
							badgeView_kc.setTextSize(6);
							badgeView_kc.setText("");
						}
					}
				}
				if ("m_pad_pjfk".equals(menu)) {
					for (int i = 0; i < kfzyxx_list.size(); i++) {
						Map<String, Object> map = kfzyxx_list.get(i);
						if (menu.equals(map.get("menu"))) {
							badgeView_fk.setTargetView(l);
							badgeView_fk.setBadgeMargin(0, 15, 10, 0);// 设置边界
							badgeView_fk.setTextSize(6);
							badgeView_fk.setText("");
						}
					}
				}
			}
			if (wx_list.size() > 0 && currType==3) {
				if ("m_kdg_pjxg".equals(menu)) {
					for (int i = 0; i < wx_list.size(); i++) {
						Map<String, Object> map = wx_list.get(i);
						if (menu.equals(map.get("menu"))) {
							badgeView_wxpjxg.setTargetView(l);
							badgeView_wxpjxg.setBadgeMargin(0, 15, 10, 0);// 设置边界
							badgeView_wxpjxg.setTextSize(6);
							badgeView_wxpjxg.setText("");
						}
					}
				}

				if ("m_pad_kdg_fwbgxg_wx".equals(menu)) {
					for (int i = 0; i < wx_list.size(); i++) {
						Map<String, Object> map = wx_list.get(i);
						if (menu.equals(map.get("menu"))) {
							badgeView_wxbgxg.setTargetView(l);
							badgeView_wxbgxg.setBadgeMargin(0, 15, 10, 0);// 设置边界
							badgeView_wxbgxg.setTextSize(6);
							badgeView_wxbgxg.setText("");
						}
					}
				}
				if ("m_pad_p2gd".equals(menu)) {
					for (int i = 0; i < wx_list.size(); i++) {
						Map<String, Object> map = wx_list.get(i);
						if (menu.equals(map.get("menu"))) {
							badgeView_wxp2lr.setTargetView(l);
							badgeView_wxp2lr.setBadgeMargin(0, 15, 10, 0);// 设置边界
							badgeView_wxp2lr.setTextSize(6);
							badgeView_wxp2lr.setText("");
						}
					}
				}
			}

			return view;
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
				Map<String, Object> item = data_listview.get(position);
				String sfcs = (String) item.get("dygdh1");
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

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			switch (msg.what) {
			case Constant.NETWORK_ERROR:
				dialogShowMessage_P(Constant.NETWORK_ERROR_STR, null);
				break;

			case Constant.SUCCESS:

				adapter_listview = new CurrAdapter(MainActivity.this,
						ServiceReportCache.getObjectdata(),
						R.layout.listview_dispatchinginformationreceiving_item,
						from_listview, to_listview);
				listview.setAdapter(adapter_listview);
				adapter_listview.notifyDataSetChanged();
				break;
			case Constant.FAIL:
				listview.setAdapter(null);
				if (adapter_listview != null) {
					adapter_listview.notifyDataSetChanged();
				}

				break;
			case Constant.NUM_7:
				tishi.setText(ts_num_msg_kdg);
				if (kfzyxx_list.size() > 0) {
					badgeView_sy.setTargetView(tab_bottom_w);
					badgeView_sy.setBadgeMargin(30, 5, 15, 5);// 设置边界
					badgeView_sy.setTextSize(6);
					badgeView_sy.setText("");
					
				}
				if (wx_list.size() > 0) {
					badgeView_wx.setTargetView(tab_bottom_js);
					badgeView_wx.setBadgeMargin(30, 5, 15, 5);// 设置边界
					badgeView_wx.setTextSize(6);
					badgeView_wx.setText("");
					
				}
				break;
			case Constant.NUM_8:
				changeBackground();
				break;
			case Constant.NUM_9:
				tv_ry.setText(token);
				break;
			}

		};
	};

}