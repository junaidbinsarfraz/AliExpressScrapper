package com.aliexperssscrapper.controller;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.jsoup.Connection;
import org.jsoup.Connection.Request;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;

import com.aliexperssscrapper.gui.GUI;
import com.aliexperssscrapper.model.Input;
import com.aliexperssscrapper.model.Product;
import com.aliexperssscrapper.util.Constants;
import com.aliexperssscrapper.util.CrawlUtil;
import com.aliexperssscrapper.util.ProductUtil;
import com.aliexperssscrapper.util.RequestResponseUtil;
import com.aliexperssscrapper.util.Util;

/**
 * The class ScrapperController is use to scrap each category's products
 * 
 * @author Junaid
 */
public class ScrapperController {

	/**
	 * The method extractProductsFromCategory() is use to extract all the
	 * products of given category.
	 * 
	 * @param input
	 *            contains url, category name and max page to be scrapped
	 * @return list of products
	 */
	public List<Product> extractProductsFromCategory(Input input) {
		List<Product> products = new LinkedList<>();
		
		File categoryFolder = new File(GUI.outputDirectory.getAbsolutePath() + "\\" + input.getCategoryName());
		
		Boolean isCategoryFolderCreated = Boolean.TRUE;
		
		if (Boolean.TRUE.equals(categoryFolder.exists())) {
			try {
				FileUtils.deleteDirectory(categoryFolder);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		isCategoryFolderCreated = categoryFolder.mkdir();
		
		if(Boolean.TRUE.equals(isCategoryFolderCreated)) {
			
			// For first request
			String actionUrl = input.getCategoryUrl();
			Document document = null;
			Integer parsedPageCount = 0;
			
			while(Util.isNotNullAndEmpty(actionUrl) && (Util.isNull(input.getMaxPageNumber()) || parsedPageCount++ < input.getMaxPageNumber())) {
				try {
					
					// Add delay
					TimeUnit.SECONDS.sleep(GUI.delay);
					
					// Make page request
					Connection connection = RequestResponseUtil.makeRequest(actionUrl);
					
					document = connection.get();
					Request request = connection.request();
					Response response = connection.response();
					
					System.out.println("Connection to : " + actionUrl);
					
					// Fetch each page (48 products max)
					List<String> productsLink = CrawlUtil.getAllProductsLink(document);
					
					// For each link of products make a url and fetch them
					for(String productLink : productsLink) {
						if(Util.isNotNullAndEmpty(productLink)) {
							
							try {
								
								// Add delay
								TimeUnit.SECONDS.sleep(GUI.delay);
								
								Connection productConnection = RequestResponseUtil.makeRequest(productLink);
								
								Document productDocument = productConnection.get();
								Request productRequest = productConnection.request();
								Response productResponse = productConnection.response();
								
								System.out.println("Connection to : " + productLink);
								
								// Extract all info of the a product (call extractProduct() method)
								Product product = ProductUtil.extractProduct(productDocument, productResponse, input.getCategoryName());
								
								products.add(product);
								
							} catch (Exception e) {
								System.out.println(e);
							}
						}
					}
					
				} catch (Exception e) {
					// Log exception
					System.out.println(e);
				}
	
				// Can goto next page or not?
				actionUrl = CrawlUtil.getNextPage(document);
				
			}
		}
		
		return products;
	}
	
}
