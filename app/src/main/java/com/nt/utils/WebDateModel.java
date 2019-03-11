package com.nt.utils;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

/**
 * Spinner的simpleadapter 封装
 * 
 * @author asus
 * 
 */
public class WebDateModel {
	// 入
	private JSONObject jsonObject;
	private String map_id;
	private String map_name;
	private String form_id;
	private String sjbm;
	/**
	 * 类别
	 */
	private String form_sbzl;
	/**
	 * 营业厅经度
	 */
	private String form_xxx;
	/**
	 * 营业厅纬度
	 */
	private String form_yyy;
	private String form_name;
	// 出
	private List<Map<String, String>> date;// 入也有
	private String[] from;
	private int[] to;

	public String getSjbm() {
		return sjbm;
	}

	public void setSjbm(String sjbm) {
		this.sjbm = sjbm;
	}

	public String getForm_xxx() {
		return form_xxx;
	}

	public void setForm_xxx(String form_xxx) {
		this.form_xxx = form_xxx;
	}

	public String getForm_yyy() {
		return form_yyy;
	}

	public void setForm_yyy(String form_yyy) {
		this.form_yyy = form_yyy;
	}

	public String getForm_sbzl() {
		return form_sbzl;
	}

	public void setForm_sbzl(String form_sbzl) {
		this.form_sbzl = form_sbzl;
	}

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public String getMap_id() {
		return map_id;
	}

	public void setMap_id(String map_id) {
		this.map_id = map_id;
	}

	public String getMap_name() {
		return map_name;
	}

	public void setMap_name(String map_name) {
		this.map_name = map_name;
	}

	public String getForm_id() {
		return form_id;
	}

	public void setForm_id(String form_id) {
		this.form_id = form_id;
	}

	public String getForm_name() {
		return form_name;
	}

	public void setForm_name(String form_name) {
		this.form_name = form_name;
	}

	public List<Map<String, String>> getDate() {
		return date;
	}

	public void setDate(List<Map<String, String>> date) {
		this.date = date;
	}

	public String[] getFrom() {
		return from;
	}

	public void setFrom(String[] from) {
		this.from = from;
	}

	public int[] getTo() {
		return to;
	}

	public void setTo(int[] to) {
		this.to = to;
	}

}
