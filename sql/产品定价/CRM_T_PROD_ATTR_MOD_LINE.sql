-- Create table
create table CRM_T_PROD_ATTR_MOD_LINE
(
  c_id             VARCHAR2(40) not null,
  c_maintain_id    VARCHAR2(40),
  c_op_type        VARCHAR2(40),
  c_mater_code     VARCHAR2(40),
  c_prod_attr_id   VARCHAR2(40),
  c_old_value      VARCHAR2(500),
  c_new_value      VARCHAR2(500),
  c_reason         VARCHAR2(500),
  c_created_by_id  VARCHAR2(40),
  dt_created_at    DATE,
  c_created_pos_id VARCHAR2(40),
  c_created_org_id VARCHAR2(40),
  c_updated_by_id  VARCHAR2(40),
  dt_updated_at    DATE
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 8K
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table 
comment on table CRM_T_PROD_ATTR_MOD_LINE
  is '产品信息变更明细';
-- Add comments to the columns 
comment on column CRM_T_PROD_ATTR_MOD_LINE.c_id
  is '主键';
comment on column CRM_T_PROD_ATTR_MOD_LINE.c_maintain_id
  is '申请单ID';
comment on column CRM_T_PROD_ATTR_MOD_LINE.c_op_type
  is '申请类型';
comment on column CRM_T_PROD_ATTR_MOD_LINE.c_mater_code
  is '物料号';
comment on column CRM_T_PROD_ATTR_MOD_LINE.c_prod_attr_id
  is '产品属性ID';
comment on column CRM_T_PROD_ATTR_MOD_LINE.c_old_value
  is '旧值';
comment on column CRM_T_PROD_ATTR_MOD_LINE.c_new_value
  is '新值';
comment on column CRM_T_PROD_ATTR_MOD_LINE.c_reason
  is '变更原因';
comment on column CRM_T_PROD_ATTR_MOD_LINE.c_created_by_id
  is '创建人';
comment on column CRM_T_PROD_ATTR_MOD_LINE.dt_created_at
  is '创建时间';
comment on column CRM_T_PROD_ATTR_MOD_LINE.c_created_pos_id
  is '创建人岗位';
comment on column CRM_T_PROD_ATTR_MOD_LINE.c_created_org_id
  is '创建人组织';
comment on column CRM_T_PROD_ATTR_MOD_LINE.c_updated_by_id
  is '修改人';
comment on column CRM_T_PROD_ATTR_MOD_LINE.dt_updated_at
  is '修改时间';
-- Create/Recreate primary, unique and foreign key constraints 
alter table CRM_T_PROD_ATTR_MOD_LINE
  add constraint CRM_T_PROD_ATTR_MOD_PK primary key (C_ID)
  using index 
  tablespace USERS
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
