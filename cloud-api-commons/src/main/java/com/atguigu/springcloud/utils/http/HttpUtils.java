package com.atguigu.springcloud.utils.http;

import com.atguigu.springcloud.utils.CollectionUtils;
import com.atguigu.springcloud.utils.enums.ECharset;
import com.atguigu.springcloud.utils.enums.EContentType;
import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.*;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.javatuples.KeyValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.CodingErrorAction;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);

	/**
	 * 连接超时时间 可以配到配置文件  （单位毫秒）
	 */
	private static final int MAX_TIME_OUT = 5000;
	
	private static enum HttpMethod {
		GET, POST
	}

	private static CloseableHttpClient client;
	private static PoolingHttpClientConnectionManager connManager = null;

	static {
		enabledSSL();
		client = getCloseableHttpClient(null);
	}

	/**
	 * 开启自动SSL
	 */
	private static void enabledSSL() {
		try {
			SSLContext sslContext = SSLContexts.custom().build();
			sslContext.init(null, new TrustManager[] { new X509TrustManager() {

				public void checkClientTrusted(X509Certificate[] arg0, String arg1)
						throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] arg0, String arg1)
						throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			} }, null);
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
					.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.INSTANCE)
					.register("https", new SSLConnectionSocketFactory(sslContext)).build();
			connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
			SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
			connManager.setDefaultSocketConfig(socketConfig);
			MessageConstraints messageConstraints = MessageConstraints.custom().setMaxHeaderCount(200)
					.setMaxLineLength(2000).build();

			ConnectionConfig connectionConfig = ConnectionConfig.custom()
					.setMalformedInputAction(CodingErrorAction.IGNORE)
					.setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(Consts.UTF_8)
					.setMessageConstraints(messageConstraints).build();
			connManager.setDefaultConnectionConfig(connectionConfig);
			connManager.setMaxTotal(200);
			connManager.setDefaultMaxPerRoute(20);
		} catch (KeyManagementException e) {

		} catch (NoSuchAlgorithmException e) {

		}
	}

	private static CloseableHttpClient getCloseableHttpClient(HttpProxy proxy) {
		HttpClientBuilder httpClientBuilder = HttpClients.custom();
		if (null != proxy) {
			HttpHost proxyHost = new HttpHost(proxy.getHost(), proxy.getPort());
			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			if (null != proxy.getUser() && null != proxy.getPassword()) {
				credentialsProvider.setCredentials(new AuthScope(proxy.getHost(), proxy.getPort()),
						new UsernamePasswordCredentials(proxy.getUser(), proxy.getPassword()));
			}
			httpClientBuilder.setProxy(proxyHost).setDefaultCredentialsProvider(credentialsProvider);
		}
		
		//设置Socket超时
		httpClientBuilder.setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(MAX_TIME_OUT).build());

		httpClientBuilder.setConnectionManager(connManager);
		// 重试规则
		httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler());
		//超时规则
		httpClientBuilder.setDefaultRequestConfig(builderConfig(null).build());

		return httpClientBuilder.build();
	}

	/**
	 * Get方式访问URL
	 * 
	 * @param url - URL
	 * @return ResponseStatus
	 */
	public static ResponseStatus get(String url) throws HttpException, IOException {
		return sendRequest(url, ECharset.UTF_8, HttpMethod.GET, null, null, null, EContentType.TEXT_HTML , null , null);
	}
	
	/**
	 * Get方式访问URL
	 *
	 * @param url - URL
	 * @param contentType - 请求类型
	 * @return ResponseStatus
	 */
	public static ResponseStatus get(String url, EContentType contentType) throws HttpException, IOException {
		return sendRequest(url, ECharset.UTF_8, HttpMethod.GET, null, null, null, contentType , null , null);
	}

	/**
	 * Get方式访问URL
	 *
	 * @param url - URL
	 * @param charset - 字符集编码
	 * @return ResponseStatus
	 */
	public static ResponseStatus get(String url, ECharset charset) throws HttpException, IOException {
		return sendRequest(url, charset, HttpMethod.GET, null, null, null, EContentType.TEXT_HTML , null , null);
	}

	/**
	 * Get方式访问URL
	 *
	 * @param url - URL
	 * @param charset - 字符集编码
	 * @param contentType - 请求类型
	 * @return ResponseStatus
	 */
	public static ResponseStatus get(String url, ECharset charset, EContentType contentType) throws HttpException, IOException {
		return sendRequest(url, charset, HttpMethod.GET, null, null, null, contentType , null , null);
	}

	/**
	 * Get方式访问URL
	 *
	 * @param url - URL
	 * @param charset - 字符集编码
	 * @param contentType - 请求类型
	 * @param headerMap - 请求头
	 * @return ResponseStatus
	 */
	public static ResponseStatus get(String url, ECharset charset, EContentType contentType, Map<String, String> headerMap) throws HttpException, IOException {
		return sendRequest(url, charset, HttpMethod.GET, null, headerMap, null, contentType , null , null);
	}
	
	/**
	 * Get方式访问URL
	 *
	 * @param url - URL
	 * @param charset - 字符集编码
	 * @param contentType - 请求类型
	 * @param headerMap - 请求头
	 * @param cookieList - cookies
	 * @return ResponseStatus
	 */
	public static ResponseStatus get(String url, ECharset charset, EContentType contentType, Map<String, String> headerMap , List<Cookie> cookieList) throws HttpException, IOException {
		return sendRequest(url, charset, HttpMethod.GET, null, headerMap, cookieList, contentType , null , null);
	}
	
	/**
	 * Get方式访问URL
	 *
	 * @param url - URL
	 * @param charset - 字符集编码
	 * @param contentType - 请求类型
	 * @param headerMap - 请求头
	 * @param cookieList - cookies
	 * @param proxy - 代理对象
	 * @return ResponseStatus
	 */
	public static ResponseStatus get(String url, ECharset charset, EContentType contentType, Map<String, String> headerMap , List<Cookie> cookieList , HttpProxy proxy) throws HttpException, IOException {
		return sendRequest(url, charset, HttpMethod.GET, null, headerMap, cookieList, contentType , proxy , null);
	}
	
	/**
	 * Get方式访问URL
	 *
	 * @param url - URL
	 * @param charset - 字符集编码
	 * @param contentType - 请求类型
	 * @param headerMap - 请求头
	 * @param cookieList - cookies
	 * @param proxy - 代理对象
	 * @param credentials - 设置HTTP身份认证
	 * @return ResponseStatus
	 */
	public static ResponseStatus get(String url, ECharset charset, EContentType contentType, Map<String, String> headerMap , List<Cookie> cookieList , HttpProxy proxy , UsernamePasswordCredentials credentials) throws HttpException, IOException {
		return sendRequest(url, charset, HttpMethod.GET, null, headerMap, cookieList, contentType , proxy , credentials);
	}

	/**
	 * Post方式访问URL
	 * 
	 * @param url - URL
	 * @return ResponseStatus
	 */
	public static ResponseStatus post(String url) throws HttpException, IOException {
		return sendRequest(url, ECharset.UTF_8, HttpMethod.POST, null, null, null, EContentType.APPLICATION_FORM_URLENCODED,null,null);
	}

	/**
	 * Post方式访问URL
	 * 
	 * @param url - URL
	 * @return ResponseStatus
	 */
	public static ResponseStatus post(String url, Map<String, String> parameterMap) throws HttpException, IOException {
		return sendRequest(url, ECharset.UTF_8, HttpMethod.POST, parameterMap, null, null, EContentType.APPLICATION_FORM_URLENCODED,null,null);
	}

	/**
	 * Post方式访问URL
	 * 
	 * @param url - URL
	 * @param pairs - 参数
	 * @return ResponseStatus
	 */
	public static ResponseStatus post(String url, String... pairs) throws HttpException, IOException {
		return sendRequest(url, ECharset.UTF_8, HttpMethod.POST, CollectionUtils.toStringMap(pairs), null, null, EContentType.APPLICATION_FORM_URLENCODED,null,null);
	}

	/**
	 * Post方式访问URL
	 * 
	 * @param url - URL
	 * @param charset - 编码
	 * @param pairs - 参数
	 * @return ResponseStatus
	 */
	public static ResponseStatus post(String url, ECharset charset, String... pairs) throws HttpException, IOException {
		return sendRequest(url, charset, HttpMethod.POST, CollectionUtils.toStringMap(pairs), null, null, EContentType.APPLICATION_FORM_URLENCODED,null , null);
	}

	/**
	 * Post方式访问URL
	 * 
	 * @param url - URL
	 * @param parameterMap - 参数
	 * @param charset - 编码
	 * @return ResponseStatus
	 */
	public static ResponseStatus post(String url, Map<String, String> parameterMap, ECharset charset) throws HttpException, IOException {
		return sendRequest(url, charset, HttpMethod.POST, parameterMap, null, null, EContentType.APPLICATION_FORM_URLENCODED,null,null);
	}

	/**
	 * Post方式访问URL
	 * 
	 * @param url - URL
	 * @param parameterMap - 参数
	 * @param charset - 编码
	 * @param contentType - 请求类型
	 * @return ResponseStatus
	 */
	public static ResponseStatus post(String url, Map<String, String> parameterMap, ECharset charset, EContentType contentType) throws HttpException, IOException {
		return sendRequest(url, charset, HttpMethod.POST, parameterMap, null, null, contentType,null , null);
	}

	/**
	 * Post方式访问URL
	 * 
	 * @param url - URL
	 * @param parameterMap - 参数
	 * @param charset - 编码
	 * @param contentType - 请求类型
	 * @param headerMap - 请求头
	 * @return ResponseStatus
	 */
	public static ResponseStatus post(String url, Map<String, String> parameterMap, ECharset charset, EContentType contentType, Map<String, String> headerMap) throws HttpException, IOException {
		return sendRequest(url, charset, HttpMethod.POST, parameterMap, headerMap, null,contentType,null,null);
	}
	
	/**
	 * Post方式访问URL
	 * 
	 * @param url - URL
	 * @param parameterMap - 参数
	 * @param charset - 编码
	 * @param contentType - 请求类型
	 * @param headerMap - 请求头
	 * @param cookieList - cookies
	 * @return ResponseStatus
	 */
	public static ResponseStatus post(String url, Map<String, String> parameterMap, ECharset charset, EContentType contentType, Map<String, String> headerMap , List<Cookie> cookieList) throws HttpException, IOException {
		return sendRequest(url, charset, HttpMethod.POST, parameterMap, headerMap, cookieList,contentType,null , null);
	}
	
	/**
	 * Post方式访问URL
	 * 
	 * @param url - URL
	 * @param parameterMap - 参数
	 * @param charset - 编码
	 * @param contentType - 请求类型
	 * @param headerMap - 请求头
	 * @param cookieList - cookies
	 * @param proxy - 代理对象
	 * @return ResponseStatus
	 */
	public static ResponseStatus post(String url, Map<String, String> parameterMap, ECharset charset, EContentType contentType, Map<String, String> headerMap , List<Cookie> cookieList , HttpProxy proxy) throws HttpException, IOException {
		return sendRequest(url, charset, HttpMethod.POST, parameterMap, headerMap, cookieList,contentType,proxy,null);
	}
	
	/**
	 * Post方式访问URL
	 * 
	 * @param url - URL
	 * @param parameterMap - 参数
	 * @param charset - 编码
	 * @param contentType - 请求类型
	 * @param headerMap - 请求头
	 * @param cookieList - cookies
	 * @param proxy - 代理对象
	 * @param credentials - 设置HTTP身份认证
	 * @return ResponseStatus
	 */
	public static ResponseStatus post(String url, Map<String, String> parameterMap, ECharset charset, EContentType contentType, Map<String, String> headerMap , List<Cookie> cookieList , HttpProxy proxy , UsernamePasswordCredentials credentials) throws HttpException, IOException {
		return sendRequest(url, charset, HttpMethod.POST, parameterMap, headerMap, cookieList,contentType,proxy,credentials);
	}

	/**
	 * 普通的访问http URL
	 * 
	 * @param url - URL
	 * @param charset - 字符编码
	 * @param bodyType - 请求类型
	 * @param parameterMap - 参数
	 * @param headerMap - 请求头参数
	 * @param cookieList - cookies
	 * @param contentType - 请求头
	 * @param proxy - 代理对象
	 * @param credentials - 设置HTTP身份认证
	 * @return ResponseStatus
	 */
	public static ResponseStatus sendRequest(String url, ECharset charset, HttpMethod bodyType, Map<String, String> parameterMap, Map<String, String> headerMap, List<Cookie> cookieList , EContentType contentType , HttpProxy proxy , UsernamePasswordCredentials credentials) throws HttpException, IOException {
		if (url == null || url.length() == 0) {
			return errorResponseStatus(HttpStatus.SC_FORBIDDEN, "url is error!");
		}

		HttpEntity entity = null;
		HttpRequestBase request = null;
		CloseableHttpResponse response = null;
		try {
			if (HttpMethod.GET == bodyType) {
				request = new HttpGet(url);
			} else if (HttpMethod.POST == bodyType) {
				HttpPost httpPost = new HttpPost(url);
				if (parameterMap != null && !parameterMap.isEmpty()) {
					List<NameValuePair> nvBodyList = new ArrayList<>();

					parameterMap.forEach((key, value) -> {
						nvBodyList.add(new BasicNameValuePair(key, value));
					});

					httpPost.setEntity(new UrlEncodedFormEntity(nvBodyList, charset.toString()));
				}
				request = httpPost;
			}

			if (contentType != null) {
				request.addHeader(HttpHeaders.CONTENT_TYPE, contentType.toString());
			}

			if (headerMap != null && !headerMap.isEmpty()) {
				for (Map.Entry<String, String> me : headerMap.entrySet()) {
					request.addHeader(me.getKey(), me.getValue());
				}
			}
			//如果没有agent，则自动添加一个
			if(headerMap != null && !headerMap.containsKey(HttpHeaders.USER_AGENT)) {
				request.addHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36");
			}
			
			
			//设置代理
			Builder builder = null;
			if(proxy != null) {
				builder = setProxy(builder, proxy);
			}
			
			if(builder != null) {
				request.setConfig(builder.build());
			}
			
			//获取器
			HttpClientContext context = HttpClientContext.create();
			CookieStore cookieStore = new BasicCookieStore();
			context.setCookieStore(cookieStore);

			if(cookieList != null && !cookieList.isEmpty()) {
				for(Cookie cookie : cookieList) {
					cookieStore.addCookie(cookie);
				}
			}
			
			if(credentials != null) {
				CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
				credentialsProvider.setCredentials(AuthScope.ANY,credentials);
				context.setCredentialsProvider(credentialsProvider);
			}

			response = client.execute(request , context);
			entity = response.getEntity(); // 获取响应实体
			StatusLine statusLine = response.getStatusLine();
			ResponseStatus ret = new ResponseStatus();
			//设置返回码
			ret.setStatusCode(statusLine.getStatusCode());
			//设置返回的字符编码
			ret.setEncoding(charset);
			//设置返回头信息
			ret.setHeader(response.getAllHeaders());
			//设置url
			ret.setUrl(url);
			//设置Cookies
			ret.setCookies(cookieStore.getCookies());
			//充实返回对象
			getResponseStatus(entity, ret);
			return ret;
		} catch (ConnectTimeoutException | SocketTimeoutException | SocketException e) {
			throw e;
		} finally {
			close(entity, request, response);
		}
	}

	/**
	 * 以postEntity方式请求URL
	 * 
	 * @param url - URL
	 * @param content - 内容
	 * @return ResponseStatus
	 */
	public static ResponseStatus postEntity(String url, String content) throws HttpException, IOException {
		return postEntity(url, content, ECharset.UTF_8, EContentType.APPLICATION_FORM_URLENCODED, null , null , null , null);
	}

	/**
	 * 
	 * 以postEntity方式请求URL
	 * 
	 * @param url - URL
	 * @param content - 内容
	 * @param charset - 编码
	 * @return ResponseStatus
	 */
	public static ResponseStatus postEntity(String url, String content, ECharset charset) throws HttpException, IOException {
		return postEntity(url, content, charset, EContentType.APPLICATION_FORM_URLENCODED, null , null , null , null);
	}

	/**
	 * 
	 * 以postEntity方式请求URL
	 * 
	 * @param url - URL
	 * @param content - 内容
	 * @param charset - 编码
	 * @param contentType - 请求类型
	 * @return ResponseStatus
	 */
	public static ResponseStatus postEntity(String url, String content, ECharset charset, EContentType contentType) throws HttpException, IOException {
		return postEntity(url, content, charset, contentType, null , null , null , null);
	}

	/**
	 * 以json postEntity方式请求URL
	 * 
	 * @param url - URL
	 * @param content - 内容
	 * @return ResponseStatus
	 */
	public static ResponseStatus postJsonEntity(String url, String content) throws HttpException, IOException {
		return postEntity(url, content, ECharset.UTF_8, EContentType.APPLICATION_JSON, null , null , null , null);
	}

	/**
	 * 以xml postEntity方式请求URL
	 * 
	 * @param url - URL
	 * @param content - 内容
	 * @return ResponseStatus
	 */
	public static ResponseStatus postXmlEntity(String url, String content) throws HttpException, IOException {
		return postEntity(url, content, ECharset.UTF_8, EContentType.APPLICATION_XML, null , null , null , null);
	}
	
	/**
	 * 
	 * 以postEntity方式请求URL
	 * 
	 * @param url - URL
	 * @param content - 内容
	 * @param charset - 编码
	 * @param contentType - 请求类型
	 * @param headerMap - 头信息
	 * @return ResponseStatus
	 */
	public static ResponseStatus postEntity(String url, String content, ECharset charset, EContentType contentType, Map<String, String> headerMap) throws HttpException, IOException {
		return postEntity(url , content , charset , contentType , headerMap , null , null , null);
	}
	
	/**
	 * 
	 * 以postEntity方式请求URL
	 * 
	 * @param url - URL
	 * @param content - 内容
	 * @param charset - 编码
	 * @param contentType - 请求类型
	 * @param headerMap - 头信息
	 * @param proxy - 代理对象
	 * @return ResponseStatus
	 */
	public static ResponseStatus postEntity(String url, String content, ECharset charset, EContentType contentType, Map<String, String> headerMap , HttpProxy proxy) throws HttpException, IOException {
		return postEntity(url , content , charset , contentType , headerMap , null , proxy , null);
	}
	
	/**
	 * 
	 * 以postEntity方式请求URL
	 * 
	 * @param url - URL
	 * @param content - 内容
	 * @param charset - 编码
	 * @param contentType - 请求类型
	 * @param headerMap - 头信息
	 * @param cookieList - cookies
	 * @return ResponseStatus
	 */
	public static ResponseStatus postEntity(String url, String content, ECharset charset, EContentType contentType, Map<String, String> headerMap , List<Cookie> cookieList) throws HttpException, IOException {
		return postEntity(url , content , charset , contentType , headerMap , cookieList , null , null);
	}
	
	/**
	 * 
	 * 以postEntity方式请求URL
	 * 
	 * @param url - URL
	 * @param content - 内容
	 * @param charset - 编码
	 * @param contentType - 请求类型
	 * @param headerMap - 头信息
	 * @param cookieList - cookies
	 * @param proxy - 代理对象
	 * @return ResponseStatus
	 */
	public static ResponseStatus postEntity(String url, String content, ECharset charset, EContentType contentType, Map<String, String> headerMap , List<Cookie> cookieList , HttpProxy proxy) throws HttpException, IOException {
		return postEntity(url , content , charset , contentType , headerMap , cookieList , proxy , null);
	}

	/**
	 * 
	 * 以postEntity方式请求URL
	 * 
	 * @param url - URL
	 * @param content - 内容
	 * @param charset - 编码
	 * @param contentType - 请求类型
	 * @param headerMap - 头信息
	 * @param cookieList - cookies
	 * @param proxy - 代理对象
	 * @param credentials - Http身份认证
	 * @return ResponseStatus
	 */
	public static ResponseStatus postEntity(String url, String content, ECharset charset, EContentType contentType, Map<String, String> headerMap , List<Cookie> cookieList , HttpProxy proxy , UsernamePasswordCredentials credentials) throws HttpException, IOException {
		if (url == null || url.length() == 0) {
			return errorResponseStatus(HttpStatus.SC_FORBIDDEN, "url is error!");
		}

		HttpEntity entity = null;
		HttpRequestBase request = null;
		CloseableHttpResponse response = null;
		try {
			HttpPost httpPost = new HttpPost(url);
			if (content != null) {
				StringEntity s = new StringEntity(content , charset.toString());
				s.setContentType(contentType.toString());
				httpPost.setEntity(s);
			}
			request = httpPost;

			if (contentType != null) {
				request.addHeader(HttpHeaders.CONTENT_TYPE, contentType.toString());
			}

			// setTimeOut(request, timeout);

			if (headerMap != null && !headerMap.isEmpty()) {
				for (Map.Entry<String, String> me : headerMap.entrySet()) {
					request.addHeader(me.getKey(), me.getValue());
				}
			}
			
			//补入头部信息
			if(headerMap != null && !headerMap.containsKey(HttpHeaders.USER_AGENT)) {
				request.addHeader(HttpHeaders.USER_AGENT,"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36");
			}
			
			//设置代理
			Builder builder = null;
			if(proxy != null) {
				builder = setProxy(builder, proxy);
			}
			
			if(builder != null) {
				request.setConfig(builder.build());
			}
			
			//获取器
			HttpClientContext context = HttpClientContext.create();
			
			if(cookieList != null && !cookieList.isEmpty()) {
				CookieStore cookieStore = new BasicCookieStore();
				
				for(Cookie cookie : cookieList) {
					cookieStore.addCookie(cookie);
				}
				
				context.setCookieStore(cookieStore);
			}
			
			if(credentials != null) {
				CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
				credentialsProvider.setCredentials(AuthScope.ANY,credentials);
				context.setCredentialsProvider(credentialsProvider);
			}

			response = client.execute(request , context);
			entity = response.getEntity(); // 获取响应实体
			StatusLine statusLine = response.getStatusLine();
			ResponseStatus ret = new ResponseStatus();
			//设置返回码
			ret.setStatusCode(statusLine.getStatusCode());
			//设置返回的字符编码
			ret.setEncoding(charset);
			//设置返回头信息
			ret.setHeader(response.getAllHeaders());
			//设置url
			ret.setUrl(url);
			//设置Cookies
			CookieStore cookieStore = context.getCookieStore();
			if(cookieStore != null) {
				ret.setCookies(cookieStore.getCookies());
			}
			//充实返回对象
			getResponseStatus(entity, ret);
			return ret;
		} catch (ConnectTimeoutException | SocketTimeoutException | SocketException e) {
			throw e;
		} finally {
			close(entity, request, response);
		}
	}
	
	/**
	 * 上传文件
	 * @param url - URL
	 * @param paramList - 参数列表
	 * @param fileList - 文件列表
	 * @return ResponseStatus
	 */
	public static ResponseStatus uploadFiles(String url , List<KeyValue<String , String>> paramList , List<HttpFileEntity> fileList) throws HttpException, IOException {
		return uploadFiles(url, paramList, fileList , null , MAX_TIME_OUT , null , null);
	}
	
	/**
	 * 上传文件
	 * @param url - URL
	 * @param paramList - 参数列表
	 * @param fileList - 文件列表
	 * @param headerMap - 自定义头部Map
	 * @return ResponseStatus
	 */
	public static ResponseStatus uploadFiles(String url , List<KeyValue<String , String>> paramList , List<HttpFileEntity> fileList , Map<String , String> headerMap) throws HttpException, IOException {
		return uploadFiles(url, paramList, fileList , headerMap , MAX_TIME_OUT , null , null);
	}
	
	/**
	 * 上传文件
	 * @param url - URL
	 * @param paramList - 参数列表
	 * @param fileList - 文件列表
	 * @param headerMap - 自定义头部Map
	 * @param proxy - 代理对象
	 * @return ResponseStatus
	 */
	public static ResponseStatus uploadFiles(String url , List<KeyValue<String , String>> paramList , List<HttpFileEntity> fileList , Map<String , String> headerMap , HttpProxy proxy) throws HttpException, IOException {
		return uploadFiles(url, paramList, fileList , headerMap , MAX_TIME_OUT , null , proxy);
	}
	
	/**
	 * 上传文件
	 * @param url - URL
	 * @param paramList - 参数列表
	 * @param fileList - 文件列表
	 * @param headerMap - 自定义头部Map
	 * @param timeout - 超时时间
	 * @return ResponseStatus
	 */
	public static ResponseStatus uploadFiles(String url , List<KeyValue<String , String>> paramList , List<HttpFileEntity> fileList , Map<String , String> headerMap , int timeout) throws HttpException, IOException {
		return uploadFiles(url, paramList, fileList , headerMap , timeout , null , null);
	}
	
	/**
	 * 上传文件
	 * @param url - URL
	 * @param paramList - 参数列表
	 * @param fileList - 文件列表
	 * @param headerMap - 自定义头部Map
	 * @param timeout - 超时时间
	 * @param cookieList - Cookie集合
	 * @return ResponseStatus
	 */
	public static ResponseStatus uploadFiles(String url , List<KeyValue<String , String>> paramList , List<HttpFileEntity> fileList , Map<String , String> headerMap , int timeout , List<Cookie> cookieList) throws HttpException, IOException {
		return uploadFiles(url, paramList, fileList , headerMap , timeout , cookieList , null);
	}
	
	/**
	 * 上传文件
	 * @param url - URL
	 * @param paramList - 参数列表
	 * @param fileList - 文件列表
	 * @param headerMap - 自定义头部Map
	 * @param timeout - 超时时间
	 * @param proxy - 代理对象
	 * @return ResponseStatus
	 */
	public static ResponseStatus uploadFiles(String url , List<KeyValue<String , String>> paramList , List<HttpFileEntity> fileList , Map<String , String> headerMap , int timeout , List<Cookie> cookieList , HttpProxy proxy) throws HttpException, IOException {
		if(fileList == null || fileList.isEmpty()) {
			throw new IllegalArgumentException("fileList size = 0");
		}
		
		if(timeout <= 0) timeout = MAX_TIME_OUT;
		
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("uploadFiles url :" + url);
			if(headerMap != null && !headerMap.isEmpty()) {
				headerMap.forEach((k , v) -> LOGGER.debug("header -> " + k + ":" + v));
			}
			if(paramList != null && !paramList.isEmpty()) {
				paramList.forEach(k -> LOGGER.debug("param -> "+k.getKey()+":"+k.getValue()));
			}
			LOGGER.debug("file size : " + fileList.size());
		}
		
		HttpEntity entity = null;
		HttpRequestBase request = null;
		CloseableHttpResponse response = null;
		
		try {
			HttpPost httpPost = new HttpPost(url);
			
			//以浏览器兼容模式运行，防止文件名乱码。
			MultipartEntityBuilder builder = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE).setCharset(ECharset.UTF_8.getCharset());
			
			//将文件加入到数据中
			for(HttpFileEntity httpFile : fileList) {
				if(httpFile.getFileType() == HttpFileEntity.FILE_TYPE_FILE) {
					builder.addBinaryBody(httpFile.getName(), httpFile.getFile() , ContentType.DEFAULT_BINARY , httpFile.getFileName());
				} else if(httpFile.getFileType() == HttpFileEntity.FILE_TYPE_BYTES) {
					builder.addBinaryBody(httpFile.getName(), httpFile.getFileByte() , ContentType.DEFAULT_BINARY , httpFile.getFileName());
				} else {
					builder.addBinaryBody(httpFile.getName(), httpFile.getInputStream() , ContentType.DEFAULT_BINARY , httpFile.getFileName());
				}
			}
			
			if(paramList != null && !paramList.isEmpty()) {
				paramList.forEach(k -> builder.addTextBody(k.getKey() , k.getValue()));
			}
			
			request = httpPost;

			if(headerMap != null && !headerMap.isEmpty()) {
				for(Map.Entry<String, String> me : headerMap.entrySet()) {
					request.addHeader(me.getKey(), me.getValue());
				}
			}
			
			//补入头部信息
			if(headerMap != null && !headerMap.containsKey(HttpHeaders.USER_AGENT)) {
				request.addHeader(HttpHeaders.USER_AGENT,"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36");
			}
			
			/*StringBody uploadFileName = new StringBody("my.png",ContentType.create("text/plain", Consts.UTF_8));*/
            //.addPart("uploadFileName", uploadFileName)// uploadFileName对应服务端类的同名属性<String类型>
			
			httpPost.setEntity(builder.build());
			
			Builder b = null;
			//如果跟全局的超时时间一致，则不需要设置了
			if(timeout != MAX_TIME_OUT) {
				//设置超时时间
				b = RequestConfig.custom()
					.setRedirectsEnabled(false)//禁止自动跳转
					.setSocketTimeout(timeout)
					.setConnectTimeout(timeout)//设置请求和传输超时时间
					.setCookieSpec(CookieSpecs.STANDARD);//Cookie策略
			}
			
			//设置代理
			if(proxy != null) {
				b = setProxy(b, proxy);
			}
			
			if(b != null) {
				httpPost.setConfig(b.build());
			}
			
			//获取器
			HttpClientContext context = HttpClientContext.create();
			CookieStore cookieStore = new BasicCookieStore();
			context.setCookieStore(cookieStore);

			if(cookieList != null && !cookieList.isEmpty()) {
				for(Cookie cookie : cookieList) {
					cookieStore.addCookie(cookie);
				}
			}
			
			response = client.execute(request);
			entity = response.getEntity(); // 获取响应实体
			StatusLine statusLine = response.getStatusLine();
			ResponseStatus ret = new ResponseStatus();
			//设置返回码
			ret.setStatusCode(statusLine.getStatusCode());
			//设置返回的字符编码
			ret.setEncoding(ECharset.UTF_8.getName());
			//设置返回头信息
			ret.setHeader(response.getAllHeaders());
			//设置url
			ret.setUrl(url);
			//设置Cookies
			ret.setCookies(cookieStore.getCookies());
			//充实返回对象
			getResponseStatus(entity, ret);
			return ret;
		} finally {
			close(entity, request, response);
		}
	}
	
	/**
	 * 使用get调用URL，并且执行回调函数
	 * @param url - URL
	 * @param callback - 回调函数
	 */
	public static void getCallback(String url , Map<String, String> headerMap  , List<Cookie> cookieList , HttpProxy proxy , IHttpCallback callback) throws HttpException, IOException , Exception {
		if(callback == null) {
			throw new IllegalArgumentException("callback can't null.");
		}
		
		HttpRequestBase request = new HttpGet(url);
		if (headerMap != null && !headerMap.isEmpty()) {
			for (Map.Entry<String, String> me : headerMap.entrySet()) {
				request.addHeader(me.getKey(), me.getValue());
			}
		}
		//如果没有agent，则自动添加一个
		if(headerMap != null && !headerMap.containsKey(HttpHeaders.USER_AGENT)) {
			request.addHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36");
		}
		
		
		//设置代理
		Builder builder = null;
		if(proxy != null) {
			builder = setProxy(builder, proxy);
		}
		
		if(builder != null) {
			request.setConfig(builder.build());
		}
		//获取器
		HttpClientContext context = HttpClientContext.create();
		CookieStore cookieStore = new BasicCookieStore();
		context.setCookieStore(cookieStore);

		if(cookieList != null && !cookieList.isEmpty()) {
			for(Cookie cookie : cookieList) {
				cookieStore.addCookie(cookie);
			}
		}
		
		HttpEntity entity = null;
		CloseableHttpResponse response = null;
		try {
			//执行getMethod
			response = client.execute(request , context);
			entity = response.getEntity(); // 获取响应实体
			StatusLine statusLine = response.getStatusLine();
			ResponseCallbackStatus ret = new ResponseCallbackStatus();
			//设置返回码
			ret.setStatusCode(statusLine.getStatusCode());
			//设置返回头信息
			ret.setHeader(response.getAllHeaders());
			//设置url
			ret.setUrl(url);
			//充实返回对象
			Header enHeader = entity.getContentEncoding();
			if (enHeader != null) {
				String charset = enHeader.getValue().toLowerCase();
				ret.setEncoding(charset);
			} else {
				ret.setEncoding(ECharset.UTF_8.getName());
			}
			String contenttype = getResponseStatusType(entity);
			ret.setContentType(contenttype);
			ret.setContentTypeString(getResponseStatusTypeString(entity));
			ret.setEntity(entity);
			
			callback.callback(ret);
		} finally {
			close(entity, request, response);
		}
	}
	
	private static void getResponseStatus(HttpEntity entity, ResponseStatus ret) throws IOException {
		Header enHeader = entity.getContentEncoding();
		if (enHeader != null) {
			String charset = enHeader.getValue().toLowerCase();
			ret.setEncoding(charset);
		}
		String contenttype = getResponseStatusType(entity);
		ret.setContentType(contenttype);
		ret.setContentTypeString(getResponseStatusTypeString(entity));
		ret.setContentBytes(EntityUtils.toByteArray(entity));
	}

	private static String getResponseStatusType(HttpEntity method) {
		Header contenttype = method.getContentType();
		if (contenttype == null) {
			return null;
		}
		String ret = null;
		try {
			HeaderElement[] hes = contenttype.getElements();
			if (hes != null && hes.length > 0) {
				ret = hes[0].getName();
			}
		} catch (Exception e) {
		}
		return ret;
	}

	private static String getResponseStatusTypeString(HttpEntity method) {
		Header contenttype = method.getContentType();
		if (contenttype == null) {
			return null;
		}
		return contenttype.getValue();
	}

	private static void close(HttpEntity entity, HttpRequestBase request, CloseableHttpResponse response) throws IOException {
		if (request != null) {
			request.releaseConnection();
		}
		if (entity != null) {
			entity.getContent().close();
		}
		if (response != null) {
			response.close();
		}
	}

	private static ResponseStatus errorResponseStatus(int statusCode, String content) throws UnsupportedEncodingException {
		ResponseStatus status = new ResponseStatus();
		status.setContentBytes(content.getBytes(ECharset.UTF_8.getCharset()));
		status.setContentType(null);
		status.setContentTypeString(null);
		status.setEncoding(ECharset.UTF_8);
		status.setStatusCode(statusCode);

		return status;
	}
	
	/**
	 * 组装默认的Builder
	 * @param builder - Builder
	 * @return Builder
	 */
	private static Builder builderConfig(Builder builder) {
		if(builder == null) {
			return RequestConfig.custom()
					.setRedirectsEnabled(false)//禁止自动跳转
					.setConnectionRequestTimeout(MAX_TIME_OUT)// 设置从连接池获取连接实例的超时
					.setConnectTimeout(MAX_TIME_OUT)// 设置连接超时
					.setSocketTimeout(MAX_TIME_OUT)// 设置读取超时
					.setCookieSpec(CookieSpecs.STANDARD);//Cookie策略
		}
		return builder;
	}
	
	/**
	 * 设置代理
	 * @param builder - Builder
	 * @param proxy - 代理对象
	 * @return Builder
	 */
	private static Builder setProxy(Builder builder , HttpProxy proxy) {
		builder = builderConfig(builder);
		builder.setProxy(new HttpHost(proxy.getHost(), proxy.getPort()));
		
		return builder;
	}
	
	/**
	 * 下载文件
	 * @param url - URL
	 * @param path - 保存路径
	 * @return True or False
	 */
	public static boolean downloadFile(String url , String path) {
		try {
			getCallback(url, null , null , null , entity-> {
				File storeFile = new File(path);
				if(!storeFile.getParentFile().exists()) storeFile.getParentFile().mkdirs();
				if(!storeFile.exists()) {
					storeFile.createNewFile();
				}
				
				try(InputStream instream = entity.getEntity().getContent();
					FileOutputStream output = new FileOutputStream(storeFile)) {
					FileCopyUtils.copy(instream, output);
					
					output.flush();
				} catch (IOException ex) {
					LOGGER.error(ex.getMessage() , ex);
					if(storeFile.exists()) storeFile.delete();
				}
			});
			
			return true;
		} catch (Exception e) {
			LOGGER.error(e.getMessage() , e);
		}
		
		return false;
	}

}
