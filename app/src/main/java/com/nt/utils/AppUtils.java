package com.nt.utils;

import java.lang.reflect.Field;

import com.nt.R;
import com.nt.activity.load.LoadingActivity;
import com.nt.activity.main.MainActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class AppUtils {
	private final static String lancherActivityClassName = LoadingActivity.class.getName();
	/**
     * 设置桌面Icon角标，当前支持小米，索尼，三星。
     * @param context
     * @param number
     */
    public static void sendBadgeNumber(Context context,String number) {
    	try {
    		if (TextUtils.isEmpty(number)) {
                number = "0";
            } else {
                int numInt = Integer.valueOf(number);
                number = String.valueOf(Math.max(0, Math.min(numInt, 99)));
            }

            if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {
                sendToXiaoMi(context,number);
            } else if (Build.MANUFACTURER.equalsIgnoreCase("sony")) {
                sendToSony(context,number);
            } else if (Build.MANUFACTURER.toLowerCase().contains("samsung")) {
                sendToSamsumg(context,number);
            } else if (Build.MANUFACTURER.toLowerCase().contains("huawei")) {
                sendToSamsumg(context,number);
            } else {
            	Toast.makeText(context, "sendBadgeNumber: Not Support", 1);
            }
		} catch (Exception e) {
			// TODO: handle exception
		}
    }
    
    private static void sendToXiaoMi(Context context,String number) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = null;
        boolean isMiUIV6 = true;
        try {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setContentTitle("您有"+number+"未读消息");
            builder.setTicker("您有"+number+"未读消息");
            builder.setAutoCancel(true);
            builder.setSmallIcon(R.drawable.logo);
            builder.setDefaults(Notification.DEFAULT_LIGHTS);
            notification = builder.build();
            Class miuiNotificationClass = Class.forName("android.app.MiuiNotification");
            Object miuiNotification = miuiNotificationClass.newInstance();
            Field field = miuiNotification.getClass().getDeclaredField("messageCount");
            field.setAccessible(true);
            field.set(miuiNotification, Integer.valueOf(number));// 设置信息数
            field = notification.getClass().getField("extraNotification");
            field.setAccessible(true);
            field.set(notification, miuiNotification);
        }catch (Exception e) {
            e.printStackTrace();
            //miui 6之前的版本
            isMiUIV6 = false;
            Intent localIntent = new Intent("android.intent.action.APPLICATION_MESSAGE_UPDATE");
            localIntent.putExtra("android.intent.extra.update_application_component_name",context.getPackageName() + "/"+ lancherActivityClassName );
            localIntent.putExtra("android.intent.extra.update_application_message_text",number);
            context.sendBroadcast(localIntent);
        }
        finally
        {
            if(notification!=null && isMiUIV6 )
            {
                //miui6以上版本需要使用通知发送
                nm.notify(101010, notification);
            }
        }

    }

    private static  void sendToSony(Context context,String number) {
        boolean isShow = true;
        if ("0".equals(number)) {
            isShow = false;
        }
        Intent localIntent = new Intent();
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE",isShow);//是否显示
        localIntent.setAction("com.sonyericsson.home.action.UPDATE_BADGE");
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME",lancherActivityClassName );//启动页
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", number);//数字
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME",context.getPackageName());//包名
        context.sendBroadcast(localIntent);
    }

    private static void sendToSamsumg(Context context,String number)
    {
        Intent localIntent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        localIntent.putExtra("badge_count", Integer.valueOf(number));//数字
        localIntent.putExtra("badge_count_package_name", context.getPackageName());//包名
        localIntent.putExtra("badge_count_class_name",lancherActivityClassName ); //启动页
        context.sendBroadcast(localIntent);
        Log.d("AppUtils","Samsumg isSendOk"+number);
    }
}
