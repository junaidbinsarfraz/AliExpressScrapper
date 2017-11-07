package com.aliexperssscrapper.util;

import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;

import com.aliexperssscrapper.model.Product;

public class ProductUtil {
	
	public static Product extractProduct(Response response) {
		
		Product product = new Product();
		
		// Title
		
		// Product page url
		
		// Save Images into the product Id folder
		
		// Simple price or high/low price
		
		// order numbers
		
		// colors 
		
		// sizes
		
		// Item specifics
		
		return product;
	}
	
	public static Boolean isLastPage(Document document) {
		
		// check if class="ui-pagination-next" has class "ui-pagination-disabled" or not
		
		return Boolean.FALSE;
	}
	
}
