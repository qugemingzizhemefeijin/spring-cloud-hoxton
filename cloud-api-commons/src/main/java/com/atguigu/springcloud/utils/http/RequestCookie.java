package com.atguigu.springcloud.utils.http;

import org.apache.http.impl.cookie.BasicClientCookie;

/**
 * Request Cookie
 * 
 */
public class RequestCookie extends BasicClientCookie {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1589182388247075882L;

	public RequestCookie(String name, String value) {
		super(name, value);
	}
	
	public RequestCookie(String name, String value , String domain) {
		super(name, value);
		this.setDomain(domain);
	}

}
