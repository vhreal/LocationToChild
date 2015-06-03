package com.locationtochild.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

/**
 * 负责与服务器交互的类
 * @author Administrator
 *
 */
public class HttpUtils {
	private static HttpUtils mHttpUtils = null;
	private final static String IP = "115.29.48.16";
	private final static int CONNECTION_TIME_OUT = 5000;//连接超时
	private final static int SO_TIME_OUT = 10000;//请求超时
	private HttpUtils () {}
	
	public synchronized static HttpUtils getInstance(){
		if(mHttpUtils == null)
			mHttpUtils = new HttpUtils();
		return mHttpUtils;
	}
	
	/***
	 * 登录
	 * @param username--登录的手机号
	 * @param password--登录的密码
	 * @return isRight--服务端返回的字符串
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String login(String username, String password, String deviceID) throws ClientProtocolException, IOException{
		HttpClient client = new DefaultHttpClient(setTimeout());
		String url = "http://"+IP+"/index.php/Customer/login";
		HttpPost request = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("phoneid", deviceID));
		request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpResponse response = client.execute(request);
		String result = convertStreamToString(response.getEntity().getContent());
		return result;
	}
	
	/***
	 * 注册用户
	 * @param username-手机号
	 * @param password-密码
	 * @param email-邮件
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String register(String username, String password, String email) throws ClientProtocolException, IOException{
		HttpClient client = new DefaultHttpClient(setTimeout());
		String url = "http://"+IP+"/index.php/Customer/register";
		HttpPost request = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("email", email));
		request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpResponse response = client.execute(request);
		String result = convertStreamToString(response.getEntity().getContent());
		return result;
	}
	
	/***
	 * 上传与账号绑定的手表号码，并从服务器查询手表的设置信息
	 * @param username 登录名
	 * @param watchphone 手表号码
	 * @return result 结果字符串
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String postWatchPhone(String username, String watchphone) throws ClientProtocolException, IOException{
		HttpClient client = new DefaultHttpClient(setTimeout());
		String url = "http://"+IP+"/index.php/Watch/bindWatchPhone";
		HttpPost request = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("watchphone", watchphone));
		request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpResponse response = client.execute(request);
		String result = convertStreamToString(response.getEntity().getContent());
		return result;
	}
	
	/***
	 * 上传登录用户设置的亲情号
	 * @param watchphone 手表号码
	 * @param count	亲情号设置的数量
	 * @param qq 亲情号字符串数组
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String postQQNumber(String watchphone, String count, String[] qq) throws ClientProtocolException, IOException{
		HttpClient client = new DefaultHttpClient(setTimeout());
		String url = "http://"+IP+"/index.php/Watch/bindFamily";
		HttpPost request = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("watchphone", watchphone));
		params.add(new BasicNameValuePair("count", count));
		params.add(new BasicNameValuePair("qqOne", qq[0]));
		params.add(new BasicNameValuePair("qqTwo", qq[1]));
		params.add(new BasicNameValuePair("qqThree", qq[2]));
		request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpResponse response = client.execute(request);
		String result = convertStreamToString(response.getEntity().getContent());
		return result;
	}
	
	/***
	 * 上传登录用户设置的中心号码
	 * @param watchphone 手表号码
	 * @param centre 中心号码
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String postCentreNumber(String watchphone, String centre) throws ClientProtocolException, IOException{
		HttpClient client = new DefaultHttpClient(setTimeout());
		String url = "http://"+IP+"/index.php/Watch/bindCentre";
		HttpPost request = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("watchphone", watchphone));
		params.add(new BasicNameValuePair("centre", centre));
		request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpResponse response = client.execute(request);
		String result = convertStreamToString(response.getEntity().getContent());
		return result;
	}
	
	/**
	 * 设置连接超时和请求超时
	 * @return HttpParams
	 */
	private HttpParams setTimeout(){
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIME_OUT);
		HttpConnectionParams.setSoTimeout(httpParams, SO_TIME_OUT);
		return httpParams;
	}
	
	/***
	 * 将返回的输入流转换为字符串
	 * @param is--需要转换的输入流
	 * @return resultStr --转换后的字符串
	 */
	private String convertStreamToString(InputStream is){
		StringBuffer sb = new StringBuffer();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String tempStr = "";
		try {
			while((tempStr=br.readLine())!=null){
				sb.append(tempStr);
			}
		} catch (IOException e) {
			// TODO: handle exception
		}
		String resultStr = sb.toString();
		return resultStr;
	}
}
