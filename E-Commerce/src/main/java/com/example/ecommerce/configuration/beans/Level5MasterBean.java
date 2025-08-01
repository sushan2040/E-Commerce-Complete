package com.example.ecommerce.configuration.beans;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Level5MasterBean extends PaginationCommonBean implements Serializable{

	private Integer level5Id;
	private Integer level4Id;
	private String level5Name;
	private String deleted;
	private String status;
	private Integer createdBy;
	private Date createdDate;
	private Integer updatedBy;
	private Date updatedDate;
	private String macId;
	private String ipAddress;
	private String level4Name;
	
	
	public String getLevel5Name() {
		return level5Name;
	}
	public void setLevel5Name(String level5Name) {
		this.level5Name = level5Name;
	}
	public Integer getLevel4Id() {
		return level4Id;
	}
	public void setLevel4Id(Integer level4Id) {
		this.level4Id = level4Id;
	}
	
	public Integer getLevel5Id() {
		return level5Id;
	}
	public void setLevel5Id(Integer level5Id) {
		this.level5Id = level5Id;
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
	public Level5MasterBean(Integer level5Id, Integer level4Id, String level5Name, String level4Name,String status) {
		super();
		this.level5Id = level5Id;
		this.level4Id = level4Id;
		this.level5Name = level5Name;
		this.status = status;
		this.level4Name = level4Name;
	}
	
	public Level5MasterBean(Integer level5Id, String level5Name) {
		super();
		this.level5Id = level5Id;
		this.level5Name = level5Name;
	}
	public Level5MasterBean() {
		// TODO Auto-generated constructor stub
	}
	
	
}
