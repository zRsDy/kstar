-- Create table
create table CRM_T_ORDER_HEADER
(
  c_pid                     VARCHAR2(32) not null,
  c_order_code              VARCHAR2(32) not null,
  c_customer_code           VARCHAR2(32) not null,
  c_customer_name           VARCHAR2(300),
  c_cust_attn_code          VARCHAR2(300),
  c_cust_attn_name          VARCHAR2(300),
  c_customer_po             VARCHAR2(300),
  c_delivery_address        VARCHAR2(300),
  c_bill_address            VARCHAR2(300),
  c_business_entity         VARCHAR2(300),
  c_ship_org                VARCHAR2(300),
  c_source_type             VARCHAR2(300),
  c_source_code             VARCHAR2(300),
  c_order_type              VARCHAR2(300),
  dt_order_date             DATE,
  dt_request_date           DATE,
  c_currency                VARCHAR2(32),
  c_convert_type            VARCHAR2(32),
  dt_convert_date           DATE,
  c_price_table_id          VARCHAR2(32),
  n_amount                  NUMBER(16,4),
  c_remark                  VARCHAR2(1000),
  c_salesman_id             VARCHAR2(32),
  c_salesman_center         VARCHAR2(32),
  c_salesman_dep            VARCHAR2(300),
  c_sales_territory         VARCHAR2(32),
  c_execute_status          VARCHAR2(32),
  c_control_status          VARCHAR2(32),
  n_is_abnormal_price       NUMBER(1),
  n_is_abnormal_credit      NUMBER(1),
  c_term_payment            VARCHAR2(32),
  c_term_payment_detail     VARCHAR2(1000),
  c_payment_customer_id     VARCHAR2(32),
  c_final_cust_id           VARCHAR2(32),
  c_final_cust_trade_b      VARCHAR2(32),
  c_final_cust_trade_s      VARCHAR2(32),
  c_is_install              VARCHAR2(32),
  c_is_am                   VARCHAR2(32),
  c_inspection_cycle        VARCHAR2(32),
  c_warranty_period         VARCHAR2(32),
  c_ship_type               VARCHAR2(32),
  c_is_home_delivery        VARCHAR2(10),
  c_is_destination_delivery VARCHAR2(10),
  c_service_clause          VARCHAR2(300),
  n_collection_freight      NUMBER(16,4),
  n_die_cost                NUMBER(16,4),
  n_development_costs       NUMBER(16,4),
  c_cust_brand_class        VARCHAR2(32),
  c_is_free_spare           VARCHAR2(32),
  c_is_random_spare         VARCHAR2(10),
  n_spare_amount            NUMBER(16,4),
  n_spare_balance           NUMBER(16,4),
  c_spare_amount_situation  VARCHAR2(32),
  c_spare_order_no          VARCHAR2(32),
  c_customer_id             VARCHAR2(32),
  c_source_name             VARCHAR2(300),
  c_salesman_name           VARCHAR2(300),
  c_payment_customer_name   VARCHAR2(300),
  c_final_cust_name         VARCHAR2(300),
  c_delivery_address_id     VARCHAR2(32),
  c_bill_address_id         VARCHAR2(32),
  c_erp_order_code          VARCHAR2(32),
  c_price_table_name        VARCHAR2(300),
  c_created_by_id           VARCHAR2(100),
  dt_created_at             DATE,
  c_created_pos_id          VARCHAR2(100),
  c_created_org_id          VARCHAR2(100),
  c_updated_by_id           VARCHAR2(100),
  dt_updated_at             DATE,
  c_source_id               VARCHAR2(32),
  c_salesman_code           VARCHAR2(100),
  c_customer_erp_code       VARCHAR2(32),
  c_cust_attn_tel           VARCHAR2(300),
  c_zip_code                VARCHAR2(300),
  c_business_manager_id     VARCHAR2(32),
  c_business_manager_code   VARCHAR2(300),
  c_business_manager_name   VARCHAR2(300),
  c_salesman_position       VARCHAR2(300)
)
tablespace TS_CRM
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
comment on table CRM_T_ORDER_HEADER
  is '订单头表';
