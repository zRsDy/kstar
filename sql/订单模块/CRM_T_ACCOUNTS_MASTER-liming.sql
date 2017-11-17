-- Create table
create table CRM_T_ACCOUNTS_MASTER
(
  c_pid            VARCHAR2(32) not null,
  c_accounts_code  VARCHAR2(32),
  c_proposer_id    VARCHAR2(32),
  c_company_id     VARCHAR2(32),
  c_period_agt     VARCHAR2(300),
  c_period_delay   VARCHAR2(300),
  dt_apply_date    DATE,
  n_delay_amount   NUMBER(16,4),
  c_apply_reason   VARCHAR2(300),
  c_status         VARCHAR2(32),
  dt_create_time   DATE,
  c_proposer_name  VARCHAR2(300),
  c_company_name   VARCHAR2(300),
  c_created_by_id  VARCHAR2(100),
  dt_created_at    DATE,
  c_created_pos_id VARCHAR2(100),
  c_created_org_id VARCHAR2(100),
  c_updated_by_id  VARCHAR2(100),
  dt_updated_at    DATE,
  n_overdue_amount NUMBER(16,4),
  c_control_status VARCHAR2(100)
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
-- Add comments to the table 
comment on table CRM_T_ACCOUNTS_MASTER
  is '账期申请主表';
-- Add comments to the columns 
comment on column CRM_T_ACCOUNTS_MASTER.c_pid
  is '主键';
comment on column CRM_T_ACCOUNTS_MASTER.c_accounts_code
  is '账期申请编号';
comment on column CRM_T_ACCOUNTS_MASTER.c_proposer_id
  is '申请人ID';
comment on column CRM_T_ACCOUNTS_MASTER.c_company_id
  is '申请单位ID';
comment on column CRM_T_ACCOUNTS_MASTER.c_period_agt
  is '协议账期';
comment on column CRM_T_ACCOUNTS_MASTER.c_period_delay
  is '账期延期';
comment on column CRM_T_ACCOUNTS_MASTER.dt_apply_date
  is '申请日期';
comment on column CRM_T_ACCOUNTS_MASTER.n_delay_amount
  is '延期金额';
comment on column CRM_T_ACCOUNTS_MASTER.c_apply_reason
  is '申请原因';
comment on column CRM_T_ACCOUNTS_MASTER.c_status
  is '状态';
comment on column CRM_T_ACCOUNTS_MASTER.dt_create_time
  is '创建日期';
comment on column CRM_T_ACCOUNTS_MASTER.c_proposer_name
  is '申请人名称';
comment on column CRM_T_ACCOUNTS_MASTER.c_company_name
  is '申请单位名称';
comment on column CRM_T_ACCOUNTS_MASTER.c_created_by_id
  is '创建人';
comment on column CRM_T_ACCOUNTS_MASTER.dt_created_at
  is '创建时间';
comment on column CRM_T_ACCOUNTS_MASTER.c_created_pos_id
  is '创建人岗位';
comment on column CRM_T_ACCOUNTS_MASTER.c_created_org_id
  is '创建人组织';
comment on column CRM_T_ACCOUNTS_MASTER.c_updated_by_id
  is '更新者';
comment on column CRM_T_ACCOUNTS_MASTER.dt_updated_at
  is '更新时间';
comment on column CRM_T_ACCOUNTS_MASTER.n_overdue_amount
  is '逾期金额';
comment on column CRM_T_ACCOUNTS_MASTER.c_control_status
  is '控制状态';
-- Create/Recreate primary, unique and foreign key constraints 
alter table CRM_T_ACCOUNTS_MASTER
  add constraint PK_M_T_ACCOUNTS_MASTER primary key (C_PID)
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
