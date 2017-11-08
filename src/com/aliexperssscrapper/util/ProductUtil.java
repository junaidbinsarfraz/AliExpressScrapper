package com.aliexperssscrapper.util;

import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.aliexperssscrapper.model.Product;

public class ProductUtil {
	
	public static Product extractProduct(Document document, Response response) {
		
		Product product = new Product();
		
		// Title
		String title = findAndExtractTextByClassName(document, "product-name");
		
		product.setTitle(title);
		
		// Product page url
		product.setUrl(response.url().toString());
		
		// TODO: Create new Folder with product Id name, then save Images into the folder
//		new File("/path/to/folder").mkdir();
		
		// Simple price or high/low price
		
		Elements lowPriceElems = document.select("[itemprop=\"lowPrice\"]");
		
		Elements highPriceElems = document.select("[itemprop=\"highPrice\"]");
		
		if(Util.isNotNull(lowPriceElems) && lowPriceElems.size() > 0 
				&& Util.isNotNull(highPriceElems) && highPriceElems.size() > 0) {
			// High/low price
			
			product.setHightPrice(highPriceElems.first().text());
			product.setLowPrice(lowPriceElems.first().text());
			
		} else {
			// Simple price
			String simplePrice = findAndExtractTextByClassName(document, "p-price");
			
			product.setPrice(simplePrice);
		}
		
		// order numbers
		Element orderElem = document.getElementById("j-order-num");
		
		if(Util.isNotNull(orderElem)) {
			product.setOrderNumber(orderElem.text());
		}
		
		// colors 
		
		
		// sizes
		
		// Item specifics
		
		return product;
	}
	
	private static String findAndExtractTextByClassName(Document document, String className) {
		String text = null;
		
		Elements elems = document.getElementsByClass(className);
		
		if(Boolean.TRUE.equals(elems.size() > 0)) {
			return elems.first().text();
		}
		
		return text;
	}
	
}
