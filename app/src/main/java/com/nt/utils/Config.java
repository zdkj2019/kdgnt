package com.nt.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;

public class Config {

	private static ExecutorService pool;
	private static String tmp = "InformationReceivinglist";

	public static ExecutorService getExecutorService() {

		if (pool == null) {

			pool = Executors.newFixedThreadPool(10);
		}
		return pool;
	}

	public static void writeFile(String name, String writestr, Context mContext) {

		// getFilesDir()
		FileOutputStream fout;
		try {
			fout = mContext.openFileOutput(name, mContext.MODE_PRIVATE);
			byte[] bytes = writestr.getBytes();
			fout.write(bytes);
			fout.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public static void writeFileSdcardFile(String fileName, String write_str)
			throws IOException {
		try {
			FileOutputStream fout = new FileOutputStream(fileName);
			byte[] bytes = write_str.getBytes("UTF-8");
			fout.write(bytes);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String readFileSdcardFile(String fileName) throws IOException {

		FileInputStream in = new FileInputStream(fileName);
		int length = in.available();
		byte[] buffer = new byte[length];
		in.read(buffer);
		in.close();
		return new String(buffer);

	}

	public static String readFile(String fileName, Context mContext) {
		String res = null;
		try {
			FileInputStream fin = null;
			fin = mContext.openFileInput(fileName);
			int length = fin.available();
			byte[] buffer = new byte[length];
			fin.read(buffer);
			res =  new String(buffer);
			fin.close();
		} catch (Exception e) {
			e.printStackTrace();
			new File(fileName);
		}
		return res;

	}

}
