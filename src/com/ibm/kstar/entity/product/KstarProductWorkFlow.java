package com.ibm.kstar.entity.product;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "CRM_T_PRODUCT_WORKFLOW")
public class KstarProductWorkFlow implements  Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "kstarproductprocess_id_generator")
	@GenericGenerator(name="kstarproductprocess_id_generator", strategy="uuid")
	@Column(name="C_PID")
	private String id;
	
	// 流程ID
	@Column(name = "C_PRO_PROCESS_ID")
	private String processID;
	
	// 流程名称
	@Column(name = "C_PRO_PROCESS_NAME")
	private String processName;
	
	// 产品ID（业务实体ID）
	@Column(name = "C_PRO_ID")
	private String productId;
	
	// 提交人
	@Column(name = "C_SUBMIT_BY")
	private String createBy;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProcessID() {
		return processID;
	}

	public void setProcessID(String processID) {
		this.processID = processID;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
