package com.ibm.kstar.entity.channel;


import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.ibm.kstar.entity.BaseEntity;

/**
 * 活动费用表(crm_t_activity_expense)
 * 
 * @author lhm
 * @version 1.0.0 2017-02-06
 */
@Entity
@Table(name = "crm_t_activity_expense")
public class ActivityExpense extends BaseEntity implements java.io.Serializable {
    /** 版本号 */
	private static final long serialVersionUID = 1424170285704997986L;

	/** 主键自增 */
    @Id
    @Column(name = "c_pid", unique = true, nullable = false)
    @GeneratedValue(generator = "activity_expense_c_pid_generator")
   	@GenericGenerator(name="activity_expense_c_pid_generator", strategy="uuid")
    private String id;
    
    /** 申请单号 */
    @Column(name = "c_apply_id")
    private String applyId;
    
    /** 费用项目 */
    @Column(name = "c_expense_project")
    private String expenseProject;
    
    /** 预算费用 */
    @Column(name = "c_estimated_expense")
    private BigDecimal estimatedExpense;
	
    /** 实际费用 */
    @Column(name = "c_actual_expense")
    private BigDecimal actualExpense;
    
    /** 承办单位 */
    @Column(name = "c_organizer")
    private String organizer;
    
    /** 是否需收取 */
    @Column(name = "c_charge_expense")
    private String chargeExpense;
    
    /** 说明 */
    @Column(name = "c_explain")
    private String explain;

    public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApplyId() {
		return applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	public String getExpenseProject() {
		return expenseProject;
	}

	public void setExpenseProject(String expenseProject) {
		this.expenseProject = expenseProject;
	}

	public BigDecimal getEstimatedExpense() {
		return estimatedExpense;
	}

	public void setEstimatedExpense(BigDecimal estimatedExpense) {
		this.estimatedExpense = estimatedExpense;
	}

	public BigDecimal getActualExpense() {
		return actualExpense;
	}

	public void setActualExpense(BigDecimal actualExpense) {
		this.actualExpense = actualExpense;
	}

	public String getOrganizer() {
		return organizer;
	}

	public void setOrganizer(String organizer) {
		this.organizer = organizer;
	}

	public String getOrganizerName() {
		return this.getLovName(organizer);
	}

	public String getChargeExpense() {
		return chargeExpense;
	}

	public void setChargeExpense(String chargeExpense) {
		this.chargeExpense = chargeExpense;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

}