package com.nt.utils;

import java.util.ArrayList;

/**
 * activity管理工具类，提供登记，全部关闭功能
 * 
 * @author Cheng
 * 
 */

public class DateManager {
	private static ArrayList<WebDateModel> webDateModelStack;
	private static DateManager instance;

	private DateManager() {
	}

	public static DateManager getDateManager() {
		if (instance == null) {
			instance = new DateManager();
		}
		return instance;
	}

	// 退出栈顶Activity
	public void popWebDateModel(WebDateModel wdm) {
		if (wdm != null) {
			// 在从自定义集合中取出当前Activity时，也进行了Activity的关闭操作
			// activity.finish();
			webDateModelStack.remove(wdm);
			wdm = null;
		}
	}

	// 获得当前栈顶serviceReportCache
	public WebDateModel currentWebDateModel() {
		WebDateModel wmd = null;
		if (!webDateModelStack.isEmpty())
			wmd = webDateModelStack.get(webDateModelStack.size() - 1);
		return wmd;
	}

	// 将当前serviceReportCache推入栈中
	public void pushWebDateModel(WebDateModel wmd) {
		if (webDateModelStack == null) {
			webDateModelStack = new ArrayList<WebDateModel>();
		}
		webDateModelStack.add(wmd);
	}

	// 退出栈中除指定所有Activity
	public void popAllWebDateModelExceptOne(Class<?> cls) {
		while (true) {
			WebDateModel wdm = currentWebDateModel();
			if (wdm == null) {
				break;
			}
			if (wdm.getClass().equals(cls)) {
				break;
			}
			popWebDateModel(wdm);
		}
	}

	// 退出栈中所有Activity
	public void popAllWebDateModel() {

		while (webDateModelStack.size() > 0) {
			WebDateModel wdm = currentWebDateModel();
			if (wdm != null) {
				popWebDateModel(wdm);
			}

		}
	}
}