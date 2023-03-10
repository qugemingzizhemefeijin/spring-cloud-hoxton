package com.atguigu.springcloud.utils.http;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.cookie.Cookie;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 封装HttpClient Callback返回数据
 */
public class ResponseCallbackStatus {
	
	private String url;

	private String encoding;

	private HttpEntity entity;
	
	private int statusCode;

	private String contentType;

	private String contentTypeString;
	
	private List<Cookie> cookies;
	
	private Map<String ,String> headerMap;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public HttpEntity getEntity() {
		return entity;
	}

	public void setEntity(HttpEntity entity) {
		this.entity = entity;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContentTypeString() {
		return contentTypeString;
	}

	public void setContentTypeString(String contentTypeString) {
		this.contentTypeString = contentTypeString;
	}

	public List<Cookie> getCookies() {
		return cookies;
	}

	public void setCookies(List<Cookie> cookies) {
		this.cookies = cookies;
	}

	public Map<String, String> getAllHeader() {
		return this.headerMap;
	}
	
	public String getHeader(String name) {
		return this.headerMap == null ? null : this.headerMap.get(name);
	}

	public void setHeader(Header[] headers) {
		if(headers != null && headers.length > 0) {
			this.headerMap = new HashMap<>();
			for(Header h : headers) {
				headerMap.put(h.getName(), h.getValue());
			}
		}
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

}
