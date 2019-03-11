package com.nt.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@SuppressLint("SimpleDateFormat")
public class DataUtil {
	private Random random = new Random();
	private String str = "";

	/**
	 * 格式化系统时间
	 * 
	 * @param format
	 * @return
	 */
	public static String toDataString(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date now = new Date();
		return sdf.format(now);
	}

	/**
	 * 格式化系统时间
	 * 
	 * @param format
	 * @return
	 */
	public static String toDataString(String time, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(time);
	}

	/**
	 * 时间反格式化
	 * 
	 * @param s
	 * @return
	 */
	public static Date StringToDate(String s) {
		Date time = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			time = df.parse(s);

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return time;
	}

	/**
	 * 把传输的数据转换成16进制
	 * 
	 * @param str
	 *            需要转换的字符串
	 * @return
	 * @throws Exception
	 */
	public static String encode(String str) throws Exception {
		String hexString = "0123456789ABCDEF";
		// 根据默认编码获取字节数组
		byte[] bytes = null;
		bytes = str.getBytes("GBK");//
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		// 将字节数组中每个字节拆解成2位16进制整数
		for (int i = 0; i < bytes.length; i++) {
			sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
			sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
		}
		String s = sb.toString();
		return s;
	}

	/**
	 * 获取流水号
	 * 
	 * @return
	 */
	public int toInt() {
		String s = toDataString("HH:mm:ss.S");
		String temp = "";

		switch (s.length()) {
		case 8:
			s = s + random.nextInt(9) + random.nextInt(9) + random.nextInt(9);
			break;

		case 9:
			s = s + random.nextInt(9) + random.nextInt(9);
			break;
		case 10:
			s = s + random.nextInt(9);
			break;
		}

		for (int i = 0; i < 12; i = i + 3) {
			temp = s.substring(i, i + 2);
			str = str + temp;
		}
		int i = Integer.parseInt(str);
		return i;
	}

	/**
	 * 把金额进行元、角、分的转换
	 * 
	 * @param recfee
	 * @return
	 */
	public String changeRecfee(String recfee) {
		int temp = Integer.parseInt(recfee);
		int y = temp / 100;
		int j = (temp % 100) / 10;
		int f = (temp % 100) % 10;
		return "" + y + "." + j + "" + f + " 元";
	}

	/**
	 * 生成随机验证码或者是二次密码
	 * 
	 * @return
	 */
	public String generateIdentifyingCode() {
		String identifyingcode = "";
		for (int i = 0; i < 6; i++) {
			int j = random.nextInt(9);
			identifyingcode = identifyingcode + j;
		}

		return identifyingcode;
	}
}
