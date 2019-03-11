package com.nt.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.nt.R;
import com.nt.activity.util.ImgPz;
import com.nt.common.Constant;
import com.nt.webservice.CallWebserviceImp;

@SuppressLint("SimpleDateFormat")
public abstract class FrameActivity extends BaseActivity {

	protected CallWebserviceImp callWebserviceImp = new CallWebserviceImp();
	protected TextView title, showUserId;
	protected LinearLayout topBack;
	protected Calendar c = Calendar.getInstance();
	protected int m_year, m_month, m_day;
	protected List<String> wxaz;

	// protected DataCache dataCache = DataCache.getinition();

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// if (null != savedInstanceState)

		// {
		// Log.e("ddd","null != savedInstanceState");
		// Object saved = savedInstanceState.getSerializable("datacache");
		// if (saved instanceof Stack) {
		// @SuppressWarnings("unused")
		// Stack<DataCache> mLocationInfoVector = (Stack<DataCache>) saved;
		// } else if (saved != null) {
		// Log.e("RestoreBug", "Restored object class: "
		// + saved.getClass().getName());
		// }
		// // System.err.println("savedInstanceState="
		// // + savedInstanceState.getSerializable("datacache"));
		// // DataCache
		// // .setDataCache((com.nt.cache.DataCache) savedInstanceState
		// // .getSerializable("datacache"));
		//
		// List<String> menu = DataCache.getinition().getMenu();
		// String string = menu.get(0);
		// Log.e("dd", "menu.get(0)=" + string);
		//
		// String StrTest = savedInstanceState.getString("StrTest");
		//
		// Log.e("dd", "onCreate get the savedInstanceState+IntTest="
		// + "+StrTest=" + StrTest);
		//
		// }
		setContentView(R.layout.activity_frame);
		topBack = (LinearLayout) findViewById(R.id.bt_topback);
		title = (TextView) findViewById(R.id.interfacename);
		m_year = c.get(Calendar.YEAR);
		m_month = c.get(Calendar.MONTH) + 1;
		m_day = c.get(Calendar.DAY_OF_MONTH);
		wxaz = new LinkedList<String>();
		wxaz.add("巡检");// 1
		wxaz.add("维修");// 2
		wxaz.add("安装");// 3
		wxaz.add("其他");// 4
		wxaz.add("撤机");// 5
		wxaz.add("换密键");// 6
		wxaz.add("升级");// 7

	}

	/**
	 * 获取屏幕宽度
	 * 
	 * @return
	 */
	protected int getScreenWidth() {
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;// 宽度
		return width;
	}

	/**
	 * 获取屏幕高度
	 * 
	 * @return
	 */
	protected int getScreenHeight() {
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int height = dm.heightPixels;// 高度
		return height;
	}

	protected String getVersion() {
		try {
			// 获取packagemanager的实例
			PackageManager packageManager = getPackageManager();
			// getPackageName()是你当前类的包名，0代表是获取版本信息
			PackageInfo packInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			return packInfo.versionName;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	// @Override
	// public void onSaveInstanceState(Bundle savedInstanceState) {
	//
	// // Save away the original text, so we still have it if the activity
	//
	// // needs to be killed while paused.
	//
	// savedInstanceState.putSerializable("datacache", DataCache.class);
	//
	// super.onSaveInstanceState(savedInstanceState);
	//
	// Log.e("dd", "onSaveInstanceState");
	//
	// }
	//
	// @Override
	// public void onRestoreInstanceState(Bundle savedInstanceState) {
	//
	// super.onRestoreInstanceState(savedInstanceState);
	//
	// DataCache.setDataCache((DataCache) savedInstanceState
	// .getSerializable("datacache"));
	// List<String> menu = DataCache.getinition().getMenu();
	// String string = menu.get(0);
	// Log.e("ss", "menu.get(0)=" + string);
	//
	// Log.e("dd", "onRestoreInstanceState");
	//
	// }

	protected void appendMainBody(int resId) {
		LinearLayout mainBody = (LinearLayout) findViewById(R.id.layMainBody);
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(resId, null);
		LinearLayout.LayoutParams layoutParams = new LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		mainBody.addView(view, layoutParams);
	}

	protected void myDialog(String title, View view,
			OnClickListener confirmListener, OnClickListener cancelListener) {
		AlertDialog.Builder builder = new Builder(this);
		builder.setCancelable(false);
		builder.setTitle(title);
		builder.setView(view);
		builder.setPositiveButton("确认", confirmListener);
		builder.setNegativeButton("取消", cancelListener);
		builder.create().show();
	}

	protected void Call(String tel) {
		Intent phoneIntent = new Intent("android.intent.action.CALL",
				Uri.parse("tel:" + tel));
		startActivity(phoneIntent);
	}

	// protected void showDateDialog(String title, final Class<?> cls) {
	// final TextView start, end;
	// View view = LayoutInflater.from(this).inflate(R.layout.custom_dialog,
	// null);
	//
	// start = (TextView) view.findViewById(R.id.starttime);
	// end = (TextView) view.findViewById(R.id.endtime);
	//
	// start.setOnClickListener(new View.OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// new DatePickerDialog(FrameActivity.this,
	// new DatePickerDialog.OnDateSetListener() {
	//
	// @Override
	// public void onDateSet(DatePicker view, int year,
	// int monthOfYear, int dayOfMonth) {
	// // TODO Auto-generated method stub
	// c.set(Calendar.YEAR, year);
	// c.set(Calendar.MONTH, monthOfYear);
	// c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
	// DataCache.setStart(formatTime());
	// // System.out.println("开始时间1-------"
	// // + formatTime());
	// start.setText(formatTime());
	// }
	// }, m_year, m_month, m_day).show();
	// }
	//
	// });
	//
	// end.setOnClickListener(new View.OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// new DatePickerDialog(FrameActivity.this,
	// new DatePickerDialog.OnDateSetListener() {
	//
	// @Override
	// public void onDateSet(DatePicker view, int year,
	// int monthOfYear, int dayOfMonth) {
	// // TODO Auto-generated method stub
	// c.set(Calendar.YEAR, year);
	// c.set(Calendar.MONTH, monthOfYear);
	// c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
	// DataCache.setEnd(formatTime());
	// // System.out.println("结束时间2-------"
	// // + formatTime());
	// end.setText(formatTime());
	// }
	// }, m_year, m_month, m_day).show();
	// }
	// });
	//
	// myDialog(title, view, new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// // TODO Auto-generated method stub
	// if (DataCache.getStart() != null
	// && !"".equals(DataCache.getStart())
	// && DataCache.getEnd() != null
	// && !"".equals(DataCache.getEnd())) {
	// skipActivity(cls);
	// } else {
	// dialogShowMessage("请设置查询的开始和结束时间", null,null);
	// }
	// }
	// }, null);
	// }

	protected String formatTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String s = df.format(c.getTime());
		return s;
	}

	protected String formatTime2() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String s = df.format(c.getTime());
		return s;
	}

	protected String formatTime3() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
		String s = df.format(c.getTime());
		return s;
	}

	protected void dateDialog(TextView textView) {
		final TextView time;
		time = textView;
		new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				c.set(Calendar.YEAR, year);
				c.set(Calendar.MONTH, monthOfYear);
				c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				// DataCache.setStart(formatTime2());
				time.setText(formatTime2());
			}
		}, m_year, m_month, m_day).show();
	}

	protected void dateDialog(final EditText tv) {
		new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				c.set(Calendar.YEAR, year);
				c.set(Calendar.MONTH, monthOfYear);
				c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				// DataCache.setStart(formatTime2());
				tv.setText(formatTime2());
			}
		}, m_year, m_month, m_day).show();
	}

	protected void MonthDialog(final EditText tv) {
		DatePickerDialog dp = new DatePickerDialog(this,
				new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						// TODO Auto-generated method stub
						c.set(Calendar.YEAR, year);
						c.set(Calendar.MONTH, monthOfYear);
						c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
						// DataCache.setStart(formatTime2());
						tv.setText(formatTime3());
					}
				}, m_year, m_month, m_day);
		dp.show();
	}

	/**
	 * 显示日期控件
	 * 
	 * @param v
	 */
	@SuppressLint("NewApi")
	public void showDateSelector(final View v) {
		final Calendar calendar = Calendar.getInstance();
		final EditText et = (EditText) v;
		/**
		 * @description 日期设置匿名类
		 */
		DatePickerDialog.OnDateSetListener DateSet = new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// 每次保存设置的日期
				calendar.set(Calendar.YEAR, year);
				calendar.set(Calendar.MONTH, monthOfYear);
				calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				monthOfYear = monthOfYear + 1;
				String month = "";
				String day = "";
				if (monthOfYear < 10) {
					month = "0" + monthOfYear;
				} else {
					month = "" + monthOfYear;
				}
				if (dayOfMonth < 10) {
					day = "0" + dayOfMonth;
				} else {
					day = "" + dayOfMonth;
				}
				String str = year + "-" + month + "-" + day;
				// String str = year + month;
				et.setText(str);
			}
		};
		DatePickerDialog datePickerDialog = new DatePickerDialog(this, DateSet,
				calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
		datePickerDialog.getDatePicker().setCalendarViewShown(false);
		datePickerDialog.show();
	}

	protected String mdateformat(int format, String time) {

		String[] times = time.split(" ");
		String t = times[format];

		if (time.indexOf("1990") != -1) {
			t = "";
		}
		if (format == 1) {
			String[] strings = t.split(":");
			t = strings[0] + ":" + strings[1];
		}
		return t;
	}

	protected int addone(int index2) {
		if (index2 >= 2) {
			index2 = index2 + 1;
		}
		return ++index2;
	}

	/**
	 * 根据data里面的arg字段的值s 返回 arg所在的index
	 * 
	 * @param data
	 * @param s
	 * @param arg
	 * @return
	 */
	protected int setChooseOptional(List<Map<String, String>> data, String s,
			String arg) {
		if (s != null && !"".equals(s)) {
			for (int i = 0; i < data.size(); i++) {
				if (s.equals(data.get(i).get(arg))) {
					return i;
				}
			}
		}
		return 0;
	}

	/**
	 * EditText的text是否为空
	 * 
	 * @param v
	 *            EditText
	 * @return boolean 非空 -》true
	 */
	protected boolean isNotNull(View v) {
		String temp = null;
		if (v instanceof EditText) {
			temp = ((EditText) v).getText().toString();
		} else if (v instanceof TextView) {
			temp = ((TextView) v).getText().toString();
		}
		if (temp != null && !"".equals(temp)) {
			return true;
		} else {
			if (v != null && v.getTag() != null) {
				String left = v.getTag() + "";
				// Toast.makeText(this, "请填写完"+left, Toast.LENGTH_SHORT).show();
			}
			return false;
		}
	}

	/**
	 * spinner 选中的不是第一个（id！=0）
	 * 
	 * @param v
	 *            Spinner
	 * @return boolean
	 */
	protected boolean isNotOne(View v) {
		long i = ((Spinner) v).getSelectedItemId();
		if (i > 0) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 
	 * @param s
	 *            一个map的tostring
	 * @param start
	 *            重多少位开始取
	 * @return 取得的string
	 */
	protected String getId(String s) {
		if (!"".equals(s) && s != null) {
			String[] s1 = s.split(",");
			String temp = s1[0];

			return temp.substring(4, temp.length());
		}
		return "";
	}

	protected void camera(int requestCode, ArrayList<String> list) {
		Intent intent = new Intent(getApplicationContext(), ImgPz.class);
		intent.putStringArrayListExtra("imglist", list);
		startActivityForResult(intent, requestCode);
	}

	protected void setSpinnerVal(Spinner spinner,
			ArrayList<Map<String, String>> data, String id) {
		for (int i = 0; i < data.size(); i++) {
			if (id.equals(data.get(i).get("id"))) {
				spinner.setSelection(i, true);
				break;
			}
		}
	}

	public static Uri getUriForFile(Context context, File file) {
		return FileProvider.getUriForFile(context, Constant.PackageName+".fileProvider", file);
	}

	protected boolean uploadPic(String num, String mxh, final byte[] data1,
			final String methed, String zbh, String sqlid) throws Exception {

		if (data1 != null && mxh != null) {
			JSONObject json = callWebserviceImp.getWebServerInfo2_pic(sqlid,
					num, zbh + "*" + mxh, "0001", data1,
					"uf_json_setdata2_p11", getApplicationContext());
			String flag = json.getString("flag");
			if ("1".equals(flag)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	protected byte[] readJpeg(File filename) {
		if (filename == null) {
			return null;
		}

		ByteArrayOutputStream baos = null;
		byte[] filebyteArray = null;
		try {
			@SuppressWarnings("resource")
			FileInputStream fis = new FileInputStream(filename);
			baos = new ByteArrayOutputStream();

			byte[] buffer = new byte[1024];
			int count = 0;
			while ((count = fis.read(buffer)) >= 0) {
				baos.write(buffer, 0, count);
			}

			filebyteArray = baos.toByteArray();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return filebyteArray;
	}

	protected int getListItemIcon(String hb) {
		if ("H000001".equals(hb)) {// 工行
			return R.drawable.yh_001;
		} else if ("H000002".equals(hb)) {// 中行
			return R.drawable.yh_002;
		} else if ("H000003".equals(hb)) {// 建行
			return R.drawable.yh_003;
		} else if ("H000004".equals(hb)) {// 光大
			return R.drawable.yh_004;
		} else if ("H000005".equals(hb)) {// 交行
			return R.drawable.yh_005;
		} else if ("H000006".equals(hb)) {// 恒丰
			return R.drawable.yh_006;
		} else if ("H000007".equals(hb)) {// 中信
			return R.drawable.yh_007;
		} else if ("H000008".equals(hb)) {// 三峡
			return R.drawable.yh_008;
		} else if ("H000009".equals(hb)) {// 浦发
			return R.drawable.yh_009;
		} else if ("H000011".equals(hb)) {// 大连
			return R.drawable.yh_011;
		} else if ("H000013".equals(hb)) {// 广东南粤
			return R.drawable.yh_013;
		} else if ("H000014".equals(hb)) {// 汉口
			return R.drawable.yh_014;
		} else if ("H000015".equals(hb)) {// 招商
			return R.drawable.yh_015;
		} else if ("H000016".equals(hb)) {// 民生
			return R.drawable.yh_016;
		} else if ("H000017".equals(hb)) {// 华夏
			return R.drawable.yh_017;
		} else if ("H000019".equals(hb)) {// 邮储
			return R.drawable.yh_019;
		} else if ("H000021".equals(hb)) {// 成都
			return R.drawable.yh_021;
		} else if ("H000022".equals(hb)) {// 渤海
			return R.drawable.yh_022;
		} else if ("H000023".equals(hb)) {// 农行
			return R.drawable.yh_023;
		} else if ("H000026".equals(hb)) {// 平安
			return R.drawable.yh_026;
		} else if ("H000027".equals(hb)) {// 兴业
			return R.drawable.yh_027;
		} else if ("H000029".equals(hb)) {// 富滇
			return R.drawable.yh_029;
		} else if ("H000030".equals(hb)) {// 上海
			return R.drawable.yh_030;
		} else if ("H000031".equals(hb)) {// 东莞
			return R.drawable.yh_031;
		} else if ("H000032".equals(hb)) {// 汇丰
			return R.drawable.yh_032;
		} else if ("H000033".equals(hb)) {// 东亚
			return R.drawable.yh_033;
		} else if ("H000034".equals(hb)) {// 台州
			return R.drawable.yh_034;
		} else if ("H000035".equals(hb)) {// 贵阳
			return R.drawable.yh_035;
		} else if ("H000036".equals(hb)) {// 广发
			return R.drawable.yh_036;
		} else if ("H000037".equals(hb)) {// 华侨
			return R.drawable.yh_037;
		} else {
			return R.drawable.yh_099;
		}

	}

	protected abstract void initVariable();

	protected abstract void initView();

	protected abstract void initListeners();

	protected abstract void getWebService(String s);

	@Override
	public void onBackPressed() {
		Intent intent = getIntent();
		setResult(-1, intent);
		finish();
	}
}
