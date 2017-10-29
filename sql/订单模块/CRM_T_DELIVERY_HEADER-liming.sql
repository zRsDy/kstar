-- Create table
create table CRM_T_DELIVERY_HEADER
(
  c_pid                   VARCHAR2(32) not null,
  c_delivery_code         VARCHAR2(32) not null,
  dt_apply_date           DATE,
  c_status                VARCHAR2(32),
  c_business_entity       VARCHAR2(300),
  c_proposer_id           VARCHAR2(32),
  c_rece_customer_id      VARCHAR2(300),
  c_delivery_address      VARCHAR2(3000),
  c_consignee             VARCHAR2(300),
  c_consignee_tel         VARCHAR2(300),
  c_bill_customer_id      VARCHAR2(300),
  c_remarks               VARCHAR2(4000),
  c_rece_customer_name    VARCHAR2(300),
  c_bill_customer_name    VARCHAR2(300),
  c_proposer_name         VARCHAR2(300),
  c_delivery_address_id   VARCHAR2(32),
  c_created_by_id         VARCHAR2(100),
  dt_created_at           DATE,
  c_created_pos_id        VARCHAR2(100),
  c_created_org_id        VARCHAR2(100),
  c_updated_by_id         VARCHAR2(100),
  dt_updated_at           DATE,
  c_rece_customer_code    VARCHAR2(100),
  c_proposer_code         VARCHAR2(100),
  out_delivery_code       VARCHAR2(100),
  c_delete_flag           VARCHAR2(10),
  c_business_manager_id   VARCHAR2(32),
  c_business_manager_code VARCHAR2(300),
  c_business_manager_name VARCHAR2(300),
  c_sync_erp_log          VARCHAR2(3000)
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
comment on table CRM_T_DELIVERY_HEADER
  is '出货申请头表';
-- Add comments to the columns 
comment on column CRM_T_DELIVERY_HEADER.c_pid
  is '主键';
comment on column CRM_T_DELIVERY_HEADER.c_delivery_code
  is '出货申请头编号';
comment on column CRM_T_DELIVERY_HEADER.dt_apply_date
  is '申请日期';
comment on column CRM_T_DELIVERY_HEADER.c_stutas
  is '状态';
comment on column CRM_T_DELIVERY_HEADER.c_business_entity
  is '业务实体';
comment on column CRM_T_DELIVERY_HEADER.c_proposer
  is '申请人';
comment on column CRM_T_DELIVERY_HEADER.c_rece_custome
  is '收货客户';
comment on column CRM_T_DELIVERY_HEADER.c_delivery_address
  is '收货地点';
comment on column CRM_T_DELIVERY_HEADER.c_consignee
  is '收货人';
comment on column CRM_T_DELIVERY_HEADER.c_consignee_tel
  is '收货人电话';
comment on column CRM_T_DELIVERY_HEADER.c_rece_bill_customer
  is '收单客户';
comment on column CRM_T_DELIVERY_HEADER.c_remarks
  is '备注';
comment on column CRM_T_DELIVERY_HEADER.c_create_time
  is '创建日期';
comment on column CRM_T_DELIVERY_HEADER.c_creator
  is '创建人';
  comment on column CRM_T_DELIVERY_HEADER.c_delete_flag
  is '删除标志, 1.已删除，0 未删除';
comment on column CRM_T_DELIVERY_HEADER.c_business_manager_id
  is '商务专员ID';
comment on column CRM_T_DELIVERY_HEADER.c_business_manager_code
  is '商务专员编号';
comment on column CRM_T_DELIVERY_HEADER.c_business_manager_name
  is '商务专员名称';
comment on column CRM_T_DELIVERY_HEADER.c_sync_erp_log
  is '同步ERP错误消息';
-- Create/Recreate indexes 
create index DELIVERY_H_C_DELIVERY_CODE on CRM_T_DELIVERY_HEADER (C_DELIVERY_CODE)
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
alter table CRM_T_DELIVERY_HEADER
  add constraint PK_M_T_DELIVERY_HEADER primary key (C_PID)
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
