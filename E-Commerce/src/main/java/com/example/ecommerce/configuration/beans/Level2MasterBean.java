package com.example.ecommerce.configuration.beans;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Level2MasterBean extends PaginationCommonBean implements Serializable{

	private Integer level2Id;
	private Integer level1Id;
	private String level2Name;
	private String deleted;
	private String status;
	private Integer createdBy;
	private Date createdDate;
	private Integer updatedBy;
	private Date updatedDate;
	private String macId;
	private String ipAddress;
	private String countryName;
	
	
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public Integer getLevel2Id() {
		return level2Id;
	}
	public void setLevel2Id(Integer level2Id) {
		this.level2Id = level2Id;
	}
	public Integer getLevel1Id() {
		return level1Id;
	}
	public void setLevel1Id(Integer level1Id) {
		this.level1Id = level1Id;
	}
	public String getLevel2Name() {
		return level2Name;
	}
	public void setLevel2Name(String level2Name) {
		this.level2Name = level2Name;
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
	public Level2MasterBean(Integer level2Id, Integer level1Id, String level2Name,String countryName, String status) {
		super();
		this.level2Id = level2Id;
		this.level1Id = level1Id;
		this.level2Name = level2Name;
		this.countryName=countryName;
		this.status = status;
	}
	
	
	
	public Level2MasterBean(Integer level2Id, String level2Name) {
		super();
		this.level2Id = level2Id;
		this.level2Name = level2Name;
	}
	public Level2MasterBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
