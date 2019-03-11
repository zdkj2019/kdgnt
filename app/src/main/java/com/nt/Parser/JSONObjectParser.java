package com.nt.Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class JSONObjectParser {

	private Context mContext;

	public JSONObjectParser(Context mContext) {
		this.mContext = mContext;
	}

	public static List<Map<String, String>> jsonToList(JSONArray json)
			throws JSONException {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (int i = 0; i < json.length(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			String data = (String) json.get(i);
			JSONObject object = new JSONObject(data);
			Iterator<String> it = object.keys();
			while (it.hasNext()) {
				String key = it.next();
				String value = object.getString(key);
				map.put(key, value);
			}
			list.add(map);
		}
		return list;
	}

	public static List<Map<String, String>> jsonToListByJson(JSONArray json)
			throws JSONException {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (int i = 0; i < json.length(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			JSONObject object = (JSONObject) json.get(i);
			Iterator<String> it = object.keys();
			while (it.hasNext()) {
				String key = it.next();
				String value = object.getString(key);
				map.put(key, value);
			}
			list.add(map);
		}
		return list;
	}

	public static Map<String, String> jsonToMap(JSONObject json)
			throws JSONException {
		Map<String, String> map = new HashMap<String, String>();
		JSONObject object = json;
		Iterator<String> it = object.keys();
		while (it.hasNext()) {
			String key = it.next();
			String value = object.getString(key);
			map.put(key, value);
		}
		return map;
	}

	/**
	 * 小区jsonObject解析 成ArrayList<Map<String, String>>
	 * 
	 * @param jsonObject
	 *            jsonObject
	 * @return ArrayList<Map<String, String>>
	 * @throws JSONException
	 * @throws Exception
	 */
	public ArrayList<Map<String, String>> xqParser(JSONObject jsonObject)
			throws JSONException {

		ArrayList<Map<String, String>> data_xq = new ArrayList<Map<String, String>>();

		JSONArray jsonArray2 = jsonObject.getJSONArray("tableA");

		Log.e("dd", jsonObject.toString());
		String flag2 = jsonObject.getString("flag");
		if (Integer.parseInt(flag2) > 0) {

			Map<String, String> item = new HashMap<String, String>();
			// {"main_jcsj_khjgb_khjgbm":"00000002","main_jcsj_dqb_dqbm":"01003001","main_jcsj_khjgb_khjgmc":"片区2","main_jcsj_dqb_dqmc":"成都市"}
			for (int i = 0; i < jsonArray2.length(); i++) {
				JSONObject temp = jsonArray2.getJSONObject(i);
				item = new HashMap<String, String>();

				item.put("id", temp.getString("khjgbm"));
				item.put("name", temp.getString("khjgmc"));
				item.put("qybm", temp.getString("dqbm"));
				item.put("qymc", temp.getString("dqmc"));

				data_xq.add(item);
			}

			return data_xq;
		}

		return null;

	}

	public ArrayList<Map<String, String>> FBFParser(JSONObject jsonObject)
			throws JSONException {
		ArrayList<Map<String, String>> data_FBF = new ArrayList<Map<String, String>>();

		JSONArray jsonArray2 = jsonObject.getJSONArray("tableA");

		Log.e("dd", jsonObject.toString());
		String flag2 = jsonObject.getString("flag");
		if (Integer.parseInt(flag2) > 0) {

			Map<String, String> item = new HashMap<String, String>();
			// {"zzjgb_pz_zzjgmc":"重庆百货","zzjgb_pz_zzjgbm":"00000002"}
			for (int i = 0; i < jsonArray2.length(); i++) {
				JSONObject temp = jsonArray2.getJSONObject(i);
				item = new HashMap<String, String>();

				item.put("id", temp.getString("zzjgb_pz_zzjgbm"));
				item.put("name", temp.getString("zzjgb_pz_zzjgmc"));
				item.put("hplbmc", temp.getString("hpgl_jcsj_hplbb_hplbmc"));
				item.put("hplbbm", temp.getString("hpgl_jcsj_hplbb_hplbbm"));

				data_FBF.add(item);
			}

			return data_FBF;
		}

		return null;
	}

	public ArrayList<Map<String, String>> PjlxParser(JSONObject jsonObject,
			String type) throws JSONException {
		ArrayList<Map<String, String>> data_FBF = new ArrayList<Map<String, String>>();

		JSONArray jsonArray2 = jsonObject.getJSONArray("tableA");

		Log.e("dd", jsonObject.toString());
		String flag2 = jsonObject.getString("flag");
		if (Integer.parseInt(flag2) > 0) {

			Map<String, String> item = new HashMap<String, String>();
			for (int i = 0; i < jsonArray2.length(); i++) {
				JSONObject temp = jsonArray2.getJSONObject(i);
				item = new HashMap<String, String>();
				if ("xl".equals(type)) {
					item.put("id", temp.getString("hpbm"));
					item.put("name", temp.getString("hpmc"));
					//item.put("type", temp.getString("hplbbm"));
				} else if ("zl".equals(type)) {
					item.put("id", temp.getString("hplbbm"));
					item.put("name", temp.getString("hplbmc"));
					item.put("type", temp.getString("sjlbbm"));
				} else {
					item.put("id", temp.getString("hplbbm"));
					item.put("name", temp.getString("hplbmc"));
					item.put("type", "");
				}
				data_FBF.add(item);
			}

			return data_FBF;
		}

		return null;
	}

	public ArrayList<Map<String, String>> FylxParser(JSONObject jsonObject)
			throws JSONException {
		ArrayList<Map<String, String>> data_FBF = new ArrayList<Map<String, String>>();

		JSONArray jsonArray2 = jsonObject.getJSONArray("tableA");

		Log.e("dd", jsonObject.toString());
		String flag2 = jsonObject.getString("flag");
		if (Integer.parseInt(flag2) > 0) {

			Map<String, String> item = new HashMap<String, String>();
			// {"zzjgb_pz_zzjgmc":"重庆百货","zzjgb_pz_zzjgbm":"00000002"}
			for (int i = 0; i < jsonArray2.length(); i++) {
				JSONObject temp = jsonArray2.getJSONObject(i);
				item = new HashMap<String, String>();

				item.put("mxh", temp.getString("dzpt_htfyxmb_fbf_mxh"));
				item.put("fymc", temp.getString("fymc"));
				item.put("ywlx", temp.getString("dzpt_htfyxmb_fbf_ywlx"));
				item.put("sblx", temp.getString("dzpt_htfyxmb_fbf_sblx"));
				item.put("zbh", temp.getString("dzpt_htfyxmb_fbf_zbh"));
				item.put("dylx", temp.getString("dzpt_htfyxmb_fbf_dylx"));
				item.put("wlsjbm", temp.getString("dzpt_htzb_fbf_wlsjbm"));

				data_FBF.add(item);
			}

			return data_FBF;
		}

		return null;
	}

	public ArrayList<Map<String, String>> KhwdParser(JSONObject jsonObject)
			throws JSONException {
		ArrayList<Map<String, String>> data_FBF = new ArrayList<Map<String, String>>();

		JSONArray jsonArray2 = jsonObject.getJSONArray("tableA");

		Log.e("dd", jsonObject.toString());
		String flag2 = jsonObject.getString("flag");
		if (Integer.parseInt(flag2) > 0) {

			Map<String, String> item = new HashMap<String, String>();
			// {"zzjgb_pz_zzjgmc":"重庆百货","zzjgb_pz_zzjgbm":"00000002"}
			for (int i = 0; i < jsonArray2.length(); i++) {
				JSONObject temp = jsonArray2.getJSONObject(i);
				item = new HashMap<String, String>();

				item.put("email", temp.getString("ccgl_wlsjb_email"));
				item.put("wlsjmc", temp.getString("ccgl_wlsjb_wlsjmc"));
				item.put("wlsjbm", temp.getString("ccgl_wlsjb_wlsjbm"));

				data_FBF.add(item);
			}

			return data_FBF;
		}

		return null;
	}

	public ArrayList<Map<String, String>> kfParser(JSONObject jsonObject_kf) {

		return null;
	}

	public ArrayList<Map<String, String>> cwParser(JSONObject jsonObject_cw) {

		return null;
	}

}
