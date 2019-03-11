package com.nt.activity.login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.nt.R;
import com.nt.Parser.JSONObjectParser;
import com.nt.activity.FrameActivity;
import com.nt.activity.main.MainActivity;
import com.nt.activity.register.RegisterActivity;
import com.nt.activity.w.SetParams;
import com.nt.cache.DataCache;
import com.nt.common.Constant;
import com.nt.utils.Config;

/**
 * 登录页面
 * 
 * @author
 */
@SuppressLint("HandlerLeak")
public class LoginActivity extends FrameActivity {

	private EditText userId, userPs;
	private Button login;
	private String id, ps, flag, flag_DL, msgStr;
	private CheckBox saveuser, saveps, autologin;
	private SharedPreferences spf;
	private SharedPreferences.Editor spfe;
	private JSONObject jsonObject;
	private TextView tv_register, tv_sz;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		initView();// 是否自动登录
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initVariable();
		initListeners();

	}

	@Override
	protected void initVariable() {
		userId = (EditText) findViewById(R.id.userId);
		userPs = (EditText) findViewById(R.id.userPs);
		login = (Button) findViewById(R.id.login);

		saveuser = (CheckBox) findViewById(R.id.saveuser);
		saveps = (CheckBox) findViewById(R.id.saveps);
		autologin = (CheckBox) findViewById(R.id.autologin);

		tv_register = (TextView) findViewById(R.id.tv_register);
		tv_register.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		tv_sz = (TextView) findViewById(R.id.tv_sz);
		tv_sz.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		userId.setText(id);
		userPs.setText(ps);

		PackageInfo packageInfo = null;
		try {
			packageInfo = getPackageManager().getPackageInfo(getPackageName(),
					0);

			TextView v = (TextView) findViewById(R.id.vers);
			v.setText("V: " + packageInfo.versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 是否自动登录
	 */
	@Override
	protected void initView() {

		spf = getSharedPreferences("loginsp", LoginActivity.MODE_PRIVATE);
		boolean autologin = spf.getBoolean("autologin", false);
		id = spf.getString("userid", "");
		ps = spf.getString("userPs", "");
		spfe = spf.edit();
		spfe.putString("URL", Constant.STM_WEBSERVICE_URL_dx);
	}

	@Override
	protected void initListeners() {

		// 登录
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				id = userId.getText().toString().trim();
				ps = userPs.getText().toString().trim();
				if (!id.equals("") && !ps.equals("")) {
					showProgressDialog();
					Config.getExecutorService().execute(new Runnable() {
						@Override
						public void run() {
							getWebService("Login");
						}
					});
				} else {
					msgStr = "账号密码不能为空";
					Message msg = new Message();
					msg.what = Constant.FAIL;// 空
					handler.sendMessage(msg);
				}
			}
		});

		tv_register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this,
						RegisterActivity.class);
				startActivity(intent);
			}
		});

		tv_sz.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, SetParams.class);
				startActivity(intent);
			}
		});

	}

	@Override
	protected void getWebService(String s) {

		if ("Login".equals(s)) {

			try {
				jsonObject = callWebserviceImp.getWebServerInfo("_DL_2", id
						+ "*" + id + "*" + ps, "uf_json_getdata", this);
				flag_DL = jsonObject.getString("flag");
				if (Integer.parseInt(flag_DL) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					JSONObject temp = jsonArray.getJSONObject(0);
					id = temp.getString("userid");
					DataCache.getinition().setUserId(id);
					Config.getExecutorService().execute(new Runnable() {
						@Override
						public void run() {
							getWebService("权限");
						}
					});
					//

					DataCache.getinition().setUsername(
							temp.getString("username"));

					// 是否保存用户名和密码
					if (saveuser.isChecked()) {
						spfe.putString("userid", userId.getText().toString()
								.trim());
						spfe.putString("userId", id);
					} else {
						spfe.putString("userid", "");
						spfe.putString("userId", "");
					}
					if (saveps.isChecked()) {
						spfe.putString("userPs", ps);
					} else {
						spfe.putString("userPs", "");
					}
					if (autologin.isChecked()) {
						spfe.putBoolean("autologin", true);
					} else {
						spfe.putBoolean("autologin", false);
					}

					spfe.commit();

				} else {
					if (flag_DL.equals("4")) {
						msgStr = "密码输入错误3次,帐号已锁定,请联系管理员解锁!";
					} else {
						msgStr = "请输入正确的密码!输入密码错误超过3次,系统将锁定帐号。";
					}
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
		}

		if (s.equals("权限")) {
			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo("_PAD_GNQX", id
						+ "*" + id, "uf_json_getdata", this);
				Log.e("dd", jsonObject.toString());
				flag = jsonObject.getString("flag");

				if (Integer.parseInt(flag) > 0) {

					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					List<String> menu_name = new ArrayList<String>();
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject object = jsonArray.getJSONObject(i);

						menu_name.add(object.getString("menu_name"));
					}
					DataCache.getinition().setMenu(menu_name);
					Message msg = new Message();
					msg.what = Constant.SUCCESS;// 成功
					handler.sendMessage(msg);
				} else {
					msgStr = "没有菜单权限，请联系管理员!";
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

		}

		if ("getTsxx".equals(s)) {
			try {
				try {
					// 查询登陆后 弹出框显示信息
					jsonObject = callWebserviceImp.getWebServerInfo(
							"_PAD_YGXX_XXNR1", spf.getString("userId", ""),
							"uf_json_getdata", this);
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					JSONObject temp = jsonArray.getJSONObject(0);
					Map<String, String> item = new HashMap<String, String>();
					item.put("zdrq", temp.getString("zdrq"));
					item.put("var_kzzd1", temp.getString("var_kzzd1"));
					item.put("ryid", temp.getString("ryid"));
					item.put("xxzt", temp.getString("xxzt"));
					item.put("zbh", temp.getString("zbh"));
					item.put("bz", temp.getString("bz"));
					DataCache.getinition().setLogin_show_map(item);
				} catch (Exception e) {

				}

				jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SJXX_ZDKJ", "", "uf_json_getdata", this);
				String flag2 = jsonObject.getString("flag");
				if (Integer.parseInt(flag2) > 0) {
					JSONArray array = jsonObject.getJSONArray("tableA");
					JSONObject temp = array.getJSONObject(0);
					DataCache.getinition().setTsxx(temp.getString("ts_msg"));
					Config.getExecutorService().execute(new Runnable() {
						@Override
						public void run() {
							getWebService("getYHKxx");
						}
					});
				} else {
					Config.getExecutorService().execute(new Runnable() {
						@Override
						public void run() {
							getWebService("getYHKxx");
						}
					});
				}

			} catch (Exception e) {
				Message msg = Message.obtain();
				msg.what = Constant.NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}
		}

		if ("getYHKxx".equals(s)) {
			try {
				// 查询银行卡信息是否完善
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_YHK_CX", DataCache.getinition().getUserId(),
						"uf_json_getdata", this);
				JSONArray jsonArray = jsonObject.getJSONArray("tableA");
				jsonObject = (JSONObject) jsonArray.get(0);
				if (!"1".equals(jsonObject.getString("yhk"))) {
					DataCache.getinition().setHasYHK(true);
				} else {
					DataCache.getinition().setHasYHK(false);
				}
				Message msg = Message.obtain();
				msg.what = Constant.SUCCESS;
				handler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = Message.obtain();
				msg.what = Constant.NETWORK_ERROR;
				handler.sendMessage(msg);
			}
		}

		if ("fb".equals(s)) {

			try {
				jsonObject = callWebserviceImp.getWebServerInfo("_PAD_QX_FBF",
						id, "uf_json_getdata", this);

				flag = jsonObject.getString("flag");

				if (Integer.parseInt(flag) > 0) {

					Config.getExecutorService().execute(new Runnable() {

						@Override
						public void run() {

							Config.writeFile("_PAD_QX_FBF",
									jsonObject.toString(),
									getApplicationContext());
						}
					});

					getWebService("xq");

				} else {
					Message msg = new Message();
					msg.what = Constant.FAIL;// 失败
					handler.sendMessage(msg);
				}

			} catch (Exception e) {
				// Toast.makeText(this, jsonObject.toString(), 1).show();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}
		}

		if ("xq".equals(s)) {

			try {
				jsonObject = callWebserviceImp.getWebServerInfo("_PAD_PQSZ",
						"", "uf_json_getdata", this);

				flag = jsonObject.getString("flag");

				if (Integer.parseInt(flag) > 0) {

					Config.getExecutorService().execute(new Runnable() {

						@Override
						public void run() {

							Config.writeFile("_PAD_PQSZ",
									jsonObject.toString(),
									getApplicationContext());
						}
					});

					Message msg = new Message();
					msg.what = Constant.SUCCESS;// 成功
					handler.sendMessage(msg);
					// getWebService("fy");

				} else {
					Message msg = new Message();
					msg.what = 3;// 失败
					handler.sendMessage(msg);
				}

			} catch (Exception e) {
				// Toast.makeText(this, jsonObject.toString(), 1).show();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}
		}

		if ("fy".equals(s)) {

			try {
				jsonObject = callWebserviceImp.getWebServerInfo("_PAD_FYXMLX",
						"", "uf_json_getdata", this);

				flag = jsonObject.getString("flag");

				if (Integer.parseInt(flag) > 0) {

					Config.getExecutorService().execute(new Runnable() {

						@Override
						public void run() {

							Config.writeFile("_PAD_FYXMLX",
									jsonObject.toString(),
									getApplicationContext());
						}
					});

					getWebService("wd");

				} else {
					Message msg = new Message();
					msg.what = 3;// 失败
					handler.sendMessage(msg);
				}

			} catch (Exception e) {
				// Toast.makeText(this, jsonObject.toString(), 1).show();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}
		}

		if ("wd".equals(s)) {

			try {
				// jsonObject = callWebserviceImp.getWebServerInfo("_PAD_WD",
				// "","uf_json_getdata", this);
				//
				// Config.getExecutorService().execute(new Runnable() {
				//
				// @Override
				// public void run() {
				//
				// Config.writeFile("_PAD_WD",
				// jsonObject.toString(), getApplicationContext());
				// }
				// });

				Message msg = new Message();
				msg.what = Constant.SUCCESS;// 成功
				handler.sendMessage(msg);

			} catch (Exception e) {
				// Toast.makeText(this, jsonObject.toString(), 1).show();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}
		}

	}

	/**
	 * 重写回退键
	 */
	@Override
	public void onBackPressed() {
		dialogShowMessage("确定退出?", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
				System.exit(0);
				// AppManager.getAppManager().AppExit(getApplicationContext());
			}
		}, null);

	}

	public boolean isrepeatlogin = false;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.NETWORK_ERROR:
				dialogShowMessage_P(Constant.NETWORK_ERROR_STR, null);
				break;
			case Constant.SUCCESS:
				DataCache.getinition().setUserId(id);
				skipActivity(MainActivity.class);
				break;
			case Constant.FAIL:
				dialogShowMessage_P(msgStr, null);
				break;
			}
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		}

	};

	private boolean isWorked() {
		ActivityManager myManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager
				.getRunningServices(60);
		for (int i = 0; i < runningService.size(); i++) {
			if (runningService.get(i).service.getClassName().toString()
					.equals("com.nt.service.PatrolService")) {
				return true;
			}
		}
		return false;
	}
}
