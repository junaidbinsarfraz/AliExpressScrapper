package com.aliexperssscrapper.controller;

import java.util.LinkedList;
import java.util.List;

import com.aliexperssscrapper.model.Input;
import com.aliexperssscrapper.model.Product;

public class ScrapperController {
	
	public List<Product> extractProductsFromCategory(Input input) {
		List<Product> products = new LinkedList<>();
		
		// Can goto next page or not?
		
			// Fetch each page (48 products max)
			
				// For each link of products make a url and fetch them
				
				// Extract all info of the a product (call extractProduct() method)
		
		return products;
	}
	
}
