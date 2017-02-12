package org.platform.utils.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.HttpHost;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtils extends HttpUtils {

	private final static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);
	
	private static PoolingHttpClientConnectionManager connManager = null;
	
	private static CloseableHttpClient httpclient = null;
	
	public final static int connectTimeout = 5000;
	
	public final static int soTimeout = 5000;
	
	static {
		try {
			SSLContext sslContext = SSLContexts.custom().useTLS().build();
			sslContext.init(null, new TrustManager[] { new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs,
						String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs,
						String authType) {
				}
			} }, null);
			
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
					.<ConnectionSocketFactory> create()
					.register("http", PlainConnectionSocketFactory.INSTANCE)
					.register("https",new SSLConnectionSocketFactory(sslContext)).build();
			
			connManager = new PoolingHttpClientConnectionManager(
					socketFactoryRegistry);
			
			httpclient = HttpClients.custom().setConnectionManager(connManager).build();
			
			// Create socket configuration
			SocketConfig socketConfig = SocketConfig.custom()
					.setTcpNoDelay(true).build();
			connManager.setDefaultSocketConfig(socketConfig);
			// Create message constraints
			MessageConstraints messageConstraints = MessageConstraints.custom()
					.setMaxHeaderCount(200).setMaxLineLength(2000).build();
			// Create connection configuration
			ConnectionConfig connectionConfig = ConnectionConfig.custom()
					.setMalformedInputAction(CodingErrorAction.IGNORE)
					.setUnmappableInputAction(CodingErrorAction.IGNORE)
					.setCharset(Consts.UTF_8)
					.setMessageConstraints(messageConstraints).build();
			connManager.setDefaultConnectionConfig(connectionConfig);
			connManager.setMaxTotal(200);
			connManager.setDefaultMaxPerRoute(20);
		} catch (KeyManagementException e) {
			logger.error("KeyManagementException", e);
		} catch (NoSuchAlgorithmException e) {
			logger.error("NoSuchAlgorithmException", e);
		}
	}
	
	@SuppressWarnings("unused")
	public static HttpClientBuilder getInstanceClientBuilder(boolean isNeedProxy, CookieStore store, HttpHost host, HttpRequestRetryHandler handler, String userAgent) {  
		SSLContextBuilder context_b = new SSLContextBuilder();
//        org.apache.http.ssl.SSLContextBuilder context_b = SSLContextBuilder.create();  
        SSLContext ssl_context = null;  
        try {  
//            context_b.loadTrustMaterial(null, (x509Certificates, s) -> true);  
            //信任所有证书，解决https证书问题  
            ssl_context = context_b.build();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        ConnectionSocketFactory sslSocketFactory = null;  
        Registry<ConnectionSocketFactory> registry = null;  
        if (ssl_context != null) {  
//            sslSocketFactory = new SSLConnectionSocketFactory(ssl_context, new String[]{"TLSv1", "TLSv1.1", "TLSv1.2"}, null, (s, sslSession) -> true);  
//            //应用多种tls协议，解决偶尔握手中断问题  
//            registry = RegistryBuilder.<ConnectionSocketFactory>create().register("https", sslSocketFactory).register("http", new PlainConnectionSocketFactory()).build();  
        }  
        PoolingHttpClientConnectionManager manager = null;  
        if (registry != null) {  
            manager = new PoolingHttpClientConnectionManager(registry);  
        } else {  
            manager = new PoolingHttpClientConnectionManager();  
        }  
        manager.setMaxTotal(150);  
        manager.setDefaultMaxPerRoute(200);  
        HttpClientBuilder builder = HttpClients.custom().setRetryHandler(handler)  
//                .setConnectionTimeToLive(6000, TimeUnit.SECONDS)  
                .setUserAgent(userAgent);  
        if (store != null) {  
            builder.setDefaultCookieStore(store);  
        }  
        if (isNeedProxy && host != null) {  
//            HttpHost proxy = new HttpHost("127.0.0.1", 1080);// 代理ip  
//            DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(host);  
//            builder = builder.setRoutePlanner(routePlanner);  
        }  
        builder.setConnectionManager(manager);//httpclient连接池  
//        builder.setRedirectStrategy(new AllowAllRedirectStrategy());//默认重定向所有302和307，否则httpclient只自动处理get请求导致的302和307  
        return builder;  
    }  
	
	/**
	  * HTTP请求，默认超时为5S
	  * @param url
	  * @return
	 */
	public static String post(String url, String encode) {
		return post(url, null, connectTimeout, encode);
	}
	
	/**
	  * HTTP请求，默认超时为5S
	  * @param url
	  * @param params
	  * @return
	 */
	public static String post(String url, Map<String, String> params, String encode) {
		return post(url, params, connectTimeout, encode);
	}
	
	/**
	  * HTTP请求
	  * @param url
	  * @param params
	  * @param connectTimeout
	  * @return
	 */
	public static String post(String url, Map<String, String> params, int connectTimeout, String encode) {
		String responseContent = null;
		HttpPost httpPost = new HttpPost(url);
		try {
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(connectTimeout)
					.setConnectTimeout(connectTimeout)
					.setConnectionRequestTimeout(connectTimeout).build();
			List<NameValuePair> formParams = new ArrayList<NameValuePair>();
			if (null == encode) encode = ENCODE_UTF8;
			httpPost.setEntity(new UrlEncodedFormEntity(formParams, Charset.forName(encode)));
			httpPost.setConfig(requestConfig);
			// 绑定到请求 Entry
			if (null != params && params.size() > 0) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
			}
			CloseableHttpResponse response = httpclient.execute(httpPost);
			try {
				// 执行POST请求
				HttpEntity entity = response.getEntity(); // 获取响应实体
				try {
					if (null != entity) {
						responseContent = EntityUtils.toString(entity, Charset.forName(encode));
					}
				} finally {
					if (entity != null) {
						entity.getContent().close();
					}
				}
			} finally {
				if (response != null) {
					response.close();
				}
			}
			logger.info("requestURI : " + httpPost.getURI() + ", responseContent: " + responseContent);
		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException", e);
		} catch (IOException e) {
			logger.error("IOException", e);
		} finally {
			httpPost.releaseConnection();
		}
		return responseContent;
	}
	
	public static String get(String url, String encode) {
		return get(url, null, encode, connectTimeout, soTimeout);
	}
	
	public static String get(String url, Map<String, String> params, String encode) {
		return get(url, params, encode, connectTimeout, soTimeout);
	}
	
	@SuppressWarnings("deprecation")
	public static String get(String url, Map<String, String> params,
			String encode, int connectTimeout, int soTimeout) {
		String responseString = null;
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(connectTimeout)
				.setConnectTimeout(connectTimeout)
				.setConnectionRequestTimeout(connectTimeout).build();
		StringBuilder sb = new StringBuilder(url);
		int i = 0;
		if (null != params && params.size() > 0) {
			for (Entry<String, String> entry : params.entrySet()) {
				if (i == 0 && !url.contains("?")) {
					sb.append("?");
				} else {
					sb.append("&");
				}
				sb.append(entry.getKey());
				sb.append("=");
				String value = entry.getValue();
				try {
					sb.append(URLEncoder.encode(value, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					logger.warn("encode http get params error, value is " + value, e);
					sb.append(URLEncoder.encode(value));
				}
				i++;
			}
		}
		logger.info("[HttpClientUtils Get] begin invoke:" + sb.toString());
		HttpGet get = new HttpGet(sb.toString());
		get.setConfig(requestConfig);
		try {
			CloseableHttpResponse response = httpclient.execute(get);
			try {
				HttpEntity entity = response.getEntity();
				try {
					if (entity != null) {
						responseString = EntityUtils.toString(entity, encode);
					}
				} finally {
					if (entity != null) {
						entity.getContent().close();
					}
				}
			} catch (Exception e) {
				logger.error(String.format("[HttpClientUtils Get]get response error, url:%s", sb.toString()), e);
				return responseString;
			} finally {
				if (response != null) {
					response.close();
				}
			}
			logger.info(String.format("[HttpClientUtils Get]Debug url:%s , response string %s:", sb.toString(), responseString));
		} catch (SocketTimeoutException e) {
			logger.error(String.format("[HttpClientUtils Get]invoke get timout error, url:%s", sb.toString()), e);
			return responseString;
		} catch (Exception e) {
			logger.error(String.format("[HttpClientUtils Get]invoke get error, url:%s", sb.toString()), e);
		} finally {
			get.releaseConnection();
		}
		return responseString;
	}

}
