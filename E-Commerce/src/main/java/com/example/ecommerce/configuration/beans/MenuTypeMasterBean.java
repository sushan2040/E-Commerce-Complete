package com.example.ecommerce.configuration.beans;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuTypeMasterBean extends PaginationCommonBean implements Serializable{

	private Integer menuTypeId;
	private String menuTypeName;
	@JsonIgnore
	private String status;
	@JsonIgnore
	private String deleted;
	@JsonIgnore
	private Integer createdBy;
	@JsonIgnore
	private Date createdDate;
	@JsonIgnore
	private Integer updatedBy;
	@JsonIgnore
	private Date updatedDate;
	@JsonIgnore
	private String macAdress;
	@JsonIgnore
	private String ipAddress;
	
	@JsonGetter("menuTypeId")
	public Integer getMenuTypeId() {
		return menuTypeId;
	}
	public void setMenuTypeId(Integer menuTypeId) {
		this.menuTypeId = menuTypeId;
	}
	@JsonGetter("menuTypeName")
	public String getMenuTypeName() {
		return menuTypeName;
	}
	public void setMenuTypeName(String menuTypeName) {
		this.menuTypeName = menuTypeName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDeleted() {
		return deleted;
	}
	public void setDeleted(String deleted) {
		this.deleted = deleted;
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
	public String getMacAdress() {
		return macAdress;
	}
	public void setMacAdress(String macAdress) {
		this.macAdress = macAdress;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	
}
