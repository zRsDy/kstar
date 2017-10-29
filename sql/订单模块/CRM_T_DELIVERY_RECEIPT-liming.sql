-- Create table
create table CRM_T_DELIVERY_RECEIPT
(
  c_pid                     VARCHAR2(32) not null,
  c_receipt_code            VARCHAR2(32),
  c_delivery_code           VARCHAR2(32),
  c_order_code              VARCHAR2(32),
  c_materiel_code           VARCHAR2(32),
  c_receiving_address       VARCHAR2(300),
  c_consignee               VARCHAR2(300),
  c_consignee_tel           VARCHAR2(300),
  c_unit                    VARCHAR2(32),
  n_delivery_quantity       NUMBER(15,4),
  c_remarks                 VARCHAR2(600),
  c_materiel_name           VARCHAR2(300),
  c_external_no             VARCHAR2(32),
  dt_invoice_print_time     DATE,
  n_receipt_quantity        NUMBER(15,4) default 0,
  c_logistics_company       VARCHAR2(300),
  dt_create_time            DATE,
  c_creator                 VARCHAR2(300),
  c_delivery_lines_num      VARCHAR2(32),
  c_logistics_no            VARCHAR2(300),
  dt_estimate_arrival_time  DATE,
  dt_actual_arrival_time    DATE,
  n_estimate_time           NUMBER(16,2),
  n_actual_time             NUMBER(16,2),
  c_order_line_no           VARCHAR2(32),
  n_delivery_amount         NUMBER(16,4),
  c_updated_by_id           VARCHAR2(100),
  dt_updated_at             DATE,
  c_created_by_id           VARCHAR2(100),
  dt_created_at             DATE,
  c_created_pos_id          VARCHAR2(100),
  c_created_org_id          VARCHAR2(100),
  c_control_status          VARCHAR2(100),
  c_status                  VARCHAR2(100),
  c_is_main                 VARCHAR2(10),
  c_order_id                VARCHAR2(32),
  c_source_code             VARCHAR2(300),
  c_erp_order_code          VARCHAR2(300),
  c_delete_flag             VARCHAR2(10),
  c_logistics_contacts_name VARCHAR2(300),
  c_logistics_contacts_tel  VARCHAR2(300),
  c_local_logistics_no      VARCHAR2(300),
  c_single_cust_id          VARCHAR2(32),
  c_single_cust_code        VARCHAR2(32),
  c_single_cust_name        VARCHAR2(300),
  c_pro_desc                VARCHAR2(3000),
  c_erp_import_flag         VARCHAR2(300) default 'No',
  c_error_message           VARCHAR2(3000)
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
comment on table CRM_T_DELIVERY_RECEIPT
  is '签收单';
-- Add comments to the columns 
comment on column CRM_T_DELIVERY_RECEIPT.c_pid
  is '主键';
comment on column CRM_T_DELIVERY_RECEIPT.c_receipt_code
  is '签收单编号';
comment on column CRM_T_DELIVERY_RECEIPT.c_delivery_code
  is '发货申请编号';
comment on column CRM_T_DELIVERY_RECEIPT.c_order_code
  is '订单编号';
comment on column CRM_T_DELIVERY_RECEIPT.c_materiel_code
  is '物料编码';
comment on column CRM_T_DELIVERY_RECEIPT.c_delivery_address
  is '收货地点';
comment on column CRM_T_DELIVERY_RECEIPT.c_consignee
  is '收货人';
comment on column CRM_T_DELIVERY_RECEIPT.c_consignee_tel
  is '收货人电话';
comment on column CRM_T_DELIVERY_RECEIPT.c_unit
  is '单位';
comment on column CRM_T_DELIVERY_RECEIPT.n_delivery_quantity
  is '发货数量';
comment on column CRM_T_DELIVERY_RECEIPT.c_remarks
  is '备注';
comment on column CRM_T_DELIVERY_RECEIPT.c_materiel_name
  is '物料名称';
comment on column CRM_T_DELIVERY_RECEIPT.c_external_no
  is '出货单编号(外部)';
comment on column CRM_T_DELIVERY_RECEIPT.dt_invoice_print_time
  is '出货单打印日期（外部）';
comment on column CRM_T_DELIVERY_RECEIPT.n_receipt_quantity
  is '签收数量';
comment on column CRM_T_DELIVERY_RECEIPT.c_logistics_company
  is '物流公司';
comment on column CRM_T_DELIVERY_RECEIPT.dt_create_time
  is '创建日期';
comment on column CRM_T_DELIVERY_RECEIPT.c_creator
  is '创建人';
  comment on column CRM_T_DELIVERY_RECEIPT.c_source_code
  is '来源编号,当来源为合同时为合同编号，当来源是渠道是为商机编号';
comment on column CRM_T_DELIVERY_RECEIPT.c_erp_order_code
  is 'ERP订单编号';
comment on column CRM_T_DELIVERY_RECEIPT.c_delete_flag
  is '删除标志, 1.已删除，0 未删除';
comment on column CRM_T_DELIVERY_RECEIPT.c_logistics_contacts_name
  is '当地物流联系人名称';
comment on column CRM_T_DELIVERY_RECEIPT.c_logistics_contacts_tel
  is '当地物流联系人电话';
comment on column CRM_T_DELIVERY_RECEIPT.c_local_logistics_no
  is '当地物流单号';
comment on column CRM_T_DELIVERY_RECEIPT.c_single_cust_id
  is '客户ID';
comment on column CRM_T_DELIVERY_RECEIPT.c_single_cust_code
  is '客户编码';
comment on column CRM_T_DELIVERY_RECEIPT.c_single_cust_name
  is '客户名称';
comment on column CRM_T_DELIVERY_RECEIPT.c_pro_desc
  is '产品说明';
comment on column CRM_T_DELIVERY_RECEIPT.c_erp_import_flag
  is '是否导入ERP';
-- Create/Recreate primary, unique and foreign key constraints 
alter table CRM_T_DELIVERY_RECEIPT
  add constraint PK_M_T_DELIVERY_RECEIPT primary key (C_PID, C_RECEIPT_CODE, C_DELIVERY_CODE)
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
