package com.aliexperssscrapper.util;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.aliexperssscrapper.model.Product;

public class ProductUtil {
	
	public static Product extractProduct(Document document, Response response, String categoryName) {
		
		Product product = new Product();
		
		// Title
		String title = findAndExtractTextByClassName(document, "product-name");
		
		product.setTitle(title);
		
		// Product page url
		product.setUrl(response.url().toString());
		
		//Product Id
		product.setId(getProductId(response.url().toString()));
		
		// TODO: Create new Folder with product Id name, then save Images into the folder
		String productPath = Constants.OUTPUT_DIRECTORY + categoryName + "\\" + title;
		
		Boolean isProductFolderCreated = new File(productPath).mkdir();
		
		if(Boolean.TRUE.equals(isProductFolderCreated)) {
			Elements imageTagElems = document.getElementsByClass("div.ui-box-title:contains(Product Description)+div img");
			
			for(Element imageTagElem : imageTagElems) {
				Connection connection = RequestResponseUtil.makeRequest(imageTagElems.attr("src"));
				
				try {
					Response imageResponse = connection.ignoreContentType(true).execute();
					
					// TODO: Write into the image
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		// Simple price or high/low price
		
		Elements lowPriceElems = document.select("[itemprop=\"lowPrice\"]");
		
		Elements highPriceElems = document.select("[itemprop=highPrice]");
		
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
		product.setColors(extractColorsOrSizes(document, Boolean.TRUE));
		
		// sizes
		product.setSizes(extractColorsOrSizes(document, Boolean.FALSE));
		
		// Item specifics
		product.setItemSpecs(extractItemSpecs(document));
		
		// TODO: Other Types
		
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
	
	private static List<String> extractColorsOrSizes(Document document, Boolean isColor) {
		List<String> colors = new LinkedList<>();
		
		Elements elems = document.getElementsByClass("p-property-item");
		
		for(Element elem : elems) {
			
			Element dtElem = elem.child(0);
			
			if(Util.isNotNull(dtElem) && Util.isNotNullAndEmpty(dtElem.text())) {
				if(dtElem.text().toLowerCase().contains(
						Boolean.TRUE.equals(isColor) ? Constants.COLOR_TITLE.toLowerCase() : 
							Constants.SIZE_TITLE.toLowerCase())) {
					// Next sibling 
					
					Elements linkElems = elem.select("a[data-role=sku]");
					
					for(Element linkElem : linkElems) {
						Element spanElem = linkElem.select("span").first();
						
						if(Util.isNotNull(spanElem)) {
							colors.add(spanElem.text());
						} else {
							Element imgElem = linkElem.select("img").first();
							
							if(Util.isNotNull(imgElem)) {
								colors.add(imgElem.attr("title"));
							}
						}
					}
				}
			}
			
		}
		
		return colors;
	}
	
	private static Map<String, String> extractItemSpecs(Document document) {
		Map<String, String> itemSpecs = new LinkedHashMap<>();
		
		Elements elems = document.getElementsByClass("div.ui-box-title:contains(Item specifics)+div ul.product-property-list li.property-item");
		
		if(Util.isNotNullAndEmpty(elems)) {
			
			for(Element elem : elems) {
				Element titleElem = elem.getElementsByClass("propery-title").first();
				Element desElem = elem.getElementsByClass("propery-des").first();
				
				if(Util.isNotNull(titleElem) && Util.isNotNull(desElem)) {
					itemSpecs.put(titleElem.text(), desElem.text());
				}
			}
			
		}
		
		return itemSpecs;
	}
	
	private static String getProductId(String url) {
		return url.substring(url.lastIndexOf("/"), url.indexOf(".htm"));
	}
	
}
