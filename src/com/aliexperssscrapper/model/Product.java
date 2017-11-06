package com.aliexperssscrapper.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Product implements Serializable {
	
	private String title;
	private String url;
	private String id;
	private String price;
	private String lowPrice;
	private String hightPrice;
	private String orderNumber;
	private List<String> colors;
	private List<String> sizes;
	private Map<String, String> itemSpecs;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getLowPrice() {
		return lowPrice;
	}

	public void setLowPrice(String lowPrice) {
		this.lowPrice = lowPrice;
	}

	public String getHightPrice() {
		return hightPrice;
	}

	public void setHightPrice(String hightPrice) {
		this.hightPrice = hightPrice;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public List<String> getColors() {
		return colors;
	}

	public void setColors(List<String> colors) {
		this.colors = colors;
	}

	public List<String> getSizes() {
		return sizes;
	}

	public void setSizes(List<String> sizes) {
		this.sizes = sizes;
	}

	public Map<String, String> getItemSpecs() {
		return itemSpecs;
	}

	public void setItemSpecs(Map<String, String> itemSpecs) {
		this.itemSpecs = itemSpecs;
	}
	
}
