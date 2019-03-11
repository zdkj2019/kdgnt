package com.nt.activity.js;

import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.CharacterStyle;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.activity.main.MainActivity;
import com.nt.cache.DataCache;
import com.nt.cache.ServiceReportCache;

/**
 * 报告填写，确认
 * 
 * @author wlj
 * 
 */
@SuppressLint("HandlerLeak")
public class LiuShuiZhangActivity extends FrameActivity {
	
	private Map<String, String> itemmap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_liushuizhang );
		initVariable();
		initView();
		initListeners();

	}

	@Override
	protected void initVariable() {

		
	}
	
	public String formatValue(String v){
		String s = "";
		try {
			if("0".equals(v)){
				s = "0.00";
			}else if(v.length()<10){
				s = Double.parseDouble(v)+"0";
			}else{
				v = v.substring(0, 10);
				s = Double.parseDouble(v)+"0";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
	
	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());
		int index = ServiceReportCache.getIndex();
		ServiceReportCache.setItemmap(ServiceReportCache.getData().get(index));
		itemmap = ServiceReportCache.getItemmap();
		
		((TextView) findViewById(R.id.text_1)).setText(itemmap.get("流水号"));
		((TextView) findViewById(R.id.text_2)).setText(itemmap.get("派工对象"));
		((TextView) findViewById(R.id.text_3)).setText(itemmap.get("所属片区"));
		((TextView) findViewById(R.id.text_4)).setText(itemmap.get("报障时间"));
		((TextView) findViewById(R.id.text_5)).setText(itemmap.get("派工时间"));
		((TextView) findViewById(R.id.text_6)).setText(itemmap.get("受理时间"));
		((TextView) findViewById(R.id.text_7)).setText(itemmap.get("到达时间"));
		((TextView) findViewById(R.id.text_8)).setText(itemmap.get("完成时间"));
		((TextView) findViewById(R.id.text_9)).setText(itemmap.get("服务评价"));
		((TextView) findViewById(R.id.text_10)).setText(itemmap.get("服务工程师"));
		((TextView) findViewById(R.id.text_11)).setText(itemmap.get("备注"));
		((TextView) findViewById(R.id.text_12)).setText(formatValue(itemmap.get("标准服务费")));
		((TextView) findViewById(R.id.text_13)).setText(formatValue(itemmap.get("其他费用")));
		((TextView) findViewById(R.id.text_14)).setText(formatValue(itemmap.get("上门费用")));
		((TextView) findViewById(R.id.text_15)).setText(formatValue(itemmap.get("快递费用")));
		((TextView) findViewById(R.id.text_16)).setText(formatValue(itemmap.get("车辆费用")));
		((TextView) findViewById(R.id.text_17)).setText(formatValue(itemmap.get("付款金额")));
		((TextView) findViewById(R.id.text_18)).setText(formatValue(itemmap.get("折扣")));
		((TextView) findViewById(R.id.text_19)).setText(formatValue(itemmap.get("实际付款金额")));
		((TextView) findViewById(R.id.text_20)).setText(formatValue(itemmap.get("应付金额")));
		((TextView) findViewById(R.id.text_21)).setText(formatValue(itemmap.get("当前累计")));
	}


	@Override
	protected void initListeners() {

		// Button点击
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		};
		
		topBack.setOnClickListener(onClickListener);
		findViewById(R.id.cancel).setOnClickListener(onClickListener);
		findViewById(R.id.confirm).setOnClickListener(onClickListener);

	}

	@Override
	protected void getWebService(String s) {}



	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		intent.putExtra("currType", 3);
		startActivity(intent);
		finish();
	}

}
