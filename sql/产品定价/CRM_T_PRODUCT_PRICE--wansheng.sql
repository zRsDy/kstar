CREATE TABLE "KCRM"."CRM_T_PRODUCT_PRICE" 
   (	"C_PID" VARCHAR2(32), 
	"C_PRICE_NAME" VARCHAR2(30) NOT NULL ENABLE, 
	"C_CURRENCY" VARCHAR2(6) NOT NULL ENABLE, 
	"N_CATALOG_PRICE" NUMBER NOT NULL ENABLE, 
	"N_LEVEL_PRICE" NUMBER, 
	"C_CLIENT" VARCHAR2(32), 
	"DT_EFFECT_START_TIME" DATE NOT NULL ENABLE, 
	"DT_EFFECT_END_TIME" DATE NOT NULL ENABLE, 
	"C_ORG_NAME" VARCHAR2(30), 
	"C_PRO_ID" VARCHAR2(32) NOT NULL ENABLE, 
	 CONSTRAINT "PRO_PRICE_ID" PRIMARY KEY ("C_PID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS NOCOMPRESS LOGGING
  TABLESPACE "TS_CRM"  ENABLE
   ) SEGMENT CREATION DEFERRED 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  TABLESPACE "TS_CRM" ;
 
  CREATE UNIQUE INDEX "KCRM"."PRO_PRICE_ID" ON "KCRM"."CRM_T_PRODUCT_PRICE" ("C_PID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS NOCOMPRESS LOGGING
  TABLESPACE "TS_CRM" ;
 
  ALTER TABLE "KCRM"."CRM_T_PRODUCT_PRICE" ADD CONSTRAINT "PRO_PRICE_ID" PRIMARY KEY ("C_PID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS NOCOMPRESS LOGGING
  TABLESPACE "TS_CRM"  ENABLE;
 
  ALTER TABLE "KCRM"."CRM_T_PRODUCT_PRICE" MODIFY ("C_PRICE_NAME" NOT NULL ENABLE);
 
  ALTER TABLE "KCRM"."CRM_T_PRODUCT_PRICE" MODIFY ("C_CURRENCY" NOT NULL ENABLE);
 
  ALTER TABLE "KCRM"."CRM_T_PRODUCT_PRICE" MODIFY ("N_CATALOG_PRICE" NOT NULL ENABLE);
 
  ALTER TABLE "KCRM"."CRM_T_PRODUCT_PRICE" MODIFY ("DT_EFFECT_START_TIME" NOT NULL ENABLE);
 
  ALTER TABLE "KCRM"."CRM_T_PRODUCT_PRICE" MODIFY ("DT_EFFECT_END_TIME" NOT NULL ENABLE);
 
  ALTER TABLE "KCRM"."CRM_T_PRODUCT_PRICE" MODIFY ("C_PRO_ID" NOT NULL ENABLE);
 
   COMMENT ON COLUMN "KCRM"."CRM_T_PRODUCT_PRICE"."C_PID" IS '价格表ID';
 
   COMMENT ON COLUMN "KCRM"."CRM_T_PRODUCT_PRICE"."C_PRICE_NAME" IS '价格表名称';
 
   COMMENT ON COLUMN "KCRM"."CRM_T_PRODUCT_PRICE"."C_CURRENCY" IS '货币';
 
   COMMENT ON COLUMN "KCRM"."CRM_T_PRODUCT_PRICE"."N_CATALOG_PRICE" IS '目录价格';
 
   COMMENT ON COLUMN "KCRM"."CRM_T_PRODUCT_PRICE"."N_LEVEL_PRICE" IS '层级价格';
 
   COMMENT ON COLUMN "KCRM"."CRM_T_PRODUCT_PRICE"."C_CLIENT" IS '客户';
 
   COMMENT ON COLUMN "KCRM"."CRM_T_PRODUCT_PRICE"."DT_EFFECT_START_TIME" IS '有效期开始时间';
 
   COMMENT ON COLUMN "KCRM"."CRM_T_PRODUCT_PRICE"."DT_EFFECT_END_TIME" IS '有效期结束时间';
 
   COMMENT ON COLUMN "KCRM"."CRM_T_PRODUCT_PRICE"."C_ORG_NAME" IS '组织';
 
   COMMENT ON COLUMN "KCRM"."CRM_T_PRODUCT_PRICE"."C_PRO_ID" IS '产品ID';
 
   COMMENT ON TABLE "KCRM"."CRM_T_PRODUCT_PRICE"  IS '产品价格表';