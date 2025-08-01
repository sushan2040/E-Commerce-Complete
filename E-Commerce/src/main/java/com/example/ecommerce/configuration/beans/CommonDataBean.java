package com.example.ecommerce.configuration.beans;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Column;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonDataBean extends PaginationCommonBean{

	private Integer commonDataId;
	private String commonDataName;
	private String commonDataDesc;
	private Integer bridgeParameterId;
	private Integer parentCommonDataId;
	private String status;
	private String deleted;
	private Integer createdBy;
	private Date createdDate;
	private Integer updatedBy;
	private Date updatedDate;
	private String macId;
	private String ipAddress;
	private Integer businessId;
	
	
	private String parentCommonDataName;
	private String bridgeParameterName;
	
	
	
	public Integer getBusinessId() {
		return businessId;
	}
	public void setBusinessId(Integer businessId) {
		this.businessId = businessId;
	}
	public String getCommonDataName() {
		return commonDataName;
	}
	public void setCommonDataName(String commonDataName) {
		this.commonDataName = commonDataName;
	}
	public String getCommonDataDesc() {
		return commonDataDesc;
	}
	public void setCommonDataDesc(String commonDataDesc) {
		this.commonDataDesc = commonDataDesc;
	}
	public Integer getCommonDataId() {
		return commonDataId;
	}
	public void setCommonDataId(Integer commonDataId) {
		this.commonDataId = commonDataId;
	}
	
	public Integer getBridgeParameterId() {
		return bridgeParameterId;
	}
	public void setBridgeParameterId(Integer bridgeParameterId) {
		this.bridgeParameterId = bridgeParameterId;
	}
	public Integer getParentCommonDataId() {
		return parentCommonDataId;
	}
	public void setParentCommonDataId(Integer parentCommonDataId) {
		this.parentCommonDataId = parentCommonDataId;
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
	public CommonDataBean(Integer commonDataId, String commonDataName, String commonDataDesc,
			String parentCommonDataName, String bridgeParameterName,String status) {
		super();
		this.commonDataId = commonDataId;
		this.commonDataName = commonDataName;
		this.commonDataDesc = commonDataDesc;
		this.parentCommonDataName = parentCommonDataName;
		this.bridgeParameterName = bridgeParameterName;
		this.status=status;
	}
	public String getParentCommonDataName() {
		return parentCommonDataName;
	}
	public void setParentCommonDataName(String parentCommonDataName) {
		this.parentCommonDataName = parentCommonDataName;
	}
	public String getBridgeParameterName() {
		return bridgeParameterName;
	}
	public void setBridgeParameterName(String bridgeParameterName) {
		this.bridgeParameterName = bridgeParameterName;
	}
	public CommonDataBean(Integer commonDataId, String commonDataName) {
		super();
		this.commonDataId = commonDataId;
		this.commonDataName = commonDataName;
	}
	
	public CommonDataBean(Integer commonDataId, String commonDataName, String commonDataDesc) {
		super();
		this.commonDataId = commonDataId;
		this.commonDataName = commonDataName;
		this.commonDataDesc = commonDataDesc;
	}
	public CommonDataBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
