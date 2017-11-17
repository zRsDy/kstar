package com.ibm.kstar.action.common;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;

public interface IConstants {
	public static final String YMD_FORMAT_1 = "yyyy-MM-dd"; 
	public static final String YMDHMS_FORMAT_1 = "yyyyMMddHHmmsss";
	public static final String YMD_FORMAT_2 = "yyyy-MM-dd HH：mm：ss";
	
	public static final String YES = "1";// 是
	public static final String NO = "0";// 否
	
	public static final String YES_Yes = "Yes";// 是
	public static final String NO_No = "No";// 否
	
	public static final String ACTION_TYPE_ADD = "ADD"; // 画面模式：新增
	public static final String ACTION_TYPE_EDIT = "EDIT"; // 画面模式：编辑
	
	public static final String ADDRESSREGION = "ADDRESSREGION"; // ADDRESSREGION
	public static final String ADDRESSREGION_CN = "CN"; // ADDRESSREGION：中国
	
	public static final String PRM_FLAG = "PRM"; // 
	public static final String CRM_FLAG = "CRM"; // 

	/** 客户管理审批ID&权限等关键字 */ 
	public static final String CUSTOM_EVENT_PROC     = "CUSTOM_EVENT_PROC"     ; // 来访接待
	public static final String CUSTOM_HANDOVER_PROC  = "CUSTOM_HANDOVER_PROC"  ; // 客户交接
	public static final String CUSTOM_REPORT_PROC    = "CUSTOM_REPORT_PROC"    ; // 启动报备
	public static final String CUSTOM_ERP_PROC       = "CUSTOM_ERP_PROC"       ; // 提交引入申请
	public static final String CUSTOM_SHARE_PROC     = "CUSTOM_SHARE_PROC"     ; // 共享授权
	public static final String CUSTOM_ADJUST_PROC    = "CUSTOM_ADJUST_PROC"    ; // 评估调整
	public static final String CUSTOM_COMPETITOR_PROC= "CUSTOM_COMPETITOR_PROC"; // 竞争对手
	public static final String CUSTOM_MERGE_PROC     = "CUSTOM_MERGE_PROC"     ; // 客户合并
	public static final String CUSTOM_CHANGE_PROC    = "CUSTOM_CHANGE_PROC"    ; // 客户基本信息变更

	
	public static final String CUSTOM_REPORT_PRM_PROC= "CUSTOM_REPORT_PRM_PROC"; // 经销商客户模块：客户报备
	public static final String CUSTOM_BIZRPT_PRM_PROC= "CUSTOM_BIZRPT_PRM_PROC"; // 经销商客户模块：商机报备
	
	public static final String CUSTOM_RELA_CONTACT   = "CUSTOM_RELA_CONTACT"   ; // 客户联系人

	/** 客户关系 */ 
	public static final String LEV_DOWN              = "levdown"               ; // 上级
	public static final String LEV_UP                = "levup"                 ; // 下级
	public static final String LEV_COMPITER          = "levcompiter"           ; // 竞争对手
	public static final String LEV_PARTNER           = "levpartner"            ; // 合作伙伴
	
	public static final String LEV_DOWN_DISP         = "下级"                   ; // 上级
	public static final String LEV_UP_DISP           = "上级"                   ; // 下级
	public static final String LEV_COMPITER_DISP     = "竞争对手"                ; // 竞争对手
	public static final String LEV_PARTNER_DISP      = "合作伙伴"                ; // 合作伙伴
	
	/** 客户管理模块一般审批 */ 
	public static final String CUSTOM_NORMAL_STATUS_10 = "CUSTOM_NORMAL_STATUS_10";// 工作流状态未启动 
	public static final String CUSTOM_NORMAL_STATUS_20 = "CUSTOM_NORMAL_STATUS_20";// 工作流状态审批中 
	public static final String CUSTOM_NORMAL_STATUS_30 = "CUSTOM_NORMAL_STATUS_30";// 工作流状态驳回 
	public static final String CUSTOM_NORMAL_STATUS_40 = "CUSTOM_NORMAL_STATUS_40";// 工作流状态审批完成
	
