CREATE TABLE "CRM_T_BID_AUTH_UNIT" 
  (  "C_PID" VARCHAR2(32) NOT NULL ENABLE,  
  "C_BID_ID" VARCHAR2(32 BYTE),
  "C_FROM_ID" VARCHAR2(32 BYTE),
  "C_INTEGRATOR" VARCHAR2(200 BYTE),
  "C_ADDRESS" VARCHAR2(200 BYTE),
  "C_CREATED_BY_ID" VARCHAR2(100 BYTE), 
  "DT_CREATED_AT" DATE, 
  "C_CREATED_POS_ID" VARCHAR2(100 BYTE), 
  "C_CREATED_ORG_ID" VARCHAR2(100 BYTE),
  "C_UPDATED_BY_ID" VARCHAR2(100 BYTE),
  "DT_UPDATED_AT" DATE, 
  CONSTRAINT "PK_CRM_T_BID_AUTH_UNIT" PRIMARY KEY ("C_PID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) ;

