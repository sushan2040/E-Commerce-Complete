package com.example.ecommerce.configuration.beans;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Level4MasterBean extends PaginationCommonBean implements Serializable{

	private Integer level4Id;
	private Integer level3Id;
	private String level4Name;
	private String deleted;
	private String status;
	private Integer createdBy;
	private Date createdDate;
	private Integer updatedBy;
	private Date updatedDate;
	private String macId;
	private String ipAddress;
	private String level3Name;
	
	
	public String getLevel3Name() {
		return level3Name;
	}
	public void setLevel3Name(String level3Name) {
		this.level3Name = level3Name;
	}
	public Integer getLevel4Id() {
		return level4Id;
	}
	public void setLevel4Id(Integer level4Id) {
		this.level4Id = level4Id;
	}
	public Integer getLevel3Id() {
		return level3Id;
	}
	public void setLevel3Id(Integer level3Id) {
		this.level3Id = level3Id;
	}
	public String getLevel4Name() {
		return level4Name;
	}
	public void setLevel4Name(String level4Name) {
		this.level4Name = level4Name;
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
	public Level4MasterBean(Integer level4Id, Integer level3Id, String level4Name, String level3Name,String status ) {
		super();
		this.level4Id = level4Id;
		this.level3Id = level3Id;
		this.level4Name = level4Name;
		this.status = status;
		this.level3Name = level3Name;
	}
	
	public Level4MasterBean(Integer level4Id, String level4Name) {
		super();
		this.level4Id = level4Id;
		this.level4Name = level4Name;
	}
	public Level4MasterBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
