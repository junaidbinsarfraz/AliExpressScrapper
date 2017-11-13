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
	
	private static List<String> ignoredTitles = new LinkedList<>();
	
	static {
		ignoredTitles.add("Shipping");
		ignoredTitles.add("Quantity");
		ignoredTitles.add("Total Price");
		ignoredTitles.add("Ships From");
		ignoredTitles.add("Fit");
		ignoredTitles.add("Services");
		ignoredTitles.add("Store Promotion");
		ignoredTitles.add("Payment");
	}

	/**
	 * The method extractProduct() is use to extract all the details related to
	 * given product
	 * 
	 * @param document
	 *            product html document to be parsed
	 * @param response
	 *            of Request
	 * @param categoryName
	 *            to determine the folder path
	 * @return product details
	 */
	public static Product extractProduct(Document document, Response response, String categoryName) {
		
		Product product = new Product();
		
		// Title
		String title = findAndExtractTextByClassName(document, "product-name");
		
		product.setTitle(title);
		
		// Product page url
		product.setUrl(response.url().toString());
		
		//Product Id
		String productId = getProductId(response.url().toString());
		
		product.setId(productId);
		
		// TODO: Create new Folder with product Id name, then save Images into the folder
		String productPath = Constants.OUTPUT_DIRECTORY + categoryName + "\\" + productId;
		
		Boolean isProductFolderCreated = new File(productPath).mkdir();
		
		if(Boolean.TRUE.equals(isProductFolderCreated)) {
			Elements imageTagElems = document.select(".image-thumb-list .img-thumb-item img");
			
			Integer imagesCount = 1;
			
			for(Element imageTagElem : imageTagElems) {
				String imageUrl = imageTagElem.attr("src");
				imageUrl = imageUrl.replaceAll(Constants.SMALL_IMAGE_REPLACE_REGEX, "");
				
				Connection connection = RequestResponseUtil.makeRequest(imageUrl);
				
				try {
					Response imageResponse = connection.ignoreContentType(true).execute();
					
					// TODO: Write into the image
					FileUtil.saveImage(imageResponse, productPath + "\\" + imagesCount++ + ".jpg", imageUrl);
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
//		product.setColors(extractItemSpecs(document));
		
		// sizes
//		product.setSizes(extractItemSpecs(document));
		
		// Item specifics
		product.setItemSpecs(extractItemSpecs(document));
		
		// TODO: Other Types
		product.setOtherSpecs(extractItemOtherSpecs(document));
		
		return product;
	}

	/**
	 * The method findAndExtractTextByClassName() is use to extract text of
	 * respective class name Element
	 * 
	 * @param document
	 *            to be parsed
	 * @param className
	 *            class name
	 * @return text written in the class named Element
	 */
	private static String findAndExtractTextByClassName(Document document, String className) {
		String text = null;
		
		Elements elems = document.getElementsByClass(className);
		
		if(Boolean.TRUE.equals(elems.size() > 0)) {
			return elems.first().text();
		}
		
		return text;
	}

	/**
	 * The method extractItemOtherSpecs() is use to extract Item's Other Specs
	 * 
	 * @param document
	 *            to be parsed
	 * @return parsed titles with values
	 */
	private static Map<String, List<String>> extractItemOtherSpecs(Document document) {
		Map<String, List<String>> specs = new LinkedHashMap<>();
		
		Elements elems = document.getElementsByClass("p-property-item");
		
		for(Element elem : elems) {
			
			Element dtElem = elem.child(0);
			
			if(Util.isNotNull(dtElem) && isValidTitle(dtElem.text())) {
				// Next sibling 
				
				Elements linkElems = elem.select("a[data-role=sku]");
				
				List<String> specValues = new LinkedList<>();
				
				for(Element linkElem : linkElems) {
					Element spanElem = linkElem.select("span").first();
					
					if(Util.isNotNull(spanElem)) {
						specValues.add(spanElem.text());
//						specs.put(dtElem.text().replaceAll(":", ""), spanElem.text());
					} else {
						Element imgElem = linkElem.select("img").first();
						
						if(Util.isNotNull(imgElem)) {
							specValues.add(imgElem.attr("title"));
//							specs.put(dtElem.text().replaceAll(":", ""), imgElem.attr("title"));
						}
					}
				}
				
				specs.put(dtElem.text().replaceAll(":", ""), specValues);
			}
			
		}
		
		return specs;
	}
	
	/**
	 * The method isValidTitle() is use to check the title against ignoredTitles
	 * for its validity
	 * 
	 * @param title
	 *            to be checked
	 * @return true if valid else false
	 */
	private static Boolean isValidTitle(String title) {
		if(Util.isNullOrEmpty(title)) {
			return Boolean.FALSE;
		}
		
		for(String ignoredTitle : ignoredTitles) {
			if(title.toLowerCase().contains(ignoredTitle.toLowerCase())) {
				return Boolean.FALSE;
			}
		}
		
		return Boolean.TRUE;
	}

	/**
	 * The method extractItemSpecs() is use to extract Item's Specs from given
	 * document
	 * 
	 * @param document
	 *            to be parsed
	 * @return parsed titles with values
	 */
	private static Map<String, String> extractItemSpecs(Document document) {
		Map<String, String> itemSpecs = new LinkedHashMap<>();
		
		Elements elems = document.select("div.ui-box-title:contains(Item specifics)+div ul.product-property-list li.property-item");
		
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

	/**
	 * The method getProductId() is use to extract product id from url
	 * 
	 * @param url
	 *            to be parsed
	 * @return product id
	 */
	private static String getProductId(String url) {
		return url.substring(url.lastIndexOf("/") + 1, url.indexOf(".htm"));
	}
	
}
