package com.nt.activity.kdg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.activity.util.ImagePagerActivity;

public class ShowImgActivity extends FrameActivity {

	private Button confirm, cancel;
	private GridView gridview;
	private ImageAdapter adapter;
	private DisplayImageOptions options;
	private String[] imageUrls;
	private Map<Integer, Boolean> checkMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_util_imgchoosepage);
		initVariable();
		initView();
		initListeners();
	}

	@Override
	protected void initVariable() {
		title.setText("批量选择图片");
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.camera)
				.showImageForEmptyUri(R.drawable.camera)
				.showImageOnFail(R.drawable.camera).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);
		cancel.setText("预览");
		gridview = (GridView) findViewById(R.id.gridview);
	}

	@Override
	protected void initView() {
		checkMap = new HashMap<Integer, Boolean>();
		List<String> list = getIntent().getStringArrayListExtra("imageUrls");
		imageUrls = list.toArray(new String[list.size()]);
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

			}
		});

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startImagePagerActivity(0);
			}
		});

		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				onBackPressed();
			}
		});

		topBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

	}

	private void startImagePagerActivity(int position) {
		Intent intent = new Intent(this, ImagePagerActivity.class);
		intent.putExtra("imageUrls", imageUrls);
		intent.putExtra("position", position);
		startActivity(intent);
	}

	@Override
	protected void getWebService(String s) {
		// TODO Auto-generated method stub

	}

	protected class ImageAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return imageUrls.length;
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view;
			ImageView child_image;
			CheckBox child_checkbox;
			// if (convertView == null) {
			// view =
			// getLayoutInflater().inflate(R.layout.item_grid_image,parent,
			// false);
			// } else {
			// view = convertView;
			// }
			view = getLayoutInflater().inflate(R.layout.item_grid_image,
					parent, false);
			child_image = (ImageView) view.findViewById(R.id.child_image);
			child_checkbox = (CheckBox) view.findViewById(R.id.child_checkbox);
			imageLoader.displayImage(imageUrls[position], child_image, options);
			child_checkbox.setVisibility(View.GONE);

			return view;
		}
	}

}
