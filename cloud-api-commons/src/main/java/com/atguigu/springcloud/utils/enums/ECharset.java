package com.atguigu.springcloud.utils.enums;

import java.nio.charset.Charset;

/**
 * 字符集常量
 *
 */
public enum ECharset {
	
	UTF_8(Charset.forName("UTF-8")),
	ASCII(Charset.forName("US-ASCII")),
	ISO_8859_1(Charset.forName("ISO-8859-1")),
	GBK(Charset.forName("GBK")),
	GB2312(Charset.forName("GB2312")),
	;
    
    private final String name;
    private final Charset charset;

	ECharset(Charset charset){
		this.name = charset.name();
		this.charset = charset;
	}
    
    public String getName() {
		return name;
	}

	public Charset getCharset() {
		return charset;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
