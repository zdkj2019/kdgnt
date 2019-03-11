package com.nt.activity.kdg;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.activity.esp.AddParts;
import com.nt.activity.util.BaiduMapActivity;
import com.nt.cache.DataCache;
import com.nt.cache.ServiceReportCache;
import com.nt.common.Constant;
import com.nt.utils.Config;
import com.nt.utils.ImageUtil;

/**
 * 快递柜-配件修改
 * 
 * @author zdkj 20170407
 *
 */
@SuppressLint("ResourceAsColor")
public class PjxgKdgWx extends FrameActivity {

	private Button confirm, cancel;
	private String zbh,msgStr,pjnsbz,ds,ywhy_bm,fy2,currmxh;
	private int currposition;
	private TextView tv_curr,tv_xxdz,tv_pjtp,tv_yscpjtp;
	private ArrayList<Map<String, String>> dataYh,dataXz,data,data_zp;
	private String[] from;
	private int[] to;
	private ListView listView,listView1;
	private SimpleAdapter adapter;
	private Map<String, ArrayList<String>> filemap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_kdg_pjxgwx);
		initVariable();
		initView();
		initListeners();
		showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				getWebService("query");
			}
		});
	}

	@Override
	protected void initVariable() {

		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);
		confirm.setText("确定");
		cancel.setText("取消");

	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());
		tv_xxdz = (TextView) findViewById(R.id.tv_xxdz);
		tv_pjtp = (TextView) findViewById(R.id.tv_pjtp);
		tv_yscpjtp = (TextView) findViewById(R.id.tv_yscpjtp);
		listView = (ListView) findViewById(R.id.listView);
		listView1 = (ListView) findViewById(R.id.listView1);
		from = new String[] { "hpmc", "sl","dj","sfhs" };
		to = new int[] { R.id.tv_1, R.id.tv_2, R.id.tv_3, R.id.tv_4 };
		filemap = new HashMap<String, ArrayList<String>>();
		
		final Map<String, Object> itemmap = ServiceReportCache.getObjectdata()
				.get(ServiceReportCache.getIndex());

		zbh = itemmap.get("zbh").toString();
		pjnsbz = itemmap.get("pjnsbz").toString();
		ds = itemmap.get("ds").toString();
		ywhy_bm = itemmap.get("ywhy_bm").toString();
		fy2 = itemmap.get("fy2").toString();
		((TextView) findViewById(R.id.tv_zbh)).setText(zbh);
		((TextView) findViewById(R.id.tv_sf)).setText(itemmap.get("sf")
				.toString());
		((TextView) findViewById(R.id.tv_ds)).setText(itemmap.get("sx")
				.toString());
		((TextView) findViewById(R.id.tv_qx)).setText(itemmap.get("qy")
				.toString());
		((TextView) findViewById(R.id.tv_hb)).setText(itemmap.get("ywhy")
				.toString());
		((TextView) findViewById(R.id.tv_wdmc)).setText(itemmap.get("xqmc")
				.toString());
		tv_xxdz.setText(itemmap.get("xxdz")
				.toString());
		((TextView) findViewById(R.id.tv_gzxx)).setText(itemmap.get("gzxx")
				.toString());
		((TextView) findViewById(R.id.tv_gzdl)).setText(itemmap.get("kzzf3_mc")
				.toString());
		((TextView) findViewById(R.id.tv_gzzl)).setText(itemmap.get("kzzf4_mc")
				.toString());
		((TextView) findViewById(R.id.tv_gzxl)).setText(itemmap.get("kzzf5_mc")
				.toString());
		((TextView) findViewById(R.id.tv_sbsx)).setText(itemmap.get("sflx")
				.toString());
		((TextView) findViewById(R.id.tv_rgsfbz)).setText(itemmap.get("fy1")
				.toString());
		((TextView) findViewById(R.id.tv_pjsfbz)).setText(itemmap.get("fy2")
				.toString());
		((TextView) findViewById(R.id.tv_wgqk)).setText(itemmap.get("wcqkmc")
				.toString());
		((TextView) findViewById(R.id.tv_ycbz)).setText(itemmap.get("kzzf2")
				.toString());
		((TextView) findViewById(R.id.tv_clgc)).setText(itemmap.get("clgc")
				.toString());
		TextView tv_pjthyy = (TextView) findViewById(R.id.tv_pjthyy);
		tv_pjthyy.setText(itemmap.get("pjthyy").toString());