-- Add comments to the columns 
comment on column CRM_T_ORDER_HEADER.c_pid
  is '主键';
comment on column CRM_T_ORDER_HEADER.c_order_code
  is '订单编号';
comment on column CRM_T_ORDER_HEADER.c_customer_code
  is '客户编号';
comment on column CRM_T_ORDER_HEADER.c_customer_name
  is '客户名称';
comment on column CRM_T_ORDER_HEADER.c_cust_attn_code
  is '客户联系人编号';
comment on column CRM_T_ORDER_HEADER.c_cust_attn_name
  is '客户联系人名称';
comment on column CRM_T_ORDER_HEADER.c_customer_po
  is '客户PO';
comment on column CRM_T_ORDER_HEADER.c_delivery_address
  is '收货地点';
comment on column CRM_T_ORDER_HEADER.c_bill_address
  is '收单地点';
comment on column CRM_T_ORDER_HEADER.c_business_entity
  is '业务实体';
comment on column CRM_T_ORDER_HEADER.c_ship_org
  is '发货组织';
comment on column CRM_T_ORDER_HEADER.c_source_type
  is '来源类型';
comment on column CRM_T_ORDER_HEADER.c_source_code
  is '来源编号';
comment on column CRM_T_ORDER_HEADER.c_order_type
  is '订单类型';
comment on column CRM_T_ORDER_HEADER.dt_order_date
  is '订购日期';
comment on column CRM_T_ORDER_HEADER.dt_request_date
  is '请求日期';
comment on column CRM_T_ORDER_HEADER.c_currency
  is '货币';
comment on column CRM_T_ORDER_HEADER.c_convert_type
  is '折换类型';
comment on column CRM_T_ORDER_HEADER.dt_convert_date
  is '折换日期';
comment on column CRM_T_ORDER_HEADER.c_price_table_id
  is '价目表ID';
comment on column CRM_T_ORDER_HEADER.n_amount
  is '合计';
comment on column CRM_T_ORDER_HEADER.c_remark
  is '题头备注';
comment on column CRM_T_ORDER_HEADER.c_salesman_id
  is '销售人员ID';
comment on column CRM_T_ORDER_HEADER.c_salesman_center
  is '销售人员所属中心';
comment on column CRM_T_ORDER_HEADER.c_salesman_dep
  is '销售人员所属部门';
comment on column CRM_T_ORDER_HEADER.c_sales_territory
  is '销售区域';
comment on column CRM_T_ORDER_HEADER.c_execute_status
  is '执行状态';
comment on column CRM_T_ORDER_HEADER.c_control_status
  is '控制状态';
comment on column CRM_T_ORDER_HEADER.n_is_abnormal_price
  is '是否价格异常';
comment on column CRM_T_ORDER_HEADER.n_is_abnormal_credit
  is '是否信用异常';
comment on column CRM_T_ORDER_HEADER.c_term_payment
  is '付款条件';
comment on column CRM_T_ORDER_HEADER.c_term_payment_detail
  is '详情付款条款';
comment on column CRM_T_ORDER_HEADER.c_payment_customer_id
  is '付款客户ID';
comment on column CRM_T_ORDER_HEADER.c_final_cust_id
  is '最终用户ID';
comment on column CRM_T_ORDER_HEADER.c_final_cust_trade_b
  is '最终用户行业大类';
comment on column CRM_T_ORDER_HEADER.c_final_cust_trade_s
  is '最终用户行业小类';
comment on column CRM_T_ORDER_HEADER.c_is_install
  is '是否上门安装';
comment on column CRM_T_ORDER_HEADER.c_is_am
  is '是否所需辅材';
comment on column CRM_T_ORDER_HEADER.c_inspection_cycle
  is '巡检周期';
comment on column CRM_T_ORDER_HEADER.c_warranty_period
  is '保修年限';
comment on column CRM_T_ORDER_HEADER.c_ship_type
  is '运输方式';
comment on column CRM_T_ORDER_HEADER.c_is_home_delivery
  is '是否送货上门';
