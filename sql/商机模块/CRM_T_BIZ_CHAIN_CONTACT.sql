CREATE TABLE CRM_T_BIZ_CHAIN_CONTACT
   (	
    C_PID VARCHAR2(32) NOT NULL ENABLE, 
	C_ROLE VARCHAR2(10), 
	C_NAME VARCHAR2(20), 
	C_DUTY VARCHAR2(20), 
	C_PHONENUMBER VARCHAR2(20), 
	C_DECISION_WEIGHT VARCHAR2(20), 
	C_SUPPORT_LEVEL VARCHAR2(20),
	C_BIZ_ORG_ID VARCHAR2(32)
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
 
   COMMENT ON COLUMN CRM_T_BIZ_CHAIN_CONTACT.C_PID IS 'primary key';
 
   COMMENT ON COLUMN CRM_T_BIZ_CHAIN_CONTACT.C_ROLE IS '角色';
 
   COMMENT ON COLUMN CRM_T_BIZ_CHAIN_CONTACT.C_NAME IS '姓名';
 
   COMMENT ON COLUMN CRM_T_BIZ_CHAIN_CONTACT.C_DUTY IS '职务';
 
   COMMENT ON COLUMN CRM_T_BIZ_CHAIN_CONTACT.C_PHONENUMBER IS '联系电话';
 
   COMMENT ON COLUMN CRM_T_BIZ_CHAIN_CONTACT.C_DECISION_WEIGHT IS '决策权重';
 
   COMMENT ON COLUMN CRM_T_BIZ_CHAIN_CONTACT.C_SUPPORT_LEVEL IS '对我司支持程度';
   
   COMMENT ON COLUMN CRM_T_BIZ_CHAIN_CONTACT.C_BIZ_ORG_ID IS '商机组织ID';
 
   COMMENT ON TABLE CRM_T_BIZ_CHAIN_CONTACT  IS '商机决策链联系人表';
   
-- Create/Recreate indexes  

-- Create/Recreate primary, unique and foreign key constraints 
alter table CRM_T_BIZ_CHAIN_CONTACT
  add constraint PT_M_T_BIZ_CHAIN_CONTACT primary key (C_PID)
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