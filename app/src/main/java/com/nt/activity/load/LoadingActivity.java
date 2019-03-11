package com.nt.activity.load;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.Toast;

import com.nt.R;
import com.nt.activity.BaseActivity;
import com.nt.activity.login.LoginActivity;
import com.nt.common.Constant;
import com.nt.update.DownLoadManager;
import com.nt.update.UpdateInfo;
import com.nt.update.UpdateInfoParser;
import com.nt.utils.Config;
import com.nt.webservice.CallWebserviceImp;

@SuppressLint("HandlerLeak")
public class LoadingActivity extends BaseActivity {
	private String retcode, version, retmsg;
	private UpdateInfo info = new UpdateInfo();

	private ProgressDialog progressDialog;
	private CallWebserviceImp callWebserviceImp = new CallWebserviceImp();
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		final View view = View.inflate(this, R.layout.activity_loading, null);
		//StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
		setContentView(view);
		AlphaAnimation aa = new AlphaAnimation(0.3f,1.0f);
		aa.setDuration(2000);
		view.startAnimation(aa);
		info.setUrl(getResources().getString(R.string.updateurl_dx));
		
		Config.getExecutorService().execute(new Runnable() {
			@Override
			public void run() {
				handler.post(new Runnable() {
					@Override
					public void run() {
						processThread();
					}
				});
			}
		});
		
	}

	private void processThread() {
		progressDialog = ProgressDialog.show(LoadingActivity.this, "提示",
				"正在处理中，请稍后...");
		Config.getExecutorService().execute(new Runnable() {
			@Override
			public void run() {
				longTimeMethod();
			}
		});

	}

	private void longTimeMethod() {
		
		try {
			callWebserviceImp.setVersion(getVersionName());
			String json = "_BBH";

			JSONObject jsonObject = callWebserviceImp.getWebServerInfo(json,
					"", "uf_json_getdata",this);
			Log.e("LoadingActivity",jsonObject.toString());
			retcode = jsonObject.getString("flag");
			retmsg = "检测到更新，请升级";// jsonObject.getString("retmsg");
			if ("1".equals(retcode)) {
				// 从服务器获取版本号成功
				JSONArray jsonArray = jsonObject.getJSONArray("tableA");

				for (int i = 0; i < jsonArray.length(); i++) {

					JSONObject temp = jsonArray.getJSONObject(i);
					version = temp.getString("bbh");
				}

				Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg);
			} else {
				// 从服务器获取信息失败
				Message msg = new Message();
				msg.what = 0;
				handler.sendMessage(msg);
			}

		} catch (Exception e) {
			e.printStackTrace();
			// 服务器超时
			Message msg = new Message();
			msg.what = -1;
			handler.sendMessage(msg);
		}

	}

	/*
	 * 获取当前程序的版本号
	 */
	private String getVersionName() throws Exception {
		// 获取packagemanager的实例
		PackageManager packageManager = getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),
				0);
		return packInfo.versionName;
	}

	/*
	 * 从服务器获取xml解析并进行比对版本号
	 */
	public class CheckVersionTask implements Runnable {

		public void run() {
			try {
				// 从资源文件获取服务器 地址
				String path = getResources().getString(R.string.serverurl);
				// 包装成url的对象
				URL url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setConnectTimeout(5000);
				InputStream is = conn.getInputStream();
				info = UpdateInfoParser.getUpdataInfo(is);

				UpdateInfo.setServerVersion(info.getVersion());
				UpdateInfo.setLocalVersion(getVersionName());

				if (info.getVersion().equals(getVersionName())) {
					// Log.i(TAG,"版本号相同无需升级");
					Message msg = new Message();
					msg.what = 0;
					handler.sendMessage(msg);
				} else {
					// Log.i(TAG,"版本号不同 ,提示用户升级 ");
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				// 连接服务器超时
				Message msg = new Message();
				msg.what = -1;
				handler.sendMessage(msg);
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			finish();
			break;

		}
		return true;
	}

	/**
	 * 弹出对话框通知用户更新程序
	 * 
	 * 弹出对话框的步骤： 1.创建alertDialog的builder. 2.要给builder设置属性, 对话框的内容,样式,按钮
	 * 3.通过builder 创建一个对话框 4.对话框show()出来
	 */
	protected void showUpdataDialog() {
		AlertDialog.Builder builer = new Builder(this);
		builer.setTitle("版本升级");
		builer.setMessage("检测到最新版本，请及时升级！");
		builer.setCancelable(false);
		// 当点确定按钮时从服务器上下载 新的apk 然后安装
		builer.setPositiveButton("确定", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Log.i(TAG,"下载apk,更新");
				downLoadApk();
			}
		});
		// 当点取消按钮时进行登录
		builer.setNegativeButton("取消", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
//				Message msg = new Message();
//				msg.what = -2;
//				handler.sendMessage(msg);
				onBackPressed();
				finish();
			}
		});
		AlertDialog dialog = builer.create();
		dialog.show();
	}

	ProgressDialog pd;

	/**
	 * 从服务器中下载APK
	 */
	protected void downLoadApk() {
		// 进度条对话框
		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setCancelable(false);
		// 更新时候的启动按钮
		pd.setButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				DownLoadManager.setCancelUpdate(true);
				Message msg = new Message();
				msg.what = -2;
				handler.sendMessage(msg);
			}
		});
		pd.setMessage("正在下载...");
		pd.show();

		Config.getExecutorService().execute(new Runnable() {
			@Override
			public void run() {
				try {
					File file = DownLoadManager.getFileFromServer(
							info.getUrl(), pd);
					Thread.sleep(3000);
					if (!DownLoadManager.isCancelUpdate()) {
						installApk(file);
					}
					pd.dismiss(); // 结束掉进度条对话框
				} catch (Exception e) {
					Message msg = new Message();
					msg.what = -2;
					handler.sendMessage(msg);
					e.printStackTrace();
				}
			}
		});
	}

	// 安装apk
	protected void installApk(File file) {
		this.finish();
		Intent intent = new Intent();
		// 执行动作
		intent.setAction(Intent.ACTION_VIEW);
		// 执行的数据类型
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		startActivity(intent);

	}

	/*
	 * 进入程序的主界面
	 */
	private void LoginMain() {
		Intent intent = new Intent(this, LoginActivity.class);
		
		startActivity(intent);
		overridePendingTransition(R.anim.fade, R.anim.hold);// 淡入淡出效果
		// 结束掉当前的activity
		this.finish();
	}

	private Handler handler = new Handler(Looper.myLooper()) {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {

			// 从服务器获取版本失败
			case 0:
				Toast.makeText(LoadingActivity.this, retmsg, Toast.LENGTH_SHORT)
						.show();
				LoginMain();
				break;

			// 对话框通知用户升级程序
			case 1:
				try {
					UpdateInfo.setServerVersion(version);
					UpdateInfo.setLocalVersion(getVersionName());

					if (!version.equals("")
							&& Double.parseDouble(version) > Double
									.parseDouble(getVersionName())) {
						showUpdataDialog();
					} else {
						LoginMain();
					}
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(
							LoadingActivity.this,
							"版本号异常" + UpdateInfo.getLocalVersion()
									+ "=LocalVersion ：" + version,
							Toast.LENGTH_LONG).show();
					progressDialog.dismiss();
					LoginMain();
				}
				progressDialog.dismiss();
				break;

			// 服务器超时
			case -1:
				Toast.makeText(LoadingActivity.this, "获取服务器更新信息超时",
						Toast.LENGTH_SHORT).show();
				LoginMain();
				finish();
				break;

			// 下载apk失败
			case -2:
				Toast.makeText(LoadingActivity.this, "安装包下载失败",
						Toast.LENGTH_SHORT).show();
				progressDialog.dismiss();
				//LoginMain();
				onBackPressed();
				finish();
				break;
			}
			// progressDialog.dismiss();
		}
	};
	
}
