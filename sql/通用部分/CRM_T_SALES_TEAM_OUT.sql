--客户信息(销售团队-外部团队)

CREATE TABLE "CRM_T_SALES_TEAM_OUT" 
  (  "C_PID" VARCHAR2(32) NOT NULL ENABLE,  
  "C_SYSTEM_PID" VARCHAR2(32 BYTE) NOT NULL ENABLE,
  "C_SYSTEM_CODE" VARCHAR2(32 BYTE) NOT NULL ENABLE,
  "C_MARK_COMP" VARCHAR2(32 BYTE) , 
  "C_MARK_RELA" VARCHAR2(100 BYTE) NOT NULL ENABLE, 
  "C_MARK_NAME" VARCHAR2(100 BYTE),
  "C_MARK_CONTACT_PERSON" VARCHAR2(100 BYTE), 
  "C_MARK_ROLE" VARCHAR2(100 BYTE),
  "C_MARK_AUTH" VARCHAR2(100 BYTE),
  "C_MARK_VALID_FROM" VARCHAR2(100 BYTE),
  "C_MARK_VALID_TO" VARCHAR2(100 BYTE),
  CONSTRAINT "PK_CRM_T_SALES_TEAM_OUT" PRIMARY KEY ("C_PID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) ;
  
  COMMENT ON COLUMN "CRM_T_SALES_TEAM_OUT"."C_PID" IS '主键自增';
  COMMENT ON COLUMN "CRM_T_SALES_TEAM_OUT"."C_SYSTEM_PID" IS '业务主键';
  COMMENT ON COLUMN "CRM_T_SALES_TEAM_OUT"."C_SYSTEM_CODE" IS '业务CODE';
  COMMENT ON COLUMN "CRM_T_SALES_TEAM_OUT"."C_MARK_COMP" IS '公司名称';
  COMMENT ON COLUMN "CRM_T_SALES_TEAM_OUT"."C_MARK_RELA" IS '关系';
  COMMENT ON COLUMN "CRM_T_SALES_TEAM_OUT"."C_MARK_NAME" IS '人员姓名';
  COMMENT ON COLUMN "CRM_T_SALES_TEAM_OUT"."C_MARK_CONTACT_PERSON" IS '主要联系人';
  COMMENT ON COLUMN "CRM_T_SALES_TEAM_OUT"."C_MARK_ROLE" IS '角色';
  COMMENT ON COLUMN "CRM_T_SALES_TEAM_OUT"."C_MARK_AUTH" IS '权限';
  COMMENT ON COLUMN "CRM_T_SALES_TEAM_OUT"."C_MARK_VALID_FROM" IS '有效日期从';
  COMMENT ON COLUMN "CRM_T_SALES_TEAM_OUT"."C_MARK_VALID_TO" IS '有效日期至';