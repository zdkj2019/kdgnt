package com.nt.service;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * 开机启动广播拦截启动一个service
 * 
 * @author Cheng
 *
 */
public class MyBroadcastReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		boolean isServiceRunning = false;
		// 检查Service状态
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if ("com.nt.service.CsyyService".equals(service.service
					.getClassName())) {
				isServiceRunning = true;
			}
		}
		if (!isServiceRunning) {
			Intent i = new Intent(context, CsyyService.class);
			context.startService(i);
		}
	}
}
