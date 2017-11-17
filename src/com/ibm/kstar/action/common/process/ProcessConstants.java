package com.ibm.kstar.action.common.process;

/**
 * Created by wangchao on 2017/3/15.
 */
public class ProcessConstants {

	public static final String PROCESS_STATUS = "PROCESS_STATUS";
    /**
     * 未启动，未生成流程实例
     */
    public static final String PROCESS_STATUS_Pending = "Pending";
    /**
     * 审批中，已生成流程实例，流程运行中
     */
    public static final String PROCESS_STATUS_Processing = "Processing";

    /**
     * 已审批，流程正常结束
     */
    public static final String PROCESS_STATUS_Completed = "Completed";
    /**
     * 已驳回，流程审批因业务驳回至流程发起人，与未启动状态控制相同
     */
    public static final String PROCESS_STATUS_Rejected = "Rejected";
    /**
     * 已销毁，流程实例被终止继续运行，不可重新启动
     */
    public static final String PROCESS_STATUS_Destroyed = "Destroyed";


    public static final String BIZOPP_PERSALE_SUPPORT_PROC = "商机管理-售前技术支持申请审批流程";

    public static final String BIZOPP_SPECIAL_PRICE_APPLY_PROC = "商机管理-特价申请审批流程";

    public static final String BIZOPP_REBATE_PRICE_APPLY_PROC = "商机管理-特价申请审批流程";
    public static final String BIZOPP_REBATE_PRICE_CHANGE_PROC = "商机管理-特价变更审批流程";

    public static final String BIZOPP_BID_AUTHORITY_APPLY_PROC = "商机管理-投标授权申请审批流程";
    public static final String BIZOPP_NEWBID_AUTHORITY_APPLY_PROC = "商机管理-投标授权申请审批流程";

    public static final String BIZOPP_PROTO_APPLY_PROC = "商机管理-样机申请流程审批";

    public static final String BIZOPP_PREPARE_APPLY_PROC = "商机管理-商机报备申请";

    public static final String BIZOPP_APPROVED_PROC = "商机管理-报备批准";

    public static final String BIZOPP_CHANGE_PROC = "商机管理-商机信息变更";

    public static final String BIZOPP_PROJECTINIT_PROC = "商机管理-立项审批申请";

    public static final String BIZOPP_BTNX_PROC = "商机管理-新加了个按钮";
    
    public static final String BIZOPP_BID_FEED_BACK = "商机管理-投标反馈审批流程";

    //产品、渠道流程
    public static final String PROCESS_STATUS_01 = "01";	//第1状态，如：新建
    public static final String PROCESS_STATUS_02 = "02";	//第2状态，如：审批中
    public static final String PROCESS_STATUS_03 = "03";	//第3状态，如：已审批
    public static final String PROCESS_STATUS_04 = "04";	//第4状态，如：已驳回
    public static final String PROCESS_STATUS_05 = "05";	//第5状态，如：已关闭、已提交
    public static final String PROCESS_STATUS_06 = "06";	//第6状态，如：已入库
    public static final String PROCESS_STATUS_07 = "07";	//第7状态，如：同步接口结果
    public static final String PROCESS_STATUS_08 = "08";	//第8状态，如：ERP回传已入库
    public static final String PRODUCT_PRICE_HEAD = "PRODUCT_PRICE_HEAD";	//权限businessType
    public static final String PRODUCT_2 = "PRODUCT2";	//非标和外购产品申请：数据权限
    public static final String GIFT_APPLY_PROC = "GIFT_APPLY_PROC";	//资料与礼品申请流程、数据权限
    public static final String ACTIVITY_APPLY_PROC = "ACTIVITY_APPLY_PROC";	//市场活动申请流程、数据权限
    public static final String TRAIN_APPLY_PROC = "TRAIN_APPLY_PROC";	//培训申请流程、数据权限
    public static final String ORDER_FORECAST_PROC = "ORDER_FORECAST_PROC";	//下单预测流程、数据权限
    public static final String IMPORT_SALE_PROC = "IMPORT_SALE_PROC";	//引入销量流程
    public static final String REBATE_POLICY_PROC = "REBATE_POLICY_PROC";	//返利政策流程
    public static final String SERVICE_APPLY_PROC = "SERVICE_APPLY_PROC";	//产品服务申请流程、数据权限
    public static final String PM_PTS_PROC = "PM_PTS_PROC";	//产品预转销流程
    public static final String PRODUCT_AU_TEST_PROC = "PRODUCT_AU_TEST_PROC";	//产品认证与测试流程
    public static final String PRODUCT_CSALE_PROC = "PRODUCT_CSALE_PROC";	//改销售状态流程
    public static final String PRODUCT_PUBLISH_PROC = "PRODUCT_PUBLISH_PROC";	//产品发布流程
    public static final String PRODUCT_ECR_PROC = "PRODUCT_ECR_PROC";	//产品ECR变更流程
    public static final String PRODUCT_DOC_PROC = "PRODUCT_DOC_PROC";	//产品文档流程
    public static final String PRODUCT_DEMAND_PROC = "PRODUCT_DEMAND_PROC";	//产品需求单流程、数据权限
    public static final String LAYER_PRICE_COMP = "LAYER_PRICE_COMP";	//价格表层级对照表：数据权限
    public static final String BATCH_PRODUCT_PRICE = "BATCH_PRODUCT_PRICE";	//批量调价：数据权限
 
