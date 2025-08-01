package com.example.ecommerce.seller.usermgmt.beans;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.ecommerce.configuration.beans.PaginationCommonBean;

public class EmployeeMasterBean extends PaginationCommonBean implements Serializable,UserDetails{

	private static final long serialVersionUID = 5925952208190069398L;
	private Integer empId;
	private Integer businessId;
	private Integer roleId;
	private String username;
	private String password;
	private String firstName;
	private String middleName;
	private String lastName;
	private String email;
	private String mobile;
	private String deleted;
	private String status;
	private Integer level1Id;
	private Integer level2Id;
	private Integer level3Id;
	private Integer level4Id;
	private Integer level5Id;
	private String areaCode;
	private String address;
	private Integer createdBy;
	private Date createdDate;
	private Integer updatedBy;
	private Date updatedDate;
	private String macId;
	private String ipAddress;
	private String roleName;
	private Integer userId;
	
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getEmpId() {
		return empId;
	}
	public void setEmpId(Integer empId) {
		this.empId = empId;
	}
	public Integer getBusinessId() {
		return businessId;
	}
	public void setBusinessId(Integer businessId) {
		this.businessId = businessId;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
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
	public Integer getLevel1Id() {
		return level1Id;
	}
	public void setLevel1Id(Integer level1Id) {
		this.level1Id = level1Id;
	}
	public Integer getLevel2Id() {
		return level2Id;
	}
	public void setLevel2Id(Integer level2Id) {
		this.level2Id = level2Id;
	}
	public Integer getLevel3Id() {
		return level3Id;
	}
	public void setLevel3Id(Integer level3Id) {
		this.level3Id = level3Id;
	}
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
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public EmployeeMasterBean(Integer empId, String firstName, String middleName, String lastName, String username,String roleName,String status) {
		super();
		this.empId = empId;
		this.username = username;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.roleName=roleName;
		this.status=status;
	}
	public EmployeeMasterBean(Integer empId, Integer businessId, Integer roleId, String username, String firstName,
			String middleName, String lastName,String password) {
		super();
		this.empId = empId;
		this.businessId = businessId;
		this.roleId = roleId;
		this.username = username;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.password=password;
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}
	public EmployeeMasterBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
}
