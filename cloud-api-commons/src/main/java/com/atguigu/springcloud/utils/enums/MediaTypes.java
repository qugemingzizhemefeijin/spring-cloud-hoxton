package com.atguigu.springcloud.utils.enums;

/**
 * 媒体类型
 *
 */
public final class MediaTypes {
	
	private MediaTypes(){}
	
	public final static String APPLICATION_XML_VALUE = "application/xml";
	
	public final static String APPLICATION_XML_VALUE_UTF8 = APPLICATION_XML_VALUE + ";charset=UTF-8";
	
	public final static String APPLICATION_JSON_VALUE = "application/json";
	
	public final static String APPLICATION_JSON_VALUE_UTF8 = APPLICATION_JSON_VALUE + ";charset=UTF-8";
	
	public final static String APPLICATION_FORM_URLENCODED_VALUE = "application/x-www-form-urlencoded";
	
	public final static String TEXT_PLAIN_VALUE = "text/plain";
	
	public final static String TEXT_PLAIN_VALUE_UTF8 = TEXT_PLAIN_VALUE + ";charset=UTF-8";
	
	public final static String TEXT_XML_VALUE = "text/xml";
	
	public final static String TEXT_XML_VALUE_UTF8 = TEXT_XML_VALUE + ";charset=UTF-8";

	public final static String TEXT_HTML_VALUE = "text/html";
	
	public final static String TEXT_HTML_VALUE_UTF8 = TEXT_HTML_VALUE + ";charset=UTF-8";
	
}
