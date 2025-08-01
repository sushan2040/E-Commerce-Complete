package com.example.ecommerce.configuration.masters;

import java.util.Date;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.cache.annotation.Cacheable;
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
@EntityListeners(value = AuditingEntityListener.class)
@Table(name = "level5_master",schema = "ecommerce")
public class Level5Master {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "level5_id")
	private Integer level5Id;
	@Column(name = "level4_id")
	private Integer level4Id;
	@Column(name = "level5_name")
	private String level5Name;
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
	public String getLevel5Name() {
		return level5Name;
	}
	public void setLevel5Name(String level5Name) {
		this.level5Name = level5Name;
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
