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
@Table(name = "CRM_INT_ECR_REQM")
public class IEcrReqmEntity {

	/** 主键 **/
	@Id
	@GeneratedValue(generator = "CRM_INT_ECR_REQM_generator")
	@GenericGenerator(name = "CRM_INT_ECR_REQM_generator", strategy = "uuid")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	private String id;

	/** CRM ECR需求单号 **/
	@Column(name = "C_ECR_CODE", nullable = false, length = 20)
	@NotNull(message = " CRM ECR需求单号不能为空.")
	private String ecrxqdh;

	/** 物料号 **/
	@Column(name = "C_MATER_CODE", length = 12)
	private String lh;

	/** 紧急程度 **/
	@Column(name = "C_ERGENT", nullable = false, length = 255)
	@NotNull(message = " 紧急程度不能为空.")
	private String jjcd;

	/** 变更内容/项目 **/
	@Column(name = "C_ECR_CHANGE_CONTENT", nullable = false, length = 100)
	@NotNull(message = " 变更内容/项目不能为空.")
	private String bgnrbgsj;

	/** 变更原因 **/
	@Column(name = "C_ECR_CHANGE_REASON", nullable = false, length = 100)
	@NotNull(message = " 变更原因不能为空.")
	private String bgyy;

	/** 变更原因类型 **/
	@Column(name = "C_ECR_CHANGE_TYPE", nullable = true, length = 255)
	private String bglx;

	/** 参考已有ECR **/
	@Column(name = "C_REFER_ECR_CODE", nullable = true, length = 20)
	private String ckyyecr;

	/** 申请人 **/
	@Column(name = "C_ECR_APPLY", nullable = false, length = 30)
	@NotNull(message = " 申请人不能为空.")
	private String sqr;

	/** ECR提交时间 **/
	@Column(name = "DT_CREATE_TIME", nullable = false)
	@NotNull(message = " ECR提交时间不能为空.")
	private Date dt_create_time;
	
	/** ECR生效时间 **/
	@Column(name = "DT_EFF_TIME", nullable = true)
	private Date dt_eff_time;
	
	/** 数据状态 **/
	@Column(name = "DATA_STATUS", nullable = false, length = 1)
	@NotNull(message = " DATA_STATUS is null.")
	private String datastatus;

	/** 接口状态 **/
	@Column(name = "INT_STATUS", nullable = false, length = 1)
	@NotNull(message = " INT_STATUS is null.")
	private String intstatus;

	/** 接口处理消息 **/
	@Column(name = "INT_MESSAGE", nullable = true)
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
	
	/** PDMECR需求单号 */
	@Column(name = "PDM_ECR_CODE", nullable = true, length = 128)
	private String pdmecrdh;

	/** 批复单据状态 **/
	@Column(name = "APPR_STATUS", nullable = true, length = 1)
	private String djzt;
	
	/** ECN单号 **/
	@Column(name = "ECN_NO", nullable = true, length = 128)
	private String ecn_no;
	
	/** 需求单申请失败处理原因  **/
	@Column(name = "R_REASON", nullable = true, length = 1024)
	private String sbyy;
	
	@Transient
	private String reMessage;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEcrxqdh() {
		return ecrxqdh;
	}

	public void setEcrxqdh(String ecrxqdh) {
		this.ecrxqdh = ecrxqdh;
	}

	public String getLh() {
		return lh;
	}

	public void setLh(String lh) {
		this.lh = lh;
	}

	public String getJjcd() {
		return jjcd;
	}

	public void setJjcd(String jjcd) {
		this.jjcd = jjcd;
	}

	public String getBgnrbgsj() {
		return bgnrbgsj;
	}

	public void setBgnrbgsj(String bgnrbgsj) {
		this.bgnrbgsj = bgnrbgsj;
	}

	public String getBgyy() {
		return bgyy;
	}

	public void setBgyy(String bgyy) {
		this.bgyy = bgyy;
	}

	public String getBglx() {
		return bglx;
	}

	public void setBglx(String bglx) {
		this.bglx = bglx;
	}

	public String getCkyyecr() {
		return ckyyecr;
	}

	public void setCkyyecr(String ckyyecr) {
		this.ckyyecr = ckyyecr;
	}

	public String getSqr() {
		return sqr;
	}

	public void setSqr(String sqr) {
		this.sqr = sqr;
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

	public void setIntmessage(String intmessage) {
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

	public String getPdmecrdh() {
		return pdmecrdh;
	}

	public void setPdmecrdh(String pdmecrdh) {
		this.pdmecrdh = pdmecrdh;
	}

	public String getDjzt() {
		return djzt;
	}

	public void setDjzt(String djzt) {
		this.djzt = djzt;
	}

	public String getReMessage() {
		return reMessage;
	}

	public void setReMessage(String reMessage) {
		this.reMessage = reMessage;
	}
	
	public Date getDt_create_time() {
		return dt_create_time;
	}

	public void setDt_create_time(Date dt_create_time) {
		this.dt_create_time = dt_create_time;
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

	public Date getDt_eff_time() {
		return dt_eff_time;
	}

	public void setDt_eff_time(Date dt_eff_time) {
		this.dt_eff_time = dt_eff_time;
	}
	
	public String getEcn_no() {
		return ecn_no;
	}

	public void setEcn_no(String ecn_no) {
		this.ecn_no = ecn_no;
	}
	
	public String getSbyy() {
		return sbyy;
	}

	public void setSbyy(String sbyy) {
		this.sbyy = sbyy;
	}

	@Override
	public String toString() {
		return BeanUtils.printBean(this);
	}

}
