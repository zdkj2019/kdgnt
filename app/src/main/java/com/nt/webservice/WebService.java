package com.nt.webservice;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;

import com.nt.cache.DataCache;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.util.Log;
import android.util.Xml;
import android.util.Xml.Encoding;

public class WebService {

//	/**
//	 * 访问指定的webservice
//	 *
//	 * @param namespace
//	 * @param url
//	 * @param method
//	 * @param param
//	 * @return
//	 */
//	public Object getWebService(String namespace, String url, String method,
//			Map<String, Object> param) {
//		SoapObject request = new SoapObject(namespace, method);
//		Set<String> set = param.keySet();
//		Iterator<String> it = set.iterator();
//		while (it.hasNext()) {
//			String paraName = it.next();
//			Object paravalue = param.get(paraName);
//			request.addProperty(paraName, paravalue);
//		}
//		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
//				SoapSerializationEnvelope.VER11);
//		envelope.dotNet = false;
//		envelope.bodyOut = request;
//		(new MarshalBase64()).register(envelope);
//
//		HttpTransportSE httpse = new HttpTransportSE(url, 60000);
//		httpse.debug = true;
//
//		String costTime = "";
//		try {
//			final long StartMillis = System.currentTimeMillis();
//			httpse.call(null, envelope);
//			Object obj = envelope.getResponse();
//			final long endMillis = System.currentTimeMillis();
//			costTime = (endMillis - StartMillis) + "";
//			Log.e("currentTimeMillis", costTime);
//			if (obj instanceof SoapObject) {
//				SoapObject result = (SoapObject) obj;
//				return result;
//			}
//			// 判断obj 是不是 String 的实例
//			if (obj instanceof String) {
//				String result = (String) obj;
//				result = result.replace("{\"flag\":", "{\"costTime\":"
//						+ costTime + ",\"flag\":");
//				return result;
//			}
//			// 判断obj 是不是 Vector 的实例
//			if (obj instanceof Vector) {
//				Vector<Object> vector = (Vector<Object>) obj;
//				Object object = vector.get(0);
//				return object;
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			return "";
//		}
//		return null;
//	}

	public static Object getWebService(String namespace, String url,
			String method, Map<String, Object> params) {
		String soapRequestData = buildRequestData(method, namespace, params);

		String s = "";
		try {
			String str = invoke(soapRequestData, url, method);
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputStream iStream = new ByteArrayInputStream(str.getBytes());
			Document doc = builder.parse(iStream);
			Element root = doc.getDocumentElement();
			NodeList nodes = root.getElementsByTagName(method + "Return");
			if (nodes != null) {
				for (int i = 0; i < nodes.getLength(); i++) {
					Node ndes = nodes.item(i);
					s = ndes.getFirstChild().getNodeValue();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	private static String invoke(String soapRequestData, String url,
			String method) throws Exception {
		PostMethod postMethod = new PostMethod(url);
		// 设置POST方法请求超时
		postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 30000);
		Header header = new Header("SOAPAction", "http://tempurl.org/" + method);
		postMethod.setRequestHeader(header);
		try {
			byte[] b = soapRequestData.getBytes("utf-8");
			InputStream inputStream = new ByteArrayInputStream(b, 0, b.length);
			RequestEntity re = new InputStreamRequestEntity(inputStream,
					b.length, "text/xml; charset=utf-8");
			postMethod.setRequestEntity(re);
			HttpClient httpClient = new HttpClient();
			HttpConnectionManagerParams managerParams = httpClient
					.getHttpConnectionManager().getParams();
			// 设置连接超时时间(单位毫秒)
			managerParams.setConnectionTimeout(30000);
			// 设置读数据超时时间(单位毫秒)
			managerParams.setSoTimeout(100000);
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode != HttpStatus.SC_OK) {
				throw new IllegalStateException("调用webservice错误 : "
						+ postMethod.getStatusLine());
			}
			InputStream txtis = postMethod.getResponseBodyAsStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(txtis));
			String tempbf;
			StringBuffer html = new StringBuffer(100);
			while ((tempbf = br.readLine()) != null) {
				html.append(tempbf);
			}
			txtis.close();
			inputStream.close();
			return html.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		} catch (HttpException e) {
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		} finally {
			postMethod.releaseConnection();
		}
	}

	private static String buildRequestData(String methodName, String namespace,
			Map<String, Object> patameterMap) {
		StringBuffer soapRequestData = new StringBuffer();
		soapRequestData.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		soapRequestData
				.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\""
						+ " xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\""
						+ " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
		soapRequestData.append("<soapenv:Body>");
		soapRequestData
				.append("<ns1:"
						+ methodName
						+ " soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:ns1=\""
						+ namespace + "\">");
		Set<String> nameSet = patameterMap.keySet();
		for (String name : nameSet) {
			if ("uf_json_setdata2_p11".equals(methodName)) {
				if ("in4".equals(name)) {
					String s = Base64.encodeToString(DataCache.getinition()
							.getImageByte(), Base64.DEFAULT);
					DataCache.getinition().setImageByte(null);
					soapRequestData.append("<" + name
							+ " xsi:type='xsd:base64Binary'>" + s + "</" + name
							+ ">");
				} else {
					soapRequestData.append("<" + name
							+ " xsi:type='xsd:string'>"
							+ patameterMap.get(name) + "</" + name + ">");
				}
			} else if ("uf_json_setdata2_p1".equals(methodName)) {
				if ("in2".equals(name)) {
					String s = Base64.encodeToString(DataCache.getinition()
							.getImageByte(), Base64.DEFAULT);
					DataCache.getinition().setImageByte(null);
					soapRequestData.append("<" + name
							+ " xsi:type='xsd:base64Binary'>" + s + "</" + name
							+ ">");
				} else {
					soapRequestData.append("<" + name
							+ " xsi:type='xsd:string'>"
							+ patameterMap.get(name) + "</" + name + ">");
				}
			} else {
				soapRequestData.append("<" + name + " xsi:type='xsd:string'>"
						+ patameterMap.get(name) + "</" + name + ">");
			}

		}
		soapRequestData.append("</ns1:" + methodName + ">");
		soapRequestData.append("</soapenv:Body>");
		soapRequestData.append("</soapenv:Envelope>");
		return soapRequestData.toString();
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
}
