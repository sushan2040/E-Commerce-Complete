package com.example.ecommerce.configuration.masters;

import java.util.Date;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "common_data",schema = "ecommerce")
@EntityListeners(value = AuditingEntityListener.class)
public class CommonData {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "common_data_id")
	private Integer commonDataId;
	@Column(name = "common_data_name")
	private String commonDataName;
	@Column(name = "common_data_desc")
	private String commonDataDesc;
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
	private String ipAddess;
	
	@Column(name = "business_id")
	private Integer businessId;
	
	
	
	public Integer getBusinessId() {
		return businessId;
	}
	public void setBusinessId(Integer businessId) {
		this.businessId = businessId;
	}
	public Integer getCommonDataId() {
		return commonDataId;
	}
	public void setCommonDataId(Integer commonDataId) {
		this.commonDataId = commonDataId;
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
	public String getIpAddess() {
		return ipAddess;
	}
	public void setIpAddess(String ipAddess) {
		this.ipAddess = ipAddess;
	}
	
	
	
}