	/** 客户管理模块客户控制状态（业务状态） */ 
	public static final String CONTROLSTATUS_01 = "CONTROLSTATUS_01";// 有效 
	public static final String CONTROLSTATUS_02 = "CONTROLSTATUS_02";// 无效 
	public static final String CONTROLSTATUS_03 = "CONTROLSTATUS_03";// 变更待复核
	
//	public static final String CUSTOM_BASE = "CUSTOMBASE";// 业务类型：客户基本属性
	public static final String CUSTOM_ORG_TREE = "CUSTOMORGTREE";// 客户组织纬度树
//	public static final String CUSTOM_HANDOVER = "CUSTOMHANDOVER";// 业务类型：客户交接
//	public static final String CUSTOM_ADJUST = "CUSTOMADJUST";// 业务类型：客户评估调整
	
	/************************** 订单管理模块常量定义 -------- Start ---------************/ 
	/** 订单来源-合同*/
	public static final String ORDER_SOURCE_CONTRACT = "10";// 从合同创建订单
	/** 订单来源-代理商*/
	public static final String ORDER_SOURCE_BIZ = "20";// 代理商
	/** 订单来源-手工*/
	public static final String ORDER_SOURCE_CREATE = "30";// 手工创建
	/** 订单来源-初始版本*/
	public static final int ORDER_VERSION = 1;
	
	
	/** 订单汇率类型-公司*/
	public static final String ORDER_EXCHANGE_RATE_10 = "10";
	/** 订单汇率类型-期间*/
	public static final String ORDER_EXCHANGE_RATE_20 = "20";
	/** 订单汇率类型-用户 */
	public static final String ORDER_EXCHANGE_RATE_30 = "30";
	
	/** 订单执行状态-已录入 */
	public static final String ORDER_EXECUTE_STATUS_ENTERED = "ENTERED";// 订单执行状态-已录入
	/** 订单执行状态-已登记 */
	public static final String ORDER_EXECUTE_STATUS_BOOKED = "BOOKED";// 订单执行状态-已登记
	/** 订单执行状态-已取消 */
	public static final String ORDER_EXECUTE_STATUS_CANCELLED = "CANCELLED";// 订单执行状态-已取消
	/** 订单执行状态-已关闭 */
	public static final String ORDER_EXECUTE_STATUS_CLOSED = "CLOSED";// 订单执行状态-已关闭
	/** 订单执行状态-已终止 */
	public static final String ORDER_EXECUTE_STATUS_TERMINATED = "TERMINATED";// 订单执行状态-已终止
	
	/** 订单控制状态-拟定 */
	public static final String ORDER_CONTROL_STATUS_10 = "10";
	/** 订单控制状态-审批中 */
	public static final String ORDER_CONTROL_STATUS_20 = "20";
	/** 订单控制状态-已批准*/
	public static final String ORDER_CONTROL_STATUS_30 = "30";
	/** 订单控制状态-已拒绝 */
	public static final String ORDER_CONTROL_STATUS_40 = "40";
	/** 订单控制状态-已暂挂 */
	public static final String ORDER_CONTROL_STATUS_50 = "50";
	/** 订单控制状态-已释放 */
	public static final String ORDER_CONTROL_STATUS_60 = "60";
	/** 订单控制状态-已销毁 */
	public static final String ORDER_CONTROL_STATUS_70 = "70";
	
	
	/** 订单行状态-已录入 */
	public static final String ORDER_LINE_STATUS_ENTERED = "ENTERED";
	/** 订单行状态-已登记 */
	public static final String ORDER_LINE_BOOKED = "BOOKED";
	/** 订单行状态-等待发运 */
	public static final String ORDER_LINE_STATUS_AWAITING_SHIPPING = "AWAITING_SHIPPING";
	/** 订单行状态-已发货  */
	public static final String ORDER_LINE_STATUS_PICKED = "PICKED";
	/** 订单行状态-已开票  */
	public static final String ORDER_LINE_STATUS_CLOSED = "CLOSED";
	/** 订单行状态-已取消  */
	public static final String ORDER_LINE_STATUS_CANCELLED = "CANCELLED";
	
	
	/** 开票申请类型-提前开票 */
	public static final String INVOICE_TYPE_01 = "01";//发货后开票
	/** 开票申请类型-发货后开票 */
	public static final String INVOICE_TYPE_02 = "02";//提前开票
	

	/** 发票类型-增值税专用发票 */
	public static final String INVOICETYPE_01 = "01";//发货后开票
	/** 发票类型-普通发票 */
	public static final String INVOICETYPE_02 = "02";//提前开票
	

