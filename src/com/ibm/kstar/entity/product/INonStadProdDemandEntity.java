package com.ibm.kstar.entity.product;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;
import org.xsnake.web.utils.BeanUtils;

@Entity
@Table(name = "CRM_INT_NONSTAD_PROD_DEMAND")
public class INonStadProdDemandEntity{
	
	/** 主键 **/
	@Id
	@GeneratedValue(generator = "CRM_INT_NONSTAD_PROD_DEMAND_generator")
	@GenericGenerator(name = "CRM_INT_NONSTAD_PROD_DEMAND_generator", strategy = "uuid")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	private String id;
	
	/** 需求单号 **/
	@Column(name="C_DEMAND_CODE", nullable = false, length = 32)
	@NotNull(message=" 需求单号不能为空.")
	private String crmsqdh;
	
	/** 预定义物料号 **/
	@Column(name="C_VMATER_CODE", nullable = false, length = 32)
	@NotNull(message=" 预定义物料号不能为空.")
	private String ydywlh;
	
	/** 需求单名称 **/
	@Column(name="C_DEMAND_NAME", nullable = false, length = 32)
	@NotNull(message=" 需求单名称不能为空.")
	private String xqdm;
	
	/** 客户名称  **/
	@Column(name="C_CUSTOM_FULL_NAME", nullable = false, length = 512)
	@NotNull(message=" 客户名称不能为空.")
	private String kh;
	
	/** 客户所在地  **/
	@Column(name="C_CUSTOM_ADDRESS", nullable = false, length = 512)
	private String khszd;
	
	/** 客户PO/合同号 **/
	@Column(name="C_CLIENT_CONTRACT", nullable = true, length = 512)
	private String khpo;
	
	/** 期望交货日期  **/
	@Column(name="DT_HOPEDELIVER_DATE", nullable = false)
	@NotNull(message=" 期望交货日期不能 为空.")
	private Date dt_hopedeliver_date;
	
	/** 需求种类  **/
	@Column(name="C_DEMAND_CATEGORY", nullable = false, length = 8)
	@NotNull(message=" 需求种类不能为空.")
	private String xqzl;
	
	/** 内部产品型号 **/
	@Column(name="C_PROD_MODEL", nullable = true, length = 128)
	private String nbxh;
	
	/** 需求数量 **/
	@Column(name="N_DEMAND_QTY", nullable = false)
	@NotNull(message=" 需求数量不能为空.")
	private Long sl;
	
	/** 需要提前备料  **/
	@Column(name="C_PREPARE_BEFORE", nullable = false, length = 1)
	@NotNull(message=" 需要提前备料不能为空.")
	private String sfbl;
	
	/** 需求部门 **/
	@Column(name="C_DEMAND_DEPT", nullable = false, length = 256)
	@NotNull(message=" 需求部门不能为空.")
	private String sqbm;
	 
	/** 需求人 **/
	@Column(name="C_DEMAND_PERSON", nullable = false, length = 256)
	@NotNull(message=" 需求人不能为空.")
	private String sqr;
	
	/** 联系电话  **/
	@Column(name="C_TEL", nullable = true, length = 32)
	private String tel;
	
	/** 特殊产品需求清单文件路径 **/
	@Column(name="C_SPEC_REQUIRE_LIST", length = 512)
	private String tgburl;
	
	/** 客户资料文件路径 **/
	@Column(name="C_CLIENT_DOC", nullable = true, length = 512)
	private String fjurl;
	
	/** CRM产品类别  **/
	@Column(name ="C_PRO_CRM_CATEGORY", nullable = false, length = 32)
	@NotNull(message=" CRM产品类别不能为空.")
	private String crmcplx;
	
	/** 外部型号 **/
	@Column(name ="C_CLIENT_CATEGORY", nullable = true, length = 12)
	private String wbxh;
	
	/** 数据状态  **/
	@Column(name="DATA_STATUS", nullable = false, length = 1)
	@NotNull(message=" DATA_STATUS is null.")
	private String datastatus;
			
	/** 接口状态  **/
	@Column(name="INT_STATUS", nullable = false, length = 1)
	@NotNull(message=" INT_STATUS is null.")
	private String intstatus;
	
	/** 接口处理消息 **/
	@Column(name ="INT_MESSAGE", nullable = true)
	private String intmessage;
	
	 /** 创建人 */
    @Column(name = "CREATED_BY_ID", nullable = true, length = 100)
    private String creator;
    
    /** 创建时间 */
    @Column(name = "CREATED_AT", nullable = true)
    private Date created_at;
    
    /** 更新者 */
    @Column(name = "UPDATED_BY_ID", nullable = true, length = 100)
    private String updator;
    
    /** 更新时间 */
    @Column(name = "UPDATED_AT", nullable = true)
    private Date updated_at;
    
    /** PDM产品物料编号  */
    @Column(name = "PDM_VMATER_CODE", nullable = true, length = 64)
    private String pdmwlh;
    
