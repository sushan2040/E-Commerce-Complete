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
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bridge_parameters",schema = "ecommerce")
@EntityListeners(value=AuditingEntityListener.class)
public class BridgeParameter {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bridge_parameter_id")
	private Integer bridgeParameterId;
	@Column(name = "bridge_parameter_name")
	private String bridgeParameterName;
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
	public Integer getBridgeParameterId() {
		return bridgeParameterId;
	}
	public void setBridgeParameterId(Integer bridgeParameterId) {
		this.bridgeParameterId = bridgeParameterId;
	}
	public String getBridgeParameterName() {
		return bridgeParameterName;
	}
	public void setBridgeParameterName(String bridgeParameterName) {
		this.bridgeParameterName = bridgeParameterName;
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
	public BridgeParameter(Integer bridgeParameterId, String bridgeParameterName) {
		super();
		this.bridgeParameterId = bridgeParameterId;
		this.bridgeParameterName = bridgeParameterName;
	}
	public BridgeParameter() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	
}
