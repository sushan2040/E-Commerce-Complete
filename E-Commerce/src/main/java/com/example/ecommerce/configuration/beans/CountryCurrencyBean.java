package com.example.ecommerce.configuration.beans;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CountryCurrencyBean extends PaginationCommonBean implements Serializable{

	private static final long serialVersionUID = 3334532047587532017L;
	private Integer countryCurrencyMasterId;
	private Integer countryId;
	private String currencyName;
	private String currencySymbol;
	private String deleted;
	private String status;
	private Integer createdBy;
	private Date createdDate;
	private Integer updatedBy;
	private Date updatedDate;
	private String macId;
	private String ipAddress;
	private String currencyCode;
	private String countryName;
	
	
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
	
	public String getCurrencySymbol() {
		return currencySymbol;
	}
	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public CountryCurrencyBean(Integer countryCurrencyMasterId, Integer countryId, String currencyName,
			String currencySymbol,String countryName,String currencyCode, String status) {
		super();
		this.countryCurrencyMasterId = countryCurrencyMasterId;
		this.countryId = countryId;
		this.currencyName = currencyName;
		this.currencySymbol = currencySymbol;
		this.status = status;
		this.currencyCode=currencyCode;
		this.countryName = countryName;
	}
	public CountryCurrencyBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