    /** 批复单据状态 **/
	@Column(name = "APPR_STATUS", nullable = true, length = 1)
	private String djzt;
	
    /** 需求单申请失败处理原因  **/
	@Column(name = "R_REASON", nullable = true, length = 1024)
	private String sbyy;
	
	@Column(name = "TGBMC", nullable = true, length = 1024)
	private String tgbmc;
	
	@Column(name = "FJMC", nullable = true, length = 1024)
	private String fjmc;
	
   /** 产品系列/型号  **/
	@Column(name = "C_PRO_SERIES_OR_MODEL", nullable = false, length = 600)
	@NotNull(message=" 产品系列/型号不能为空.")
	private String xlxh;

    @Transient
    private String reMessage;
    
    @Transient
    private Boolean result = true;
    
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCrmsqdh() {
		return crmsqdh;
	}

	public void setCrmsqdh(String crmsqdh) {
		this.crmsqdh = crmsqdh;
	}

	public String getYdywlh() {
		return ydywlh;
	}

	public void setYdywlh(String ydywlh) {
		this.ydywlh = ydywlh;
	}

	public String getXqdm() {
		return xqdm;
	}

	public void setXqdm(String xqdm) {
		this.xqdm = xqdm;
	}

	public String getKh() {
		return kh;
	}

	public void setKh(String kh) {
		this.kh = kh;
	}

	public String getKhszd() {
		return khszd;
	}

	public void setKhszd(String khszd) {
		this.khszd = khszd;
	}

	public String getKhpo() {
		return khpo;
	}

	public void setKhpo(String khpo) {
		this.khpo = khpo;
	}

	public String getXqzl() {
		return xqzl;
	}

	public void setXqzl(String xqzl) {
		this.xqzl = xqzl;
	}

	public String getNbxh() {
		return nbxh;
	}

	public void setNbxh(String nbxh) {
		this.nbxh = nbxh;
	}

	public Long getSl() {
		return sl;
	}

	public void setSl(Long sl) {
		this.sl = sl;
	}

	public String getSfbl() {
		return sfbl;
	}

	public void setSfbl(String sfbl) {
		this.sfbl = sfbl;
	}

	public String getSqbm() {
		return sqbm;
	}

	public void setSqbm(String sqbm) {
		this.sqbm = sqbm;
	}

	public String getSqr() {
		return sqr;
	}

	public void setSqr(String sqr) {
		this.sqr = sqr;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getTgburl() {
		return tgburl;
	}

	public void setTgburl(String tgburl) {
		this.tgburl = tgburl;
	}

	public String getFjurl() {
		return fjurl;
	}

	public void setFjurl(String fjurl) {
		this.fjurl = fjurl;
	}

	public String getCrmcplx() {
		return crmcplx;
	}

	public void setCrmcplx(String crmcplx) {
		this.crmcplx = crmcplx;
	}

	public String getWbxh() {
		return wbxh;
	}

	public void setWbxh(String wbxh) {
		this.wbxh = wbxh;
	}

	public String getDatastatus() {
		return datastatus;
	}

	public void setDatastatus(String datastatus) {
		this.datastatus = datastatus;
	}

	public String getIntstatus() {
		return intstatus;
	}

	public void setIntstatus(String intstatus) {
		this.intstatus = intstatus;
	}

	public String getIntmessage() {
		return intmessage;
	}

	public void setIntMessage(String intmessage) {
		this.intmessage = intmessage;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getUpdator() {
		return updator;
	}

	public void setUpdator(String updator) {
		this.updator = updator;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}
	
	public String getReMessage() {
		return reMessage;
	}

	public void setReMessage(String reMessage) {
		this.reMessage = reMessage;
	}

	public void setIntmessage(String intmessage) {
		this.intmessage = intmessage;
	}
	
	public String getPdmwlh() {
		return pdmwlh;
	}

	public void setPdmwlh(String pdmwlh) {
		this.pdmwlh = pdmwlh;
	}
	
	public String getDjzt() {
		return djzt;
	}

	public void setDjzt(String djzt) {
		this.djzt = djzt;
	}

	public Date getDt_hopedeliver_date() {
		return dt_hopedeliver_date;
	}

	public void setDt_hopedeliver_date(Date dt_hopedeliver_date) {
		this.dt_hopedeliver_date = dt_hopedeliver_date;
	}
	
	public String getSbyy() {
		return sbyy;
	}
	
	public void setSbyy(String sbyy) {
		this.sbyy = sbyy;
	}
	
	public String getXlxh() {
		return xlxh;
	}

	public void setXlxh(String xlxh) {
		this.xlxh = xlxh;
	}

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return BeanUtils.printBean(this);
	}

	public String getTgbmc() {
		return tgbmc;
	}

	public void setTgbmc(String tgbmc) {
		this.tgbmc = tgbmc;
	}

	public String getFjmc() {
		return fjmc;
	}

	public void setFjmc(String fjmc) {
		this.fjmc = fjmc;
	}
	
}
