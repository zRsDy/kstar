package com.ibm.kstar.entity.bizopp;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author gaoyuiang  2017-2-26
 * 
 */
@Entity
@Table(name = "crm_t_competitor")
public class BizCompetitor implements Serializable {
	
	/** 版本号 */
    private static final long serialVersionUID = 419238120319491916L;
	
	@Id
	@GeneratedValue(generator = "bizcompetitor_generator")
	@GenericGenerator(name="bizcompetitor_generator", strategy="uuid")
	@Column(name = "c_pid", unique = true, nullable = false, length = 32)
	private String id;
    
    /** 商机ID */
    @Column(name = "c_fk_bizopp_id", nullable = false, length = 32)
    private String bizOppId;
    
    /** 竞争品牌 */
    @Column(name = "c_competitor_brand", nullable = true, length = 30)
    private String competitorBrand;
    
    /** 产品型号 */
    @Column(name = "c_product_model", nullable = true, length = 80)
    private String productModel;
    
    /** 客户前期选用 */
    @Column(name = "n_is_selected_befor", nullable = true, length = 2)
    private String isSelectedBefor;
    
    /** 优势 */
    @Column(name = "c_advantage", nullable = true, length = 30)
    private String advantage;
    
    /** 劣势 */
    @Column(name = "c_disadvantage", nullable = true, length = 30)
    private String disadvantage;
    
    /** 项目技术方案 */
    @Column(name = "c_pro_tech_scheme", nullable = true, length = 30)
    private String proTechScheme;
    
    /** 项目商务方案 */
    @Column(name = "c_pro_biz_plan", nullable = true, length = 30)
    private String proBizPlan;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBizOppId() {
		return bizOppId;
	}

	public void setBizOppId(String bizOppId) {
		this.bizOppId = bizOppId;
	}

	public String getCompetitorBrand() {
		return competitorBrand;
	}

	public void setCompetitorBrand(String competitorBrand) {
		this.competitorBrand = competitorBrand;
	}

	public String getProductModel() {
		return productModel;
	}

	public void setProductModel(String productModel) {
		this.productModel = productModel;
	}

	public String getIsSelectedBefor() {
		return isSelectedBefor;
	}

	public void setIsSelectedBefor(String isSelectedBefor) {
		this.isSelectedBefor = isSelectedBefor;
	}

	public String getAdvantage() {
		return advantage;
	}

	public void setAdvantage(String advantage) {
		this.advantage = advantage;
	}

	public String getDisadvantage() {
		return disadvantage;
	}

	public void setDisadvantage(String disadvantage) {
		this.disadvantage = disadvantage;
	}

	public String getProTechScheme() {
		return proTechScheme;
	}

	public void setProTechScheme(String proTechScheme) {
		this.proTechScheme = proTechScheme;
	}

	public String getProBizPlan() {
		return proBizPlan;
	}

	public void setProBizPlan(String proBizPlan) {
		this.proBizPlan = proBizPlan;
	}
}