//		if("2".equals(pjnsbz)){
//			tv_pjthyy.setTextColor(getResources().getColor(R.color.red));
//		}
	}

	@Override
	protected void initListeners() {
		//
		OnClickListener backonClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.bt_topback:
					onBackPressed();
					break;
				case R.id.cancel:
					onBackPressed();
					break;
				case R.id.confirm:
					showProgressDialog();
					Config.getExecutorService().execute(new Runnable() {

						@Override
						public void run() {
							getWebService("submit");
						}
					});

					break;
				default:
					break;
				}

			}
		};
		
		

		topBack.setOnClickListener(backonClickListener);
		cancel.setOnClickListener(backonClickListener);
		confirm.setOnClickListener(backonClickListener);
		
		tv_pjtp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tv_curr = (TextView) v;
				ArrayList<String> list = filemap.get(zbh);
				camera(1, list);
			}
		});
		
		tv_yscpjtp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ArrayList<String> arraylist = new ArrayList<String>();
				if(data_zp.size()>0){
					String tzz = data_zp.get(0).get("path");
					String path = data_zp.get(0).get("zpmc");
					String[] paths = path.split(",");
					for(int i=0;i<paths.length;i++){
						arraylist.add(Constant.ImgPath+tzz+"/"+paths[i]);
					}
				}
				Intent intent = new Intent(PjxgKdgWx.this,ShowImgActivity.class);
				intent.putStringArrayListExtra("imageUrls", arraylist);
				startActivity(intent);
			}
		});
		
		findViewById(R.id.iv_baidumap).setOnClickListener(
		new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						BaiduMapActivity.class);
				intent.putExtra("keyStr", tv_xxdz.getText().toString()
						.trim());
				startActivity(intent);
			}
		});
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
			ArrayList<String> list = data.getStringArrayListExtra("imglist");
			if (list != null) {
				loadImg(list);
			}
		}
		if (requestCode == 3 && resultCode == 1 && data != null) {
			ArrayList<Map<String, String>> list = (ArrayList<Map<String, String>>) data
					.getSerializableExtra("list");
			dataXz.add(dataXz.size() - 1, list.get(0));
			Message msg = new Message();
			msg.what = Constant.NUM_6;
			handler.sendMessage(msg);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void loadImg(final ArrayList<String> list) {
		try {
			String mxh = zbh;
			if (list.size() > 0) {
				tv_curr.setText("继续选择");
				tv_curr.setBackgroundResource(R.drawable.btn_normal_yellow);
			} else {
				tv_curr.setText("选择图片");
				tv_curr.setBackgroundResource(R.drawable.btn_normal);
			}
			
			filemap.put(mxh, list);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@Override
	protected void getWebService(String s) {
		if (s.equals("query")) {
			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_KDG_FWBG_YHBJCX", zbh, "uf_json_getdata", this);
				String flag = jsonObject.getString("flag");
				dataXz = new ArrayList<Map<String, String>>();
				dataYh = new ArrayList<Map<String, String>>();
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, String> item = new HashMap<String, String>();
						item.put("mxh", temp.getString("mxh"));
						item.put("hpmc", temp.getString("hpmc"));
						item.put("dj", temp.getString("dj"));
						String sfhs = temp.getString("sfhs");
						if("1".equals(sfhs)){
							item.put("sfhs", "是");
						}else{
							item.put("sfhs", "否");
						}
						item.put("sl", temp.getString("sl"));
						dataYh.add(item);
					}
				}
				Map<String, String> map = new HashMap<String, String>();
				map.put("hpbm", "");
				map.put("hpmc", "");
				map.put("sl", "");
				map.put("dj", "");
				map.put("sfhs", "");
				dataXz.add(map);
				
				data = new ArrayList<Map<String, String>>();
				jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SBPJXL", "", "uf_json_getdata",
						getApplicationContext());
				flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						HashMap<String, String> item = new HashMap<String, String>();
						item.put("id", temp.getString("hpbm"));
						item.put("name", temp.getString("hpmc"));
						item.put("sfhs", temp.getString("sfhs"));
						item.put("sfhs_bm", temp.getString("sfhs_bm"));
						data.add(item);
					}
				}
				
				data_zp = new ArrayList<Map<String, String>>();
				jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_KDG_BJZP_YC", zbh, "uf_json_getdata",
						getApplicationContext());
				flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						HashMap<String, String> item = new HashMap<String, String>();
						item.put("path", temp.getString("path"));
						item.put("zpmc", temp.getString("zpmc"));
						data_zp.add(item);
					}
				}
				
				Message msg = new Message();
				msg.what = Constant.NUM_6;
				handler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;
				handler.sendMessage(msg);
			}
		}
		
		if (s.equals("delpj")) {// 提交
			try {
				String cs = "";
				
				// 再提交服务报告
				String str = "";
				str = zbh + "*PAM*" + DataCache.getinition().getUserId();
				str += "*PAM*";
				str += fy2;
				str += "*PAM*";
				str += currmxh;
				
				JSONObject json = this.callWebserviceImp.getWebServerInfo(
						"c#_PAD_KDG_FWBG_BJDEL", str,
						DataCache.getinition().getUserId(), "",
						"uf_json_setdata2", this);
				String flag = json.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					 Message msg = new Message();
					 msg.what = Constant.NUM_7;
					 handler.sendMessage(msg);
				} else {
					msgStr = json.getString("msg");
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
		
		if (s.equals("submit")) {// 提交
			try {
				String cs = "";
				
				// 再提交服务报告
				String str = "";
				str = zbh + "*PAM*" + DataCache.getinition().getUserId();
				str += "*PAM*";
				str += fy2;
				str += "*PAM*";
				// 新增配件
				String xzpj_str = "";
				try {
					if (dataXz != null && dataXz.size() > 0) {
						for (int i = 0; i < dataXz.size()-1; i++) {
							Map<String, String> map = dataXz.get(i);
							String sfhs = "是".equals(map.get("sfhs"))?"1":"2";
							xzpj_str = xzpj_str + map.get("hpbm") + "#@#"
									+ map.get("sl")+ "#@#"+ map.get("dj")+ "#@#"+ sfhs;
							xzpj_str = xzpj_str + "#^#";
						}
						xzpj_str = xzpj_str.substring(0, xzpj_str.length() - 3);
					} else {
						xzpj_str += "0";
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				str = str + xzpj_str;

				JSONObject json = this.callWebserviceImp.getWebServerInfo(
						"c#_PAD_KDG_FWBG_BJADD", str,
						DataCache.getinition().getUserId(), "",
						"uf_json_setdata2", this);
				String flag = json.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					upload();

				} else {
					msgStr = json.getString("msg");
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
	
	private void upload() {
		try {
			boolean flag = true;
			List<Map<String, String>> filelist = new ArrayList<Map<String, String>>();
			for (String mxh : filemap.keySet()) {
				List<String> filepathlist = filemap.get(mxh);
				for (int j = 0; j < filepathlist.size(); j++) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("mxh", mxh);
					map.put("num", j + "");
					map.put("filepath", filepathlist.get(j));
					filelist.add(map);
				}
			}
			int filenum = filelist.size();
			for (int i = 0; i < filenum; i++) {
				Map<String, String> map = filelist.get(i);
				if (flag) {
					String mxh = map.get("mxh");
					String filepath = map.get("filepath");
					String num = map.get("num");
					filepath = filepath.substring(7, filepath.length());
					// 压缩图片到100K
					filepath = ImageUtil.compressAndGenImage(ImageUtil.ratio(
							filepath, getScreenWidth(), getScreenHeight()),
							200, "jpg");
					File file = new File(filepath);
					flag = uploadPic(num, mxh, readJpeg(file),
							"uf_json_setdata", zbh, "c#_KDG_BJZP");
					file.delete();
				} else {
					flag = false;
					break;
				}
			}
			if (flag) {
				 Message msg = new Message();
				 msg.what = Constant.SUCCESS;
				 handler.sendMessage(msg);
			} else {
				Message msg = new Message();
				msg.what = 13;
				handler.sendMessage(msg);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Message msg = new Message();
			msg.what = 13;
			handler.sendMessage(msg);
		}

	}
	
	private class MyAdapter extends SimpleAdapter {

		public MyAdapter(Context context, List<? extends Map<String, ?>> data,
				int resource, String[] from, int[] to) {
			super(context, data, resource, from, to);

		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = super.getView(position, convertView, parent);
			ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);
			iv_close.setVisibility(View.VISIBLE);
			if (position == dataXz.size() - 1) {
				iv_close.setImageResource(R.drawable.add);
				iv_close.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Map<String, String> map = dataXz.get(position);
						ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
						list.add(map);
						Intent intent = new Intent(getApplicationContext(),
								AddParts.class);
						intent.putExtra("data", list);
						intent.putExtra("bjlb", data);
						intent.putExtra("ds", ds);
						intent.putExtra("zbh", zbh);
						intent.putExtra("ywhy_bm", ywhy_bm);
						startActivityForResult(intent, 3);
					}
				});
			}else{
				iv_close.setImageResource(R.drawable.close);
				iv_close.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dataXz.remove(position);
						if(dataXz.size()==0){
							Map<String, String> map = new HashMap<String, String>();
							map.put("hpbm", "");
							map.put("hpmc", "");
							map.put("sl", "");
							map.put("dj", "0");
							map.put("sfhs", "");
							dataXz.add(map);
						}
						Message msg = new Message();
						msg.what = Constant.NUM_6;
						handler.sendMessage(msg);
					}
				});
			}
			
			return view;
		}

	}
	
	private class MyYhpjAdapter extends SimpleAdapter {

		public MyYhpjAdapter(Context context, List<? extends Map<String, ?>> data,
				int resource, String[] from, int[] to) {
			super(context, data, resource, from, to);

		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = super.getView(position, convertView, parent);
			ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);
			iv_close.setVisibility(View.VISIBLE);
			iv_close.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					currmxh = dataYh.get(position).get("mxh");
					currposition = position;
					
					dialogShowMessage("是否删除该配件？",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface face,
										int paramAnonymous2Int) {
									showProgressDialog();
									Config.getExecutorService().execute(new Runnable() {

										@Override
										public void run() {
											getWebService("delpj");
										}
									});
								}
							}, null);
				}
			});
			return view;
		}

	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.FAIL:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P(msgStr, null);
				break;
			case Constant.SUCCESS:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P("提交成功",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								onBackPressed();
							}
						});
				break;
			case Constant.NETWORK_ERROR:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P(Constant.NETWORK_ERROR_STR, null);
				break;
			case Constant.NUM_6:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				adapter = new MyAdapter(getApplicationContext(), dataXz,
						R.layout.listview_item_addpj, from, to);
				listView.setAdapter(adapter);
				
				adapter = new MyYhpjAdapter(getApplicationContext(), dataYh,
						R.layout.listview_item_addpj, from, to);
				listView1.setAdapter(adapter);
				if(data_zp.size()>0){
					tv_yscpjtp.setBackgroundResource(R.drawable.btn_normal_yellow);
				}
				break;
			case Constant.NUM_7:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dataYh.remove(currposition);
				adapter = new MyYhpjAdapter(getApplicationContext(), dataYh,
						R.layout.listview_item_addpj, from, to);
				listView1.setAdapter(adapter);
				break;
			case 13:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P("提交成功,图片上传失败！",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								onBackPressed();
							}
						});
				break;
			}

		}
	};

	

}
