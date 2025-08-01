package com.example.ecommerce.seller.usermgmt.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.ecommerce.configuration.beans.PaginationCommonBean;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleMasterBean extends PaginationCommonBean implements Serializable{

	private Integer roleId;
	private String roleName;
	private Integer businessId;
	private List<Integer> viewAccess;
	private List<Integer> editAccess;
	private List<Integer> deleteAccess;
	private String deleted;
	private String status;
	private Integer createdBy;
	private Date createdDate;
	private Integer updatedBy;
	private Date updatedDate;
	private String macId;
	private String ipAddress;
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public Integer getBusinessId() {
		return businessId;
	}
	public void setBusinessId(Integer businessId) {
		this.businessId = businessId;
	}
	
	public List<Integer> getViewAccess() {
		return viewAccess;
	}
	public void setViewAccess(List<Integer> viewAccess) {
		this.viewAccess = viewAccess;
	}
	public List<Integer> getEditAccess() {
		return editAccess;
	}
	public void setEditAccess(List<Integer> editAccess) {
		this.editAccess = editAccess;
	}
	public List<Integer> getDeleteAccess() {
		return deleteAccess;
	}
	public void setDeleteAccess(List<Integer> deleteAccess) {
		this.deleteAccess = deleteAccess;
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
	public RoleMasterBean(Integer roleId, String roleName,String status) {
		super();
		this.roleId = roleId;
		this.roleName = roleName;
		this.status=status;
	}
	public RoleMasterBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public RoleMasterBean(Integer roleId, String roleName,String viewAccess,String editAccess,
			String deleteAccess, String status) throws JsonMappingException, JsonProcessingException {
		super();
		ObjectMapper mapper=new ObjectMapper();
		this.roleId = roleId;
		this.roleName = roleName;
		if(viewAccess!=null) {
		this.viewAccess = mapper.readValue(viewAccess,List.class);
		}
		else {
			this.viewAccess=new ArrayList<Integer>();
		}
		if(editAccess!=null) {
		this.editAccess = mapper.readValue(editAccess,List.class);;
		}
		else {
			this.editAccess=new ArrayList<Integer>();
		}
		if(deleteAccess!=null) {
		this.deleteAccess = mapper.readValue(deleteAccess,List.class);;
		}
		else {
			this.deleteAccess=new ArrayList<Integer>();
		}
		this.status = status;
	}
	
	
}
