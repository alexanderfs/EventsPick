package com.alexan.findevents.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;

import android.util.Log;

public class NetworkTool {
	/**
	 * 通过GET方式获取网络数据，返回Json字符串
	 * 
	 * @param url
	 *            网址，例如：http://www.baidu.com
	 * @return {"status":"200","mes":"成功","data":""}
	 */
	public String httpGet(String url) {
		StringBuilder stringBuilder = new StringBuilder();// 初始化缓存字符串
		String result = "";// 初始化结果值
		HttpClient client = new DefaultHttpClient();
		client.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT,
				15000); // 超时设置
		client.getParams().setIntParameter(
				HttpConnectionParams.CONNECTION_TIMEOUT, 15000);// 连接超时
		try {
			HttpResponse response = client.execute(new HttpGet(url));
			HttpEntity entity = response.getEntity();
			if (entity != null
					&& response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(entity.getContent(), "UTF-8"),
						1024);
				String line = null;
				while ((line = reader.readLine()) != null) {
					stringBuilder.append(line + "\n");
				}
				reader.close();
				result = stringBuilder.toString().trim();
			} else {
				// 统一封装为Json规定格式
				result = "{\"status\":\"400\",\"mes\":\""
						+ Configure.OTHER_ERROR + "\",\"data\":\"\"" + "}";
			}
		} catch (Exception e) {
			result = "{\"status\":\"500\",\"mes\":\"" + Configure.NETWORK_ERROR
					+ "\",\"data\":\"\"" + "}";
		}
		if (Configure.DEBUG) {
			// 打印调试信息
			Log.d(Configure.TAG, "提交参数：\n" + "url:" + url);
			Log.d(Configure.TAG, "返回结果：\n" + result);
		}
		return result;
	}

	/**
	 * 通过POST方式获取网络数据，返回Json字符串
	 * 
	 * @param url
	 *            网址，例如：http://www.baidu.com
	 * @param params
	 *            上传参数，例如：uid=11&passwd=123
	 * @return {"status":"200","mes":"成功","data":""}
	 */
	public String httpPost(String url, String params) {
		StringBuilder stringBuilder = new StringBuilder();// 初始化缓存字符串
		String result = "";// 初始化结果值
		HttpPost httpRequest = new HttpPost(url);
		httpRequest.setHeader("Content-Type",
				"application/x-www-form-urlencoded");
		HttpClient client = new DefaultHttpClient();
		client.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT,
				15000); // 超时设置
		client.getParams().setIntParameter(
				HttpConnectionParams.CONNECTION_TIMEOUT, 15000);// 连接超时
		try {
			httpRequest.setEntity(new StringEntity(params, HTTP.UTF_8));
			HttpResponse response = client.execute(httpRequest);
			HttpEntity entity = response.getEntity();
			if (entity != null
					&& response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(entity.getContent(), "UTF-8"),
						8192);
				String line = null;
				while ((line = reader.readLine()) != null) {
					stringBuilder.append(line + "\n");
				}
				reader.close();
				result = stringBuilder.toString().trim();
			} else {
				result = "{\"status\":\"400\",\"mes\":\""
						+ Configure.OTHER_ERROR + "\",\"data\":\"\"" + "}";
			}

		} catch (Exception e) {
			// 统一封装为Json规定格式
			result = "{\"status\":\"500\",\"mes\":\"" + Configure.NETWORK_ERROR
					+ "\",\"data\":\"\"" + "}";
		}
		if (Configure.DEBUG) {
			// 打印调试信息
			Log.d(Configure.TAG, "提交参数：\n" + "url:" + url + "\nparams:"
					+ params);
			Log.d(Configure.TAG, "返回结果：\n" + result);
		}
		return result;
	}

	/**
	 * 通过表单上传文件和数据，返回Json字符串
	 * 
	 * @param urlString
	 *            文件上传地址
	 * @param params
	 *            非文件参数
	 * @param name
	 *            服务器文件接收的键
	 * @param filename
	 *            本地文件的位置
	 * @param filetype
	 *            上传文件的类型，例如：image/jpeg
	 * @return {"status":"200","mes":"成功","data":""}
	 */
	public String formPost(String urlString, Map<String, Object> params,
			String name, String filename, String filetype) {
		String result = "";
		String uploadUrl = "";
		String end = "\r\n";
		String MULTIPART_FORM_DATA = "multipart/form-data";
		String BOUNDARY = "-----------------------------7dc3b51c907e4"; // 数据分隔线
		String fileuri = "";
		if (!filename.equals("")) {
			fileuri = fileuri + filename;// 获得文件位置
		}

		if (!urlString.equals("")) {
			uploadUrl = uploadUrl + urlString;// 文件上传地址

			try {
				URL url = new URL(uploadUrl);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setDoInput(true);// 允许输入
				conn.setDoOutput(true);// 允许输出
				conn.setUseCaches(false);// 不使用Cache
				conn.setConnectTimeout(15000);// 15秒钟连接超时
				conn.setReadTimeout(15000);// 15秒钟读数据超时
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("Charset", "UTF-8");
				conn.setRequestProperty("Content-Type", MULTIPART_FORM_DATA
						+ "; boundary=" + BOUNDARY);

				StringBuilder sb = new StringBuilder();

				// 上传的表单参数部分
				for (Map.Entry<String, Object> entry : params.entrySet()) {// 构建表单字段内容
					sb.append("--");
					sb.append(BOUNDARY);
					sb.append("\r\n");
					sb.append("Content-Disposition: form-data; name=\""
							+ entry.getKey() + "\"\r\n\r\n");
					sb.append(entry.getValue());
					sb.append("\r\n");
				}

				sb.append("--");
				sb.append(BOUNDARY);
				sb.append("\r\n");

				DataOutputStream dos = new DataOutputStream(
						conn.getOutputStream());
				dos.write(sb.toString().getBytes());

				if (!filename.equals("") && !filename.equals(null)) {
					dos.writeBytes("Content-Disposition: form-data; name=\""
							+ name + "\"; filename=\"" + fileuri + "\""
							+ "\r\n" + "Content-Type:" + filetype + "\r\n\r\n");
					FileInputStream fis = new FileInputStream(filename);
					byte[] buffer = new byte[1024]; // 8k
					int count = 0;
					while ((count = fis.read(buffer)) != -1) {
						dos.write(buffer, 0, count);
					}
					dos.writeBytes(end);
					fis.close();
				}
				dos.writeBytes("--" + BOUNDARY + "--\r\n");
				dos.flush();
				InputStream is = conn.getInputStream();
				int ch;
				StringBuffer sbf = new StringBuffer();
				while ((ch = is.read()) != -1) {
					sbf.append((char) ch);
				}
				result = sbf.toString().trim();
			} catch (IOException e) {
				// 统一封装为Json规定格式
				result = "{\"status\":\"400\",\"mes\":\""
						+ Configure.OTHER_ERROR + "\",\"data\":\"\"" + "}";
			} catch (Exception e) {
				result = "{\"status\":\"500\",\"mes\":\""
						+ Configure.NETWORK_ERROR + "\",\"data\":\"\"" + "}";
			}
		}
		if (Configure.DEBUG) {
			// 打印调试信息
			Log.d(Configure.TAG, "提交参数：\n" + "urlString:" + urlString
					+ "\nname:" + name + "\nfilename:" + filename
					+ "\nfiletype:" + filetype);
			Log.d(Configure.TAG, "返回结果：\n" + result);
		}
		return result;
	}
}
