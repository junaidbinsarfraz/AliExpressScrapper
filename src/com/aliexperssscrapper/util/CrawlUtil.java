package com.aliexperssscrapper.util;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class CrawlUtil {
	
	public static Connection makeRequest(String url) {

		// Connection making and proxy setting
		Connection connection = (Connection) Jsoup.connect(url);

		connection.userAgent(Constants.USER_AGENT);
		connection.timeout(Constants.TIME_OUT);
		connection.validateTLSCertificates(Boolean.FALSE);

		return connection;
	}
	
}
