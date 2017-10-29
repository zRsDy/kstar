-- Create table
create table CRM_T_DELIVERY_LINES
(
  c_pid               VARCHAR2(32) not null,
  c_delivery_code     VARCHAR2(300),
  c_order_code        VARCHAR2(32),
  c_order_type        VARCHAR2(300),
  c_single_cust_code  VARCHAR2(32),
  c_single_cust_name  VARCHAR2(300),
  c_product_model     VARCHAR2(300),
  c_materiel_code     VARCHAR2(300),
  c_materiel_name     VARCHAR2(300),
  c_unit              VARCHAR2(32),
  n_receipt_quantity  NUMBER(15,4),
  n_delivery_quantity NUMBER(15,4),
  n_residual_quantity NUMBER(15,4),
  dt_order_date       DATE,
  dt_arrival_date     DATE,
  dt_promise_date     DATE,
  c_plan_status       VARCHAR2(32),
  c_line_num          VARCHAR2(32),
  c_external_no       VARCHAR2(300),
  dt_print_time       DATE,
  c_remarks           VARCHAR2(1000),
  c_order_id          VARCHAR2(32),
  c_create_time       DATE,
  c_creator           VARCHAR2(32),
  c_single_cust_po    VARCHAR2(32),
  c_order_line_no     VARCHAR2(32),
  c_delivery_id       VARCHAR2(32),
  c_delivery_date     DATE,
  c_single_cust_id    VARCHAR2(32),
  n_invoice_quantity  NUMBER(15,4),
  c_updated_by_id     VARCHAR2(32),
  dt_updated_at       DATE,
  c_rece_customer     VARCHAR2(300),
  c_is_post_account   VARCHAR2(10),
  c_source_code       VARCHAR2(300),
  c_erp_order_code    VARCHAR2(300),
  c_delete_flag       VARCHAR2(10),
  c_erp_status        VARCHAR2(300),
  n_a                 NUMBER(16,4),
  n_delivery_amount   NUMBER(16,4),
  n_price             NUMBER(16,4),
  c_pro_desc          VARCHAR2(3000),
  c_erp_import_flag   VARCHAR2(300) default 'No'
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
comment on table CRM_T_DELIVERY_LINES
  is '出货申请行表';
-- Add comments to the columns 
comment on column CRM_T_DELIVERY_LINES.c_pid
  is '主键';
comment on column CRM_T_DELIVERY_LINES.c_delivery_code
  is '发货申请单头编号';
comment on column CRM_T_DELIVERY_LINES.c_order_code
  is '订单编号';
comment on column CRM_T_DELIVERY_LINES.c_order_type
  is '订单类型';
comment on column CRM_T_DELIVERY_LINES.c_single_cust_code
  is '下单客户编号';
comment on column CRM_T_DELIVERY_LINES.c_single_cust_name
  is '下单客户编号';
comment on column CRM_T_DELIVERY_LINES.n_price
  is '单价';
comment on column CRM_T_DELIVERY_LINES.c_rece_customer
  is '收货客户';
comment on column CRM_T_DELIVERY_LINES.c_product_model
  is '产品型号';
comment on column CRM_T_DELIVERY_LINES.c_part_no
  is '物料编码';
comment on column CRM_T_DELIVERY_LINES.c_part_name
  is '物料名称';
comment on column CRM_T_DELIVERY_LINES.c_unit
  is '单位';
comment on column CRM_T_DELIVERY_LINES.n_non_delivery_quantity
  is '未发运数量';
comment on column CRM_T_DELIVERY_LINES.n_delivery_quantity
  is '发货数量';
comment on column CRM_T_DELIVERY_LINES.n_addr_delivery_quantity
  is '地址送货数量';
comment on column CRM_T_DELIVERY_LINES.n_residual_quantity
  is '剩余数量';
comment on column CRM_T_DELIVERY_LINES.dt_order_date
  is '下单日期';
comment on column CRM_T_DELIVERY_LINES.dt_arrival_date
  is '客户要货日期';
comment on column CRM_T_DELIVERY_LINES.dt_promise_date
  is '工厂承诺日期';
comment on column CRM_T_DELIVERY_LINES.c_plan_status
  is '计划状态';
comment on column CRM_T_DELIVERY_LINES.c_line_num
  is '行号';
comment on column CRM_T_DELIVERY_LINES.c_external_no
  is '出货单编号(外部)';
comment on column CRM_T_DELIVERY_LINES.dt_print_time
  is '出货单打印日期（外部）';
comment on column CRM_T_DELIVERY_LINES.c_remarks
  is '备注';
comment on column CRM_T_DELIVERY_LINES.c_source_code
  is '来源编号,当来源为合同时为合同编号，当来源是渠道是为商机编号';
comment on column CRM_T_DELIVERY_LINES.c_erp_order_code
  is 'ERP订单编号';
comment on column CRM_T_DELIVERY_LINES.c_delete_flag
  is '删除标志, 1.已删除，0 未删除';
comment on column CRM_T_DELIVERY_LINES.c_erp_status
  is 'ERP状态';
comment on column CRM_T_DELIVERY_LINES.c_pro_desc
  is '产品说明';
comment on column CRM_T_DELIVERY_LINES.c_erp_import_flag
  is '是否导入ERP';
-- Create/Recreate indexes 
create index DELIVERY_L_C_DELIVERY_CODE on CRM_T_DELIVERY_LINES (C_DELIVERY_CODE)
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
-- Create/Recreate primary, unique and foreign key constraints 
alter table CRM_T_DELIVERY_LINES
  add constraint PK_M_T_DELIVERY_LINES primary key (C_PID)
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
