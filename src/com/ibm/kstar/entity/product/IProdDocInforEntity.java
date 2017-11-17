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
@Table(name = "CRM_INT_PROD_DOC_INFOR")
public class IProdDocInforEntity {

	/** 主键 **/
	@Id
	@GeneratedValue(generator = "CRM_INT_PROD_DOC_INFOR_generator")
	@GenericGenerator(name = "CRM_INT_PROD_DOC_INFOR_generator", strategy = "uuid")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	private String id;

	/** 产品物料编号 */
	@Column(name = "C_MATER_CODE", nullable = false, length = 128)
	@NotNull(message = " 物料号不能为空.")
	private String lh;

	/** 文档名称 **/
	@Column(name = "C_DOC_NAME", nullable = false, length = 256)
	@NotNull(message = " 文档名称不能为空.")
	private String wdmc;

	/** 文档说明 **/
	@Column(name = "C_DOC_DESC", nullable = false, length = 256)
	@NotNull(message = " 文档说明不能为空.")
	private String wdsm;

	/** 申请人 **/
	@Column(name = "C_APPLY_PERSON", nullable = true, length = 32)
	private String sqr;

	/** 文档类型 **/
	@Column(name = "DOC_TYPE", nullable = false, length = 16)
	@NotNull(message = " 文档类型不能为空.")
	private String wdlx;

	/** 申请时间 **/
	@Column(name = "DT_APPLY_DATE", nullable = true)
	private Date sqsj;

	/** 状态 **/
	@Column(name = "DOC_STATUS", nullable = false, length = 16)
	@NotNull(message = " 状态不能为空.")
	private String zt;

	/** 上传人 **/
	@Column(name = "C_UPLOAD_PERSON", nullable = false, length = 32)
	@NotNull(message = " 上传人不能为空.")
	private String scr;

	/** 上传时间 **/
	@Column(name = "DT_UPLOAD_DATE", nullable = false, length = 32)
	@NotNull(message = " 上传时间不能为空.")
	private Date scsj;

	/** 文档附件 **/
	@Column(name = "DOC_ATTACH", nullable = false, length = 1024)
	@NotNull(message = " 文档附件不能为空.")
	private String wdfj;

	/** 数据状态 **/
	@Column(name = "DATA_STATUS", nullable = false, length = 1)
	@NotNull(message = " 数据状态不能为空.")
	private String datastatus;

	/** 接口状态 **/
	@Column(name = "INT_STATUS", nullable = false, length = 1)
	@NotNull(message = " 接口状态不能为空.")
	private String intstatus;

	/** 接口处理消息 **/
	@Column(name = "INT_MESSAGE", nullable = true)
	private String intmessage;

	/** 创建人 */
	@Column(name = "CREATED_BY_ID", nullable = false, length = 100)
	@NotNull(message = " 创建人不能为空.")
	private String creator;

	/** 创建时间 */
	@Column(name = "CREATED_AT", nullable = false)
	@NotNull(message = " 创建时间不能为空.")
	private Date createtime;

	/** 更新者 */
	@Column(name = "UPDATED_BY_ID", nullable = false, length = 100)
	@NotNull(message = " 更新者不能为空.")
	private String updator;

	/** 更新时间 */
	@Column(name = "UPDATED_AT", nullable = false)
	@NotNull(message = " 更新时间不能为空.")
	private Date updatetime;

	@Transient
	private String reMessage;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLh() {
		return lh;
	}

	public void setLh(String lh) {
		this.lh = lh;
	}

	public String getWdmc() {
		return wdmc;
	}

	public void setWdmc(String wdmc) {
		this.wdmc = wdmc;
	}

	public String getWdsm() {
		return wdsm;
	}

	public void setWdsm(String wdsm) {
		this.wdsm = wdsm;
	}

	public String getSqr() {
		return sqr;
	}

	public void setSqr(String sqr) {
		this.sqr = sqr;
	}

	public String getWdlx() {
		return wdlx;
	}

	public void setWdlx(String wdlx) {
		this.wdlx = wdlx;
	}

	public Date getSqsj() {
		return sqsj;
	}

	public void setSqsj(Date sqsj) {
		this.sqsj = sqsj;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getScr() {
		return scr;
	}

	public void setScr(String scr) {
		this.scr = scr;
	}

	public Date getScsj() {
		return scsj;
	}

	public void setScsj(Date scsj) {
		this.scsj = scsj;
	}

	public String getWdfj() {
		return wdfj;
	}

	public void setWdfj(String wdfj) {
		this.wdfj = wdfj;
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
	
	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public String getReMessage() {
		return reMessage;
	}

	public void setReMessage(String reMessage) {
		this.reMessage = reMessage;
	}

	@Override
	public String toString() {
		return BeanUtils.printBean(this);
	}

}
