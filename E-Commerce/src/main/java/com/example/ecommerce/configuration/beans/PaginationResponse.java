package com.example.ecommerce.configuration.beans;

import java.io.Serializable;
import java.util.List;

public class PaginationResponse<T> implements Serializable{

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

