package com.aliexperssscrapper.model;

import java.io.Serializable;

public class Input implements Serializable {
	
	private String categoryUrl;
	private String categoryName;
	private Integer maxPageNumber;

	public String getCategoryUrl() {
		return categoryUrl;
	}

	public void setCategoryUrl(String categoryUrl) {
		this.categoryUrl = categoryUrl;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Integer getMaxPageNumber() {
		return maxPageNumber;
	}

	public void setMaxPageNumber(Integer maxPageNumber) {
		this.maxPageNumber = maxPageNumber;
	}
	
}
