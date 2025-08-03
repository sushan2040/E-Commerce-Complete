package com.example.ecommerce.seller.inventory.masters;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "user_product_cart") 
@Data
public class UserProductCart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_product_cart_id")
	private Integer userProductCartId;
	@Column(name = "user_id")
	private Integer userId;
	@Column(name = "product_final_cost_master_id")
	private Integer productFinalCostMasterId;
	public Integer getUserProductCartId() {
		return userProductCartId;
	}
	public void setUserProductCartId(Integer userProductCartId) {
		this.userProductCartId = userProductCartId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getProductFinalCostMasterId() {
		return productFinalCostMasterId;
	}
	public void setProductFinalCostMasterId(Integer productFinalCostMasterId) {
		this.productFinalCostMasterId = productFinalCostMasterId;
	}
	public UserProductCart() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
}
