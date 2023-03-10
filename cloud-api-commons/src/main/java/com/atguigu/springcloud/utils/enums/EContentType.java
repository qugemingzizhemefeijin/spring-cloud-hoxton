package com.atguigu.springcloud.utils.enums;

import org.apache.http.entity.ContentType;

/**
 * 请求ContentType常量
 *
 */
public enum EContentType {
	
	TEXT_HTML(ContentType.create("text/html", ECharset.UTF_8.getCharset())),
	TEXT_PLAIN(ContentType.create("text/plain", ECharset.UTF_8.getCharset())),
	TEXT_XML(ContentType.create("text/xml", ECharset.UTF_8.getCharset())),
	APPLICATION_JSON(ContentType.create("application/json", ECharset.UTF_8.getCharset())),
	APPLICATION_FORM_URLENCODED(ContentType.create("application/x-www-form-urlencoded", ECharset.UTF_8.getCharset())),
	APPLICATION_OCTET_STREAM(ContentType.create("application/octet-stream", ECharset.UTF_8.getCharset())),
	APPLICATION_XML(ContentType.create("application/xml", ECharset.UTF_8.getCharset())),
	MULTIPART_FORM_DATA(ContentType.create("multipart/form-data", ECharset.UTF_8.getCharset())),
	APPLICATION_XHTML_XML(ContentType.create("application/xhtml+xml", ECharset.UTF_8.getCharset())),
	;
	
	private final String fullContentType;
	private final String mimeType;
	private final ContentType contentType;
	
	EContentType(ContentType contentType){
		this.contentType = contentType;
		this.mimeType = contentType.getMimeType();
		this.fullContentType = contentType.toString();
	}

	public String getFullContentType() {
		return fullContentType;
	}

	public String getMimeType() {
		return mimeType;
	}

	public ContentType getContentType() {
		return contentType;
	}

	@Override
	public String toString() {
		return this.fullContentType;
	}

}
