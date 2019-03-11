package com.nt.activity.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.common.Constant;

/**
 * 照片目录
 * 
 * @author zdkj
 *
 */
public class ImgChoose extends FrameActivity {

	private GridView gridview;
	private ImageAdapter adapter;
	private Map<String, List<String>> groupMap;
	private List<Map<String, Object>> data_list;
	private DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_util_imgchoose);
		initVariable();
		initView();
		initListeners();
	}

	@Override
	protected void initVariable() {
		title.setText("图片目录");
		groupMap = new HashMap<String, List<String>>();
		data_list = new ArrayList<Map<String, Object>>();
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.camera)
				.showImageForEmptyUri(R.drawable.camera)
				.showImageOnFail(R.drawable.camera).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		gridview = (GridView) findViewById(R.id.gridview);
	}

	@Override
	protected void initView() {

		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
			return;
		}

		Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		ContentResolver mContentResolver = getContentResolver();

		// 只查询jpeg和png的图片
		Cursor mCursor = mContentResolver.query(mImageUri, null,
				MediaStore.Images.Media.MIME_TYPE + "=? or "
						+ MediaStore.Images.Media.MIME_TYPE + "=?",
				new String[] { "image/jpeg", "image/png" },
				MediaStore.Images.Media.DATE_MODIFIED + " DESC");

		while (mCursor.moveToNext()) {
			// 获取图片的路径
			String path = mCursor.getString(mCursor
					.getColumnIndex(MediaStore.Images.Media.DATA));
			// 获取该图片的父路径名
			String parentName = new File(path).getParentFile().getName();

			// 根据父路径名将图片放入到mGruopMap中
			if (!groupMap.containsKey(parentName)) {
				List<String> chileList = new ArrayList<String>();
				chileList.add("file://" + path);
				groupMap.put(parentName, chileList);
			} else {
				groupMap.get(parentName).add("file://" + path);
			}

			// String imageUri = "http://site.com/image.png"; // from Web
			// String imageUri = "file:///mnt/sdcard/image.png"; // from SD card
			// String imageUri = "content://media/external/audio/albumart/13";
			// // from content provider
			// String imageUri = "assets://image.png"; // from assets
			// String imageUri = "drawable://" + R.drawable.image; // from
			// drawables (only images, non-9patch)
		}
		mCursor.close();
		for (String key : groupMap.keySet()) {
			List<String> value = groupMap.get(key);
			Map<String, Object> map_ = new HashMap<String, Object>();
			map_.put("group_image", value.get(0));
			map_.put("group_count", value.size());
			map_.put("group_title", key);
			data_list.add(map_);
		}
		adapter = new ImageAdapter();
		// 配置适配器
		gridview.setAdapter(adapter);
	}

	@Override
	protected void initListeners() {
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ArrayList<String> arraylist = (ArrayList<String>) groupMap
						.get(data_list.get(position).get("group_title")
								.toString());
				Intent intent = new Intent(getApplicationContext(),
						ImgChoosePage.class);
				intent.putStringArrayListExtra("imageUrls", arraylist);
				startActivityForResult(intent, 1);
			}
		});

		topBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ImgChoosePage.class);
				intent.putStringArrayListExtra("imageUrls", new ArrayList<String>());
				startActivityForResult(intent, 1);

			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
			Intent intent = getIntent();
			intent.putStringArrayListExtra("imglist",
					data.getStringArrayListExtra("imglist"));
			setResult(-1, intent);
			finish();
		}
	}

	@Override
	protected void getWebService(String s) {
		// TODO Auto-generated method stub

	}

	protected class ImageAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return data_list.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			View view = convertView;
			if (view == null) {
				view = getLayoutInflater().inflate(R.layout.item_grid_group,
						parent, false);
				holder = new ViewHolder();
				assert view != null;
				holder.group_image = (ImageView) view
						.findViewById(R.id.group_image);
				holder.group_count = (TextView) view
						.findViewById(R.id.group_count);
				holder.group_title = (TextView) view
						.findViewById(R.id.group_title);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			Map<String, Object> map_ = data_list.get(position);
			imageLoader.displayImage(map_.get("group_image").toString(),
					holder.group_image, options);
			holder.group_count.setText(map_.get("group_count").toString());
			holder.group_title.setText(map_.get("group_title").toString());
			return view;
		}

		class ViewHolder {
			ImageView group_image;
			TextView group_count;
			TextView group_title;
		}
	}
}
