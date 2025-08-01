package com.example.ecommerce.configuration.beans;

import java.util.List;

public class PaginationResponse<T> {

    private int page;

    private int totalPages;

    private List<T> data;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}
    // Constructor, Getters, and Setters
    
}

