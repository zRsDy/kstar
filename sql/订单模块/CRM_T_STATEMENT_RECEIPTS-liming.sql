-- Create table
create table CRM_T_STATEMENT_RECEIPTS
(
  c_pid             VARCHAR2(32) not null,
  c_statement_code  VARCHAR2(32) not null,
  c_receipts_code   VARCHAR2(32),
  dt_receipts_date  DATE,
  c_receipts_bank   VARCHAR2(300),
  c_currency        VARCHAR2(32),
  n_receipts_amount NUMBER(16,4),
  c_remarks         VARCHAR2(300),
  c_updated_by_id   VARCHAR2(32),
  dt_updated_at     DATE
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
comment on table CRM_T_STATEMENT_RECEIPTS
  is '对账单发货表';
-- Add comments to the columns 
comment on column CRM_T_STATEMENT_RECEIPTS.c_pid
  is '主键';
comment on column CRM_T_STATEMENT_RECEIPTS.c_statement_code
  is '对账申请编号';
comment on column CRM_T_STATEMENT_RECEIPTS.c_receipts_code
  is '收款单编号';
comment on column CRM_T_STATEMENT_RECEIPTS.dt_receipts_date
  is '收款日期';
comment on column CRM_T_STATEMENT_RECEIPTS.c_receipts_bank
  is '收款银行';
comment on column CRM_T_STATEMENT_RECEIPTS.c_currency
  is '币种';
comment on column CRM_T_STATEMENT_RECEIPTS.n_receipts_amount
  is '收款金额';
comment on column CRM_T_STATEMENT_RECEIPTS.c_remarks
  is '备注';
comment on column CRM_T_STATEMENT_RECEIPTS.c_updated_by_id
  is '更新者';
comment on column CRM_T_STATEMENT_RECEIPTS.dt_updated_at
  is '更新时间';
-- Create/Recreate primary, unique and foreign key constraints 
alter table CRM_T_STATEMENT_RECEIPTS
  add constraint PK_M_T_STATEMENT_RECEIPTS primary key (C_PID)
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
