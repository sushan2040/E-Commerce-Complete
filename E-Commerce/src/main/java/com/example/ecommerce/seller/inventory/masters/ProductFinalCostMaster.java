package com.example.ecommerce.seller.inventory.masters;

import java.util.Date;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "product_final_cost_master",schema = "ecommerce")
@Entity
@EntityListeners(value = AuditingEntityListener.class)
public class ProductFinalCostMaster {
	
	@Id
	@Column(name = "product_final_cost_master_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer productFinalCostMasterId;
	@Column(name = "product_id")
	private Integer productId;
	@Column(name = "product_specifications")
	private String productSpecifications;
	@Column(name = "country_id")
	private Integer countryId;
	@Column(name = "cost")
	private Double cost;
	@Column(name = "deleted")
	private String deleted;
	@Column(name = "status")
	private String status;
	@CreatedBy
	@Column(name = "created_by")
	private Integer createdBy;
	@CreatedDate
	@Column(name = "created_date")
	private Date createdDate;
	@LastModifiedBy
	@Column(name = "updated_by")
	private Integer updatedBy;
	@LastModifiedDate
	@Column(name = "updated_date")
	private Date updatedDate;
	@Column(name = "mac_id")
	private String macId;
	@Column(name = "ip_address")
	private String ipAddress;
	public Integer getProductFinalCostMasterId() {
		return productFinalCostMasterId;
	}
	public void setProductFinalCostMasterId(Integer productFinalCostMasterId) {
		this.productFinalCostMasterId = productFinalCostMasterId;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public String getProductSpecifications() {
		return productSpecifications;
	}
	public void setProductSpecifications(String productSpecifications) {
		this.productSpecifications = productSpecifications;
	}
	public Integer getCountryId() {
		return countryId;
	}
	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}
	public Double getCost() {
		return cost;
	}
	public void setCost(Double cost) {
		this.cost = cost;
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
	
	
}
