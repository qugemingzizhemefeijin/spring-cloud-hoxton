package com.atguigu.springcloud.utils.http;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Http Client 连接池
 *
 */
public final class HttpClientPool {
	
	private final Timer connectionManagerTimer = new Timer("ApacheHttpClientConfiguration.connectionManagerTimer", true);
	
	private CloseableHttpClient httpClient;
	
	public HttpClientConnectionManager connectionManager(HttpClientProperties httpClientProperties) {
		return connectionManager(new DefaultApacheHttpClientConnectionManagerFactory() , null , httpClientProperties);
	}

	public HttpClientConnectionManager connectionManager(RegistryBuilder<ConnectionSocketFactory> registryBuilder , HttpClientProperties httpClientProperties) {
		return connectionManager(new DefaultApacheHttpClientConnectionManagerFactory() , registryBuilder , httpClientProperties);
	}
	
	public HttpClientConnectionManager connectionManager(ApacheHttpClientConnectionManagerFactory connectionManagerFactory, RegistryBuilder<ConnectionSocketFactory> registryBuilder , HttpClientProperties httpClientProperties) {
		final HttpClientConnectionManager connectionManager = connectionManagerFactory
				.newConnectionManager(httpClientProperties.isDisableSslValidation(), httpClientProperties.getMaxConnections(),
						httpClientProperties.getMaxConnectionsPerRoute(),
						httpClientProperties.getTimeToLive(),
						httpClientProperties.getTimeToLiveUnit(), registryBuilder);
		
		this.connectionManagerTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				connectionManager.closeExpiredConnections();
			}
		}, 30000, httpClientProperties.getConnectionTimerRepeat());
		return connectionManager;
	}
	
	public CloseableHttpClient customHttpClient(HttpClientProperties httpClientProperties) {
		return customHttpClient(connectionManager(httpClientProperties) , httpClientProperties);
	}
	
	//禁用Cookie管理
	public CloseableHttpClient customHttpClient(HttpClientConnectionManager httpClientConnectionManager, HttpClientProperties httpClientProperties) {
		HttpClientBuilder builder = HttpClientBuilder.create().disableCookieManagement().useSystemProperties();
		this.httpClient = createClient(builder, httpClientConnectionManager, httpClientProperties);
		return this.httpClient;
	}
	
	//自定义
	public CloseableHttpClient httpClient(ApacheHttpClientFactory httpClientFactory, HttpClientConnectionManager httpClientConnectionManager, HttpClientProperties httpClientProperties) {
		this.httpClient = createClient(httpClientFactory.createBuilder(), httpClientConnectionManager, httpClientProperties);
		return this.httpClient;
	}
	
	private CloseableHttpClient createClient(HttpClientBuilder builder, HttpClientConnectionManager httpClientConnectionManager, HttpClientProperties httpClientProperties) {
		RequestConfig defaultRequestConfig = RequestConfig.custom()
				.setConnectTimeout(httpClientProperties.getConnectionTimeout())
				.setRedirectsEnabled(httpClientProperties.isFollowRedirects()).build();
		
		CloseableHttpClient httpClient = builder.setDefaultRequestConfig(defaultRequestConfig).setConnectionManager(httpClientConnectionManager).build();
		return httpClient;
	}
	
	public void destroy() throws Exception {
		connectionManagerTimer.cancel();
		if(httpClient != null) {
			httpClient.close();
		}
	}

}
