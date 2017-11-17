package com.ibm.kstar.exchange.pdm;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.xsnake.web.utils.PropertiesUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ServiceUtil {
	
	public static final String charset = "utf-8";
	private static final String hType = "text/plain;charset=utf-8";
	private static final String cType = "application/json";
	private static final String algorithm = "AES";
	private static final String sKey = "kstarkstarsinocc";
	private static final String AESPara = "AES/ECB/PKCS5Padding";
	private static final String tuKey = "fetchtoken";
	
	private ServiceUtil() {
	}
	
	public static ResultBean callService(String uri, String jsonParam) {
		String jsonStr = null;
		
		HttpClientBuilder hcb = HttpClients.custom();
		CloseableHttpClient chc = hcb.build();
		
		HttpPost httpPost = new HttpPost(uri);
		httpPost.setHeader("Content-type", hType);
		System.out.println("request URI: " + httpPost.getURI());

		if (null != jsonParam) {
			StringEntity se = new StringEntity(jsonParam,charset);
			System.out.println("request parameters: " + jsonParam);
			se.setContentEncoding(charset);
			se.setContentType(cType);  
			httpPost.setEntity(se);
		}
		
		CloseableHttpResponse response = null;
		try {
			response = chc.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (null != entity) {
				jsonStr = EntityUtils.toString(entity);
				System.out.println("response: " + jsonStr);
			}
			EntityUtils.consume(entity);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
				chc.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return (ResultBean) jsonString2Object(jsonStr, ResultBean.class);
	}

	private static Object jsonString2Object(String jsStr, Class<?> clazz) {
		JSONObject jso = JSONObject.fromObject(jsStr);
		return JSONObject.toBean(jso, clazz);
	}

	public static String object2JsonString(Object obj) {
		return JSONObject.fromObject(obj).toString();
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> parseJSON2Map(String jsStr) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject json = JSONObject.fromObject(jsStr);
		for (Object k : json.keySet()) {
			Object v = json.get(k);
			if (v instanceof JSONArray) {
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				Iterator<JSONObject> it = ((JSONArray) v).iterator();
				while (it.hasNext()) {
					JSONObject json2 = it.next();
					list.add(parseJSON2Map(json2.toString()));
				}
				map.put(k.toString(), list);
			} else {
				map.put(k.toString(), v);
			}
		}
		return map;
	}
	
	public static ResultBean getToken() throws Exception {
		String tokenURI = new StringBuilder().append(PropertiesUtil.getStringByKey(tuKey)).append("&entcode=kstar")
				.append("&authheader=").append(getAuthHeader()).toString();
		return callService(tokenURI, null);
	}
	
	public static String encodeStr(String str){
		String result = null;
		try {
			result = URLEncoder.encode(str, ServiceUtil.charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private static String getAuthHeader() throws Exception {
		String authheader = null;
		String sSrc = new StringBuilder("{\"userid\":\"crmuser\",\"time\":")
				.append(String.valueOf(System.currentTimeMillis())).append("}").toString();
		try {
			byte[] raw = sKey.getBytes(charset);
			SecretKeySpec skeySpec = new SecretKeySpec(raw, algorithm);
			Cipher cipher = Cipher.getInstance(AESPara);
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			byte[] encrypted = cipher.doFinal(sSrc.getBytes(charset));
			authheader = new Base64().encodeToString(encrypted);
			authheader = URLEncoder.encode(authheader, charset);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		return authheader;
	}
}
