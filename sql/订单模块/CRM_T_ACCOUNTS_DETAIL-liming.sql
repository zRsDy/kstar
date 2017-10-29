-- Create table
create table CRM_T_ACCOUNTS_DETAIL
(
  c_pid            VARCHAR2(32) not null,
  c_accounts_code  VARCHAR2(32),
  c_bo_code        VARCHAR2(32),
  c_bo_name        VARCHAR2(300),
  c_order_code     VARCHAR2(32),
  dt_delivery_date DATE,
  c_materiel_code  VARCHAR2(300),
  c_materiel_name  VARCHAR2(300),
  n_price          NUMBER(16,4),
  n_amount         NUMBER(16,4),
  c_currency       VARCHAR2(32),
  n_qty            NUMBER(16),
  c_is_delivery    VARCHAR2(32),
  c_is_invoice     VARCHAR2(32),
  c_is_delay       VARCHAR2(32),
  n_payable_date   DATE,
  dt_delay_date    DATE,
  c_accounts_id    VARCHAR2(32),
  c_updated_by_id  VARCHAR2(32),
  dt_updated_at    DATE,
  n_is_delay       NUMBER(10),
  isdelaylable     VARCHAR2(255),
  n_is_delivery    NUMBER(10),
  n_is_invoice     NUMBER(10)
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
comment on table CRM_T_ACCOUNTS_DETAIL
  is '账期申请明细表';
-- Add comments to the columns 
comment on column CRM_T_ACCOUNTS_DETAIL.c_pid
  is '主键';
comment on column CRM_T_ACCOUNTS_DETAIL.c_accounts_code
  is '账期申请主表编号';
comment on column CRM_T_ACCOUNTS_DETAIL.c_bo_code
  is '商机编号';
comment on column CRM_T_ACCOUNTS_DETAIL.c_bo_name
  is '商机名称';
comment on column CRM_T_ACCOUNTS_DETAIL.c_order_code
  is '订单编号';
comment on column CRM_T_ACCOUNTS_DETAIL.dt_delivery_date
  is '发货日期';
comment on column CRM_T_ACCOUNTS_DETAIL.c_materiel_code
  is '物料编码';
comment on column CRM_T_ACCOUNTS_DETAIL.c_materiel_name
  is '物料名称';
comment on column CRM_T_ACCOUNTS_DETAIL.n_price
  is '单价';
comment on column CRM_T_ACCOUNTS_DETAIL.n_amount
  is '金额';
comment on column CRM_T_ACCOUNTS_DETAIL.c_currency
  is '币种';
comment on column CRM_T_ACCOUNTS_DETAIL.n_qty
  is '发货数量';
comment on column CRM_T_ACCOUNTS_DETAIL.c_is_delivery
  is '是否发货';
comment on column CRM_T_ACCOUNTS_DETAIL.c_is_invoice
  is '是否开票';
comment on column CRM_T_ACCOUNTS_DETAIL.c_is_delay
  is '是否逾期';
comment on column CRM_T_ACCOUNTS_DETAIL.n_payable_date
  is '应付日期';
comment on column CRM_T_ACCOUNTS_DETAIL.dt_delay_date
  is '延期至';
comment on column CRM_T_ACCOUNTS_DETAIL.c_accounts_id
  is '账期申请主表ID';
comment on column CRM_T_ACCOUNTS_DETAIL.c_updated_by_id
  is '更新者';
comment on column CRM_T_ACCOUNTS_DETAIL.dt_updated_at
  is '更新时间';
-- Create/Recreate primary, unique and foreign key constraints 
alter table CRM_T_ACCOUNTS_DETAIL
  add constraint PK_M_T_ACCOUNTS_DETAIL primary key (C_PID)
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
