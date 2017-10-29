-- Create table
create table CRM_T_STATEMENT_MASTER
(
  c_pid                   VARCHAR2(32) not null,
  c_statement_code        VARCHAR2(32) not null,
  c_proposer_id           VARCHAR2(300),
  n_init_balance          NUMBER(16,4),
  c_period                VARCHAR2(300),
  c_status                VARCHAR2(300),
  dt_apply_date           DATE,
  n_current_amount        NUMBER(16,4),
  dt_create_time          DATE,
  c_created_by_id         VARCHAR2(100),
  dt_created_at           DATE,
  c_created_pos_id        VARCHAR2(100),
  c_created_org_id        VARCHAR2(100),
  c_updated_by_id         VARCHAR2(100),
  dt_updated_at           DATE,
  c_is_publish            VARCHAR2(10),
  c_proposer_name         VARCHAR2(300),
  c_customer_id           VARCHAR2(32),
  c_customer_code         VARCHAR2(300),
  c_customer_name         VARCHAR2(300),
  dt_statement_date_begin DATE,
  dt_statement_date_end   DATE,
  c_is_post_account       VARCHAR2(10)
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
comment on table CRM_T_STATEMENT_MASTER
  is '对账单头表';
-- Add comments to the columns 
comment on column CRM_T_STATEMENT_MASTER.c_pid
  is '主键';
comment on column CRM_T_STATEMENT_MASTER.c_statement_code
  is '对账申请编号';
comment on column CRM_T_STATEMENT_MASTER.c_proposer
  is '申请人';
comment on column CRM_T_STATEMENT_MASTER.n_init_balance
  is '期初余额';
comment on column CRM_T_STATEMENT_MASTER.c_period
  is '对账期间';
comment on column CRM_T_STATEMENT_MASTER.c_status
  is '状态';
comment on column CRM_T_STATEMENT_MASTER.dt_apply_date
  is '申请日期';
comment on column CRM_T_STATEMENT_MASTER.n_current_amount
  is '本期累计';
comment on column CRM_T_STATEMENT_MASTER.dt_create_time
  is '创建日期';
comment on column CRM_T_STATEMENT_MASTER.c_creator
  is '创建人';
-- Create/Recreate primary, unique and foreign key constraints 
alter table CRM_T_STATEMENT_MASTER
  add constraint PK_M_T_STATEMENT_MASTER primary key (C_PID)
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
