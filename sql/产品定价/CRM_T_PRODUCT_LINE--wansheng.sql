CREATE TABLE "KCRM"."CRM_T_PRODUCT_LINE" 
   (	"C_PID" VARCHAR2(32) NOT NULL ENABLE, 
	"C_PRO_CATEGORY" VARCHAR2(40), 
	"C_PRO_LINE" VARCHAR2(40), 
	"C_PRO_SERIES" VARCHAR2(40), 
	"C_PRO_EXTEND1" VARCHAR2(40), 
	"C_PRO_EXTEND2" VARCHAR2(40), 
	"DT_CREATE_DATE" DATE, 
	"C_CREATE_BY" VARCHAR2(20), 
	"C_PRO_TYPE" VARCHAR2(40), 
	"C_PRO_MANAG_DEPART" VARCHAR2(40), 
	"C_PRO_GROUP" VARCHAR2(40), 
	"C_PRO_POWCAP" VARCHAR2(32), 
	 CONSTRAINT "PK_PRO_LINE" PRIMARY KEY ("C_PID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TS_CRM"  ENABLE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TS_CRM" ;
 
  CREATE UNIQUE INDEX "KCRM"."PK_PRO_LINE" ON "KCRM"."CRM_T_PRODUCT_LINE" ("C_PID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TS_CRM" ;
 
  CREATE UNIQUE INDEX "KCRM"."UNIQUE_LINE" ON "KCRM"."CRM_T_PRODUCT_LINE" ("C_PRO_CATEGORY", "C_PRO_LINE", "C_PRO_SERIES", "C_PRO_TYPE", "C_PRO_MANAG_DEPART", "C_PRO_GROUP", "C_PRO_POWCAP") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TS_CRM" ;
 
  ALTER TABLE "KCRM"."CRM_T_PRODUCT_LINE" ADD CONSTRAINT "PK_PRO_LINE" PRIMARY KEY ("C_PID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TS_CRM"  ENABLE;
 
  ALTER TABLE "KCRM"."CRM_T_PRODUCT_LINE" MODIFY ("C_PID" NOT NULL ENABLE);
 
   COMMENT ON COLUMN "KCRM"."CRM_T_PRODUCT_LINE"."C_PID" IS '产品线ID';
 
   COMMENT ON COLUMN "KCRM"."CRM_T_PRODUCT_LINE"."C_PRO_CATEGORY" IS '产品分类';
 
   COMMENT ON COLUMN "KCRM"."CRM_T_PRODUCT_LINE"."C_PRO_LINE" IS '产品线';
 
   COMMENT ON COLUMN "KCRM"."CRM_T_PRODUCT_LINE"."C_PRO_SERIES" IS '产品系列';
 
   COMMENT ON COLUMN "KCRM"."CRM_T_PRODUCT_LINE"."C_PRO_EXTEND1" IS '产品维度扩展1';
 
   COMMENT ON COLUMN "KCRM"."CRM_T_PRODUCT_LINE"."C_PRO_EXTEND2" IS '产品维度扩展2';
 
   COMMENT ON COLUMN "KCRM"."CRM_T_PRODUCT_LINE"."DT_CREATE_DATE" IS '创建时间';
 
   COMMENT ON COLUMN "KCRM"."CRM_T_PRODUCT_LINE"."C_CREATE_BY" IS '创建人';
 
   COMMENT ON COLUMN "KCRM"."CRM_T_PRODUCT_LINE"."C_PRO_TYPE" IS '产品类别';
 
   COMMENT ON COLUMN "KCRM"."CRM_T_PRODUCT_LINE"."C_PRO_MANAG_DEPART" IS '产品管理部';
 
   COMMENT ON COLUMN "KCRM"."CRM_T_PRODUCT_LINE"."C_PRO_GROUP" IS '产品组';
 
   COMMENT ON COLUMN "KCRM"."CRM_T_PRODUCT_LINE"."C_PRO_POWCAP" IS '功率或容量';
 
   COMMENT ON TABLE "KCRM"."CRM_T_PRODUCT_LINE"  IS '产品生产产品线';
   
   --extend the length for interface data imported from ERP.
   alter table KCRM.CRM_T_PRODUCT_LINE modify (C_PRO_POWCAP VARCHAR2(128));
   alter table KCRM.CRM_T_PRODUCT_LINE modify(C_PRO_SERIES VARCHAR2(128));
   alter table KCRM.CRM_T_PRODUCT_LINE modify(C_PRO_TYPE VARCHAR2(128));
