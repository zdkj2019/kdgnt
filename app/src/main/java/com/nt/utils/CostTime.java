package com.nt.utils;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

public class CostTime {

	public static void showTime(JSONObject jSONObject,final Context mContext,Handler handle){
		try {
			final Object object = jSONObject.get("costTime");
			handle.post(new Runnable() {
				
				@Override
				public void run() {
					Toast.makeText(mContext, object+"", Toast.LENGTH_SHORT).show();
				}
			});
			
		} catch (JSONException e) {
			Toast.makeText(mContext, jSONObject+"", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}
}
