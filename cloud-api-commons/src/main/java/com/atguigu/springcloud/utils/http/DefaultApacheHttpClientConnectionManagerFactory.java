package com.atguigu.springcloud.utils.http;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

public class DefaultApacheHttpClientConnectionManagerFactory implements ApacheHttpClientConnectionManagerFactory {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultApacheHttpClientConnectionManagerFactory.class);

	public HttpClientConnectionManager newConnectionManager(boolean disableSslValidation, int maxTotalConnections, int maxConnectionsPerRoute) {
		return newConnectionManager(disableSslValidation, maxTotalConnections, maxConnectionsPerRoute, -1, TimeUnit.MILLISECONDS, null);
	}

	@Override
	public HttpClientConnectionManager newConnectionManager(boolean disableSslValidation, int maxTotalConnections, int maxConnectionsPerRoute, long timeToLive, TimeUnit timeUnit, RegistryBuilder<ConnectionSocketFactory> registryBuilder) {
		if (registryBuilder == null) {
			registryBuilder = RegistryBuilder.<ConnectionSocketFactory> create().register(HTTP_SCHEME, PlainConnectionSocketFactory.INSTANCE);
		}
		if (disableSslValidation) {
			try {
				final SSLContext sslContext = SSLContext.getInstance("SSL");
				sslContext.init(null, new TrustManager[] { new DisabledValidationTrustManager()}, new SecureRandom());
				registryBuilder.register(HTTPS_SCHEME, new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE));
			}
			catch (NoSuchAlgorithmException e) {
				LOGGER.warn("Error creating SSLContext", e);
			}
			catch (KeyManagementException e) {
				LOGGER.warn("Error creating SSLContext", e);
			}
		} else {
			registryBuilder.register("https", SSLConnectionSocketFactory.getSocketFactory());
		}
		final Registry<ConnectionSocketFactory> registry = registryBuilder.build();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry, null, null, null, timeToLive, timeUnit);
		connectionManager.setMaxTotal(maxTotalConnections);
		connectionManager.setDefaultMaxPerRoute(maxConnectionsPerRoute);

		return connectionManager;
	}

	class DisabledValidationTrustManager implements X509TrustManager {
		@Override
		public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}

}
