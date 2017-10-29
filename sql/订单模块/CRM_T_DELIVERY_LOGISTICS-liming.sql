-- Create table
create table CRM_T_DELIVERY_LOGISTICS
(
  c_pid                VARCHAR2(32) not null,
  c_logistics_code     VARCHAR2(32),
  c_delivery_code      VARCHAR2(32),
  c_logts_co_code      VARCHAR2(32),
  c_logts_co_name      VARCHAR2(300),
  c_transport_mode     VARCHAR2(300),
  c_logistics_no       VARCHAR2(300),
  c_ship_name          VARCHAR2(300),
  dt_shipment          DATE,
  c_weight             VARCHAR2(300),
  c_volume             VARCHAR2(300),
  n_freight_internal   NUMBER(16,4),
  n_freight_external   NUMBER(16,4),
  dt_arrival           DATE,
  c_insurer            VARCHAR2(300),
  c_policy_no          VARCHAR2(300),
  n_policy_fee         NUMBER(16,4),
  dt_tender            DATE,
  c_policy_remarks     VARCHAR2(300),
  c_policy_status      VARCHAR2(300),
  c_customs_broker     VARCHAR2(300),
  c_customs_contract   VARCHAR2(300),
  c_export_invoice     VARCHAR2(300),
  c_forex_verification VARCHAR2(300),
  c_customs_date       DATE,
  c_customs_status     VARCHAR2(300),
  c_updated_by_id      VARCHAR2(32),
  dt_updated_at        DATE,
  dt_closing_time      DATE,
  dt_cut_off_date      DATE,
  c_booking_number     VARCHAR2(300)
)
tablespace TS_CRM
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the table 
comment on table CRM_T_DELIVERY_LOGISTICS
  is '出货物流表';
-- Add comments to the columns 
comment on column CRM_T_DELIVERY_LOGISTICS.c_pid
  is '主键自增';
comment on column CRM_T_DELIVERY_LOGISTICS.c_logistics_code
  is '物流编号';
comment on column CRM_T_DELIVERY_LOGISTICS.c_delivery_code
  is '发货申请编号';
comment on column CRM_T_DELIVERY_LOGISTICS.c_logts_co_code
  is '物流公司编号';
comment on column CRM_T_DELIVERY_LOGISTICS.c_logts_co_name
  is '物流公司名称';
comment on column CRM_T_DELIVERY_LOGISTICS.c_transport_mode
  is '运输方式';
comment on column CRM_T_DELIVERY_LOGISTICS.c_logistics_no
  is '运单号';
comment on column CRM_T_DELIVERY_LOGISTICS.c_ship_name
  is '船名';
comment on column CRM_T_DELIVERY_LOGISTICS.dt_shipment
  is '船期';
comment on column CRM_T_DELIVERY_LOGISTICS.c_weight
  is '重量';
comment on column CRM_T_DELIVERY_LOGISTICS.c_volume
  is '体积';
comment on column CRM_T_DELIVERY_LOGISTICS.n_freight_internal
  is '国内运费';
comment on column CRM_T_DELIVERY_LOGISTICS.n_freight_external
  is '国际运费';
comment on column CRM_T_DELIVERY_LOGISTICS.dt_arrival
  is '计划到港日期';
comment on column CRM_T_DELIVERY_LOGISTICS.c_insurer
  is '保险公司';
comment on column CRM_T_DELIVERY_LOGISTICS.c_policy_no
  is '保单号';
comment on column CRM_T_DELIVERY_LOGISTICS.n_policy_fee
  is '保费';
comment on column CRM_T_DELIVERY_LOGISTICS.dt_tender
  is '投标有效期';
comment on column CRM_T_DELIVERY_LOGISTICS.c_policy_remarks
  is '保险说明';
comment on column CRM_T_DELIVERY_LOGISTICS.c_policy_status
  is '保险状态';
comment on column CRM_T_DELIVERY_LOGISTICS.c_customs_broker
  is '报关行';
comment on column CRM_T_DELIVERY_LOGISTICS.c_customs_contract
  is '报关合同';
comment on column CRM_T_DELIVERY_LOGISTICS.c_export_invoice
  is '出口发票';
comment on column CRM_T_DELIVERY_LOGISTICS.c_forex_verification
  is '外汇核销单';
comment on column CRM_T_DELIVERY_LOGISTICS.c_customs_date
  is '报关日期';
comment on column CRM_T_DELIVERY_LOGISTICS.c_customs_status
  is '报关状态';
-- Create/Recreate primary, unique and foreign key constraints 
alter table CRM_T_DELIVERY_LOGISTICS
  add constraint PK_M_T_DELIVERY_LOGISTICS primary key (C_PID)
  using index 
  tablespace TS_CRM
  pctfree 10
  initrans 2
  maxtrans 255;
