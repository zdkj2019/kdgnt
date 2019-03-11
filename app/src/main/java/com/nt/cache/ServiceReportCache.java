package com.nt.cache;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 服务报告封装类
 * 
 * @author asus
 * 
 */
public class ServiceReportCache implements Serializable {

	private static int index;
	private static Map<String, String> itemmap;
	private static final long serialVersionUID = 1L;
	private static List<Map<String, String>> data;
	private static List<Map<String, Object>> objectdata;

	public static List<Map<String, Object>> getObjectdata() {
		return objectdata;
	}

	public static void setObjectdata(List<Map<String, Object>> objectdata) {
		ServiceReportCache.objectdata = objectdata;
	}

	public static List<Map<String, String>> getData() {
		return data;
	}

	public static void setData(List<Map<String, String>> data) {
		ServiceReportCache.data = data;
	}

	public static int getIndex() {
		return index;
	}

	public static void setIndex(int index) {
		ServiceReportCache.index = index;
	}

	public static Map<String, String> getItemmap() {
		return itemmap;
	}

	public static void setItemmap(Map<String, String> itemmap) {
		ServiceReportCache.itemmap = itemmap;
	}

}
