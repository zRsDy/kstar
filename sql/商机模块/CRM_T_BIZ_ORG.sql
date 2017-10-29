CREATE TABLE CRM_T_BIZ_ORG
   (	
    C_PID VARCHAR2(32) NOT NULL ENABLE, 
	C_FK_BIZOPP_ID VARCHAR2(32) NOT NULL ENABLE, 
	C_ENTERPRISE_NAME VARCHAR2(80), 
	C_BIZ_RELATIONSHIP VARCHAR2(32)
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
 
   COMMENT ON COLUMN CRM_T_BIZ_ORG.C_PID IS 'primary key';
 
   COMMENT ON COLUMN CRM_T_BIZ_ORG.C_FK_BIZOPP_ID IS '外键 关联商机编号';
 
   COMMENT ON COLUMN CRM_T_BIZ_ORG.C_ENTERPRISE_NAME IS '干系方名称';
 
   COMMENT ON COLUMN CRM_T_BIZ_ORG.C_BIZ_RELATIONSHIP IS '业务关系';
 
   COMMENT ON TABLE CRM_T_BIZ_ORG  IS '商机决策链组织关系表';
   
-- Create/Recreate indexes  

-- Create/Recreate primary, unique and foreign key constraints 
alter table CRM_T_BIZ_ORG
  add constraint PT_M_T_CRM_T_BIZ_ORG primary key (C_PID)
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