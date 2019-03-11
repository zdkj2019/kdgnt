package com.nt.utils;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

/**
 * 给TextView的text局部变色
 */
public class TextColor {
	/**
	 * @param str
	 *            文本内容
	 * @param start
	 *            变色的开始
	 * @param ent
	 *            结束
	 * @return
	 */
	public static SpannableStringBuilder makecolor(String str, int start,
			int ent) {
		if (str != null) {
			SpannableStringBuilder style = new SpannableStringBuilder(str);
			style.setSpan(new ForegroundColorSpan(Color.RED), start, ent,
					Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

			return style;
		}
		return null;
	}

}
