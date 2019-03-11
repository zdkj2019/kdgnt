package com.nt.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;

import com.nt.R;
import com.nt.activity.kdg.ListKdg;
import com.nt.activity.login.LoginActivity;
import com.nt.activity.w.PqzgjslShow;
import com.nt.activity.w.XxglActivity;
import com.nt.cache.DataCache;
import com.nt.common.Constant;
import com.nt.utils.DataUtil;
import com.nt.webservice.CallWebserviceImp;

@SuppressLint("HandlerLeak")
public class NumberService extends Service {

	private CallWebserviceImp callWebserviceImp;
	private String flag;
	private String txzd = "";
	private SharedPreferences spf;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// 去查询数据，进行更新
		callWebserviceImp = new CallWebserviceImp();
		spf = getSharedPreferences("loginsp", LoginActivity.MODE_PRIVATE);
		getWebService("query");
		return super.onStartCommand(intent, flags, startId);
	}

	Calendar c = Calendar.getInstance();

	private void getWebService(String s) {
		try {
			new Thread() {

				@Override
				public void run() {

					int currentTime = c.get(Calendar.HOUR_OF_DAY);

					if (currentTime >= 8 && currentTime <= 22) {
						query();
					}

				}

			}.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			new Thread() {

				@Override
				public void run() {

					int currentTime = c.get(Calendar.HOUR_OF_DAY);

					if (currentTime >= 8 && currentTime <= 22) {
						queryJsl();
					}

				}

			}.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 片区主管监控
	private void query() {
		try {
			String userid = spf.getString("userId", "");
			JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
					"_PAD_KDG_PQGD_WWG_TS", userid + "*" + userid + "*"
							+ userid + "*" + userid, "uf_json_getdata", this);
			flag = jsonObject.getString("flag");
			JSONArray jsonArray = jsonObject.getJSONArray("tableA");
			if (Integer.parseInt(flag) > 0) {
				JSONObject temp = jsonArray.getJSONObject(0);
				txzd = temp.getString("pqts");
				Message msg = new Message();
				msg.what = Constant.SUCCESS;// 成功
				handler.sendMessage(msg);
			} else {
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;
				handler.sendMessage(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Message msg = new Message();
			msg.what = Constant.NETWORK_ERROR;
			handler.sendMessage(msg);
		}

	}

	// 片区主管及时率
	@SuppressLint("SimpleDateFormat")
	private void queryJsl() {
		try {
			String userid = spf.getString("userId", "");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			c.set(Calendar.MONTH, c.get(Calendar.MONTH) - 1);
			String start = sdf.format(new Date(c.getTimeInMillis()));
			String end = DataUtil.toDataString("yyyy-MM-dd");
			JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
					"_PAD_PQJSL1", userid + "*" + start + "*" + end,
					"uf_json_getdata", this);
			flag = jsonObject.getString("flag");
			JSONArray jsonArray = jsonObject.getJSONArray("tableA");
			Map<String, String> map = new HashMap<String, String>();
			if (Integer.parseInt(flag) > 0) {
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject temp = jsonArray.getJSONObject(i);
					map.put("pqmc", temp.getString("pqmc"));
					map.put("zs", temp.getString("zs"));
					map.put("csgd", temp.getString("csgd"));
					map.put("jsl", temp.getString("jsl"));
				}
				double jsl = Double.parseDouble(map.get("jsl"));
				if (jsl < 0.98) {
					Message msg = new Message();
					msg.what = 3;// 成功
					handler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = Constant.NETWORK_ERROR;// 失败
					handler.sendMessage(msg);
				}
			} else {
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;// 失败
				handler.sendMessage(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Message msg = new Message();
			msg.what = Constant.NETWORK_ERROR;
			handler.sendMessage(msg);
		}

	}
	
	private void addNotificaction() {
		NotificationManager manager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// 创建一个Notification
		Notification notification = new Notification();
		// 设置显示在手机最上边的状态栏的图标
		notification.icon = R.drawable.logo;
		// 当当前的notification被放到状态栏上的时候，提示内容
		notification.tickerText = txzd;
		notification.defaults = Notification.DEFAULT_ALL;
		long[] vibrate = { 0, 100, 200, 300 };
		notification.vibrate = vibrate;
		//notification.flags |= Notification.FLAG_AUTO_CANCEL;
		// audioStreamType的值必须AudioManager中的值，代表着响铃的模式
		AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		mAudioManager.setStreamVolume(android.media.AudioManager.STREAM_ALARM,
				7, 0); // tempVolume:音量绝对值
		notification.audioStreamType = AudioManager.ADJUST_RAISE;
		DataCache.getinition().setQueryType(2501);
		DataCache.getinition().setTitle("片区主管监控");

		Intent intent = new Intent(this, ListKdg.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		RemoteViews remoteViews = new RemoteViews(getPackageName(),
				R.layout.notification_remont);
		remoteViews.setImageViewResource(R.id.imageView1, R.drawable.logo);
		remoteViews.setTextViewText(R.id.txtnotification, txzd);
		notification.contentView = remoteViews;
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				intent, PendingIntent.FLAG_ONE_SHOT);
		notification.contentIntent = pendingIntent;
		manager.notify(2, notification);

	}

	private void addNotificactionJsl() {
		NotificationManager manager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// 创建一个Notification
		Notification notification = new Notification();
		// 设置显示在手机最上边的状态栏的图标
		notification.icon = R.drawable.logo;
		// 当当前的notification被放到状态栏上的时候，提示内容
		String msg = "存在及时率不达标的情况，请即时查看";
		notification.tickerText = msg;
		notification.defaults = Notification.DEFAULT_ALL;
		long[] vibrate = { 0, 100, 200, 300 };
		notification.vibrate = vibrate;
		//notification.flags |= Notification.FLAG_AUTO_CANCEL;
		// audioStreamType的值必须AudioManager中的值，代表着响铃的模式
		AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		mAudioManager.setStreamVolume(android.media.AudioManager.STREAM_ALARM,
				7, 0); // tempVolume:音量绝对值
		notification.audioStreamType = AudioManager.ADJUST_RAISE;
		DataCache.getinition().setQueryType(2501);
		DataCache.getinition().setTitle("片区主管及时率");

		Intent intent = new Intent(this, PqzgjslShow.class);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MONTH, c.get(Calendar.MONTH) - 1);
		String start = sdf.format(new Date(c.getTimeInMillis()));
		String end = DataUtil.toDataString("yyyy-MM-dd");
		intent.putExtra("status", start + "*" + end);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		RemoteViews remoteViews = new RemoteViews(getPackageName(),
				R.layout.notification_remont);
		remoteViews.setImageViewResource(R.id.imageView1, R.drawable.logo);
		remoteViews.setTextViewText(R.id.txtnotification, msg);
		notification.contentView = remoteViews;
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				intent, PendingIntent.FLAG_ONE_SHOT);
		notification.contentIntent = pendingIntent;
		manager.notify(3, notification);

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.NETWORK_ERROR:
				break;

			case Constant.SUCCESS:
				addNotificaction();
				break;
			case 3:
				addNotificactionJsl();
				break;
			}
			txzd = "";
			stopSelf();
		}

	};

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
