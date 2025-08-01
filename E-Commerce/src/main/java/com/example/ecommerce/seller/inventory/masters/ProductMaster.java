package com.example.ecommerce.seller.inventory.masters;

import java.util.List;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.ecommerce.utils.CommonEntity;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Table(name = "product_master",schema = "ecommerce")
@Entity
@EntityListeners(value = AuditingEntityListener.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductMaster extends CommonEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	private Integer productId;
	@Column(name = "product_name")
	private String productName;
	@Column(name = "product_category_id")
	private Integer productCategoryId;
	@Column(name = "product_desc")
	private String productDesc;
	@Column(name = "cost")
	private Double cost;
	@Column(name = "min_price")
	private Double minPrice;
	@Column(name = "max_price")
	private Double maxPrice;
	@Column(name = "current_discount")
	private Double currentDiscount;
	@Column(name = "imp_info")
	private String impInfo;
	@Column(name = "status")
	private String status;
	@Column(name = "manufacturer")
	private String manufacturerName;
	@Column(name = "country_of_origin")
	private Integer countryOfOrigin;
	@Column(name = "item_part_number")
	private String itemPartNumber;
	@Column(name = "product_diamensions")
	private String productDiamentions;
	@Column(name = "net_qty")
	private Integer netQty;
	@Column(name = "business_id")
	private Integer businessId;
	@Column(name = "brand_id")
	private Integer brandId;
	
	// One-to-many mapping with ProductImages
    @OneToMany(mappedBy = "productMaster", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductImages> productImages;
    
    
	
	public List<ProductImages> getProductImages() {
		return productImages;
	}
	public void setProductImages(List<ProductImages> productImages) {
		this.productImages = productImages;
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
	public Integer getProductCategoryId() {
		return productCategoryId;
	}
	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}
	
	
	
}
