package com.ibm.kstar.entity.bizopp;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by wangchao on 2017/5/10.
 */
@Entity
@Table(name = "CRM_T_REBATE_RIVAL")
public class RebateRival {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "row_id")
    @GeneratedValue(generator = "rebate_rival_id_generator")
    @GenericGenerator(name = "rebate_rival_id_generator", strategy = "uuid")
    private String id;

    @Column(name = "rebate_id")
    private String rebateId;

    @Column(name = "agent_name")
    private String agentName;

    @Column(name = "brand")
    private String brand;

    @Column(name = "product_model")
    private String productModel;

    @Column(name = "price_range")
    private String priceRange;
    
    @Column(name = "offer")
    private String offer;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRebateId() {
        return rebateId;
    }

    public void setRebateId(String rebateId) {
        this.rebateId = rebateId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getProductModel() {
        return productModel;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }
}
