package com.atguigu.springcloud.utils.http;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.springcloud.utils.enums.ECharset;
import org.apache.http.Header;
import org.apache.http.cookie.Cookie;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 封装HttpClient返回数据
 */
public class ResponseStatus {
	
	private String url;

	private String encoding;

	private byte[] contentBytes;

	private int statusCode;

	private String contentType;

	private String contentTypeString;
	
	private List<Cookie> cookies;
	
	private Map<String ,String> headerMap;

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	
	public void setEncoding(Charset charset) {
		if(charset == null) charset = ECharset.UTF_8.getCharset();
		this.encoding = charset.name();
	}
	
	public void setEncoding(ECharset charset) {
		if(charset == null) charset = ECharset.UTF_8;
		this.encoding = charset.getName();
	}

	public String getContentType() {
		return this.contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContentTypeString() {
		return this.contentTypeString;
	}

	public void setContentTypeString(String contenttypeString) {
		this.contentTypeString = contenttypeString;
	}

	public String getContent() throws UnsupportedEncodingException {
		return this.getContent(this.encoding);
	}

	public String getContent(String encoding) throws UnsupportedEncodingException {
		if (encoding == null) {
			return new String(contentBytes);
		}
		return new String(contentBytes, encoding);
	}
	
	public JSONObject getJsonObjectContent() throws UnsupportedEncodingException , JSONException {
		return getJsonObjectContent(ECharset.UTF_8.name());
	}
	
	public JSONObject getJsonObjectContent(String encoding) throws UnsupportedEncodingException , JSONException {
		String json = getContent();
		if(json == null) {
			return null;
		}
		
		return JSONObject.parseObject(json);
	}
	
	public JSONArray getJsonArrayContent() throws UnsupportedEncodingException , JSONException {
		return getJsonArrayContent(ECharset.UTF_8.name());
	}
	
	public JSONArray getJsonArrayContent(String encoding) throws UnsupportedEncodingException , JSONException {
		String json = getContent();
		if(json == null) {
			return null;
		}
		
		return JSONArray.parseArray(json);
	}

	public String getUTFContent() throws UnsupportedEncodingException {
		return this.getContent(ECharset.UTF_8.name());
	}

	public byte[] getContentBytes() {
		return contentBytes;
	}

	public void setContentBytes(byte[] contentBytes) {
		this.contentBytes = contentBytes;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
