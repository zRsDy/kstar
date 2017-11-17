package com.ibm.kstar.entity.bizopp;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.permission.utils.Permission;
import com.ibm.kstar.permission.utils.PermissionBusinessId;

/**
 * 特价申请
 * Created by wangchao on 2017/5/10.
 */
@Entity
@Table(name = "crm_t_rebate_header")
@Permission(businessType = "Rebate")
public class Rebate extends ComEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "row_id")
    @Id
    @GeneratedValue(generator = "rebate_header_id_generator")
    @GenericGenerator(name = "rebate_header_id_generator", strategy = "uuid")
    @PermissionBusinessId
    private String id;

    /**
     * 申请单号
     */
    @Column(name = "no")
    private String no;
    /**
     * 申请名称
     */
    @Column(name = "name")
    private String name;
    /**
     * 申请类型
     */
    @Column(name = "type")
    private String type;

    /**
     * 所属区域
     */
    @Column(name = "belong_area")
    private String belongArea;

    /**
     * 申请代理商
     */
    @Column(name = "apply_Agent")
    private String applyAgent;
    @Column(name = "apply_Agent_name")
    private String applyAgentName;

    /**
     * 申请项目所属行业
     */
    @Column(name = "belong_industry")
    private String belongIndustry;

    /**
     * 代理商联系人
     */
    @Column(name = "agent_contact")
    private String agentContact;

    /**
     * 代理商联系电话
     */
    @Column(name = "phone")
    private String phone;

    /**
     * 折后总金额
     */
    @Column(name = "rebate_amount")
    private String rebateAmount;

    /**
     * 特价有效期至
     */
    @Column(name = "start_date")
    private Date startDate;

    /**
     * 特价有效期至
     */
    @Column(name = "end_date")
    private Date endDate;

    /**
     * 项目分析
     */
    @Column(name = "REBATE_DESC")
    private String rebateDesc;

    /**
     * 所属运营商
     */
    @Column(name = "belong_Operator")
    private String belongOperator;

    /**
     * 商机名称
     */
    @Column(name = "biz_id_h")
    private String bizIdh;

    @Column(name = "biz_name_h")
    private String bizNameh;
    
    /** 特价版本号 */
	@Column(name = "C_VERSION")
	private Integer version;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * 采购周期
     */
    @Column(name = "purchase_Type")
    private String purchaseType;
    
    /**
     * 特价类型
     */
    @Column(name = "special_off")
    private String specialOff;
    
    /**
     * 审核状态
     */
    @Column(name = "STATUS")
    private String status;
    
    /**
     * 商务专员
     */
    @Column(name = "BUSINESS_EXECUTIVE")
    private String businessExecutive;

    public String getBusinessExecutive() {
		return businessExecutive;
	}

	public void setBusinessExecutive(String businessExecutive) {
		this.businessExecutive = businessExecutive;
	}

	@Column(name = "belong_IndustrySub")
    private String belongIndustrySub;

    public String getBelongIndustrySub() {
        return belongIndustrySub;
    }

    public void setBelongIndustrySub(String belongIndustrySub) {
        this.belongIndustrySub = belongIndustrySub;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Transient
    private String rivalList_str;

    @Transient
    private String t1List_str;

    @Transient
    private String t2List_str;

    @Transient
    private String t3List_str;

    @Transient
    private String t4List_str;

    @Transient
    private String t5List_str;

    public String getT5List_str() {
        return t5List_str;
    }

    public void setT5List_str(String t5List_str) {
        this.t5List_str = t5List_str;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRivalList_str() {
        return rivalList_str;
    }

    public void setRivalList_str(String rivalList_str) {
        this.rivalList_str = rivalList_str;
    }

    public String getT1List_str() {
        return t1List_str;
    }

    public void setT1List_str(String t1List_str) {
        this.t1List_str = t1List_str;
    }

    public String getT2List_str() {
        return t2List_str;
    }

    public void setT2List_str(String t2List_str) {
        this.t2List_str = t2List_str;
    }

    public String getT3List_str() {
        return t3List_str;
    }

    public void setT3List_str(String t3List_str) {
        this.t3List_str = t3List_str;
    }

    public String getT4List_str() {
        return t4List_str;
    }

    public void setT4List_str(String t4List_str) {
        this.t4List_str = t4List_str;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBelongArea() {
        return belongArea;
    }

    public void setBelongArea(String belongArea) {
        this.belongArea = belongArea;
    }

    public String getApplyAgent() {
        return applyAgent;
    }

    public void setApplyAgent(String applyAgent) {
        this.applyAgent = applyAgent;
    }

    public String getBelongIndustry() {
        return belongIndustry;
    }

    public void setBelongIndustry(String belongIndustry) {
        this.belongIndustry = belongIndustry;
    }

    public String getAgentContact() {
        return agentContact;
    }

    public void setAgentContact(String agentContact) {
        this.agentContact = agentContact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRebateAmount() {
        return rebateAmount;
    }

    public void setRebateAmount(String rebateAmount) {
        this.rebateAmount = rebateAmount;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getRebateDesc() {
        return rebateDesc;
    }

    public void setRebateDesc(String rebateDesc) {
        this.rebateDesc = rebateDesc;
    }

    public String getBelongOperator() {
        return belongOperator;
    }

    public void setBelongOperator(String belongOperator) {
        this.belongOperator = belongOperator;
    }


    public String getPurchaseType() {
        return purchaseType;
    }

    public void setPurchaseType(String purchaseType) {
        this.purchaseType = purchaseType;
    }

    public String getApplyAgentName() {
        return applyAgentName;
    }

    public void setApplyAgentName(String applyAgentName) {
        this.applyAgentName = applyAgentName;
    }

    public String getBizIdh() {
        return bizIdh;
    }

    public void setBizIdh(String bizIdh) {
        this.bizIdh = bizIdh;
    }

    public String getBizNameh() {
        return bizNameh;
    }

    public void setBizNameh(String bizNameh) {
        this.bizNameh = bizNameh;
    }

    public String getStatusName(){
        LovMember lov = (LovMember) CacheData.getInstance().get("PROCESS_STATUS_" + status);
        return lov == null ? "" : lov.getName();
    }

	public String getSpecialOff() {
		return specialOff;
	}

	public void setSpecialOff(String specialOff) {
		this.specialOff = specialOff;
	}
	
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	
}
