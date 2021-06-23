package com.shrb.hop.https;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

/**
 * This example demonstrates how to create secure connections with a custom SSL
 * context.
 */

public class HttpsClientDemo {
	
	static String httpurl = "https://devopen.shrbank.com:7443/secapi/user/getVeriCode";
	static String certfile = "/Users/abc/Documents/open.pfx";
	static String pwd = "123456";

	public final static void main(String[] args) throws Exception {

		KeyStore keyStore = KeyStore.getInstance("PKCS12");

		FileInputStream instream = new FileInputStream(new File(certfile));
		try {
			keyStore.load(instream, pwd.toCharArray());
		} finally {
			instream.close();
		}

		// Trust own CA and all self-signed certs
		SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, pwd.toCharArray()).build();

		// Allow TLSv1 protocol only
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1.2" }, null,
				// SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
				SSLConnectionSocketFactory.getDefaultHostnameVerifier());

		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
				// .register("http", PlainConnectionSocketFactory.INSTANCE)
				.register("https", sslsf).build();

		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		cm.setMaxTotal(200);
		cm.setDefaultMaxPerRoute(200);

		CloseableHttpClient httpclient = HttpClients.custom().setConnectionManager(cm).setConnectionManagerShared(true).build();
		CloseableHttpResponse response = null;

		System.out.println("Test start-------------------->");
		try {
			HttpPost post = new HttpPost(httpurl);
			// 设置请求的配置
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(6000).build();
			post.setConfig(requestConfig);
			
			StringEntity se = new StringEntity("msg");
			post.setEntity(se);
			post.addHeader("Content-Type", "application/json; charset=UTF-8");
			
			response = httpclient.execute(post);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
				String text;
				while ((text = bufferedReader.readLine()) != null) {
					System.out.println(text);
				}
			}
			EntityUtils.consume(entity);
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			cm.close();
		}
		System.out.println("Test end<--------------------");
	}

}
