package com.nt.activity.kdg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.activity.main.MainActivity;
import com.nt.activity.util.BaiduMapActivity;
import com.nt.cache.DataCache;
import com.nt.cache.ServiceReportCache;
import com.nt.common.Constant;
import com.nt.utils.Config;

/**
 * 快递柜-接单响应（维修）
 * 
 * @author zdkj
 *
 */
public class JdxyKdgWx extends FrameActivity {

	private Button confirm, cancel;
	private String flag, zbh, type = "1", message, tsxx = "确认提交？";
	private TextView tv_xxdz;
	private ArrayList<Map<String, String>> data_bjxx;
	private LinearLayout ll_yybj;
	private ImageView iv_yybj;
	private boolean ishide = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_kdg_jdxy_wx);
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
		confirm.setText("接单");
		cancel.setText("拒绝");
		tv_xxdz = (TextView) findViewById(R.id.tv_5);
		ll_yybj = (LinearLayout) findViewById(R.id.ll_yybj);
		iv_yybj = (ImageView) findViewById(R.id.iv_yybj);
	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());

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
		((TextView) findViewById(R.id.tv_5)).setText("   "+itemmap.get("xxdz")
				.toString());
		((TextView) findViewById(R.id.tv_6)).setText(itemmap.get("gzxx")
				.toString());
		((TextView) findViewById(R.id.tv_7)).setText(itemmap.get("ywlx")
				.toString());
		((TextView) findViewById(R.id.tv_8)).setText(itemmap.get("bz")
				.toString());
		((TextView) findViewById(R.id.tv_9)).setText(itemmap.get("sf")
				.toString());
		((TextView) findViewById(R.id.tv_sflx)).setText(itemmap.get("axdh")
				.toString());
		((TextView) findViewById(R.id.tv_bzsj)).setText(itemmap.get("bzsj")
				.toString());
		((TextView) findViewById(R.id.tv_sbsx)).setText(itemmap.get("sflx")
				.toString());
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
					dialogShowMessage("确认拒绝接单？",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface face,
										int paramAnonymous2Int) {
									showProgressDialog();
									Config.getExecutorService().execute(
											new Runnable() {

												@Override
												public void run() {
													type = "2";
													getWebService("submit");
												}
											});
								}
							}, null);
					break;
				case R.id.confirm:
					showProgressDialog();
					Config.getExecutorService().execute(new Runnable() {

						@Override
						public void run() {
							type = "1";
							getWebService("getxx");
						}
					});
					break;
				case R.id.ll_yybj:
					showBjxx();
					break;
				case R.id.iv_yybj:
					showBjxx();
					break;
				default:
					break;
				}

			}
		};

		topBack.setOnClickListener(onClickListener);
		cancel.setOnClickListener(onClickListener);
		confirm.setOnClickListener(onClickListener);
		ll_yybj.setOnClickListener(onClickListener);
		iv_yybj.setOnClickListener(onClickListener);
		
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
		if ("query".equals(s)) {
			try {
				data_bjxx = new ArrayList<Map<String, String>>();
				// 已换配件信息
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_JCSJ_JDXY_BJXX", zbh, "uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						HashMap<String, String> item = new HashMap<String, String>();
						item.put("bjmc", temp.getString("bjmc"));
						data_bjxx.add(item);
					}
				}
				Message msg = new Message();
				msg.what = Constant.NUM_7;
				handler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;
				handler.sendMessage(msg);
			}
		}

		if ("getxx".equals(s)) {
			try {

				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SBGL_JSXYTSXX", zbh, "uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						HashMap<String, String> item = new HashMap<String, String>();
						tsxx = temp.getString("tsxx");
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
				String typeStr = "1".equals(type) ? "jdxy" : "jjgd";
				message = "1".equals(type) ? "接单成功！请在30分钟内与客户取得联系进行响应。"
						: "拒单成功！";
				String str = zbh + "*PAM*" + DataCache.getinition().getUserId();
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

	@SuppressLint("ResourceAsColor")
	private void addBjxx() {
		int num = data_bjxx.size();
		for (int i = 0; i < num; i++) {
			Map<String, String> map = data_bjxx.get(i);
			String bjmc = map.get("bjmc");
			TextView tv = new TextView(getApplicationContext());
			tv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 40));
			tv.setText(bjmc);
			tv.setTextColor(R.color.black);
			ll_yybj.addView(tv);
		}

		showBjxx();
	}
	
	private void showBjxx(){
		int childrennum = ll_yybj.getChildCount();
		if (ishide) {
			ishide = false;
			for (int i = 0; i < childrennum; i++) {
				ll_yybj.getChildAt(i).setVisibility(View.VISIBLE);
				iv_yybj.setImageResource(R.drawable.btn_img_down);
			}
		} else {
			ishide = true;
			for (int i = 0; i < childrennum; i++) {
				if (i > 1) {
					ll_yybj.getChildAt(i).setVisibility(View.GONE);
					iv_yybj.setImageResource(R.drawable.btn_img_right);
				}
			}
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.FAIL:
				dialogShowMessage_P("失败，请检查后重试...错误标识：" + flag, null);
				break;
			case Constant.SUCCESS:
				dialogShowMessage_P(message,
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
				dialogShowMessage_P(tsxx,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								showProgressDialog();
								Config.getExecutorService().execute(
										new Runnable() {
											@Override
											public void run() {
												type = "1";
												getWebService("submit");
											}
										});
							}
						});
				break;
			case Constant.NUM_7:
				addBjxx();
				break;
			}
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		}
	};
}