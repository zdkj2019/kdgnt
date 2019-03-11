package com.nt.activity.kc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.zip.Inflater;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nt.R;
import com.nt.Parser.JSONObjectParser;
import com.nt.activity.FrameActivity;
import com.nt.activity.main.MainActivity;
import com.nt.cache.DataCache;
import com.nt.common.Constant;
import com.nt.utils.Config;
import com.nt.utils.DataUtil;

/**
 * 外部调拨出库
 * 
 * @author cheng
 */
@SuppressLint({ "HandlerLeak", "WorldReadableFiles" })
public class WbdbckActivity extends FrameActivity {

	private TextView tv_tbry,tv_cardtitle;
	private EditText edit_sgdh,edit_gdbz;
	private Button btn_addmx,confirm, cancel;
	private LinearLayout ll_mx,ll_mx_item;
	private List<Map<String, String>> data_mx;
	private String flag,type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_wbdbck);
		initVariable();
		initView();
		initListeners();

	}

	@Override
	protected void initVariable() {
		
		tv_tbry = (TextView) findViewById(R.id.tv_tbry);
		tv_cardtitle = (TextView) findViewById(R.id.tv_cardtitle);
		edit_sgdh = (EditText) findViewById(R.id.edit_sgdh);
		edit_gdbz = (EditText) findViewById(R.id.edit_gdbz);
		
		btn_addmx= (Button) findViewById(R.id.btn_addmx);
		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);
		ll_mx = (LinearLayout) findViewById(R.id.ll_mx);
		tv_tbry.setText(DataCache.getinition().getUserId()+"("+DataCache.getinition().getUsername()+")");
	
	}

	@Override
	protected void initView() {
		String titleStr = DataCache.getinition().getTitle();
		title.setText(titleStr);
		if("库存-外部调拨出库".equals(titleStr)){
			type="dbck";
		}else{
			type = "pjfk";
			tv_cardtitle.setText("返库明细");
		}
		data_mx = new ArrayList<Map<String,String>>();
	}

	@Override
	protected void initListeners() {

		topBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!isNotNull(edit_sgdh)){
					toastShowMessage("物流号不能为空！");
					return;
				}
				if(data_mx.size()==0){
					toastShowMessage("出库明细不能为空！");
					return;
				}
				showProgressDialog();
				Config.getExecutorService().execute(
					new Runnable() {
						@Override
						public void run() {
							getWebService("submit");
						}
					}
				);
			}
		});
		
		btn_addmx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if("pjfk".equals(type)){
					Intent intent = new Intent(WbdbckActivity.this,PjfkMxActivity.class);
					startActivityForResult(intent, 1);
				}else{
					Intent intent = new Intent(WbdbckActivity.this,WbdbckMxActivity.class);
					startActivityForResult(intent, 1);
				}
				
			}
		});
	}
	
	OnLongClickListener onLongClickListener = new OnLongClickListener() {
		
		@Override
		public boolean onLongClick(final View v) {
			dialogShowMessage("确定要删除该明细？",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface face,int paramAnonymous2Int) {
					TextView tv_id = (TextView) v.findViewById(R.id.tv_id);
					data_mx.remove(Integer.parseInt(tv_id.getText().toString()));
					Message msg = new Message();
					msg.what = Constant.NUM_6;
					handler.sendMessage(msg);
				}
			},null);
			return false;
		}
	};
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == 1 && data != null) {
			boolean result = data.getBooleanExtra("result", false);
			if(result){
				data_mx.add(DataCache.getinition().getMap());
				DataCache.getinition().setMap(null);
				Message msg = new Message();
				msg.what = Constant.NUM_6;
				handler.sendMessage(msg);
			}else{
				toastShowMessage("新增明细失败,数据转换错误！");
			}	
		}
	}

	@Override
	protected void getWebService(String s) {

		if ("submit".equals(s)) {
			try {
				String sql = ""
						+edit_sgdh.getText().toString()
						+ "*PAM*"
						+DataCache.getinition().getUserId()
						+ "*PAM*"
						+edit_gdbz.getText().toString();
				String str_mx = "*PAM*";
				for(int i=0;i<data_mx.size();i++){
					Map<String, String> map = data_mx.get(i);
					str_mx = str_mx
							+map.get("ckkfmc_id").replace("id_", "")+"#@#"
							+map.get("ckcwmc_id").replace("id_", "")+"#@#"
							+map.get("rkkfmc_id").replace("id_", "")+"#@#"
							+map.get("rkcwmc_id").replace("id_", "")+"#@#"
							+map.get("hpmc_id").replace("id_", "")+"#@#"
							+map.get("sl")+"#@#"
							+map.get("dqkc")+"#@#"
							+map.get("bz");
					str_mx = str_mx + "#^#";
				}
				str_mx = str_mx.substring(0, str_mx.length()-3);
				sql = sql+str_mx;
				flag = this.callWebserviceImp.getWebServerInfo(
						"c#_ccgl_ywgl_wbck_gsz_c",
						sql,
						DataCache.getinition().getUserId(),
						"and" + "*" + DataCache.getinition().getUserId() + "*"
								+ "13854364705" + "*" + "yzm",
						"uf_json_setdata2", this).getString("flag");
				if(Integer.parseInt(flag)>0){
					Message msg = new Message();
					msg.what = Constant.SUCCESS;
					handler.sendMessage(msg);
				}else{
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

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.NETWORK_ERROR:
				dialogShowMessage_P(Constant.NETWORK_ERROR_STR,null);
				break;
			case Constant.SUCCESS:
				dialogShowMessage_P("提交成功",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								onBackPressed();
							}
						});
				break;
			case Constant.FAIL:
				dialogShowMessage_P("提交失败，返回"+flag,null);
				break;
			case Constant.NUM_6:
				ll_mx.removeAllViews();
				for(int i=0;i<data_mx.size();i++){
					ll_mx_item = (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.listview_kfmx_item, null);
					Map<String, String> map = data_mx.get(i);
					((TextView) ll_mx_item.findViewById(R.id.tv_id)).setText(i+"");
					((TextView) ll_mx_item.findViewById(R.id.tv_ckkfmc)).setText(map.get("ckkfmc_name"));
					((TextView) ll_mx_item.findViewById(R.id.tv_ckcwmc)).setText(map.get("ckcwmc_name"));
					((TextView) ll_mx_item.findViewById(R.id.tv_rkkfmc)).setText(map.get("rkkfmc_name"));
					((TextView) ll_mx_item.findViewById(R.id.tv_rkcwmc)).setText(map.get("rkcwmc_name"));
					((TextView) ll_mx_item.findViewById(R.id.tv_hpmc)).setText(map.get("hpmc_name"));
					((TextView) ll_mx_item.findViewById(R.id.tv_hpbm)).setText(map.get("hpmc_id"));
					((TextView) ll_mx_item.findViewById(R.id.tv_ggxh)).setText(map.get("dw"));
					((TextView) ll_mx_item.findViewById(R.id.tv_sl)).setText(map.get("sl"));
					((TextView) ll_mx_item.findViewById(R.id.tv_dqkc)).setText(map.get("dqkc"));
					((TextView) ll_mx_item.findViewById(R.id.tv_bz)).setText(map.get("bz"));
					ll_mx_item.setOnLongClickListener(onLongClickListener);
					ll_mx.addView(ll_mx_item);
				}
				break;
			}
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		}
	};

}
