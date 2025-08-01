package com.example.ecommerce.configuration.beans;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Level3MasterBean extends PaginationCommonBean implements Serializable{

	private Integer level3Id;
	private Integer level2Id;
	private String level3Name;
	private String deleted;
	private String status;
	private Integer createdBy;
	private Date createdDate;
	private Integer updatedBy;
	private Date updatedDate;
	private String macId;
	private String ipAddress;
	private String level2Name;
	
	
	public String getLevel2Name() {
		return level2Name;
	}
	public void setLevel2Name(String level2Name) {
		this.level2Name = level2Name;
	}
	public Integer getLevel3Id() {
		return level3Id;
	}
	public void setLevel3Id(Integer level3Id) {
		this.level3Id = level3Id;
	}
	public Integer getLevel2Id() {
		return level2Id;
	}
	public void setLevel2Id(Integer level2Id) {
		this.level2Id = level2Id;
	}
	public String getLevel3Name() {
		return level3Name;
	}
	public void setLevel3Name(String level3Name) {
		this.level3Name = level3Name;
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
	public Level3MasterBean(Integer level3Id, Integer level2Id, String level3Name, String level2Name,String status) {
		super();
		this.level3Id = level3Id;
		this.level2Id = level2Id;
		this.level3Name = level3Name;
		this.status = status;
		this.level2Name = level2Name;
	}
	
	public Level3MasterBean(Integer level3Id, String level3Name) {
		super();
		this.level3Id = level3Id;
		this.level3Name = level3Name;
	}
	public Level3MasterBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
