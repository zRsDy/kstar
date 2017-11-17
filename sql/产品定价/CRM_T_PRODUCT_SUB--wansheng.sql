CREATE TABLE "KCRM"."CRM_T_PRODUCT_SUB" 
   (	"C_PID" VARCHAR2(32) NOT NULL ENABLE, 
	"C_PRO_ID" VARCHAR2(32) NOT NULL ENABLE, 
	"C_PRO_SUB_ID" VARCHAR2(32) NOT NULL ENABLE, 
	"DT_CREATE_DATE" DATE, 
	"C_PRO_SUB_DESC" VARCHAR2(200), 
	 CONSTRAINT "PK_PRO_SUB" PRIMARY KEY ("C_PID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TS_CRM"  ENABLE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TS_CRM" ;
 
  CREATE UNIQUE INDEX "KCRM"."PK_PRO_SUB" ON "KCRM"."CRM_T_PRODUCT_SUB" ("C_PID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TS_CRM" ;
 
  CREATE UNIQUE INDEX "KCRM"."UN_PRO_SUB" ON "KCRM"."CRM_T_PRODUCT_SUB" ("C_PRO_ID", "C_PRO_SUB_ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TS_CRM" ;
 
  ALTER TABLE "KCRM"."CRM_T_PRODUCT_SUB" ADD CONSTRAINT "PK_PRO_SUB" PRIMARY KEY ("C_PID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TS_CRM"  ENABLE;
 
  ALTER TABLE "KCRM"."CRM_T_PRODUCT_SUB" MODIFY ("C_PID" NOT NULL ENABLE);
 
  ALTER TABLE "KCRM"."CRM_T_PRODUCT_SUB" MODIFY ("C_PRO_ID" NOT NULL ENABLE);
 
  ALTER TABLE "KCRM"."CRM_T_PRODUCT_SUB" MODIFY ("C_PRO_SUB_ID" NOT NULL ENABLE);
 
   COMMENT ON COLUMN "KCRM"."CRM_T_PRODUCT_SUB"."C_PID" IS '主键';
 
   COMMENT ON COLUMN "KCRM"."CRM_T_PRODUCT_SUB"."C_PRO_ID" IS '主产品ID';
 
   COMMENT ON COLUMN "KCRM"."CRM_T_PRODUCT_SUB"."C_PRO_SUB_ID" IS '选配产品ID';
 
   COMMENT ON COLUMN "KCRM"."CRM_T_PRODUCT_SUB"."DT_CREATE_DATE" IS '创建日期';
 
   COMMENT ON COLUMN "KCRM"."CRM_T_PRODUCT_SUB"."C_PRO_SUB_DESC" IS '产品主辅关系说明';
 
   COMMENT ON TABLE "KCRM"."CRM_T_PRODUCT_SUB"  IS '选配料对应关系';