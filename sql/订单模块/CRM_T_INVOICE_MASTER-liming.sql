-- Create table
create table CRM_T_INVOICE_MASTER
(
  c_pid                   VARCHAR2(32) not null,
  c_invoice_code          VARCHAR2(32) not null,
  c_proposer_id           VARCHAR2(32),
  dt_apply_date           DATE,
  c_postal_address        VARCHAR2(600),
  c_express_no            VARCHAR2(300),
  c_consignee             VARCHAR2(300),
  c_consignee_tel         VARCHAR2(300),
  dt_invoice_date         DATE,
  n_invoice_amount        NUMBER(16,4),
  c_status                VARCHAR2(32),
  c_remarks               VARCHAR2(1000),
  dt_create_time          DATE,
  c_proposer_name         VARCHAR2(300),
  c_customer_id           VARCHAR2(32),
  c_customer_code         VARCHAR2(300),
  c_customer_name         VARCHAR2(300),
  c_created_by_id         VARCHAR2(100),
  dt_created_at           DATE,
  c_created_pos_id        VARCHAR2(100),
  c_created_org_id        VARCHAR2(100),
  c_updated_by_id         VARCHAR2(100),
  dt_updated_at           DATE,
  c_invoice_type          VARCHAR2(100),
  c_tax_no                VARCHAR2(100),
  c_contract_code         VARCHAR2(300),
  c_address               VARCHAR2(600),
  c_tel_no                VARCHAR2(300),
  c_bank_act              VARCHAR2(300),
  c_act_no                VARCHAR2(300),
  c_business_entity       VARCHAR2(32),
  c_currency              VARCHAR2(32),
  c_business_manager_id   VARCHAR2(32),
  c_business_manager_code VARCHAR2(300),
  c_business_manager_name VARCHAR2(300),
  c_golden_tax_no         VARCHAR2(300)
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
comment on table CRM_T_INVOICE_MASTER
  is '开票主表表';
-- Add comments to the columns 
comment on column CRM_T_INVOICE_MASTER.c_pid
  is '主键';
comment on column CRM_T_INVOICE_MASTER.c_invoice_code
  is '开票申请编号';
comment on column CRM_T_INVOICE_MASTER.c_proposer
  is '申请人';
comment on column CRM_T_INVOICE_MASTER.dt_apply_date
  is '申请日期';
comment on column CRM_T_INVOICE_MASTER.c_postal_address
  is '邮寄地址';
comment on column CRM_T_INVOICE_MASTER.c_express_no
  is '快递单号';
comment on column CRM_T_INVOICE_MASTER.c_consignee
  is '收票人';
comment on column CRM_T_INVOICE_MASTER.c_consignee_tel
  is '收票人电话';
comment on column CRM_T_INVOICE_MASTER.dt_invoice_date
  is '开票日期';
comment on column CRM_T_INVOICE_MASTER.n_invoice_amount
  is '开票金额';
comment on column CRM_T_INVOICE_MASTER.c_stutas
  is '状态';
comment on column CRM_T_INVOICE_MASTER.c_remarks
  is '开票说明';
comment on column CRM_T_INVOICE_MASTER.dt_create_time
  is '创建日期';
comment on column CRM_T_INVOICE_MASTER.c_creator
  is '创建人';
comment on column CRM_T_INVOICE_MASTER.c_business_entity
  is '业务实体';
comment on column CRM_T_INVOICE_MASTER.c_currency
  is '货币';
comment on column CRM_T_INVOICE_MASTER.c_business_manager_id
  is '商务专员ID';
comment on column CRM_T_INVOICE_MASTER.c_business_manager_code
  is '商务专员编号';
comment on column CRM_T_INVOICE_MASTER.c_business_manager_name
  is '商务专员名称';
comment on column CRM_T_INVOICE_MASTER.c_golden_tax_no
  is '金税发票';
-- Create/Recreate primary, unique and foreign key constraints 
alter table CRM_T_INVOICE_MASTER
  add constraint PK_M_T_INVOICE_MASTER primary key (C_PID)
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
