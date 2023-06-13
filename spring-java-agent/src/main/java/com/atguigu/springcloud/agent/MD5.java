package com.atguigu.springcloud.agent;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class MD5 {

	private MD5() {}
	
	private static char hexs[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String encode(String source) {
		if(source == null) return null;
		
		try {
			char result[];
			MessageDigest digester = MessageDigest.getInstance("MD5");
			byte sbs[] = source.getBytes("UTF8");
			digester.update(sbs);
			byte rbs[] = digester.digest();
			int j = rbs.length;
			result = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte b = rbs[i];
				result[k++] = hexs[b >>> 4 & 15];
				result[k++] = hexs[b & 15];
			}

			return new String(result);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