	/**  收款单状态-已取消 */
	public static final String RECEIPT_STATUS_R20 = "-20";
	/**  收款单状态-业务退回 */
	public static final String RECEIPT_STATUS_R10 = "-10";
	/**  收款单状态-新建 */
	public static final String RECEIPT_STATUS_10 = "10";
	/**  收款单状态-已发布 */
	public static final String RECEIPT_STATUS_20 = "20";
	/**  收款单状态-部分核销*/
	public static final String RECEIPT_STATUS_30 = "30";
	/**  收款单状态-已核销*/
	public static final String RECEIPT_STATUS_40 = "40";
	/**  收款单状态-已确认 */
	public static final String RECEIPT_STATUS_50 = "50";
	
	/** 订单流程业务类型-订单 */
	public static final String PERMISSION_BUSINESS_TYPE_ORDER = "ORDER";
	/** 订单流程业务类型-订单变更 */
	public static final String PERMISSION_BUSINESS_TYPE_ORDER_CHANGE = "ORDERCHANGE";
	/** 订单流程业务类型-发货单 */
	public static final String PERMISSION_BUSINESS_TYPE_DELIVERY = "DELIVERY";
	/** 订单流程业务类型-发货单签收 */
	public static final String PERMISSION_BUSINESS_TYPE_DELIVERYRECEIPT = "DELIVERY_RECEIPT";
	/** 订单流程业务类型-开票申请 */
	public static final String PERMISSION_BUSINESS_TYPE_INVOICE = "INVOICE";
	/** 订单流程业务类型-收款单 */
	public static final String PERMISSION_BUSINESS_TYPE_RECEIPTS = "RECEIPTS";
	/** 订单流程业务类型-对账单 */
	public static final String PERMISSION_BUSINESS_TYPE_STATEMENT = "STATEMENT";
	/** 订单流程业务类型-账期申请 */
	public static final String PERMISSION_BUSINESS_TYPE_ACCOUNTS = "ACCOUNTS";
	/** 订单流程业务类型-合同回款计划明细*/
	public static final String PERMISSION_BUSINESS_TYPE_CONTRACT_RECEIPT_DETAIL = "CONTRACT_RECEIPT_DETAIL";
	
	//订单工作流APP
	/** 订单工作流APP-订单提交审核 */
	public static final String ORDER_AUDIT_FLOW_APP_ORDER_AUDIT = "ORDER_AUDIT";
	/** 订单工作流APP-订单登记审核 */
	public static final String ORDER_AUDIT_FLOW_APP_ORDER_REGISTER = "ORDER_REGISTER";
	/** 订单工作流APP-发货申请提交审核 */
	public static final String ORDER_AUDIT_FLOW_APP_DELIVERY_AUDIT = "ORDER_DELIVERY_AUDIT";
	/** 订单工作流APP-开票申请单提交审核  */
	public static final String ORDER_AUDIT_FLOW_APP_INVOICE_AUDIT = "ORDER_INVOICE_AUDIT";
	/** 订单工作流APP-账期申请单提交审核 */
	public static final String ORDER_AUDIT_FLOW_APP_ACCOUNTS_AUDIT = "ORDER_ACCOUNTS_AUDIT";
	/** 订单工作流APP-账期延期审核 */
	public static final String ORDER_AUDIT_FLOW_APP_ACCOUNTS_DELAY = "ORDER_ACCOUNTS_DELAY";
	/** 订单工作流APP-收款单分配提交审核*/
	public static final String ORDER_AUDIT_FLOW_APP_RECEIPTS_AUDIT = "ORDER_RECEIPTS_AUDIT";
	/** 订单工作流APP-对账申请提交审核 */
	public static final String ORDER_AUDIT_FLOW_APP_STATEMENT_AUDIT = "ORDER_STATEMENT_AUDIT";
	/** 订单工作流APP-物流签收提交审核*/
	public static final String ORDER_AUDIT_FLOW_APP_LOGISTICS_AUDIT = "ORDER_LOGISTICS_AUDIT";
	/** 订单工作流APP-订单提交审核 */
	public static final String ORDER_AUDIT_FLOW_APP_ORDER_CHANGE = "ORDER_CHANGE_AUDIT";
	
