package com.example.ecommerce.seller.inventory.beans;

import java.util.Date;
import java.util.List;

import com.example.ecommerce.configuration.beans.PaginationCommonBean;

public class ProductSpecificationValueBean extends PaginationCommonBean{

	private Integer productSpecificationValueMasterId;
	private Integer productId;
	private Integer specificationId;
	private String value;
	private String deleted;
	private String status;
	private Integer createdBy;
	private Date createdDate;
	private Integer updatedBy;
	private Date updatedDate;
	private String macId;
	private String ipAddress;
	
	private String productName;
	private String specificationName;
	
	private List<String> values;
	
	private List<ProductSpecificationValueBean> specificationValues;
	
	
	
	public List<ProductSpecificationValueBean> getSpecificationValues() {
		return specificationValues;
	}
	public void setSpecificationValues(List<ProductSpecificationValueBean> specificationValues) {
		this.specificationValues = specificationValues;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getSpecificationName() {
		return specificationName;
	}
	public void setSpecificationName(String specificationName) {
		this.specificationName = specificationName;
	}
	public List<String> getValues() {
		return values;
	}
	public void setValues(List<String> values) {
		this.values = values;
	}
	public Integer getProductSpecificationValueMasterId() {
		return productSpecificationValueMasterId;
	}
	public void setProductSpecificationValueMasterId(Integer productSpecificationValueMasterId) {
		this.productSpecificationValueMasterId = productSpecificationValueMasterId;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public Integer getSpecificationId() {
		return specificationId;
	}
	public void setSpecificationId(Integer specificationId) {
		this.specificationId = specificationId;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDeleted() {
		return deleted;
	}
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	public String getMacId() {
		return macId;
	}
	public void setMacId(String macId) {
		this.macId = macId;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public ProductSpecificationValueBean(Integer productSpecificationValueMasterId,
			String productName, String specificationName, String value,String status) {
		super();
		this.productSpecificationValueMasterId = productSpecificationValueMasterId;
		this.value = value;
		this.status = status;
		this.productName = productName;
		this.specificationName = specificationName;
	}
	
	public ProductSpecificationValueBean(Integer productSpecificationValueMasterId, Integer productId,
			Integer specificationId, String value) {
		super();
		this.productSpecificationValueMasterId = productSpecificationValueMasterId;
		this.productId = productId;
		this.specificationId = specificationId;
		this.value = value;
	}
	public ProductSpecificationValueBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ProductSpecificationValueBean(Integer productSpecificationValueMasterId,
			String specificationName, String value) {
		super();
		this.productSpecificationValueMasterId = productSpecificationValueMasterId;
		this.value = value;
		this.specificationName = specificationName;
	}
	
	
}
