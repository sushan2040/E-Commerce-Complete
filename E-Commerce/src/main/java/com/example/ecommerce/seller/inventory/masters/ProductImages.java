package com.example.ecommerce.seller.inventory.masters;

import java.util.Date;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@EntityListeners(value = AuditingEntityListener.class)
@Table(name = "product_images",schema = "ecommerce")
public class ProductImages {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_image_id")
	private Integer productImagesId;
	@Column(name = "product_image_path")
	private String imagePath;
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
	@Column(name = "is_primary")
	private String isPrimary;
	
	// Many-to-one mapping to ProductMaster
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonIgnore // Prevent circular reference by ignoring the back reference
    private ProductMaster productMaster;
    
    
	
	public ProductMaster getProductMaster() {
		return productMaster;
	}
	public void setProductMaster(ProductMaster productMaster) {
		this.productMaster = productMaster;
	}
	public String getIsPrimary() {
		return isPrimary;
	}
	public void setIsPrimary(String isPrimary) {
		this.isPrimary = isPrimary;
	}
	public Integer getProductImagesId() {
		return productImagesId;
	}
	public void setProductImagesId(Integer productImagesId) {
		this.productImagesId = productImagesId;
	}
	
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
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
