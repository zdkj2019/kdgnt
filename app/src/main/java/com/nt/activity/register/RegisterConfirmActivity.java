package com.nt.activity.register;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.utils.Config;
import com.nt.utils.IDCard;
import com.nt.utils.ImageUtil;
import com.nt.utils.PhoneNum;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterConfirmActivity extends FrameActivity {

	private Spinner spinner_province, spinner_city, spinner_area;
	private TextView tv_name, tv_fwqy;
	private EditText et_name, et_selfnum, et_address, et_phonenum, et_sfz,
			et_yyzz, et_qyname, et_phonenum2;
	private LinearLayout linear_yytzz, ll_qyname;
	private ImageView image_yyzz, image_sfz;
	private Button btn_next, btn_select;
	private int type = 0;
	private String filename;
	private File photo_file1 = null, photo_file2 = null;
	private int photonum = 1;

	private List<Map<String, String>> list_province, list_city, list_area,
			list_fwqy;

	private View login_view;
	protected AlertDialog.Builder builder;
	protected AlertDialog dialog;

	private String ids = "", names = "", name = "", selfnum = "", address = "",
			phonenum = "", phonenum2 = "", province = "", city = "", area = "",
			qyname = "", province_id = "", city_id = "", area_id = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_confirm);
		initView();
		initListener();
		loadProvinces();
	}

	protected void initView() {

		Intent intent = getIntent();
		type = intent.getIntExtra("type", 0);

		tv_name = (TextView) findViewById(R.id.tv_name);
		et_name = (EditText) findViewById(R.id.et_name);
		et_selfnum = (EditText) findViewById(R.id.et_selfnum);
		et_address = (EditText) findViewById(R.id.et_address);
		et_phonenum = (EditText) findViewById(R.id.et_phonenum);
		et_sfz = (EditText) findViewById(R.id.et_sfz);
		et_yyzz = (EditText) findViewById(R.id.et_yyzz);
		et_qyname = (EditText) findViewById(R.id.et_qyname);
		et_phonenum2 = (EditText) findViewById(R.id.et_phonenum2);

		spinner_province = (Spinner) findViewById(R.id.spinner_province);
		spinner_city = (Spinner) findViewById(R.id.spinner_city);
		spinner_area = (Spinner) findViewById(R.id.spinner_area);
		tv_fwqy = (TextView) findViewById(R.id.tv_fwqy);

		linear_yytzz = (LinearLayout) findViewById(R.id.linear_yytzz);
		ll_qyname = (LinearLayout) findViewById(R.id.ll_qyname);

		image_yyzz = (ImageView) findViewById(R.id.image_yyzz);
		image_sfz = (ImageView) findViewById(R.id.image_sfz);

		btn_next = (Button) findViewById(R.id.btn_next);
		btn_select = (Button) findViewById(R.id.btn_select);

		if (type == 1) {
			tv_name.setText("姓名：");
			linear_yytzz.setVisibility(View.GONE);
			ll_qyname.setVisibility(View.GONE);
			findViewById(R.id.line_1).setVisibility(View.GONE);
		} else {
			tv_name.setText("签约代表姓名：");
			linear_yytzz.setVisibility(View.VISIBLE);
			ll_qyname.setVisibility(View.VISIBLE);
			findViewById(R.id.line_1).setVisibility(View.VISIBLE);
		}
	}

	protected void initListener() {
		et_sfz.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				photonum = 1;
				camera();
			}
		});

		et_yyzz.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				photonum = 2;
				camera();
			}
		});

		spinner_province
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						String dqbm = list_province.get(position).get("dqbm")
								.toString();
						loadCitys(dqbm);
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});
		spinner_city.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String dqbm = list_city.get(position).get("dqbm").toString();
				loadAreas(dqbm);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		btn_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					qyname = et_qyname.getText().toString().trim();
					name = et_name.getText().toString().trim();
					selfnum = et_selfnum.getText().toString().trim();
					address = et_address.getText().toString().trim();
					phonenum = et_phonenum.getText().toString().trim();
					phonenum2 = et_phonenum2.getText().toString().trim();
					province = spinner_province.getSelectedItem().toString()
							.trim();
					city = spinner_city.getSelectedItem().toString().trim();
					area = "";
					try {
						area = spinner_area.getSelectedItem().toString();
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (type == 2) {
						if ("".equals(qyname)) {
							Toast.makeText(getApplicationContext(), "企业名称不能为空",
									1).show();
							return;
						}
					}
					if ("".equals(name)) {
						String msg = "";
						if (type == 1) {
							msg = "姓名";
						} else {
							msg = "签约代表姓名";
						}
						Toast.makeText(getApplicationContext(), msg + "不能为空", 1)
								.show();
						return;
					}
					if ("".equals(selfnum)) {
						Toast.makeText(getApplicationContext(), "身份证号码不能为空", 1)
								.show();
						return;
					}
					String ret = new IDCard().IDCardValidate(selfnum);
					if (!"".equals(ret)) {
						Toast.makeText(getApplicationContext(), ret, 1).show();
						return;
					}
					if ("".equals(names)) {
						Toast.makeText(getApplicationContext(), "服务区域不能为空", 1)
								.show();
						return;
					}
					if ("".equals(address)) {
						Toast.makeText(getApplicationContext(), "具体地址不能为空", 1)
								.show();
						return;
					}
					if ("".equals(phonenum)) {
						Toast.makeText(getApplicationContext(), "联系方式不能为空", 1)
								.show();
						return;
					}
					if ("".equals(phonenum2)) {
						Toast.makeText(getApplicationContext(), "紧急联系方式不能为空", 1)
								.show();
						return;
					}
					if (!PhoneNum.isMobile(phonenum)
							&& !PhoneNum.isPhone(phonenum)) {
						Toast.makeText(getApplicationContext(), "联系方式不正确", 1)
								.show();
						return;
					}
					if (!PhoneNum.isMobile(phonenum2)
							&& !PhoneNum.isPhone(phonenum2)) {
						Toast.makeText(getApplicationContext(), "紧急联系方式不正确", 1)
								.show();
						return;
					}
					if (phonenum.equals(phonenum2)) {
						Toast.makeText(getApplicationContext(),
								"联系方式与紧急联系方式不能相同", 1).show();
						return;
					}
					if (type == 2) {
						if (photo_file2 == null) {
							Toast.makeText(getApplicationContext(),
									"营业执照扫描件不能为空", 1).show();
							return;
						}
					}
					if (photo_file1 == null) {
						Toast.makeText(getApplicationContext(), "身份证扫描件不能为空", 1)
								.show();
						return;
					}
					address = province + city + area + address;
					checkPhone(phonenum);
				} catch (Exception e) {
					e.printStackTrace();
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);
				}
			}

		});

		btn_select.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadFwqy();
			}
		});
	}

	private void checkPhone(final String phonenum) {
		showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				try {
					JSONObject jsonobject = callWebserviceImp.getWebServerInfo(
							"_PAD_ZC_SJHYZ", phonenum, "uf_json_getdata",
							getApplicationContext());
					if ("0".equals(jsonobject.get("flag"))) {
						Message msg = new Message();
						msg.what = 7;
						handler.sendMessage(msg);
					} else {
						Message msg = new Message();
						msg.what = 8;
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Message msg = new Message();
					msg.what = 0;
					handler.sendMessage(msg);
				}

			}
		});
	}

	/**
	 * 弹出登录框
	 */
	protected void showLoginDialog() {
		login_view = LayoutInflater.from(this).inflate(R.layout.include_fwqy,
				null);

		builder = new AlertDialog.Builder(this);

		dialog = builder.setTitle("选择服务区域")
				.setIcon(android.R.drawable.ic_dialog_info).setView(login_view)
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				})
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						addFwqy();
					}
				}).create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();

		LinearLayout lin_fwqy = (LinearLayout) login_view
				.findViewById(R.id.lin_fwqy);
		for (int i = 0; i < list_fwqy.size(); i++) {
			String dqmc = list_fwqy.get(i).get("dqmc");
			String dqbm = list_fwqy.get(i).get("dqbm");
			CheckBox c = new CheckBox(getApplicationContext());
			c.setTag(dqbm);
			c.setText(dqmc);
			c.setTextColor(Color.rgb(0, 0, 0));
			lin_fwqy.addView(c);
		}

	}

	private void addFwqy() {
		try {
			LinearLayout lin_fwqy = (LinearLayout) login_view
					.findViewById(R.id.lin_fwqy);
			int num = lin_fwqy.getChildCount();

			ids = "";
			names = "";
			for (int i = 0; i < num; i++) {
				CheckBox c = (CheckBox) lin_fwqy.getChildAt(i);
				if (c.isChecked()) {
					ids += c.getTag() + ",";
					names += c.getText() + "\n";
				}
			}
			if (!"".equals(ids)) {
				ids = ids.substring(0, ids.length() - 1);
			}

			Message msg = new Message();
			msg.what = 6;
			handler.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
			Message msg = new Message();
			msg.what = 0;
			handler.sendMessage(msg);
		}

	}

	private void loadProvinces() {
		// showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				try {
					JSONObject jsonobject = callWebserviceImp.getWebServerInfo(
							"_PAD_ESP_ZC_S", "", "uf_json_getdata",
							getApplicationContext());
					JSONArray jsonarray = jsonobject.getJSONArray("tableA");
					list_province = new ArrayList<Map<String, String>>();
					for (int i = 0; i < jsonarray.length(); i++) {
						JSONObject json = jsonarray.getJSONObject(i);
						Map<String, String> map = new HashMap<String, String>();
						map.put("dqbm", (String) json.get("dqbm"));
						map.put("dqmc", (String) json.get("dqmc"));
						list_province.add(map);
					}
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
					Message msg = new Message();
					msg.what = 0;
					handler.sendMessage(msg);
				}

			}
		});
	}

	private void loadCitys(final String id) {
		// showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				try {
					JSONObject jsonobject = callWebserviceImp.getWebServerInfo(
							"_PAD_ESP_ZC_SS", id, "uf_json_getdata",
							getApplicationContext());
					JSONArray jsonarray = jsonobject.getJSONArray("tableA");
					list_city = new ArrayList<Map<String, String>>();
					for (int i = 0; i < jsonarray.length(); i++) {
						JSONObject json = jsonarray.getJSONObject(i);
						Map<String, String> map = new HashMap<String, String>();
						map.put("dqbm", (String) json.get("dqbm"));
						map.put("dqmc", (String) json.get("dqmc"));
						list_city.add(map);
					}
					Message msg = new Message();
					msg.what = 2;
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
					Message msg = new Message();
					msg.what = 0;
					handler.sendMessage(msg);
				}

			}
		});
	}

	private void loadAreas(final String id) {
		// showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				try {
					JSONObject jsonobject = callWebserviceImp.getWebServerInfo(
							"_PAD_ESP_ZC_SSX", id, "uf_json_getdata",
							getApplicationContext());
					JSONArray jsonarray = jsonobject.getJSONArray("tableA");
					list_area = new ArrayList<Map<String, String>>();
					for (int i = 0; i < jsonarray.length(); i++) {
						JSONObject json = jsonarray.getJSONObject(i);
						Map<String, String> map = new HashMap<String, String>();
						map.put("dqbm", (String) json.get("dqbm"));
						map.put("dqmc", (String) json.get("dqmc"));
						list_area.add(map);
					}
					Message msg = new Message();
					msg.what = 3;
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
					Message msg = new Message();
					msg.what = 0;
					handler.sendMessage(msg);
				}

			}
		});
	}

	private void loadFwqy() {
		showProgressDialog();
		try {
			province_id = list_province.get(
					spinner_province.getSelectedItemPosition()).get("dqbm");
			city_id = list_city.get(spinner_city.getSelectedItemPosition())
					.get("dqbm");
			area_id = list_area.get(spinner_area.getSelectedItemPosition())
					.get("dqbm");
		} catch (Exception e) {
			e.printStackTrace();
		}
		final String provinceid = province_id;
		final String cityid = city_id;
		final String areaid = area_id;
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				try {
					JSONObject jsonobject = callWebserviceImp.getWebServerInfo(
							"_PAD_KF_SSPQ", provinceid + "*" + provinceid + "*"
									+ cityid + "*" + areaid, "uf_json_getdata",
							getApplicationContext());
					JSONArray jsonarray = jsonobject.getJSONArray("tableA");
					list_fwqy = new ArrayList<Map<String, String>>();
					for (int i = 0; i < jsonarray.length(); i++) {
						JSONObject json = jsonarray.getJSONObject(i);
						Map<String, String> map = new HashMap<String, String>();
						map.put("dqbm", (String) json.get("khjgbm"));
						map.put("dqmc", (String) json.get("khjgmc"));
						list_fwqy.add(map);
					}
					Message msg = new Message();
					msg.what = 5;
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
					Message msg = new Message();
					msg.what = 0;
					handler.sendMessage(msg);
				}

			}
		});
	}
	
	private void camera() {
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
			toastShowMessage("没有储存卡，不能拍照");
			return;
		}
		filename = String.valueOf(System.currentTimeMillis()).trim().substring(4);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
				Environment.getExternalStorageDirectory(), filename + ".jpg")));
		startActivityForResult(intent, 1);

	}

	@SuppressLint("ShowToast")
	@SuppressWarnings("unused")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
			// 读pic
			String filepath = Environment.getExternalStorageDirectory()
					.toString() + "/" + filename + ".jpg";
			try {
				File file = new File(filepath);
				if (file == null) {
					Toast.makeText(getApplicationContext(), "图片不存在！", 1).show();
					return;
				}
				// 压缩图片到100K
				filepath = ImageUtil.compressAndGenImage(ImageUtil.ratio(filepath,getScreenWidth(),getScreenHeight()), 200, "jpg");
				if (photonum == 1) {
					photo_file1 = new File(filepath);
					image_sfz.setImageBitmap(ImageUtil.getBitmap(filepath));
					image_sfz.setVisibility(View.VISIBLE);
					et_sfz.setText("重新拍照");
				} else if (photonum == 2) {
					photo_file2 = new File(filepath);
					image_yyzz.setImageBitmap(ImageUtil.getBitmap(filepath));
					image_yyzz.setVisibility(View.VISIBLE);
					et_yyzz.setText("重新拍照");
				}
			} catch (IOException e) {
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), "压缩图片尺寸失败，请调低像素后重新拍照",
						1).show();
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), "压缩图片大小失败，请调低像素后重新拍照",
						1).show();
			}

		} else {
			Toast.makeText(getApplicationContext(), "拍照失败", 1).show();
		}
	}

	@Override
	protected void initVariable() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void getWebService(String s) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initListeners() {
		// TODO Auto-generated method stub

	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			switch (msg.what) {
			case 0:
				dialogShowMessage_P("网络连接失败,请检查网络连接是否正常！", null);
				break;
			case 1:
				List list = new ArrayList();
				for (int i = 0; i < list_province.size(); i++) {
					list.add(list_province.get(i).get("dqmc"));
				}
				list.add("");
				ArrayAdapter<Map<String, String>> province_adapter = new ArrayAdapter<Map<String, String>>(
						getApplicationContext(),
						R.layout.spinner_item_register, list);
				province_adapter
						.setDropDownViewResource(R.layout.spinner_dropdown_item);
				spinner_province.setAdapter(province_adapter);
				break;
			case 2:
				List list1 = new ArrayList();
				for (int i = 0; i < list_city.size(); i++) {
					list1.add(list_city.get(i).get("dqmc"));
				}
				list1.add("");
				ArrayAdapter<Map<String, String>> city_adapter = new ArrayAdapter<Map<String, String>>(
						getApplicationContext(),
						R.layout.spinner_item_register, list1);
				city_adapter
						.setDropDownViewResource(R.layout.spinner_dropdown_item);
				spinner_city.setAdapter(city_adapter);
				break;

			case 3:

				List list2 = new ArrayList();
				for (int i = 0; i < list_area.size(); i++) {
					list2.add(list_area.get(i).get("dqmc"));
				}
				list2.add("");
				ArrayAdapter<Map<String, String>> area_adapter = new ArrayAdapter<Map<String, String>>(
						getApplicationContext(),
						R.layout.spinner_item_register, list2);
				area_adapter
						.setDropDownViewResource(R.layout.spinner_dropdown_item);
				spinner_area.setAdapter(area_adapter);

				break;
			case 4:
				dialogShowMessage_P("网络连接失败,请检查网络连接是否正常！", null);
				break;
			case 5:
				showLoginDialog();
				break;
			case 6:
				tv_fwqy.setText(names);
				break;
			case 7:
				Intent intent = new Intent(getApplicationContext(),
						RegisterAgreeActivity.class);
				intent.putExtra("type", type);
				intent.putExtra("name", name);
				intent.putExtra("qyname", qyname);
				intent.putExtra("selfnum", selfnum);
				intent.putExtra("address", address);
				intent.putExtra("phonenum", phonenum);
				intent.putExtra("phonenum2", et_phonenum2.getText().toString());
				intent.putExtra("province", province);
				intent.putExtra("ids", ids);
				intent.putExtra("city", city);
				intent.putExtra("area", area);
				intent.putExtra("province_id", province_id);
				intent.putExtra("city_id", city_id);
				intent.putExtra("area_id", area_id);
				intent.putExtra("photo_file2", readJpeg(photo_file2));
				intent.putExtra("photo_file1", readJpeg(photo_file1));
				startActivity(intent);

				if (photo_file1 != null) {
					photo_file1.delete();
				}
				if (photo_file2 != null) {
					photo_file2.delete();
				}
				break;
			case 8:
				dialogShowMessage_P("该手机号码已注册，不能重复注册！", null);
				break;
			case 9:
				dialogShowMessage_P("照片压缩失败！", null);
				break;
			default:
				break;
			}
		};
	};
}
