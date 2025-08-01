package com.example.ecommerce.configuration.beans;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude
public class SubModuleMasterBean extends PaginationCommonBean{

	private Integer subModuleId;
	private String subModuleName;
	private String parentName;
	private String menuTypeName;
	private Integer menuTypeId;
	private Integer parentId;
	@JsonIgnore
	private String deleted;
	
	private String status;
	private String requestMapping;
	@JsonIgnore
	private Integer createdBy;
	@JsonIgnore
	private Date createdDate;
	@JsonIgnore
	private Integer updatedBy;
	@JsonIgnore
	private Date updatedDate;
	@JsonIgnore
	private String macId;
	@JsonIgnore
	private String ipAddress;
	@JsonIgnore
	private String devicefrom;
	
	private String icon;
	
	private List<SubModuleMasterBean> subModule;
	
	
	@JsonGetter("subModule")
	public List<SubModuleMasterBean> getSubModule() {
		return subModule;
	}
	public void setSubModule(List<SubModuleMasterBean> subModule) {
		this.subModule = subModule;
	}
	@JsonGetter("icon")
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	@JsonGetter("requestMapping")
	public String getRequestMapping() {
		return requestMapping;
	}
	public void setRequestMapping(String requestMapping) {
		this.requestMapping = requestMapping;
	}
	@JsonGetter("parentName")
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	@JsonGetter("menuTypeName")
	public String getMenuTypeName() {
		return menuTypeName;
	}
	public void setMenuTypeName(String menuTypeName) {
		this.menuTypeName = menuTypeName;
	}
	@JsonGetter("subModuleId")
	public Integer getSubModuleId() {
		return subModuleId;
	}
	public void setSubModuleId(Integer subModuleId) {
		this.subModuleId = subModuleId;
	}
	@JsonGetter("subModuleName")
	public String getSubModuleName() {
		return subModuleName;
	}
	public void setSubModuleName(String subModuleName) {
		this.subModuleName = subModuleName;
	}
	@JsonGetter("menuTypeId")
	public Integer getMenuTypeId() {
		return menuTypeId;
	}
	public void setMenuTypeId(Integer menuTypeId) {
		this.menuTypeId = menuTypeId;
	}
	@JsonGetter("parentId")
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public String getDeleted() {
		return deleted;
	}
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}
	@JsonGetter("status")
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
	public String getDevicefrom() {
		return devicefrom;
	}
	public void setDevicefrom(String devicefrom) {
		this.devicefrom = devicefrom;
	}
	
	
}
