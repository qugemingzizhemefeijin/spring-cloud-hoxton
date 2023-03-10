package com.atguigu.springcloud.utils.http;

import java.util.concurrent.TimeUnit;

/**
 * HttpClient 属性设置
 *
 */
public class HttpClientProperties {

	public static final boolean DEFAULT_DISABLE_SSL_VALIDATION = false;
	public static final int DEFAULT_MAX_CONNECTIONS = 200;
	public static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 50;
	public static final long DEFAULT_TIME_TO_LIVE = 900L;
	public static final TimeUnit DEFAULT_TIME_TO_LIVE_UNIT = TimeUnit.SECONDS;
	public static final boolean DEFAULT_FOLLOW_REDIRECTS = true;
	public static final int DEFAULT_CONNECTION_TIMEOUT = 10000;
	public static final int DEFAULT_CONNECTION_TIMER_REPEAT = 3000;

	/**
	 * 禁用SSL校验，默认false
	 */
	private boolean disableSslValidation = DEFAULT_DISABLE_SSL_VALIDATION;
	
	/**
	 * 连接池最大连接数，默认200
	 */
	private int maxConnections = DEFAULT_MAX_CONNECTIONS;
	
	/**
	 * /每一个IP最大占用多少连接 默认 50
	 */
	private int maxConnectionsPerRoute = DEFAULT_MAX_CONNECTIONS_PER_ROUTE;
	
	/**
	 * 默认的存活时间，默认900秒
	 */
	private long timeToLive = DEFAULT_TIME_TO_LIVE;
	
	/**
	 * 连接池中存活时间单位，默认为秒
	 */
	private TimeUnit timeToLiveUnit = DEFAULT_TIME_TO_LIVE_UNIT;
	
	/**
	 * http请求是否允许重定向
	 */
	private boolean followRedirects = DEFAULT_FOLLOW_REDIRECTS;
	
	/**
	 * 默认连接超时时间：10000毫秒
	 */
	private int connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;
	
	/**
	 * 连接池管理定时器执行频率：默认 3000毫秒
	 */
	private int connectionTimerRepeat = DEFAULT_CONNECTION_TIMER_REPEAT;

	public int getConnectionTimerRepeat() {
		return connectionTimerRepeat;
	}

	public void setConnectionTimerRepeat(int connectionTimerRepeat) {
		this.connectionTimerRepeat = connectionTimerRepeat;
	}

	public boolean isDisableSslValidation() {
		return disableSslValidation;
	}

	public void setDisableSslValidation(boolean disableSslValidation) {
		this.disableSslValidation = disableSslValidation;
	}

	public int getMaxConnections() {
		return maxConnections;
	}

	public void setMaxConnections(int maxConnections) {
		this.maxConnections = maxConnections;
	}

	public int getMaxConnectionsPerRoute() {
		return maxConnectionsPerRoute;
	}

	public void setMaxConnectionsPerRoute(int maxConnectionsPerRoute) {
		this.maxConnectionsPerRoute = maxConnectionsPerRoute;
	}

	public long getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(long timeToLive) {
		this.timeToLive = timeToLive;
	}

	public TimeUnit getTimeToLiveUnit() {
		return timeToLiveUnit;
	}

	public void setTimeToLiveUnit(TimeUnit timeToLiveUnit) {
		this.timeToLiveUnit = timeToLiveUnit;
	}

	public boolean isFollowRedirects() {
		return followRedirects;
	}

	public void setFollowRedirects(boolean followRedirects) {
		this.followRedirects = followRedirects;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

}
