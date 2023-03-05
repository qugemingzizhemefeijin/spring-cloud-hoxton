package com.atguigu.springcloud.utils.http;

import java.io.File;
import java.io.InputStream;

/**
 * Http文件
 *
 */
public class HttpFileEntity {
	
	/**
	 * 上传类型为文件
	 */
	public static final int FILE_TYPE_FILE = 0;
	
	/**
	 * 上传类型为byte[]
	 */
	public static final int FILE_TYPE_BYTES = 1;
	
	/**
	 * 上传类型为InputStream
	 */
	public static final int FILE_TYPE_INPUTSTREAM = 2;
	
	/**
	 * 文件类型
	 */
	private int fileType;
	
	/**
	 * 参数名称
	 */
	private String name;
	
	/**
	 * 文件名称
	 */
	private String fileName;
	
	/**
	 * File对象
	 */
	private File file;
	
	/**
	 * byte[]数组
	 */
	private byte[] fileByte;
	
	/**
	 * 流
	 */
	private InputStream inputStream;
	
	public HttpFileEntity(String name , String fileName , File file) {
		this.name = name;
		this.fileName = (fileName == null ? file.getName() : fileName);
		this.file = file;
		this.fileType = FILE_TYPE_FILE;
	}
	
	public HttpFileEntity(String name , File file) {
		this.name = name;
		this.fileName = file.getName();
		this.file = file;
		this.fileType = FILE_TYPE_FILE;
	}
	
	public HttpFileEntity(String name , String fileName , byte[] fileByte) {
		this.name = name;
		this.fileName = fileName;
		this.fileByte = fileByte;
		this.fileType = FILE_TYPE_BYTES;
	}
	
	public HttpFileEntity(String name , String fileName , InputStream inputStream) {
		this.name = name;
		this.fileName = fileName;
		this.inputStream = inputStream;
		this.fileType = FILE_TYPE_INPUTSTREAM;
	}

	public int getFileType() {
		return fileType;
	}

	public String getFileName() {
		return fileName;
	}

	public File getFile() {
		return file;
	}

	public byte[] getFileByte() {
		return fileByte;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public String getName() {
		return name;
	}

}
