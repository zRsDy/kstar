-- Create table
create table CRM_T_INVOICE_GOLDEN_TAX
(
  c_pid             VARCHAR2(32) not null,
  c_invoice_code    VARCHAR2(32) not null,
  c_materiel_desc   VARCHAR2(600),
  n_invoice_qty     NUMBER(15,4),
  n_invoice_price   NUMBER(16,4),
  n_invoice_amount  NUMBER(16,4),
  c_invoice_id      VARCHAR2(32),
  c_updated_by_id   VARCHAR2(32),
  dt_updated_at     DATE,
  c_pro_model       VARCHAR2(300),
  c_unit            VARCHAR2(300),
  c_remarks         VARCHAR2(300),
  c_invoice_no      VARCHAR2(300),
  c_cust_name       VARCHAR2(300),
  c_erp_import_flag VARCHAR2(300) default 'No'
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
-- Add comments to the columns 
comment on column CRM_T_INVOICE_GOLDEN_TAX.c_erp_import_flag
  is '是否导入ERP';
-- Create/Recreate primary, unique and foreign key constraints 
alter table CRM_T_INVOICE_GOLDEN_TAX
  add constraint PK_CRM_T_INVOICE_GOLDEN_TAX primary key (C_PID)
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
