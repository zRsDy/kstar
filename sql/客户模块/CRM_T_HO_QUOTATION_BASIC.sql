  CREATE TABLE "CRM_T_HO_QUOTATION_BASIC" 
   (	"C_PID" VARCHAR2(32 BYTE) NOT NULL ENABLE, 
   "C_BUSINESS_ID" VARCHAR2(32 BYTE), 
   "C_BUSINESS_STATUS" VARCHAR2(32 BYTE),
   "C_BASE_ID" VARCHAR2(32 BYTE),
   "C_QID" VARCHAR2(32 BYTE),
	"N_VERSION" NUMBER(32,0) NOT NULL ENABLE, 
	"C_NAME" VARCHAR2(300 BYTE), 
	"C_SALESREP" VARCHAR2(300 BYTE), 
	"C_SALESREP_DEP" VARCHAR2(300 BYTE), 
	"C_BO_CODE" VARCHAR2(32 BYTE), 
	"C_BO_NAME" VARCHAR2(300 BYTE), 
	"C_ORG" VARCHAR2(300 BYTE), 
	"C_CUSTOMER_NAME" VARCHAR2(300 BYTE), 
	"C_CUSTOMER_CODE" VARCHAR2(300 BYTE), 
	"C_CONTACTS" VARCHAR2(300 BYTE), 
	"C_STATUS" VARCHAR2(300 BYTE), 
	"C_CLOSURE_REASON" VARCHAR2(300 BYTE), 
	"C_PRO_REVIEW_STATUS" VARCHAR2(300 BYTE), 
	"C_BID_AUDIT_STATUS" VARCHAR2(300 BYTE), 
	"C_CREATOR" VARCHAR2(32 BYTE), 
	"C_PRICE_LIST" VARCHAR2(300 BYTE), 
	"C_CURRENCY" VARCHAR2(32 BYTE), 
	"N_AMOUNT" NUMBER(16,2), 
	"C_BID_RESULTS" VARCHAR2(32 BYTE), 
	"C_BID_RESULTS_REASON" VARCHAR2(300 BYTE), 
	"N_IS_PROREVIEW" NUMBER(1,0) NOT NULL ENABLE, 
	"N_IS_BIDPRO" NUMBER(1,0) NOT NULL ENABLE, 
	"N_BID_NO" VARCHAR2(300 BYTE), 
	"N_IS_VALID" NUMBER(1,0), 
	"C_PAY_TYPE" VARCHAR2(300 BYTE), 
	"C_PRC_ID" VARCHAR2(300 BYTE), 
	"DT_CREATE_DATE" DATE, 
	"C_SAL_DEP" VARCHAR2(300 BYTE), 
	"C_BIDDOC_NO" VARCHAR2(300 BYTE), 
	"C_MEMO" VARCHAR2(300 BYTE), 
	"C_PRICE_LISTID" VARCHAR2(300 BYTE), 
	"C_CREATED_BY_ID" VARCHAR2(100 BYTE), 
	"DT_CREATED_AT" DATE, 
	"C_CREATED_POS_ID" VARCHAR2(100 BYTE), 
	"C_CREATED_ORG_ID" VARCHAR2(100 BYTE), 
	"C_UPDATED_BY_ID" VARCHAR2(100 BYTE), 
	"DT_UPDATED_AT" DATE, 
	"C_IF_SUBMITTED" VARCHAR2(300 BYTE), 
	"C_MARKET_DEPT" VARCHAR2(300 BYTE), 
  CONSTRAINT "PK_HO_QUOTATION_BASIC" PRIMARY KEY ("C_PID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) ;
 

   COMMENT ON COLUMN "CRM_T_HO_QUOTATION_BASIC"."C_QID" IS '报价单编号';
 
   COMMENT ON COLUMN "CRM_T_HO_QUOTATION_BASIC"."N_VERSION" IS '报价单版本';
 
   COMMENT ON COLUMN "CRM_T_HO_QUOTATION_BASIC"."C_NAME" IS '报价单名称';
 
   COMMENT ON COLUMN "CRM_T_HO_QUOTATION_BASIC"."C_SALESREP" IS '销售代表';
 
   COMMENT ON COLUMN "CRM_T_HO_QUOTATION_BASIC"."C_SALESREP_DEP" IS '销售代表部门';
 
   COMMENT ON COLUMN "CRM_T_HO_QUOTATION_BASIC"."C_BO_CODE" IS '商机编码';
 
   COMMENT ON COLUMN "CRM_T_HO_QUOTATION_BASIC"."C_BO_NAME" IS '商机名称';
 
   COMMENT ON COLUMN "CRM_T_HO_QUOTATION_BASIC"."C_ORG" IS '组织';
 
   COMMENT ON COLUMN "CRM_T_HO_QUOTATION_BASIC"."C_CUSTOMER_NAME" IS '客户名称';
 
   COMMENT ON COLUMN "CRM_T_HO_QUOTATION_BASIC"."C_CUSTOMER_CODE" IS '客户编号';
 
   COMMENT ON COLUMN "CRM_T_HO_QUOTATION_BASIC"."C_CONTACTS" IS '联系人';
 
   COMMENT ON COLUMN "CRM_T_HO_QUOTATION_BASIC"."C_STATUS" IS '状态';
 
   COMMENT ON COLUMN "CRM_T_HO_QUOTATION_BASIC"."C_CLOSURE_REASON" IS '关闭原因';
 
   COMMENT ON COLUMN "CRM_T_HO_QUOTATION_BASIC"."C_PRO_REVIEW_STATUS" IS '工程评审状态';
 
   COMMENT ON COLUMN "CRM_T_HO_QUOTATION_BASIC"."C_BID_AUDIT_STATUS" IS '标书审核状态';
 
   COMMENT ON COLUMN "CRM_T_HO_QUOTATION_BASIC"."C_CREATOR" IS '创建人';
 
   COMMENT ON COLUMN "CRM_T_HO_QUOTATION_BASIC"."C_PRICE_LIST" IS '价格表';
 
   COMMENT ON COLUMN "CRM_T_HO_QUOTATION_BASIC"."C_CURRENCY" IS '货币';
 
   COMMENT ON COLUMN "CRM_T_HO_QUOTATION_BASIC"."N_AMOUNT" IS '总额';
 
   COMMENT ON COLUMN "CRM_T_HO_QUOTATION_BASIC"."C_BID_RESULTS" IS '投标结果';
 
   COMMENT ON COLUMN "CRM_T_HO_QUOTATION_BASIC"."C_BID_RESULTS_REASON" IS '丢标/赢标原因';
 
   COMMENT ON COLUMN "CRM_T_HO_QUOTATION_BASIC"."N_IS_PROREVIEW" IS '是否需工程评审';
 
   COMMENT ON COLUMN "CRM_T_HO_QUOTATION_BASIC"."N_IS_BIDPRO" IS '是否投标项目';
 
   COMMENT ON COLUMN "CRM_T_HO_QUOTATION_BASIC"."N_BID_NO" IS '有效标识';
 
   COMMENT ON COLUMN "CRM_T_HO_QUOTATION_BASIC"."N_IS_VALID" IS '有效标识';
 
   COMMENT ON COLUMN "CRM_T_HO_QUOTATION_BASIC"."C_PAY_TYPE" IS '付款方式';
 
   COMMENT ON COLUMN "CRM_T_HO_QUOTATION_BASIC"."C_PRC_ID" IS '流程编号';
 
   COMMENT ON COLUMN "CRM_T_HO_QUOTATION_BASIC"."DT_CREATE_DATE" IS '创建日期';
 
   COMMENT ON COLUMN "CRM_T_HO_QUOTATION_BASIC"."C_SAL_DEP" IS '销售部门';
 
   COMMENT ON COLUMN "CRM_T_HO_QUOTATION_BASIC"."C_BIDDOC_NO" IS '关联标书号';
 
   COMMENT ON COLUMN "CRM_T_HO_QUOTATION_BASIC"."C_MEMO" IS '备注';
 
   COMMENT ON COLUMN "CRM_T_HO_QUOTATION_BASIC"."C_CREATED_BY_ID" IS '创建人';
 
   COMMENT ON COLUMN "CRM_T_HO_QUOTATION_BASIC"."DT_CREATED_AT" IS '创建时间';
 
   COMMENT ON COLUMN "CRM_T_HO_QUOTATION_BASIC"."C_CREATED_POS_ID" IS '创建人岗位';
 
   COMMENT ON COLUMN "CRM_T_HO_QUOTATION_BASIC"."C_CREATED_ORG_ID" IS '创建人组织';
 
   COMMENT ON COLUMN "CRM_T_HO_QUOTATION_BASIC"."C_UPDATED_BY_ID" IS '更新者';
 
   COMMENT ON COLUMN "CRM_T_HO_QUOTATION_BASIC"."DT_UPDATED_AT" IS '更新时间';
 
   COMMENT ON TABLE "CRM_T_HO_QUOTATION_BASIC"  IS '报价单基本信息表';
 

