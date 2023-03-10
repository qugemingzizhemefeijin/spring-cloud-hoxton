package com.atguigu.springcloud.utils.http;

import org.apache.http.impl.client.HttpClientBuilder;

public interface ApacheHttpClientFactory {
	
	/**
	 * Creates an {@link HttpClientBuilder} that can be used to create a new {@link CloseableHttpClient}.
	 * @return A {@link HttpClientBuilder}
	 */
	public HttpClientBuilder createBuilder();

}