	/**  账期申请状态-未延期 */
	public static final String ACCOUNTS_STATUS_10 = "10";
	/**  账期申请状态-已延期 */
	public static final String ACCOUNTS_STATUS_20 = "20";
	
	/**  出货单物流签收状态-未签收 */
	public static final String LOGISTICS_RECEIPT_STATUS_10 = "10";
	/**  出货单物流签收状态-已签收 */
	public static final String LOGISTICS_RECEIPT_STATUS_20 = "20";
	
	
	
	/**  回款计划-收款分类-货款 */
	public static final String RECEIPT_TYPE_10 = "10";
	/**  回款计划-收款分类-代垫费用 */
	public static final String RECEIPT_TYPE_20 = "20";
	/**  回款计划-收款分类-预收款*/
	public static final String RECEIPT_TYPE_30 = "30";

	
	/** 订单行-ERP计划状态_未回复交期 */
	public static final String ORDER_ERP_PLAN_STATUS_10 = "未回复交期";
	/** 订单行-ERP计划状态_交期确认中-待营销确认*/
	public static final String ORDER_ERP_PLAN_STATUS_20 = "交期确认中-待营销确认";
	/** 订单行-ERP计划状态_交期确认中-待资材确认 */
	public static final String ORDER_ERP_PLAN_STATUS_30  = "交期确认中-待资材确认";
	/** 订单行-ERP计划状态_交期变更-待营销确认 */
	public static final String ORDER_ERP_PLAN_STATUS_40 = "交期变更-待营销确认";
	/** 订单行-ERP计划状态_交期变更-待资材确认*/
	public static final String ORDER_ERP_PLAN_STATUS_50 = "交期变更-待资材确认";
	/** 订单行-ERP计划状态_交期变更-驳回 */
	public static final String ORDER_ERP_PLAN_STATUS_60 = "交期变更-驳回";
	/** 订单行-ERP计划状态_交期变更-同意 */
	public static final String ORDER_ERP_PLAN_STATUS_70 = "交期变更-同意";
	/** 订单行-ERP计划状态_已确认交期 */
	public static final String ORDER_ERP_PLAN_STATUS_80 = "已确认交期";
	
	/** 出货单行ERP状态-已备货 */
	public static final String DELIVERY_LINE_ERP_STATUS_STOCK = "STOCK";
	
	/************************** 订单管理模块常量定义 -------- end ---------************/ 



	/************************** 报价管理常量定义 -------- Start ---------************/ 
	
	public static final String QUOT_PRESALE = "QUOTE_PRE_SALES_REVIEW"; // 报价售前工程评审流程 报价技术评审流程
	public static final String QUOT_AFTSALE = "QUOTE_POST_SALES_REVIEW"; // 报价售后工程评审流程
	public static final String QUOT_BUSPRC = "QUOTE_COMMERCIAL_REVIEW"; // 报价商务工程评审流程
	public static final String QUOT_DECPRC = "QUOTE_DECISION_REVIEW"; // 报价决策工程评审流程
	public static final String QUOT_PRCPRC = "QUOTE_PRICE_PROC_REVIEW"; // 报价价格工程评审流程
	public static final String QUOT_BIDADT = "QUOTE_PROPOSAL_REVIEW"; // 报价标书评审流程
	public static final String QUOT_SPPROC = "SPECIAL_OFFER_REVIEW"; // 报价特价审批流程
	public static final String QUOT_SBMROC = "QUOTE_SUBMIT_REVIEW"; // 报价工程评审提交流程
	public static final String QUOT_MRPROC = "QUOTE_PREPARE_REVIEW"; // 报价备料申请流程
	public static final String QUOT_BRPROC = "QUOTE_FEEDBACK_REVIEW"; // 报价投标反馈流程
	
	/************************** 报价管理常量定义 -------- end ---------************/ 


	/************************** 合同管理模块常量定义 -------- Start ---------************/ 
	
