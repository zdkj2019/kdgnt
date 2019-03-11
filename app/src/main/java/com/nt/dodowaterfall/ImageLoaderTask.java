package com.nt.dodowaterfall;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class ImageLoaderTask extends AsyncTask<TaskParam, Void, Bitmap> {

	private TaskParam param;
	private final WeakReference<ImageView> imageViewReference; // 闃叉鍐呭瓨婧㈠嚭

	public ImageLoaderTask(ImageView imageView) {
		imageViewReference = new WeakReference<ImageView>(imageView);

	}

	@Override
	protected Bitmap doInBackground(TaskParam... params) {

		param = params[0];
		return loadImageFile(param.getFilename(), param.getAssetManager());
	}

	private Bitmap loadImageFile(final String filename,
			final AssetManager manager) {
		InputStream is = null;
		try {
			
			Bitmap bmp = BitmapCache.getInstance().getBitmap(filename,
					param.getAssetManager());
			return bmp;
		} catch (Exception e) {
			Log.e(this.getClass().getSimpleName(), "fetchDrawable failed", e);
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(Bitmap bitmap) {
		if (isCancelled()) {
			bitmap = null;
		}

		if (imageViewReference != null) {
			ImageView imageView = imageViewReference.get();
			if (imageView != null) {
				if (bitmap != null) {
					int width = bitmap.getWidth();// 鑾峰彇鐪熷疄瀹介珮
					int height = bitmap.getHeight();
					LayoutParams lp = imageView.getLayoutParams();
					lp.height = (height * param.getItemWidth()) / width;// 璋冩暣楂樺害

					imageView.setLayoutParams(lp);

					imageView.setImageBitmap(bitmap);

				}

			}
		}
	}
}