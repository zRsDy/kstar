package com.ibm.kstar.entity.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.entity.BaseEntity;

@Entity
@Table(name = "CRM_T_PRICE_LAYER_COMLINE")
public class PriceLayCompareLine extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "kstarpricelayercompl_id_generator")
	@GenericGenerator(name="kstarpricelayercompl_id_generator", strategy="uuid")
	@Column(name="C_PID")
	private String id;
	
	// 层级名称
	@Column(name = "C_LAYER_LINE_NAME")
	private String layLineName;
	
	// 有效期结束时间
	@Column(name = "DT_START_DATE")
	private Date startDate;
	
	// 有效期结束时间
	@Column(name = "DT_END_DATE")
	private Date endDate;
	

	// 默认折扣率
	@Column(name = "N_DISCOUNT_RATE")
	private BigDecimal discountRate;
	
	// 头ID
	@Column(name="C_HEADER_ID")
	private String headerId;
	
	// 审批层级
	@Column(name="C_APPROVE_LAYER_LINE")
	private String approveLayLine;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLayLineName() {
		return layLineName;
	}

	public void setLayLineName(String layLineName) {
		this.layLineName = layLineName;
	}
	
	public String getLayLineNameCode() {
		return this.getLovCode(layLineName);
	}

	public String getLayLineNameName() {
		return this.getLovName(layLineName);
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public BigDecimal getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(BigDecimal discountRate) {
		this.discountRate = discountRate;
	}

	public String getHeaderId() {
		return headerId;
	}

	public void setHeaderId(String headerId) {
		this.headerId = headerId;
	}

	public String getApproveLayLine() {
		return approveLayLine;
	}

	public void setApproveLayLine(String approveLayLine) {
		this.approveLayLine = approveLayLine;
	}
	
}