	public static final String FLOW_APPLICATION = "FLOW_APPLICATION"; // 流程应用group_code;

//	P_GNORG_B1_0001	国内数据中心
//	P_GJORG_B1_0001	国际营销中心
//	P_GNQCORG_B1_0001	新能源汽车行业部
//	P_GNGFORG_B1_0001	新能源国内营销
	public static final String CONTR_TYPE_X_ORG_STR = "国际营销中心";// 国际营销中心
	public static final String CONTR_ORG_GN_B1_STR = "P_GNORG_B1_0001";	//P_GNORG_B1_0001 国内数据中心
	public static final String CONTR_ORG_GJ_B1_STR = "P_GJORG_B1_0001";	//P_GJORG_B1_0001	国际营销中心
	public static final String CONTR_ORG_GNQCORG_B1_STR = "P_GNQCORG_B1_0001";	//P_GNQCORG_B1_0001	新能源汽车行业部
	public static final String CONTR_ORG_GNGFORG_B1_STR = "P_GNGFORG_B1_0001";	//P_GNGFORG_B1_0001	新能源国内营销

	public static final String CONTR_STAND = "CONTR_STAND";// 标准销售类合同
	public static final String CONTR_LOAN = "CONTR_LOAN";// 借货合同
	public static final String CONTR_LOAN_ELIMINATE = "CONTR_LOAN_ELIMINATE";// 无借货合同核销
	public static final String CONTR_PI = "CONTR_PI";// PI合同
	public static final String CONTR_CHANGE = "CONTR_CHANGE";// 合同变更
	public static final String CONTR_LOAN_PI = "CONTR_LOAN_PI";// 借货/PI 合同 businessType

	public static final String CONTR_STAND_01 = "CONTR_STAND-01";// 标准销售类合同-国内合同类型
	public static final String CONTR_STAND_02 = "CONTR_STAND-02";// 标准销售类合同-国际合同类型
	public static final String CONTR_LOAN_0101 = "CONTR_LOAN-0101";// 借货合同
	public static final String CONTR_PI_0101 = "CONTR_PI-0101";// PI合同
	public static final String CONTR_STAND_0101 = "CONTR_STAND-0101";// 标准销售类合同-国内合同类型-标准销售合同
	public static final String CONTR_STAND_0102 = "CONTR_STAND-0102";// 标准销售类合同-国内合同类型-框架协议
	public static final String CONTR_STAND_0103 = "CONTR_STAND-0103";// 标准销售类合同-国内合同类型-框架下合同
	public static final String CONTR_STAND_0202 = "CONTR_STAND-0202";// 标准销售类合同-国际合同类型-框架协议
	public static final String CONTR_STAND_0201 = "CONTR_STAND-0201";// 标准销售类合同-国际合同类型-直销合同
	
	public static final String CONTR_PRJLST_AUX_NAME_PATH_STR="/国内数据中心/非标产品/安装辅材";
	public static final String CONTR_PRJLST_ROOT_NAME ="工程包" ; // 合同工程清单根目录节点名字
	public static final String CONTR_PRJLST_LOV_GROUP_ID = "PRJLSTPRDCAT" ; // 合同工程清单 目录树 lov Group Id 
	
	public static final String CONTR_STAND_TRIAL_PROC  = "CONTR_STAND_TRIAL_PROC";
	public static final String CONTR_STAND_PRESALE_PROC  = "CONTR_STAND_PRESALE_PROC";
	public static final String CONTR_STAND_AFTSALE_PROC  = "CONTR_STAND_AFTSALE_PROC";
	public static final String CONTR_STAND_BUSINESS_PROC  = "CONTR_STAND_BUSINESS_PROC";
	public static final String CONTR_STAND_LEGALE_PROC  = "CONTR_STAND_LEGALE_PROC";
	public static final String CONTR_STAND_RISK_PROC  = "CONTR_STAND_RISK_PROC";
	public static final String CONTR_STAND_PRICE_PROC  = "CONTR_STAND_PRICE_PROC";
	public static final String CONTR_STAND_SUM_PROC  = "CONTR_STAND_SUM_PROC";
	public static final String CONTR_STAND_DESICION_PROC  = "CONTR_STAND_DESICION_PROC";
	public static final String CONTR_STAND_FINAL_PROC  = "CONTR_STAND_FINAL_PROC";
	public static final String CONTR_STAND_SPPRICE_PROC  = "CONTR_STAND_SPPRICE_PROC";
	
