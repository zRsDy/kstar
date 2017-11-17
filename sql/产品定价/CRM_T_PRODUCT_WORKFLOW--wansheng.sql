CREATE TABLE "KCRM"."CRM_T_PRODUCT_WORKFLOW" 
   (	"C_PID" VARCHAR2(32) NOT NULL ENABLE, 
	"C_PRO_PROCESS_ID" VARCHAR2(32) NOT NULL ENABLE, 
	"C_PRO_PROCESS_NAME" VARCHAR2(20), 
	"C_PRO_ID" VARCHAR2(32) NOT NULL ENABLE, 
	"C_SUBMIT_BY" VARCHAR2(30)
   ) SEGMENT CREATION DEFERRED 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  TABLESPACE "TS_CRM" ;
 
  ALTER TABLE "KCRM"."CRM_T_PRODUCT_WORKFLOW" MODIFY ("C_PID" NOT NULL ENABLE);
 
  ALTER TABLE "KCRM"."CRM_T_PRODUCT_WORKFLOW" MODIFY ("C_PRO_PROCESS_ID" NOT NULL ENABLE);
 
  ALTER TABLE "KCRM"."CRM_T_PRODUCT_WORKFLOW" MODIFY ("C_PRO_ID" NOT NULL ENABLE);
 
   COMMENT ON COLUMN "KCRM"."CRM_T_PRODUCT_WORKFLOW"."C_PID" IS '主键';
 
   COMMENT ON COLUMN "KCRM"."CRM_T_PRODUCT_WORKFLOW"."C_PRO_PROCESS_ID" IS '流程ID';
 
   COMMENT ON COLUMN "KCRM"."CRM_T_PRODUCT_WORKFLOW"."C_PRO_PROCESS_NAME" IS '流程名称';
 
   COMMENT ON COLUMN "KCRM"."CRM_T_PRODUCT_WORKFLOW"."C_PRO_ID" IS '业务实体ID';
 
   COMMENT ON COLUMN "KCRM"."CRM_T_PRODUCT_WORKFLOW"."C_SUBMIT_BY" IS '提交人';
 
   COMMENT ON TABLE "KCRM"."CRM_T_PRODUCT_WORKFLOW"  IS '产品相关工作流记录表';