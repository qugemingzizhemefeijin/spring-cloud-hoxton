package com.atguigu.springcloud.utils.http;

/**
 * Http调用后的回调接口
 *
 */
public interface IHttpCallback {
	
	/**
	 * Http调用成功后的回调接口
	 * @param callbackStatus - ResponseCallbackStatus
	 * @throws Exception
	 */
	public void callback(ResponseCallbackStatus callbackStatus) throws Exception;

}
