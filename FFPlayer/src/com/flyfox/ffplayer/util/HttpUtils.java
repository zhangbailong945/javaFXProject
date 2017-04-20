package com.flyfox.ffplayer.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

public class HttpUtils {

	private static final HttpClient httpClient = HttpClients.createDefault();
	private static final String ENCODE = "UTF-8";

	/**
	 * get获取
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String get(String url) throws IOException {
		return get(url, null, ENCODE);
	}

	/**
	 * post获取
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String post(String url) throws IOException {
		return post(url, null, ENCODE);
	}

	/**
	 * 以Post方法访问
	 * 
	 * @param url
	 *            请求地址
	 * @return String
	 * @throws IOException
	 */
	public static String post(String url, Map<String, String> argsMap) throws IOException {
		return post(url, argsMap, ENCODE);
	}

	/**
	 * 以Post方法访问
	 * 
	 * @param url
	 *            请求地址
	 * @param argsMap
	 *            携带的参数
	 * @return String 返回结果
	 * @throws Exception
	 */
	public static String post(String url, Map<String, String> argsMap, String charsetName) throws IOException {
		byte[] dataByte = null;
		HttpPost httpPost = new HttpPost(url);
		// 设置参数
		if (argsMap != null) {
			UrlEncodedFormEntity encodedFormEntity = new UrlEncodedFormEntity(setHttpParams(argsMap), ENCODE);
			httpPost.setEntity(encodedFormEntity);
		}

		// 执行请求
		HttpResponse httpResponse = httpClient.execute(httpPost);
		// 获取返回的数据
		HttpEntity httpEntity = httpResponse.getEntity();
		if (httpEntity != null && httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			byte[] responseBytes = getData(httpEntity);
			dataByte = responseBytes;
			httpPost.abort();
		}
		// 将字节数组转换成为字符串
		String result = bytesToString(dataByte, charsetName);
		return result;
	}

	/**
	 * 以Get方法访问
	 * 
	 * @param url
	 *            请求地址
	 * @return String
	 * @throws IOException
	 */
	public static String get(String url, Map<String, String> argsMap) throws IOException {
		return get(url, argsMap, ENCODE);
	}

	/**
	 * 以Get方法访问
	 * 
	 * @param url
	 *            请求地址
	 * @param argsMap
	 * @param charsetName
	 * @return
	 * @throws IOException
	 */
	public static String get(String url, Map<String, String> argsMap, String charsetName) throws IOException {
		byte[] dataByte = null;
		// 为GET请求链接构造参数
		if (argsMap != null) {
			url = formatGetParameter(url, argsMap);
		}

		HttpGet httpGet = new HttpGet(url);
		
		HttpResponse httpResponse = httpClient.execute(httpGet);
		HttpEntity httpEntity = httpResponse.getEntity();
		if (httpEntity != null && httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			byte[] responseBytes = getData(httpEntity);
			dataByte = responseBytes;
			httpGet.abort();
		}
		// 将字节数组转换成为字符串
		String result = bytesToString(dataByte, charsetName);
		return result;
	}

	/**
	 * 构造GET请求地址的参数拼接
	 * 
	 * @param url
	 * @param argsMap
	 * @return String
	 */
	private static String formatGetParameter(String url, Map<String, String> argsMap) {
		if (url != null && url.length() > 0) {
			if (!url.endsWith("?")) {
				url = url + "?";
			}

			if (argsMap != null && !argsMap.isEmpty()) {
				Set<Entry<String, String>> entrySet = argsMap.entrySet();
				Iterator<Entry<String, String>> iterator = entrySet.iterator();
				while (iterator.hasNext()) {
					Entry<String, String> entry = iterator.next();
					if (entry != null) {
						String key = entry.getKey();
						String value = entry.getValue();
						url = url + key + "=" + value;
						if (iterator.hasNext()) {
							url = url + "&";
						}
					}
				}
			}
		}
		return url;
	}

	/**
	 * 获取数据
	 * 
	 * @param httpEntity
	 * @return
	 * @throws Exception
	 */
	private static byte[] getData(HttpEntity httpEntity) throws IOException {
		BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(httpEntity);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		bufferedHttpEntity.writeTo(byteArrayOutputStream);
		byte[] responseBytes = byteArrayOutputStream.toByteArray();
		return responseBytes;
	}

	/**
	 * 设置HttpPost请求参数
	 * 
	 * @param argsMap
	 * @return BasicHttpParams
	 */
	private static List<BasicNameValuePair> setHttpParams(Map<String, String> argsMap) {
		List<BasicNameValuePair> nameValuePairList = new ArrayList<BasicNameValuePair>();
		// 设置请求参数
		if (argsMap != null && !argsMap.isEmpty()) {
			Set<Entry<String, String>> set = argsMap.entrySet();
			Iterator<Entry<String, String>> iterator = set.iterator();
			while (iterator.hasNext()) {
				Entry<String, String> entry = iterator.next();
				BasicNameValuePair basicNameValuePair = new BasicNameValuePair(entry.getKey(), entry.getValue()
						.toString());
				nameValuePairList.add(basicNameValuePair);
			}
		}
		return nameValuePairList;
	}

	/**
	 * 将字节数组转换成字符串
	 * 
	 * @param bytes
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static String bytesToString(byte[] bytes, String charsetName) throws UnsupportedEncodingException {
		if (bytes != null) {
			String returnStr = new String(bytes, charsetName);
			return returnStr;
		}
		return null;
	}
}
