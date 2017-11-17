package com.ibm.kstar.entity.bizopp;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;

/**
 * 商机变更
 * Created by wangchao on 2017/3/17.
 */

@Entity
@Table(name = "CRM_T_BIZOPP_CHANGE")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BizOppChange extends ComEntity implements Serializable {

    @Column(name = "ROW_ID")//	id
    @Id
    @GeneratedValue(generator = "crm_t_bizopp_change_generator")
    @GenericGenerator(name = "crm_t_bizopp_change_generator", strategy = "uuid")
    private String id;
    @Column(name = "SOURCE_ID")//	商机id
    private String sourceId;
    @Column(name = "OPPORTUNITY_NAME")//	商机名称
    private String opportunityName;
    @Column(name = "BIZ_OPP_ADDRESS")//	商机所在地
    private String bizOppAddress;
    @Column(name = "biz_Opp_Address_Name")
    private String bizOppAddressName;
    @Column(name = "ADDRESS")//	详细地址
    private String address;
    @Column(name = "CLIENT_ID")//	终端用户
    private String clientId;
    @Column(name = "CLIENT_NAME")//	终端用户
    private String clientName;
    @Column(name = "BID_UNIT")//	招标单位
    private String bidUnit;
    @Column(name = "BID_NO")
    private String bidNo;
    @Column(name = "OPPORTUNITY_STEP")//	商机阶段
    private String opportunityStep;//saleStage
    @Column(name = "SUCCESS_RATE")//	成功率
    private String successRate;
    @Column(name = "PLAN_FIN_DATE")//	预计成交时间
    private Date planFinDate;
    @Column(name = "PROJECT_PROGRESS")//	项目推动情况
    private String projectProgress;
    @Column(name = "COMPETITIVE_BRANDS")//	竞争品牌
    private String competitiveBrands;
    @Column(name = "AUDIT_DATE")//	审核时间
    private Date auditDate;
    @Column(name = "layer2")
    private String layer2;
    @Column(name = "layer3")
    private String layer3;
    @Column(name = "layer4")
    private String layer4;

    @Column(name = "OPPORTUNITY_NAME2")//	商机名称
    private String opportunityName2;
    @Column(name = "BIZ_OPP_ADDRESS2")//	商机所在地
    private String bizOppAddress2;
    @Column(name = "ADDRESS2")//	详细地址
    private String address2;
    @Column(name = "BIZ_OPP_ADDRESS_NAME2")
    private String bizOppAddressName2;
    @Column(name = "CLIENT_ID2")//	终端用户
    private String clientId2;
    @Column(name = "CLIENT_NAME2")//	终端用户
    private String clientName2;
    @Column(name = "BID_UNIT2")//	招标单位
    private String bidUnit2;
    @Column(name = "BID_NO2")
    private String bidNo2;
    @Column(name = "OPPORTUNITY_STEP2")//	商机阶段
    private String opportunityStep2;//saleStage
    @Column(name = "SUCCESS_RATE2")//	成功率
    private String successRate2;
    @Column(name = "PLAN_FIN_DATE2")//	预计成交时间
    private Date planFinDate2;
    @Column(name = "PROJECT_PROGRESS2")//	项目推动情况
    private String projectProgress2;
    @Column(name = "COMPETITIVE_BRANDS2")//	竞争品牌
    private String competitiveBrands2;

    @Column(name = "AUDIT_STATUS")
    private String auditStatus;

    @Column(name = "layer22")
    private String layer22;
    @Column(name = "layer32")
    private String layer32;
    @Column(name = "layer42")
    private String layer42;

    @Transient
    private List<Integrator> integrators;

    @Transient
	private List<ProductDetail> details;

	public List<Integrator> getIntegrators() {
		return integrators;
	}

	public void setIntegrators(List<Integrator> integrators) {
		this.integrators = integrators;
	}

	public List<ProductDetail> getDetails() {
		return details;
	}

	public void setDetails(List<ProductDetail> details) {
		this.details = details;
	}
    
    
    public String getLayer2() {
        return layer2;
    }

    public void setLayer2(String layer2) {
        this.layer2 = layer2;
    }

    public String getLayer3() {
        return layer3;
    }

    public void setLayer3(String layer3) {
        this.layer3 = layer3;
    }

    public String getLayer4() {
        return layer4;
    }

    public void setLayer4(String layer4) {
        this.layer4 = layer4;
    }

    public String getLayer22() {
        return layer22;
    }

    public void setLayer22(String layer22) {
        this.layer22 = layer22;
    }

    public String getLayer32() {
        return layer32;
    }

    public void setLayer32(String layer32) {
        this.layer32 = layer32;
    }

    public String getLayer42() {
        return layer42;
    }

    public void setLayer42(String layer42) {
        this.layer42 = layer42;
    }

    public String getBidNo() {
        return bidNo;
    }

    public void setBidNo(String bidNo) {
        this.bidNo = bidNo;
    }

    public String getBidNo2() {
        return bidNo2;
    }

    public void setBidNo2(String bidNo2) {
        this.bidNo2 = bidNo2;
    }

    public String getOpportunityStepName() {
        LovMember lov = (LovMember) CacheData.getInstance().get(opportunityStep);
        return lov == null ? "" : lov.getName();
    }

    public String getSuccessRateName() {
        LovMember lov = (LovMember) CacheData.getInstance().get(successRate);
        return lov == null ? "" : lov.getName();
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientName2() {
        return clientName2;
    }

    public void setClientName2(String clientName2) {
        this.clientName2 = clientName2;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getOpportunityName() {
        return opportunityName;
    }

    public void setOpportunityName(String opportunityName) {
        this.opportunityName = opportunityName;
    }

    public String getBizOppAddress() {
        return bizOppAddress;
    }

    public void setBizOppAddress(String bizOppAddress) {
        this.bizOppAddress = bizOppAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getBidUnit() {
        return bidUnit;
    }

    public void setBidUnit(String bidUnit) {
        this.bidUnit = bidUnit;
    }

    public String getOpportunityStep() {
        return opportunityStep;
    }

    public void setOpportunityStep(String opportunityStep) {
        this.opportunityStep = opportunityStep;
    }

    public String getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(String successRate) {
        this.successRate = successRate;
    }

    public Date getPlanFinDate() {
        return planFinDate;
    }

    public void setPlanFinDate(Date planFinDate) {
        this.planFinDate = planFinDate;
    }

    public String getProjectProgress() {
        return projectProgress;
    }

    public void setProjectProgress(String projectProgress) {
        this.projectProgress = projectProgress;
    }

    public String getCompetitiveBrands() {
        return competitiveBrands;
    }

    public void setCompetitiveBrands(String competitiveBrands) {
        this.competitiveBrands = competitiveBrands;
    }


    public Date getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(Date auditDate) {
        this.auditDate = auditDate;
    }

    public String getOpportunityName2() {
        return opportunityName2;
    }

    public void setOpportunityName2(String opportunityName2) {
        this.opportunityName2 = opportunityName2;
    }

    public String getBizOppAddress2() {
        return bizOppAddress2;
    }

    public void setBizOppAddress2(String bizOppAddress2) {
        this.bizOppAddress2 = bizOppAddress2;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getClientId2() {
        return clientId2;
    }

    public void setClientId2(String clientId2) {
        this.clientId2 = clientId2;
    }

    public String getBidUnit2() {
        return bidUnit2;
    }

    public void setBidUnit2(String bidUnit2) {
        this.bidUnit2 = bidUnit2;
    }

    public String getOpportunityStep2() {
        return opportunityStep2;
    }

    public void setOpportunityStep2(String opportunityStep2) {
        this.opportunityStep2 = opportunityStep2;
    }

    public String getSuccessRate2() {
        return successRate2;
    }

    public void setSuccessRate2(String successRate2) {
        this.successRate2 = successRate2;
    }

    public Date getPlanFinDate2() {
        return planFinDate2;
    }

    public void setPlanFinDate2(Date planFinDate2) {
        this.planFinDate2 = planFinDate2;
    }

    public String getProjectProgress2() {
        return projectProgress2;
    }

    public void setProjectProgress2(String projectProgress2) {
        this.projectProgress2 = projectProgress2;
    }

    public String getCompetitiveBrands2() {
        return competitiveBrands2;
    }

    public void setCompetitiveBrands2(String competitiveBrands2) {
        this.competitiveBrands2 = competitiveBrands2;
    }

    public String getBizOppAddressName() {
        return bizOppAddressName;
    }

    public void setBizOppAddressName(String bizOppAddressName) {
        this.bizOppAddressName = bizOppAddressName;
    }

    public String getBizOppAddressName2() {
        return bizOppAddressName2;
    }

    public void setBizOppAddressName2(String bizOppAddressName2) {
        this.bizOppAddressName2 = bizOppAddressName2;
    }

    @Override
    public String toString() {
        return "BizOppChange{" +
                "id='" + id + '\'' +
                ", sourceId='" + sourceId + '\'' +
                ", opportunityName='" + opportunityName + '\'' +
                ", bizOppAddress='" + bizOppAddress + '\'' +
                ", bizOppAddressName='" + bizOppAddressName + '\'' +
                ", address='" + address + '\'' +
                ", clientId='" + clientId + '\'' +
                ", clientName='" + clientName + '\'' +
                ", bidUnit='" + bidUnit + '\'' +
                ", opportunityStep='" + opportunityStep + '\'' +
                ", successRate='" + successRate + '\'' +
                ", planFinDate=" + planFinDate +
                ", projectProgress='" + projectProgress + '\'' +
                ", competitiveBrands='" + competitiveBrands + '\'' +
                ", auditDate=" + auditDate +
                ", opportunityName2='" + opportunityName2 + '\'' +
                ", bizOppAddress2='" + bizOppAddress2 + '\'' +
                ", address2='" + address2 + '\'' +
                ", bizOppAddressName2='" + bizOppAddressName2 + '\'' +
                ", clientId2='" + clientId2 + '\'' +
                ", clientName2='" + clientName2 + '\'' +
                ", bidUnit2='" + bidUnit2 + '\'' +
                ", opportunityStep2='" + opportunityStep2 + '\'' +
                ", successRate2='" + successRate2 + '\'' +
                ", planFinDate2=" + planFinDate2 +
                ", projectProgress2='" + projectProgress2 + '\'' +
                ", competitiveBrands2='" + competitiveBrands2 + '\'' +
                ", auditStatus='" + auditStatus + '\'' +
                '}';
    }
}
