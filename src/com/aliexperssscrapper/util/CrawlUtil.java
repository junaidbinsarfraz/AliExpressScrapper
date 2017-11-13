package com.aliexperssscrapper.util;

import java.util.LinkedList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * The class CrawlUtil is use to get parse the document
 * 
 * @author Junaid
 */
public class CrawlUtil {

	/**
	 * The method getNextPage() is use to extract the next page url
	 * 
	 * @param document
	 *            to be parsed
	 * @return url if it has next page else null
	 */
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

	/**
	 * The method getAllProductsLink() is use to get All Products' pages Link
	 * from the document
	 * 
	 * @param document
	 *            to be parsed
	 * @return list of links
	 */
	public static List<String> getAllProductsLink(Document document) {
		List<String> productsLink = new LinkedList<>();
		
		Elements elems = document.select(".product");
		
		for(Element elem : elems) {
			productsLink.add(elem.attr("href"));
		}
		
		return productsLink;
	}
	
}
