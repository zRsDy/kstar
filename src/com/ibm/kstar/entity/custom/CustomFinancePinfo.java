package com.ibm.kstar.entity.custom;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.xsnake.web.utils.BeanUtils;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;

@Entity
@Table(name = "crm_t_custom_finance_pinfo")
public class CustomFinancePinfo implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = 1L;
    
    /** 主键自增 */
    @Id
    @Column(name = "c_pid")
    @GeneratedValue(generator = "custom_finance_pinfo_pid_generator")
	@GenericGenerator(name="custom_finance_pinfo_pid_generator", strategy="uuid")
    private String id;
    
    /** 客户信息主键 */
    @Column(name = "c_custom_pid", nullable = false, length = 32)
    private String customId;
    
    /** 开票银行 */
    @Column(name = "c_corp_invoice_bank", nullable = true, length = 100)
    private String corpInvoiceBank;
    
    /** 银行账号 */
    @Column(name = "c_corp_invoice_account", nullable = true, length = 20)
    private String corpInvoiceAccount;
    
    /** 银行状态 */
    @Column(name = "c_corp_invoice_bank_sta", nullable = true, length = 32)
    private String corpInvoiceBankSta;
    
    @Transient
	private String corpInvoiceBankStaGrid;
    
    public Object getCorpInvoiceBankStaGrid() {
		
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(corpInvoiceBankSta);
		if (obj != null) {
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return  lov;
	}
    
    public void setCorpInvoiceBankStaGrid(String corpInvoiceBankStaGrid) {
		this.corpInvoiceBankStaGrid = corpInvoiceBankStaGrid;
	}
    
    /** 创建人 */
    @Column(name = "c_created_by_id", nullable = true, length = 100)
    private String createdById;
    
    /** 创建时间 */
    @Column(name = "dt_created_at", nullable = true)
    private Date createdAt;
    
    /** 创建人岗位 */
    @Column(name = "c_created_pos_id", nullable = true, length = 100)
    private String createdPosId;
    
    /** 创建人组织 */
    @Column(name = "c_created_org_id", nullable = true, length = 100)
    private String createdOrgId;
    
    /** 更新者 */
    @Column(name = "c_updated_by_id", nullable = true, length = 100)
    private String updatedById;
    
    /** 更新时间 */
    @Column(name = "dt_updated_at", nullable = true)
    private Date updatedAt;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCustomId() {
		return customId;
	}

	public void setCustomId(String customId) {
		this.customId = customId;
	}

	public String getCorpInvoiceBank() {
		return corpInvoiceBank;
	}

	public void setCorpInvoiceBank(String corpInvoiceBank) {
		this.corpInvoiceBank = corpInvoiceBank;
	}

	public String getCorpInvoiceAccount() {
		return corpInvoiceAccount;
	}

	public void setCorpInvoiceAccount(String corpInvoiceAccount) {
		this.corpInvoiceAccount = corpInvoiceAccount;
	}

	public String getCorpInvoiceBankSta() {
		return corpInvoiceBankSta;
	}

	public void setCorpInvoiceBankSta(String corpInvoiceBankSta) {
		this.corpInvoiceBankSta = corpInvoiceBankSta;
	}

	public String getCreatedById() {
		return createdById;
	}

	public void setCreatedById(String createdById) {
		this.createdById = createdById;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedPosId() {
		return createdPosId;
	}

	public void setCreatedPosId(String createdPosId) {
		this.createdPosId = createdPosId;
	}

	public String getCreatedOrgId() {
		return createdOrgId;
	}

	public void setCreatedOrgId(String createdOrgId) {
		this.createdOrgId = createdOrgId;
	}

	public String getUpdatedById() {
		return updatedById;
	}

	public void setUpdatedById(String updatedById) {
		this.updatedById = updatedById;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}


}