    /**
     * 合同管理模块常量定义
     * ---------  Start ----------
     */
    public static final String CONTR_STAND_TRIAL_PROC = "合同管理-合同申请初审流程";
    public static final String CONTR_STAND_PRESALE_PROC = "合同管理-合同申请售前评审流程";
    public static final String CONTR_STAND_AFTSALE_PROC = "合同管理-合同申请售后评审流程";
    public static final String CONTR_STAND_BUSINESS_PROC = "合同管理-合同申请商务评审流程";
    public static final String CONTR_STAND_LEGALE_PROC = "合同管理-合同申请法务评审流程";
    public static final String CONTR_STAND_RISK_PROC = "合同管理-合同申请风控评审流程";
    public static final String CONTR_STAND_PRICE_PROC = "合同管理-合同申请价格评审流程";
    public static final String CONTR_STAND_SUM_PROC = "合同管理-合同申请综合评审流程";
    public static final String CONTR_STAND_DESICION_PROC = "合同管理-合同申请决策评审流程";    
    public static final String CONTR_STAND_FINAL_PROC = "合同管理-合同申请合同书评审流程";

    public static final String CONTR_CHANGE_TRIAL_PROC = "合同管理-合同变更初审流程";
    public static final String CONTR_CHANGE_PRESALE_PROC = "合同管理-合同变更售前评审流程";
    public static final String CONTR_CHANGE_AFTSALE_PROC = "合同管理-合同变更售后评审流程";
    public static final String CONTR_CHANGE_BUSINESS_PROC = "合同管理-合同变更商务评审流程";
    public static final String CONTR_CHANGE_LEGALE_PROC = "合同管理-合同变更法务评审流程";
    public static final String CONTR_CHANGE_RISK_PROC = "合同管理-合同变更风控评审流程";
    public static final String CONTR_CHANGE_PRICE_PROC = "合同管理-合同变更价格评审流程";
    public static final String CONTR_CHANGE_SUM_PROC = "合同管理-合同变更综合评审流程";
    public static final String CONTR_CHANGE_DESICION_PROC = "合同管理-合同变更决策评审流程";    

    public static final String CONTR_LOAN_TRIAL_PROC = "合同管理-借货合同评审流程";

    public static final String CONTR_PI_TRIAL_PROC = "合同管理-合同PI初审流程";
    public static final String CONTR_PI_PRESALE_PROC = "合同管理-合同PI售前评审流程";
    public static final String CONTR_PI_AFTSALE_PROC = "合同管理-合同PI售后评审流程";
    public static final String CONTR_PI_BUSINESS_PROC = "合同管理-合同PI商务评审流程";
    public static final String CONTR_PI_LEGALE_PROC = "合同管理-合同PI法务评审流程";
    public static final String CONTR_PI_RISK_PROC = "合同管理-合同PI风控评审流程";
    public static final String CONTR_PI_PRICE_PROC = "合同管理-合同PI价格评审流程";
    public static final String CONTR_PI_SUM_PROC = "合同管理-合同PI综合评审流程";
    public static final String CONTR_PI_DESICION_PROC = "合同管理-合同PI决策评审流程";    
    /**
     * 合同管理模块常量定义
     * ---------  End ----------
     */

}
