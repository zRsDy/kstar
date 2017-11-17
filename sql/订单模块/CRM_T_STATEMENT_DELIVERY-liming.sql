-- Create table
create table CRM_T_STATEMENT_DELIVERY
(
  c_pid            VARCHAR2(32) not null,
  c_statement_code VARCHAR2(32) not null,
  c_delivery_code  VARCHAR2(32),
  c_order_code     VARCHAR2(32),
  dt_delivery_date DATE,
  c_materiel_name  VARCHAR2(300),
  c_currency       VARCHAR2(32),
  n_price          NUMBER(16,4),
  n_delivery_qty   NUMBER(16,4),
  n_payable_amount NUMBER(16,4),
  c_updated_by_id  VARCHAR2(32),
  dt_updated_at    DATE
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
comment on table CRM_T_STATEMENT_DELIVERY
  is '对账单发货表';
-- Add comments to the columns 
comment on column CRM_T_STATEMENT_DELIVERY.c_pid
  is '主键';
comment on column CRM_T_STATEMENT_DELIVERY.c_statement_code
  is '对账申请编号';
comment on column CRM_T_STATEMENT_DELIVERY.c_delivery_code
  is '发货申请编号';
comment on column CRM_T_STATEMENT_DELIVERY.c_order_code
  is '订单编号';
comment on column CRM_T_STATEMENT_DELIVERY.dt_delivery_date
  is '发货日期';
comment on column CRM_T_STATEMENT_DELIVERY.c_materiel_name
  is '物料名称';
comment on column CRM_T_STATEMENT_DELIVERY.c_currency
  is '币种';
comment on column CRM_T_STATEMENT_DELIVERY.n_price
  is '单价';
comment on column CRM_T_STATEMENT_DELIVERY.n_delivery_qty
  is '发货数量';
comment on column CRM_T_STATEMENT_DELIVERY.n_payable_amount
  is '应付金额';
comment on column CRM_T_STATEMENT_DELIVERY.c_updated_by_id
  is '更新者';
comment on column CRM_T_STATEMENT_DELIVERY.dt_updated_at
  is '更新时间';
-- Create/Recreate primary, unique and foreign key constraints 
alter table CRM_T_STATEMENT_DELIVERY
  add constraint PK_M_T_STATEMENT_DELIVERY primary key (C_PID)
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
