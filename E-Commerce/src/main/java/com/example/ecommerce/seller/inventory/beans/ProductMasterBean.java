package com.example.ecommerce.seller.inventory.beans;

import java.io.Serializable;
import java.util.List;

import com.example.ecommerce.seller.inventory.masters.ProductImages;
import com.example.ecommerce.seller.inventory.masters.ProductMaster;
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
	private Integer productCategoryId;
	private String currencySymbol;
	private List<ProductMasterBean> products;
	private String productCategoryCode;
	private String productCategoryDesc;
	private List<ProductImages> productImages;
	List<ProductImagesBean> beanList;
	List<ProductSpecificationValueBean> specificationList;
	private Integer productFinalCostMasterId;
	 
	
	
	
	public String getCurrencySymbol() {
		return currencySymbol;
	}
	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}
	public Integer getProductFinalCostMasterId() {
		return productFinalCostMasterId;
	}
	public void setProductFinalCostMasterId(Integer productFinalCostMasterId) {
		this.productFinalCostMasterId = productFinalCostMasterId;
	}
	public List<ProductSpecificationValueBean> getSpecificationList() {
		return specificationList;
	}
	public void setSpecificationList(List<ProductSpecificationValueBean> specificationList) {
		this.specificationList = specificationList;
	}
	public String getProductCategoryDesc() {
		return productCategoryDesc;
	}
	public void setProductCategoryDesc(String productCategoryDesc) {
		this.productCategoryDesc = productCategoryDesc;
	}
	public List<ProductImages> getProductImages() {
		return productImages;
	}
	public void setProductImages(List<ProductImages> productImages) {
		this.productImages = productImages;
	}
	public String getProductCategoryCode() {
		return productCategoryCode;
	}
	public void setProductCategoryCode(String productCategoryCode) {
		this.productCategoryCode = productCategoryCode;
	}
	public List<ProductMasterBean> getProducts() {
		return products;
	}
	public void setProducts(List<ProductMasterBean> products2) {
		this.products = products2;
	}
	public Integer getProductCategoryId() {
		return productCategoryId;
	}
	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}
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
	
	
	
	public ProductMasterBean(Integer productId, String productName, String productDesc, Double cost, Double minPrice,
			Double maxPrice, Double currentDiscount, String impInfo, String status, String manufacturerName,
			Integer countryOfOrigin, String itemPartNumber, String productDiamentions, Integer netQty,
			String countryName, Integer businessId, Integer brandId, Integer productCategoryId) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.productDesc = productDesc;
		this.cost = cost;
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
		this.currentDiscount = currentDiscount;
		this.impInfo = impInfo;
		this.status = status;
		this.manufacturerName = manufacturerName;
		this.countryOfOrigin = countryOfOrigin;
		this.itemPartNumber = itemPartNumber;
		this.productDiamentions = productDiamentions;
		this.netQty = netQty;
		this.countryName = countryName;
		this.businessId = businessId;
		this.brandId = brandId;
		this.productCategoryId = productCategoryId;
	}
	public ProductMasterBean(Integer productId, String productName) {
		super();
		this.productId = productId;
		this.productName = productName;
	}
	public ProductMasterBean() {
		super();
	}
	public ProductMasterBean(Integer productId, String productName, Double cost,
			List<ProductImages> productImages) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.cost = cost;
		this.productImages = productImages;
	}
	public ProductMasterBean(Integer productId, String productName, Double cost,
			List<ProductImages> productImages,Integer productFinalCostMasterId) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.cost = cost;
		this.productImages = productImages;
		this.productFinalCostMasterId=productFinalCostMasterId;
	}
	public ProductMasterBean(Integer productId, String productName, Double cost,
			List<ProductImages> productImages,String productDesc,String currencySymbol) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.cost = cost;
		this.productImages = productImages;
		this.productDesc=productDesc;
		this.currencySymbol=currencySymbol;
	}
	public ProductMasterBean(Integer productId, String productName, Double cost,
			String productDesc,String currencySymbol) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.cost = cost;
		this.productDesc=productDesc;
		this.currencySymbol=currencySymbol;
	}
	public List<ProductImagesBean> getBeanList() {
		return beanList;
	}
	public void setBeanList(List<ProductImagesBean> beanList) {
		this.beanList = beanList;
	}
	
	
	
}
