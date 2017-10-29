package com.ibm.kstar.entity.product.permission;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "crm_t_product_permission_rel")
public class PermissionRel implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "crm_t_product_permission_rel_id_generator")
	@GenericGenerator(name = "crm_t_product_permission_rel_id_generator", strategy = "uuid")
	@Column(name = "C_PID")
	private String id;

	@Column(name = "PRODUCT_CATALOG_ID")
	private String productCatalogId;

	@Column(name = "ORG_ID")
	private String orgId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProductCatalogId() {
		return productCatalogId;
	}

	public void setProductCatalogId(String productCatalogId) {
		this.productCatalogId = productCatalogId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
}
