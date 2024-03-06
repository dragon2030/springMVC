package com.bigDragon.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpClientUtil {
	private RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(15000).setConnectTimeout(15000)
			.setConnectionRequestTimeout(15000).build();
	private static HttpClientUtil instance = null;

	private HttpClientUtil() {
	}
	public static HttpClientUtil getInstance() {
		if (instance == null) {
			instance = new HttpClientUtil();
		}
		return instance;
	}
	/**
	 * 发送 post请求
	 * @param httpUrl 地址
	 * @throws Exception
	 */
	public String sendHttpPost(String httpUrl) throws Exception {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
		return sendHttpPost(httpPost);
	}
	/**
	 * 发送 post请求(x-www-form-urlencoded UTF-8)
	 * @param httpUrl 地址
	 * @param params 参数(格式:key1=value1&key2=value2)
	 * @throws Exception
	 */
	public String sendHttpPost(String httpUrl, String params) throws Exception {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
		// 设置参数
		StringEntity stringEntity = new StringEntity(params, "UTF-8");
		stringEntity.setContentType("application/x-www-form-urlencoded");
		httpPost.setEntity(stringEntity);
		return sendHttpPost(httpPost);
	}
	
	/**
	 * 发送 post请求(x-www-form-urlencoded GBK)
	 * @param httpUrl 地址
	 * @param params 参数(格式:key1=value1&key2=value2)
	 * @throws Exception
	 */
	public String sendHttpPostGBK(String httpUrl, String params) throws Exception {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
		// 设置参数
		StringEntity stringEntity = new StringEntity(params, "GBK");
		stringEntity.setContentType("application/x-www-form-urlencoded");
		httpPost.setEntity(stringEntity);
		return sendHttpPost(httpPost);
	}
	
	/**
	 * 发送 post请求(application/json)
	 * @param httpUrl 地址
	 * @param json 参数
	 * @throws Exception
	 */
	public String sendHttpPostUseJson(String httpUrl, String json) throws Exception {
		HttpPost httpPost = new HttpPost(httpUrl);
		// 设置参数
		StringEntity stringEntity = new StringEntity(json, "UTF-8");
		stringEntity.setContentType("application/json");
		stringEntity.setContentEncoding("UTF-8");
		httpPost.setEntity(stringEntity);
		return sendHttpPost(httpPost);
	}
	/**
	 * 发送 post请求
	 * @param httpUrl 地址
	 * @param maps 参数
	 * @throws Exception
	 */
	public String sendHttpPost(String httpUrl, Map<String, String> maps) throws Exception {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
		// 创建参数队列
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for (String key : maps.keySet()) {
			nameValuePairs.add(new BasicNameValuePair(key, maps.get(key)));
		}
		httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "GBK"));
		return sendHttpPost(httpPost);
	}
	/**
	 * 发送 post请求
	 * @param httpUrl 地址
	 * @param heads 参数
	 * @param maps 参数
	 * @throws Exception
	 */
	public String sendHttpPost(String httpUrl, Map<String, String> maps, Map<String, String> heads) throws Exception {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
		// 创建参数队列
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for (String key : maps.keySet()) {
			nameValuePairs.add(new BasicNameValuePair(key, maps.get(key)));
		}
		for (String key : heads.keySet()) {
			httpPost.setHeader(key, heads.get(key));
		}
		httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
		return sendHttpPost(httpPost);
	}
	/**
	 * 发送 post请求（带文件）
	 * @param httpUrl 地址
	 * @param maps 参数
	 * @param fileLists 附件
	 * @throws Exception
	 */
	public String sendHttpPost(String httpUrl, Map<String, String> maps, List<File> fileLists) throws Exception {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
		MultipartEntityBuilder meBuilder = MultipartEntityBuilder.create();
		for (String key : maps.keySet()) {
			meBuilder.addPart(key, new StringBody(maps.get(key), ContentType.TEXT_PLAIN));
		}
		for (File file : fileLists) {
			FileBody fileBody = new FileBody(file);
			meBuilder.addPart("files", fileBody);
		}
		HttpEntity reqEntity = meBuilder.build();
		httpPost.setEntity(reqEntity);
		return sendHttpPost(httpPost);
	}
	/**
	 * 发送Post请求
	 * @param httpPost
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public String sendHttpPost(HttpPost httpPost) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String responseContent = null;
		try {
			// 创建默认的httpClient实例.
			httpClient = HttpClients.createDefault();
			httpPost.setConfig(requestConfig);
			// 执行请求
			response = httpClient.execute(httpPost);
			entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
			EntityUtils.consume(entity);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭连接,释放资源
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return responseContent;
	}
	/**
	 * 发送 get请求
	 * @param httpUrl
	 */
	public String sendHttpGet (String httpUrl) {
		HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求
		return sendHttpGet(httpGet);
	}
	/**
	 * 发送 get请求Https
	 * @param httpUrl
	 */
	public String sendHttpsGet(String httpUrl) {
		HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求
		return sendHttpsGet(httpGet);
	}
	/**
	 * 发送Get请求
	 * @param httpGet
	 * @return
	 */
	private String sendHttpGet(HttpGet httpGet) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String responseContent = null;
		try {
			// 创建默认的httpClient实例.
			httpClient = HttpClients.createDefault();
			httpGet.setConfig(requestConfig);
			// 执行请求
			response = httpClient.execute(httpGet);
			entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭连接,释放资源
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return responseContent;
	}
	/**
	 * 发送Get请求Https
	 * @param httpGet
	 * @return
	 */
	public String sendHttpsGet(HttpGet httpGet) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String responseContent = null;
		try {
			// 创建默认的httpClient实例.
			PublicSuffixMatcher publicSuffixMatcher = PublicSuffixMatcherLoader
					.load(new URL(httpGet.getURI().toString()));
			DefaultHostnameVerifier hostnameVerifier = new DefaultHostnameVerifier(publicSuffixMatcher);
			httpClient = HttpClients.custom().setSSLHostnameVerifier(hostnameVerifier).build();
			httpGet.setConfig(requestConfig);
			// 执行请求
			response = httpClient.execute(httpGet);
			entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, "gbk");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭连接,释放资源
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return responseContent;
	}
}