	public static final String CONTR_STAND_NOTIFY_PROC  = "CONTR_STAND_NOTIFY_PROC";

	
	public static final String CONTR_CHANGE_TRIAL_PROC  = "CONTR_CHANGE_TRIAL_PROC";
	public static final String CONTR_CHANGE_PRESALE_PROC  = "CONTR_CHANGE_PRESALE_PROC";
	public static final String CONTR_CHANGE_AFTSALE_PROC  = "CONTR_CHANGE_AFTSALE_PROC";
	public static final String CONTR_CHANGE_BUSINESS_PROC  = "CONTR_CHANGE_BUSINESS_PROC";
	public static final String CONTR_CHANGE_LEGALE_PROC  = "CONTR_CHANGE_LEGALE_PROC";
	public static final String CONTR_CHANGE_RISK_PROC  = "CONTR_CHANGE_RISK_PROC";
	public static final String CONTR_CHANGE_PRICE_PROC  = "CONTR_CHANGE_PRICE_PROC";
	public static final String CONTR_CHANGE_SUM_PROC  = "CONTR_CHANGE_SUM_PROC";
	public static final String CONTR_CHANGE_DESICION_PROC  = "CONTR_CHANGE_DESICION_PROC";
	public static final String CONTR_CHANGE_FINAL_PROC  = "CONTR_CHANGE_FINAL_PROC";
	
	public static final String CONTR_LOAN_TRIAL_PROC = "CONTR_LOAN_TRIAL_PROC";
	public static final String CONTR_LOAN_WRITEOFF_PROC  = "CONTR_LOAN_WRITEOFF_PROC";
	
	public static final String CONTR_PI_TRIAL_PROC  = "CONTR_PI_TRIAL_PROC";
	public static final String CONTR_PI_PRESALE_PROC  = "CONTR_PI_PRESALE_PROC";
	public static final String CONTR_PI_AFTSALE_PROC  = "CONTR_PI_AFTSALE_PROC";
	public static final String CONTR_PI_BUSINESS_PROC  = "CONTR_PI_BUSINESS_PROC";
	public static final String CONTR_PI_LEGALE_PROC  = "CONTR_PI_LEGALE_PROC";
	public static final String CONTR_PI_RISK_PROC  = "CONTR_PI_RISK_PROC";
	public static final String CONTR_PI_PRICE_PROC  = "CONTR_PI_PRICE_PROC";
	public static final String CONTR_PI_SUM_PROC  = "CONTR_PI_SUM_PROC";
	public static final String CONTR_PI_DESICION_PROC  = "CONTR_PI_DESICION_PROC";
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	/************************** 合同管理模块常量定义 -------- End ---------************/ 
	
	
	
	/** 任务调度状态 */
	public static final String TASK_SCHEDULER_STATUS_STOP  = "TASK_SCHEDULER_STATUS_STOP";//未启动
	public static final String TASK_SCHEDULER_STATUS_RUNNING  = "TASK_SCHEDULER_STATUS_RUNNING";//正在执行
	public static final String TASK_SCHEDULER_STATUS_PAUSE  = "TASK_SCHEDULER_STATUS_PAUSE";//暂停
	
	/**
	 * 代码逻辑开关编码
	 */
	public static final String JAVA_CODE_LOGIC_SWITCH  = "logicSwitch";
	public static final String JAVA_CODE_SWITCH_OPEN  = "OPEN";
	
	public static final String SWITCH_CODE_LOGIN_IP_LIMIT  = "loginIPLimit";//登陆IP限制开关
	
	public static final String MAIL_SERVICE_ADDR = CacheData.getInstance().getMember("MailServiceConfig","MailServiceAddr") !=null 
			&& ((LovMember)CacheData.getInstance().getMember("MailServiceConfig","MailServiceAddr")).getOptTxt1() != null? 
					((LovMember)CacheData.getInstance().getMember("MailServiceConfig","MailServiceAddr")).getOptTxt1():"10.2.1.25";
					
	public static final String PROD_INFO_MAINTAIN  = "ProdInfoMaintain";
	
	public static final String PROD_INFO_MAINTAIN_PROC  = "PROD_INFO_MAINTAIN_PROC"  ; //产品维护申请
	public static final String PROD_INFO_MAINTAIN_PROC_STUTAS_10 = "10";//新建
	public static final String PROD_INFO_MAINTAIN_PROC_STUTAS_20 = "20";//审核中
	public static final String PROD_INFO_MAINTAIN_PROC_STUTAS_30 = "30";//驳回
	public static final String PROD_INFO_MAINTAIN_PROC_STUTAS_40 = "40";//审核通过
}
