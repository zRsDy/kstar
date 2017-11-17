-- Create table
create table CRM_T_ORDER_LINES_CHANGE
(
  c_pid                   VARCHAR2(32) not null,
  c_order_code            VARCHAR2(300),
  c_pro_model             VARCHAR2(300),
  c_materiel_code         VARCHAR2(300),
  c_item_description      VARCHAR2(300),
  n_product_quantity      NUMBER(15,4) default 0,
  c_unit                  VARCHAR2(32),
  n_price                 NUMBER(32,4),
  n_amount                NUMBER(32,4),
  dt_request_date         DATE,
  dt_promise_date         DATE,
  c_status                VARCHAR2(300),
  c_confirm_delivery_date VARCHAR2(10),
  c_ship_org              VARCHAR2(300),
  c_is_advance_billing    VARCHAR2(100),
  c_return_reason         VARCHAR2(300),
  c_return_reference      VARCHAR2(300),
  c_is_pending            VARCHAR2(10),
  n_delivery_quantity     NUMBER(15,4) default 0,
  n_billing_quantity      NUMBER(15,4) default 0,
  c_receiving_customer    VARCHAR2(300),
  c_consignee             VARCHAR2(300),
  c_line_no               VARCHAR2(300),
  c_order_id              VARCHAR2(32),
  c_pro_id                VARCHAR2(32),
  c_create_time           DATE,
  c_creator               VARCHAR2(32),
  c_materiel_name         VARCHAR2(300),
  n_cancel_quantity       NUMBER(15,4) default 0,
  c_updated_by_id         VARCHAR2(32),
  dt_updated_at           DATE,
  c_original_line_id      VARCHAR2(32),
  c_source_id             VARCHAR2(32),
  c_remark                VARCHAR2(600),
  c_erp_plan_status       VARCHAR2(100),
  c_is_erp_delivery       VARCHAR2(100),
  n_erp_settlement_price  NUMBER(32,4),
  c_erp_status            VARCHAR2(100),
  c_special_price_code    VARCHAR2(300),
  c_is_special_price      VARCHAR2(10),
  c_pro_brand             VARCHAR2(300),
  c_source_code           VARCHAR2(300),
  c_special_price_line_id VARCHAR2(32),
  parent_line_num         VARCHAR2(32),
  root_line_num           VARCHAR2(32),
  back_line_num           VARCHAR2(32),
  c_pro_desc              VARCHAR2(1000),
  c_from_id               VARCHAR2(300),
  c_order_change_id       VARCHAR2(300)
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
comment on table CRM_T_ORDER_LINES_CHANGE
  is '订单行表';
-- Add comments to the columns 
comment on column CRM_T_ORDER_LINES_CHANGE.c_pid
  is '主键';
comment on column CRM_T_ORDER_LINES_CHANGE.c_order_code
  is '订单头号';
comment on column CRM_T_ORDER_LINES_CHANGE.c_pro_model
  is '产品型号';
comment on column CRM_T_ORDER_LINES_CHANGE.c_materiel_code
  is '物料编号';
comment on column CRM_T_ORDER_LINES_CHANGE.c_item_description
  is '项目说明';
comment on column CRM_T_ORDER_LINES_CHANGE.n_product_quantity
  is '数量';
comment on column CRM_T_ORDER_LINES_CHANGE.c_unit
  is '单位';
comment on column CRM_T_ORDER_LINES_CHANGE.n_price
  is '单价';
comment on column CRM_T_ORDER_LINES_CHANGE.n_amount
  is '金额';
comment on column CRM_T_ORDER_LINES_CHANGE.dt_request_date
  is '请求日期';
comment on column CRM_T_ORDER_LINES_CHANGE.dt_promise_date
  is '承诺日期';
comment on column CRM_T_ORDER_LINES_CHANGE.c_status
  is '状态';
comment on column CRM_T_ORDER_LINES_CHANGE.c_confirm_delivery_date
  is '是否需要启动交期确认';
comment on column CRM_T_ORDER_LINES_CHANGE.c_ship_org
  is '发货组织';
comment on column CRM_T_ORDER_LINES_CHANGE.c_is_advance_billing
  is '是否提前开票';
comment on column CRM_T_ORDER_LINES_CHANGE.c_return_reason
  is '退货原因';
comment on column CRM_T_ORDER_LINES_CHANGE.c_return_reference
  is '退货参考';
comment on column CRM_T_ORDER_LINES_CHANGE.c_is_pending
  is '是否暂挂';
comment on column CRM_T_ORDER_LINES_CHANGE.n_delivery_quantity
  is '发运数量';
comment on column CRM_T_ORDER_LINES_CHANGE.n_billing_quantity
  is '开票数量';
comment on column CRM_T_ORDER_LINES_CHANGE.c_receiving_customer
  is '收货客户';
comment on column CRM_T_ORDER_LINES_CHANGE.c_consignee
  is '收货方';
comment on column CRM_T_ORDER_LINES_CHANGE.c_line_no
  is '行号';
comment on column CRM_T_ORDER_LINES_CHANGE.c_order_id
  is '订单ID';
comment on column CRM_T_ORDER_LINES_CHANGE.c_pro_id
  is '产品ID';
comment on column CRM_T_ORDER_LINES_CHANGE.c_create_time
  is '创建日期';
comment on column CRM_T_ORDER_LINES_CHANGE.c_creator
  is '创建人';
comment on column CRM_T_ORDER_LINES_CHANGE.c_materiel_name
  is '物料名称';
comment on column CRM_T_ORDER_LINES_CHANGE.n_cancel_quantity
  is '取消数量';
comment on column CRM_T_ORDER_LINES_CHANGE.c_updated_by_id
  is '更新者';
comment on column CRM_T_ORDER_LINES_CHANGE.dt_updated_at
  is '更新时间';
comment on column CRM_T_ORDER_LINES_CHANGE.c_original_line_id
  is '原订单行ID';
comment on column CRM_T_ORDER_LINES_CHANGE.c_source_id
  is '来源行ID：合同未工程清单ID，商机为';
comment on column CRM_T_ORDER_LINES_CHANGE.c_remark
  is '备注';
comment on column CRM_T_ORDER_LINES_CHANGE.c_erp_plan_status
  is 'ERP计划状态';
comment on column CRM_T_ORDER_LINES_CHANGE.c_is_erp_delivery
  is 'ERP是否已发货';
comment on column CRM_T_ORDER_LINES_CHANGE.n_erp_settlement_price
  is 'ERP结算单价';
comment on column CRM_T_ORDER_LINES_CHANGE.c_erp_status
  is 'ERP状态';
comment on column CRM_T_ORDER_LINES_CHANGE.c_special_price_code
  is '特价编号';
comment on column CRM_T_ORDER_LINES_CHANGE.c_is_special_price
  is '是否特价';
comment on column CRM_T_ORDER_LINES_CHANGE.c_pro_brand
  is '品牌';
comment on column CRM_T_ORDER_LINES_CHANGE.c_source_code
  is '来源编号：合同为合同编号，商机为商机编号';
comment on column CRM_T_ORDER_LINES_CHANGE.c_special_price_line_id
  is '特价行ID';
comment on column CRM_T_ORDER_LINES_CHANGE.parent_line_num
  is '拆行上级行号';
comment on column CRM_T_ORDER_LINES_CHANGE.root_line_num
  is '拆行根行号';
comment on column CRM_T_ORDER_LINES_CHANGE.c_pro_desc
  is '产品描述';
comment on column CRM_T_ORDER_LINES_CHANGE.c_from_id
  is '来源订单行ID';
comment on column CRM_T_ORDER_LINES_CHANGE.c_order_change_id
  is '订单变更头ID';
