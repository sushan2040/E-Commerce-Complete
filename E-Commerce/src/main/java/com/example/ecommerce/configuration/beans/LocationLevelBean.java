package com.example.ecommerce.configuration.beans;

import java.io.Serializable;

import com.example.ecommerce.utils.CommonBean;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Transient;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LocationLevelBean extends CommonBean implements Serializable{

	private Integer locationLevelId;
	private Integer countryId;
	private Integer locationLevel1;
	private String locationLevel1Label;
	private Integer locationLevel2;
	private String locationLevel2Label;
	private Integer locationLevel3;
	private String locationLevel3Label;
	private Integer locationLevel4;
	private String locationLevel4Label;
	private Integer locationLevel5;
	private String locationLevel5Label;
	private String status;
	
	@Transient
	private Integer locationLevel;
	
	@Transient
	private String countryName;
	
	
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
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	
	public Integer getLocationLevel() {
		return locationLevel;
	}
	public void setLocationLevel(Integer locationLevel) {
		this.locationLevel = locationLevel;
	}
	public LocationLevelBean(Integer locationLevelId, String status, String countryName) {
		super();
		this.locationLevelId = locationLevelId;
		this.status = status;
		this.countryName = countryName;
	}
	public LocationLevelBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
