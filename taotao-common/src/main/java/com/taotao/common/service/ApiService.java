package com.taotao.common.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import com.taotao.common.httpclient.HttpResult;



@Service
public class ApiService implements BeanFactoryAware {
	
	private BeanFactory beanFactory;
	@Autowired(required=false)
	private RequestConfig requestConfig;

	/**
	 * httpclient的get请求
	 * 
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String doGet(String url) throws ClientProtocolException, IOException {
		// 创建http GET请求
		HttpGet httpGet = new HttpGet(url);
		httpGet.setConfig(requestConfig);
		CloseableHttpResponse response = null;
		try {
			// 执行请求
			response = this.getHttpClient().execute(httpGet);
			// 判断返回状态是否为200
			if (response.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(response.getEntity(), "utf-8");
			}
		} finally {
			if (response != null) {
				response.close();
			}

		}
		return null;
	}

	/**
	 * 带参数的httpcilent的get请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public String doGetParam(String url, Map<String, String> params)
			throws ClientProtocolException, IOException, URISyntaxException {
		URIBuilder builder = new URIBuilder(url);
		for (Map.Entry<String, String> entry : params.entrySet()) {
			builder.addParameter(entry.getKey(), entry.getValue());

		}

		return doGet(builder.build().toString());
	}

	/**
	 * httpclient的post请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public HttpResult doPost(String url, Map<String, String> params)
			throws ClientProtocolException, IOException, URISyntaxException {

		// 创建http POST请求
		HttpPost httpPost = new HttpPost(url);
		httpPost .setConfig(requestConfig);
		List<NameValuePair> parameters = new ArrayList<>();
		if (null != params) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			//构造表单
			UrlEncodedFormEntity  entity= new UrlEncodedFormEntity(parameters,"UTF-8");
			//将表单设置进入htppost
			httpPost.setEntity(entity);
		}
		CloseableHttpResponse response = null;
		try {
			// 执行请求
			response = this.getHttpClient().execute(httpPost);
			// 判断返回状态是否为200
			
				HttpResult result = new HttpResult();
				result.setData(EntityUtils.toString(response.getEntity(), "UTF-8"));
				result.setCode(response.getStatusLine().getStatusCode());
				return result;

			
		} finally {
			if (response != null) {
				response.close();
			}
		
		}


	}
	/**
	 * json格式的HTTPPOST
	 * @param url
	 * @param json
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public HttpResult doPostJson(String url, String json)
			throws ClientProtocolException, IOException, URISyntaxException {

		// 创建http POST请求
		HttpPost httpPost = new HttpPost(url);
		httpPost .setConfig(requestConfig);
		
		if (null != json) {
			StringEntity entity=new StringEntity(json, ContentType.APPLICATION_JSON);
			httpPost.setEntity(entity);
		}
		CloseableHttpResponse response = null;
		try {
			// 执行请求
			response = this.getHttpClient().execute(httpPost);
			// 判断返回状态是否为200
			
				HttpResult result = new HttpResult();
				result.setData(EntityUtils.toString(response.getEntity(), "UTF-8"));
				result.setCode(response.getStatusLine().getStatusCode());
				return result;

			
		} finally {
			if (response != null) {
				response.close();
			}
		
		}


	}
	/**
	 * 获取单例httpclient
	 * @return
	 */
	private CloseableHttpClient getHttpClient() {
		return beanFactory.getBean(CloseableHttpClient.class);
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory=beanFactory;
		
	}
}
