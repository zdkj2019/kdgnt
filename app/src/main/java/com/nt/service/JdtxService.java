package com.nt.service;

import java.util.Calendar;

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
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.widget.RemoteViews;

import com.nt.R;
import com.nt.activity.login.LoginActivity;
import com.nt.activity.main.MainActivity;
import com.nt.common.Constant;
import com.nt.webservice.CallWebserviceImp;

/**
 * 接单提醒 20170426
 * 
 * @author zdkj
 *
 */
@SuppressLint("HandlerLeak")
public class JdtxService extends Service {

	private CallWebserviceImp callWebserviceImp;
	private String flag;
	private SharedPreferences spf;
	private Calendar c = Calendar.getInstance();
	private WakeLock wakeLock = null;
	private JSONArray array;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass()
				.getCanonicalName());
		wakeLock.acquire();

		callWebserviceImp = new CallWebserviceImp();
		spf = getSharedPreferences("loginsp", LoginActivity.MODE_PRIVATE);
		getWebService("query");
		return super.onStartCommand(intent, flags, startId);
	}

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
	}

	// 催单提醒
	private void query() {
		try {
			String userid = spf.getString("userId", "");
			JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
					"_PAD_SHGL_JDTX", userid, "uf_json_getdata", this);
			flag = jsonObject.getString("flag");
			array = jsonObject.getJSONArray("tableA");
			
//			array = new JSONArray();
//			JSONObject json = new JSONObject();
//			json.put("zbh", "000001");
//			json.put("nr", "adasd");
//			array.put(json);
			
			
			Message msg = new Message();
			msg.what = Constant.SUCCESS;
			handler.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
			Message msg = new Message();
			msg.what = Constant.NETWORK_ERROR;
			handler.sendMessage(msg);
		}
	}

	private void addNotificaction() {
		try {
			for (int i = 0; i < array.length(); i++) {
				JSONObject json = array.getJSONObject(i);
				NotificationManager manager = (NotificationManager) this
						.getSystemService(Context.NOTIFICATION_SERVICE);
				// 创建一个Notification
				Notification notification = new Notification();
				// 设置显示在手机最上边的状态栏的图标
				notification.icon = R.drawable.logo;
				// 当当前的notification被放到状态栏上的时候，提示内容
				notification.tickerText = json.getString("nr");
				
//				notification.defaults = Notification.DEFAULT_ALL;
//				long[] vibrate = { 0, 100, 200, 300 };
//				notification.vibrate = vibrate;//震动
//				//notification.flags |= Notification.FLAG_AUTO_CANCEL;//自动消失
//				// audioStreamType的值必须AudioManager中的值，代表着响铃的模式
//				AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//				mAudioManager.setStreamVolume(
//						android.media.AudioManager.STREAM_ALARM, 7, 0); // tempVolume:音量绝对值
//				notification.audioStreamType = AudioManager.ADJUST_RAISE;
				
				// 添加声音提示  
		        notification.defaults |= Notification.DEFAULT_SOUND;  
				//notification.flags |= Notification.FLAG_AUTO_CANCEL;
		        // audioStreamType的值必须AudioManager中的值，代表着响铃的模式  
		        notification.audioStreamType = android.media.AudioManager.ADJUST_LOWER;  
		  
		        // 下边的两个方式可以添加音乐  
		        notification.defaults = Notification.DEFAULT_LIGHTS;    
		        notification.sound = Uri.parse("android.resource://" + getPackageName()    
		                + "/" + R.raw.strum);

				Intent intent = new Intent(this, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				RemoteViews remoteViews = new RemoteViews(getPackageName(),
						R.layout.notification_remont);
				remoteViews.setImageViewResource(R.id.imageView1,
						R.drawable.logo);
				remoteViews.setTextViewText(R.id.txtnotification,
						json.getString("nr"));
				notification.contentView = remoteViews;
				PendingIntent pendingIntent = PendingIntent.getActivity(this,
						0, intent, PendingIntent.FLAG_ONE_SHOT);
				notification.contentIntent = pendingIntent;
				manager.notify(20 + i, notification);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

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
			}
			// stopSelf();
		}

	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		if (wakeLock != null) {
			wakeLock.release();
			wakeLock = null;
		}
		super.onDestroy();
	}

}
