package com.nt.activity.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.common.Constant;
import com.nt.utils.ImageUtil;

/**
 * 拍照
 * 
 * @author zdkj
 *
 */
public class ImgPz extends FrameActivity {

	private Button confirm, cancel;
	private GridView gridview;
	private List<String> imglist;
	private ImageAdapter adapter;
	private DisplayImageOptions options;
	private String[] imageUrls;
	private String filename, filepath, errorMsg = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_util_imgpz);
		initVariable();
		initView();
		initListeners();
	}

	@Override
	protected void initVariable() {
		title.setText("批量选择照片");
		imglist = getIntent().getStringArrayListExtra("imglist");
		if(imglist==null){
			imglist = new ArrayList<String>();
		}
	}

	@Override
	protected void initView() {
		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);
		cancel.setText("预览");
		gridview = (GridView) findViewById(R.id.gridview);
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.camera)
				.showImageForEmptyUri(R.drawable.camera)
				.showImageOnFail(R.drawable.camera).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		loadGridView();
	}

	@Override
	protected void initListeners() {

		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {

				if (imglist.size() == position) {
					String[] items = new String[]{"手机拍照", "从相册选择", "取消"};
					final AlertDialog.Builder builder = new AlertDialog.Builder(ImgPz.this);
					builder.setIcon(R.drawable.btn_img_down)
							.setTitle("请选择照片来源")
							.setItems(items,new android.content.DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
													int which) {
									if (which == 0) {
										filename = String.valueOf(System.currentTimeMillis()).trim().substring(4);
										Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
										intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
										intent.putExtra(MediaStore.EXTRA_OUTPUT,getUriForFile(getApplicationContext(),new File(Constant.SAVE_PIC_PATH, filename + ".jpg")));
										startActivityForResult(intent, 1);
									} else if (which == 1) {
										Intent intent = new Intent(getApplicationContext(),ImgChoose.class);
										startActivityForResult(intent, 2);
									} else {
										builder.create().dismiss();
									}

								}
							});
					builder.create().show();
				}
			}
		});

		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = getIntent();
				ArrayList<String> list = new ArrayList<String>();
				for (int i = 0; i < imageUrls.length - 1; i++) {
					list.add(imageUrls[i]);
				}
				intent.putStringArrayListExtra("imglist", list);
				setResult(-1, intent);
				finish();
			}
		});

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ImagePagerActivity.class);
				intent.putExtra("imageUrls", imageUrls);
				intent.putExtra("position", 0);
				startActivity(intent);

			}
		});

		topBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
			filepath = Constant.SAVE_PIC_PATH + "/" + filename + ".jpg";
			// 压缩图片
			try {
				ImageUtil.compressAndGenImage(
						ImageUtil.ratio(filepath, getScreenWidth(),getScreenHeight()),
						filepath, 200);
				// 发一个系统广播通知手机有图片更新
				Intent intent = new Intent(
						Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
				Uri uri = Uri.fromFile(new File(filepath));
				intent.setData(uri);
				sendBroadcast(intent);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			imglist.add("file://" + filepath);
			loadGridView();
		}
		if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
			ArrayList<String> list = data.getStringArrayListExtra("imglist");
			if(list!=null){
				for (int i = 0; i < list.size(); i++) {
					imglist.add(list.get(i));
				}
				loadGridView();
			}
		}
	}

	private void loadGridView() {
		imageUrls = new String[imglist.size() + 1];
		for (int i = 0; i < imglist.size(); i++) {
			imageUrls[i] = imglist.get(i);
		}
		imageUrls[imglist.size()] = "drawable://" + R.drawable.camera;

		adapter = new ImageAdapter();
		// 配置适配器
		gridview.setAdapter(adapter);
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
			ImageView child_checkbox;
			// if (convertView == null) {
			// view = getLayoutInflater().inflate(R.layout.item_grid_image_pz,
			// parent, false);
			// } else {
			// view = convertView;
			// }
			view = getLayoutInflater().inflate(R.layout.item_grid_image_pz,
					parent, false);
			child_image = (ImageView) view.findViewById(R.id.child_image);
			child_checkbox = (ImageView) view.findViewById(R.id.child_checkbox);
			imageLoader.displayImage(imageUrls[position], child_image, options);
			if (position < imageUrls.length - 1) {
				child_checkbox.setVisibility(View.VISIBLE);
				child_checkbox.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						for (int i = 0; i < imglist.size(); i++) {
							if (i == position) {
								imglist.remove(i);
							}
						}
						Message msg = new Message();
						msg.what = Constant.SUCCESS;
						handler.sendMessage(msg);
					}
				});
			}
			return view;
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.FAIL:
				dialogShowMessage_P("拍照失败，系统错误：" + errorMsg, null);
				break;
			case Constant.SUCCESS:
				loadGridView();
				break;
			case Constant.NETWORK_ERROR:
				dialogShowMessage_P(Constant.NETWORK_ERROR_STR, null);
				break;
			}
		}
	};

}
