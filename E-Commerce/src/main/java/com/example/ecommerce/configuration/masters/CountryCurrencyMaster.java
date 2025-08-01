package com.example.ecommerce.configuration.masters;

import java.util.Date;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "country_currency_master",schema = "ecommerce")
@EntityListeners(value = AuditingEntityListener.class)
public class CountryCurrencyMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "country_currency_master_id")
	private Integer countryCurrencyMasterId;
	@Column(name = "country_id")
	private Integer countryId;
	@Column(name = "currency_name")
	private String currencyName;
	@Column(name = "currency_symbol")
	private String currencySymbol;
	@Column(name = "deleted")
	private String deleted;
	@Column(name = "status")
	private String status;
	@Column(name = "created_by")
	private Integer createdBy;
	@Column(name = "created_date")
	private Date createdDate;
	@Column(name = "updated_by")
	private Integer updatedBy;
	@Column(name = "updated_date")
	private Date updatedDate;
	@Column(name = "mac_id")
	private String macId;
	@Column(name = "ip_address")
	private String ipAddress;
	@Column(name = "currency_code")
	private String currencyCode;
	
	
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public Integer getCountryCurrencyMasterId() {
		return countryCurrencyMasterId;
	}
	public void setCountryCurrencyMasterId(Integer countryCurrencyMasterId) {
		this.countryCurrencyMasterId = countryCurrencyMasterId;
	}
	public Integer getCountryId() {
		return countryId;
	}
	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}
	
	public String getCurrencyName() {
		return currencyName;
	}
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	public String getcurrencySymbol() {
		return currencySymbol;
	}
	public void setcurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
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
