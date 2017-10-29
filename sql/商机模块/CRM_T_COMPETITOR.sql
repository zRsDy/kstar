CREATE TABLE CRM_T_COMPETITOR
   (	
    C_PID VARCHAR2(32) NOT NULL ENABLE, 
    C_FK_BIZOPP_ID VARCHAR2(32) NOT NULL ENABLE,
    C_COMPETITOR_BRAND VARCHAR2(30),
    C_PRODUCT_MODEL VARCHAR2(80), 
    N_IS_SELECTED_BEFOR VARCHAR2(2), 
    C_ADVANTAGE VARCHAR2(30 BYTE), 
	C_DISADVANTAGE VARCHAR2(30 BYTE), 
    C_PRO_TECH_SCHEME VARCHAR2(30 BYTE), 
	C_PRO_BIZ_PLAN VARCHAR2(30 BYTE)
   ) 
 tablespace TS_CRM
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
 );
 
   COMMENT ON COLUMN CRM_T_COMPETITOR.C_PID IS 'primary key';
   
   COMMENT ON COLUMN CRM_T_COMPETITOR.C_FK_BIZOPP_ID IS '商机ID';
 
   COMMENT ON COLUMN CRM_T_COMPETITOR.C_COMPETITOR_BRAND IS '竞争品牌';
 
   COMMENT ON COLUMN CRM_T_COMPETITOR.C_PRODUCT_MODEL IS '产品型号';
 
   COMMENT ON COLUMN CRM_T_COMPETITOR.N_IS_SELECTED_BEFOR IS '客户前期选用';
 
   COMMENT ON COLUMN CRM_T_COMPETITOR.C_ADVANTAGE IS '优势';
   
   COMMENT ON COLUMN CRM_T_COMPETITOR.C_DISADVANTAGE IS '劣势';
   
   COMMENT ON COLUMN CRM_T_COMPETITOR.C_PRO_TECH_SCHEME IS '项目技术方案';
   
   COMMENT ON COLUMN CRM_T_COMPETITOR.C_PRO_BIZ_PLAN IS '项目商务方案';
 
   COMMENT ON TABLE CRM_T_COMPETITOR  IS '商机_竞争对手';
   
-- Create/Recreate indexes  

-- Create/Recreate primary, unique and foreign key constraints 
alter table CRM_T_COMPETITOR
  add constraint PT_M_T_CRM_T_COMPETITOR primary key (C_PID)
  using index 
  tablespace TS_CRM
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );