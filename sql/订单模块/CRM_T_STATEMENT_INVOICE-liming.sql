-- Create table
create table CRM_T_STATEMENT_INVOICE
(
  c_pid             VARCHAR2(32) not null,
  c_statement_code  VARCHAR2(32) not null,
  c_invoice_code    VARCHAR2(32) not null,
  c_invoice_type    VARCHAR2(32),
  c_invoice_code_gt VARCHAR2(32),
  c_currency        VARCHAR2(32),
  n_invoice_amount  NUMBER(16,6),
  n_payable_amount  NUMBER(16,4),
  dt_invoice_date   DATE,
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
comment on table CRM_T_STATEMENT_INVOICE
  is '对账单发票表';
-- Add comments to the columns 
comment on column CRM_T_STATEMENT_INVOICE.c_pid
  is '主键';
comment on column CRM_T_STATEMENT_INVOICE.c_statement_code
  is '对账申请编号';
comment on column CRM_T_STATEMENT_INVOICE.c_invoice_code
  is '开票编号';
comment on column CRM_T_STATEMENT_INVOICE.c_invoice_type
  is '开票类型';
comment on column CRM_T_STATEMENT_INVOICE.c_invoice_code_gt
  is '金税发票号';
comment on column CRM_T_STATEMENT_INVOICE.c_currency
  is '币种';
comment on column CRM_T_STATEMENT_INVOICE.n_invoice_amount
  is '开票金额';
comment on column CRM_T_STATEMENT_INVOICE.n_payable_amount
  is '应付金额';
comment on column CRM_T_STATEMENT_INVOICE.dt_invoice_date
  is '开票日期';
comment on column CRM_T_STATEMENT_INVOICE.c_updated_by_id
  is '更新者';
comment on column CRM_T_STATEMENT_INVOICE.dt_updated_at
  is '更新时间';
-- Create/Recreate primary, unique and foreign key constraints 
alter table CRM_T_STATEMENT_INVOICE
  add constraint PK_M_T_STATEMENT_INVOICE primary key (C_PID)
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
