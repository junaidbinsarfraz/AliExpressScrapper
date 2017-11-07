package com.aliexperssscrapper.util;

import java.util.LinkedList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlUtil {
	
	public static String getNextPage(Document document) {
		
		if(Util.isNotNull(document)) {
			
			// check if class="ui-pagination-next" has class "ui-pagination-disabled" or not
			Elements elems = document.getElementsByClass("ui-pagination-next");
			
			if(Util.isNotNull(elems)) {
				Element elem = elems.first();
				
				if(elem.hasClass("ui-pagination-disabled")) {
					return null;
				} else {
					return elem.attr("href");
				}
			}
		
		}
		
		// Null if last page else action url
		return null;
	}
	
	public static List<String> getAllProductsLink(Document document) {
		List<String> productsLink = new LinkedList<>();
		
		Elements elems = document.getElementsByClass("product");
		
		for(Element elem : elems) {
			productsLink.add(elem.attr("href"));
		}
		
		return productsLink;
	}
	
}
