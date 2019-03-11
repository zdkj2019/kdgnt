package com.nt.update;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.nt.common.Constant;

import android.app.ProgressDialog;
import android.os.Environment;

public class DownLoadManager {

	private static boolean cancelUpdate = false;

	public static boolean isCancelUpdate() {
		return cancelUpdate;
	}

	public static void setCancelUpdate(boolean cancelUpdate) {
		DownLoadManager.cancelUpdate = cancelUpdate;
	}

	public static File getFileFromServer(String path, ProgressDialog pd)
			throws Exception {
		// 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			// 获取到文件的大小
			pd.setMax(conn.getContentLength());
			InputStream is = conn.getInputStream();
			File file = new File(Environment.getExternalStorageDirectory(),
					Constant.AppName);
			FileOutputStream fos = new FileOutputStream(file);
			BufferedInputStream bis = new BufferedInputStream(is);
			byte[] buffer = new byte[1024];
			int len;
			int total = 0;
			while ((len = bis.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
				total += len;
				// 获取当前下载量
				pd.setProgress(total);
				if (isCancelUpdate()) {
					fos.close();
					bis.close();
					is.close();
				}
			}
			fos.close();
			bis.close();
			is.close();
			return file;
			// } else {
			//
			// URL url = new URL(path);
			// HttpURLConnection conn = (HttpURLConnection)
			// url.openConnection();
			// conn.setConnectTimeout(5000);
			// // 获取到文件的大小
			// pd.setMax(conn.getContentLength());
			// InputStream is = conn.getInputStream();
			// // data/data/
			// File file = new File("/",
			// "StmAfterServicelf.apk");
			// FileOutputStream fos = new FileOutputStream(file);
			// BufferedInputStream bis = new BufferedInputStream(is);
			// byte[] buffer = new byte[1024];
			// int len;
			// int total = 0;
			// while ((len = bis.read(buffer)) != -1) {
			// fos.write(buffer, 0, len);
			// total += len;
			// // 获取当前下载量
			// pd.setProgress(total);
			// if (isCancelUpdate()) {
			// fos.close();
			// bis.close();
			// is.close();
			// }
			// }
			// fos.close();
			// bis.close();
			// is.close();
			// return file;
			//
			//
		}
		return null;
	}
}
