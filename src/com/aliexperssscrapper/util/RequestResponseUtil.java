package com.aliexperssscrapper.util;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class RequestResponseUtil {
	
	public static Connection makeRequest(String url) {

		// Connection making and proxy setting
		Connection connection = (Connection) Jsoup.connect(beautifyUrl(url));

		connection.userAgent(Constants.USER_AGENT);
		connection.timeout(Constants.TIME_OUT);
		connection.validateTLSCertificates(Boolean.FALSE);

		return connection;
	}
	
	private static String beautifyUrl(String url) {
		if(Util.isNotNullAndEmpty(url)) {
			if(!url.startsWith("http:") || !url.startsWith("https:")) {
				url = "https:" + url;
			}
		}
		
		return url;
	}
	
}
