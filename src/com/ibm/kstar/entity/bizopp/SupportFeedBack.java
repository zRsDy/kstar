package com.ibm.kstar.entity.bizopp;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.cache.CacheData;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by wangchao on 2017/3/27.
 */
@Entity
@Table(name = "CRM_T_SUPPORT_FEEDBACK")
public class SupportFeedBack implements Serializable {

	@Column(name = "row_id")
	@Id
	@GeneratedValue(generator = "crm_t_biz_support_feedback_id_generator")
	@GenericGenerator(name = "crm_t_biz_support_feedback_id_generator", strategy = "uuid")
	private String id;

	@Column(name = "biz_id")
	private String businessId;

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	@Column(name = "sales")
	private String sales;

	@Transient
	private String saleName;

	@Column(name = "distribute_date")
	private Date distributeDate;

	@Column(name = "support_begin")
	private Date supportBegin;

	@Column(name = "support_end")
	private Date supportEnd;

	@Column(name = "result_desc")
	private String resultDesc;

	/** 效果概述 */
	@Column(name = "c_event_result", nullable = true, length = 100)
	private String eventResult;
	
	/**具体交流内容*/
	@Column(name = "C_CONCRETE_CONTENT", nullable = true,length = 1000)
	private String concreteContent;
	
	/**客户关注问题*/
	@Column(name = "C_ATTENTION_TROUBLE", nullable = true,length = 2000)
	private String attentionTrouble;
	
	/**客户待解决问题*/
	@Column(name = "C_SOLVE_TROUBLE", nullable = true,length = 2000)
	private String solveTrouble;
	
	/**客户需要提交资料及截至时间*/
	@Column(name = "C_CUSTOM_DATA", nullable = true,length = 1000)
	private String customData;
	
	@Transient
	private String eventResultName;

	public String getEventResultName() {
		LovMember lov = ((LovMember) CacheData.getInstance().get(eventResult));
		return lov == null ? null : lov.getName();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSales() {
		return sales;
	}

	public void setSales(String sales) {
		this.sales = sales;
	}

	public Date getDistributeDate() {
		return distributeDate;
	}

	public void setDistributeDate(Date distributeDate) {
		this.distributeDate = distributeDate;
	}

	public Date getSupportBegin() {
		return supportBegin;
	}

	public void setSupportBegin(Date supportBegin) {
		this.supportBegin = supportBegin;
	}

	public Date getSupportEnd() {
		return supportEnd;
	}

	public void setSupportEnd(Date supportEnd) {
		this.supportEnd = supportEnd;
	}

	public String getResultDesc() {
		return resultDesc;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}

	public String getSaleName() {
		Employee e = (Employee) CacheData.getInstance().get(sales);
		if (e != null) {
			return e.getName();
		}
		return "";
	}

	public void setSaleName(String saleName) {
		this.saleName = saleName;
	}

	public String getEventResult() {
		return eventResult;
	}

	public void setEventResult(String eventResult) {
		this.eventResult = eventResult;
	}

	public void setEventResultName(String eventResultName) {
		this.eventResultName = eventResultName;
	}

	public String getConcreteContent() {
		return concreteContent;
	}

	public void setConcreteContent(String concreteContent) {
		this.concreteContent = concreteContent;
	}

	public String getAttentionTrouble() {
		return attentionTrouble;
	}

	public void setAttentionTrouble(String attentionTrouble) {
		this.attentionTrouble = attentionTrouble;
	}

	public String getSolveTrouble() {
		return solveTrouble;
	}

	public void setSolveTrouble(String solveTrouble) {
		this.solveTrouble = solveTrouble;
	}

	public String getCustomData() {
		return customData;
	}

	public void setCustomData(String customData) {
		this.customData = customData;
	}
	
	
}
