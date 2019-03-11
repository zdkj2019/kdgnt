package com.nt.activity.w;

import java.util.Map;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.TextView;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.activity.main.MainActivity;
import com.nt.cache.DataCache;
import com.nt.cache.ServiceReportCache;
import com.nt.common.Constant;
import com.nt.utils.Config;

/**
 * 消息展示
 * 
 * @author
 */
@SuppressLint("HandlerLeak")
public class XxglShowActivity extends FrameActivity {
	
	private TextView tv_title,tv_time,tv_content,tv_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_xxglshow);
		initVariable();
		initView();
		initListeners();
	}

	@Override
	protected void initVariable() {
		
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_time= (TextView) findViewById(R.id.tv_time);
		tv_content= (TextView) findViewById(R.id.tv_content);
		tv_id = (TextView) findViewById(R.id.tv_id);
		
		final int index = ServiceReportCache.getIndex();
		final Map<String, String> itemmap = ServiceReportCache.getData().get(index);
		
		tv_title.setText(itemmap.get("title"));
		tv_time.setText(itemmap.get("xrsj"));
		tv_content.setText("        "+itemmap.get("message"));
		tv_id.setText(itemmap.get("id"));
		
		
		if("1".equals(itemmap.get("yfbs"))){
			Config.getExecutorService().execute(new Runnable() {
				
				@Override
				public void run() {
					try {
						String sql = "update zkgl_jcsj_fsappxx2 set yfbs = '2' where id = '" +itemmap.get("id")+"'";
						JSONObject object = callWebserviceImp.getWebServerInfo(
								"_RZ", sql, DataCache.getinition().getUserId(),
								"uf_json_setdata", getApplicationContext());
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					
				}
			});
		}
		
	}

	@Override
	protected void initView() {
		title.setText("消息展示");
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

				}
			}
		};
		topBack.setOnClickListener(onClickListener);
	}

	@Override
	protected void getWebService(String s) {

	}

	@Override
	public void onBackPressed() {
		finish();
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
				
				break;

			case Constant.FAIL:
				
				break;

			}
			if (!backboolean) {
				progressDialog.dismiss();
			}
		}

	};

}
