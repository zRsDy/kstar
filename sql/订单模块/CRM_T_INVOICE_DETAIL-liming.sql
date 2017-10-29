-- Create table
create table CRM_T_INVOICE_DETAIL
(
  c_pid              VARCHAR2(32) not null,
  c_invoice_code     VARCHAR2(32) not null,
  c_invoice_type     VARCHAR2(32),
  c_order_code       VARCHAR2(32),
  c_cust_code        VARCHAR2(32),
  c_cust_name        VARCHAR2(300),
  c_cust_addr        VARCHAR2(600),
  c_cust_po          VARCHAR2(32),
  c_materiel_code    VARCHAR2(300),
  c_materiel_desc    VARCHAR2(600),
  dt_delivery_date   DATE,
  n_invoice_qty      NUMBER(15,4),
  n_invoice_price    NUMBER(16,4),
  n_invoice_amount   NUMBER(16,4),
  c_remarks          VARCHAR2(600),
  dt_create_time     DATE,
  c_creator          VARCHAR2(32),
  c_invoice_id       VARCHAR2(32),
  c_delivery_code    VARCHAR2(32),
  c_order_line_id    VARCHAR2(32),
  c_delivery_line_id VARCHAR2(32),
  c_updated_by_id    VARCHAR2(32),
  dt_updated_at      DATE,
  c_pro_model        VARCHAR2(300),
  c_unit             VARCHAR2(300),
  c_text             VARCHAR2(300),
  c_erp_import_flag  VARCHAR2(300) default 'No',
  c_error_message    VARCHAR2(3000),
  n_billed_qty       NUMBER default 0
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
comment on table CRM_T_INVOICE_DETAIL
  is '开票明细表';
-- Add comments to the columns 
comment on column CRM_T_INVOICE_DETAIL.c_pid
  is '主键';
comment on column CRM_T_INVOICE_DETAIL.c_invoice_code
  is '开票申请编号';
comment on column CRM_T_INVOICE_DETAIL.c_invoice_type
  is '开票类型';
comment on column CRM_T_INVOICE_DETAIL.c_order_code
  is '订单编号';
comment on column CRM_T_INVOICE_DETAIL.c_cust_code
  is '客户编号';
comment on column CRM_T_INVOICE_DETAIL.c_cust_name
  is '客户名称';
comment on column CRM_T_INVOICE_DETAIL.c_cust_addr
  is '客户地址';
comment on column CRM_T_INVOICE_DETAIL.c_cust_po
  is '客户PO';
comment on column CRM_T_INVOICE_DETAIL.c_materiel_code
  is '物料编码';
comment on column CRM_T_INVOICE_DETAIL.c_materiel_desc
  is '物料说明';
comment on column CRM_T_INVOICE_DETAIL.dt_delivery_date
  is '发货日期';
comment on column CRM_T_INVOICE_DETAIL.n_invoice_qty
  is '开票数量';
comment on column CRM_T_INVOICE_DETAIL.n_invoice_price
  is '开票单价';
comment on column CRM_T_INVOICE_DETAIL.n_invoice_amount
  is '开票金额';
comment on column CRM_T_INVOICE_DETAIL.c_remarks
  is '备注';
comment on column CRM_T_INVOICE_DETAIL.dt_create_time
  is '创建日期';
comment on column CRM_T_INVOICE_DETAIL.c_creator
  is '创建人';
 comment on column CRM_T_INVOICE_DETAIL.c_erp_import_flag
  is '是否导入ERP';
-- Create/Recreate primary, unique and foreign key constraints 
alter table CRM_T_INVOICE_DETAIL
  add constraint PK_M_T_INVOICE_DETAIL primary key (C_PID)
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
