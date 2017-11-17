package com.ibm.kstar.entity.product;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * KstarProSerial.java
 * 预定义物料号 序列号表实体类
 * @author Neil Wan 
 * 2017年2月7日 下午15:12:35
 */

@Entity
@Table(name = "CRM_T_PRODUCT_SERIAL")
public class KstarProSerial implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "kstarserial_id_generator")
	@GenericGenerator(name="kstarserial_id_generator", strategy="uuid")
	@Column(name="C_PID")
	private String id;

	// 标准品（S，非标品（N），服务类（F），商务类（B），外购品（P） ECR(ECR)
	@Column(name = "C_PRO_TYPE")
	private String proType;
	
	//日期格式
	@Column(name = "C_PRO_DATE")
	private String dataString;
	
	// 紧急程度
	@Column(name = "N_COUNT")
	private Integer count;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProType() {
		return proType;
	}

	public void setProType(String proType) {
		this.proType = proType;
	}

	public String getDataString() {
		return dataString;
	}

	public void setDataString(String dataString) {
		this.dataString = dataString;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
}
