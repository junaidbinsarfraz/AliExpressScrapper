package com.aliexperssscrapper.util;

import java.net.Proxy;
import java.net.SocketAddress;
import java.net.Proxy.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.http.conn.HttpInetSocketAddress;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class RequestResponseUtil {
	
	private static List<String> proxies = new LinkedList<>();
	
	static {
		proxies.add("23.89.201.190:8800");
		proxies.add("192.157.249.212:8800");
		proxies.add("198.98.101.143:8800");
		proxies.add("172.82.168.157:8800");
		proxies.add("172.82.168.152:8800");
		proxies.add("155.254.32.46:8080");
		proxies.add("23.81.173.236:8800");
		proxies.add("172.245.217.220:8800");
		proxies.add("192.157.253.52:8800");
		proxies.add("172.82.167.40:8800");
	}
	
	public static Connection makeRequest(String url) {
		
		String username = "";
		String password = "";
		
		/*Authenticator.setDefault(
				new Authenticator() {
					public PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(
								username, password.toCharArray());
					}
				});*/
		
		/*System.setProperty("http.proxyHost", "127.0.0.1");
		System.setProperty("http.proxyPort", "8080");
		System.setProperty("http.proxyUser", username);
		System.setProperty("http.proxyPassword", password);*/
		
		// Connection making and proxy setting
		Connection connection = Jsoup.connect(beautifyUrl(url));

		try {
			if (Util.isNotNullAndEmpty(proxies)) {
				String ipPort = proxies.get(generateRandomNumber());
				String[] socketAddress = ipPort.split(":");
				if (socketAddress.length == 2) {
//					connection.proxy(socketAddress[0], Integer.parseInt(socketAddress[1]));
					System.setProperty("http.proxyHost", socketAddress[0]);
					System.setProperty("http.proxyPort", socketAddress[1]);
				}
			}
		} catch (Exception e) {
			System.out.println("Unable to use proxy");
		}
		
		connection.userAgent(Constants.USER_AGENT);
		connection.timeout(Constants.TIME_OUT);
		connection.validateTLSCertificates(Boolean.FALSE);

		return connection;
	}
	
	private static String beautifyUrl(String url) {
		if(Util.isNotNullAndEmpty(url)) {
			if(!url.startsWith("http")) {
				url = "https:" + url;
			}
		}
		
		return url;
	}
	
	private static Integer generateRandomNumber() {
		return 	new Random().nextInt(proxies.size());
	}
	
}
