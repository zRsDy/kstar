package com.ibm.kstar.log;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 接口日志
 * @author Administrator
 *
 */
@Entity
@Table(name = "SYS_T_INTERFACE_LOG")
public class MethodLogger implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ROW_ID", unique = true, nullable = false, length = 32)
	String id;
	
	/**
	 * 日志类型
	 */
	@Column(name = "T_LOG_TYPE")
	private String logType;
	
	/**
	 * 单号
	 */
	@Column(name = "ORDER_NUMBER")
	private String orderNumber;

	/**
	 * 接口调用时间
	 */
	@Column(name = "INTERFACE_START_DATE")
	private Date interfaceStartDate;
	
	/**
	 * 接口参数1
	 */
	@Column(name = "INTERFACE_PARAMETER1")
	private String interfaceParameter1;
	
	/**
	 * 接口参数2
	 */
	@Column(name = "INTERFACE_PARAMETER2")
	private String interfaceParameter2;
	
	/**
	 * 接口参数3
	 */
	@Column(name = "INTERFACE_PARAMETER3")
	private String interfaceParameter3;
	
	/**
	 * 接口参数4
	 */
	@Column(name = "INTERFACE_PARAMETER4")
	private String interfaceParameter4;
	
	/**
	 * 接口参数5
	 */
	@Column(name = "INTERFACE_PARAMETER5")
	private String interfaceParameter5;
	
	/**
	 * 接口名称1
	 */
	@Column(name = "INTERFACE_NAME1")
	private String interfaceName1;
	
	/**
	 * 接口名称2
	 */
	@Column(name = "INTERFACE_NAME2")
	private String interfaceName2;
	
	/**
	 * 接口名称3
	 */
	@Column(name = "INTERFACE_NAME3")
	private String interfaceName3;
	
	/**
	 * 接口名称4
	 */
	@Column(name = "INTERFACE_NAME4")
	private String interfaceName4;
	
	/**
	 * 接口名称5
	 */
	@Column(name = "INTERFACE_NAME5")
	private String interfaceName5;
	
	/**
	 * 接口返回数据1
	 */
	@Column(name = "INTERFACE_REPORT_DATA1")
	private String interfaceReturnData1;
	
	/**
	 * 接口返回数据2
	 */
	@Column(name = "INTERFACE_REPORT_DATA2")
	private String interfaceReturnData2;
	
	/**
	 * 接口返回数据3
	 */
	@Column(name = "INTERFACE_REPORT_DATA3")
	private String interfaceReturnData3;
	
	/**
	 * 接口返回数据4
	 */
	@Column(name = "INTERFACE_REPORT_DATA4")
	private String interfaceReturnData4;
	
	/**
	 * 接口返回数据5
	 */
	@Column(name = "INTERFACE_REPORT_DATA5")
	private String interfaceReturnData5;
	
	/**
	 * 接口返回时间
	 */
	@Column(name = "INTERFACE_REPORT_DATE")
	private Date interfaceReportDate;
	
	
	/**
	 * 处理状态
	 */
	@Column(name = "PROCESSING_STATUS")
	private String processingStatus;
	
	/**
	 * 处理结果
	 */
	@Column(name = "PROCESSING_REPORT")
	private String processingReport;

	/**
	 * 用户组织ID
	 */
	@Column(name = "USER_ORG_ID")
	private String userOrgId;

	/**
	 * 用户工号
	 */
	@Column(name = "USER_NUM")
	private String userNum;
	
	/**
	 * 用户名称
	 */
	@Column(name = "USER_NAME")
	private String userName;
	
	/**
	 * 用户岗位ID
	 */
	@Column(name = "USER_POSITION_ID")
	private String userPositionId;
	
	/**
	 * 用户岗位名称
	 */
	@Column(name = "USER_POSITION")
	private String userPosition;
	
	/**
	 * 用户组织名称
	 */
	@Column(name = "USER_ORG_NAME")
	private String userOrgName;
	
	/**
	 * 用户职位ID
	 */
	@Column(name = "USER_EMPLOYEE_ID")
	private String userEmployeeId;

	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Date getInterfaceStartDate() {
		return interfaceStartDate;
	}

	public void setInterfaceStartDate(Date interfaceStartDate) {
		this.interfaceStartDate = interfaceStartDate;
	}

	public String getInterfaceParameter1() {
		return interfaceParameter1;
	}

	public void setInterfaceParameter1(String interfaceParameter1) {
		this.interfaceParameter1 = interfaceParameter1;
	}

	public String getInterfaceParameter2() {
		return interfaceParameter2;
	}

	public void setInterfaceParameter2(String interfaceParameter2) {
		this.interfaceParameter2 = interfaceParameter2;
	}

	public String getInterfaceParameter3() {
		return interfaceParameter3;
	}

	public void setInterfaceParameter3(String interfaceParameter3) {
		this.interfaceParameter3 = interfaceParameter3;
	}

	public String getInterfaceParameter4() {
		return interfaceParameter4;
	}

	public void setInterfaceParameter4(String interfaceParameter4) {
		this.interfaceParameter4 = interfaceParameter4;
	}

	public String getInterfaceParameter5() {
		return interfaceParameter5;
	}

	public void setInterfaceParameter5(String interfaceParameter5) {
		this.interfaceParameter5 = interfaceParameter5;
	}

	public String getInterfaceName1() {
		return interfaceName1;
	}

	public void setInterfaceName1(String interfaceName1) {
		this.interfaceName1 = interfaceName1;
	}

	public String getInterfaceName2() {
		return interfaceName2;
	}

	public void setInterfaceName2(String interfaceName2) {
		this.interfaceName2 = interfaceName2;
	}

	public String getInterfaceName3() {
		return interfaceName3;
	}

	public void setInterfaceName3(String interfaceName3) {
		this.interfaceName3 = interfaceName3;
	}

	public String getInterfaceName4() {
		return interfaceName4;
	}

	public void setInterfaceName4(String interfaceName4) {
		this.interfaceName4 = interfaceName4;
	}

	public String getInterfaceName5() {
		return interfaceName5;
	}

	public void setInterfaceName5(String interfaceName5) {
		this.interfaceName5 = interfaceName5;
	}

	public String getInterfaceReturnData1() {
		return interfaceReturnData1;
	}

	public void setInterfaceReturnData1(String interfaceReturnData1) {
		this.interfaceReturnData1 = interfaceReturnData1;
	}

	public String getInterfaceReturnData2() {
		return interfaceReturnData2;
	}

	public void setInterfaceReturnData2(String interfaceReturnData2) {
		this.interfaceReturnData2 = interfaceReturnData2;
	}

	public String getInterfaceReturnData3() {
		return interfaceReturnData3;
	}

	public void setInterfaceReturnData3(String interfaceReturnData3) {
		this.interfaceReturnData3 = interfaceReturnData3;
	}

	public String getInterfaceReturnData4() {
		return interfaceReturnData4;
	}

	public void setInterfaceReturnData4(String interfaceReturnData4) {
		this.interfaceReturnData4 = interfaceReturnData4;
	}

	public String getInterfaceReturnData5() {
		return interfaceReturnData5;
	}

	public void setInterfaceReturnData5(String interfaceReturnData5) {
		this.interfaceReturnData5 = interfaceReturnData5;
	}

	public Date getInterfaceReportDate() {
		return interfaceReportDate;
	}

	public void setInterfaceReportDate(Date interfaceReportDate) {
		this.interfaceReportDate = interfaceReportDate;
	}

	public String getProcessingStatus() {
		return processingStatus;
	}

	public void setProcessingStatus(String processingStatus) {
		this.processingStatus = processingStatus;
	}

	public String getProcessingReport() {
		return processingReport;
	}

	public void setProcessingReport(String processingReport) {
		this.processingReport = processingReport;
	}

	public String getUserOrgId() {
		return userOrgId;
	}

	public void setUserOrgId(String userOrgId) {
		this.userOrgId = userOrgId;
	}

	public String getUserNum() {
		return userNum;
	}

	public void setUserNum(String userNum) {
		this.userNum = userNum;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPositionId() {
		return userPositionId;
	}

	public void setUserPositionId(String userPositionId) {
		this.userPositionId = userPositionId;
	}

	public String getUserPosition() {
		return userPosition;
	}

	public void setUserPosition(String userPosition) {
		this.userPosition = userPosition;
	}

	public String getUserOrgName() {
		return userOrgName;
	}

	public void setUserOrgName(String userOrgName) {
		this.userOrgName = userOrgName;
	}

	public String getUserEmployeeId() {
		return userEmployeeId;
	}

	public void setUserEmployeeId(String userEmployeeId) {
		this.userEmployeeId = userEmployeeId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
	
}
