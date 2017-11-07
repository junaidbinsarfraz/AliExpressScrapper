package com.aliexperssscrapper.controller;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Connection.Request;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;

import com.aliexperssscrapper.model.Input;
import com.aliexperssscrapper.model.Product;
import com.aliexperssscrapper.util.Constants;
import com.aliexperssscrapper.util.CrawlUtil;
import com.aliexperssscrapper.util.ProductUtil;
import com.aliexperssscrapper.util.RequestResponseUtil;
import com.aliexperssscrapper.util.Util;

public class ScrapperController {
	
	public List<Product> extractProductsFromCategory(Input input) {
		List<Product> products = new LinkedList<>();
		
		new File(Constants.OUTPUT_DIRECTORY + input.getCategoryName()).mkdir();
		
		// For first request
		String actionUrl = input.getCategoryUrl();
		Document document = null;
		
		do {
			try {
			
				// Make page request
				Connection connection = RequestResponseUtil.makeRequest(actionUrl);
				
				document = connection.get();
				Request request = connection.request();
				Response response = connection.response();
				
				// Fetch each page (48 products max)
				List<String> productsLink = CrawlUtil.getAllProductsLink(document);
				
				// For each link of products make a url and fetch them
				for(String productLink : productsLink) {
					if(Util.isNotNullAndEmpty(productLink)) {
						
						try {
							
							Connection productConnection = RequestResponseUtil.makeRequest(productLink);
							
							Document productDocument = productConnection.get();
							Request productRequest = productConnection.request();
							Response productResponse = productConnection.response();
							
							// Extract all info of the a product (call extractProduct() method)
							Product product = ProductUtil.extractProduct(productDocument, productResponse);
							
							products.add(product);
							
						} catch (Exception e) {
							
						}
					}
				}
				
			} catch (Exception e) {
				// Log exception
			}

			// Can goto next page or not?
			actionUrl = CrawlUtil.getNextPage(document);
			
		} while(Util.isNotNullAndEmpty(actionUrl));
		
		return products;
	}
	
}
