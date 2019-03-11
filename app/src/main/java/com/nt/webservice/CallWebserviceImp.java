package com.nt.webservice;

import it.sauronsoftware.base64.Base64;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import com.nt.cache.DataCache;
import com.nt.utils.DataUtil;

import android.content.Context;

public class CallWebserviceImp {

	private String trancode;
	private String cashworkno;
	private String version;
	private String mac;

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	private String timestamp = new DataUtil()
			.toDataString("yyyy-MM-dd HH:mm:ss.S");
	private int seq = new DataUtil().toInt();

	public int getSeq() {
		return seq;
	}

	public String getCashworkno() {
		return cashworkno;
	}

	public void setCashworkno(String cashworkno) {
		this.cashworkno = cashworkno;
	}

	public String getBosscode() {
		return bosscode;
	}

	public void setBosscode(String bosscode) {
		this.bosscode = bosscode;
	}

	private String bosscode;

	public String getTrancode() {
		return trancode;
	}

	public void setTrancode(String trancode) {
		this.trancode = trancode;
	}

	private String delInitTime(String s){
		if(!"".equals(s)){
			s = s.replace("1900-01-01 00:00:00", "");
		}
		return s;
	}

	// 访问正德的服务器
	public synchronized JSONObject getWebServerInfo(String astrsql, String astrparms,
			String methodName,Context mContext) throws Exception {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("in0", DataUtil.encode(astrparms));
		param.put("in1", DataUtil.encode(astrsql));
		String jsonData = (String) new WsClient().loginTest(param, methodName, "ZD",mContext);
		System.out.println("json:"+jsonData);
		jsonData = delInitTime(jsonData);
		JSONObject jsonObject = new JSONObject(jsonData);
		return jsonObject;
	}
	
	public synchronized JSONObject getWebServerInfoBlob(String astrsql, String astrparms,
			String methodName,Context mContext) throws Exception {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("in0", DataUtil.encode(astrparms));
		param.put("in1", DataUtil.encode(astrsql));
		String jsonData = (String) new WsClient().loginTest(param, methodName, "ZD",mContext);
		System.out.println("json:"+jsonData);
		jsonData = delInitTime(jsonData);
		jsonData = Base64.decode(jsonData, "gbk");
		JSONObject jsonObject = new JSONObject(jsonData);
		return jsonObject;
	}

	public JSONObject getWebServerInfo(String astrczbs, String astrparms,
			String astrczry, String methodName,Context mContext) throws Exception {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("in0", DataUtil.encode(astrczbs));
		param.put("in1", DataUtil.encode(astrparms));
		param.put("in2", DataUtil.encode(astrczry));
		String jsonData = (String) new WsClient().loginTest(param, methodName, "ZD", mContext);
		jsonData = delInitTime(jsonData);
		JSONObject jsonObject = new JSONObject(jsonData);
		return jsonObject;
	}

	public JSONObject getWebServerInfo(String astrczbs, String astrparms,
			String astrczry, String astrpk, String methodName,Context mContext) throws Exception {
		System.out.println("传递的参数为" + astrparms);
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("in0", DataUtil.encode(astrczbs));
		param.put("in1", DataUtil.encode(astrparms));
		param.put("in2", DataUtil.encode(astrczry));
		param.put("in3", DataUtil.encode(astrpk));
		String jsonData = (String) new WsClient().loginTest(param, methodName, "ZD", mContext);
		jsonData = delInitTime(jsonData);
		JSONObject jsonObject = new JSONObject(jsonData);	
		return jsonObject;
	}
	
	public JSONObject getWebServerInfo_web(String astrczbs, String astrparms,
			String astrczry, String astrpk, String methodName,Context mContext) throws Exception {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("in0", DataUtil.encode(astrczbs));
		param.put("in1", DataUtil.encode(astrparms));
		param.put("in2", DataUtil.encode(astrczry));
		param.put("in3", DataUtil.encode(astrpk));
		String jsonData = (String) new WsClient().loginTest(param, methodName,
				"ZD",mContext);
		JSONObject jsonObject = new JSONObject(jsonData);
		return jsonObject;
	}
	
	/**
	 * 
	 * 上传图片
	 * @param astrczbs
	 * @param astrparms
	 * @param astrczry
	 * @param astrpk
	 * @param data1
	 * @param methodName
	 * @param mContext
	 * @return
	 * @throws Exception
	 */
	public JSONObject getWebServerInfo2_pic(String astrczbs,
			String astrparms, String astrczry, String astrpk, byte[] data1,
			String methodName,Context mContext) throws Exception {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("in0", DataUtil.encode(astrczbs));
		param.put("in1", DataUtil.encode(astrparms));
		param.put("in3", DataUtil.encode(astrczry));
		param.put("in2", DataUtil.encode(astrpk));
		param.put("in4", data1);
		DataCache.getinition().setImageByte(data1);
		String jsonData = (String) new WsClient().loginTest(param, methodName, "ZD",mContext);
		System.out.println("result:"+jsonData);
		JSONObject jsonObject = new JSONObject(jsonData);
		return jsonObject;
	}
	
	public JSONObject getWebServerInfo2(String astrczbs ,String astrpk ,byte[] data1, String methodName,Context mContext)
			throws Exception {
		HashMap<String, Object> param = new LinkedHashMap<String, Object>();
		param.put("in0", astrczbs);
		param.put("in1", astrpk);
		param.put("in2", data1);
		DataCache.getinition().setImageByte(data1);
		String jsonData = (String) new WsClient().loginTest(param, methodName, "ZD",mContext);
		String str = "";
		if("1".equals(jsonData)){
			str = "{flag:1}";
		}else{
			str = "{flag:0}";
		}
		JSONObject jsonObject = new JSONObject(str);
		return jsonObject;
	}

}
