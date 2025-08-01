package com.example.ecommerce.seller.inventory.beans;

import java.io.Serializable;

import com.example.ecommerce.utils.CommonBean;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductMasterBean extends CommonBean implements Serializable{
	private static final long serialVersionUID = 7013546239774037178L;
	private Integer productId;
	private String productName;
	private String productDesc;
	private Double cost;
	private Double minPrice;
	private Double maxPrice;
	private Double currentDiscount;
	private String impInfo;
	private String status;
	private String manufacturerName;
	private Integer countryOfOrigin;
	private String itemPartNumber;
	private String productDiamentions;
	private Integer netQty;
	private String countryName;
	private Integer businessId;
	private Integer brandId;
	
	
	
	public Integer getBusinessId() {
		return businessId;
	}
	public void setBusinessId(Integer businessId) {
		this.businessId = businessId;
	}
	public Integer getBrandId() {
		return brandId;
	}
	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductDesc() {
		return productDesc;
	}
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}
	public Double getCost() {
		return cost;
	}
	public void setCost(Double cost) {
		this.cost = cost;
	}
	public Double getMinPrice() {
		return minPrice;
	}
	public void setMinPrice(Double minPrice) {
		this.minPrice = minPrice;
	}
	public Double getMaxPrice() {
		return maxPrice;
	}
	public void setMaxPrice(Double maxPrice) {
		this.maxPrice = maxPrice;
	}
	public Double getCurrentDiscount() {
		return currentDiscount;
	}
	public void setCurrentDiscount(Double currentDiscount) {
		this.currentDiscount = currentDiscount;
	}
	public String getImpInfo() {
		return impInfo;
	}
	public void setImpInfo(String impInfo) {
		this.impInfo = impInfo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getManufacturerName() {
		return manufacturerName;
	}
	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}
	public Integer getCountryOfOrigin() {
		return countryOfOrigin;
	}
	public void setCountryOfOrigin(Integer countryOfOrigin) {
		this.countryOfOrigin = countryOfOrigin;
	}
	public String getItemPartNumber() {
		return itemPartNumber;
	}
	public void setItemPartNumber(String itemPartNumber) {
		this.itemPartNumber = itemPartNumber;
	}
	public String getProductDiamentions() {
		return productDiamentions;
	}
	public void setProductDiamentions(String productDiamentions) {
		this.productDiamentions = productDiamentions;
	}
	public Integer getNetQty() {
		return netQty;
	}
	public void setNetQty(Integer netQty) {
		this.netQty = netQty;
	}
	public ProductMasterBean(Integer productId, String productName, Double cost, Double minPrice, Double maxPrice,
			Double currentDiscount, String status, String countryName) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.cost = cost;
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
		this.currentDiscount = currentDiscount;
		this.status = status;
		this.countryName = countryName;
	}
	
	
	public ProductMasterBean(Integer productId, String productName) {
		super();
		this.productId = productId;
		this.productName = productName;
	}
	public ProductMasterBean() {
		super();
	}
	
	
}