comment on column CRM_T_ORDER_HEADER.c_is_destination_delivery
  is '是否卸货到客户指定安装地点';
comment on column CRM_T_ORDER_HEADER.c_service_clause
  is '售后服务条款';
comment on column CRM_T_ORDER_HEADER.n_collection_freight
  is '代收运费';
comment on column CRM_T_ORDER_HEADER.n_die_cost
  is '模具费';
comment on column CRM_T_ORDER_HEADER.n_development_costs
  is '开发费用';
comment on column CRM_T_ORDER_HEADER.c_cust_brand_class
  is '客户品牌分类';
comment on column CRM_T_ORDER_HEADER.c_is_free_spare
  is '是否免费赠送备件';
comment on column CRM_T_ORDER_HEADER.c_is_random_spare
  is '是否随机发赠备件';
comment on column CRM_T_ORDER_HEADER.n_spare_amount
  is '备件额度';
comment on column CRM_T_ORDER_HEADER.n_spare_balance
  is '备件剩余额度';
comment on column CRM_T_ORDER_HEADER.c_spare_amount_situation
  is '备件额度使用情况';
comment on column CRM_T_ORDER_HEADER.c_spare_order_no
  is '备件订单编号';
comment on column CRM_T_ORDER_HEADER.c_customer_id
  is '客户ID';
comment on column CRM_T_ORDER_HEADER.c_source_name
  is '来源名称，当来源为合同时为合同名称，当来源是渠道是为商机名称';
comment on column CRM_T_ORDER_HEADER.c_salesman_name
  is '销售人员名称';
comment on column CRM_T_ORDER_HEADER.c_payment_customer_name
  is '付款客户名称';
comment on column CRM_T_ORDER_HEADER.c_final_cust_name
  is '最终用户名称';
comment on column CRM_T_ORDER_HEADER.c_delivery_address_id
  is '收货地点ID';
comment on column CRM_T_ORDER_HEADER.c_bill_address_id
  is '收单地点ID';
comment on column CRM_T_ORDER_HEADER.c_erp_order_code
  is 'ERP订单编号';
comment on column CRM_T_ORDER_HEADER.c_price_table_name
  is '价目表名称';
comment on column CRM_T_ORDER_HEADER.c_created_by_id
  is '创建人';
comment on column CRM_T_ORDER_HEADER.dt_created_at
  is '创建时间';
comment on column CRM_T_ORDER_HEADER.c_created_pos_id
  is '创建人岗位';
comment on column CRM_T_ORDER_HEADER.c_created_org_id
  is '创建人组织';
comment on column CRM_T_ORDER_HEADER.c_updated_by_id
  is '更新者';
comment on column CRM_T_ORDER_HEADER.dt_updated_at
  is '更新时间';
comment on column CRM_T_ORDER_HEADER.c_source_id
  is '来源ID，当来源为合同时为合同ID，当来源是渠道是为商机ID';
comment on column CRM_T_ORDER_HEADER.c_salesman_code
  is '销售人员编码';
comment on column CRM_T_ORDER_HEADER.c_customer_erp_code
  is '客户ERP编号';
comment on column CRM_T_ORDER_HEADER.c_cust_attn_tel
  is '客户联系人/收货人电话';
comment on column CRM_T_ORDER_HEADER.c_zip_code
  is '收人地址邮编';
comment on column CRM_T_ORDER_HEADER.c_business_manager_id
  is '商务专员ID';
comment on column CRM_T_ORDER_HEADER.c_business_manager_code
  is '商务专员编号';
comment on column CRM_T_ORDER_HEADER.c_business_manager_name
  is '商务专员名称';
comment on column CRM_T_ORDER_HEADER.c_salesman_position
  is '销售人员岗位';
-- Create/Recreate primary, unique and foreign key constraints 
alter table CRM_T_ORDER_HEADER
  add constraint PK_CRM_T_ORDER_HEADER primary key (C_PID)
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
alter table CRM_T_ORDER_HEADER
  add constraint UK_CRM_T_ORDER_HEADER unique (C_ORDER_CODE)
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
