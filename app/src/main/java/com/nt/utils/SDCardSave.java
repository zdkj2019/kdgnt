package com.nt.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.os.Environment;
import android.widget.Toast;
/**
 *  向sdcard中写入文件
 * @author wlj
 *
 */
public class SDCardSave extends Activity {

	/**
	 * 向sdcard中写入文件
	 * 
	 * @param filename
	 *            文件名
	 * @param content
	 *            文件内容
	 */
	public void saveToSDCard(String filename, String content) throws Exception {

		// 向SDCard中保存
		String en = Environment.getExternalStorageState();
		// 获取SDCard状态,如果SDCard插入了手机且为非写保护状态
		if (en.equals(Environment.MEDIA_MOUNTED)) {
			try {
				save(filename, content);
				Toast.makeText(getApplicationContext(), "保存成功", 1).show();
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), "保存失败", 1).show();
			}
		} else {

			// 提示用户SDCard不存在或者为写保护状态
			Toast.makeText(getApplicationContext(), "SDCard不存在或者为写保护状态", 1)
					.show();
		}

	}

	private void save(String filename, String content) throws Exception {
		File file = new File(Environment.getExternalStorageDirectory(),
				filename);
		OutputStream out = new FileOutputStream(file);
		out.write(content.getBytes());
		out.close();
	}

}
