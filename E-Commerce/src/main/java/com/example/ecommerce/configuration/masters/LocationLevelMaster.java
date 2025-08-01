package com.example.ecommerce.configuration.masters;

import java.util.Date;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.ecommerce.utils.CommonEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@EntityListeners(value = AuditingEntityListener.class)
@Table(name = "location_level_master",schema = "ecommerce")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "locationLevelMasterCache")
public class LocationLevelMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "location_level_id")
	private Integer locationLevelId;
	@Column(name = "country_id")
	private Integer countryId;
	@Column(name = "location_lvl_1")
	private Integer locationLevel1;
	@Column(name = "location_lvl_1_label")
	private String locationLevel1Label;
	@Column(name = "location_lvl_2")
	private Integer locationLevel2;
	@Column(name = "location_lvl_2_label")
	private String locationLevel2Label;
	@Column(name = "location_lvl_3")
	private Integer locationLevel3;
	@Column(name = "location_lvl_3_label")
	private String locationLevel3Label;
	@Column(name = "location_lvl_4")
	private Integer locationLevel4;
	@Column(name = "location_lvl_4_label")
	private String locationLevel4Label;
	@Column(name = "location_lvl_5")
	private Integer locationLevel5;
	@Column(name = "location_lvl_5_label")
	private String locationLevel5Label;
	@Column(name = "status")
	private String status;
	
	@Column(name = "deleted")
	private String deleted;
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
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getLocationLevelId() {
		return locationLevelId;
	}
	public void setLocationLevelId(Integer locationLevelId) {
		this.locationLevelId = locationLevelId;
	}
	public Integer getCountryId() {
		return countryId;
	}
	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}
	public Integer getLocationLevel1() {
		return locationLevel1;
	}
	public void setLocationLevel1(Integer locationLevel1) {
		this.locationLevel1 = locationLevel1;
	}
	public String getLocationLevel1Label() {
		return locationLevel1Label;
	}
	public void setLocationLevel1Label(String locationLevel1Label) {
		this.locationLevel1Label = locationLevel1Label;
	}
	public Integer getLocationLevel2() {
		return locationLevel2;
	}
	public void setLocationLevel2(Integer locationLevel2) {
		this.locationLevel2 = locationLevel2;
	}
	public String getLocationLevel2Label() {
		return locationLevel2Label;
	}
	public void setLocationLevel2Label(String locationLevel2Label) {
		this.locationLevel2Label = locationLevel2Label;
	}
	public Integer getLocationLevel3() {
		return locationLevel3;
	}
	public void setLocationLevel3(Integer locationLevel3) {
		this.locationLevel3 = locationLevel3;
	}
	public String getLocationLevel3Label() {
		return locationLevel3Label;
	}
	public void setLocationLevel3Label(String locationLevel3Label) {
		this.locationLevel3Label = locationLevel3Label;
	}
	public Integer getLocationLevel4() {
		return locationLevel4;
	}
	public void setLocationLevel4(Integer locationLevel4) {
		this.locationLevel4 = locationLevel4;
	}
	public String getLocationLevel4Label() {
		return locationLevel4Label;
	}
	public void setLocationLevel4Label(String locationLevel4Label) {
		this.locationLevel4Label = locationLevel4Label;
	}
	public Integer getLocationLevel5() {
		return locationLevel5;
	}
	public void setLocationLevel5(Integer locationLevel5) {
		this.locationLevel5 = locationLevel5;
	}
	public String getLocationLevel5Label() {
		return locationLevel5Label;
	}
	public void setLocationLevel5Label(String locationLevel5Label) {
		this.locationLevel5Label = locationLevel5Label;
	}
	
	
	
}
